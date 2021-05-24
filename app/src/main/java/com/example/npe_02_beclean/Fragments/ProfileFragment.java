package com.example.npe_02_beclean.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npe_02_beclean.Activities.LoginActivity;
import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    // widgets
    private CircleImageView civPhotoProfile;
    private TextView btnKeluar, tvName;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        // initialize widgets
        civPhotoProfile = view.findViewById(R.id.civ_photo_profile_profile);
        tvName = view.findViewById(R.id.tv_name_profile);
        btnKeluar = view.findViewById(R.id.tv_keluar_profile);

        // set user data
        setUserData();


        // if button clicked
        btnKeluar.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_keluar_profile:
                // delete user id value from shared preferences
                Util.saveUserIdToLocal(getActivity(), null);

                // move to login activity
                Intent goToLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(goToLogin);
                getActivity().finishAffinity();
                break;
        }
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