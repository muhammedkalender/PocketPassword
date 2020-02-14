package com.muhammedkalender.pocketpassword;

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
import com.muhammedkalender.pocketpassword.Abstracts.ModelAbstract;
import com.muhammedkalender.pocketpassword.Adapters.PasswordAdapter;
import com.muhammedkalender.pocketpassword.Globals.Config;
import com.muhammedkalender.pocketpassword.Globals.Helpers;
import com.muhammedkalender.pocketpassword.Helpers.DatabaseHelper;
import com.muhammedkalender.pocketpassword.Helpers.LogHelpers;
import com.muhammedkalender.pocketpassword.Helpers.ResourceHelper;
import com.muhammedkalender.pocketpassword.Models.PasswordModel;
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

    private RecyclerView rvPasswordList;
    public static PasswordAdapter adapterPassword;
    private RecyclerView.LayoutManager rvlmPasswordList;

    public static List<PasswordModel> dsPasswords;

    public static TabLayout tabs;
    public static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Helpers.logger = new LogHelpers();
        Helpers.resource = new ResourceHelper();
        Helpers.database = new DatabaseHelper(this);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Global.CONTEXT = this;
        Global.TAB_LAYOUT = tabs;
        Global.VIEW_PAGER = viewPager;
        //tabs.getTabAt(1).select();
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
//                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_controller_view_tag);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//
//        dsPasswords = new ArrayList<>();
//        dsPasswords.add(new PasswordModel("MB", "ASDSAD", "#FFFFFF"));
//        dsPasswords.add(new PasswordModel("ZZ", "HASH ZZ", "#CCCCCC"));
//
//        rvPasswordList = findViewById(R.id.rvPasswordList);
//        rvPasswordList.setHasFixedSize(true);
//
//        rvlmPasswordList = new LinearLayoutManager(this);
//        rvPasswordList.setLayoutManager(rvlmPasswordList);
//
//        adapterPassword = new PasswordAdapter(dsPasswords, this);
//        rvPasswordList.setAdapter(adapterPassword);
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setTitle("TİT");
//        alertDialog.setMessage("Messa");
//
//        final EditText input = new EditText(this);
//// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//
//        alertDialog.setView(input);
//
//        alertDialog.setNeutralButton("asda", null);
//        alertDialog.create().show();

  //      CryptHelper cryptHelper = new CryptHelper();

//      String encrypedData =   cryptHelper.encrypt("Test", "TestTestTestTestTest");

     //   Log.e("asda", encrypedData);
//
//        String rawData = "test";
//
//
//
//        try{
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e("KEYS", "START GENERATION");
//
//                    try{
//                        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//                        keyPairGenerator.initialize(4096);
//                        KeyPair keys = keyPairGenerator.generateKeyPair();
//
//                        keyPrivate = keys.getPrivate();
//                        keyPublic = keys.getPublic();
//                    }catch (Exception e){
//
//                    }
//
//
//                    Log.e("KEYS", "GENERATED");
//                }
//            }).start();
//        }catch (Exception e){
//
//        }
//
//
//        DatabaseHelper databaseHelper = new DatabaseHelper(this);
//        ResultObject resultObject = databaseHelper.cursor("SELECT * FROM 'test'");

        //Log.e("ISAVAB", databaseHelper.isAvailable("SELECT * FROM 'test' LIMIT 1", "bakem");

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
        if(Global.TAB_LAYOUT.getSelectedTabPosition() == Config.TAB_HOME_INDEX){
            super.onBackPressed();
        }else{
            Global.TAB_LAYOUT.getTabAt(Config.TAB_HOME_INDEX).select();
        }
    }
}
