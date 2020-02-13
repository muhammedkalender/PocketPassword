package com.muhammedkalender.pocketpassword.Pages;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Adapters.PasswordAdapter;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.R;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends PageAbstract implements PageInterface {
    private RecyclerView rvPasswordList;
    private PasswordAdapter adapterPassword;
    private RecyclerView.LayoutManager rvlmPasswordList;

    public List<PasswordModel> dsPasswords;

    @Override
    public void initialize(View viewRoot) {
        if (isInitialized()){
            return;
        }

        try{
           this.viewRoot = viewRoot;

            dsPasswords = new ArrayList<>();
            dsPasswords.add(new PasswordModel("MB", "ASDSAD", "#FFFFFF"));
            dsPasswords.add(new PasswordModel("ZZ", "HASH ZZ", "#CCCCCC"));

            rvPasswordList = viewRoot.findViewById(R.id.rvPasswordList);
            rvPasswordList.setHasFixedSize(true);

            rvlmPasswordList = new LinearLayoutManager(Global.CONTEXT);
            rvPasswordList.setLayoutManager(rvlmPasswordList);

            adapterPassword = new PasswordAdapter(dsPasswords, Global.CONTEXT);
            rvPasswordList.setAdapter(adapterPassword);

            this.initialized = true;

            PasswordModel passwordModel = new PasswordModel();

            dsPasswords.addAll(passwordModel.selectActive());
        }catch (Exception e){
            this.initialized = false;

            Helpers.logger.error(ErrorCodeConstants.HOME_PAGE_INITIALIZE, e);
        }
    }

    @Override
    public View getView() {
        return this.viewRoot;
    }
}
