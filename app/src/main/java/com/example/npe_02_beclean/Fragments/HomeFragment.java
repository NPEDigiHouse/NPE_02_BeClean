package com.example.npe_02_beclean.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npe_02_beclean.Activities.CategoryActivity;
import com.example.npe_02_beclean.Activities.EdukasiActivity;
import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment implements View.OnClickListener {

    // widgets
    private CircleImageView civPhotoProfile;
    private TextView tvName, tvMoney;
    private CardView cvRumah, cvSekolah, cvTaman, cvToko, cvApartemen, cvEdukasi;


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        // initialize widgets
        initializeWidgets(view);

        // set user data to widgets
        setUserData();

        // if button clicked
        cvRumah.setOnClickListener(this);
        cvSekolah.setOnClickListener(this);
        cvTaman.setOnClickListener(this);
        cvToko.setOnClickListener(this);
        cvApartemen.setOnClickListener(this);
        cvEdukasi.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent goToCategory = new Intent(getActivity(), CategoryActivity.class);
        switch (v.getId()) {
            case R.id.cv_rumah_home:
                goToCategory.putExtra(CategoryActivity.EXTRA_CATEGORY, "rumah");
                goToCategory.putExtra(CategoryActivity.EXTRA_TITLE, "Rumah");
                goToCategory.putExtra(CategoryActivity.EXTRA_ICON, R.drawable.ic_rumah);
                break;
            case R.id.cv_sekolah_home:
                goToCategory.putExtra(CategoryActivity.EXTRA_CATEGORY, "sekolah");
                goToCategory.putExtra(CategoryActivity.EXTRA_TITLE, "Sekolah");
                goToCategory.putExtra(CategoryActivity.EXTRA_ICON, R.drawable.ic_sekolah);
                break;
            case R.id.cv_taman_home:
                goToCategory.putExtra(CategoryActivity.EXTRA_CATEGORY, "taman");
                goToCategory.putExtra(CategoryActivity.EXTRA_TITLE, "Taman");
                goToCategory.putExtra(CategoryActivity.EXTRA_ICON, R.drawable.ic_taman);
                break;
            case R.id.cv_toko_home:
                goToCategory.putExtra(CategoryActivity.EXTRA_CATEGORY, "toko");
                goToCategory.putExtra(CategoryActivity.EXTRA_TITLE, "Toko");
                goToCategory.putExtra(CategoryActivity.EXTRA_ICON, R.drawable.ic_toko);
                break;
            case R.id.cv_apartemen_home:
                goToCategory.putExtra(CategoryActivity.EXTRA_CATEGORY, "apartemen");
                goToCategory.putExtra(CategoryActivity.EXTRA_TITLE, "Apartemen");
                goToCategory.putExtra(CategoryActivity.EXTRA_ICON, R.drawable.ic_apartment_black_18dp);
                break;
            case R.id.cv_edukasi_home:
                Intent goToEdukasi = new Intent(getActivity(), EdukasiActivity.class);
                startActivity(goToEdukasi);
                return;
        }
        startActivity(goToCategory);
    }

    private void initializeWidgets(View view) {
        civPhotoProfile = view.findViewById(R.id.civ_photo_profile_home);
        tvName = view.findViewById(R.id.tv_name_home);
        tvMoney = view.findViewById(R.id.tv_money_home);
        cvRumah = view.findViewById(R.id.cv_rumah_home);
        cvSekolah = view.findViewById(R.id.cv_sekolah_home);
        cvTaman = view.findViewById(R.id.cv_taman_home);
        cvToko = view.findViewById(R.id.cv_toko_home);
        cvApartemen = view.findViewById(R.id.cv_apartemen_home);
        cvEdukasi = view.findViewById(R.id.cv_edukasi_home);
    }

    private void setUserData() {
        // get user data from firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(Util.getUserIdLocal(getActivity()));
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // set data to widgets
                tvName.setText(snapshot.child("name").getValue().toString());
                tvMoney.setText(Util.convertToRupiah(snapshot.child("money").getValue().toString()));
                Picasso.with(getActivity())
                        .load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop()
                        .fit()
                        .into(civPhotoProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}