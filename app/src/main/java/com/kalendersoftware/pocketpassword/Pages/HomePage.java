package com.kalendersoftware.pocketpassword.Pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kalendersoftware.pocketpassword.Abstracts.PageAbstract;
import com.kalendersoftware.pocketpassword.Adapters.PasswordAdapter;
import com.kalendersoftware.pocketpassword.Constants.ErrorCodeConstants;
import com.kalendersoftware.pocketpassword.Global;
import com.kalendersoftware.pocketpassword.Globals.Helpers;
import com.kalendersoftware.pocketpassword.Interfaces.PageInterface;
import com.kalendersoftware.pocketpassword.Models.CategoryModel;
import com.kalendersoftware.pocketpassword.Models.PasswordModel;
import com.kalendersoftware.pocketpassword.R;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends PageAbstract implements PageInterface {
    //region Variables

    private RecyclerView rvPasswordList;
    private RecyclerView.LayoutManager rvlmPasswordList;
    private TextInputLayout tilSearch;
    private TextInputEditText etSearch;

    private LinearLayout llCategory;
    private TextView tvCategory;
    private ImageView ivCategory;

    //endregion

    //region Initialize

    @Override
    public void initialize(View viewRoot) {
        if (isInitialized()) {
            return;
        }

        try {
            this.viewRoot = viewRoot;

            Global.LIST_PASSWORDS = new ArrayList<>();
            Global.LIST_PASSWORDS_SOLID = new ArrayList<>();

            rvPasswordList = viewRoot.findViewById(R.id.rvPasswordList);
            rvPasswordList.setHasFixedSize(true);

            rvlmPasswordList = new LinearLayoutManager(Global.CONTEXT);
            rvPasswordList.setLayoutManager(rvlmPasswordList);

            tilSearch = viewRoot.findViewById(R.id.tilSearch);

            etSearch = viewRoot.findViewById(R.id.etSearch);

            llCategory = viewRoot.findViewById(R.id.llCategory);
            tvCategory = viewRoot.findViewById(R.id.tvCategory);
            ivCategory = viewRoot.findViewById(R.id.ivCategory);

            CategoryModel categoryModel = new CategoryModel();
            List<CategoryModel> listCategory =  categoryModel.selectActive();

            Global.ADAPTER_CATEGORY = new ArrayAdapter<>(Global.CONTEXT, android.R.layout.select_dialog_singlechoice);

            Global.LIST_CATEGORY = listCategory;

            for(CategoryModel category : Global.LIST_CATEGORY){
                Global.ADAPTER_CATEGORY.add(category.getName());
            }

            tvCategory.setText(listCategory.get(0).getName());

            View.OnClickListener clickListener = v -> {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(Global.CONTEXT);
                    builderSingle.setIcon(R.drawable.ic_format_list_bulleted_24dp);
                    builderSingle.setTitle(R.string.select_category);

                    builderSingle.setSingleChoiceItems(Global.ADAPTER_CATEGORY, Global.SELECTED_CATEGORY_INDEX, (dialog, which) -> {
                        dialog.dismiss();

                        Global.SELECTED_CATEGORY = Global.LIST_CATEGORY.get(which).getId();
                        Global.SELECTED_CATEGORY_INDEX = which;

                        tvCategory.setText(Global.LIST_CATEGORY.get(which).getName());

                        filter("");
                    });

                    builderSingle.setNegativeButton(R.string.select_category_cancel, (dialog, which) -> dialog.dismiss());


                    builderSingle.show();
            };

            llCategory.setOnClickListener(clickListener);
            tvCategory.setOnClickListener(clickListener);
            ivCategory.setOnClickListener(clickListener);

            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    filter(s.toString());
                }
            });

            Global.PASSWORD_ADAPTER = new PasswordAdapter(Global.CONTEXT);
            rvPasswordList.setAdapter(Global.PASSWORD_ADAPTER);

            this.initialized = true;

            PasswordModel passwordModel = new PasswordModel();
            List<PasswordModel> list = passwordModel.selectActive();


            Global.LIST_PASSWORDS.addAll(list);
            Global.LIST_PASSWORDS_SOLID.addAll(list);

            Helpers.logger.info(String.format("Listeler hazırlandı, Listede %1$d geçicide %2$d adet, kalıcıda %3$d adet kayıt var",list.size(),  Global.LIST_PASSWORDS.size(), Global.LIST_PASSWORDS_SOLID.size()));
        } catch (Exception e) {
            this.initialized = false;

            Helpers.logger.error(ErrorCodeConstants.HOME_PAGE_INITIALIZE, e);
        }
    }

    @Override
    public void refresh() {
        //todo
    }

    //endregion

    //region Secondary Methods

    public void filter(final String keyword){
        new Thread(() -> {
            Helpers.loading.showDelayedWithKeyboard();

            String _keyword = keyword;

            if(_keyword.trim().length() == 0){
                _keyword = "";
            }

            List<PasswordModel> tempPasswords = new ArrayList<>();

            for(int i = 0; i < Global.LIST_PASSWORDS_SOLID.size(); i++){
                if(_keyword.equals("") || Global.LIST_PASSWORDS_SOLID.get(i).getName().toLowerCase().contains(_keyword.toLowerCase())){
                    if(Global.SELECTED_CATEGORY != 0){
                        if(Global.SELECTED_CATEGORY != Global.LIST_PASSWORDS_SOLID.get(i).getCategoryID()){
                            continue;
                        }
                    }

                    tempPasswords.add(Global.LIST_PASSWORDS_SOLID.get(i));
                }
            }

            Global.LIST_PASSWORDS.clear();
            Global.LIST_PASSWORDS.addAll(tempPasswords);

            ((Activity)Global.CONTEXT).runOnUiThread(() -> {
                Global.PASSWORD_ADAPTER.notifyDataSetChanged();

                Helpers.loading.hide();
            });
        }).start();
    }

    //endregion

    //region Getters & Setters

    //region Getters

    @Override
    public View getView() {
        return this.viewRoot;
    }

    //endregion

    //endregion
}
