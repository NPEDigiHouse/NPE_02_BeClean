package com.example.npe_02_beclean.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    // widgets
    private CircleImageView civPhotoProfile;
    private TextView tvName, tvMoney;

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


        return view;
    }

    private void initializeWidgets(View view) {
        civPhotoProfile = view.findViewById(R.id.civ_photo_profile_home);
        tvName = view.findViewById(R.id.tv_name_home);
        tvMoney = view.findViewById(R.id.tv_money_home);
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