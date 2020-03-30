package com.kalendersoftware.pocketpassword.Components;

import android.content.Context;

import com.hypertrack.hyperlog.LogFormat;

public class CustomLogMessageFormat extends LogFormat {
    public CustomLogMessageFormat(Context context) {
        super(context);
    }

    public String getFormattedLogMessage(String logLevelName, String message, String timeStamp,
                                         String senderName, String osVersion, String deviceUUID) {

        //Custom Log Message Format
        String formattedMessage = timeStamp + " : " + logLevelName + " : " + osVersion +" : " + message;

        return formattedMessage;
    }
}
