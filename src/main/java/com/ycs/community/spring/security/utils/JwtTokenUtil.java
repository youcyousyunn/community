package com.ycs.community.spring.security.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.ycs.community.spring.property.SecurityProperties;
import com.ycs.community.sysbo.domain.po.UserPo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    private final long serialVersionUID = 7819721266984186335L;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private RedisTemplate redisTemplate;
    private Clock clock = DefaultClock.INSTANCE;


    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(securityProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        // 过期时间为空，则说明设置了永不过期
        if(expiration == null) {
            return false;
        }
        return expiration.before(clock.now());
    }

    private boolean isCreatedBeforeLastPasswordReset(Date created, Long lastPasswordReset) {
        return (lastPasswordReset != null && created.before(new Date(lastPasswordReset)));
    }

    private boolean ignoreTokenExpiration(String token) {
        return false;
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + securityProperties.getExpiration());
    }

    /**
     * 生成令牌
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * 创建token，设置永不过期
     * 过期时间转交给Redis进行维护
     * @param claims
     * @param subject
     * @return
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
//                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, securityProperties.getSecret())
                .compact();
    }

    /**
     * 获取令牌
     * @param request
     * @return
     */
    public String getToken(HttpServletRequest request) {
        final String token = request.getHeader(securityProperties.getHeader());
        return token;
    }

    /**
     * 验证令牌是否过期
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        UserPo userPo = (UserPo) userDetails;
        final Date created = getIssuedAtDateFromToken(token);
        // 如果token存在，且token创建日期 > 最后修改密码的日期 则代表token有效
        return (!isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, userPo.getLastPasswordResetTime())
        );
    }

    /**
     * 检查令牌是否需要续期，满足条件则续期
     * @param token
     */
    public void checkRenewal(String token) {
        // 获取令牌过期时间
        long expireTime = redisTemplate.getExpire(securityProperties.getOnlineKey() + token, TimeUnit.MILLISECONDS);
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) expireTime);
        // 判断当前时间与过期时间的时间差
        long diff = expireDate.getTime() - System.currentTimeMillis();
        // 如果在续期检查的范围内，则续期
        if (diff <= securityProperties.getDetect()) {
            long renewTime = expireTime + securityProperties.getRenew();
            redisTemplate.expire(securityProperties.getOnlineKey() + token, renewTime, TimeUnit.MILLISECONDS);
        }
    }
}

