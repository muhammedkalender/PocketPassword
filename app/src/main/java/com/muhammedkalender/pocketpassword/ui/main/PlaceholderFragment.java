package com.muhammedkalender.pocketpassword.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.muhammedkalender.pocketpassword.Global;
import com.muhammedkalender.pocketpassword.MainActivity;
import com.muhammedkalender.pocketpassword.Pages.HomePage;
import com.muhammedkalender.pocketpassword.Pages.PasswordPage;
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


        if(pageViewModel.getIndex() == 2){
            //Home Page
            root = inflater.inflate(R.layout.fragment_main, container, false);

            HomePage homePage = Global.getPageHome(root);

            return homePage.getView();
        }else if(pageViewModel.getIndex() == 3){
            //Password Page
            root = inflater.inflate(R.layout.fragment_send, container, false);

            PasswordPage passwordPage = new PasswordPage();
            passwordPage.initialize(root);

            return passwordPage.getView();
        } else{
        //    root = inflater.inflate(R.layout.activity_main1, container, false);
            root = inflater.inflate(R.layout.fragment_gallery, container, false);
        }


        return root;
//        final TextView textView = root.findViewById(R.id.section_label);

//
//        pageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(String.valueOf(a));
//            }
//        });

//a++;
//a+++
//
//            pageViewModel.getText().observe(this, new Observer<String>() {
//                @Override
//                public void onChanged(@Nullable String s) {
//                    textView.setText(String.valueOf(a++));
//                }
//            });

//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("asda", "klik");
//                    SectionsPagerAdapter.isa = 6;
//                    MainActivity.viewPager.setCurrentItem(0);
//                    //     MainActivity.tabs.getTabAt(1).select();
//                }
//            });
//

    //    return root;
    }
}
