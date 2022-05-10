package com.amw.mws.myapplication.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.amw.mws.myapplication.R;
import com.amw.mws.myapplication.fragment.DriverFragment;
import com.amw.mws.myapplication.fragment.SearchFragment;
import com.amw.mws.myapplication.fragment.VehiclesFragment;
import com.amw.mws.myapplication.fragment.ViolationsLogFragment;
import com.amw.mws.myapplication.fragment.ViolationsTypesFragment;
import com.amw.mws.myapplication.member.VehicleLog;
import com.amw.mws.myapplication.utils.LoginManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_DRIVER = "driver";
    private String[]activityTitles;

    NavigationView navigationView;
    DrawerLayout drawer;

    public static int navItemIndex=0;

    LoginManager loginManager;

    String plugedNumber;

    private static final String TAG_VIOLATIONS_LOG="violationslog";
    private static final String TAG_VIOLATIONS_TYPES="violationstypes";
    private static final String TAG_VEHICLES_LOG="vehicleslog";
    private static final String TAG_SEARCH="search";
    public static String CURRENT_TAG=TAG_VIOLATIONS_LOG;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginManager=LoginManager.getInstance(getApplicationContext());

        loginManager.ifUserLoggedOut();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (navItemIndex){
                    case 0:
                        Intent updateVLogIntent=new Intent(MainActivity.this,AddViolationLogActivity.class);
                        //updateVLogIntent.putExtra()
                        startActivity(updateVLogIntent);
                        break;
                    case 1:
                        Intent updateVType=new Intent(MainActivity.this,AddViolationTypeActivity.class);
                        //updateVType.putExtra();
                        startActivity(updateVType);
                        break;
                }
            }
        });

        mHandler=new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        activityTitles=getResources().getStringArray(R.array.nav_item_activity_title);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpNavigationView();

        if(savedInstanceState==null){
            if(loginManager.isAdmin()) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_VIOLATIONS_LOG;
                loadHomeFragment();
            }else{
                navItemIndex=4;
                CURRENT_TAG=TAG_DRIVER;
                loadHomeFragment();
            }
        }



        Intent intent=getIntent();
        if(! loginManager.isAdmin()){
            plugedNumber =intent.getStringExtra(VehicleLog.PLUGED_NUMBER);
            navigationView.getMenu().setGroupVisible(R.id.menu_admin,false);
            navigationView.getMenu().setGroupVisible(R.id.menu_driver,true);
        }else{
            navigationView.getMenu().setGroupVisible(R.id.menu_admin,true);
            navigationView.getMenu().setGroupVisible(R.id.menu_driver,false);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        loginManager.ifUserLoggedOut();
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_violations_log:
                        navItemIndex=0;
                        CURRENT_TAG=TAG_VIOLATIONS_LOG;
                        break;
                    case R.id.nav_violations_types:
                        navItemIndex=1;
                        CURRENT_TAG=TAG_VIOLATIONS_TYPES;
                        break;
                    case R.id.nav_vehicles_log:
                        navItemIndex=2;
                        CURRENT_TAG=TAG_VEHICLES_LOG;
                        break;
                    case  R.id.nav_search:
                        navItemIndex=3;
                        CURRENT_TAG=TAG_SEARCH;
                        break;
                    case R.id.nav_driver_violations:
                        navItemIndex=4;
                        CURRENT_TAG=TAG_DRIVER;
                        break;
                    case R.id.menu_item_logout:
                        loginManager.clearAndLogout();
                }

                if(item.isChecked()){
                    item.setChecked(false);
                }else{
                    item.setChecked(true);
                }

                item.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });
    }



    private void loadHomeFragment() {

        setToolbarTitle();

        Runnable mPendingRunnable=new Runnable() {
            @Override
            public void run() {
                Fragment fragment=getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame,fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if(mPendingRunnable!=null){
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void selectNavMenu(){
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    public Fragment getHomeFragment() {
        switch (navItemIndex){
            case 0:
                ViolationsLogFragment fragment=new ViolationsLogFragment();
                return fragment;
            case 1:
                ViolationsTypesFragment fragment1=new ViolationsTypesFragment();
                return fragment1;
            case 2:
                VehiclesFragment fragment2 =new VehiclesFragment();
                return fragment2;
            case 3:
                SearchFragment searchFragment=new SearchFragment();
                return searchFragment;
            case 4:
                DriverFragment driverFragment=DriverFragment.newInstance(plugedNumber);
                return  driverFragment;
                default:
                    ViolationsLogFragment fragment4=new ViolationsLogFragment();
                    return fragment4;
        }
    }
}
