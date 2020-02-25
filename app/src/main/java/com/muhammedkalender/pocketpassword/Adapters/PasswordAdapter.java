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

import com.google.android.material.tabs.TabLayout;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Constants.ErrorCodeConstants;
import com.muhammedkalender.pocketpassword.Constants.InfoCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.CryptHelper;
import com.muhammedkalender.pocketpassword.Holders.PasswordListHolder;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.R;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordListHolder> {
    private Context context;

    private static CryptHelper defCryptHelper;

    public PasswordAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PasswordListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(this.context).inflate(R.layout.item_list_password, parent, false);

        Helpers.logger.info(InfoCodeConstants.ADAPTER_CREATE_HOLDER, "Veri hazırlandı");

        return new PasswordListHolder(this.context, viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordListHolder holder, int position) {
        if(defCryptHelper == null){
            defCryptHelper = CryptHelper.buildDefault();
        }

        int tintColor = ColorConstants.colorItem[position % ColorConstants.colorItem.length].getTint();

        PasswordModel passwordModel = Global.LIST_PASSWORDS_SOLID.get(Helpers.list.findIndexFromTempIndex(position));

        if(!passwordModel.isDecrypted()){
            passwordModel.decrypt();
        }

        holder.tvName.setText(passwordModel.getName());

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.LOCK_PASSWORD_PAGE = false;

                Helpers.logger.info(InfoCodeConstants.PASSWORD_HOLDER_CLICK, String.format("Index : %1$d", position));

                Helpers.loading.show();

                Global.CURRENT_PASSWORD_MODEL_INDEX = position;

                Helpers.logger.info(String.format("Şuan %1$d adet sayfa var", Global.SECTION_PAGER_ADAPTER.getCount()));

                if(Global.SECTION_PAGER_ADAPTER.getCount() < Config.TAB_HOME_INDEX + 2) {
                    Global.SECTION_PAGER_ADAPTER.add(Global.LIST_PASSWORDS.get(Global.CURRENT_PASSWORD_MODEL_INDEX).getName());
                    Global.SECTION_PAGER_ADAPTER.notifyDataSetChanged();
                }

                Helpers.loading.hide();

                Global.TAB_LAYOUT.getTabAt(Config.TAB_PASSWORD_INDEX).select();
                Global.TAB_LAYOUT.getTabAt(Config.TAB_PASSWORD_INDEX).setText(passwordModel.getName());
                Global.TAB_LAYOUT.getTabAt(Config.TAB_PASSWORD_INDEX).setContentDescription(passwordModel.getName());

                Global.PAGE_PASSWORD.load(passwordModel);

                holder.ivClipboard.callOnClick();
            }
        });

        holder.llContainer.setBackgroundColor(passwordModel.getColor());

        holder.ivClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String password = passwordModel.getPassword();

                    if(!passwordModel.isDecrypted()){
                        passwordModel.decrypt();
                    }

                    ClipboardManager clipboard = (ClipboardManager) Global.CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(Helpers.resource.getString(R.string.clipboard_title), password);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(Global.CONTEXT,R.string.password_clipboard, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Helpers.logger.error(ErrorCodeConstants.CLIPBOARD_PASSWORD_IN_LIST, e);
                }
            }
        });

        holder.ivForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.llContainer.callOnClick();
            }
        });

        holder.ivForward.setColorFilter(tintColor);
        holder.ivClipboard.setColorFilter(tintColor);
        holder.ivShow.setColorFilter(tintColor);

        if(Config.CONFIG_HIDE_VIEW){
            holder.ivShow.setClickable(false);
        }else{
            holder.ivShow.setClickable(true);
        }
    }

    @Override
    public int getItemCount() {
        return Global.LIST_PASSWORDS.size();
    }
}
