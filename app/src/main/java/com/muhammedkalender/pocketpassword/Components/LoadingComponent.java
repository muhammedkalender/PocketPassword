package com.muhammedkalender.pocketpassword.Components;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.R;

//https://stackoverflow.com/a/22879207
public class LoadingComponent {
    //region Variables

    private RelativeLayout rlLoading;
    private TextView tvLoading;
    private ProgressBar pbLoading;

    private String defaultMessage;

    public boolean isShowing = false;

    //endregion

    //region Constructors

    public LoadingComponent(RelativeLayout rlLoading) {
        this.rlLoading = rlLoading;
        this.tvLoading = rlLoading.findViewById(R.id.tvLoading);
        this.pbLoading = rlLoading.findViewById(R.id.pbLoading);
    }

    //endregion

    //region Primary Methods

    public void show() {
        try {
            show(defaultMessage);
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.LOADING_SHOW, e);
        }
    }

    public void showDirect() {
        show(defaultMessage, true, true);
    }

    public void showDelayedWithKeyboard() {
        show(defaultMessage, false, false);
    }

    public void show(int resource) {
        try {
            show(Helpers.resource.getString(resource), true, true);
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.LOADING_SHOW_WITH_RESOURCE, e);
        }
    }

    public void show(boolean hideKeyboard) {
        show(defaultMessage, hideKeyboard, true);
    }

    public void show(String message) {
        show(message, true, true);
    }

    public void show(int resource, boolean hideKeyboard) {
        show(Helpers.resource.getString(resource), hideKeyboard, true);
    }

    public void show(int resource, boolean hideKeyboard, boolean showDirect) {
        show(Helpers.resource.getString(resource), hideKeyboard, showDirect);
    }

    public void show(String message, boolean hideKeyboard, boolean showDirect) {
        if (!showDirect && isShowing) {
            return;
        }

        isShowing = true;

        tvLoading.post(() -> {
            tvLoading.setText(message);
        });

        try {
            if (showDirect) {
                rlLoading.post(() -> {
                    if (rlLoading.getVisibility() != View.VISIBLE) {
                        rlLoading.setVisibility(View.VISIBLE);

                        if (hideKeyboard) {
                            Helpers.system.hideSoftKeyboard();
                        }
                    }
                });
            } else {
                rlLoading.postDelayed(() -> {
                    if (rlLoading.getVisibility() != View.VISIBLE) {
                        if (isShowing) {
                            rlLoading.setVisibility(View.VISIBLE);
                        }

                        if (hideKeyboard) {
                            Helpers.system.hideSoftKeyboard();
                        }
                    }
                }, Config.LOADING_SHOW_DELAY);
            }
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.LOADING_SHOW_WITH_MESSAGE, e);
        }
    }

    public void showDelayed() {
        show(defaultMessage, true, false);
    }

    public void hide() {
        if (!isShowing) {
            return;
        }

        isShowing = false;

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

    //endregion
}
