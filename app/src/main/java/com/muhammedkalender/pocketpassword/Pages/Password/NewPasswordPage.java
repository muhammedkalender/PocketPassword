package com.muhammedkalender.pocketpassword.Pages.Password;

import android.view.View;

import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;

public class NewPasswordPage  extends PageAbstract implements PageInterface {

    @Override
    public void initialize(View viewRoot) {
        //todo

        this.viewRoot = viewRoot;
        this.initialized = true;
    }

    @Override
    public View getView() {
        return this.viewRoot;
    }
}
