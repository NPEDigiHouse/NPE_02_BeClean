package com.example.npe_02_beclean.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.npe_02_beclean.R;

public class LoginActivity extends Activity implements View.OnClickListener {
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_masuk);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_masuk:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
        }
    }
}