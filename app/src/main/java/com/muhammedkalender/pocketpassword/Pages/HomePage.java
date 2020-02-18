package com.muhammedkalender.pocketpassword.Pages;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import java.util.logging.Logger;

public class HomePage extends PageAbstract implements PageInterface {
    private RecyclerView rvPasswordList;
    private RecyclerView.LayoutManager rvlmPasswordList;
    private TextInputLayout tilSearch;
    private TextInputEditText etSearch;

    @Override
    public void initialize(View viewRoot) {
        if (isInitialized()) {
            return;
        }

        try {
            this.viewRoot = viewRoot;

            Global.LIST_PASSWORDS = new ArrayList<>();
            Global.LIST_PASSWORDS_SOLID = new ArrayList<>();

            rvPasswordList = viewRoot.findViewById(R.id.rvPasswordList);
            rvPasswordList.setHasFixedSize(true);

            rvlmPasswordList = new LinearLayoutManager(Global.CONTEXT);
            rvPasswordList.setLayoutManager(rvlmPasswordList);

            tilSearch = viewRoot.findViewById(R.id.tilSearch);

            etSearch = viewRoot.findViewById(R.id.etSearch);

            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    filter(s.toString());
                }
            });

            Global.PASSWORD_ADAPTER = new PasswordAdapter(Global.CONTEXT);
            rvPasswordList.setAdapter(Global.PASSWORD_ADAPTER);

            this.initialized = true;

            PasswordModel passwordModel = new PasswordModel();
            List<PasswordModel> list = passwordModel.selectActive();

            Global.LIST_PASSWORDS.addAll(list);
            Global.LIST_PASSWORDS_SOLID.addAll(list);

            Helpers.logger.info(String.format("Listeler hazırlandı, Listede %1$d geçicide %2$d adet, kalıcıda %3$d adet kayıt var",list.size(),  Global.LIST_PASSWORDS.size(), Global.LIST_PASSWORDS_SOLID.size()));
        } catch (Exception e) {
            this.initialized = false;

            Helpers.logger.error(ErrorCodeConstants.HOME_PAGE_INITIALIZE, e);
        }
    }

    public void filter(String keyword){
        Helpers.logger.info(keyword + " Arandı");
        List<PasswordModel> tempPasswords = new ArrayList<>();

        for(int i = 0; i < Global.LIST_PASSWORDS_SOLID.size(); i++){
            if(Global.LIST_PASSWORDS_SOLID.get(i).getName().toLowerCase().contains(keyword.toLowerCase())){
                tempPasswords.add(Global.LIST_PASSWORDS_SOLID.get(i));
            }
        }

        Global.LIST_PASSWORDS.clear();
        Global.LIST_PASSWORDS.addAll(tempPasswords);
        Global.PASSWORD_ADAPTER.notifyDataSetChanged();
    }

    @Override
    public View getView() {
        return this.viewRoot;
    }
}
