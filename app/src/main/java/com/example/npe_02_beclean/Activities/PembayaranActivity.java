package com.example.npe_02_beclean.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.Models.Pembayaran;
import com.example.npe_02_beclean.R;
import com.mapbox.geojson.Point;

// WAW
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class PembayaranActivity extends AppCompatActivity {

    // extras
    public static final String EXTRA_PEMBAYARAN = "extra_pembayaran";

    // widgets
    private TextView tvCategory, tvTeamName, tvMembers,
            tvAddress, tvDistance, tvDuration,
            tvCostService, tvCostTransport, tvCostTotal;
    private Button btnContinuePay;
    private Pembayaran pembayaran;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        // initialize widgets
        initializeWidgets();

        // set data to widgets
        setDataToWidgets();

        // continue paying
        btnContinuePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set pembayaran to true
                Util.savePembayaranToLocal(PembayaranActivity.this, true);

                // go to paid
                Intent goToPaid = new Intent(PembayaranActivity.this, PaidActivity.class);
                goToPaid.putExtra(PaidActivity.EXTRA_TOTAL_COST, pembayaran.getCostTotal()); // int
                startActivity(goToPaid);
            }
        });

    }

    private void initializeWidgets() {
        tvCategory = findViewById(R.id.tv_categori_pembersihan_pembayaran);
        tvTeamName = findViewById(R.id.tv_team_name_pembayaran);
        tvMembers = findViewById(R.id.tv_members_pembayaran);
        tvAddress = findViewById(R.id.tv_alamat_pembayaran);
        tvDistance = findViewById(R.id.tv_distance_pembayaran);
        tvDuration = findViewById(R.id.tv_duration_pembayaran);
        tvCostService = findViewById(R.id.tv_harga_layanan_pembayaran);
        tvCostTransport = findViewById(R.id.tv_biaya_transportasi_pembarayan);
        tvCostTotal = findViewById(R.id.tv_total_cost_pembayaran);
        btnContinuePay = findViewById(R.id.btn_lanjutkan_pembayaran);
    }

    private void setDataToWidgets() {
        pembayaran = getIntent().getParcelableExtra(EXTRA_PEMBAYARAN);
        tvCategory.setText(pembayaran.getCategory());
        tvTeamName.setText(pembayaran.getTeamName());
        tvMembers.setText(pembayaran.getMembers());
        tvAddress.setText(pembayaran.getAddress());
        tvDistance.setText(String.format("%.1f km", pembayaran.getDistance()));
        tvDuration.setText(String.format("%.1f menit", pembayaran.getDuration()));
        tvCostService.setText(Util.convertToRupiah(String.valueOf(pembayaran.getCostService())));
        tvCostTransport.setText(Util.convertToRupiah(String.valueOf(pembayaran.getCostTransport())));
        tvCostTotal.setText(Util.convertToRupiah(String.valueOf(pembayaran.getCostTotal())));
    }

    public void back(View view) {
//        Intent i = new Intent(this, TimPembersihActivity.class);
//        startActivity(i);
        finish();
    }
}