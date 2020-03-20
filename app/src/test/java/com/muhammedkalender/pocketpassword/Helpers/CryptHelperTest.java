package com.muhammedkalender.pocketpassword.Helpers;

import android.util.Base64;

import com.muhammedkalender.pocketpassword.MainActivity;

import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

public class CryptHelperTest {
    @Test
    public void encryptAndDecryptData(){

        String d = null;
        String customData = "aAaAaAaA";


        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(4096);
            KeyPair keys = keyPairGenerator.generateKeyPair();

            PrivateKey keyPrivate = keys.getPrivate();
            PublicKey keyPublic = keys.getPublic();

            CryptHelper cryptHelper = new CryptHelper();



            String base64Public = String.valueOf(Base64.decode(Base64.encodeToString(keyPublic.getEncoded(), Base64.DEFAULT) , Base64.DEFAULT));
            String base64Private = String.valueOf(Base64.decode(Base64.encodeToString(keyPrivate.getEncoded(), Base64.DEFAULT) , Base64.DEFAULT));

            String e = cryptHelper.encrypt(customData, base64Public);
             d = cryptHelper.decrypt(e, base64Private);




        }catch (Exception e){
            String b = e.getMessage();
        }

        assertEquals(customData, d);
    }
}
