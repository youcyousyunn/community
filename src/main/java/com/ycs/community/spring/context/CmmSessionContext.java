package com.ycs.community.spring.context;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CmmSessionContext {
    private static CmmSessionContext instance;
    private Map<String, Object> sessionMap = new ConcurrentHashMap<>();

    static {
        instance = new CmmSessionContext();
    }

    public static CmmSessionContext getInstance () {
        if (StringUtils.isEmpty(instance)) {
            synchronized (CmmSessionContext.class) {
                if (StringUtils.isEmpty(instance)) {
                    instance = new CmmSessionContext();
                }
            }
        }

        return instance;
    }

    public void addSession (HttpSession session) {
        if (!StringUtils.isEmpty(session)) {
            sessionMap.put(session.getId(), session);
        }
    }

    public void delSession (HttpSession session) {
        if (!StringUtils.isEmpty(session)) {
            sessionMap.remove(session.getId());
        }
    }

    public HttpSession getSession (String sessionId) {
        if (!StringUtils.isEmpty(sessionId)) {
            return (HttpSession) sessionMap.get(sessionId);
        }
        return null;
    }
}
