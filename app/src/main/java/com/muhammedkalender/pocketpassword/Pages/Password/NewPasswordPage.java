package com.muhammedkalender.pocketpassword.Pages.Password;

import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Controllers.PasswordController;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.DatabaseHelper;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.R;

public class NewPasswordPage  extends PageAbstract implements PageInterface {

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

        PasswordController passwordController = new PasswordController();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String password = etPassword.getText().toString();

                if(etName.getText().toString() == null || etName.getText().toString().length() == 0){
                    tilName.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.input_name)));

                    return;
                }else{
                    if(name.length() > Helpers.resource.getInt(R.integer.name_max_length)){
                        tilName.setError(Helpers.resource.getString(R.string.max_length, "", Helpers.resource.getString(R.string.input_name), Helpers.resource.getInt(R.integer.name_max_length)));

                        return;
                    }else if(name.length() < Helpers.resource.getInt(R.integer.name_min_length)){
                        tilName.setError(Helpers.resource.getString(R.string.min_length, "", Helpers.resource.getString(R.string.input_name), Helpers.resource.getInt(R.integer.name_min_length)));

                        return;
                    }else if(passwordController.checkDuplicate(name)){
                        tilName.setError(Helpers.resource.getString(R.string.already_used, "", name));

                        return;
                    }else{
                        tilName.setErrorEnabled(false);
                    }
                }

                if(etPassword.getText().toString() == null || etPassword.getText().toString().length() == 0){
                    tilPassword.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.input_password)));

                    return;
                }else{
                    if(password.length() > Helpers.resource.getInt(R.integer.password_max_length)){
                        tilPassword.setError(Helpers.resource.getString(R.string.max_length, "", Helpers.resource.getString(R.string.input_password)));

                    }else if(password.length() < Helpers.resource.getInt(R.integer.password_min_length)){
                        tilPassword.setError(Helpers.resource.getString(R.string.min_length, "", Helpers.resource.getString(R.string.input_password)));

                        return;
                    }else{
                        tilPassword.setErrorEnabled(false);
                    }
                }

                //todo color
                PasswordModel passwordModel = new PasswordModel(name, password, "");

                passwordModel.insert();
            }
        });
    }

    @Override
    public View getView() {
        return this.viewRoot;
    }
}
