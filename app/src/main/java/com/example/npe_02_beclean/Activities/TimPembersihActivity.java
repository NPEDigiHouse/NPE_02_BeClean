package com.example.npe_02_beclean.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npe_02_beclean.Adapters.TimPembersihAdapter;
import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.Models.KategoriPembersihan;
import com.example.npe_02_beclean.Models.TimPembersih;
import com.example.npe_02_beclean.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimPembersihActivity extends AppCompatActivity implements TimPembersihAdapter.OnItemClick {

    // extras
    public static final String EXTRA_CATEGORY = "extra_category";

    // widgets
    private CircleImageView civPhotoProfile;
    private TextView tvQuantity, tvCost;

    // recyclerview attr
    private RecyclerView rvTimPembersih;
    private TimPembersihAdapter timPembersihAdapter;
    private List<TimPembersih> timPembersihList;

    // map attr
    private static  final int REQUEST_LOCATION=1;
    LocationManager locationManager;
    Point myLocation;

    // attr
    private int finalCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_pembersih);

        //Add permission
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        // initialize widgets
        civPhotoProfile = findViewById(R.id.civ_photo_profile_tim_pembersih);

        // initialize adapter with empty list
        timPembersihList = new ArrayList<>();
        timPembersihAdapter = new TimPembersihAdapter(timPembersihList, this);

        // set recyclerview
        rvTimPembersih = findViewById(R.id.rv_tim_pembersih);
        rvTimPembersih.setHasFixedSize(true);
        rvTimPembersih.setLayoutManager(new LinearLayoutManager(this));
        rvTimPembersih.setAdapter(timPembersihAdapter);

        // update adapter's list with data from firebase
        updateTimPembersihList();
    }

    private void updateTimPembersihList() {
        // get tim reference
        DatabaseReference pembersihanRef = FirebaseDatabase.getInstance().getReference()
                .child("teams");
        pembersihanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    // update the adapter's list
                    TimPembersih timPembersih = new TimPembersih();
                    timPembersih.setName(data.child("name").getValue().toString());
                    timPembersih.setCost(Integer.parseInt(data.child("cost").getValue().toString()));
                    timPembersih.setTotalMember(Integer.parseInt(data.child("total_member").getValue().toString()));
                    timPembersih.setImageUrl(data.child("url_img").getValue().toString());
                    timPembersihList.add(timPembersih);
                }
                timPembersihAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TimPembersihActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void btnOrderClicked(int position) {
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

            // move to MapActivity
            Intent goToMap = new Intent(this, MapActivity.class);
            goToMap.putExtra(MapActivity.EXTRA_POSITION, myLocation);

            // from prev intent
            goToMap.putExtra(EXTRA_CATEGORY, getIntent().getStringExtra(EXTRA_CATEGORY));

            // from this intent
            goToMap.putExtra(MapActivity.EXTRA_TEAM_NAME, timPembersihList.get(position).getName());
            if (tvCost == null && finalCost == 0) {
                goToMap.putExtra(MapActivity.EXTRA_QUANTITY, "1");
                goToMap.putExtra(MapActivity.EXTRA_COST, timPembersihList.get(position).getCost());
            } else {
                goToMap.putExtra(MapActivity.EXTRA_QUANTITY, tvQuantity.getText().toString());
                goToMap.putExtra(MapActivity.EXTRA_COST, finalCost);
            }
            startActivity(goToMap);
        }

    }

    @Override
    public void btnPlusClicked(int position, TextView tvQuantity, TextView tvCost) {
        // update quantity
        int tempQuantity = Integer.parseInt(tvQuantity.getText().toString());
        int totalMember = timPembersihList.get(position).getTotalMember();
        String timName = timPembersihList.get(position).getName();
        if (tempQuantity + 1 > totalMember) {
            Toast.makeText(
                    this,
                    String.format("Tim %s hanya memiliki %d anggota.", timName, totalMember),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            tvQuantity.setText(String.valueOf(++tempQuantity));
        }

        // update total cost
        int cost = timPembersihList.get(position).getCost();
        int tempTotal = tempQuantity * cost;
        tvCost.setText(Util.convertToRupiah(String.valueOf(tempTotal)));

        // set TimPembersihActivty extra
        this.tvQuantity = tvQuantity;
        this.tvCost = tvCost;
        finalCost = tempTotal;
    }

    @Override
    public void btnMinusClicked(int position, TextView tvQuantity, TextView tvCost) {
        // update quantity
        int tempQuantity = Integer.parseInt(tvQuantity.getText().toString());
        if (tempQuantity - 1 < 1) {
            Toast.makeText(this, "Minimal kamu harus memesan 1 pembersih.", Toast.LENGTH_SHORT).show();
        } else {
            tvQuantity.setText(String.valueOf(--tempQuantity));
        }

        // update total cost
        int cost = timPembersihList.get(position).getCost();
        int tempTotal = tempQuantity * cost;
        tvCost.setText(Util.convertToRupiah(String.valueOf(tempTotal)));

        // set TimPembersihActivty extra
        this.tvQuantity = tvQuantity;
        this.tvCost = tvCost;
        finalCost = tempTotal;
    }

    // open maps function -------------------------------------------------------------------------------------------------
    private void getLocation() {
        //Check Permissions again
        if (ActivityCompat.checkSelfPermission(TimPembersihActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TimPembersihActivity.this,

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
}