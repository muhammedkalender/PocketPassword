package com.muhammedkalender.pocketpassword.Pages;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hypertrack.hyperlog.HyperLog;
import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.AESHelper;
import com.muhammedkalender.pocketpassword.Helpers.CryptHelper;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;
import com.muhammedkalender.pocketpassword.Objects.ColorObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;
import com.muhammedkalender.pocketpassword.R;
import com.muhammedkalender.pocketpassword.ui.main.SectionsPagerAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.crypto.spec.SecretKeySpec;

public class SettingsPage extends PageAbstract implements PageInterface {
    private SwitchMaterial switchOnlyLogin, switchHideView, switchDisableErrorLog, switchDisableInfoLog;
    private LinearLayout llLogin;

    @Override
    public void initialize(View viewRoot) {
        Global.PAGE_SETTINGS = this;

        this.viewRoot = viewRoot;


        //region Switches

        switchOnlyLogin = this.viewRoot.findViewById(R.id.switchOnlyLogin);
        switchOnlyLogin.setChecked(Config.CONFIG_ONLY_LOGIN);
        switchOnlyLogin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Helpers.config.setBoolean("only_login", isChecked)) {
                Config.CONFIG_ONLY_LOGIN = isChecked;

                buttonView.setChecked(isChecked);
            } else {
                buttonView.setChecked(!isChecked);
            }
        });

        switchHideView = this.viewRoot.findViewById(R.id.switchHideView);
        switchHideView.setChecked(Config.CONFIG_HIDE_VIEW);
        switchHideView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Helpers.config.setBoolean("hide_view", isChecked)) {

                Config.CONFIG_HIDE_VIEW = isChecked;

                buttonView.setChecked(isChecked);
            } else {
                buttonView.setChecked(!isChecked);
            }
        });

        switchDisableErrorLog = this.viewRoot.findViewById(R.id.switchDisableErrorLog);
        switchDisableErrorLog.setChecked(Config.CONFIG_ENABLE_ERROR_LOG);
        switchDisableErrorLog.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Helpers.config.setBoolean("enable_error_log", isChecked)) {

                Config.CONFIG_ENABLE_ERROR_LOG = isChecked;

                buttonView.setChecked(isChecked);
            } else {
                buttonView.setChecked(!isChecked);
            }
        });

        switchDisableInfoLog = this.viewRoot.findViewById(R.id.switchDisableInfoLog);
        switchDisableInfoLog.setChecked(Config.CONFIG_ENABLE_INFO_LOG);
        switchDisableInfoLog.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Helpers.config.setBoolean("enable_info_log", isChecked)) {

                Config.CONFIG_ENABLE_INFO_LOG = isChecked;

                buttonView.setChecked(isChecked);
            } else {
                buttonView.setChecked(!isChecked);
            }
        });

        //endregion

        this.viewRoot.findViewById(R.id.btnContactUS).setOnClickListener(v -> {
            Helpers.loading.show();

            //https://stackoverflow.com/questions/8701634/send-email-intent
            Uri uri = Uri.parse("mailto:") //TODO
                    .buildUpon()
                    .appendQueryParameter("subject", Helpers.resource.getString(R.string.mail_subject))
                    .build();

            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            //https://stackoverflow.com/a/9097251
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"email@email.com"}); //todo
            Global.CONTEXT.startActivity(Intent.createChooser(intent, Helpers.resource.getString(R.string.mail_chooser)));

            Helpers.loading.hide();
        });

        ((TextInputEditText) this.viewRoot.findViewById(R.id.etPassword)).setText("");
        ((TextInputLayout) this.viewRoot.findViewById(R.id.tilPassword)).setErrorEnabled(false);

        this.viewRoot.findViewById(R.id.btnLogin).setOnClickListener(v -> {
            Helpers.loading.show();

            new Thread(() -> {
                Global.PASSWORD = ((TextInputEditText) viewRoot.findViewById(R.id.etPassword)).getText().toString();

                String confirmString = Helpers.config.getString("confirm_password");

                ResultObject resultDecryptConfirmString = Helpers.aes.decrypt(confirmString, Global.PASSWORD);

                if (resultDecryptConfirmString.isFailure()) {
                    ((Activity) Global.CONTEXT).runOnUiThread(() -> {
                        ((TextInputEditText) viewRoot.findViewById(R.id.etPassword)).setText(null);
                        ((TextInputLayout) viewRoot.findViewById(R.id.tilPassword)).setError(Helpers.resource.getString(R.string.password_wrong_1));

                        Helpers.loading.hide();
                    });

                    return;
                }

                Helpers.crypt = CryptHelper.buildDefault();

                ResultObject resultDecryptRSAConfirmString = Helpers.crypt.decrypt((String) resultDecryptConfirmString.getData(), Helpers.crypt.getPublicKey());

                ((Activity) Global.CONTEXT).runOnUiThread(() -> {
                    if (resultDecryptRSAConfirmString.isFailure()) {
                        ((TextInputEditText) viewRoot.findViewById(R.id.etPassword)).setText(null);
                        ((TextInputLayout) viewRoot.findViewById(R.id.tilPassword)).setError(Helpers.resource.getString(R.string.password_wrong));

                        Helpers.loading.hide();

                        return;
                    }

                    viewRoot.findViewById(R.id.llPassword).setVisibility(View.INVISIBLE);

                    Helpers.system.hideSoftKeyboard();

                    Helpers.loading.hide();
                });
            }).start();
        });

        ScrollView svSettings = this.viewRoot.findViewById(R.id.svSettings);

        svSettings.post(() -> {
            svSettings.setScrollX(0);
        });

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
                Uri uri = Uri.parse("mailto:") //TODO
                        .buildUpon()
                        .appendQueryParameter("subject", Helpers.resource.getString(R.string.mail_subject))
                        .appendQueryParameter("body", stringBuilder.toString())
                        .build();

                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

                //https://stackoverflow.com/a/9097251
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"email@email.com"}); //todo
                intent.setData(uri);
                Global.CONTEXT.startActivity(Intent.createChooser(intent, Helpers.resource.getString(R.string.mail_chooser)));

                Helpers.loading.hide();
            } catch (Exception e) {
                Helpers.logger.error(ErrorCodeConstants.SETTINGS_SEND_ERROR_LOG, e);
            }
        });

        //endregion

        //region Change Password

        this.viewRoot.findViewById(R.id.btnChangePassword).setOnClickListener(v -> {
            try {
                CryptHelper cryptHelper = new CryptHelper();
                cryptHelper.generateKeys();

                Helpers.logger.info(String.valueOf(cryptHelper.keyToString(cryptHelper.getPrivateKey()).getData()));
                Helpers.logger.info(String.valueOf(cryptHelper.keyToString(cryptHelper.getPublicKey()).getData()));

                AESHelper aesHelper = new AESHelper();

                String secret = "TESTSifre_-_";
                SecretKeySpec aesSecret = aesHelper.secretToKey(secret); //TODO
                Helpers.logger.info(String.valueOf(aesHelper.encrypt("aa", secret).getData()));

                String encryptedPrivateKey = (String) aesHelper.encrypt(
                        ((String) cryptHelper.keyToString(cryptHelper.getPrivateKey()).getData())
                        , secret).getData();

                String encryptedPublicKey = (String) aesHelper.encrypt(
                        ((String) cryptHelper.keyToString(cryptHelper.getPublicKey()).getData())
                        , secret).getData();

                CryptHelper cryptHelperDecoded = new CryptHelper();
                cryptHelper.setPublicKey((String) aesHelper.decrypt(encryptedPublicKey, secret).getData());
                cryptHelper.setPrivateKey((String) aesHelper.decrypt(encryptedPrivateKey, secret).getData());

                String data = "oldumu";

                Helpers.logger.info(-1111, cryptHelperDecoded.quickEncrypt(data));

                Helpers.logger.info(-1111, cryptHelperDecoded.quickDecrypt(cryptHelperDecoded.quickEncrypt(data)));

            } catch (Exception e) {
                Helpers.logger.error(ErrorCodeConstants.SETTINGS_CHANGE_PASSWORD, e);
            }
        });

        //endregion
    }

    @Override
    public View getView() {
        return viewRoot;
    }
}
