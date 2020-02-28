package com.muhammedkalender.pocketpassword.Pages;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.CryptHelper;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;
import com.muhammedkalender.pocketpassword.Objects.ColorObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;
import com.muhammedkalender.pocketpassword.R;
import com.muhammedkalender.pocketpassword.ui.main.SectionsPagerAdapter;

public class SettingsPage extends PageAbstract implements PageInterface {
    private SwitchMaterial switchOnlyLogin, switchHideView, switchDisableErrorLog, switchDisableInfoLog;
    private LinearLayout llLogin;

    @Override
    public void initialize(View viewRoot) {
        Global.PAGE_SETTINGS = this;

        this.viewRoot = viewRoot;

        switchOnlyLogin = this.viewRoot.findViewById(R.id.switchOnlyLogin);
        switchOnlyLogin.setChecked(Config.CONFIG_ONLY_LOGIN);
        switchOnlyLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(Helpers.config.setBoolean("only_login", isChecked)){
                    Config.CONFIG_ONLY_LOGIN = isChecked;

                    buttonView.setChecked(isChecked);
                }else{
                    buttonView.setChecked(!isChecked);
                }
            }
        });

        switchHideView = this.viewRoot.findViewById(R.id.switchHideView);
        switchHideView.setChecked(Config.CONFIG_HIDE_VIEW);
        switchHideView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(Helpers.config.setBoolean("hide_view", isChecked)){

                    Config.CONFIG_HIDE_VIEW = isChecked;

                    buttonView.setChecked(isChecked);
                }else{
                    buttonView.setChecked(!isChecked);
                }
            }
        });

        switchDisableErrorLog = this.viewRoot.findViewById(R.id.switchDisableErrorLog);
        switchDisableErrorLog.setChecked(Config.CONFIG_ENABLE_ERROR_LOG);
        switchDisableErrorLog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(Helpers.config.setBoolean("enable_error_log", isChecked)){

                    Config.CONFIG_ENABLE_ERROR_LOG = isChecked;

                    buttonView.setChecked(isChecked);
                }else{
                    buttonView.setChecked(!isChecked);
                }
            }
        });

        switchDisableInfoLog = this.viewRoot.findViewById(R.id.switchDisableInfoLog);
        switchDisableInfoLog.setChecked(Config.CONFIG_ENABLE_INFO_LOG);
        switchDisableInfoLog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(Helpers.config.setBoolean("enable_info_log", isChecked)){

                    Config.CONFIG_ENABLE_INFO_LOG = isChecked;

                    buttonView.setChecked(isChecked);
                }else{
                    buttonView.setChecked(!isChecked);
                }
            }
        });

        ((TextInputEditText)this.viewRoot.findViewById(R.id.etPassword)).setText("");
        ((TextInputLayout)this.viewRoot.findViewById(R.id.tilPassword)).setErrorEnabled(false);

        this.viewRoot.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.loading.show();

                new Thread(() -> {
                    Global.PASSWORD = ((TextInputEditText)viewRoot.findViewById(R.id.etPassword)).getText().toString();

                    String confirmString = Helpers.config.getString("confirm_password");

                    ResultObject resultDecryptConfirmString = Helpers.aes.decrypt(confirmString, Global.PASSWORD);

                    if (resultDecryptConfirmString.isFailure()) {
                        ((Activity)Global.CONTEXT).runOnUiThread(() -> {
                            ((TextInputEditText)viewRoot.findViewById(R.id.etPassword)).setText(null);
                            ((TextInputLayout)viewRoot.findViewById(R.id.tilPassword)).setError(Helpers.resource.getString(R.string.password_wrong_1));

                            Helpers.loading.hide();
                        });

                        return;
                    }

                    Helpers.crypt = CryptHelper.buildDefault();

                    ResultObject resultDecryptRSAConfirmString = Helpers.crypt.decrypt((String) resultDecryptConfirmString.getData(), Helpers.crypt.getPublicKey());

                    ((Activity)Global.CONTEXT).runOnUiThread(() -> {
                        if (resultDecryptRSAConfirmString.isFailure()) {
                            ((TextInputEditText)viewRoot.findViewById(R.id.etPassword)).setText(null);
                            ((TextInputLayout)viewRoot.findViewById(R.id.tilPassword)).setError(Helpers.resource.getString(R.string.password_wrong));

                            Helpers.loading.hide();

                            return;
                        }

                        viewRoot.findViewById(R.id.llPassword).setVisibility(View.INVISIBLE);

                        Helpers.system.hideSoftKeyboard();

                        Helpers.loading.hide();
                    });
                }).start();
            }
        });

        ScrollView svSettings = this.viewRoot.findViewById(R.id.svSettings);

        svSettings.post(() -> {
            svSettings.setScrollX(0);
        });
    }

    @Override
    public View getView() {
        return viewRoot;
    }
}
