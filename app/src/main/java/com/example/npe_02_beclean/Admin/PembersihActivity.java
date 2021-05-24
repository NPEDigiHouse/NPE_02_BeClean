package com.example.npe_02_beclean.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.npe_02_beclean.R;

public class PembersihActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembersih);


    }

    public void got(View view) {
        Intent i = new Intent(this, AddPembersihActivity.class);
        startActivity(i);
    }
}