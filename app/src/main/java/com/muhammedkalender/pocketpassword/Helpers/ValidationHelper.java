package com.muhammedkalender.pocketpassword.Helpers;

import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

public class ValidationHelper {
    public static int PASSWORD_STRONG = 1, PASSWORD_NORMAL = 2, PASSWORD_WEAK = 3;

    //https://stackoverflow.com/a/3802238
    public boolean checkPassword(String password, int type) {
        if (type == PASSWORD_STRONG) {
            /*
                ^                 # start-of-string
                (?=.*[0-9])       # a digit must occur at least once
                (?=.*[a-z])       # a lower case letter must occur at least once
                (?=.*[A-Z])       # an upper case letter must occur at least once
                (?=.*[@#$%^&+=])  # a special character must occur at least once
                (?=\S+$)          # no whitespace allowed in the entire string
                .{8,}             # anything, at least eight places though
                $                 # end-of-string
             */

            String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$\\-_+=])(?=\\S+$).{8,}$";

            return password.matches(pattern);
        }

        return false;
    }

    public ResultObject encryptPassword(String password) {
        try {
            CryptHelper cryptHelper = CryptHelper.buildDefault();
            SHAHelper shaHelper = new SHAHelper();

            String encryptedText = (String) Helpers.aes.encrypt(password, password).getData();
            encryptedText = cryptHelper.quickEncrypt(encryptedText);
            encryptedText = (String) shaHelper.encrypt(encryptedText).getData();

            return new ResultObject()
                    .setData(encryptedText);
        } catch (Exception e) {
            return new ResultObject(-999)
                    .setError(e);
        }
    }

    public boolean validPassword(String password) {
        CryptHelper cryptHelper = CryptHelper.buildDefault();

        String encryptedData = (String) cryptHelper.encrypt(password, cryptHelper.getPrivateKey()).getData();
        encryptedData = (String) Helpers.aes.encrypt(encryptedData, password).getData();

        return encryptedData == Helpers.config.getString("confirm_password");
    }
}
