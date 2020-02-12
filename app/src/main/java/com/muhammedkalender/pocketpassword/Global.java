package com.muhammedkalender.pocketpassword;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.Pages.HomePage;

public class Global {
    //region Basic

    public static Context CONTEXT = null;

    //endregion

    public static TabLayout TAB_LAYOUT = null;
    public static ViewPager VIEW_PAGER = null;
    public static PasswordModel CURRENT_PASSWORD_MODEL = null;

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

    //endregion
}
