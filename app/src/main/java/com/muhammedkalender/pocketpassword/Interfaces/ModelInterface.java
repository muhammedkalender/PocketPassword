package com.muhammedkalender.pocketpassword.Interfaces;

import android.database.Cursor;

import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

import java.util.ArrayList;
import java.util.List;

public interface ModelInterface<T> {
    public ResultObject initTable();

  //todo ? spesifik return lazım
    public T insert(Object model);
    public T get(int id);
    public List select();
    public List selectActive();
    public ResultObject delete(int id);
    public T update(Object model);
}
