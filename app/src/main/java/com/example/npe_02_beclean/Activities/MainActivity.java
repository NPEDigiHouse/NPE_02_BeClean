package com.example.npe_02_beclean.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.npe_02_beclean.R;
import com.example.npe_02_beclean.Fragments.HomeFragment;
import com.example.npe_02_beclean.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private Map<Integer, Fragment> fragmentMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        fragmentMap = new HashMap<>();


    }

    @Override
    protected void onStart() {
        super.onStart();
        fragmentMap.put(R.id.menu_item_home, HomeFragment.newInstance());
        fragmentMap.put(R.id.menu_item_profile, ProfileFragment.newInstance());
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_item_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item_pay){
            Intent i = new Intent(MainActivity.this, PembayaranActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            return true;
        }
        Fragment fragment = fragmentMap.get(item.getItemId());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_main, fragment)
                .commit();
        return true;
    }


}