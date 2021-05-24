package com.example.npe_02_beclean.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.npe_02_beclean.Activities.LoginActivity;
import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.R;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    // widgets
    private TextView btnKeluar;

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
        btnKeluar = view.findViewById(R.id.tv_keluar_profile);
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
}