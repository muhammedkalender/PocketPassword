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
}
