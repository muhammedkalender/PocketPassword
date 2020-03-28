package com.muhammedkalender.pocketpassword.Helpers;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.MainActivity;

public class ClipboardHelper {
    private String title, data, message;

    public ClipboardHelper(String title, String data) {
        this.title = title;
        this.data = data;
    }

    public ClipboardHelper withMessage(String message){
        this.message = message;

        return this;
    }

    public ClipboardHelper withMessage(int resMessage, Object... messages){
        this.message = Helpers.resource.getString(resMessage, "", messages);

        return this;
    }

    public ClipboardHelper withMessage(int resMessage){
        this.message = Helpers.resource.getString(resMessage);

        return this;
    }

    public static ClipboardHelper build(String title, String data){
        return new ClipboardHelper(title, data);
    }

    public static ClipboardHelper build(int resTitle, String data){
        return new ClipboardHelper(Helpers.resource.getString(resTitle), data);
    }

    public void show(){
        try {
            ((MainActivity) Global.CONTEXT).runOnUiThread(() -> {
                try {
                    ClipboardManager clipboardManager = (ClipboardManager) Global.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText(title, data);
                    clipboardManager.setPrimaryClip(clipData);

                    if(message != null){
                        Toast.makeText(Global.CONTEXT, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Helpers.logger.error(ErrorCodeConstants.CLIPBOARD_MANAGER_THREAD, e);
                }
            });
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.CLIPBOARD_MANAGER_MESSAGE, e);
        }
    }
}
