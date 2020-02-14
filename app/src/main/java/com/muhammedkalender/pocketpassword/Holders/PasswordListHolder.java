package com.muhammedkalender.pocketpassword.Holders;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
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
    public View view;
    public TextView tvName;
    public ImageView ivShow, ivForward, ivClipboard;
    public LinearLayout llContainer;

    public PasswordListHolder(Context context, View view) {
        super(view);

        this.view = view;
        this.tvName = view.findViewById(R.id.tvName);
        this.ivShow = view.findViewById(R.id.ivShow);
        this.ivForward = view.findViewById(R.id.ivForward);
        this.ivClipboard = view.findViewById(R.id.ivClipboard);
        this.llContainer = view.findViewById(R.id.llContainer);
    }
}
