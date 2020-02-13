package com.muhammedkalender.pocketpassword.Helpers;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Constants.ValueConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;

public class ResourceHelper {
    public int getDimen(int res, int def){
        try{
            return Global.CONTEXT.getResources().getDimensionPixelSize(res);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.RESOURCE_DIMEN_TO_INTEGER, e);

            return def;
        }
    }

    public float getDimen(int res, float def){
        try{
            return Global.CONTEXT.getResources().getDimension(res);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.RESOURCE_DIMEN, e);

            return def;
        }
    }

    //region Integer

    public int getInt(int res){
        return getInt(res, ValueConstants.RESOURCE_INTEGER);
    }

    public int getInt(int res, int def) {
        try{
            return Global.CONTEXT.getResources().getInteger(res);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.RESOURCE_INT, e);

            return def;
        }
    }

    //endregion

    //region String

    public String getString(int res){
        return getString(res, "");
    }

    public String getString(int res, String def, Object... params){
        try {
            return Global.CONTEXT.getResources().getString(res, params);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.RESOURCE_STRING, e);

            return def;
        }
    }

    //endregion
}
