package com.example.npe_02_beclean.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.example.npe_02_beclean.Activities.LoginActivity;
import com.example.npe_02_beclean.Activities.MainActivity;
import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.R;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // set timer for 2 seconds
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // check if user is already login
                String userId = Util.getUserIdLocal(SplashScreen.this);
                Intent intent;
                if (userId.isEmpty()) {
                    intent = new Intent(SplashScreen.this, LoginActivity.class);
                } else {
                    intent = new Intent(SplashScreen.this, MainActivity.class);
                }

                // start activity
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }},2000);
    }
}
