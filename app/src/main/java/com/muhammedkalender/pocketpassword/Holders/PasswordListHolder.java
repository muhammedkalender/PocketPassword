package com.muhammedkalender.pocketpassword.Holders;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muhammedkalender.pocketpassword.MainActivity;
import com.muhammedkalender.pocketpassword.R;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

public class PasswordListHolder extends RecyclerView.ViewHolder {
    public TextView tvName;
public String encrypedText = null;
    public PasswordListHolder(Context context, View view) {
        super(view);

        this.tvName = view.findViewById(R.id.tvName);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String rawData = "testTESTtestTEST";

                    Log.e("st","Start Enc");


                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE, MainActivity.keyPublic);
                    byte[] encrypted = cipher.doFinal(rawData.getBytes());

                    //Log.e("key", Base64.encodeToString(MainActivity.keyPublic.getEncoded(), Base64.DEFAULT));
                   // Log.e("pri", Base64.encodeToString(MainActivity.keyPrivate.getEncoded(), Base64.DEFAULT));


                    encrypedText = Base64.encodeToString(encrypted, Base64.DEFAULT);

                    Log.e("ENCRYPED", encrypedText);
                    Log.e("st", "End Enc");
                }catch (Exception $e){

                }
            }
        });

        view.findViewById(R.id.ivShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    Log.e("Decy", "Start");
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.DECRYPT_MODE, MainActivity.keyPrivate);

                    byte[] dectypted = cipher.doFinal(Base64.decode(encrypedText, Base64.DEFAULT));
                    Log.e("Decyped Text",  new String(Base64.decode(Base64.encodeToString(dectypted, Base64.DEFAULT), Base64.DEFAULT))+"");

                }catch (Exception e){
Log.e("ZZZ", e.getMessage());
                }
            }
        });

        view.findViewById(R.id.ivForward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                 Log.e("WRONG DECY", "Start");

                    Cipher cipher = Cipher.getInstance("RSA");
                    byte[] ley = Base64.decode(Base64.encodeToString(MainActivity.keyPrivate.getEncoded(), Base64.DEFAULT) , Base64.DEFAULT);

                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(ley);

                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    PrivateKey temp = kf.generatePrivate(keySpec);

                    cipher.init(Cipher.DECRYPT_MODE, temp);

                    byte[] dectypted = cipher.doFinal(Base64.decode(encrypedText, Base64.DEFAULT));

                    Log.e("Wrong Text",  new String(Base64.decode(Base64.encodeToString(dectypted, Base64.DEFAULT), Base64.DEFAULT))+"");

                    Log.e("WRONG DECY", "End");
                }catch (Exception e){
                    Log.e("asdsa", e.getMessage());
                }
            }
        });

        //  super(itemRowBinding.getRoot());
        //  this.itemRowBinding = itemRowBinding;
    }
}
