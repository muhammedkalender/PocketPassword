package com.muhammedkalender.pocketpassword.Models;

import android.database.Cursor;
import android.widget.Toast;

import com.muhammedkalender.pocketpassword.Abstracts.ModelAbstract;
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

    private String name;
    private String account;
    private String password;
    private int color;
    private int tintColor;
    private int categoryID;

    private boolean active;

    private boolean nameEncrypted = true;
    private boolean decrypted = false;

    //endregion

    //region Constructors

    public PasswordModel() {
        initTable();
    }

    public PasswordModel(String name, String account, String password, int color, int tintColor) {
        initTable();

        this.name = name;
        this.account = account;
        this.password = password;
        this.color = color;
        this.tintColor = tintColor;
    }

    public PasswordModel(String name, String account, String password, int color, int tintColor, int categoryID) {
        initTable();

        this.name = name;
        this.account = account;
        this.password = password;
        this.color = color;
        this.tintColor = tintColor;
        this.categoryID = categoryID;
    }

    public PasswordModel(int id, String name, String account, String password, int color, int tintColor) {
        initTable();

        super.id = id;
        this.name = name;
        this.account = account;
        this.password = password;
        this.color = color;
        this.tintColor = tintColor;
    }

    public PasswordModel(int id, String name, String account, String password, int color, int tintColor, int categoryID) {
        initTable();

        super.id = id;
        this.name = name;
        this.account = account;
        this.password = password;
        this.color = color;
        this.tintColor = tintColor;
        this.categoryID = categoryID;
    }

    public PasswordModel(int id, String name, String account, String password, int color, int tintColor, boolean active) {
        initTable();

        super.id = id;
        this.name = name;
        this.account = account;
        this.password = password;
        this.color = color;
        this.tintColor = tintColor;
        this.active = active;
    }

    public PasswordModel(int id, String name, String account, String password, int color, int tintColor, int categoryID, boolean active) {
        initTable();

        super.id = id;
        this.name = name;
        this.account = account;
        this.password = password;
        this.color = color;
        this.tintColor = tintColor;
        this.categoryID = categoryID;
        this.active = active;
    }

    public PasswordModel(String name, String account, String password, int color, int tintColor, int categoryID, boolean active) {
        initTable();

        this.name = name;
        this.account = account;
        this.password = password;
        this.color = color;
        this.tintColor = tintColor;
        this.categoryID = categoryID;
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
                        .setName("account")
                        .setType(SQLConstants.TYPE_STRING)
                        .setNotNull()
                        .setMinLength(1)
                        .setMaxLength(32),
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
                        .setMaxLength(8),
                new ColumnObject()
                        .setName("tint_color")
                        .setType(SQLConstants.TYPE_STRING)
                        .setDef("")
                        .setMinLength(0)
                        .setMaxLength(8),
                new ColumnObject()
                        .setName("category")
                        .setType(SQLConstants.TYPE_INTEGER)
                        .setDef("0")
                        .setMinLength(1)
                        .setMaxLength(8),
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
            return Helpers.database.insert("INSERT INTO passwords (password_name, password_account, password_password, password_color, password_tint_color, password_category) VALUES ('" + name + "', '" + Helpers.crypt.quickEncrypt(account) + "', '" + Helpers.crypt.quickEncrypt(password) + "', '" + this.color + "', '" + this.tintColor + "', '" + this.categoryID + "')");
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.MODEL_PASSWORD_INSERT)
                    .setError(e);
        }
    }

    @Override
    public List select() {
        ResultObject select = Helpers.database.cursor("SELECT * FROM " + table + " ORDER BY " + prefix + "_id DESC");

        if (select.isSuccess()) {
            Cursor cursor = (Cursor) select.getData();

            Helpers.logger.info(String.format("Selected %1$s password from all", cursor.getCount()));

            List passwords = new ArrayList();

            while (cursor.moveToNext()) {
                passwords.add(new PasswordModel(
                        cursor.getInt(cursor.getColumnIndex(prefix + "_id")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_name")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_account")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_password")),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(prefix + "_color"))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(prefix + "_tint_color"))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(prefix + "_category"))),
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
    public List<PasswordModel> selectActive() {
        ResultObject select = Helpers.database.cursor("SELECT * FROM " + table + " WHERE " + prefix + "_active = 1 ORDER BY " + prefix + "_id DESC");

        if (select.isSuccess()) {
            Cursor cursor = (Cursor) select.getData();

            Helpers.logger.info(String.format("Selected %1$s password", cursor.getCount()));

            List passwords = new ArrayList();

            while (cursor.moveToNext()) {
                passwords.add(new PasswordModel(
                        cursor.getInt(cursor.getColumnIndex(prefix + "_id")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_name")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_account")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_password")),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(prefix + "_color"))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(prefix + "_tint_color"))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(prefix + "_category"))),
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
                "UPDATE %1$s SET %2$s_name = '%4$s', %2$s_account= '%5$s', %2$s_password = '%6$s', %2$s_color = '%7$s', %2$s_tint_color = '%9$s', %2$s_active = %8$s, %2$s_category = %10$d WHERE %2$s_id = %3$s",
                this.table,
                this.prefix,
                super.id,
                this.name,
                this.account,
                this.password,
                this.color,
                this.active,
                this.tintColor,
                this.categoryID
        );

        return super.update(queryUpdate);
    }

    @Override
    public ResultObject update(Object obj) {
        PasswordModel model = (PasswordModel) obj;

        String queryUpdate = String.format(
                "UPDATE %1$s SET %2$s_name = '%4$s',%2$s_account = '%5$s', %2$s_password = '%6$s', %2$s_color = '%7$s', %2$s_tint_color = '%9$s', %2$s_active = %8$s, %2$s_category = %10$s WHERE %2$s_id = %3$s",
                model.table,
                model.prefix,
                model.id,
                model.name,
                model.account,
                model.password,
                model.color,
                model.active,
                model.tintColor,
                model.categoryID
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

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getEncryptedPassword() {
        if (isDecrypted()) {
            return Helpers.crypt.quickEncrypt(this.password);
        } else {
            return password;
        }
    }

    public int getColor() {
        return color;
    }

    public int getTintColor() {
        return tintColor;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public boolean isActive() {
        return active;
    }

    //endregion

    //region Setters

    public void setId(int id) {
        super.id = id;
    }

    public void setDecrypted(boolean decrypted) {
        this.decrypted = decrypted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    //endregion

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
        decrypt(Helpers.crypt);
    }

    public void decrypt(CryptHelper cryptHelper) {
        if (isDecrypted()) {
            return;
        }

        Helpers.logger.info("Model Decrypt Girildi");

        this.account = cryptHelper.quickDecrypt(this.account);
        this.password = cryptHelper.quickDecrypt(this.password);
        this.decrypted = true;
    }

    public void encrypt(){
        encrypt(Helpers.crypt);
    }


    public void encrypt(CryptHelper cryptHelper) {
        if (!isDecrypted()) {
            return;
        }

        this.account = Helpers.crypt.quickEncrypt(this.account);
        this.password = Helpers.crypt.quickEncrypt(this.password);
        this.decrypted = false;
    }

    public String getDecryptedPassword() {
        if (this.isDecrypted()) {
            return this.password;
        } else {
            return Helpers.crypt.quickDecrypt(this.password);
        }
    }

    public String getDecryptedAccount() {
        if (this.isDecrypted()) {
            return this.account;
        } else {
            return Helpers.crypt.quickDecrypt(this.account);
        }
    }

    //endregion
}
