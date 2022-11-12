package com.techalgo.spendplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class DashBoard extends AppCompatActivity{
    BottomNavigationView btm_nav;
    public String trxn="";

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView nav;

    //Bottom Navigation Fragments
    Dash dashboardFragment = new Dash();
    Income incomeFragment = new Income();
    Expense expenseFragment = new Expense();
    HistoryFragment historyFragment = new HistoryFragment();

    //Navigation Drawer Fragments
    AboutFragment about = new AboutFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        trxn = getIntent().getExtras().getString("trxn");

        btm_nav = findViewById(R.id.bottom_nav);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},1000);
        }

        btm_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,dashboardFragment).commit();
                        btm_nav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_SELECTED);
                        return true;

                    case R.id.income:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,incomeFragment).commit();
                        btm_nav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_SELECTED);
                        return true;

                    case R.id.expense:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,expenseFragment).commit();
                        btm_nav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_SELECTED);
                        return true;

                    case R.id.history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,historyFragment).commit();
                        btm_nav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_SELECTED);
                        return true;
                }
                return false;
            }
        });
        btm_nav.setSelectedItemId(R.id.dashboard);

        Bundle bundle = new Bundle();
        bundle.putString("trxn",trxn);
        dashboardFragment.setArguments(bundle);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        nav =findViewById(R.id.nav_View);

//        nav.setCheckedItem(R.id.dashboard);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                try {
                    switch (item.getItemId()) {
                        case R.id.home:
                            btm_nav.setVisibility(View.VISIBLE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, dashboardFragment).commit();
                            break;
                        case R.id.about_us:
                            btm_nav.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, about).commit();
                            break;
                            
                        case R.id.rate_us:
                            try
                            {
                                Intent rateIntent = rateIntentForUrl("market://details");
                                startActivity(rateIntent);
                            }
                            catch (ActivityNotFoundException e)
                            {
                                Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
                                startActivity(rateIntent);
                            }
                            btm_nav.setVisibility(View.VISIBLE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, dashboardFragment).commit();
                            nav.setCheckedItem(R.id.dashboard);
                            break;
                    }
                }catch (Exception e){
                    Log.d("exception",e.toString());
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();




    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}