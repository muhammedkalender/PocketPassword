package com.muhammedkalender.pocketpassword.Helpers;

import android.util.Log;

import com.muhammedkalender.pocketpassword.Globals.Config;

public class LogHelpers {

    //region Error

    public void error(int errorCode, String message){
        error(errorCode, null, message);
    }

    public void error(int errorCode, Exception e){
        error(errorCode, e, null);
    }

    public void error(int errorCode, Exception e, String message){
        try{
            if(!Config.LOG_ENABLE){
                return;
            }

            if(message == null || message.equals("")){
                if(e != null){
                    message = e.getMessage();
                }

                if(message == null || message.equals("")){
                    message = "NOPE";
                }
            }

            Log.e("ERROR_" + errorCode, message);
        }catch (Exception ignored){
            //TODO ?
        }
    }

    //endregion
}
