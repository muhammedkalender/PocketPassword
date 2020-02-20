package com.muhammedkalender.pocketpassword;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.muhammedkalender.pocketpassword.Abstracts.ModelAbstract;
import com.muhammedkalender.pocketpassword.Adapters.PasswordAdapter;
import com.muhammedkalender.pocketpassword.Components.LoadingComponent;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.AESHelper;
import com.muhammedkalender.pocketpassword.Helpers.CryptHelper;
import com.muhammedkalender.pocketpassword.Helpers.DatabaseHelper;
import com.muhammedkalender.pocketpassword.Helpers.ListHelper;
import com.muhammedkalender.pocketpassword.Helpers.LogHelpers;
import com.muhammedkalender.pocketpassword.Helpers.ResourceHelper;
import com.muhammedkalender.pocketpassword.Helpers.SharedPreferencesHelper;
import com.muhammedkalender.pocketpassword.Helpers.ValidationHelper;
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
        // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        buildHelpers();

        Config.initConfig();

        loadComponents();

        firstOpen();

        registered();

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
            Global.TAB_LAYOUT.getTabAt(Config.TAB_HOME_INDEX).select();
        }
    }

    public boolean buildHelpers() {
        Global.CONTEXT = this;

        Helpers.logger = new LogHelpers();
        Helpers.resource = new ResourceHelper();
        Helpers.database = new DatabaseHelper(this);
        Helpers.loading = new LoadingComponent(this);
        Helpers.list = new ListHelper();
        Helpers.config = new SharedPreferencesHelper(this);
        Helpers.validation = new ValidationHelper();
        Helpers.aes = new AESHelper();

        return true;
    }

    public boolean loadComponents() {
        tilMainPassword = findViewById(R.id.tilMainPassword);
        etMainPassword = findViewById(R.id.etMainPassword);
        tilMainPasswordRepeat = findViewById(R.id.tilMainPasswordRepeat);
        etMainPasswordRepeat = findViewById(R.id.etMainPasswordRepeat);

        return true;
    }

    public boolean firstOpen() {
        if (!Helpers.config.getBoolean("first_open", true)) {
            Helpers.logger.info("girdi");
            return true;
        }

        CryptHelper cryptHelper = new CryptHelper();

        ResultObject resultGenerateKeys = cryptHelper.generateKeys();

        if (resultGenerateKeys.isFailure()) {
            //todo
            Helpers.logger.info("Key üretilemedi");
            return false;
        }

        KeyPair keyPair = (KeyPair) resultGenerateKeys.getData();

        ResultObject resultPrivateKeyToString = cryptHelper.keyToString(keyPair.getPrivate());
        ResultObject resultPublicKeyToString = cryptHelper.keyToString(keyPair.getPublic());

        if (resultPrivateKeyToString.isFailure() || resultPublicKeyToString.isFailure()) {
            //todo
            Helpers.logger.info("Keyler base64e atanamadı");
            return false;
        }

        boolean setPrivateKey = Helpers.config.setString("private_key", String.valueOf(resultPrivateKeyToString.getData()));
        boolean setPublicKey = Helpers.config.setString("public_key", String.valueOf(resultPublicKeyToString.getData()));

        if (!(setPrivateKey && setPublicKey)) {
            //todo
            Helpers.logger.info("keyler shared prefe aktarılamadı");
            return false;
        }

        String name = cryptHelper.quickEncrypt("test");
        String password = cryptHelper.quickEncrypt("password");

        PasswordModel passwordModel = new PasswordModel(name, password, "");
        ResultObject resultInsert = passwordModel.insert();

        if (resultInsert.isSuccess()) {
            Helpers.config.setBoolean("first_open", false);
        }

        Helpers.config.setString("device_id", Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));

        return true;

        //todo şifre alma kaydetme
    }

    public void login() {


        if (!etMainPassword.getText().toString().equals("1111")) {
            etMainPassword.setError("Olmadı");

            return;
        }

        Global.PASSWORD = etMainPassword.getText().toString();

        findViewById(R.id.appBar).setVisibility(View.VISIBLE);
        findViewById(R.id.llMainPassword).setVisibility(View.INVISIBLE);

        Helpers.loading.show();

        ColorConstants.colorItem = new ColorObject[]{
                new ColorObject(Helpers.resource.getColor(R.color.pink), Helpers.resource.getColor(R.color.tintPink)),
                new ColorObject(Helpers.resource.getColor(R.color.lightBlue), Helpers.resource.getColor(R.color.tintLightBlue)),
                new ColorObject(Helpers.resource.getColor(R.color.amber), Helpers.resource.getColor(R.color.tintAmber)),
                new ColorObject(Helpers.resource.getColor(R.color.red), Helpers.resource.getColor(R.color.tintRed)),
                new ColorObject(Helpers.resource.getColor(R.color.purple), Helpers.resource.getColor(R.color.tintPurple)),
                new ColorObject(Helpers.resource.getColor(R.color.deepOrange), Helpers.resource.getColor(R.color.tintDeepOrange))
        };

        Global.SECTION_PAGER_ADAPTER = new SectionsPagerAdapter(this, getSupportFragmentManager());
        Global.VIEW_PAGER = findViewById(R.id.view_pager);
        Global.VIEW_PAGER.setAdapter(Global.SECTION_PAGER_ADAPTER);
        Global.TAB_LAYOUT = findViewById(R.id.tabs);
        Global.TAB_LAYOUT.setupWithViewPager(Global.VIEW_PAGER);

        Global.TAB_LAYOUT.getTabAt(Config.TAB_HOME_INDEX).select();

        Helpers.loading.hide();
    }

    private void registered() {
        etMainPasswordRepeat.setText("");
        tilMainPasswordRepeat.setErrorEnabled(false);
        etMainPassword.setText("");
        tilMainPassword.setErrorEnabled(false);

        if (Helpers.config.getBoolean("registered", false)) {
            tilMainPasswordRepeat.setVisibility(View.INVISIBLE);

            ((MaterialButton) findViewById(R.id.btnLogin)).setText(R.string.login_failed);
            ((MaterialButton) findViewById(R.id.btnLogin)).setIcon(Helpers.resource.getDrawable(R.drawable.ic_person_24dp));

            tilMainPassword.setHelperText(Helpers.resource.getString(R.string.input_password_edit));
            tilMainPassword.setHint(Helpers.resource.getString(R.string.hint_password_edit));

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
                            //todo

                            return;
                        }

                        String encryptedBase64PrivateKey = String.valueOf(resultEncryptBase64PrivateKey.getData());
                        String encryptedBase64PublicKey = String.valueOf(resultEncryptBase64PublicKey.getData());

                        boolean setBase64PrivateKey = Helpers.config.setString("private_key", encryptedBase64PrivateKey);
                        boolean setBase64PublicKey = Helpers.config.setString("public_key", encryptedBase64PublicKey);

                        if (setBase64PrivateKey && setBase64PublicKey) {
                            //TODO HERŞEY OKEY

                            Helpers.logger.info("Kayıt Oldu");

                            CryptHelper cryptHelper = new CryptHelper();
                            cryptHelper.setPrivateKey((String) Helpers.aes.decrypt(encryptedBase64PrivateKey, Global.PASSWORD).getData());
                            cryptHelper.setPublicKey((String) Helpers.aes.decrypt(encryptedBase64PublicKey, Global.PASSWORD).getData());

                            String confirmText = cryptHelper.generateValidationText();
                            Helpers.logger.info(1, "step");
                            ResultObject resultRSAEncrypt = cryptHelper.encrypt(confirmText, cryptHelper.getPrivateKey());

                            if (resultRSAEncrypt.isFailure()) {
                                //todo
                                Helpers.logger.info("HATA RSA");
                            }
                            Helpers.logger.info(2, "step");
                            ResultObject resultAESEncrypt = Helpers.aes.encrypt((String) resultRSAEncrypt.getData(), Global.PASSWORD);

                            if (resultAESEncrypt.isFailure()){
                                Helpers.logger.info("HATA AES");
                                //todo
                            }
                            Helpers.logger.info(3, "step");
                            Helpers.config.setString("confirm_password", (String) resultAESEncrypt.getData());
                            Helpers.config.setBoolean("registered", true);

                            registered();
                        } else {

                        }

                    }


//todo                    login();
                }
            });
        }
    }
}
