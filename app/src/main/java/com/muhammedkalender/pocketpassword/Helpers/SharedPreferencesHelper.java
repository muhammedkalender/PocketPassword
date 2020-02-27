package com.muhammedkalender.pocketpassword.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;

public class SharedPreferencesHelper {
    //region Variables

    private Context context;

    //endregion

    //region Constructors

    public SharedPreferencesHelper(Context context){
        this.context = context;
    }

    //endregion

    //region Integer

    public int getInteger(String key){
        return getInteger(key, Config.DEFAULT_INTEGER);
    }

    public int getInteger(String key, int def){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);

            return sharedPreferences.getInt(key, def);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_INTEGER_READ, e);

            return def;
        }
    }

    public boolean setInteger(String key, int value){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putInt(key, value);

            return editor.commit();
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_INTEGER_WRITE, e);

            return false;
        }
    }

    //endregion

    //region String

    public String getString(String key){
        return getString(key, Config.DEFAULT_STRING);
    }

    public String getString(String key, String def){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);

            return sharedPreferences.getString(key, def);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_STRING_READ, e);

            return def;
        }
    }

    public boolean setString(String key, String value){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(key, value);

            return editor.commit();
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_STRING_WRITE, e);

            return false;
        }
    }

    //endregion

    //region Long

    public long getLong(String key){
        return getLong(key, Config.DEFAULT_LONG);
    }

    public long getLong(String key, long def){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);

            return sharedPreferences.getLong(key, def);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_LONG_READ, e);

            return def;
        }
    }

    public boolean setLong(String key, long value){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putLong(key, value);

            return editor.commit();
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_LONG_WRITE, e);

            return false;
        }
    }

    //endregion

    //region Boolean

    public boolean getBoolean(String key){
        return getBoolean(key, Config.DEFAULT_BOOLEAN);
    }

    public boolean getBoolean(String key, boolean def){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);

            return sharedPreferences.getBoolean(key, def);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_BOOLEAN_READ, e);

            return def;
        }
    }

    public boolean setBoolean(String key, boolean value){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean(key, value);

            Helpers.logger.info("Ayar Set Edildi : " + key + " to " + value);

            return editor.commit();
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_BOOLEAN_WRITE, e);

            return false;
        }
    }

    //endregion

    //region Float

    public float getFloat(String key){
        return getFloat(key, Config.DEFAULT_FLOAT);
    }

    public float getFloat(String key, float def){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);

            return sharedPreferences.getFloat(key, def);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_FLOAT_READ, e);

            return def;
        }
    }

    public boolean setFloat(String key, float value){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putFloat(key, value);

            return editor.commit();
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_FLOAT_WRITE, e);

            return false;
        }
    }

    //endregion

    //region Double

    public double getDouble(String key){
        return getDouble(key, Config.DEFAULT_DOUBLE);
    }

    public double getDouble(String key, double def){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);

            return sharedPreferences.getFloat(key, (float) def);
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_DOUBLE_READ, e);

            return def;
        }
    }

    public boolean setDouble(String key, double value){
        try{
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(key, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putFloat(key, (float) value);

            return editor.commit();
        }catch (Exception e){
            Helpers.logger.error(ErrorCodeConstants.SHARED_PREFERENCES_DOUBLE_WRITE, e);

            return false;
        }
    }

    //endregion
}
