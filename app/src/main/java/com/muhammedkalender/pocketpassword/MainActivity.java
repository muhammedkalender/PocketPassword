package com.muhammedkalender.pocketpassword;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hypertrack.hyperlog.HyperLog;
import com.muhammedkalender.pocketpassword.Components.CustomLogMessageFormat;
import com.muhammedkalender.pocketpassword.Components.LoadingComponent;
import com.muhammedkalender.pocketpassword.Components.SnackbarComponent;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Constants.ConfigKeys;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Constants.RequestCodeConstants;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.AESHelper;
import com.muhammedkalender.pocketpassword.Helpers.CryptHelper;
import com.muhammedkalender.pocketpassword.Helpers.DatabaseHelper;
import com.muhammedkalender.pocketpassword.Helpers.ListHelper;
import com.muhammedkalender.pocketpassword.Helpers.LogHelpers;
import com.muhammedkalender.pocketpassword.Helpers.ResourceHelper;
import com.muhammedkalender.pocketpassword.Helpers.SharedPreferencesHelper;
import com.muhammedkalender.pocketpassword.Helpers.SystemHelper;
import com.muhammedkalender.pocketpassword.Helpers.ValidationHelper;
import com.muhammedkalender.pocketpassword.Models.CategoryModel;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.Objects.ColorObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;
import com.muhammedkalender.pocketpassword.ui.main.SectionsPagerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyPair;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    //region UI Components

    private AppBarConfiguration mAppBarConfiguration;

    public TextInputLayout tilMainPassword, tilMainPasswordRepeat;
    public TextInputEditText etMainPassword, etMainPasswordRepeat;

    //endregion

    //region Override Functions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            buildHelpers();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Config.initConfig();

        loadComponents();

        firstOpen();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RequestCodeConstants.EXPORT_BACKUP_SELECTED_FILE && resultCode == Activity.RESULT_OK) {
            //region Export Backup

            //https://stackoverflow.com/a/38568666
            //https://stackoverflow.com/a/2509258
            try {
                Uri uri = data.getData();

                OutputStream output = getApplicationContext().getContentResolver().openOutputStream(uri);

                output.write(Global.EXPORT_DATA.getBytes());
                output.flush();
                output.close();

                Global.EXPORT_DATA = "";

                SnackbarComponent.direct(
                        findViewById(android.R.id.content),
                        R.string.success_export_backup
                );
            } catch (Exception e) {
                Helpers.logger.error(ErrorCodeConstants.EXPORT_BACKUP_OAR, e);
                Toast.makeText(this, R.string.failure_export_backup_oar, Toast.LENGTH_SHORT).show();
            }

            //endregion
        } else if (requestCode == RequestCodeConstants.IMPORT_BACKUP_SELECTED_FILE && resultCode == RESULT_OK) {
            //region Import Backup
            try {
                //region Dialog Builder & File Check

                Uri uri = data.getData();

                StringBuilder stringBuilder = new StringBuilder();

                BufferedReader r = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));

                for (String line; (line = r.readLine()) != null; ) {
                    stringBuilder.append(line).append('\n');
                }

                JsonObject jsonObject = new Gson().fromJson(stringBuilder.toString(), JsonObject.class);

                Helpers.logger.info(jsonObject.get("public_key").getAsString());

                LayoutInflater inflater = this.getLayoutInflater();

                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setView(inflater.inflate(R.layout.custom_import_dialog, null))
                        .setPositiveButton("OK", null)
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//todo
                            }
                        })
                        .show();

                //endregion

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v) -> {
                    //region Input Check

                    TextInputEditText etImportPassword = alertDialog.findViewById(R.id.etImportPassword);
                    TextInputLayout tilImportPassword = alertDialog.findViewById(R.id.tilImportPassword);

                    if (etImportPassword.getText() == null || etImportPassword.getText().toString().equals("")) {
                        tilImportPassword.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.password)));

                        return;
                    }

                    String password = etImportPassword.getText().toString();

                    if (password.length() < 8) {
                        tilImportPassword.setError(Helpers.resource.getString(R.string.min_length, "", Helpers.resource.getString(R.string.password), 8));

                        return;
                    } else if (password.length() > 32) {
                        tilImportPassword.setError(Helpers.resource.getString(R.string.max_length, "", Helpers.resource.getString(R.string.password), 32));

                        return;
                    } else if (!Helpers.validation.checkPassword(password, ValidationHelper.PASSWORD_STRONG)) {
                        tilImportPassword.setError(Helpers.resource.getString(R.string.password_must_strong));

                        return;
                    } else {
                        tilImportPassword.setErrorEnabled(false);

                        if (etImportPassword.getText() == null || etImportPassword.getText().toString().equals("")) {
                            tilImportPassword.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.password)));

                            return;
                        }
                    }

                    //endregion
                    Helpers.logger.var("Import Şifre", password);
                    Helpers.loading.show();

                    Helpers.system.hideSoftKeyboard();

                    new Thread(() -> {
                        //region Password Check

                        String publicKey = jsonObject.get("public_key").getAsString();

                        AESHelper aesHelper = new AESHelper();

                        if (!jsonObject.get("confirm_password").getAsString().equals(aesHelper.encrypt(Config.EXPORT_CONFIRM_TEXT, password).getDataAsString())) {
                            runOnUiThread(() -> {
                                tilImportPassword.setError(Helpers.resource.getString(R.string.password_wrong));
                            });

                            Helpers.loading.hide();

                            return;
                        }

                        Helpers.system.hideSoftKeyboard();

                        //endregion

                        alertDialog.dismiss();

                        CryptHelper cryptHelper = new CryptHelper();

                        cryptHelper.setPublicKey(aesHelper.decrypt(publicKey, password).getDataAsString());

                        JsonArray passwords = jsonObject.get("passwords").getAsJsonArray();

                        HashMap<PasswordModel, ResultObject> listFailure = new HashMap<>();

                        for (int i = 0; i < passwords.size(); i++) {
                            JsonElement _element = passwords.get(i);

                            if (_element == null || _element.isJsonNull()) {
                                continue;
                            }

                            JsonObject _password = _element.getAsJsonObject();

                            Helpers.logger.info(_password.get("name").getAsString());

                            PasswordModel _passwordModel = new PasswordModel(
                                    _password.get("name").getAsString(),
                                    _password.get("account").getAsString(),
                                    _password.get("password").getAsString(),
                                    _password.get("color").getAsInt(),
                                    _password.get("tint").getAsInt(),
                                    _password.get("category").getAsInt(),
                                    _password.get("active").getAsInt() == 1
                            );

                            Helpers.logger.var("Yedek Yükleme Saf Şifre", _passwordModel.getPassword());

                            _passwordModel.decrypt(cryptHelper);

                            Helpers.logger.var("Yedek Yükleme Çözülmüş Şifre", _passwordModel.getPassword());

                            _passwordModel.encrypt(); //todo var burda bi bokluk

                            Helpers.logger.var("Yedek Yükleme Şifrelenmiş Şifre", _passwordModel.getPassword());

                            ResultObject resultInsert = _passwordModel.insert();

                            if (resultInsert.isFailure()) {
                                listFailure.put(_passwordModel, resultInsert);
                            } else {
                                _passwordModel.setId((int) resultInsert.getData());

                                if(_passwordModel.isActive()){
                                    _passwordModel.decrypt();

                                    Global.LIST_PASSWORDS.add(_passwordModel);
                                    Global.LIST_PASSWORDS_SOLID.add(_passwordModel);
                                }
                            }
                        }

                        //region After Process

                        Global.PASSWORD_ADAPTER.notifyDataSetChanged();
                        Global.PAGE_HOME.filter("");

                        Helpers.loading.hide();

                        alertDialog.dismiss();

                        Helpers.system.hideSoftKeyboard();

                        if (listFailure.size() == 0) {
                            SnackbarComponent.direct(Global.PAGE_SETTINGS.getView(), R.string.success_insert_backup, R.string.action_confirm);
                        } else {
                            SnackbarComponent.direct(
                                    Global.PAGE_SETTINGS.getView(),
                                    Helpers.resource.getString(
                                            R.string.failure_insert_backup,
                                            "",
                                            listFailure.size()
                                    ),
                                    R.string.action_confirm
                                    , v1 -> {
                                        runOnUiThread(() -> Global.TAB_LAYOUT.getTabAt(Config.TAB_HOME_INDEX).select());
                                    }
                            );
                        }

                        //endregion
                    }).start();
                });
            } catch (Exception e) {
                Helpers.logger.error(ErrorCodeConstants.IMPORT_BACKUP_OAR, e);
                Toast.makeText(this, "Error IMPORT", Toast.LENGTH_SHORT).show();
            }

            //endregion
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        if(Helpers.loading.isShowing){
            return;
        }

        if (Global.TAB_LAYOUT.getSelectedTabPosition() == Config.TAB_HOME_INDEX) {
            super.onBackPressed();
        } else {
            Helpers.system.hideSoftKeyboard();
            Global.TAB_LAYOUT.getTabAt(Config.TAB_HOME_INDEX).select();
        }
    }

    //endregion

    //region Declare Global Helpers

    public boolean buildHelpers() throws IOException {
        Global.CONTEXT = this;
        Global.ACTIVITY = MainActivity.this;

        HyperLog.initialize(this, new CustomLogMessageFormat(this));
        Helpers.logger = new LogHelpers();
        Helpers.resource = new ResourceHelper();
        Helpers.database = new DatabaseHelper(this);
        Helpers.loading = new LoadingComponent(findViewById(R.id.rlLoading));
        Helpers.list = new ListHelper();
        Helpers.config = new SharedPreferencesHelper(this);
        Helpers.validation = new ValidationHelper();
        Helpers.aes = new AESHelper();
        Helpers.system = new SystemHelper();

        return true;
    }

    //endregion

    //region Declare UI Compoenents

    public boolean loadComponents() {
        tilMainPassword = findViewById(R.id.tilMainPassword);
        etMainPassword = findViewById(R.id.etMainPassword);
        tilMainPasswordRepeat = findViewById(R.id.tilMainPasswordRepeat);
        etMainPasswordRepeat = findViewById(R.id.etMainPasswordRepeat);

        return true;
    }

    //endregion

    //region Initializer

    public void firstOpen() {
        new Thread(() -> {
            if (!Helpers.config.getBoolean(ConfigKeys.FIRST_OPEN, true)) {
                runOnUiThread(this::registered);

                return;
            }

            Helpers.loading.show();

            CryptHelper cryptHelper = new CryptHelper();

            ResultObject resultGenerateKeys = cryptHelper.generateKeys();

            if (resultGenerateKeys.isFailure()) {
                SnackbarComponent snackbarComponent = new SnackbarComponent(getWindow().getDecorView().getRootView(), R.string.first_open_error, R.string.action_ok);
                snackbarComponent.show();

                Helpers.loading.hide();

                return;
            }

            Helpers.loading.hide();

            KeyPair keyPair = (KeyPair) resultGenerateKeys.getData();

            ResultObject resultPrivateKeyToString = cryptHelper.keyToString(keyPair.getPrivate());
            ResultObject resultPublicKeyToString = cryptHelper.keyToString(keyPair.getPublic());

            if (resultPrivateKeyToString.isFailure() || resultPublicKeyToString.isFailure()) {
                Helpers.logger.info("Keyler base64e atanamadı");

                SnackbarComponent snackbarComponent = new SnackbarComponent(getWindow().getDecorView().getRootView(), R.string.success_update_password, R.string.action_ok);
                snackbarComponent.show();

                Helpers.loading.hide();

                return;
            }

            boolean setPrivateKey = Helpers.config.setString(ConfigKeys.PRIVATE_KEY, String.valueOf(resultPrivateKeyToString.getData()));
            boolean setPublicKey = Helpers.config.setString(ConfigKeys.PUBLIC_KEY, String.valueOf(resultPublicKeyToString.getData()));

            if (!(setPrivateKey && setPublicKey)) {
                SnackbarComponent snackbarComponent = new SnackbarComponent(getWindow().getDecorView().getRootView(), R.string.first_open_error, R.string.action_ok);
                snackbarComponent.show();

                Helpers.logger.info("keyler shared prefe aktarılamadı");

                Helpers.loading.hide();

                return;
            }

            Helpers.config.setBoolean(ConfigKeys.FIRST_OPEN, false);

            Helpers.config.setString(ConfigKeys.DEVICE_ID, Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));

            runOnUiThread(this::registered);
        }).start();
    }

    private void registered() {
        etMainPasswordRepeat.setText("");
        tilMainPasswordRepeat.setErrorEnabled(false);
        etMainPassword.setText("");
        tilMainPassword.setErrorEnabled(false);

        if (Helpers.config.getBoolean(ConfigKeys.REGISTER, false)) {
            //region Is Registered

            etMainPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
            tilMainPasswordRepeat.setVisibility(View.GONE);
            tilMainPassword.setPasswordVisibilityToggleEnabled(false);

            ((MaterialButton) findViewById(R.id.btnLogin)).setText(R.string.button_login);
            ((MaterialButton) findViewById(R.id.btnLogin)).setIcon(Helpers.resource.getDrawable(R.drawable.ic_person_24dp));

            tilMainPassword.setHelperText(Helpers.resource.getString(R.string.input_login_password));
            tilMainPassword.setHint(Helpers.resource.getString(R.string.hint_password_edit));

            findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });

            //endregion
        } else {
            //region Is First Login

            ((MaterialButton) findViewById(R.id.btnLogin)).setText(R.string.button_confirm);

            etMainPassword.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            tilMainPassword.setHelperText(Helpers.resource.getString(R.string.input_password_register_edit));
            tilMainPassword.setHint(Helpers.resource.getString(R.string.hint_password_register_edit));

            findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etMainPassword.getText() == null || etMainPassword.getText().toString().equals("")) {
                        tilMainPassword.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.password)));

                        return;
                    }

                    String password = etMainPassword.getText().toString();

                    if (password.length() < 8) {
                        tilMainPassword.setError(Helpers.resource.getString(R.string.min_length, "", Helpers.resource.getString(R.string.password), 8));

                        return;
                    } else if (password.length() > 32) {
                        tilMainPassword.setError(Helpers.resource.getString(R.string.max_length, "", Helpers.resource.getString(R.string.password), 32));

                        return;
                    } else if (!Helpers.validation.checkPassword(password, ValidationHelper.PASSWORD_STRONG)) {
                        tilMainPassword.setError(Helpers.resource.getString(R.string.password_must_strong));

                        return;
                    } else {
                        tilMainPassword.setErrorEnabled(false);

                        if (etMainPasswordRepeat.getText() == null || etMainPasswordRepeat.getText().toString().equals("")) {
                            tilMainPasswordRepeat.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.password)));

                            return;
                        }

                        String passwordRepeat = etMainPasswordRepeat.getText().toString();

                        if (password.equals(passwordRepeat)) {
                            tilMainPasswordRepeat.setErrorEnabled(false);
                        } else {
                            tilMainPasswordRepeat.setError(Helpers.resource.getString(R.string.password_not_match));

                            return;
                        }
                    }


                    if (Helpers.config.getBoolean(ConfigKeys.FIRST_OPEN)) {
                        //todo
                    } else {
                        Global.PASSWORD = etMainPassword.getText().toString();

                        String base64PrivateKey = Helpers.config.getString(ConfigKeys.PRIVATE_KEY);
                        String base64PublicKey = Helpers.config.getString(ConfigKeys.PUBLIC_KEY);

                        Helpers.logger.info(base64PrivateKey);

                        ResultObject resultEncryptBase64PrivateKey = Helpers.aes.encrypt(base64PrivateKey, Global.PASSWORD);
                        ResultObject resultEncryptBase64PublicKey = Helpers.aes.encrypt(base64PublicKey, Global.PASSWORD);

                        if (resultEncryptBase64PrivateKey.isFailure() || resultEncryptBase64PublicKey.isFailure()) {
                            SnackbarComponent snackbarComponent = new SnackbarComponent(getWindow().getDecorView().getRootView(), R.string.register_failure_3, R.string.action_ok);
                            snackbarComponent.show();

                            return;
                        }

                        String encryptedBase64PrivateKey = String.valueOf(resultEncryptBase64PrivateKey.getData());
                        String encryptedBase64PublicKey = String.valueOf(resultEncryptBase64PublicKey.getData());

                        boolean setBase64PrivateKey = Helpers.config.setString(ConfigKeys.PRIVATE_KEY, encryptedBase64PrivateKey);
                        boolean setBase64PublicKey = Helpers.config.setString(ConfigKeys.PUBLIC_KEY, encryptedBase64PublicKey);

                        if (setBase64PrivateKey && setBase64PublicKey) {
                            CryptHelper cryptHelper = new CryptHelper();
                            cryptHelper.setPrivateKey((String) Helpers.aes.decrypt(encryptedBase64PrivateKey, Global.PASSWORD).getData());
                            cryptHelper.setPublicKey((String) Helpers.aes.decrypt(encryptedBase64PublicKey, Global.PASSWORD).getData());

                            String confirmText = cryptHelper.generateValidationText();

                            ResultObject resultRSAEncrypt = cryptHelper.encrypt(confirmText, cryptHelper.getPrivateKey());

                            if (resultRSAEncrypt.isFailure()) {
                                Helpers.logger.error(ErrorCodeConstants.REGISTER_RESULT_RSA, "HATA RSA");

                                SnackbarComponent snackbarComponent = new SnackbarComponent(getWindow().getDecorView().getRootView(), R.string.register_failure, R.string.action_ok);
                                snackbarComponent.show();
                            }

                            ResultObject resultAESEncrypt = Helpers.aes.encrypt((String) resultRSAEncrypt.getData(), Global.PASSWORD);

                            if (resultAESEncrypt.isFailure()) {
                                Helpers.logger.error(ErrorCodeConstants.REGISTER_RESULT_AES, "HATA AES");

                                SnackbarComponent snackbarComponent = new SnackbarComponent(getWindow().getDecorView().getRootView(), R.string.register_failure_1, R.string.action_ok);
                                snackbarComponent.show();
                            }

                            Helpers.config.setString(ConfigKeys.CONFIRM_TEXT, (String) resultAESEncrypt.getData());
                            Helpers.config.setBoolean(ConfigKeys.REGISTER, true);

                            Helpers.crypt = cryptHelper;

                            int color = Helpers.resource.getColor(R.color.lightBlue);
                            int tintColor = Helpers.resource.getColor(R.color.tintLightBlue);

                            CategoryModel[] categoryModels = new CategoryModel[]{
                                    new CategoryModel(Helpers.resource.getString(R.string.category_other), Helpers.resource.getColor(R.color.grey), Helpers.resource.getColor(R.color.tintGrey), true),
                                    new CategoryModel(Helpers.resource.getString(R.string.category_social_media), Helpers.resource.getColor(R.color.pink), Helpers.resource.getColor(R.color.tintPink), true),
                                    new CategoryModel(Helpers.resource.getString(R.string.category_bank), Helpers.resource.getColor(R.color.lightBlue), Helpers.resource.getColor(R.color.tintLightBlue), true),
                                    new CategoryModel(Helpers.resource.getString(R.string.category_mail), Helpers.resource.getColor(R.color.amber), Helpers.resource.getColor(R.color.tintAmber), true),
                                    new CategoryModel(Helpers.resource.getString(R.string.category_server), Helpers.resource.getColor(R.color.indigo), Helpers.resource.getColor(R.color.tintIndigo), true),
                                    new CategoryModel(Helpers.resource.getString(R.string.category_app), Helpers.resource.getColor(R.color.red), Helpers.resource.getColor(R.color.tintRed), true),
                                    new CategoryModel(Helpers.resource.getString(R.string.category_computer), Helpers.resource.getColor(R.color.green), Helpers.resource.getColor(R.color.tintGreen), true),
                                    new CategoryModel(Helpers.resource.getString(R.string.category_service), Helpers.resource.getColor(R.color.deepOrange), Helpers.resource.getColor(R.color.tintDeepOrange), true),
                                    new CategoryModel(Helpers.resource.getString(R.string.category_e_commerce), Helpers.resource.getColor(R.color.teal), Helpers.resource.getColor(R.color.tintTeal), true),
                                    new CategoryModel(Helpers.resource.getString(R.string.category_governor), Helpers.resource.getColor(R.color.purple), Helpers.resource.getColor(R.color.tintPurple), true),
                                    new CategoryModel(Helpers.resource.getString(R.string.category_licence), Helpers.resource.getColor(R.color.brown), Helpers.resource.getColor(R.color.tintBrown), true)
                            };

                            for (int i = 0; i < categoryModels.length; i++) {
                                categoryModels[i].insert();
                            }

                            PasswordModel passwordModel = new PasswordModel(Helpers.resource.getString(R.string.example_name, "Example Account"), Helpers.resource.getString(R.string.example_account, "example_account"), Helpers.resource.getString(R.string.example_password, "Example_Password"), color, tintColor, 1);
                            ResultObject resultInsert = passwordModel.insert();

                            if (resultInsert.isSuccess()) {
                                Helpers.config.setBoolean(ConfigKeys.FIRST_OPEN, false);
                            }

                            registered();
                        } else {
                            SnackbarComponent snackbarComponent = new SnackbarComponent(getWindow().getDecorView().getRootView(), R.string.register_failure_2, R.string.action_ok);
                            snackbarComponent.show();
                        }
                    }
                }
            });

            //endregion
        }
    }

    public void login() {
        Helpers.loading.show();

        new Thread(() -> {
            Global.PASSWORD = etMainPassword.getText().toString();

            String confirmString = Helpers.config.getString(ConfigKeys.CONFIRM_TEXT);

            ResultObject resultDecryptConfirmString = Helpers.aes.decrypt(confirmString, Global.PASSWORD);

            if (resultDecryptConfirmString.isFailure()) {
                runOnUiThread(() -> {
                    etMainPassword.setText(null);
                    tilMainPassword.setError(Helpers.resource.getString(R.string.password_wrong_1));

                    Helpers.loading.hide();
                });

                return;
            }

            Helpers.crypt = CryptHelper.buildDefault();

            ResultObject resultDecryptRSAConfirmString = Helpers.crypt.decrypt((String) resultDecryptConfirmString.getData(), Helpers.crypt.getPublicKey());

            ColorConstants.colorItem = new ColorObject[]{
                    new ColorObject(Helpers.resource.getColor(R.color.lightBlue), Helpers.resource.getColor(R.color.tintLightBlue)), //Prımary Color
                    new ColorObject(Helpers.resource.getColor(R.color.pink), Helpers.resource.getColor(R.color.tintPink)),
                    new ColorObject(Helpers.resource.getColor(R.color.teal), Helpers.resource.getColor(R.color.tintTeal)),
                    new ColorObject(Helpers.resource.getColor(R.color.amber), Helpers.resource.getColor(R.color.tintAmber)),
                    new ColorObject(Helpers.resource.getColor(R.color.red), Helpers.resource.getColor(R.color.tintRed)),
                    new ColorObject(Helpers.resource.getColor(R.color.indigo), Helpers.resource.getColor(R.color.tintIndigo)),
                    new ColorObject(Helpers.resource.getColor(R.color.purple), Helpers.resource.getColor(R.color.tintPurple)),
                    new ColorObject(Helpers.resource.getColor(R.color.deepOrange), Helpers.resource.getColor(R.color.tintDeepOrange)),
                    new ColorObject(Helpers.resource.getColor(R.color.brown), Helpers.resource.getColor(R.color.tintBrown)),
            };

            //region Declare UI Components

            runOnUiThread(() -> {
                if (resultDecryptRSAConfirmString.isFailure()) {
                    etMainPassword.setText(null);
                    tilMainPassword.setError(Helpers.resource.getString(R.string.password_wrong));

                    Helpers.loading.hide();

                    return;
                }

                findViewById(R.id.appBar).setVisibility(View.VISIBLE);
                findViewById(R.id.llMainPassword).setVisibility(View.INVISIBLE);

                Global.VIEW_PAGER = findViewById(R.id.view_pager);
                Global.TAB_LAYOUT = findViewById(R.id.tabs);

                //https://stackoverflow.com/a/24612529
                Global.VIEW_GROUP = (ViewGroup) findViewById(android.R.id.content);

                Global.SECTION_PAGER_ADAPTER = new SectionsPagerAdapter(this, getSupportFragmentManager());
                Global.VIEW_PAGER.setAdapter(Global.SECTION_PAGER_ADAPTER);

                Global.TAB_LAYOUT.setupWithViewPager(Global.VIEW_PAGER);

                Helpers.system.hideSoftKeyboard();

                Global.TAB_LAYOUT.getTabAt(Config.TAB_HOME_INDEX).select();

                Global.TAB_LAYOUT.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if (tab.getPosition() == Config.TAB_SETTINGS_INDEX) {
                            Helpers.logger.info(tab.getPosition() + " Settingse girdimi");
                            Global.PAGE_SETTINGS.initialize(Global.PAGE_SETTINGS.getView());
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

                Helpers.loading.hide();
            });

            //endregion
        }).start();
    }

    //endregion
}

/*
    TODO
    9 - Model üzerinden validation ? mesajı felan outpu verebilir direkt
    15 - Parmak İzi
    16 - Şifre uygulaması ( siteye girdiğinde buraya soracak felan )
    19 - Hata mesajları - kontrolleri
    20 - Ayarlar Hakkında Info ?
    21 - Dilleri kontrol et
    23 - Modellerdede insert vs.. çok sağlıklı değil kafada çizip gir
    24 - Boş uyarısı ( Eventi olamyan iemle öğre yok felan tarzı boş item ekleme )
    25 - Dialogları güncelle tasarımı tuhaf şuan -- https://developer.android.com/guide/topics/ui/dialogs
    26 - İnputlarda hata olunca refresh gibi flash çakıyor
    27 - Dialog butonlarının tasarımı değişekcek ( mavi - siyah pek şı kdeğil )
    27 - Onayladna sonra ana sayfaua at ( import - snackbar onay )
    28 - Renk seçildiğinde inputların kenarlarının rengini dğeişmesi ?
    29 - Kayıt yazım yanlışı ( şfr gibi bişey yazıyor )
    30 - Giriş footer tarzı contact us - site linki vs..
    31 - Alışveriş kategorisi
    33 - Toastlarda kopyalama işlemlerinde vs.. daha b elirgin yap, ismin ilk 16 karakteri vs...
    34 - Lisanslar vs.. tazı kategoride olabilir :) pratik olur öyle
    36 - Şifre değiştirmede uyaruı ( veriler gidebilir diye )
    40 - Şifre düzenleme hatalar şifre değiştirincede duruyoır ( sıfırlaması lazım ) ? İnput Hataları

 */