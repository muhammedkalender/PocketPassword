package com.muhammedkalender.pocketpassword.Helpers;

import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Objects.ResultObject;

import java.security.MessageDigest;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SHAHelper {
    //region Encrypt

    //https://www.baeldung.com/sha-256-hashing-java
    public ResultObject encrypt(String pureData) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
                    pureData.getBytes(UTF_8));

            return new ResultObject()
                    .setData(new String(encodedHash));
        } catch (Exception e) {
            return new ResultObject(ErrorCodeConstants.AES_ENCRYPT)
                    .setError(e);
        }
    }

    //endregion
}
