package com.muhammedkalender.pocketpassword.Helpers;

import android.content.Context;

import com.muhammedkalender.pocketpassword.Abstracts.ModelAbstract;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Constants.InfoCodeConstants;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Models.CategoryModel;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

public class DatabaseHelper {

    //region Variables

    private boolean initialized = false;
    private ResultObject error = null;
    private SQLiteDatabase database;

    //endregion

    //region Constructors

    public DatabaseHelper(Context context) {
        try {
            SQLiteDatabase.loadLibs(context);
            File databaseFile = new File(context.getCacheDir(), "db.db");

            boolean isFirst = true;

            if (databaseFile.exists()) {
                isFirst = false;
            }

            database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "PocketPassword", null);

            if (isFirst) {
                ModelAbstract[] models = new ModelAbstract[]{
                        new CategoryModel(),
                        new PasswordModel()
                };

                for (ModelAbstract model : models) {
                    this.execute(model.queryTable());
                }
            }

            this.initialized = true;
        } catch (Exception e) {
            this.initialized = false;
            this.error = new ResultObject(ErrorCodeConstants.SQL_INITIALIZE)
                    .setError(e);
        }
    }

    //endregion

    //region Class

    public ResultObject checkInit() {
        if (this.initialized) {
            return new ResultObject(true);
        } else {
            return this.error;
        }
    }

    //endregion

    //region Execute

    public ResultObject execute(String query) {
        if (Config.LOG_SQL_EXECUTE) {
            Helpers.logger.info(InfoCodeConstants.SQL_EXECUTE, query);
        }

        try {
            database.execSQL(query);

            return new ResultObject(true);
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.SQL_EXECUTE)
                    .setError(e);
        }
    }

    //endregion

    //region Available

    public ResultObject isAvailable(String query, String column){
        return isAvailable(query, column, false);
    }

    public ResultObject isAvailable(String query, String column, boolean ignoreCamelCase) {
        try {
            if(ignoreCamelCase){
                query += "  COLLATE NOCASE";
            }

            ResultObject select = this.cursor(query);

            if (select.isFailure()) {
                return select;
            }

            Cursor cursor = (Cursor) select.getData();

            if (cursor == null || cursor.getCount() <= 0) {
                return new ResultObject(true, "", false);
            }

            while (cursor.moveToNext()) {
                if (cursor.getColumnIndex(column) < 0) {
                    return new ResultObject(true, "", false);
                }

                if (cursor.getString(cursor.getColumnIndex(column)) == null) {
                    return new ResultObject(true, "", false);
                }
            }

            return new ResultObject(true, "", true);

        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.SQL_IS_AVAILABLE)
                    .setError(e);
        }
    }

    //endregion

    //region Select

    public ResultObject select(String query, String column) {
        try {
            ResultObject select = this.cursor(query);

            if (select.isFailure()) {
                return select;
            }

            Cursor cursor = (Cursor) select.getData();

            if (cursor == null || cursor.getCount() <= 0) {
                return new ResultObject(true, "", false);
            }

            while (cursor.moveToNext()) {
                if (cursor.getColumnIndex(column) <= 0 || cursor.getString(cursor.getColumnIndex(column)) == null) {
                    return new ResultObject(ErrorCodeConstants.SQL_SELECT_SINGLE_COLUMN_EMPTY);
                }

                return new ResultObject()
                        .setData(cursor.getString(cursor.getColumnIndex(column)));
            }

            return new ResultObject(ErrorCodeConstants.SQL_SELECT_SINGLE_COLUMN_EMPTY);
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.SQL_SELECT_SINGLE_COLUMN)
                    .setError(e);
        }
    }

    //endregion

    //region Cursor

    public ResultObject cursor(String query) {
        if (Config.LOG_SQL_EXECUTE) {
            Helpers.logger.info(InfoCodeConstants.SQL_CURSOR, query);
        }

        try {
            Cursor cursor = (Cursor) database.query(query);

            return new ResultObject()
                    .setData(cursor);
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.SQL_SELECT)
                    .setError(e);
        }
    }

    public ResultObject cursorWithData(String query) {
        try {
            Cursor cursor = (Cursor) database.query(query);

            if (cursor == null || cursor.getCount() <= 0) {
                return new ResultObject(ErrorCodeConstants.SQL_SELECT_WITH_DATA_EMPTY)
                        .setError(new Exception("QUERY IS EMPTT"));
            }

            return new ResultObject()
                    .setData(cursor);
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.SQL_SELECT_WITH_DATA)
                    .setError(e);
        }
    }

    //endregion

    //region Insert

    public ResultObject insert(String query) {
        if (Config.LOG_SQL_EXECUTE) {
            Helpers.logger.info(InfoCodeConstants.SQL_CURSOR, query);
        }

        try {
            database.execSQL(query);

            Cursor cursor = (Cursor) database.query("SELECT last_insert_rowid();");

            if (cursor.moveToFirst()) {
                return new ResultObject()
                        .setData(cursor.getInt(0));
            } else {
                return new ResultObject()
                        .setData(-1)
                        .setError(new Exception("TODO"));
            }
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.SQL_EXECUTE)
                    .setData(-1)
                    .setError(e);
        }
    }

    //endregion
}
