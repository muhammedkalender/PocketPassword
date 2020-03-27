package com.muhammedkalender.pocketpassword.Pages.Password;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
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
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;
import com.muhammedkalender.pocketpassword.Models.CategoryModel;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;
import com.muhammedkalender.pocketpassword.R;

import java.util.List;

public class PasswordPage extends PageAbstract implements PageInterface {
    //region UI Components

    public MaterialButton btnSave = null;

    public TextInputLayout tilName = null;
    public TextInputEditText etName = null;

    public TextInputLayout tilAccount = null;
    public TextInputEditText etAccount = null;

    public TextInputLayout tilPassword = null;
    public TextInputEditText etPassword = null;

    public TextInputLayout tilCategory = null;
    public TextInputEditText etCategory = null;

    public MaterialButton
            btnClipboard = null,
            btnClipboardAccount = null,
            btnDeletePassword = null;

    private HorizontalScrollView hsvColors = null;
    private LinearLayout llColors = null;

    private ColorPickerComponent colorPickerComponent;

    //endregion

    //region Variables

    public int selectedCategoryIndex = 0;
    public int selectedCategoryId = 0;

    private List<CategoryModel> listCategory;

    ArrayAdapter arrayAdapter;

    //endregion

    //region Initialize

    @Override
    public void initialize(View viewRoot) {
        //region Declare UI Components

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
        this.tilCategory = this.viewRoot.findViewById(R.id.tilCategory);
        this.etCategory = this.viewRoot.findViewById(R.id.etCategory);
        this.btnDeletePassword = this.viewRoot.findViewById(R.id.btnDeletePassword);

        //endregion

        final PasswordModel passwordModel = Helpers.list.findBySelectedId();

        Helpers.logger.info(InfoCodeConstants.PASSWORD_FILL_VIEW, passwordModel.getName());

        colorPickerComponent = new ColorPickerComponent(Global.VIEW_GROUP, passwordModel.getColor(), btnSave);

        colorPickerComponent.fillLayout(viewRoot.findViewById(R.id.llColors));

        CategoryModel categoryModel = new CategoryModel();
        listCategory = categoryModel.selectActive(false);
        arrayAdapter = new ArrayAdapter<>(Global.CONTEXT, android.R.layout.select_dialog_singlechoice);

        for (int i = 0; i < listCategory.size(); i++) {
            if (listCategory.get(i).getId() == passwordModel.getCategoryID()) {
                selectedCategoryIndex = i;
                selectedCategoryId = passwordModel.getCategoryID();
                etCategory.setText(listCategory.get(selectedCategoryIndex).getName());
            }

            arrayAdapter.add(listCategory.get(i).getName());
        }

        this.tilCategory.setOnClickListener(v -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(Global.CONTEXT);
            builderSingle.setIcon(R.drawable.ic_format_list_bulleted_24dp);
            builderSingle.setTitle(R.string.select_category);

            builderSingle.setSingleChoiceItems(arrayAdapter, selectedCategoryIndex, (dialog, which) -> {
                dialog.dismiss();

                selectedCategoryId = listCategory.get(which).getId();
                selectedCategoryIndex = which;

                etCategory.setText(listCategory.get(which).getName());
            });

            builderSingle.setNegativeButton(R.string.select_category_cancel, (dialog, which) -> dialog.dismiss());

            builderSingle.show();
        });

        this.etCategory.setOnClickListener(v -> {
            this.tilCategory.callOnClick();
        });

        this.btnDeletePassword.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(Global.CONTEXT);
            alert.setTitle(R.string.dialog_delete_password_title);
            alert.setMessage(R.string.dialog_delete_password);
            alert.setPositiveButton(R.string.dialog_confirm, (dialog, which) -> {
                Helpers.list.findAndDeleteBySelectedId();
                Helpers.list.findAndDeleteBySelectedIdFromTempList();

                Global.PASSWORD_ADAPTER.notifyDataSetChanged();

                Global.TAB_LAYOUT.removeTabAt(Config.TAB_PASSWORD_INDEX);

                SnackbarComponent snackbarComponent = new SnackbarComponent(viewRoot, R.string.success_delete_password, R.string.action_ok);
                snackbarComponent.show();

                Global.SECTION_PAGER_ADAPTER.delete(Config.TAB_PASSWORD_INDEX);
                Global.SECTION_PAGER_ADAPTER.notifyDataSetChanged();

                Global.PAGE_HOME.filter("");

                passwordModel.delete();

                dialog.dismiss();
            });

            alert.setNeutralButton(R.string.dialog_cancel, (dialog, which) -> dialog.dismiss());

            alert.show();
        });

        load(passwordModel);
    }

    @Override
    public void refresh() {
        this.tilCategory.setErrorEnabled(false);
        this.tilName.setErrorEnabled(false);
        this.tilAccount.setErrorEnabled(false);
        this.tilPassword.setErrorEnabled(false);
        this.viewRoot.findViewById(R.id.svPassword).post(() -> this.viewRoot.findViewById(R.id.svPassword).setScrollY(0));
    }

    //endregion

    //region Getters & Setters

    //region Getters

    @Override
    public View getView() {
        return this.viewRoot;
    }

    //endregion

    //endregion

    //region Loaders

    public void load(PasswordModel passwordModel) {
        passwordModel.decrypt();

        this.hsvColors.post(() -> {
            this.hsvColors.setScrollX(0);
        });

        ScrollView svPassword = this.viewRoot.findViewById(R.id.svPassword);

        svPassword.post(() -> {
            svPassword.setScrollY(0);
        });

        this.etName.setText(passwordModel.getName());
        this.etAccount.setText(passwordModel.getAccount());
        this.etPassword.setText(passwordModel.getPassword());
        this.etPassword.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);

        this.colorPickerComponent.refresh(passwordModel.getColor());

        for (int i = 0; i < listCategory.size(); i++) {
            if (listCategory.get(i).getId() == passwordModel.getCategoryID()) {
                selectedCategoryIndex = i;
                selectedCategoryId = passwordModel.getCategoryID();
                etCategory.setText(listCategory.get(selectedCategoryIndex).getName());
            }
        }

        this.btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String account = etAccount.getText().toString();
            String password = etPassword.getText().toString();

            String oldName = passwordModel.getName();

            int category = listCategory.get(selectedCategoryIndex).getId();

            if (etName.getText().toString() == null || etName.getText().toString().length() == 0) {
                tilName.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.input_name_edit)));

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
                } else if (!oldName.equals(name) && passwordModel.checkActiveDuplicate(name)) {
                    tilName.setError(Helpers.resource.getString(R.string.already_used, "", name));

                    Helpers.loading.hide();
                    return;
                } else {
                    tilName.setErrorEnabled(false);
                }
            }

            if (etAccount.getText().toString() == null || etAccount.getText().toString().length() == 0) {
                tilAccount.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.input_account_edit)));

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

            Helpers.loading.showDelayed();

            new Thread(() -> {
                passwordModel.setName(name);
                passwordModel.setAccount(account);
                passwordModel.setPassword(password);
                passwordModel.setColor(colorPickerComponent.getColor());
                passwordModel.setTintColor(colorPickerComponent.getTintColor());
                passwordModel.setCategoryID(category);
                passwordModel.encrypt();

                ResultObject update = passwordModel.update();

                passwordModel.decrypt();

                if (update.isSuccess()) {
                    Global.LIST_PASSWORDS.set(Global.CURRENT_PASSWORD_MODEL_INDEX, passwordModel);
                    Helpers.list.findAndUpdate(passwordModel);
                    Global.LIST_PASSWORDS_SOLID.set(Helpers.list.findIndexFromSolid(passwordModel), passwordModel);

                    ((Activity) Global.CONTEXT).runOnUiThread(() -> {
                        Global.PASSWORD_ADAPTER.notifyDataSetChanged();
                        Global.TAB_LAYOUT.getTabAt(Config.TAB_PASSWORD_INDEX).setText(passwordModel.getName());
                        Global.TAB_LAYOUT.getTabAt(Config.TAB_PASSWORD_INDEX).setContentDescription(passwordModel.getName());

                        SnackbarComponent snackbarComponent = new SnackbarComponent(viewRoot, R.string.success_update_record, R.string.action_ok);
                        snackbarComponent.show();

                        Global.PAGE_HOME.filter("");
                    });
                } else {
                    SnackbarComponent snackbarComponent = new SnackbarComponent(viewRoot, R.string.failure_update_password, R.string.action_ok);
                    snackbarComponent.show();
                }

                Helpers.system.hideSoftKeyboard();

                Helpers.loading.hide();
            }).start();
        });

        btnClipboard.setOnClickListener(v -> {
            try {
                new Thread(() -> {
                    ClipboardManager clipboard = (ClipboardManager) Global.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(Helpers.resource.getString(R.string.clipboard_title), passwordModel.getDecryptedPassword());
                    clipboard.setPrimaryClip(clip);

                    SnackbarComponent.direct(viewRoot, Helpers.resource.getString(R.string.password_clipboard, "", passwordModel.getShortName()));
                }).start();
            } catch (Exception e) {
                Helpers.logger.error(ErrorCodeConstants.CLIPBOARD_PASSWORD, e);

                Toast.makeText(Global.CONTEXT, R.string.failure_password_clipboard, Toast.LENGTH_SHORT).show();
            }
        });

        btnClipboardAccount.setOnClickListener(v -> {
            try {
                new Thread(() -> {
                    ClipboardManager clipboardManager = (ClipboardManager) Global.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText(Helpers.resource.getString(R.string.account_name), passwordModel.getDecryptedAccount());
                    clipboardManager.setPrimaryClip(clipData);

                    SnackbarComponent.direct(viewRoot, Helpers.resource.getString(R.string.account_clipboard, "", passwordModel.getShortName()));
                }).start();
            } catch (Exception e) {
                Helpers.logger.error(ErrorCodeConstants.CLIPBOARD_ACCOUNT, e);

                Toast.makeText(Global.CONTEXT, R.string.failure_account_clipboard, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //endregion
}
