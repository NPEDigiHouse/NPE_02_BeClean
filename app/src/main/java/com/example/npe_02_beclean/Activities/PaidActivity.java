package com.example.npe_02_beclean.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.R;

public class PaidActivity extends AppCompatActivity {

    // extras
    public static final String EXTRA_TOTAL_COST = "extra_total_cost";

    // widgets
    private TextView tvTotalCost, tvPoin;
    private Button btnBeranda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid);

        tvTotalCost = findViewById(R.id.tv_tarif_total_value_paid);
        tvPoin = findViewById(R.id.tv_poin_value_paid);
        btnBeranda = findViewById(R.id.btn_beranda_paid);

        int totalCost = getIntent().getIntExtra(EXTRA_TOTAL_COST, 0);
        int poin = totalCost / 1000;

        tvTotalCost.setText(Util.convertToRupiah(String.valueOf(totalCost)));
        tvPoin.setText(String.format("+%d", poin));

        btnBeranda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(PaidActivity.this, MainActivity.class);
                startActivity(goToMain);
                finishAffinity();
            }
        });

    }
}