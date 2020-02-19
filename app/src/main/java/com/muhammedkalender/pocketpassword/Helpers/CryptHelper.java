package com.muhammedkalender.pocketpassword.Helpers;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
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

//RSA
public class CryptHelper {
    //region Encrypt

    public String encrypt(@NonNull String pureData, @NonNull String base64PrivateKey) {
        try {
            ResultObject resultGeneratePrivateKeyFromString = privateKeyFromString(base64PrivateKey);

            if (resultGeneratePrivateKeyFromString.isSuccess()) {
                return encrypt(pureData, (PrivateKey) resultGeneratePrivateKeyFromString.getData());
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.CRYPT_ENCRYPT_WITH_BASE64, e);

            return ""; //todo
        }
    }

    public String encrypt(@NonNull String pureData, @NonNull PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeToString(cipher.doFinal(pureData.getBytes()), Base64.DEFAULT);
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.CRYPT_ENCRYPT, e);

            return ""; //todo
        }
    }

    //endregion

    //region Decrypt

    public String decrypt(@NonNull String encryptedData, @NonNull String base64PublicKey) {
        try {
            ResultObject resultGeneratePublicKeyFromString = publicKeyFromString(base64PublicKey);

            if (resultGeneratePublicKeyFromString.isSuccess()) {
                return decrypt(encryptedData, (PublicKey) resultGeneratePublicKeyFromString.getData());
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.CRYPT_DECRYPT_WITH_BASE64, e);
            return ""; //todo
        }
    }

    public String decrypt(@NonNull String encryptedData, @NonNull PublicKey publicKey) {
        try {
            Cipher cipherD = Cipher.getInstance("RSA");
            cipherD.init(Cipher.DECRYPT_MODE, publicKey);

            return new String(cipherD.doFinal(Base64.decode(encryptedData, Base64.DEFAULT)));
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.CRYPT_DECRYPT, e);
            return ""; //todo
        }
    }

    //endregion

    //region Generate

    public ResultObject generateKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(Config.RSA_KEY_SIZE);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            return new ResultObject()
                    .setData(keyPair);
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.CRYPT_GENERATE_KEY)
                    .setError(e);
        }
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
}
