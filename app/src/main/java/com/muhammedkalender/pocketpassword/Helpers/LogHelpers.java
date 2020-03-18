package com.muhammedkalender.pocketpassword.Helpers;

import android.util.Log;

import com.hypertrack.hyperlog.HyperLog;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
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
            if(!Config.CONFIG_ENABLE_ERROR_LOG){
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

            HyperLog.e("ERROR_" + errorCode, message);
            Log.e("ERROR_" + errorCode, message);
        }catch (Exception ignored){
        }
    }

    //endregion

    //region Info

    public void info(String message){
        info(-1, message);
    }

    public void info(int infoCode, String message){
        if(!Config.CONFIG_ENABLE_INFO_LOG){
            return;
        }

        try {
            Log.e("INFO_"+infoCode, message);
        }catch (Exception e){
            error(ErrorCodeConstants.LOGGER_INFO_WITH_MESSAGE, e);
        }
    }

    public void info(String prefix, String message){
        info(0, String.format(prefix, message));
    }

    public void var(String prefix, String value){
        if(!Config.CONFIG_ENABLE_VAR_LOG){
            return;
        }

        try{
            Log.e("VAR_" + prefix, value);
        }catch (Exception e){
            //todo
        }
    }


        //endregion
}