package com.dott.webapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashscreenActivity extends Activity {
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        String indicator=getIntent().getStringExtra("indicator");
        int secondsDelayed = 4;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(sharedpreferences.getBoolean("logged",false) == true)
                {
                    //redirect to home page
                    startActivity(new Intent(SplashscreenActivity.this, HomeActivity.class));
                    finish();


                }
                else
                {
                    startActivity(new Intent(SplashscreenActivity.this, MainActivity.class));
                    finish();


                }

                if(sharedpreferences.getBoolean("first_time",true) == true)
                {
                    startActivity(new Intent(SplashscreenActivity.this, RegisterActivity.class));
                    finish();

                }
                else
                {


                }
            }
        }, secondsDelayed * 1000);











    }
}
