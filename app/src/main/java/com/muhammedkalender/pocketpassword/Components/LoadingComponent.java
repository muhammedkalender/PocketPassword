package com.muhammedkalender.pocketpassword.Components;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.R;

import java.util.concurrent.CompletableFuture;

//https://stackoverflow.com/a/22879207
public class LoadingComponent {
    private ProgressDialog progressDialog;
    private String defaultMessage;

    public LoadingComponent(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        defaultMessage = Helpers.resource.getString(R.string.loading);
    }

    public void show() {
        try {
            show(defaultMessage);
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.LOADING_SHOW, e);
        }
    }

    public void show(int resource) {
        try {
            show(Helpers.resource.getString(resource));
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.LOADING_SHOW_WITH_RESOURCE, e);
        }
    }

    public void show(String message) {
        try {
            if (!progressDialog.isShowing()) {
                progressDialog.setMessage(message);
                progressDialog.show();
            }
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.LOADING_SHOW_WITH_MESSAGE, e);
        }
    }

    public void hide() {
        try {
            if (progressDialog.isShowing()) {
                progressDialog.hide();
            }
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.LOADING_HIDE, e);
        }
    }
}
