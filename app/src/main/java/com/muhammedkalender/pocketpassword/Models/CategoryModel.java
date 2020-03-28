package com.muhammedkalender.pocketpassword.Models;

import android.database.Cursor;
import android.widget.Toast;

import com.muhammedkalender.pocketpassword.Abstracts.ModelAbstract;
import com.muhammedkalender.pocketpassword.Constants.SQLConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Interfaces.ModelInterface;
import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;
import com.muhammedkalender.pocketpassword.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryModel extends ModelAbstract implements ModelInterface {
    private int id, color, tintColor;
    private String name;
    private boolean active;

    public CategoryModel() {
        initTable();
    }

    public CategoryModel(String name, int color, int tintColor, boolean active) {
        initTable();

        this.color = color;
        this.tintColor = tintColor;
        this.name = name;
        this.active = active;
    }

    public CategoryModel(int id, String name, int color, int tintColor, boolean active) {
        initTable();

        this.id = id;
        this.color = color;
        this.tintColor = tintColor;
        this.name = name;
        this.active = active;
    }

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
    public ResultObject insert() {
        return insert(this);
    }

    @Override
    public ResultObject insert(Object model) {
        CategoryModel obj = (CategoryModel) model;

        String queryInsert = String.format(
                "INSERT INTO %1$s (%2$s_name, %2$s_color, %2$s_tint_color) VALUES ('%3$s', '%4$s', '%5$s')",
                obj.table,
                obj.prefix,
                obj.name,
                obj.color,
                obj.tintColor
        );

        return super.insert(queryInsert);
    }

    @Override
    public ResultObject insertWithCheckDuplicate() {
        return this.insertWithCheckDuplicate(this);
    }

    @Override
    public ResultObject insertWithCheckDuplicate(Object model) {
        CategoryModel obj = (CategoryModel) model;

        ResultObject resultCheckDuplicate = Helpers.database.isAvailable(String.format(
                "SELECT %2$s_id FROM %1$s WHERE %2$s_name = '%3$s'",
                obj.table,
                obj.prefix,
                obj.name
        ), obj.prefix + "_id", true);

        if (resultCheckDuplicate.isFailure() || resultCheckDuplicate.getDataAsBoolean()) {
            return null;
        }

        return this.insert();
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
        return selectActive(true);
    }

    public List selectActive(boolean addAllOfThem) {
        ResultObject select = Helpers.database.cursor("SELECT * FROM " + table + " WHERE " + prefix + "_active = 1 ORDER BY " + prefix + "_name");

        if (select.isSuccess()) {
            Cursor cursor = (Cursor) select.getData();

            Helpers.logger.info(String.format("Selected %1$s password", cursor.getCount()));

            List<CategoryModel> categories = new ArrayList<>();

            categories.add(new CategoryModel(0, Helpers.resource.getString(R.string.category_all), -1, -1, true));

            categories.add(null); //Reserve for Others Category

            String resOther = Helpers.resource.getString(R.string.category_other, "Others");

            while (cursor.moveToNext()) {
                CategoryModel categoryModel = new CategoryModel(
                        cursor.getInt(cursor.getColumnIndex(prefix + "_id")),
                        cursor.getString(cursor.getColumnIndex(prefix + "_name")),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(prefix + "_color"))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(prefix + "_tint_color"))),
                        cursor.getInt(cursor.getColumnIndex(prefix + "_active")) == 1
                );

                if(categoryModel.getName().equals(resOther)){
                    categories.set(1, categoryModel);
                }else{
                    categories.add(categoryModel);
                }
            }

            if(categories.get(1) == null){
                categories.remove(1);
            }

            return categories;
        } else {
            Toast.makeText(Global.CONTEXT, R.string.failure_load_category_list, Toast.LENGTH_SHORT).show();

            return new ArrayList();
        }
    }

    @Override
    public ResultObject update(Object model) {
        String queryUpdate = String.format(
                "UPDATE %1$s SET %2$s_name = '%4$s', %2$s_account= '%5$s', %2$s_password = '%6$s', %2$s_color = '%7$s', %2$s_tint_color = '%9$s', %2$s_active = %8$s WHERE %2$s_id = %3$s",
                this.table,
                this.prefix,
                this.id,
                this.name,
                this.color,
                this.active,
                this.tintColor
        );

        return super.update(queryUpdate);
    }

    @Override
    public ResultObject validation() {
        return null;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public int getColor() {
        return color;
    }

    public int getTintColor() {
        return tintColor;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }
}
