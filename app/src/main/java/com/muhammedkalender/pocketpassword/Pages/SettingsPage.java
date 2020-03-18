package com.muhammedkalender.pocketpassword.Pages;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypertrack.hyperlog.HLCallback;
import com.hypertrack.hyperlog.HyperLog;
import com.muhammedkalender.pocketpassword.Abstracts.PageAbstract;
import com.muhammedkalender.pocketpassword.Components.AlertDialogComponent;
import com.muhammedkalender.pocketpassword.Components.SnackbarComponent;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Constants.ConfigKeys;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Constants.RequestCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.AESHelper;
import com.muhammedkalender.pocketpassword.Helpers.CryptHelper;
import com.muhammedkalender.pocketpassword.Helpers.ValidationHelper;
import com.muhammedkalender.pocketpassword.Interfaces.PageInterface;
import com.muhammedkalender.pocketpassword.MainActivity;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.Objects.ColorObject;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;
import com.muhammedkalender.pocketpassword.R;
import com.muhammedkalender.pocketpassword.ui.main.SectionsPagerAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.spec.SecretKeySpec;

public class SettingsPage extends PageAbstract implements PageInterface {
    //region UI Components

    //region Switch Configs

    private SwitchMaterial switchOnlyLogin, switchHideView, switchDisableErrorLog, switchDisableInfoLog;

    //endregion

    //region Login

    private LinearLayout llLogin;

    //endregion

    //region Change Password

    private TextInputLayout tilChangePassword, tilChangePasswordRepeat;
    private TextInputEditText etChangePassword, etChangePasswordRepeat;

    //endregion

    //endregion

    //region Initialize

    @Override
    public void initialize(View viewRoot) {
        Global.PAGE_SETTINGS = this;

        this.viewRoot = viewRoot;

        //region Switches

// TODO       switchOnlyLogin = this.viewRoot.findViewById(R.id.switchOnlyLogin);
//        switchOnlyLogin.setChecked(Config.CONFIG_ONLY_LOGIN);
//        switchOnlyLogin.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (Helpers.config.setBoolean("only_login", isChecked)) {
//                Config.CONFIG_ONLY_LOGIN = isChecked;
//
//                buttonView.setChecked(isChecked);
//            } else {
//                buttonView.setChecked(!isChecked);
//            }
//        });

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

        ((TextInputEditText) this.viewRoot.findViewById(R.id.etPassword)).setText("");
        ((TextInputLayout) this.viewRoot.findViewById(R.id.tilPassword)).setErrorEnabled(false);

        //endregion

        //region Reset Page

        ScrollView svSettings = this.viewRoot.findViewById(R.id.svSettings);

        svSettings.post(() -> {
            svSettings.setScrollX(0);
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

        //region Change Password

        //region Viewler Atanıyor

        etChangePassword = this.viewRoot.findViewById(R.id.etChangePassword);
        tilChangePassword = this.viewRoot.findViewById(R.id.tilChangePassword);
        etChangePasswordRepeat = this.viewRoot.findViewById(R.id.etChangePasswordRepeat);
        tilChangePasswordRepeat = this.viewRoot.findViewById(R.id.tilChangePasswordRepeat);

        //endregion

        //region Listener

        this.viewRoot.findViewById(R.id.btnChangePassword).setOnClickListener(v -> {
            try {
                //region Text Validation

                if (etChangePassword.getText() == null || etChangePassword.getText().toString().equals("")) {
                    tilChangePassword.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.password)));

                    return;
                }

                final String password = etChangePassword.getText().toString();

                if (password.length() < 8) {
                    tilChangePassword.setError(Helpers.resource.getString(R.string.min_length, "", Helpers.resource.getString(R.string.password), 8));

                    return;
                } else if (password.length() > 32) {
                    tilChangePassword.setError(Helpers.resource.getString(R.string.max_length, "", Helpers.resource.getString(R.string.password), 32));

                    return;
                } else if (!Helpers.validation.checkPassword(password, ValidationHelper.PASSWORD_STRONG)) {
                    tilChangePassword.setError(Helpers.resource.getString(R.string.password_must_strong));

                    return;
                } else {
                    tilChangePassword.setErrorEnabled(false);

                    if (etChangePasswordRepeat.getText() == null || etChangePasswordRepeat.getText().toString().equals("")) {
                        tilChangePasswordRepeat.setError(Helpers.resource.getString(R.string.not_null, "", Helpers.resource.getString(R.string.password)));

                        return;
                    }

                    String passwordRepeat = etChangePasswordRepeat.getText().toString();

                    if (password.equals(passwordRepeat)) {
                        tilChangePasswordRepeat.setErrorEnabled(false);
                    } else {
                        tilChangePasswordRepeat.setError(Helpers.resource.getString(R.string.password_not_match));

                        return;
                    }
                }

                ((Activity) Global.CONTEXT).runOnUiThread(() -> {
                    etChangePassword.setText("");
                    tilChangePassword.setErrorEnabled(false);
                    etChangePasswordRepeat.setText("");
                    tilChangePasswordRepeat.setErrorEnabled(false);
                });

                Helpers.loading.show();

                //endregion

                new Thread(() -> {
                    try {
                        //region Ön Şifrelem Elemanları Ayarlarnıyor

                        //Yeni şifreler türetiliyor
                        CryptHelper cryptHelper = new CryptHelper();
                        cryptHelper.generateKeys();

                        AESHelper aesHelper = new AESHelper();

                        String encryptedPrivateKey = (String) aesHelper.encrypt(
                                ((String) cryptHelper.keyToString(cryptHelper.getPrivateKey()).getData())
                                , password).getData();

                        //cryptHelper.setPrivateKey((String) aesHelper.decrypt(encryptedPrivateKey, password).getData());

                        String encryptedPublicKey = (String) aesHelper.encrypt(
                                ((String) cryptHelper.keyToString(cryptHelper.getPublicKey()).getData())
                                , password).getData();

                        //cryptHelper.setPublicKey((String) aesHelper.decrypt(encryptedPublicKey, password).getData());


                        //Yeni şifrelerle hazırlanmış şifreleme sınıfı
                        //Decode - Encode sırasında problem oluşursah ata almak için ayrı sınıf kullanılıyor
                        CryptHelper _cryptHelper = new CryptHelper();
                        _cryptHelper.setPublicKey(
                                (String) aesHelper.decrypt(
                                        encryptedPublicKey, password
                                ).getData()
                        );

                        _cryptHelper.setPrivateKey(
                                (String) aesHelper.decrypt(
                                        encryptedPrivateKey, password
                                ).getData()
                        );

                        //endregion

                        //region Listeler Çıkartılıyor

                        //Şifrelerin listesi çekiliyor
                        //Mantık : Şifre listesi > Şifre Decode > Yeni Şifre İle Encode
                        PasswordModel passwordModelHelper = new PasswordModel();
                        List<PasswordModel> passwordModels = passwordModelHelper.select();

                        //Hata durumunda geri dönebilmek için, modellerin yedeği alınıyor
                        List<PasswordModel> _passwordModels = passwordModels;


                        //Hata oluşursa listeye ve sayaça işliyecek
                        int errorCount = 0, indexProcess = 0;
                        List<PasswordModel> listFailureModels = new ArrayList<>();
                        boolean[] isProcessed = new boolean[passwordModels.size()];

                        //endregion

                        //region Şifreleme Deneniyor

                        for (PasswordModel passwordModel : passwordModels) {
                            //Hesap & Şifre decode ediliyor
                            passwordModel.decrypt();

                            //Hesap & Şifre yeni şifre ile encode ediliyor
                            ResultObject resultEncryptAccount = _cryptHelper.encrypt(passwordModel.getAccount());
                            ResultObject resultEncryptPassword = _cryptHelper.encrypt(passwordModel.getPassword());

                            //Hata kontrolü
                            if (resultEncryptAccount.isFailure() || resultEncryptPassword.isFailure()) {
                                errorCount++;
                                listFailureModels.add(passwordModel);
                            }
                        }

                        //endregion

                        //Ön Deoode ve Encode işlemi gibi düşünülebilir
                        //Muhtemelen şimdiki işlem sırasındada hata alınmayacak, önceden denenmiş oldu
                        if (errorCount != 0) {
                            //region Ön Denemede Hata Oluştu

                            //Ön denemede sorun oluştu, muhtemelen asıl denemedede sorun oluşacak, işlem iptal ediliyor

                            new AlertDialogComponent()
                                    .setTitle(R.string.title_failure_change_password)
                                    .setMessage(R.string.message_failure_change_password__pre_error)
                                    .show();

                            Helpers.loading.hide();

                            return;

                            //endregion
                        } else {
                            //region Şifreler güncelleniyor

                            //Yeni işlemlerde hata kontrolü yapabilmek için liste ve sayaç sıfırlanıyor
                            listFailureModels.clear();
                            errorCount = 0;

                            for (PasswordModel passwordModel : passwordModels) {
                                //Öncelikle şifreli veriler çözülüyor
                                passwordModel.decrypt();

                                //Hesap & Şifre şifreleniyor
                                ResultObject resultEncryptAccount = _cryptHelper.encrypt(passwordModel.getAccount());
                                ResultObject resultEncryptPassword = _cryptHelper.encrypt(passwordModel.getPassword());

                                if (resultEncryptAccount.isFailure() || resultEncryptPassword.isFailure()) {
                                    errorCount++;
                                    listFailureModels.add(passwordModel);

                                    continue;
                                }

                                //Yeniden şifrelenmiş Hesap & Şifre String olarak alınıyor
                                String strEncryptedAccount = resultEncryptAccount.getDataAsString();
                                String strEncryptedPassword = resultEncryptPassword.getDataAsString();

                                //Manuel olarak şifreli veriler girileceğindne modelin çözümü false yapılıyor
                                passwordModel.setDecrypted(false);

                                //Şifreli veriler modele aktarılıyor
                                passwordModel.setAccount(strEncryptedAccount);
                                passwordModel.setPassword(strEncryptedPassword);

                                //Update ile yeni veriler veritabanına aktarılıyor
                                ResultObject resultUpdate = passwordModel.update();

                                if (resultUpdate.isFailure()) {
                                    //İlgili modelin işlendiğini işaretliyoruz, hata durumunda geri dönmek için
                                    errorCount++;
                                    listFailureModels.add(passwordModel);
                                    isProcessed[indexProcess++] = true;
                                } else {
                                    //İlgili modelin işlendiğini işaretliyoruz, hata durumunda geri dönmek için
                                    isProcessed[indexProcess++] = true;
                                }
                            }

                            //endregion

                            if (errorCount == 0) {
                                //Hata olmadan tüm veriler aktarıldı demek

                                //region region Keyler Ayarlanıyor

                                //Set edilecek dataların yedeği

                                String _privateKey = Helpers.config.getString(ConfigKeys.PRIVATE_KEY);
                                String _publicKey = Helpers.config.getString(ConfigKeys.PUBLIC_KEY);
                                String _confirmPassword = Helpers.config.getString(ConfigKeys.CONFIRM_TEXT);

                                //Yeni şifre ayarlara geçiliyor
                                boolean setPrivateKey = Helpers.config.setString(ConfigKeys.PRIVATE_KEY, encryptedPrivateKey);
                                boolean setPublicKey = Helpers.config.setString(ConfigKeys.PUBLIC_KEY, encryptedPublicKey);

                                //Şifre onaylama metni ayarlara geçiliyor
                                boolean setConfirmPassword = Helpers.config.setString(ConfigKeys.CONFIRM_TEXT,
                                        aesHelper.encrypt(
                                                _cryptHelper.quickEncrypt(
                                                        _cryptHelper.generateValidationText()
                                                ),
                                                password).getDataAsString()
                                );

                                if (!setPrivateKey || !setPublicKey || !setConfirmPassword) {
                                    //Yeni şifreler set edilirken sorun oluştu

                                    //Tekrar deneniyor
                                    setPrivateKey = Helpers.config.setString(ConfigKeys.PRIVATE_KEY, encryptedPrivateKey);
                                    setPublicKey = Helpers.config.setString(ConfigKeys.PUBLIC_KEY, encryptedPublicKey);
                                    setConfirmPassword = Helpers.config.setString(ConfigKeys.CONFIRM_TEXT,
                                            aesHelper.encrypt(
                                                    _cryptHelper.quickEncrypt(
                                                            _cryptHelper.generateValidationText()
                                                    ),
                                                    password).getDataAsString()
                                    );

                                    if (!setPrivateKey || !setPublicKey || !setConfirmPassword) {
                                        //Tekrar patladı

                                        Helpers.config.setString(ConfigKeys.PRIVATE_KEY, _privateKey);
                                        Helpers.config.setString(ConfigKeys.PUBLIC_KEY, _publicKey);
                                        Helpers.config.setString(ConfigKeys.CONFIRM_TEXT, _confirmPassword);

                                        new AlertDialogComponent()
                                                .setTitle(R.string.title_failure_change_password)
                                                .setMessage(R.string.message_failure_change_password_set_data)
                                                .show();

                                        Helpers.loading.hide();

                                        return;
                                    }
                                }

                                //endregion

                                //region Yeniden Başlatmaya İhtiyaç Duymamak İçin Değişkenler Güncelleniyor

                                //Global yeni şifre geçiliyor
                                Global.PASSWORD = password;

                                //Global çözücü değiştiriliyor
                                Helpers.crypt = _cryptHelper;

                                PasswordModel passwordModel = new PasswordModel();

                                //Yeni veriler veritabanından çekiliyor
                                List<PasswordModel> list = passwordModel.select();

                                //Listeler temizlenip yeni veriler çekiliyor
                                Global.LIST_PASSWORDS.clear();
                                Global.LIST_PASSWORDS.addAll(list);
                                Global.LIST_PASSWORDS_SOLID.clear();
                                Global.LIST_PASSWORDS_SOLID.addAll(list);

                                //Adaptör uyarılıyor
                                Global.PASSWORD_ADAPTER.notifyDataSetChanged();

                                //Filtre yeniden yükleniyor
                                Global.PAGE_HOME.filter("");

                                //endregion

                                new AlertDialogComponent()
                                        .setTitle(R.string.title_success_change_password)
                                        .setMessage(R.string.message_success_change_password)
                                        .show();
                            } else {
                                //Hata alındı, dataların silinmesi gerekiyor

                                //region Geri Dönme Deneniyor

                                //Geri dönmedeki hata sayısı
                                int _errorCount = 0;

                                for (int i = 0; i < indexProcess; i++) {
                                    //Veriler şifreleniyor
                                    _passwordModels.get(i).encrypt();

                                    //Update işlemi yapılıyor
                                    ResultObject resultRevertPassword = _passwordModels.get(i).update();

                                    //Hata kontrolü yapılıyor
                                    if (resultRevertPassword.isFailure()) {
                                        _errorCount++;
                                    }
                                }

                                if (_errorCount == 0) {
                                    //Hata ile karşılaşıldı ve başarıyla geri dönüldü

                                    new AlertDialogComponent()
                                            .setTitle(R.string.title_failure_change_password)
                                            .setMessage(R.string.message_failure_change_password_and_revert)
                                            .show();

                                } else {
                                    //Hata ile karşılaşıldı ve dönüştede hata oluştu

                                    new AlertDialogComponent()
                                            .setTitle(R.string.title_failure_change_password)
                                            .setMessage(R.string.message_failure_change_password_and_failure_revert)
                                            .show();
                                }

                                //endregion
                            }
                        }

                        Helpers.loading.hide();
                    } catch (Exception e) {
                        Helpers.loading.hide();
                        Helpers.logger.error(ErrorCodeConstants.SETTINGS_CHANGE_PASSWORD_ON_THREAD, e);
                    }
                }).start();

            } catch (Exception e) {
                Helpers.loading.hide();
                Helpers.logger.error(ErrorCodeConstants.SETTINGS_CHANGE_PASSWORD, e);
            }
        });

        //endregion

        //endregion

        //region Confirm Permission

        this.viewRoot.findViewById(R.id.btnLogin).setOnClickListener(v -> {
            Helpers.loading.show();

            new Thread(() -> {
                Global.PASSWORD = ((TextInputEditText) viewRoot.findViewById(R.id.etPassword)).getText().toString();

                String confirmString = Helpers.config.getString(ConfigKeys.CONFIRM_TEXT);

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

        //endregion

        //region Import Backup

        this.viewRoot.findViewById(R.id.btnImportData).setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");
                        ((Activity) Global.CONTEXT).startActivityForResult(intent, RequestCodeConstants.IMPORT_BACKUP_SELECTED_FILE);
                    } catch (Exception e) {
                        Helpers.logger.error(ErrorCodeConstants.BACKUP_IMPORT, e);
                        //todo
                    }
                }
        );

        //endregion

        //region Export Backup

        this.viewRoot.findViewById(R.id.btnExportData).setOnClickListener(v -> {
                    try {
                        String fileName = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date()) + ".pocketpassword";

                        StringBuilder jsonFile = new StringBuilder();

                        jsonFile.append("{");

                        jsonFile.append(String.format("\"public_key\":\"%1$s\",",
                                Helpers.config.getString(ConfigKeys.PUBLIC_KEY)));

                        jsonFile.append(String.format("\"confirm_password\":\"%1$s\",",
                                Helpers.aes.encrypt(Config.EXPORT_CONFIRM_TEXT, Global.PASSWORD).getDataAsString()));

                        Helpers.logger.var("Export Şifre", Global.PASSWORD);

                        jsonFile.append("\"passwords\":[");

                        PasswordModel model = new PasswordModel();
                        int sizeActive = model.selectActive().size();

                        if(sizeActive == 0){
                            Toast.makeText(Global.CONTEXT, R.string.failure_export_null, Toast.LENGTH_LONG).show();

                            Helpers.loading.hide();

                            return;
                        }

                        List<PasswordModel> passwordModels = model.select();

                        for (PasswordModel passwordModel : passwordModels) {
                            passwordModel.encrypt();

                            jsonFile.append(String.format("{\"name\":\"%1$s\", \"account\":\"%2$s\", \"password\":\"%3$s\", \"category\"=\"%4$s\", \"color\":\"%5$s\", \"tint\":\"%6$s\", \"active\":\"%7$s\"},",
                                    passwordModel.getName(),
                                    passwordModel.getAccount(),
                                    passwordModel.getPassword(),
                                    passwordModel.getCategoryID(),
                                    passwordModel.getColor(),
                                    passwordModel.getTintColor(),
                                    passwordModel.isActive() ? '1' : '0')
                            );
                        }

                        jsonFile.append("]");

                        jsonFile.append("}");

                        Helpers.logger.info(jsonFile.toString());

                        Global.EXPORT_DATA = jsonFile.toString();

                        Intent intentExport = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                        intentExport.addCategory(Intent.CATEGORY_OPENABLE);
                        intentExport.setType("*/*");
                        intentExport.putExtra(Intent.EXTRA_TITLE, fileName);

                        ((Activity) Global.CONTEXT).startActivityForResult(intentExport, RequestCodeConstants.EXPORT_BACKUP_SELECTED_FILE);
                    } catch (Exception e) {
                        Global.EXPORT_DATA = "";
                        Helpers.logger.error(ErrorCodeConstants.BACKUP_EXPORT, e);
                        Toast.makeText(Global.CONTEXT, R.string.failure_export_data, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        //endregion

    }

    //endregion

    //region Getters & Setters

    //region Getters

    @Override
    public View getView() {
        return viewRoot;
    }

    //endregion

    //endregion
}
