package com.muhammedkalender.pocketpassword.Models;

import android.database.Cursor;
import android.widget.Toast;

import com.muhammedkalender.pocketpassword.Abstracts.ModelAbstract;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Constants.SQLConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.CryptHelper;
import com.muhammedkalender.pocketpassword.Interfaces.ModelInterface;
import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;
import com.muhammedkalender.pocketpassword.R;

import java.util.ArrayList;
import java.util.List;

public class PasswordModel extends ModelAbstract implements ModelInterface {
    //region Variables

    private int id;

    private String name;
    private String password;
    private int color;

    private boolean active;

    private boolean nameEncrypted = true;
    private boolean decrypted = false;

    //endregion

    //region Constructors

    public PasswordModel() {
        initTable();
    }

    public PasswordModel(String name, String password, int color) {
        initTable();

        this.name = name;
        this.password = password;
        this.color = color;
    }

    public PasswordModel(int id, String name, String password, int color) {
        initTable();

        this.id = id;
        this.name = name;
        this.password = password;
        this.color = color;
    }

    public PasswordModel(int id, String name, String password, int color, boolean active) {
        initTable();

        this.id = id;
        this.name = name;
        this.password = password;
        this.color = color;
        this.active = active;
    }

    //endregion

    //region Initializer

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

    //endregion

    //region Database Functions

    @Override
    public PasswordModel insert(Object model) {
        return null;
    }

    @Override
    public Object get(int id) {
        return null;
    }

    public ResultObject insert() {
        try {
            return Helpers.database.insert("INSERT INTO passwords (password_name, password_password, password_color) VALUES ('" + Helpers.crypt.quickEncrypt(name) + "', '" + Helpers.crypt.quickEncrypt(password) + "', '')");
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.MODEL_PASSWORD_INSERT)
                    .setError(e);
        }
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

            int index = 0;

            while (cursor.moveToNext()) {
                int color = ColorConstants.colorItem[index++ % ColorConstants.colorItem.length].getColor();

                passwords.add(new PasswordModel(
                        cursor.getInt(cursor.getColumnIndex(prefix + "_id")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_name")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_password")),
                        color, //todo cursor.getInt(cursor.getColumnIndex(prefix + "_color")),
                        cursor.getInt(cursor.getColumnIndex(prefix + "_active")) == 1

                ));
            }

            return passwords;
        } else {
            Toast.makeText(Global.CONTEXT, R.string.failure_load_list, Toast.LENGTH_SHORT).show();

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

    @Override
    public ResultObject update() {
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

        return super.update(queryUpdate);
    }

    @Override
    public ResultObject update(Object obj) {
        PasswordModel model = (PasswordModel) obj;

        String queryUpdate = String.format(
                "UPDATE %1$s SET %2$s_name = '%4$s', %2$s_password = '%5$s', %2$s_color = '%6$s', %2$s_active = %7$s WHERE %2$s_id = %3$s",
                model.table,
                model.prefix,
                model.id,
                model.name,
                model.password,
                model.color,
                model.active
        );

        return super.update(queryUpdate);
    }

    //endregion

    //region Getters & Setters

    //region Getters

    public int getId() {
        return id;
    }

    public boolean isNameEncrypted() {
        return nameEncrypted;
    }

    public boolean isDecrypted() {
        return decrypted;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEncryptedPassword() {
        if(isDecrypted()){
            return Helpers.crypt.quickEncrypt(this.password);
        }else{
            return password;
        }
    }

    public int getColor() {
        return color;
    }

    public boolean isActive() {
        return active;
    }

    //endregion

    //region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNameEncrypted(boolean nameEncrypted) {
        this.nameEncrypted = nameEncrypted;
    }

    public void setDecrypted(boolean decrypted) {
        this.decrypted = decrypted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    //endregion

    //endregion

    //region Secondary Functions

    public void decryptName(CryptHelper cryptHelper){
        if(isNameEncrypted()){
            this.name = cryptHelper.quickDecrypt(this.name);

            setNameEncrypted(false);
        }
    }

    //endregion

    //region Check Duplicate

    public boolean checkDuplicate(String name) {
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

    //endregion

    //region Quick Encrypt & Decrypt

    public void decrypt() {
        if (isDecrypted()) {
            return;
        }

        this.name = Helpers.crypt.quickDecrypt(this.name);
        this.password = Helpers.crypt.quickDecrypt(this.password);
        this.decrypted = true;
    }

    public void encrypt() {
        if (!isDecrypted()) {
            return;
        }

        this.name = Helpers.crypt.quickEncrypt(this.name);
        this.password = Helpers.crypt.quickEncrypt(this.password);
        this.decrypted = false;
    }

    //endregion
}
