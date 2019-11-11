package com.ycs.community.sysbo.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtil {

    /**
     * 获取异常堆栈信息
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        try (PrintWriter pw = new PrintWriter(stringWriter)) {
            throwable.printStackTrace(pw);
            return stringWriter.toString();
        }
    }
}
