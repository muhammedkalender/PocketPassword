package com.muhammedkalender.pocketpassword;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.muhammedkalender.pocketpassword.Abstracts.ModelAbstract;
import com.muhammedkalender.pocketpassword.Adapters.PasswordAdapter;
import com.muhammedkalender.pocketpassword.Components.LoadingComponent;
import com.muhammedkalender.pocketpassword.Constants.ColorConstants;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.DatabaseHelper;
import com.muhammedkalender.pocketpassword.Helpers.ListHelper;
import com.muhammedkalender.pocketpassword.Helpers.LogHelpers;
import com.muhammedkalender.pocketpassword.Helpers.ResourceHelper;
import com.muhammedkalender.pocketpassword.Helpers.SharedPreferencesHelper;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
import com.muhammedkalender.pocketpassword.Objects.ColorObject;
import com.muhammedkalender.pocketpassword.Objects.ColumnObject;
import com.muhammedkalender.pocketpassword.ui.main.SectionsPagerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    TextInputLayout tilMainPassword;
    TextInputEditText etMainPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Global.CONTEXT = this;

        Helpers.logger = new LogHelpers();
        Helpers.resource = new ResourceHelper();
        Helpers.database = new DatabaseHelper(this);
        Helpers.loading = new LoadingComponent(this);
        Helpers.list = new ListHelper();
        Helpers.config = new SharedPreferencesHelper(this);

        Config.initConfig();

        tilMainPassword = findViewById(R.id.tilMainPassword);
        etMainPassword = findViewById(R.id.etMainPassword);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_controller_view_tag);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

    @Override
    public void onBackPressed() {
        if (Global.TAB_LAYOUT.getSelectedTabPosition() == Config.TAB_HOME_INDEX) {
            super.onBackPressed();
        } else {
            Global.TAB_LAYOUT.getTabAt(Config.TAB_HOME_INDEX).select();
        }
    }

    public void login(){


        if(!etMainPassword.getText().toString().equals("1111")){
            etMainPassword.setError("Olmadı");

            return;
        }



        findViewById(R.id.appBar).setVisibility(View.VISIBLE);

        Helpers.loading.show();

        ColorConstants.colorItem = new ColorObject[]{
                new ColorObject(Helpers.resource.getColor(R.color.pink), Helpers.resource.getColor(R.color.tintPink)),
                new ColorObject(Helpers.resource.getColor(R.color.lightBlue), Helpers.resource.getColor(R.color.tintLightBlue)),
                new ColorObject(Helpers.resource.getColor(R.color.amber), Helpers.resource.getColor(R.color.tintAmber)),
                new ColorObject(Helpers.resource.getColor(R.color.red), Helpers.resource.getColor(R.color.tintRed)),
                new ColorObject(Helpers.resource.getColor(R.color.purple), Helpers.resource.getColor(R.color.tintPurple)),
                new ColorObject(Helpers.resource.getColor(R.color.deepOrange), Helpers.resource.getColor(R.color.tintDeepOrange))
        };

        Global.SECTION_PAGER_ADAPTER = new SectionsPagerAdapter(this, getSupportFragmentManager());
        Global.VIEW_PAGER = findViewById(R.id.view_pager);
        Global.VIEW_PAGER.setAdapter(Global.SECTION_PAGER_ADAPTER);
        Global.TAB_LAYOUT = findViewById(R.id.tabs);
        Global.TAB_LAYOUT.setupWithViewPager(Global.VIEW_PAGER);

        Global.TAB_LAYOUT.getTabAt(Config.TAB_HOME_INDEX).select();

        Helpers.loading.hide();
    }
}
