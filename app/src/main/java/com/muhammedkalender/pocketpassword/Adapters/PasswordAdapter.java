package com.muhammedkalender.pocketpassword.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Holders.PasswordListHolder;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.R;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordListHolder> {
    private Context context;

    public PasswordAdapter(Context context) {
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
        holder.tvName.setText(Global.LIST_PASSWORDS.get(position).getName());

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpers.loading.show();

                Global.CURRENT_PASSWORD_MODEL_INDEX = position;

                if(Global.SECTION_PAGER_ADAPTER.getCount() < 3){
                    Global.SECTION_PAGER_ADAPTER.add(Global.LIST_PASSWORDS.get(Global.CURRENT_PASSWORD_MODEL_INDEX).getName());
                    Global.SECTION_PAGER_ADAPTER.notifyDataSetChanged();
                }

                Helpers.loading.hide();

                Global.TAB_LAYOUT.getTabAt(2).select();
            }
        });

        holder.llContainer.setBackgroundColor(ColorConstants.colorItem[position % ColorConstants.colorItem.length]);

        holder.ivClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ClipboardManager clipboard = (ClipboardManager) Global.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(Helpers.resource.getString(R.string.clipboard_title), Global.LIST_PASSWORDS.get(Global.CURRENT_PASSWORD_MODEL_INDEX).getPassword());
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(Global.CONTEXT,R.string.password_clipboard, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    //todo
                    Helpers.logger.error(ErrorCodeConstants.CLIPBOARD_PASSWORD_IN_LIST, e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Global.LIST_PASSWORDS.size();
    }
}
