package com.muhammedkalender.pocketpassword.Models;

import com.muhammedkalender.pocketpassword.Abstracts.ModelAbstract;
import com.muhammedkalender.pocketpassword.Constants.SQLConstants;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Interfaces.ModelInterface;
import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

import java.util.List;

public class CategoryModel extends ModelAbstract implements ModelInterface {
    private int id, color;
    private String name;
    private boolean active;

    @Override
    public ResultObject initTable() {
        this.table = "categories";
        this.prefix = "category";

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
    public CategoryModel insert(Object model) {


        String queryUpdate = String.format(
                "UPDATE %1$s SET %2$s_name = '%4$s', %2$s_account= '%5$s', %2$s_password = '%6$s', %2$s_color = '%7$s', %2$s_tint_color = '%9$s', %2$s_active = %8$s WHERE %2$s_id = %3$s",
                this.table,
                this.prefix,
                this.id,
                this.name,
                this.account,
                this.password,
                this.color,
                this.active,
                this.tintColor
        );

        return super.insert(queryUpdate);
        return null;
    }

    @Override
    public Object get(int id) {
        return null;
    }

    @Override
    public List select() {
        return null;
    }

    @Override
    public List selectActive() {
        return null;
    }

    @Override
    public ResultObject update(Object model) {
        return null;
    }

    @Override
    public ResultObject validation() {
        return null;
    }
}
