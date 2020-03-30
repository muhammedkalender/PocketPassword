package com.kalendersoftware.pocketpassword.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kalendersoftware.pocketpassword.Constants.ColorConstants;
import com.kalendersoftware.pocketpassword.Constants.ErrorCodeConstants;
import com.kalendersoftware.pocketpassword.Constants.InfoCodeConstants;
import com.kalendersoftware.pocketpassword.Global;
import com.kalendersoftware.pocketpassword.Globals.Config;
import com.kalendersoftware.pocketpassword.Globals.Helpers;
import com.kalendersoftware.pocketpassword.Helpers.ClipboardHelper;
import com.kalendersoftware.pocketpassword.Helpers.CryptHelper;
import com.kalendersoftware.pocketpassword.Holders.PasswordListHolder;
import com.kalendersoftware.pocketpassword.Models.PasswordModel;
import com.kalendersoftware.pocketpassword.R;

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
        if (defCryptHelper == null) {
            defCryptHelper = CryptHelper.buildDefault();
        }

        int tintColor = ColorConstants.colorItem[position % ColorConstants.colorItem.length].getTint();

        final PasswordModel passwordModel = Global.LIST_PASSWORDS_SOLID.get(Helpers.list.findIndexFromTempIndex(position));

        passwordModel.decrypt();

        holder.tvName.setText(passwordModel.getName());

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.LOCK_PASSWORD_PAGE = false;

                Helpers.logger.info(InfoCodeConstants.PASSWORD_HOLDER_CLICK, String.format("Index : %1$d", Helpers.list.findTempIndexFromId(passwordModel.getId())));

                Helpers.loading.show();

                Global.CURRENT_PASSWORD_MODEL_INDEX = Helpers.list.findTempIndexFromId(passwordModel.getId());
                Global.SELECTED_PASSWORD_ID = passwordModel.getId();

                Helpers.logger.info(String.format("Şuan %1$d adet sayfa var", Global.SECTION_PAGER_ADAPTER.getCount()));

                if (Global.SECTION_PAGER_ADAPTER.getCount() < Config.TAB_HOME_INDEX + 2) {
                    Global.SECTION_PAGER_ADAPTER.add(Helpers.list.findBySelectedId().getName());
                    Global.SECTION_PAGER_ADAPTER.notifyDataSetChanged();
                }

                Helpers.loading.hide();

                Helpers.system.hideSoftKeyboard();

                Global.TAB_LAYOUT.getTabAt(Config.TAB_PASSWORD_INDEX).select();
                Global.TAB_LAYOUT.getTabAt(Config.TAB_PASSWORD_INDEX).setText(passwordModel.getName());
                Global.TAB_LAYOUT.getTabAt(Config.TAB_PASSWORD_INDEX).setContentDescription(passwordModel.getName());

                Global.PAGE_PASSWORD.load(passwordModel);

                holder.ivClipboard.callOnClick();
            }
        });

        holder.llContainer.setBackgroundColor(passwordModel.getColor());

        holder.tvName.setTextColor(passwordModel.getTintColor());

        holder.ivShow.setColorFilter(passwordModel.getTintColor());
        holder.ivClipboard.setColorFilter(passwordModel.getTintColor());
        holder.ivForward.setColorFilter(passwordModel.getTintColor());
        holder.ivAccount.setColorFilter(passwordModel.getTintColor());

        holder.ivClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Thread(() -> {
                        String password = passwordModel.getPassword();

                        if (!passwordModel.isDecrypted()) {
                            passwordModel.decrypt();
                        }

                        ClipboardHelper.build(
                                R.string.clipboard_title,
                                password
                        ).withMessage(
                                R.string.password_clipboard,
                                passwordModel.getShortName()
                        )
                                .show();
                    }).start();
                } catch (Exception e) {
                    Helpers.logger.error(ErrorCodeConstants.CLIPBOARD_PASSWORD_IN_LIST, e);
                }
            }
        });

        holder.ivForward.setOnClickListener(v -> holder.llContainer.callOnClick());

        holder.ivShow.setTag(false);

        holder.ivShow.setOnClickListener(v -> {
            if ((boolean) v.getTag()) {
                holder.tvName.setText(passwordModel.getName());

                ((ImageView) v).setImageResource(R.drawable.ic_remove_red_eye_24dp);
            } else {
                holder.tvName.setText(passwordModel.getDecryptedAccount());

                ((ImageView) v).setImageResource(R.drawable.ic_visibility_off_24dp);
            }

            v.setTag(!(boolean) v.getTag());
        });

        holder.ivAccount.setOnClickListener(v -> {
            try {
                new Thread(() -> {
                    String account = passwordModel.getDecryptedAccount();

                    ClipboardHelper
                            .build(
                                    Helpers.resource.getString(R.string.clipboard_title),
                                    account
                            )
                            .withMessage(
                                    R.string.account_clipboard,
                                    passwordModel.getShortName()
                            )
                            .show();
                }).start();
            } catch (Exception e) {
                Helpers.logger.error(ErrorCodeConstants.CLIPBOARD_ACCOUNT_COPY, e);
            }
        });

        holder.ivForward.setColorFilter(tintColor);
        holder.ivClipboard.setColorFilter(tintColor);
        holder.ivShow.setColorFilter(tintColor);
    }

    @Override
    public int getItemCount() {
        return Global.LIST_PASSWORDS.size();
    }
}
