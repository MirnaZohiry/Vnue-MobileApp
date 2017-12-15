package com.example.a100535658.project;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LogoPage extends AppCompatActivity {
    //Wait time to launch the application
    private static int TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_page);

        //Start MainActivity class after timeout
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            Intent i = new Intent(LogoPage.this, MainActivity.class);
            startActivity(i);
            finish();
            }
        }, TIME_OUT);
    }
}