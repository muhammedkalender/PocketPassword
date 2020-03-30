package com.kalendersoftware.pocketpassword.Helpers;

import android.graphics.drawable.Drawable;

import com.kalendersoftware.pocketpassword.Constants.ErrorCodeConstants;
import com.kalendersoftware.pocketpassword.Constants.ValueConstants;
import com.kalendersoftware.pocketpassword.Global;
import com.kalendersoftware.pocketpassword.Globals.Helpers;

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

    //region Color

    public int getColor(int res){
        return getColor(res, -1);
    }

    public int getColor(int res, int def){
        try{
            return Global.CONTEXT.getResources().getColor(res);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.RESOURCE_COLOR, e);

            return def;
        }
    }

    //endregion

    //region Drawable

    public Drawable getDrawable(int res){
        return getDrawable(res, null);
    }

    public Drawable getDrawable(int res, Drawable def){
        try{
            return Global.CONTEXT.getResources().getDrawable(res);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.RESOURCE_DRAWABLE, e);

            return def;
        }
    }


    //endregion
}
