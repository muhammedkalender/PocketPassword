package com.muhammedkalender.pocketpassword.Controllers;

import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

public class PasswordController {
    public boolean checkDuplicate(String name){
        ResultObject check = Helpers.database.isAvailable("SELECT * FROM passwords WHERE password_name = '"+name+"'", "password_id");

        return check.isSuccess();

    }
}
