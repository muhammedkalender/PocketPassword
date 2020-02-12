package com.muhammedkalender.pocketpassword.Pages;

import android.view.View;

import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;

public class PasswordPage extends PageAbstract implements PageInterface{

    @Override
    public void initialize(View viewRoot) {
        
    }

    @Override
    public View getView() {
        return this.viewRoot;
    }
}
