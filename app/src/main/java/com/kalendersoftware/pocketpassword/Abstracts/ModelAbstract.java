package com.kalendersoftware.pocketpassword.Abstracts;

import com.kalendersoftware.pocketpassword.Globals.Helpers;
import com.kalendersoftware.pocketpassword.Interfaces.ModelInterface;
import com.kalendersoftware.pocketpassword.Objects.ColumnObject;
import com.kalendersoftware.pocketpassword.Objects.ResultObject;

public abstract class ModelAbstract<T> implements ModelInterface {
    protected String table = null;
    protected String prefix = null;

    protected int id;

    protected ColumnObject[] columns = new ColumnObject[]{};

    public ResultObject delete(int id) {
        String query = String.format(
                "UPDATE %1$s SET %2$s_active = 0 WHERE %2$s_id = %3$d",
                table,
                prefix,
                id
        );

        return Helpers.database.execute(query);
    }

    public ResultObject update() {
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

            if (i != 0) {
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

    @Override
    public T insert(Object model) {
        return null;
    }

    @Override
    public ResultObject insert(String query) {
        return Helpers.database.insert(query);
    }

    @Override
    public ResultObject delete() {
        return delete(this.id);
    }
}
