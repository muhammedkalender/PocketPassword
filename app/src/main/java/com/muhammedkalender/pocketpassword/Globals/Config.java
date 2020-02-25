package com.muhammedkalender.pocketpassword.Globals;

public class Config {
    public static final boolean LOG_ENABLE = true;

    public static final int TAB_SETTINGS_INDEX = 0;
    public static final int TAB_ADD_INDEX = 1;
    public static final int TAB_HOME_INDEX = 2;
    public static final int TAB_PASSWORD_INDEX = 3;

    public static final boolean LOG_SQL_EXECUTE = true;

    public static final int DEFAULT_INTEGER = 0;
    public static final String DEFAULT_STRING = "";
    public static final long DEFAULT_LONG = 0;
    public static final boolean DEFAULT_BOOLEAN = false;
    public static final float DEFAULT_FLOAT = 0;
    public static final double DEFAULT_DOUBLE = 0;

    public static final int RSA_KEY_SIZE = 4096; //TODO

    public static boolean CONFIG_ONLY_LOGIN = true;
    public static boolean CONFIG_HIDE_VIEW = true;

    public static void initConfig(){
        CONFIG_ONLY_LOGIN = Helpers.config.getBoolean("only_login", CONFIG_ONLY_LOGIN);
        CONFIG_HIDE_VIEW = Helpers.config.getBoolean("hide_view", CONFIG_HIDE_VIEW);
    }
}
