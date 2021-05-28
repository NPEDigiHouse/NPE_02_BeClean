package com.example.npe_02_beclean.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimPembersihActivity extends AppCompatActivity implements TimPembersihAdapter.OnItemClick {

    // widgets
    CircleImageView civPhotoProfile;

    // recyclerview attr
    private RecyclerView rvTimPembersih;
    private TimPembersihAdapter timPembersihAdapter;
    private List<TimPembersih> timPembersihList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_pembersih);

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
        // move to MapActivity
        Intent goToMap = new Intent(this, MapActivity.class);
        startActivity(goToMap);
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
    }
}