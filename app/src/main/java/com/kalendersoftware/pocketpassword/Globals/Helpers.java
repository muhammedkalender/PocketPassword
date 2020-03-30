package com.kalendersoftware.pocketpassword.Globals;

import com.kalendersoftware.pocketpassword.Components.LoadingComponent;
import com.kalendersoftware.pocketpassword.Global;
import com.kalendersoftware.pocketpassword.Helpers.AESHelper;
import com.kalendersoftware.pocketpassword.Helpers.CryptHelper;
import com.kalendersoftware.pocketpassword.Helpers.DatabaseHelper;
import com.kalendersoftware.pocketpassword.Helpers.ListHelper;
import com.kalendersoftware.pocketpassword.Helpers.LogHelpers;
import com.kalendersoftware.pocketpassword.Helpers.ResourceHelper;
import com.kalendersoftware.pocketpassword.Helpers.SharedPreferencesHelper;
import com.kalendersoftware.pocketpassword.Helpers.SystemHelper;
import com.kalendersoftware.pocketpassword.Helpers.ValidationHelper;

public class Helpers {
    public static LogHelpers logger;
    public static ResourceHelper resource;
    public static DatabaseHelper database;
    public static LoadingComponent loading;
    public static ListHelper list;
    public static SharedPreferencesHelper config;
    public static ValidationHelper validation;
    public static AESHelper aes;
    public static CryptHelper crypt;
    public static SystemHelper system;

    public static void updateLastOperationTime(){
        Global.LAST_OPERATION_TIME = System.currentTimeMillis();
    }
}
