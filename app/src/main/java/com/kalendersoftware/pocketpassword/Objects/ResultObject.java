package com.kalendersoftware.pocketpassword.Objects;

import com.kalendersoftware.pocketpassword.Constants.ErrorCodeConstants;
import com.kalendersoftware.pocketpassword.Globals.Config;
import com.kalendersoftware.pocketpassword.Globals.Helpers;

public class ResultObject {
    //region Variables

    private int errorCode = 0;
    private boolean success = false;
    private String message = null;
    private Object data = null;

    //endregion

    //region Constructors

    public ResultObject() {

    }

    public ResultObject(int errorCode) {
        this.success = false;
        this.errorCode = errorCode;
    }

    public ResultObject(boolean success) {
        this.success = success;
    }

    public ResultObject(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResultObject(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    //endregion

    //region Getters

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    //endregion

    //region Set Error

    public ResultObject setError(Exception e) {
        this.success = false;
        this.data = e;

        if (this.errorCode > 0) {
            Helpers.logger.error(this.errorCode, e);
        }

        return this;
    }

    public ResultObject setError(Exception e, String message) {
        this.success = false;
        this.data = e;
        this.message = message;

        if (this.errorCode > 0) {
            Helpers.logger.error(this.errorCode, e);
        }

        return this;
    }

    //endregion

    //region Set Data

    public ResultObject setData(Object data) {
        this.success = true;
        this.data = data;

        return this;
    }

    //endregion

    //region Custom Data Returns

    public String getDataAsString() {
        return getDataAsString(Config.DEFAULT_STRING);
    }

    public String getDataAsString(String def) {
        try {
            return (String) getData();
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.RESULT_AS_STRING, e);

            return def;
        }
    }

    public int getDataAsInteger() {
        return getDataAsInteger(Config.DEFAULT_INTEGER);
    }

    public int getDataAsInteger(int def) {
        try {
            return (int) getData();
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.RESULT_AS_INTEGER, e);

            return def;
        }
    }

    public boolean getDataAsBoolean(){
        return getDataAsBoolean(Config.DEFAULT_BOOLEAN);
    }

    public boolean getDataAsBoolean(boolean def){
        try{
            return (boolean) getData();
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.RESULT_AS_BOOLEAN, e);

            return def;
        }
    }

    //endregion
}
