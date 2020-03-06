package com.muhammedkalender.pocketpassword;

import android.app.Activity;
import android.content.Context;
import android.service.autofill.Dataset;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.muhammedkalender.pocketpassword.Adapters.PasswordAdapter;
import com.muhammedkalender.pocketpassword.Models.CategoryModel;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.Pages.HomePage;
import com.muhammedkalender.pocketpassword.Pages.Password.PasswordPage;
import com.muhammedkalender.pocketpassword.Pages.SettingsPage;
import com.muhammedkalender.pocketpassword.ui.main.SectionsPagerAdapter;

import java.util.List;

public class Global {
    //region Basic

    public static Context CONTEXT = null;

    //endregion

    public static TabLayout TAB_LAYOUT = null;
    public static ViewPager VIEW_PAGER = null;
    public static int CURRENT_PASSWORD_MODEL_INDEX = 0;
    public static SectionsPagerAdapter SECTION_PAGER_ADAPTER = null;
    public static Dataset PASSWORDS = null;
    public static List<PasswordModel> LIST_PASSWORDS_SOLID = null;
    public static List<PasswordModel> LIST_PASSWORDS = null;
    public static PasswordAdapter PASSWORD_ADAPTER = null;
    public static ViewGroup VIEW_GROUP = null;
    public static Activity ACTIVITY = null;
    public static List<CategoryModel> LIST_CATEGORY = null;
    public static ArrayAdapter<String> ADAPTER_CATEGORY = null;

    //region Pages

    //region Home

    public static HomePage PAGE_HOME = null;

    public static HomePage getPageHome(View root){
        if(PAGE_HOME != null && PAGE_HOME.isInitialized()){
            return PAGE_HOME;
        }else{
            if(PAGE_HOME == null){
                PAGE_HOME = new HomePage();
                PAGE_HOME.initialize(root);

                return getPageHome(root);
            }

            return null;
        }
    }

    //endregion

    //region Settings

    public static SettingsPage PAGE_SETTINGS = null;

    //endregion

    //region Password

    public static PasswordPage PAGE_PASSWORD = null;

    public static PasswordPage getPagePassword(View root){
        return Global.getPagePassword(root, null);
    }

    public static PasswordPage getPagePassword(View root, PasswordModel passwordModel){
        if(PAGE_PASSWORD != null && PAGE_PASSWORD.isInitialized()){
            PAGE_PASSWORD.load(passwordModel);

            return PAGE_PASSWORD;
        }else{
            if(PAGE_PASSWORD == null){
                PAGE_PASSWORD = new PasswordPage();
                PAGE_PASSWORD.initialize(root);

                return PAGE_PASSWORD;
            }

            return null;
        }
    }

    //endregion

    //endregion

    public static boolean LOCK_PASSWORD_PAGE = true;

    public static String PASSWORD = null;

    public static int SELECTED_CATEGORY = 0;
    public static int SELECTED_CATEGORY_INDEX = 0;


    public static int SELECTED_PASSWORD_ID = -1;
}
