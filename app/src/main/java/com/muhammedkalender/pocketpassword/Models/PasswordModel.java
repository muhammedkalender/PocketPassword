package com.muhammedkalender.pocketpassword.Models;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

public class PasswordModel {
    private String name;
    private String password;
    private String color;

    public PasswordModel(String name, String password, String color) {
        this.name = name;
        this.password = password;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getPassword(){
        return password;
    }

    public String getEncryptedPassword() {
        //todo
        return password;
    }

    public String getColor() {
        return color;
    }

    public ResultObject insert(){
        try {
            return Helpers.database.execute("INSERT INTO passwords (password_name, password_password, password_color) VALUES ('"+name+"', '"+password+"', NULL)");
        }catch (Exception e){
            return new ResultObject(ErrorCodeConstants.MODEL_PASSWORD_INSERT)
                    .setError(e);
        }
    }
}
