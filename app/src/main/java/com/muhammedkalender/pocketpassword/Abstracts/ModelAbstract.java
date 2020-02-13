package com.muhammedkalender.pocketpassword.Abstracts;

import com.muhammedkalender.pocketpassword.Interfaces.ModelInterface;
import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

import java.util.List;

public abstract class ModelAbstract implements ModelInterface {
    protected String table = null;
    protected String prefix = null;

    protected ColumnObject[] columns = new ColumnObject[]{};

    public Object get(int id){
        //todo

        return null;
    }

    public ResultObject delete(int id){
        //todo

        return null;
    }

    public Object update(Object update){
        //todo
        return null;
    }
}
