package com.ycs.community.spring.security.service;

import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.fastjson.JSONObject;
import com.ycs.community.cmmbo.domain.po.UserPo;
import com.ycs.community.coobo.utils.FileUtil;
import com.ycs.community.spring.security.domain.po.OnlineUserPo;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Service
public class OnlineUserService {
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.online.key}")
    private String onlineKey;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 保存在线用户信息
     * @param userPo
     * @param token
     * @param request
     */
    public void saveOnlineUserInfo(UserPo userPo, String token, HttpServletRequest request) {
        String ip = getIP(request);
        String browser = getBrowser(request);
        String address = getAddressByIp(ip);
        OnlineUserPo onlineUserPo = new OnlineUserPo();
        onlineUserPo.setName(userPo.getName());
        onlineUserPo.setRequestIp(ip);
        onlineUserPo.setBrowser(browser);
        onlineUserPo.setAddress(address);
        redisTemplate.opsForValue().set(onlineKey + token, JSONObject.toJSONString(onlineUserPo));
        redisTemplate.expire(onlineKey + token, expiration, TimeUnit.MILLISECONDS);
    }

    /**
     * 注销登录
     * @param token
     */
    public boolean logout(String token) {
        String key = onlineKey + token;
        return redisTemplate.delete(key);
    }

    /**
     * 获取IP
     * @param request
     * @return
     */
    private String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        if  ("127.0.0.1".equals(ip))  {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 获取浏览器
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request){
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * 根据ip获取地址
     * @param ip
     * @return
     */
    public static String getAddressByIp(String ip) {
        try {
            String path = "ip2region/ip2region.db";
            String name = "ip2region.db";
            DbConfig config = new DbConfig();
            File file = FileUtil.inputStreamToFile(new ClassPathResource(path).getStream(), name);
            DbSearcher searcher = new DbSearcher(config, file.getPath());
            Method method;
            method = searcher.getClass().getMethod("btreeSearch", String.class);
            DataBlock dataBlock;
            dataBlock = (DataBlock) method.invoke(searcher, ip);
            String address = dataBlock.getRegion().replace("0|","");
            if(address.charAt(address.length()-1) == '|'){
                address = address.substring(0,address.length() - 1);
            }
            return address.equals("内网IP|内网IP") ? "内网IP" : address;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}