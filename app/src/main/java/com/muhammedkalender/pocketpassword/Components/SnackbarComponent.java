package com.muhammedkalender.pocketpassword.Components;

import android.app.Activity;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.R;

public class SnackbarComponent {
    //region UI Components

    private View view;
    private String action, message;
    private View.OnClickListener listenerAction;

    //endregion

    //region Variables

    private int duration = Snackbar.LENGTH_LONG;
    private int colorText, colorTextAction, colorBackground;

    //endregion

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

    public SnackbarComponent(View view, int resMessage, String action){
        this.view = view;
        this.message = Helpers.resource.getString(resMessage);
        this.action = action;

        this.listenerAction = null;

        init();
    }

    public SnackbarComponent(View view, String message, int resAction){
        this.view = view;
        this.message = message;
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

        if(action != null){
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

    //region Multi Thread

    public static void direct(View view, String message){
        ((Activity) Global.CONTEXT).runOnUiThread(() -> {
            new SnackbarComponent(view, message).show();
        });
    }

    public static void direct(View view, int resMessage){
        ((Activity) Global.CONTEXT).runOnUiThread(() -> {
            new SnackbarComponent(view, resMessage).show();
        });
    }

    public static void direct(View view, int resMessage, int resAction){
        ((Activity) Global.CONTEXT).runOnUiThread(() -> {
            new SnackbarComponent(view, resMessage, resAction).show();
        });
    }

    public static void direct(View view, String message, String action){
        ((Activity) Global.CONTEXT).runOnUiThread(() -> {
            new SnackbarComponent(view, message, action).show();
        });
    }

    public static void direct(View view,  int resMessage, String action){
        ((Activity) Global.CONTEXT).runOnUiThread(() -> {
            new SnackbarComponent(view, resMessage, action).show();
        });
    }

    public static void direct(View view,  String message, int resAction){
        ((Activity) Global.CONTEXT).runOnUiThread(() -> {
            new SnackbarComponent(view, message, resAction).show();
        });
    }

    //endregion
}
