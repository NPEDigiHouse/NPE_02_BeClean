package com.example.npe_02_beclean.Admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.npe_02_beclean.R;

public class MainAdminActivity extends Activity implements View.OnClickListener {
    Button btnPembersih;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        btnPembersih = findViewById(R.id.btn_pembersih);
        btnPembersih.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pembersih:
                Intent i = new Intent(this, PembersihActivity.class);
                startActivity(i);
                break;
        }
    }
}