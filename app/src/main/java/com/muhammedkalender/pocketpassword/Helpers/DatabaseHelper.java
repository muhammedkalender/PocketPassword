package com.muhammedkalender.pocketpassword.Helpers;

import android.content.Context;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Constants.InfoCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

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

            //todo Password must be from a variable or changed any time etc...
            database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "PocketPassword", null);

            if (isFirst) {
                database.execSQL("CREATE TABLE \"passwords\" (\n" +
                        "\t\"password_id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "\t\"password_name\"\tINTEGER NOT NULL,\n" +
                        "\t\"password_password\"\tINTEGER NOT NULL,\n" +
                        "\t\"password_color\"\tINTEGER DEFAULT '',\n" +
                        "\t\"password_insert\"\tTEXT DEFAULT '',\n" +
                        "\t\"password_update\"\tTEXT DEFAULT '',\n" +
                        "\t\"password_active\"\tINTEGER DEFAULT 1\n" +
                        ");"
                );
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
        if(Config.LOG_SQL_EXECUTE){
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

    public ResultObject isAvailable(String query, String column) {
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
                if (cursor.getColumnIndex(column) <= 0) {
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
        if(Config.LOG_SQL_EXECUTE){
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
                        .setError(new Exception("QUERY BOŞ DÖNDÜ")); //todo
            }

            return new ResultObject()
                    .setData(cursor);
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.SQL_SELECT_WITH_DATA)
                    .setError(e);
        }
    }

    //endregion
}
