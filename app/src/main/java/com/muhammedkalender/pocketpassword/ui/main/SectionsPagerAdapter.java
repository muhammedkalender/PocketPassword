package com.muhammedkalender.pocketpassword.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.R;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final ArrayList<String> TAB_TITLES = new ArrayList();
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        //Onbackle geri gelirse sekmeler sapıtabiliyor
        TAB_TITLES.clear();

        TAB_TITLES.add(Helpers.resource.getString(R.string.tab_settings));
        TAB_TITLES.add(Helpers.resource.getString(R.string.tab_add_password));
        TAB_TITLES.add(Helpers.resource.getString(R.string.tab_list_password));
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //return mContext.getResources().getString(TAB_TITLES[position]);
        return TAB_TITLES.get(position);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.size();
    }

    public void add(String title) {
        TAB_TITLES.add(title);
    }

    public void delete(int index) {
        TAB_TITLES.remove(index);
    }

    //https://inneka.com/programming/android/add-delete-pages-to-viewpager-dynamically/
    @Override
    public int getItemPosition(Object object) {
        if (TAB_TITLES.contains(object)) {
            return TAB_TITLES.indexOf(object);
        } else {
            return POSITION_NONE;
        }
    }

}
