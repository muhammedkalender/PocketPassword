package com.muhammedkalender.pocketpassword.Objects;

public class ResultObject {
    //region Variables

    private int errorCode = 0;
    private boolean success = false;
    private String message = null;
    private Object data = null;

    //endregion

    //region Constructors

    public ResultObject(){

    }

    public ResultObject(int errorCode){
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

    public boolean isFailure(){
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

    public ResultObject setError(Exception e){
        this.success = false;
        this.data = e;

        return this;
    }

    public ResultObject setError(Exception e, String message){
        this.success = false;
        this.data = e;
        this.message = message;

        return this;
    }

    //endregion

    //region Set Data

    public ResultObject setData(Object data){
        this.success = true;
        this.data = data;

        return this;
    }

    //endregion
}
