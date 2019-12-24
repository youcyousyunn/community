package com.ycs.community.spring.security.service.impl;

import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.fastjson.JSONObject;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.coobo.utils.FileUtil;
import com.ycs.community.spring.security.domain.po.OnlineUserPo;
import com.ycs.community.spring.security.service.OnlineUserService;
import com.ycs.community.sysbo.dao.DeptDao;
import com.ycs.community.sysbo.dao.JobDao;
import com.ycs.community.sysbo.domain.dto.QryOnlineUserPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryOnlineUserPageResponseDto;
import com.ycs.community.sysbo.domain.po.DeptPo;
import com.ycs.community.sysbo.domain.po.JobPo;
import com.ycs.community.sysbo.domain.po.UserPo;
import com.ycs.community.sysbo.utils.EncryptUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OnlineUserServiceImpl implements OnlineUserService {
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.online.key}")
    private String onlineKey;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private JobDao jobDao;

    /**
     * 保存在线用户信息
     * @param userPo
     * @param token
     * @param request
     */
    @Override
    public boolean saveOnlineUserInfo(UserPo userPo, String token, HttpServletRequest request) {
        // 查询用户对应部门
        DeptPo dept = deptDao.qryDeptByUserId(userPo.getId());
        // 查询用户对应岗位
        JobPo job = jobDao.qryJobByUserId(userPo.getId());

        String ip = getIP(request);
        String browser = getBrowser(request);
        String address = getAddressByIp(ip);
        OnlineUserPo onlineUserPo = new OnlineUserPo();
        onlineUserPo.setKey(EncryptUtil.desEncrypt(token));
        onlineUserPo.setAccountId(userPo.getAccountId());
        onlineUserPo.setName(userPo.getName());
        if (!StringUtils.isEmpty(dept) && !StringUtils.isEmpty(job)) {
            onlineUserPo.setJob(dept.getName() + "/" + job.getName());
        }
        onlineUserPo.setRequestIp(ip);
        onlineUserPo.setBrowser(browser);
        onlineUserPo.setAddress(address);
        onlineUserPo.setLoginTm(new Date());
        redisTemplate.opsForValue().set(onlineKey + token, JSONObject.toJSONString(onlineUserPo));
        redisTemplate.expire(onlineKey + token, expiration, TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public QryOnlineUserPageResponseDto qryOnlinePage(QryOnlineUserPageRequestDto request) {
        List<OnlineUserPo> data = new ArrayList<>();
        List<String> keys = new ArrayList<>(redisTemplate.keys(onlineKey + "*"));
        keys.forEach(key -> {
            OnlineUserPo onlineUserPo = JSONObject.parseObject(redisTemplate.opsForValue().get(key).toString(), OnlineUserPo.class);
            if (!StringUtils.isEmpty(request.getName())) {
                if (onlineUserPo.toString().contains(request.getName())) {
                    data.add(onlineUserPo);
                }
            } else {
                data.add(onlineUserPo);
            }
        });

        // 按时间倒序
        Collections.sort(data, (o1, o2) -> {
            return o2.getLoginTm().compareTo(o1.getLoginTm());
        });

        // 查询总条数
        int totalCount = data.size();
        // 计算分页信息
        List<OnlineUserPo> page = PageUtil.toPage(request.getCurrentPage(), request.getPageSize(), data);
        // 组装分页信息
        QryOnlineUserPageResponseDto response = new QryOnlineUserPageResponseDto();
        if (!CollectionUtils.isEmpty(page)) {
            response.setData(page);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }

    @Override
    public boolean kickOut(String key) {
        return redisTemplate.delete(onlineKey + EncryptUtil.desDecrypt(key));
    }

    /**
     * 注销登录
     * @param token
     */
    @Override
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