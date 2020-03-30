package com.kalendersoftware.pocketpassword.Interfaces;

import com.kalendersoftware.pocketpassword.Objects.ResultObject;

import java.util.List;

public interface ModelInterface<T> {
    public ResultObject initTable();

  //todo ? spesifik return lazım
    public T insert(T model);
    public T insert(String query);
    public T insert();
    public T get(int id);
    public T insertWithCheckDuplicate();
    public T insertWithCheckDuplicate(T model);
    public List<T> select();
    public List<T> selectActive();
    public ResultObject delete(int id);
    public ResultObject update(T model);
    public ResultObject validation();
    public ResultObject delete();
}
