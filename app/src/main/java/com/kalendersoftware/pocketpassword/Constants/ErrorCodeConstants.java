package com.kalendersoftware.pocketpassword.Constants;

public class ErrorCodeConstants {
    //region SQL

    public static final int
            SQL_INITIALIZE = 5,
            SQL_EXECUTE = 100,
            SQL_SELECT = 101,
            SQL_SELECT_WITH_DATA = 102,
            SQL_SELECT_WITH_DATA_EMPTY = 103,
            SQL_IS_AVAILABLE = 104,
            SQL_SELECT_SINGLE_COLUMN = 105,
            SQL_SELECT_SINGLE_COLUMN_EMPTY = 106;

    //endregion

    //region Page

    public static final int HOME_PAGE_INITIALIZE = 200;

    //endregion

    //region Home Page

    public static final int
            HOME_PAGE_LISTENER_EULA = 8700,
            HOME_PAGE_LISTENER_FORGOT_PASSWORD = 8701,
            HOME_PAGE_LISTENER_CONTACT_US = 6702;

    //endregion

    //region Resource

    public static final int
            RESOURCE_DIMEN = 300,
            RESOURCE_INT = 301,
            RESOURCE_STRING = 302,
            RESOURCE_DIMEN_TO_INTEGER = 303,
            RESOURCE_COLOR = 304,
            RESOURCE_DRAWABLE = 305;

    //endregion

    //region Model Password

    public static final int
            MODEL_PASSWORD_INSERT = 600;

    //endregion

    //region Model Valid

    public static final int
            MODEL_VALID = 700;

    //endregion

    //region Loading

    public static final int
            LOADING_SHOW = 800,
            LOADING_SHOW_WITH_RESOURCE = 801,
            LOADING_SHOW_WITH_MESSAGE = 802,
            LOADING_HIDE = 803;

    //endregion

    //region Clipboard

    public static final int
            CLIPBOARD_PASSWORD = 1000,
            CLIPBOARD_PASSWORD_IN_LIST = 1001,
            CLIPBOARD_ACCOUNT = 1002,
            CLIPBOARD_ACCOUNT_COPY = 1003,
            CLIPBOARD_MANAGER_THREAD = 1100,
            CLIPBOARD_MANAGER_MESSAGE = 1101;

    //endregion

    //region Shared Preferences

    public static final int
            SHARED_PREFERENCES_INTEGER_WRITE = 2000,
            SHARED_PREFERENCES_STRING_WRITE = 2001,
            SHARED_PREFERENCES_BOOLEAN_WRITE = 2002,
            SHARED_PREFERENCES_FLOAT_WRITE = 2003,
            SHARED_PREFERENCES_DOUBLE_WRITE = 2004,
            SHARED_PREFERENCES_LONG_WRITE = 2005,
            SHARED_PREFERENCES_INTEGER_READ = 2010,
            SHARED_PREFERENCES_STRING_READ = 2011,
            SHARED_PREFERENCES_BOOLEAN_READ = 2012,
            SHARED_PREFERENCES_FLOAT_READ = 2013,
            SHARED_PREFERENCES_DOUBLE_READ = 2014,
            SHARED_PREFERENCES_LONG_READ = 2015;

    //endregion

    //region Crypt

    public static final int
            CRYPT_GENERATE_KEY = 3000,
            PUBLIC_KEY_FROM_STRING = 3001,
            PRIVATE_KEY_FROM_STRING = 3002,
            CRYPT_KEY_TO_STRING = 3003,
            CRYPT_ENCRYPT_WITH_BASE64 = 3004,
            CRYPT_ENCRYPT = 3005,
            CRYPT_DECRYPT_WITH_BASE64 = 3006,
            CRYPT_DECRYPT = 3007,
            CRYPT_QUICK_DECRYPT = 3008,
            CRYPT_QUICK_DECRYPT_WITH_BASE64 = 3009,
            CRYPT_QUICK_ENCRYPT = 3010,
            CRYPT_QUICK_ENCRYPT_WITH_BASE64 = 3011;

    //endregion

    //region AES

    public static final int
            AES_DECRYPT = 3100,
            AES_ENCRYPT = 3101,
            AES_SECRET_TO_KEY = 3102;

    //endregion

    //region System Service

    public static final int
            SYSTEM_SERVICE_HIDE_SOFT_KEYBOARD = 4000;

    //endregion

    //region Logger

    public static final int
            LOGGER_INFO_WITH_MESSAGE = 5000,
            LOGGER_VAR = 5001;

    //endregion

    //region Register

    public static final int
            REGISTER_RESULT_RSA = 6000,
            REGISTER_RESULT_AES = 6001;

    //endregion

    //region Settings

    public static final int
            SETTINGS_SEND_ERROR_LOG = 6500,
            SETTINGS_CHANGE_PASSWORD = 6051,
            SETTINGS_CHANGE_PASSWORD_ON_THREAD = 6052;

    //endregion

    //region Result As X

    public static final int
            RESULT_AS_STRING = 7000,
            RESULT_AS_INTEGER = 7001,
            RESULT_AS_BOOLEAN = 7002;

    //endregion

    //region Backup

    public static final int
            BACKUP_EXPORT = 8000,
            BACKUP_IMPORT = 8100,
            IMPORT_BACKUP_OAR = 8101,
            EXPORT_BACKUP_OAR = 8102;

    //endregion

    //region Color Picker Component

    public static final int COLOR_PICKED_RANDOM_COLOR = 8300;

    //endregion

    //region Initialize Secondary Components

    public static final int INIT_SECONDARY_COMPONENTS = 8500;

    //endregion


    //region Company Page

    public static final int COMPANY_PAGE_LISTENER = 8700;

    //endregion

    //region Worker

    public static final int WORKER_TIME_OUT_ACTION = 7000;

    //endregion
}
