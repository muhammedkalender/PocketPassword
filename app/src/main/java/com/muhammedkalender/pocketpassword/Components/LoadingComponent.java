package com.muhammedkalender.pocketpassword.Components;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.R;

import java.util.concurrent.CompletableFuture;

//https://stackoverflow.com/a/22879207
public class LoadingComponent {
    private RelativeLayout rlLoading;
    private TextView tvLoading;
    private ProgressBar pbLoading;

    private String defaultMessage;

    public LoadingComponent(RelativeLayout rlLoading) {
        this.rlLoading = rlLoading;
        this.tvLoading = rlLoading.findViewById(R.id.tvLoading);
        this.pbLoading = rlLoading.findViewById(R.id.pbLoading);
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
            rlLoading.post(() -> {
                if (rlLoading.getVisibility() != View.VISIBLE) {
                    rlLoading.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.LOADING_SHOW_WITH_MESSAGE, e);
        }
    }

    public void hide() {
        try {
            rlLoading.post(() -> {
                if (rlLoading.getVisibility() == View.VISIBLE) {
                    rlLoading.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.LOADING_HIDE, e);
        }
    }
}
