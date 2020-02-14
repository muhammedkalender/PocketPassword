package com.muhammedkalender.pocketpassword.Pages.Password;

import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;
import com.muhammedkalender.pocketpassword.R;

public class NewPasswordPage extends PageAbstract implements PageInterface {

    public MaterialButton btnAdd = null;

    public TextInputLayout tilName = null;
    public TextInputEditText etName = null;

    public TextInputLayout tilPassword = null;
    public TextInputEditText etPassword = null;

    @Override
    public void initialize(View viewRoot) {
        //todo

        this.viewRoot = viewRoot;
        this.initialized = true;

        this.tilName = this.viewRoot.findViewById(R.id.tilName);
        this.etName = this.viewRoot.findViewById(R.id.etName);
        this.tilPassword = this.viewRoot.findViewById(R.id.tilPassword);
        this.etPassword = this.viewRoot.findViewById(R.id.etPassword);
        this.btnAdd = this.viewRoot.findViewById(R.id.btnAdd);

        final PasswordModel passwordModel = new PasswordModel();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.loading.show();

                String name = etName.getText().toString();
                String password = etPassword.getText().toString();

                passwordModel.setName(name);
                passwordModel.setPassword(password);
                passwordModel.setColor("");

                ResultObject validation = passwordModel.validation();

                if (etName.getText().toString() == null || etName.getText().toString().length() == 0) {
                    tilName.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.input_name)));

                    Helpers.loading.hide();

                    return;
                } else {
                    if (name.length() > Helpers.resource.getInt(R.integer.name_max_length)) {
                        tilName.setError(Helpers.resource.getString(R.string.max_length, "", Helpers.resource.getString(R.string.input_name), Helpers.resource.getInt(R.integer.name_max_length)));

                        Helpers.loading.hide();

                        return;
                    } else if (name.length() < Helpers.resource.getInt(R.integer.name_min_length)) {
                        tilName.setError(Helpers.resource.getString(R.string.min_length, "", Helpers.resource.getString(R.string.input_name), Helpers.resource.getInt(R.integer.name_min_length)));

                        Helpers.loading.hide();

                        return;
                    } else if (passwordModel.checkDuplicate(name)) {
                        tilName.setError(Helpers.resource.getString(R.string.already_used, "", name));

                        Helpers.loading.hide();

                        return;
                    } else {
                        tilName.setErrorEnabled(false);
                    }
                }

                if (etPassword.getText().toString() == null || etPassword.getText().toString().length() == 0) {
                    tilPassword.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.input_password)));

                    Helpers.loading.hide();

                    return;
                } else {
                    if (password.length() > Helpers.resource.getInt(R.integer.password_max_length)) {
                        tilPassword.setError(Helpers.resource.getString(R.string.max_length, "", Helpers.resource.getString(R.string.input_password)));

                        Helpers.loading.hide();

                        return;
                    } else if (password.length() < Helpers.resource.getInt(R.integer.password_min_length)) {
                        tilPassword.setError(Helpers.resource.getString(R.string.min_length, "", Helpers.resource.getString(R.string.input_password)));

                        Helpers.loading.hide();

                        return;
                    } else {
                        tilPassword.setErrorEnabled(false);
                    }
                }

                //todo color
                PasswordModel passwordModel = new PasswordModel(name, password, "");

                ResultObject insert = passwordModel.insert();

                if (insert.isSuccess()) {
                    Global.TAB_LAYOUT.getTabAt(Config.TAB_HOME_INDEX).select();

                    //todo get synced data
                    //Global.SECTION_PAGER_ADAPTER.add(name);
                    Global.LIST_PASSWORDS.add(new PasswordModel(name, password, ""));
                } else {
                    //todo
                }

                Helpers.loading.hide();
            }
        });
    }

    @Override
    public View getView() {
        return this.viewRoot;
    }
}
