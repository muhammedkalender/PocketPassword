package com.muhammedkalender.pocketpassword.Models;

import android.database.Cursor;
import android.util.Log;

import com.muhammedkalender.pocketpassword.Abstracts.ModelAbstract;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Constants.SQLConstants;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.CryptHelper;
import com.muhammedkalender.pocketpassword.Interfaces.ModelInterface;
import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

import java.util.ArrayList;
import java.util.List;

public class PasswordModel extends ModelAbstract {
    private int id;

    private String name;
    private String password;
    private String color;

    private boolean nameEncrypted = true;

    private boolean active;

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

    public PasswordModel(int id, String name, String password, String color) {
        initTable();

        this.id = id;
        this.name = name;
        this.password = password;
        this.color = color;
    }

    public PasswordModel(int id, String name, String password, String color, boolean active) {
        initTable();

        this.id = id;
        this.name = name;
        this.password = password;
        this.color = color;
        this.active = active;
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
            return Helpers.database.insert("INSERT INTO passwords (password_name, password_password, password_color) VALUES ('" + name + "', '" + password + "', '')");
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.MODEL_PASSWORD_INSERT)
                    .setError(e);
        }
    }

    public boolean checkDuplicate(String name){
        return checkDuplicate(name, true);
    }

    public boolean checkDuplicate(String name, boolean ignoreCamelCase) {
        ResultObject check = Helpers.database.isAvailable("SELECT * FROM passwords WHERE password_name = '" + name + "'", "password_id", ignoreCamelCase);

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
    public List<PasswordModel> selectActive() {
        ResultObject select = Helpers.database.cursor("SELECT * FROM " + table + " WHERE " + prefix + "_active = 1");

        if (select.isSuccess()) {
            Cursor cursor = (Cursor) select.getData();

            Helpers.logger.info(String.format("Selected %1$s password", cursor.getCount()));

            List passwords = new ArrayList();

            while(cursor.moveToNext()) {
                passwords.add(new PasswordModel(
                        cursor.getInt(cursor.getColumnIndex(prefix + "_id")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_name")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_password")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_color")),
                        cursor.getInt(cursor.getColumnIndex(prefix + "_active")) == 1

                ));
            }

            return passwords;
        } else {
            //todo
            Log.e("asdas", ((Exception) select.getData()).getMessage());

            return new ArrayList();
        }
    }

    @Override
    public ResultObject validation() {
        try {
            //todo
            return null;
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.MODEL_VALID)
                    .setError(e);
        }
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public ResultObject update() {
        //todo
        return super.update();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public ResultObject update(Object update) {
        //todo

        String queryUpdate = String.format(
                "UPDATE %1$s SET %2$s_name = '%4$s', %2$s_password = '%5$s', %2$s_color = '%6$s', %2$s_active = %7$s WHERE %2$s_id = %3$s",
                this.table,
                this.prefix,
                this.id,
                this.name,
                this.password,
                this.color,
                this.active
        );

        Log.e("asda", queryUpdate);

        return super.update(queryUpdate);
    }

    public int getId() {
        return id;
    }

    public boolean isNameEncrypted() {
        return nameEncrypted;
    }

    public void setNameEncrypted(boolean nameEncrypted) {
        this.nameEncrypted = nameEncrypted;
    }

    public void decryptName(CryptHelper cryptHelper){
        if(isNameEncrypted()){
            this.name = cryptHelper.quickDecrypt(this.name);

            setNameEncrypted(false);
        }
    }
}
