package com.muhammedkalender.pocketpassword.Helpers;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.MainActivity;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.transform.Result;

import static java.nio.charset.StandardCharsets.UTF_8;

//RSA
public class CryptHelper {
    //region Variables

    private PublicKey publicKey;
    private PrivateKey privateKey;

    //endregion

    //region Quick Encrypt

    public String quickEncrypt(@NonNull String pureData) {
        return quickEncrypt(pureData, privateKey);
    }

    public String quickEncrypt(@NonNull String pureData, @NonNull String base64PrivateKey) {
        try {
            ResultObject resultGeneratePrivateKeyFromString = privateKeyFromString(base64PrivateKey);

            if (resultGeneratePrivateKeyFromString.isSuccess()) {
                return quickEncrypt(pureData, (PrivateKey) resultGeneratePrivateKeyFromString.getData());
            } else {
                return Config.DEFAULT_STRING;
            }
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.CRYPT_QUICK_ENCRYPT_WITH_BASE64, e);

            return pureData;
        }
    }

    public String quickEncrypt(@NonNull String pureData, @NonNull PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            return new String(Base64.encode(cipher.doFinal(pureData.getBytes(UTF_8)), Base64.DEFAULT));
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.CRYPT_QUICK_ENCRYPT, e);

            return pureData;
        }
    }

    //endregion

    //region Quick Decrypt

    public String quickDecrypt(@NonNull String encryptedData) {
        return quickDecrypt(encryptedData, publicKey);
    }

    public String quickDecrypt(@NonNull String encryptedData, @NonNull String base64PublicKey) {
        try {
            ResultObject resultGeneratePublicKeyFromString = publicKeyFromString(base64PublicKey);

            if (resultGeneratePublicKeyFromString.isSuccess()) {
                return quickDecrypt(encryptedData, (PublicKey) resultGeneratePublicKeyFromString.getData());
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.CRYPT_QUICK_DECRYPT_WITH_BASE64, e);

            return encryptedData;
        }
    }

    public String quickDecrypt(@NonNull String encryptedData, @NonNull PublicKey publicKey) {
        try {
            Cipher cipherD = Cipher.getInstance("RSA");
            cipherD.init(Cipher.DECRYPT_MODE, publicKey);

            return new String(cipherD.doFinal(Base64.decode(encryptedData, Base64.DEFAULT)));
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.CRYPT_QUICK_DECRYPT, e);

            return encryptedData;
        }
    }

    //endregion

    //region Encrypt

    public ResultObject encrypt(@NonNull String pureData, @NonNull String base64PrivateKey) {
        try {
            ResultObject resultGeneratePrivateKeyFromString = privateKeyFromString(base64PrivateKey);

            if (resultGeneratePrivateKeyFromString.isSuccess()) {
                return encrypt(pureData, (PrivateKey) resultGeneratePrivateKeyFromString.getData());
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.CRYPT_ENCRYPT_WITH_BASE64)
                    .setError(e);
        }
    }

    public ResultObject encrypt(@NonNull String pureData, @NonNull PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            Helpers.logger.info(pureData);

            return new ResultObject()
                    .setData(Base64.encodeToString(cipher.doFinal(pureData.getBytes(UTF_8)), Base64.DEFAULT));
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.CRYPT_ENCRYPT)
                    .setError(e);
        }
    }

    //endregion

    //region Decrypt

    public ResultObject decrypt(@NonNull String encryptedData, @NonNull String base64PublicKey) {
        try {
            ResultObject resultGeneratePublicKeyFromString = publicKeyFromString(base64PublicKey);

            if (resultGeneratePublicKeyFromString.isSuccess()) {
                return decrypt(encryptedData, (PublicKey) resultGeneratePublicKeyFromString.getData());
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.CRYPT_DECRYPT_WITH_BASE64)
                    .setError(e);
        }
    }

    public ResultObject decrypt(@NonNull String encryptedData, @NonNull PublicKey publicKey) {
        try {
            Cipher cipherD = Cipher.getInstance("RSA");
            cipherD.init(Cipher.DECRYPT_MODE, publicKey);

            return new ResultObject()
                    .setData(new String(cipherD.doFinal(Base64.decode(encryptedData, Base64.DEFAULT))));
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.CRYPT_DECRYPT)
                    .setError(e);
        }
    }

    //endregion

    //region Generate

    public ResultObject generateKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(Config.RSA_KEY_SIZE);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();

            return new ResultObject()
                    .setData(keyPair);
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.CRYPT_GENERATE_KEY)
                    .setError(e);
        }
    }

    public String generateValidationText() {
        byte[] bytes = new byte[512];

        byte[] deviceID = Helpers.config.getString("device_id").getBytes(UTF_8);

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = deviceID[i % deviceID.length];
        }

        return new String(bytes);
    }

    //endregion

    //region Key Encryption

    public ResultObject keyToString(@NonNull PublicKey publicKey) {
        try {
            return new ResultObject()
                    .setData(Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT));
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.CRYPT_KEY_TO_STRING)
                    .setError(e);
        }
    }

    public ResultObject keyToString(@NonNull PrivateKey privateKey) {
        try {
            return new ResultObject()
                    .setData(Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT));
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.CRYPT_KEY_TO_STRING)
                    .setError(e);
        }
    }

    //endregion

    //region Key Decryption

    public ResultObject publicKeyFromString(@NonNull String base64) {
        try {
            X509EncodedKeySpec keySpecPub = new X509EncodedKeySpec(Base64.decode(base64, Base64.DEFAULT));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpecPub);

            return new ResultObject()
                    .setData(publicKey);
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.PUBLIC_KEY_FROM_STRING)
                    .setError(e);
        }
    }

    public ResultObject privateKeyFromString(@NonNull String base64) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(base64, Base64.DEFAULT));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            return new ResultObject()
                    .setData(privateKey);
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.PRIVATE_KEY_FROM_STRING)
                    .setError(e);
        }
    }

    //endregion

    //region Public Key Getters & Setters

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void setPublicKey(String base64PublicKey) {
        ResultObject resultPublicKeyFromString = publicKeyFromString(base64PublicKey);

        if (resultPublicKeyFromString.isSuccess()) {
            this.publicKey = (PublicKey) resultPublicKeyFromString.getData();
        }
    }

    //endregion

    //region Private Key Getters & Setters

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public void setPrivateKey(String base64PrivateKey) {
        ResultObject resultPrivateKeyFromString = privateKeyFromString(base64PrivateKey);

        if (resultPrivateKeyFromString.isSuccess()) {
            this.privateKey = (PrivateKey) resultPrivateKeyFromString.getData();
        }
    }

    //endregion

    //region Loaders

    public static CryptHelper buildDefault() {
        CryptHelper cryptHelper = new CryptHelper();

        return cryptHelper.loadDefaultKeys() ? cryptHelper : null;
    }

    public boolean loadDefaultKeys() {
        String privateKey = Helpers.config.getString("private_key");
        String publicKey = Helpers.config.getString("public_key");

        ResultObject resultDecryptPrivateKey = Helpers.aes.decrypt(privateKey, Global.PASSWORD);
        ResultObject resultDecryptPublicKey = Helpers.aes.decrypt(publicKey, Global.PASSWORD);

        if (resultDecryptPrivateKey.isFailure() || resultDecryptPublicKey.isFailure()) {
            Helpers.logger.error(-1, "Keyler aes ile çözülemedi");

            return false;
        }

        privateKey = (String) resultDecryptPrivateKey.getData();
        publicKey = (String) resultDecryptPublicKey.getData();

        ResultObject resultPrivateKeyFromString = privateKeyFromString(privateKey);
        ResultObject resultPublicKeyFromString = publicKeyFromString(publicKey);

        if (resultPrivateKeyFromString.isFailure() || resultPublicKeyFromString.isFailure()) {
            Helpers.logger.error(-1, "Keyler RSA Keyine Dönüştürülürken Patladı");

            return false;
        }

        this.setPrivateKey((PrivateKey) resultPrivateKeyFromString.getData());
        this.setPublicKey((PublicKey) resultPublicKeyFromString.getData());

        return true;
    }

    //endregion
}
