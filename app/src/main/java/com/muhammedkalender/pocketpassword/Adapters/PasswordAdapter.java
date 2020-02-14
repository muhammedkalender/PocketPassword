package com.muhammedkalender.pocketpassword.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Holders.PasswordListHolder;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.R;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordListHolder> {
    private List<PasswordModel> listPassword;
    private Context context;

    public PasswordAdapter(List<PasswordModel> listPassword, Context context) {
        this.listPassword = listPassword;
        this.context = context;
    }

    @NonNull
    @Override
    public PasswordListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(this.context).inflate(R.layout.item_list_password, parent, false);


        return new PasswordListHolder(this.context, viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordListHolder holder, int position) {
        holder.tvName.setText(listPassword.get(position).getName());

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.CURRENT_PASSWORD_MODEL = listPassword.get(position);

                Global.TAB_LAYOUT.getTabAt(2).select();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPassword.size();
    }
}
