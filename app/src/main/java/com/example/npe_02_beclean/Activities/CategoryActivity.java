package com.example.npe_02_beclean.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.npe_02_beclean.Adapters.KategoriPembersihanAdapter;
import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.Models.KategoriPembersihan;
import com.example.npe_02_beclean.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements KategoriPembersihanAdapter.OnItemClick {

    public static final String EXTRA_CATEGORY = "extra_category";

    private KategoriPembersihanAdapter kategoriPembersihanAdapter;
    private RecyclerView rvCategory;
    private List<KategoriPembersihan> kategoriPembersihanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // initialize widgets
        rvCategory = findViewById(R.id.rv_category);

        // create a empty list cause we don't know what kategori pembersihan will be clicked
        kategoriPembersihanList = new ArrayList<>();
        kategoriPembersihanAdapter = new KategoriPembersihanAdapter(kategoriPembersihanList, this);

        // set recyclerview
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new GridLayoutManager(this, 2));
        rvCategory.setAdapter(kategoriPembersihanAdapter);

        // update the adapter with kategori pembersihan data from firebase
        String category = getIntent().getStringExtra(EXTRA_CATEGORY);
        setKategoriPembersihanList(category);

    }


    private void setKategoriPembersihanList(String category) {
        DatabaseReference pembersihanRef = FirebaseDatabase.getInstance().getReference()
                .child("pembersihan")
                .child(category);
        pembersihanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Log.d("CATEGORY", data.getKey());
                    KategoriPembersihan kategoriPembersihan = new KategoriPembersihan();
                    kategoriPembersihan.setTitle(data.child("title").getValue().toString());
                    kategoriPembersihan.setUrlImage(data.child("url_img").getValue().toString());
                    kategoriPembersihanList.add(kategoriPembersihan);
                }
                kategoriPembersihanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(this, "You clicked " + kategoriPembersihanList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }
}