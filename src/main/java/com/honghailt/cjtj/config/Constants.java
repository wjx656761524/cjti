package com.honghailt.cjtj.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";

    public static final String MONGO_TEMPLATE_CJTJ = "cjtjMongoTemplate";

    public static final String LOGIN_TYPE_FORM_ATTR_NAME = "loginType";

    /** TOP默认时间格式 **/
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    private Constants() {
    }
}
