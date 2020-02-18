package com.muhammedkalender.pocketpassword.Constants;

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

    //region Resource

    public static final int
            RESOURCE_DIMEN = 300,
            RESOURCE_INT = 301,
            RESOURCE_STRING = 302,
            RESOURCE_DIMEN_TO_INTEGER = 303,
            RESOURCE_COLOR = 304;

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
            CLIPBOARD_PASSWORD_IN_LIST = 1001;

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
}
