package com.muhammedkalender.pocketpassword.Components;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.R;

public class SnackbarComponent {
    private View view;
    private String action, message;
    private View.OnClickListener listenerAction;
    private int duration = Snackbar.LENGTH_LONG;

    private int colorText, colorTextAction, colorBackground;

    //region

    private void init(){
        this.colorText = Helpers.resource.getColor(R.color.snackbarText);
        this.colorTextAction = Helpers.resource.getColor(R.color.snackbarActionText);
        this.colorBackground = Helpers.resource.getColor(R.color.snackbarBackground);

        if(this.listenerAction == null){
            this.listenerAction = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            };
        }
    }

    //endregion

    //region Constructor

    public SnackbarComponent(View view, String message){
        this.view = view;
        this.message = message;

        init();
    }

    public SnackbarComponent(View view, int resMessage){
        this.view = view;
        this.message = Helpers.resource.getString(resMessage);

        init();
    }

    public SnackbarComponent(View view, String message, String action) {
        this.view = view;
        this.message = message;
        this.action = action;

        init();
    }

    public SnackbarComponent(View view, int resMessage, int resAction){
        this.view = view;
        this.message = Helpers.resource.getString(resMessage);
        this.action = Helpers.resource.getString(resAction);

        this.listenerAction = null;

        init();
    }

    //endregion

    public void show(){
        Snackbar snackbar = Snackbar.make(view, message, duration);

        snackbar.setActionTextColor(colorTextAction);
        snackbar.setBackgroundTint(colorBackground);
        snackbar.setTextColor(colorText);

        if(!action.equals(null)){
            snackbar.setAction(action, listenerAction);
        }

        snackbar.show();
    }

    //region Duration

    public SnackbarComponent setShort(){
        this.duration = Snackbar.LENGTH_SHORT;

        return this;
    }

    public SnackbarComponent setLong(){
        this.duration = Snackbar.LENGTH_SHORT;

        return this;
    }

    public SnackbarComponent setIdenfinite(){
        this.duration = Snackbar.LENGTH_INDEFINITE;

        return this;
    }

    //endregion
}