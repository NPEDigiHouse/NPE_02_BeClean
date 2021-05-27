package com.example.npe_02_beclean.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryActivity extends AppCompatActivity implements KategoriPembersihanAdapter.OnItemClick {

    // extras
    public static final String EXTRA_CATEGORY = "extra_category";
    public static final String EXTRA_ICON = "extra_icon";
    public static final String EXTRA_TITLE = "extra_title";

    // recyclerview attr
    private KategoriPembersihanAdapter kategoriPembersihanAdapter;
    private RecyclerView rvCategory;
    private List<KategoriPembersihan> kategoriPembersihanList;

    // widgets
    private CircleImageView civPhotoProfile;
    private ImageView ivIconCategory;
    private TextView tvName, tvTitleCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // initialize widgets
        tvName = findViewById(R.id.tv_name_category);
        tvTitleCategory = findViewById(R.id.tv_title_category);
        civPhotoProfile = findViewById(R.id.civ_photo_profile_category);
        ivIconCategory = findViewById(R.id.iv_icon_category);

        // create an empty list cause we don't know what kategori pembersihan will be clicked
        kategoriPembersihanList = new ArrayList<>();
        kategoriPembersihanAdapter = new KategoriPembersihanAdapter(kategoriPembersihanList, this);

        // set recyclerview
        rvCategory = findViewById(R.id.rv_category);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new GridLayoutManager(this, 2));
        rvCategory.setAdapter(kategoriPembersihanAdapter);

        // update the adapter with kategori pembersihan data from firebase
        String category = getIntent().getStringExtra(EXTRA_CATEGORY);
        setKategoriPembersihanList(category);

        // set category data to widgets
        setCategoryData();

    }

    private void setCategoryData() {
        // get user data from firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(Util.getUserIdLocal(this));
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // set data to widgets
                tvName.setText(snapshot.child("name").getValue().toString());
                Picasso.with(CategoryActivity.this)
                        .load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop()
                        .fit()
                        .into(civPhotoProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
            }
        });

        // set category icon
        ivIconCategory.setImageResource(getIntent().getIntExtra(EXTRA_ICON, -1));
        tvTitleCategory.setText(getIntent().getStringExtra(EXTRA_TITLE));
    }


    private void setKategoriPembersihanList(String category) {
        // get pembersihan reference
        DatabaseReference pembersihanRef = FirebaseDatabase.getInstance().getReference()
                .child("pembersihan")
                .child(category);
        pembersihanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Log.d("CATEGORY", data.getKey());
                    // update the adapter's list
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
        // move to TimPembersihActivity
        Intent goToTimPembersih = new Intent(this, TimPembersihActivity.class);
        startActivity(goToTimPembersih);
    }
}