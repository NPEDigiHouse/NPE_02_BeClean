package com.example.npe_02_beclean.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.npe_02_beclean.R;

public class PembayaranAdaActivity extends AppCompatActivity {

    private TextView tvCategory, tvTeamName, tvMembers,
            tvAddress, tvDistance, tvDuration,
            tvCostService, tvCostTransport, tvCostTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_ada);

        // initialize widgets
        initializeWidgets();

        // set data to widgets

    }

    private void initializeWidgets() {
        tvCategory = findViewById(R.id.tv_categori_pembersihan_pembayaran_ada);
        tvTeamName = findViewById(R.id.tv_team_name_pembayaran_ada);
        tvMembers = findViewById(R.id.tv_members_pembayaran_ada);
        tvAddress = findViewById(R.id.tv_alamat_pembayaran_ada);
        tvDistance = findViewById(R.id.tv_distance_pembayaran_ada);
        tvDuration = findViewById(R.id.tv_duration_pembayaran_ada);
        tvCostService = findViewById(R.id.tv_harga_layanan_pembayaran_ada);
        tvCostTransport = findViewById(R.id.tv_biaya_transportasi_pembarayan_ada);
        tvCostTotal = findViewById(R.id.tv_total_cost_pembayaran_ada);
    }
}