package com.muhammedkalender.pocketpassword.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.muhammedkalender.pocketpassword.Constants.InfoCodeConstants;
import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Pages.HomePage;
import com.muhammedkalender.pocketpassword.Pages.Password.NewPasswordPage;
import com.muhammedkalender.pocketpassword.Pages.Password.PasswordPage;
import com.muhammedkalender.pocketpassword.R;

public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root;

        if (pageViewModel.getIndex() == 1) {
            root = inflater.inflate(R.layout.fragment_new_password, container, false);

            NewPasswordPage newPasswordPage = new NewPasswordPage();
            newPasswordPage.initialize(root);

            return newPasswordPage.getView();
        } else if (pageViewModel.getIndex() == 2) {
            //Home Page
            root = inflater.inflate(R.layout.fragment_main, container, false);

            HomePage homePage = Global.getPageHome(root);

            return homePage.getView();
        } else if (pageViewModel.getIndex() == 3) {
            //Password Page
            root = inflater.inflate(R.layout.fragment_password, container, false);

            PasswordPage passwordPage = Global.getPagePassword(root);

            Helpers.logger.info(InfoCodeConstants.PAGE_CHANGE, "Password");

            return passwordPage.getView();
        } else {
            //TODO
            return null;
        }
    }
}
