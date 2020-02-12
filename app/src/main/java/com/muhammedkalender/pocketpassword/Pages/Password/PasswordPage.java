package com.muhammedkalender.pocketpassword.Pages.Password;

import android.view.View;

import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;

public class PasswordPage extends PageAbstract implements PageInterface{

    @Override
    public void initialize(View viewRoot) {
        this.initialized = true;

        this.viewRoot = viewRoot;

        //todo
    }

    @Override
    public View getView() {
        return this.viewRoot;
    }
}
