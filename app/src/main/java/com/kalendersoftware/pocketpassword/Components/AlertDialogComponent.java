package com.kalendersoftware.pocketpassword.Components;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.kalendersoftware.pocketpassword.Global;
import com.kalendersoftware.pocketpassword.Globals.Helpers;
import com.kalendersoftware.pocketpassword.R;

public class AlertDialogComponent {
    //region Variables

    private AlertDialog.Builder dialogBuilder;
    private String title, message;
    private DialogInterface.OnClickListener listenerCancel, listenerConfirm;

    private String strDialogPositive, strDialogNegative;

    //endregion

    //region Constructors

    public AlertDialogComponent(){}

    public AlertDialogComponent(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public AlertDialogComponent(String title, String message, DialogInterface.OnClickListener listenerCancel, DialogInterface.OnClickListener listenerConfirm) {
        this.title = title;
        this.message = message;
        this.listenerCancel = listenerCancel;
        this.listenerConfirm = listenerConfirm;
    }

    //endregion

    //region Getters & Setters

    //region Setters

    //region Title

    public AlertDialogComponent setTitle(String title) {
        this.title = title;

        return this;
    }

    public AlertDialogComponent setTitle(int title){
        this.title = Helpers.resource.getString(title);

        return this;
    }

    //endregion

    //region Message

    public AlertDialogComponent setMessage(String message) {
        this.message = message;

        return this;
    }

    public AlertDialogComponent setMessage(int message){
        this.message = Helpers.resource.getString(message);

        return this;
    }

    //endregion

    //region Dialog Negative

    public AlertDialogComponent setDialogNegative(String strCustomNegative) {
        return setDialogNegative(strCustomNegative, listenerCancel);
    }

    public AlertDialogComponent setDialogNegative(int resCustomNegative) {
        return setDialogNegative(Helpers.resource.getString(resCustomNegative), listenerCancel);
    }

    public AlertDialogComponent setDialogNegative(int resCustomNegative, DialogInterface.OnClickListener listenerNegative) {
        this.strDialogNegative = Helpers.resource.getString(resCustomNegative);
        this.listenerCancel = listenerNegative;

        return this;
    }

    public AlertDialogComponent setDialogNegative(String strCustomNegative, DialogInterface.OnClickListener listenerNegative) {
        this.strDialogNegative = strCustomNegative;
        this.listenerCancel = listenerNegative;

        return this;
    }

    //endregion

    //region Dialog Positive

    public AlertDialogComponent setDialogPositive(String strCustomPositive) {
        return setDialogPositive(strCustomPositive, this.listenerConfirm);
    }

    public AlertDialogComponent setDialogPositive(int resCustomPositive, DialogInterface.OnClickListener listenerPositive) {
        this.strDialogPositive = Helpers.resource.getString(resCustomPositive);
        this.listenerConfirm = listenerPositive;

        return this;
    }

    public AlertDialogComponent setDialogPositive(String strCustomPositive, DialogInterface.OnClickListener listenerPositive) {
        this.strDialogPositive = strCustomPositive;
        this.listenerConfirm = listenerPositive;

        return this;
    }

    //endregion

    //endregion

    //endregion

    //region Primary Methods

    public void show() {
        dialogBuilder = new AlertDialog.Builder(Global.CONTEXT); //, R.style.ThemeOverlay_MaterialComponents_Dark
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);

        if (listenerCancel != null) {
            dialogBuilder.setNegativeButton(strDialogNegative == null ? Helpers.resource.getString(R.string.dialog_negative) : strDialogNegative, listenerCancel);
        }

        if (listenerConfirm != null) {
            dialogBuilder.setPositiveButton(strDialogPositive == null ? Helpers.resource.getString(R.string.dialog_positive) : strDialogPositive, listenerConfirm);
        }else if(listenerCancel == null){
            //Dismiss
            dialogBuilder.setNegativeButton(Helpers.resource.getString(R.string.dialog_confirm), null);
        }

        dialogBuilder.show();
    }

    //endregion
}
