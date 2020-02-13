package com.muhammedkalender.pocketpassword.Interfaces;

import android.database.Cursor;

import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

import java.util.ArrayList;
import java.util.List;

public interface ModelInterface {
    public ResultObject initTable();

  //todo ? spesifik return lazım
    public Object insert(Object model);
    public Object get(int id);
    public List select();
    public List selectActive();
    public ResultObject delete(int id);
    public Object update(Object model);
}
