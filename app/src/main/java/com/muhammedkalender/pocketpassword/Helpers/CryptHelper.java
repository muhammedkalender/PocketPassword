package com.muhammedkalender.pocketpassword.Helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//RSA
public class CryptHelper {
    public String encrypt(@NonNull String pureData, @NonNull String customKey) {
        //todo

        try {


            while (customKey.length() < 256){
                customKey += customKey;
            }

            byte[] key = customKey.getBytes();

            MessageDigest sha = MessageDigest.getInstance("SHA-1");

            key = sha.digest(key);
            key = Arrays.copyOf(key, 64);

            SecretKeySpec secretKey = new SecretKeySpec(key, "AES-64");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encrypedData =  cipher.doFinal(pureData.getBytes("UTF-8"));


            return encrypedData.toString();
        } catch (Exception $e) {
            //todo
        }

        Log.e("asdsad", customKey);

        return null;
    }

    public String decrypt(@NonNull String encryptedData, @NonNull String privateKey) {
        //todo

        return null;
    }
}
