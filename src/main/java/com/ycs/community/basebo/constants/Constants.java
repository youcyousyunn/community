package com.ycs.community.basebo.constants;

public class Constants {
    public static final String BANNER_TXT = "                                                                   \n" +
            " _   _  ___  _   _  ___ _   _  ___  _   _ ___ _   _ _   _ _ __  _ __  \n" +
            "| | | |/ _ \\| | | |/ __| | | |/ _ \\| | | / __| | | | | | | '_ \\| '_ \\ \n" +
            "| |_| | (_) | |_| | (__| |_| | (_) | |_| \\__ \\ |_| | |_| | | | | | | |\n" +
            " \\__, |\\___/ \\__,_|\\___|\\__, |\\___/ \\__,_|___/\\__, |\\__,_|_| |_|_| |_|\n" +
            " |___/                  |___/                 |___/ ";

    /** 授权令牌 */
    public static final String AUTH_TOKEN = "Authorization";
    /** 登录验证码前缀 */
    public static final String LOGIN_CAPTCHA_PREFIX = "loginVCode";
    /** 默认当前页 */
    public static final int DEFAULT_CURRENT_PAGE = 1;
    /** 页大小 */
    public static final int DEFAULT_PAGE_SIZE = 10;
    /** 默认总条数 */
    public static final int DEFAULT_TOTAL_COUNT = 0;
    /** 默认总页数 */
    public static final int DEFAULT_TOTAL_PAGE = 0;
    /** 默认起始行数 */
    public static final int DEFAULT_START_ROW = 0;
    /** 京东搜索索引 */
    public static final String JD_INDEX = "jd";
    /** 京东搜索类型 */
    public static final String JD_TYPE = "goods";

    /**待提交**/
    public static final int SUBMITTED_STATE = 0;
    /**审核中**/
    public static final int REVIEW_STATE = 1;
    /**已废弃**/
    public static final int OBSOLETE_STATE = 2;
    /**已完成**/
    public static final int COMPLETED_STATE = 3;

    /**审批通过**/
    public static final String APPROVAL_AGREE = "agree";
    /**审批驳回**/
    public static final String APPROVAL_REJECT = "reject";
}