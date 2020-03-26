package com.muhammedkalender.pocketpassword.Pages;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.hypertrack.hyperlog.HyperLog;
import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;
import com.muhammedkalender.pocketpassword.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CompanyPage extends PageAbstract implements PageInterface {
    @Override
    public void initialize(View viewRoot) {
        //todo
        Global.PAGE_COMPANY = this;

        this.viewRoot = viewRoot;

        //region Contact Us

        this.viewRoot.findViewById(R.id.btnContactUS).setOnClickListener(v -> {
            Helpers.loading.show();

            //https://stackoverflow.com/questions/8701634/send-email-intent
            Uri uri = Uri.parse("mailto:")
                    .buildUpon()
                    .appendQueryParameter("subject", Helpers.resource.getString(R.string.mail_subject))
                    .build();

            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            //https://stackoverflow.com/a/9097251
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Helpers.resource.getString(R.string.email_contact)});
            Global.CONTEXT.startActivity(Intent.createChooser(intent, Helpers.resource.getString(R.string.mail_chooser)));

            Helpers.loading.hide();
        });

        //endregion

        //region Send Error Log

        this.viewRoot.findViewById(R.id.btnSendErrorLog).setOnClickListener(v -> {
            try {
                Helpers.loading.show();

                File file = HyperLog.getDeviceLogsInFile(Global.CONTEXT);

                StringBuilder stringBuilder = new StringBuilder();

                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                bufferedReader.close();

                //https://stackoverflow.com/questions/8701634/send-email-intent
                Uri uri = Uri.parse("mailto:")
                        .buildUpon()
                        .appendQueryParameter("subject", Helpers.resource.getString(R.string.email_log_error_subject))
                        .appendQueryParameter("body", stringBuilder.toString())
                        .build();

                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

                //https://stackoverflow.com/a/9097251
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Helpers.resource.getString(R.string.email_log_error)});
                intent.setData(uri);
                Global.CONTEXT.startActivity(Intent.createChooser(intent, Helpers.resource.getString(R.string.mail_chooser)));

                Helpers.loading.hide();
            } catch (Exception e) {
                Helpers.logger.error(ErrorCodeConstants.SETTINGS_SEND_ERROR_LOG, e);
            }
        });

        //endregion
    }

    @Override
    public View getView() {
        //todo
        return this.viewRoot;
    }

    @Override
    public void refresh() {
//todo
        this.viewRoot.findViewById(R.id.svCompany).post(() ->  {
            this.viewRoot.findViewById(R.id.svCompany).setScrollY(0);
        });
    }
}
