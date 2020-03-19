package com.muhammedkalender.pocketpassword.Holders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muhammedkalender.pocketpassword.R;

public class PasswordListHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView tvName;
    public ImageView ivShow, ivForward, ivClipboard, ivAccount;
    public LinearLayout llContainer;

    public PasswordListHolder(Context context, View view) {
        super(view);

        this.view = view;
        this.tvName = view.findViewById(R.id.tvName);
        this.ivShow = view.findViewById(R.id.ivShow);
        this.ivForward = view.findViewById(R.id.ivForward);
        this.ivClipboard = view.findViewById(R.id.ivClipboard);
        this.ivAccount = view.findViewById(R.id.ivAccount);
        this.llContainer = view.findViewById(R.id.llContainer);
    }
}
