package com.example.npe_02_beclean.Admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.npe_02_beclean.Activities.LoginActivity;
import com.example.npe_02_beclean.R;

public class MainAdminActivity extends Activity implements View.OnClickListener {
    private Button btnPembersih, btnDiskon, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        btnPembersih = findViewById(R.id.btn_pembersih);
        btnDiskon = findViewById(R.id.btn_diskon);
        btnBack = findViewById(R.id.btn_back_main_admin);

        btnPembersih.setOnClickListener(this);
        btnDiskon.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pembersih:
                Intent goToPembersih = new Intent(this, PembersihActivity.class);
                startActivity(goToPembersih);
                break;
            case R.id.btn_diskon:
                Intent goToDiskon = new Intent(this, DiscountActivity.class);
                startActivity(goToDiskon);
                break;
            case R.id.btn_back_main_admin:
                Intent goToLogin = new Intent(this, LoginActivity.class);
                startActivity(goToLogin);
                finish();
                break;
        }
    }
}