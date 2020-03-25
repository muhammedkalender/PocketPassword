package com.muhammedkalender.pocketpassword.Pages.Password;

import android.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Components.ColorPickerComponent;
import com.muhammedkalender.pocketpassword.Components.SnackbarComponent;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;
import com.muhammedkalender.pocketpassword.Models.CategoryModel;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;
import com.muhammedkalender.pocketpassword.R;

import java.util.List;

public class NewPasswordPage extends PageAbstract implements PageInterface {
    //region Variables

    public int selectedCategoryIndex = 0;
    public int selectedCategoryId = 0;

    private List<CategoryModel> listCategory;

    ArrayAdapter arrayAdapter;

    //endregion

    //region UI Components

    public MaterialButton btnAdd = null;

    public TextInputLayout tilName = null;
    public TextInputEditText etName = null;

    public TextInputLayout tilAccount = null;
    public TextInputEditText etAccount = null;

    public TextInputLayout tilPassword = null;
    public TextInputEditText etPassword = null;

    public TextInputLayout tilCategory = null;
    public TextInputEditText etCategory = null;

    private ColorPickerComponent colorPickerComponent;

    //endregion

    //region Initialize

    @Override
    public void initialize(View viewRoot) {
        if (isInitialized()) {
            selectedCategoryId = 0;
            selectedCategoryIndex = 0;
            etCategory.setText(listCategory.get(0).getName());
        }

        //region Declare UI Components

        this.viewRoot = viewRoot;
        this.initialized = true;

        this.tilName = this.viewRoot.findViewById(R.id.tilName);
        this.etName = this.viewRoot.findViewById(R.id.etName);
        this.tilAccount = this.viewRoot.findViewById(R.id.tilAccount);
        this.etAccount = this.viewRoot.findViewById(R.id.etAccount);
        this.tilPassword = this.viewRoot.findViewById(R.id.tilPassword);
        this.etPassword = this.viewRoot.findViewById(R.id.etPassword);
        this.tilCategory = this.viewRoot.findViewById(R.id.tilCategory);
        this.etCategory = this.viewRoot.findViewById(R.id.etCategory);
        this.btnAdd = this.viewRoot.findViewById(R.id.btnAdd);

        //endregion

        //region Load Categories

        CategoryModel categoryModel = new CategoryModel();
        listCategory = categoryModel.selectActive(false);
        arrayAdapter = new ArrayAdapter<>(Global.CONTEXT, android.R.layout.select_dialog_singlechoice);

        listCategory.remove(0); //Remove All Of Them

        for (CategoryModel category : listCategory) {
            arrayAdapter.add(category.getName());
        }

        //endregion

        //region Load Colors

        HorizontalScrollView hsvColors = this.viewRoot.findViewById(R.id.hsvColors);

        hsvColors.post(() -> {
            hsvColors.setScrollX(0);
        });

        colorPickerComponent = new ColorPickerComponent(Global.VIEW_GROUP, ColorConstants.colorItem[0].getColor(), btnAdd);

        colorPickerComponent.fillLayout(viewRoot.findViewById(R.id.llColors));

        colorPickerComponent.refresh(colorPickerComponent.getRandomColor());

        //endregion

        //region Reset Views

        ScrollView svNewPassword = this.viewRoot.findViewById(R.id.svNewPassword);

        svNewPassword.post(() -> {
            svNewPassword.setScrollY(0);
        });

        //endregion

        //region Models & Datas

        final PasswordModel passwordModel = new PasswordModel();

        //endregion

        //region Events

        //region Events Category

        this.tilCategory.setOnClickListener(v -> {
            showCategories();
        });

        this.etCategory.setOnClickListener(v -> {
            this.tilCategory.callOnClick();
        });

        //endregion

        btnAdd.setOnClickListener(v -> {
            newPassword(passwordModel);
        });

        //endregion

        Helpers.logger.info("Listener Atandı");

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

    //region Primary Methods

    //region Category

    private void showCategories(){
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
    }

    //endregion

    //region Password

    private void newPassword(PasswordModel passwordModel){
        Helpers.loading.show();

        String name = etName.getText().toString();
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        int color = colorPickerComponent.getColor();
        int tintColor = colorPickerComponent.getTintColor();
        int category = listCategory.get(selectedCategoryIndex).getId();

        passwordModel.setName(name);
        passwordModel.setAccount(account);
        passwordModel.setPassword(password);
        passwordModel.setColor(color);
        passwordModel.setTintColor(tintColor);
        passwordModel.setCategoryID(category);

        ResultObject validation = passwordModel.validation();

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
            } else if (passwordModel.checkActiveDuplicate(name)) {
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

        PasswordModel passwordModel1 = new PasswordModel(name, account, password, color, tintColor, category);

        ResultObject insert = passwordModel1.insert();

        if (insert.isSuccess()) {
            Helpers.logger.info(String.format("%1$d ID ile kayıt girildi", (int) insert.getData()));

            Global.TAB_LAYOUT.getTabAt(Config.TAB_HOME_INDEX).select();
            PasswordModel addedPasswordModel = new PasswordModel((int) insert.getData(), name, account, password, color, tintColor, category);
            addedPasswordModel.setDecrypted(true);

            Global.LIST_PASSWORDS.add(addedPasswordModel);
            Global.LIST_PASSWORDS_SOLID.add(addedPasswordModel);
            Global.PASSWORD_ADAPTER.notifyDataSetChanged();

            SnackbarComponent snackbarComponent = new SnackbarComponent(viewRoot, R.string.success_add_password, R.string.action_ok);
            snackbarComponent.show();

            etName.setText(null);
            etAccount.setText(null);
            etPassword.setText(null);
            selectedCategoryIndex = 0;
            selectedCategoryId = 0;
            etCategory.setText(listCategory.get(selectedCategoryIndex).getName());
            colorPickerComponent.refresh(colorPickerComponent.getRandomColor());

            Global.PAGE_HOME.filter("");
        } else {
            Helpers.logger.error(ErrorCodeConstants.MODEL_PASSWORD_INSERT, (Exception) insert.getData());
        }

        Helpers.system.hideSoftKeyboard();
        Helpers.loading.hide();
    }

    //endregion

    //endregion
}
