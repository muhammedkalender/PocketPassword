package com.kalendersoftware.pocketpassword.Globals;

public class Config {
    public static final int TAB_COMPANY = 0;
    public static final int TAB_SETTINGS_INDEX = 1;
    public static final int TAB_ADD_INDEX = 2;
    public static final int TAB_HOME_INDEX = 3;
    public static final int TAB_PASSWORD_INDEX = 4;

    public static final int SHORT_NAME_LENGTH = 16;

    public static final boolean LOG_SQL_EXECUTE = true;

    public static final int DEFAULT_INTEGER = 0;
    public static final String DEFAULT_STRING = "";
    public static final long DEFAULT_LONG = 0;
    public static final boolean DEFAULT_BOOLEAN = false;
    public static final float DEFAULT_FLOAT = 0;
    public static final double DEFAULT_DOUBLE = 0;

    public static final int RSA_KEY_SIZE = 4096;

    public static final String EXPORT_CONFIRM_TEXT = "kKXBmMyR%>}345gaOP._-8dWN7fX";

    public static boolean CONFIG_ONLY_LOGIN = false;
    public static boolean CONFIG_HIDE_VIEW = false;

    public static boolean CONFIG_ENABLE_INFO_LOG= false;
    public static boolean CONFIG_ENABLE_ERROR_LOG = true;
    public static boolean CONFIG_ENABLE_VAR_LOG = false;

    public static int LOADING_SHOW_DELAY = 300;

    public static int TIMEOUT_SECOND = 300;

    public static final int DELAY_TIME_OUT = 15;

    public static void initConfig(){
        CONFIG_ONLY_LOGIN = Helpers.config.getBoolean("only_login", CONFIG_ONLY_LOGIN);
        CONFIG_HIDE_VIEW = Helpers.config.getBoolean("hide_view", CONFIG_HIDE_VIEW);
        CONFIG_ENABLE_ERROR_LOG = Helpers.config.getBoolean("enable_error_log", CONFIG_ENABLE_ERROR_LOG);
        CONFIG_ENABLE_INFO_LOG = Helpers.config.getBoolean("enable_info_log", CONFIG_ENABLE_INFO_LOG);
    }
}
