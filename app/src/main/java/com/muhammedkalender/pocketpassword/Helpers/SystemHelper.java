package com.muhammedkalender.pocketpassword.Helpers;

import android.app.Activity;
import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputMethodManager;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;

public class SystemHelper {
    //https://stackoverflow.com/a/9494042
    public void hideSoftKeyboard() {
        try {
            if (((InputMethodManager) Global.CONTEXT.getSystemService(Activity.INPUT_METHOD_SERVICE)).isAcceptingText()) {
                ((InputMethodManager) Global.CONTEXT.getSystemService(Activity.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.SYSTEM_SERVICE_HIDE_SOFT_KEYBOARD, e);
        }
    }
}
