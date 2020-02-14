package com.muhammedkalender.pocketpassword.Models;

import android.database.Cursor;
import android.util.Log;

import com.muhammedkalender.pocketpassword.Abstracts.ModelAbstract;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Constants.SQLConstants;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Interfaces.ModelInterface;
import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

import java.util.ArrayList;
import java.util.List;

public class PasswordModel extends ModelAbstract {


    private String name;
    private String password;
    private String color;

    public PasswordModel() {
        initTable();
        //todo
    }

    public PasswordModel(String name, String password, String color) {
        initTable();

        this.name = name;
        this.password = password;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEncryptedPassword() {
        //todo
        return password;
    }

    public String getColor() {
        return color;
    }

    public ResultObject insert() {
        try {
            return Helpers.database.execute("INSERT INTO passwords (password_name, password_password, password_color) VALUES ('" + name + "', '" + password + "', '')");
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.MODEL_PASSWORD_INSERT)
                    .setError(e);
        }
    }

    public boolean checkDuplicate(String name) {
        ResultObject check = Helpers.database.isAvailable("SELECT * FROM passwords WHERE password_name = '" + name + "'", "password_id");

        if (check.isSuccess()) {
            return (Boolean) check.getData();
        } else {
            return false;
        }
    }

    @Override
    public ResultObject initTable() {
        this.table = "passwords";
        this.prefix = "password";

        this.columns = new ColumnObject[]{
                new ColumnObject()
                        .setName("id")
                        .setType(SQLConstants.TYPE_INTEGER)
                        .setAutoIncrement()
                        .setPrimary(),
                new ColumnObject()
                        .setName("name")
                        .setType(SQLConstants.TYPE_STRING)
                        .setNotNull()
                        .setMinLength(1)
                        .setMaxLength(128),
                new ColumnObject()
                        .setName("password")
                        .setType(SQLConstants.TYPE_STRING)
                        .setNotNull()
                        .setMinLength(8)
                        .setMaxLength(128),
                new ColumnObject()
                        .setName("color")
                        .setType(SQLConstants.TYPE_STRING)
                        .setDef("")
                        .setMinLength(0)
                        .setMaxLength(6),
                new ColumnObject()
                        .setName("insert")
                        .setType(SQLConstants.TYPE_DATE)
                        .setDef("")
                        .setMinLength(0)
                        .setMaxLength(32),
                new ColumnObject()
                        .setName("update")
                        .setType(SQLConstants.TYPE_DATE)
                        .setDef("")
                        .setMinLength(0)
                        .setMaxLength(32),
                new ColumnObject()
                        .setName("active")
                        .setType(SQLConstants.TYPE_BOOLEAN)
                        .setDef("1")
                        .setMinLength(0)
                        .setMaxLength(1)
        };

        return null;
    }

    @Override
    public PasswordModel insert(Object model) {
        return null;
    }

    @Override
    public List select() {
        return null;
    }

    @Override
    public List selectActive() {
        ResultObject select = Helpers.database.cursor("SELECT * FROM " + table + " WHERE " + prefix + "_active = 1");

        if (select.isSuccess()) {
            Cursor cursor = (Cursor) select.getData();

            List passwords = new ArrayList();

            if (cursor.moveToNext()) {
                passwords.add(new PasswordModel(
                        cursor.getString(cursor.getColumnIndex(prefix + "_name")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_password")),
                        ""
                ));
            }

            return passwords;
        } else {
            //todo
            Log.e("asdas", ((Exception) select.getData()).getMessage());

            return new ArrayList();
        }
    }
}
