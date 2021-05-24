package com.example.npe_02_beclean.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.npe_02_beclean.R;

public class LoginActivity extends Activity implements View.OnClickListener {

    // widgets
    private Button btnLogin;
    private TextView btnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize widgets
        btnLogin = findViewById(R.id.btn_masuk_login);
        btnDaftar = findViewById(R.id.tv_daftar_login);

        // if button clicked
        btnLogin.setOnClickListener(this);
        btnDaftar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_masuk_login:
                Intent goToMain = new Intent(this, MainActivity.class);
                startActivity(goToMain);
                break;
            case R.id.tv_daftar_login:
                Intent goToRegister = new Intent(this, RegisterActivity.class);
                startActivity(goToRegister);
                break;
        }
    }
}