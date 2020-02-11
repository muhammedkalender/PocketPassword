package com.muhammedkalender.pocketpassword.Helpers;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.muhammedkalender.pocketpassword.MainActivity;

import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//RSA
public class CryptHelper {
    public String encrypt(@NonNull String pureData, @NonNull String customKey) {
        //todo

        try {


            while (customKey.length() < 256) {
                customKey += customKey;
            }

            byte[] key = customKey.getBytes();

            MessageDigest sha = MessageDigest.getInstance("SHA-1");

            key = sha.digest(key);
            key = Arrays.copyOf(key, 64);

            SecretKeySpec secretKey = new SecretKeySpec(key, "AES-64");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encrypedData = cipher.doFinal(pureData.getBytes("UTF-8"));


            return encrypedData.toString();
        } catch (Exception $e) {
            //todo
        }

        Log.e("asdsad", customKey);

        return null;
    }

    public String decrypt(@NonNull String encryptedData, @NonNull String base64PrivateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64PrivateKey.getBytes());

            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(keySpec);

            return decrypt(encryptedData, privateKey);
        } catch (Exception e) {

            return ""; //todo
        }
    }

    public String decrypt(@NonNull String encryptedData, @NonNull PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] dectypted = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT));

            return new String(dectypted);
        } catch (Exception e) {

            return ""; //todo
        }
    }
}
