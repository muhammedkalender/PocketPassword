package com.kalendersoftware.pocketpassword.Helpers;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kalendersoftware.pocketpassword.Constants.ErrorCodeConstants;
import com.kalendersoftware.pocketpassword.Global;
import com.kalendersoftware.pocketpassword.Globals.Helpers;

public class SystemHelper {
    //https://stackoverflow.com/a/9494042
    public void hideSoftKeyboard() {
        try {
            View view = Global.ACTIVITY.findViewById(android.R.id.content);

            if (view != null) {
                InputMethodManager imm = (InputMethodManager) Global.ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.SYSTEM_SERVICE_HIDE_SOFT_KEYBOARD, e);
        }
    }
}
