package com.muhammedkalender.pocketpassword.Interfaces;

import android.database.Cursor;

import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

import java.util.ArrayList;
import java.util.List;

public interface ModelInterface<T> {
    public ResultObject initTable();

  //todo ? spesifik return lazım
    public T insert(Object model);
    public T get(int id);
    public List<T> select();
    public List<T> selectActive();
    public ResultObject delete(int id);
    public ResultObject update(T model);
    public ResultObject validation();
}
