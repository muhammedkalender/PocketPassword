package com.muhammedkalender.pocketpassword.Abstracts;

import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Interfaces.ModelInterface;
import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

public abstract class ModelAbstract implements ModelInterface {
    protected String table = null;
    protected String prefix = null;

    protected ColumnObject[] columns = new ColumnObject[]{};


    public ResultObject delete(int id) {
        //todo

        return null;
    }

    public ResultObject update(){
        //OVVERIDE
        return update(this);
    }

    public ResultObject update(String query) {
        //todo
        //OVVERIDE

        return Helpers.database.execute(query);
    }

    public String queryTable() {
        String query = String.format("CREATE TABLE '%1$s' (", this.table);

        for (int i = 0; i < columns.length; i++) {
            /*
                1 => PREFIX
                2 => COLUMN NAME
                3 => COLUMN TYPE
                4 => NOT NULL
                5 => PRIMARY KEY
                6 => AUTOINCREMENT
                7 => DEFAULT
            */

            if(i != 0){
                query += ", ";
            }

            ColumnObject column = columns[i];

            String notNull = column.isNotNull() ? "NOT NULL" : "";
            String primary = column.isPrimary() ? "PRIMARY KEY" : "";
            String autoIncrement = column.isAutoIncrement() ? "AUTOINCREMENT" : "";
            String def = column.getDef() == null ? "" : "DEFAULT " + (column.getType() == "INTEGER" ? column.getDef() : "'" + column.getDef() + "'");

            query += String.format(
                    "'%1$s_%2$s' %3$s %4$s %5$s %6$s %7$s",
                    prefix,
                    column.getName(),
                    column.getType(),
                    notNull,
                    primary,
                    autoIncrement,
                    def
            );
        }

        query += ")";

        return query;
    }
}
