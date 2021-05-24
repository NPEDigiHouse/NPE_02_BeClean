package com.example.npe_02_beclean.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.example.npe_02_beclean.Activities.LoginActivity;
import com.example.npe_02_beclean.R;

public class SplashScreen extends Activity {
    //Oncreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Atur Layout
        setContentView(R.layout.splash_screen);
        // Handler untuk pindah activty setelah delay
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }},2000);
    }
}
