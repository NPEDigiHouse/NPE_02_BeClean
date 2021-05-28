package com.example.npe_02_beclean.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    //Maps Attribut
    TextView tvGoMaps;
    LocationManager locationManager;
    String latitude,longitude;
    Point myLocation;


    private static  final int REQUEST_LOCATION=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);


        //Add permission
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        // initialize widgets
        initializeWidgets();

        // set data to widgets
        setDataToWidgets();

        // maps
        tvGoMaps=findViewById(R.id.tv_alamat);
        tvGoMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //Check gps is enable or not
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    //Write Function To enable gps
                    OnGPS();
                }
                else
                {
                    //GPS is already On then
                    getLocation();
                    Intent i = new Intent(PembayaranActivity.this, MapActivity.class);
                    i.putExtra("position", myLocation);
                    startActivity(i);
                }
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
    }

    private void setDataToWidgets() {
        Pembayaran pembayaran = getIntent().getParcelableExtra(EXTRA_PEMBAYARAN);
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

    // open maps function -------------------------------------------------------------------------------------------------
    private void getLocation() {
        //Check Permissions again
        if (ActivityCompat.checkSelfPermission(PembayaranActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PembayaranActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();
                myLocation = Point.fromLngLat(longi,lat);
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();
                myLocation = Point.fromLngLat(longi,lat);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();
                myLocation = Point.fromLngLat(longi,lat);
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    public void back(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}