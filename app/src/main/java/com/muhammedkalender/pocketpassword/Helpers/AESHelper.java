package com.muhammedkalender.pocketpassword.Helpers;

import android.util.Base64;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AESHelper {
    //region Customize Secret

    private String customizeSecret(String secret) {
        try {
            //TODO BELKİ SONRA ?
            String caesar = Helpers.config.getString("caesar_pattern", "");
            int[] steps = new int[8];


            if (caesar.equals("")) {
                Random random = new Random();

                for (int i = 0; i < 8; i++) {
                    int step = random.nextInt(6);

                    caesar += step;
                }
            }

            byte[] caesarArray = caesar.getBytes();

            for (int i = 0; i < 8; i++) {
                steps[i] = caesarArray[i];
            }

            String newSecret = secret;

            byte[] secretArray = secret.getBytes(UTF_8);

            for (int i = 0; i < secretArray.length; i++) {
                if (i % 2 == 0 && steps[i] % 2 == 0) {
                    int index = (i + steps[i]) % secretArray.length;

                    caesarArray[index] = caesarArray[caesarArray.length % (index - (steps[i] + steps[steps.length - 1]))];
                }
//                secretArray
            }
        } catch (Exception e) {

        }

        return secret;
    }

    //endregion

    //region Secret To Key

    public SecretKeySpec secretToKey(String secret) {
        try {
            secret = customizeSecret(secret);

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] key = messageDigest.digest(secret.getBytes(UTF_8));

            return new SecretKeySpec(Arrays.copyOf(key, 32), "AES");
        } catch (Exception e) {
            Helpers.logger.error(ErrorCodeConstants.AES_SECRET_TO_KEY, e);
            return null;
        }
    }

    //endregion

    //region Encrypt

    public ResultObject encrypt(String pureData, String secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretToKey(secret));

            return new ResultObject()
                    .setData(Base64.encodeToString(cipher.doFinal(pureData.getBytes(UTF_8)), Base64.DEFAULT));
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.AES_ENCRYPT)
                    .setError(e);
        }
    }

    //endregion

    //region Decrypt

    public ResultObject decrypt(String encryptedData, String secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretToKey(secret));

            return new ResultObject()
                    .setData(new String(cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT))));
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.AES_DECRYPT)
                    .setError(e);
        }
    }

    //endregion
}
