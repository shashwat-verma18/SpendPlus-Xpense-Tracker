package com.techalgo.spendplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=1000;
    Animation zoom;
    ImageView logo;
    String trxn="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            trxn = getIntent().getExtras().getString("trxn");
        }catch (Exception e){}

        logo=(ImageView)findViewById(R.id.logo);

        zoom= AnimationUtils.loadAnimation(this,R.anim.zoom);

        logo.setAnimation(zoom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),DashBoard.class);
                i.putExtra("trxn",trxn);
                startActivity(i);
                finish();
            }
        },SPLASH_SCREEN);

    }
}