package com.muhammedkalender.pocketpassword.Globals;

import com.muhammedkalender.pocketpassword.Components.LoadingComponent;
import com.muhammedkalender.pocketpassword.Helpers.AESHelper;
import com.muhammedkalender.pocketpassword.Helpers.DatabaseHelper;
import com.muhammedkalender.pocketpassword.Helpers.ListHelper;
import com.muhammedkalender.pocketpassword.Helpers.LogHelpers;
import com.muhammedkalender.pocketpassword.Helpers.ResourceHelper;
import com.muhammedkalender.pocketpassword.Helpers.SharedPreferencesHelper;
import com.muhammedkalender.pocketpassword.Helpers.ValidationHelper;

public class Helpers {
    public static LogHelpers logger;
    public static ResourceHelper resource;
    public static DatabaseHelper database;
    public static LoadingComponent loading;
    public static ListHelper list;
    public static SharedPreferencesHelper config;
    public static ValidationHelper validation;
    public static AESHelper aes;
}
