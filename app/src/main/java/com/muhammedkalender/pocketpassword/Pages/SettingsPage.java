package com.muhammedkalender.pocketpassword.Pages;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;
import com.muhammedkalender.pocketpassword.R;

public class SettingsPage extends PageAbstract implements PageInterface {

    private SwitchMaterial switchOnlyLogin, switchHideView;


    @Override
    public void initialize(View viewRoot) {
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
    }

    @Override
    public View getView() {
        return viewRoot;
    }
}
