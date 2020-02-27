package com.muhammedkalender.pocketpassword.Pages.Password;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Components.ColorPickerComponent;
import com.muhammedkalender.pocketpassword.Components.SnackbarComponent;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Constants.InfoCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.CryptHelper;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;
import com.muhammedkalender.pocketpassword.R;

public class PasswordPage extends PageAbstract implements PageInterface {
    public MaterialButton btnSave = null;

    public TextInputLayout tilName = null;
    public TextInputEditText etName = null;

    public TextInputLayout tilAccount = null;
    public TextInputEditText etAccount = null;

    public TextInputLayout tilPassword = null;
    public TextInputEditText etPassword = null;

    public MaterialButton
            btnClipboard = null,
            btnClipboardAccount = null;

    private HorizontalScrollView hsvColors = null;
    private LinearLayout llColors = null;

    private ColorPickerComponent colorPickerComponent;

    @Override
    public void initialize(View viewRoot) {
        this.viewRoot = viewRoot;
        this.initialized = true;

        this.tilName = this.viewRoot.findViewById(R.id.tilName);
        this.etName = this.viewRoot.findViewById(R.id.etName);
        this.tilAccount = this.viewRoot.findViewById(R.id.tilAccount);
        this.etAccount = this.viewRoot.findViewById(R.id.etAccount);
        this.tilPassword = this.viewRoot.findViewById(R.id.tilPassword);
        this.etPassword = this.viewRoot.findViewById(R.id.etPassword);
        this.btnSave = this.viewRoot.findViewById(R.id.btnSave);
        this.btnClipboard = this.viewRoot.findViewById(R.id.btnClipboard);
        this.hsvColors = this.viewRoot.findViewById(R.id.hsvColors);
        this.llColors = this.viewRoot.findViewById(R.id.llColors);
        this.btnClipboardAccount = this.viewRoot.findViewById(R.id.btnClipboardAccount);

        PasswordModel passwordModel = Helpers.list.findByGlobal();

        Helpers.logger.info(InfoCodeConstants.PASSWORD_FILL_VIEW, passwordModel.getName());

        colorPickerComponent = new ColorPickerComponent(Global.VIEW_GROUP, passwordModel.getColor());

        colorPickerComponent.fillLayout(viewRoot.findViewById(R.id.llColors));

        load(passwordModel);
    }

    @Override
    public View getView() {
        return this.viewRoot;
    }

    public void load(PasswordModel passwordModel) {

        passwordModel.decrypt();

        this.etName.setText(passwordModel.getName());
        this.etAccount.setText(passwordModel.getAccount());
        this.etPassword.setText(passwordModel.getPassword());
        this.tilPassword.setPasswordVisibilityToggleEnabled(!Config.CONFIG_HIDE_VIEW);

        this.colorPickerComponent.refresh(passwordModel.getColor());

        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.loading.show();

                String name = etName.getText().toString();
                String account = etAccount.getText().toString();
                String password = etPassword.getText().toString();

                String oldName = passwordModel.getName();

                passwordModel.setName(name);
                passwordModel.setAccount(account);
                passwordModel.setPassword(password);
                passwordModel.setColor(-1);

                if (etName.getText().toString() == null || etName.getText().toString().length() == 0) {
                    tilName.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.input_name)));

                    Helpers.loading.hide();
                    return;
                } else {
                    if (name.length() > Helpers.resource.getInt(R.integer.name_max_length)) {
                        tilName.setError(Helpers.resource.getString(R.string.max_length, "", Helpers.resource.getString(R.string.input_name), Helpers.resource.getInt(R.integer.name_max_length)));

                        Helpers.loading.hide();
                        return;
                    } else if (name.length() < Helpers.resource.getInt(R.integer.name_min_length)) {
                        tilName.setError(Helpers.resource.getString(R.string.min_length, "", Helpers.resource.getString(R.string.input_name), Helpers.resource.getInt(R.integer.name_min_length)));

                        Helpers.loading.hide();
                        return;
                    } else if (!oldName.equals(name) && passwordModel.checkDuplicate(name)) {
                        tilName.setError(Helpers.resource.getString(R.string.already_used, "", name));

                        Helpers.loading.hide();
                        return;
                    } else {
                        tilName.setErrorEnabled(false);
                    }
                }

                if (etAccount.getText().toString() == null || etAccount.getText().toString().length() == 0) {
                    tilAccount.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.input_account)));

                    Helpers.loading.hide();
                    return;
                } else {
                    if (name.length() > Helpers.resource.getInt(R.integer.account_max_length)) {
                        tilAccount.setError(Helpers.resource.getString(R.string.max_length, "", Helpers.resource.getString(R.string.account_name), Helpers.resource.getInt(R.integer.account_max_length)));

                        Helpers.loading.hide();
                        return;
                    } else if (name.length() < Helpers.resource.getInt(R.integer.account_min_length)) {
                        tilAccount.setError(Helpers.resource.getString(R.string.min_length, "", Helpers.resource.getString(R.string.account_name), Helpers.resource.getInt(R.integer.account_min_length)));

                        Helpers.loading.hide();
                        return;
                    } else {
                        tilAccount.setErrorEnabled(false);
                    }
                }

                if (etPassword.getText().toString() == null || etPassword.getText().toString().length() == 0) {
                    tilPassword.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.input_password)));

                    Helpers.loading.hide();
                    return;
                } else {
                    if (password.length() > Helpers.resource.getInt(R.integer.password_max_length)) {
                        tilPassword.setError(Helpers.resource.getString(R.string.max_length, "", Helpers.resource.getString(R.string.input_password)));

                        Helpers.loading.hide();
                        return;
                    } else if (password.length() < Helpers.resource.getInt(R.integer.password_min_length)) {
                        tilPassword.setError(Helpers.resource.getString(R.string.min_length, "", Helpers.resource.getString(R.string.input_password)));

                        Helpers.loading.hide();
                        return;
                    } else {
                        tilPassword.setErrorEnabled(false);
                    }
                }

                //todo color
                passwordModel.setName(name);
                passwordModel.setAccount(account);
                passwordModel.setPassword(password);
                passwordModel.setColor(colorPickerComponent.getColor());

                passwordModel.encrypt();

                ResultObject update = passwordModel.update();

                passwordModel.decrypt();

                if (update.isSuccess()) {
                    Global.LIST_PASSWORDS.set(Global.CURRENT_PASSWORD_MODEL_INDEX, passwordModel);
                    Helpers.list.findAndUpdate(passwordModel);
                    Global.LIST_PASSWORDS_SOLID.set(Helpers.list.findIndexFromSolid(passwordModel), passwordModel);
                    Global.PASSWORD_ADAPTER.notifyDataSetChanged();
                    Global.TAB_LAYOUT.getTabAt(Config.TAB_PASSWORD_INDEX).setText(passwordModel.getName());
                    Global.TAB_LAYOUT.getTabAt(Config.TAB_PASSWORD_INDEX).setContentDescription(passwordModel.getName());

                    SnackbarComponent snackbarComponent = new SnackbarComponent(viewRoot, R.string.success_update_password, R.string.action_ok);
                    snackbarComponent.show();
                } else {
                    SnackbarComponent snackbarComponent = new SnackbarComponent(viewRoot, R.string.failure_update_password, R.string.action_ok);
                    snackbarComponent.show();
                }

                Helpers.system.hideSoftKeyboard();

                Helpers.loading.hide();
            }
        });

        btnClipboard.setOnClickListener(v -> {
            try {
                ClipboardManager clipboard = (ClipboardManager) Global.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(Helpers.resource.getString(R.string.clipboard_title), passwordModel.getDecryptedPassword());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(Global.CONTEXT, R.string.password_clipboard, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Helpers.logger.error(ErrorCodeConstants.CLIPBOARD_PASSWORD, e);

                Toast.makeText(Global.CONTEXT, R.string.failure_password_clipboard, Toast.LENGTH_SHORT).show();
            }
        });

        btnClipboardAccount.setOnClickListener(v -> {
            try {
                ClipboardManager clipboardManager = (ClipboardManager) Global.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(Helpers.resource.getString(R.string.account_name), passwordModel.getDecryptedAccount());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(Global.CONTEXT, R.string.account_clipboard, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Helpers.logger.error(ErrorCodeConstants.CLIPBOARD_ACCOUNT, e);

                Toast.makeText(Global.CONTEXT, R.string.failure_account_clipboard, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
