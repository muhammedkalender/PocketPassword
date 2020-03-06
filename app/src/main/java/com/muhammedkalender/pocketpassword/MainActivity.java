package com.muhammedkalender.pocketpassword;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hypertrack.hyperlog.HyperLog;
import com.muhammedkalender.pocketpassword.Abstracts.ModelAbstract;
import com.muhammedkalender.pocketpassword.Adapters.PasswordAdapter;
import com.muhammedkalender.pocketpassword.Components.CustomLogMessageFormat;
import com.muhammedkalender.pocketpassword.Components.LoadingComponent;
import com.muhammedkalender.pocketpassword.Components.SnackbarComponent;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
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
import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;
import com.muhammedkalender.pocketpassword.ui.main.SectionsPagerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    TextInputLayout tilMainPassword, tilMainPasswordRepeat;
    TextInputEditText etMainPassword, etMainPasswordRepeat;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_controller_view_tag);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

    @Override
    public void onBackPressed() {
        if (Global.TAB_LAYOUT.getSelectedTabPosition() == Config.TAB_HOME_INDEX) {
            super.onBackPressed();
        } else {
            Helpers.system.hideSoftKeyboard();
            Global.TAB_LAYOUT.getTabAt(Config.TAB_HOME_INDEX).select();
        }
    }

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

    public boolean loadComponents() {
        tilMainPassword = findViewById(R.id.tilMainPassword);
        etMainPassword = findViewById(R.id.etMainPassword);
        tilMainPasswordRepeat = findViewById(R.id.tilMainPasswordRepeat);
        etMainPasswordRepeat = findViewById(R.id.etMainPasswordRepeat);

        return true;
    }

    //region Initializer

    public void firstOpen() {
        new Thread(() -> {
            if (!Helpers.config.getBoolean("first_open", true)) {
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

            boolean setPrivateKey = Helpers.config.setString("private_key", String.valueOf(resultPrivateKeyToString.getData()));
            boolean setPublicKey = Helpers.config.setString("public_key", String.valueOf(resultPublicKeyToString.getData()));

            if (!(setPrivateKey && setPublicKey)) {
                SnackbarComponent snackbarComponent = new SnackbarComponent(getWindow().getDecorView().getRootView(), R.string.first_open_error, R.string.action_ok);
                snackbarComponent.show();

                Helpers.logger.info("keyler shared prefe aktarılamadı");

                Helpers.loading.hide();

                return;
            }

            Helpers.config.setBoolean("first_open", false);

            Helpers.config.setString("device_id", Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));

            runOnUiThread(this::registered);
        }).start();
    }

    private void registered() {
        etMainPasswordRepeat.setText("");
        tilMainPasswordRepeat.setErrorEnabled(false);
        etMainPassword.setText("");
        tilMainPassword.setErrorEnabled(false);

        if (Helpers.config.getBoolean("registered", false)) {
            tilMainPasswordRepeat.setVisibility(View.GONE);

            ((MaterialButton) findViewById(R.id.btnLogin)).setText(R.string.button_login);
            ((MaterialButton) findViewById(R.id.btnLogin)).setIcon(Helpers.resource.getDrawable(R.drawable.ic_person_24dp));

            tilMainPassword.setHelperText(Helpers.resource.getString(R.string.input_login_password));
            tilMainPassword.setHint(Helpers.resource.getString(R.string.hint_password_edit));


            etMainPassword.setText("123456aA_");

            findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
        } else {
            ((MaterialButton) findViewById(R.id.btnLogin)).setText(R.string.button_confirm);

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


                    if (Helpers.config.getBoolean("first_open")) {
                        //todo
                    } else {
                        Global.PASSWORD = etMainPassword.getText().toString();

                        String base64PrivateKey = Helpers.config.getString("private_key");
                        String base64PublicKey = Helpers.config.getString("public_key");

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

                        boolean setBase64PrivateKey = Helpers.config.setString("private_key", encryptedBase64PrivateKey);
                        boolean setBase64PublicKey = Helpers.config.setString("public_key", encryptedBase64PublicKey);

                        if (setBase64PrivateKey && setBase64PublicKey) {
                            CryptHelper cryptHelper = new CryptHelper();
                            cryptHelper.setPrivateKey((String) Helpers.aes.decrypt(encryptedBase64PrivateKey, Global.PASSWORD).getData());
                            cryptHelper.setPublicKey((String) Helpers.aes.decrypt(encryptedBase64PublicKey, Global.PASSWORD).getData());

                            String confirmText = cryptHelper.generateValidationText();

                            ResultObject resultRSAEncrypt = cryptHelper.encrypt(confirmText, cryptHelper.getPrivateKey());

                            if (resultRSAEncrypt.isFailure()) {
                                Helpers.logger.error(ErrorCodeConstants.REGISTER_RESULT_RSA,"HATA RSA");

                                SnackbarComponent snackbarComponent = new SnackbarComponent(getWindow().getDecorView().getRootView(), R.string.register_failure, R.string.action_ok);
                                snackbarComponent.show();
                            }

                            ResultObject resultAESEncrypt = Helpers.aes.encrypt((String) resultRSAEncrypt.getData(), Global.PASSWORD);

                            if (resultAESEncrypt.isFailure()) {
                                Helpers.logger.error(ErrorCodeConstants.REGISTER_RESULT_AES,"HATA AES");

                                SnackbarComponent snackbarComponent = new SnackbarComponent(getWindow().getDecorView().getRootView(), R.string.register_failure_1, R.string.action_ok);
                                snackbarComponent.show();
                            }

                            Helpers.logger.info(3, "step");
                            Helpers.config.setString("confirm_password", (String) resultAESEncrypt.getData());
                            Helpers.config.setBoolean("registered", true);

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
                            };

                            for (int i = 0; i < categoryModels.length; i++){
                                categoryModels[i].insert();
                            }

                            PasswordModel passwordModel = new PasswordModel(Helpers.resource.getString(R.string.example_name, "Example Account"), Helpers.resource.getString(R.string.example_account, "example_account"), Helpers.resource.getString(R.string.example_password, "Example_Password"), color, tintColor, 1);
                            ResultObject resultInsert = passwordModel.insert();

                            if (resultInsert.isSuccess()) {
                                Helpers.config.setBoolean("first_open", false);
                            }

                            registered();
                        } else {
                            SnackbarComponent snackbarComponent = new SnackbarComponent(getWindow().getDecorView().getRootView(), R.string.register_failure_2, R.string.action_ok);
                            snackbarComponent.show();
                        }
                    }
                }
            });
        }
    }

    public void login() {
        Helpers.loading.show();

        new Thread(() -> {
            Global.PASSWORD = etMainPassword.getText().toString();

            String confirmString = Helpers.config.getString("confirm_password");

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
                    new ColorObject(Helpers.resource.getColor(R.color.amber), Helpers.resource.getColor(R.color.tintAmber)),
                    new ColorObject(Helpers.resource.getColor(R.color.red), Helpers.resource.getColor(R.color.tintRed)),
                    new ColorObject(Helpers.resource.getColor(R.color.purple), Helpers.resource.getColor(R.color.tintPurple)),
                    new ColorObject(Helpers.resource.getColor(R.color.deepOrange), Helpers.resource.getColor(R.color.tintDeepOrange)),
                    new ColorObject(Helpers.resource.getColor(R.color.brown), Helpers.resource.getColor(R.color.tintBrown)),
                    new ColorObject(Helpers.resource.getColor(R.color.green), Helpers.resource.getColor(R.color.tintGreen))
            };

            runOnUiThread(() ->{
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
                        if(tab.getPosition() == Config.TAB_SETTINGS_INDEX){
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
        }).start();
    }

    //endregion
}

/*
    TODO
    9 - Model üzerinden validation ? mesajı felan outpu verebilir direkt
    10 - Export | Şifreli hallerini export et
    11 - İmport | Şifreyi sor çöz, dbye import et
    15 - Parmak İzi
    16 - Şifre uygulaması ( siteye girdiğinde buraya soracak felan )
    18 - "." tarzı karakterleri ekle regexp ye
    19 - Hata mesajları - kontrolleri
    20 - Ayarlar Hakkında Info ?
    21 - Dilleri kontrol et
    23 - Modellerdede insert vs.. çok sağlıklı değil kafada çizip gir
    24 - Boş uyarısı ( Eventi olamyan iemle öğre yok felan tarzı boş item ekleme )
    25 - Dialogları güncelle tasarımı tuhaf şuan -- https://developer.android.com/guide/topics/ui/dialogs
    26 - ReGİSTERDE OTOMATİK inputtan geçmiyor. ( Bir sonraki inputa geçmiyor klavye )
    27 - Geri gelip uygulamayı açınca sapıtıyor ( kendini düzğün kapatmıyor kapat yada ona göre düzenle )(
    28 - Şifre Değiştirme ?
    29 - Renkte varsayılanı sıfırlama ( ekleidkten sorna felan )

 */
