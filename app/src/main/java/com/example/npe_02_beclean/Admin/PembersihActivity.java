package com.example.npe_02_beclean.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.npe_02_beclean.Activities.TimPembersihActivity;
import com.example.npe_02_beclean.Adapters.AdminPembersihAdapter;
import com.example.npe_02_beclean.Models.AdminPembersih;
import com.example.npe_02_beclean.Models.TimPembersih;
import com.example.npe_02_beclean.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PembersihActivity extends AppCompatActivity implements AdminPembersihAdapter.OnItemClick {

    // recyclerview attr
    private RecyclerView rvPembersihAdmin;
    private AdminPembersihAdapter adminPembersihAdapter;
    private List<AdminPembersih> adminPembersihList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembersih);

        // initialize adapter with empty list
        adminPembersihList = new ArrayList<>();
        adminPembersihAdapter = new AdminPembersihAdapter(adminPembersihList, this);

        // set recyclerview
        rvPembersihAdmin = findViewById(R.id.rv_pembersih_admin);
        rvPembersihAdmin.setHasFixedSize(true);
        rvPembersihAdmin.setLayoutManager(new LinearLayoutManager(this));
        rvPembersihAdmin.setAdapter(adminPembersihAdapter);

        // update adapter list with data from firebase
        updatePembersihList();

    }

    private void updatePembersihList() {
        // get tim reference
        DatabaseReference pembersihanRef = FirebaseDatabase.getInstance().getReference()
                .child("teams");
        pembersihanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    // update the adapter's list
                    AdminPembersih adminPembersih = new AdminPembersih();
                    adminPembersih.setName(data.child("name").getValue().toString());
                    adminPembersih.setImageUrl(data.child("url_img").getValue().toString());
                    adminPembersih.setCost(Integer.parseInt(data.child("cost").getValue().toString()));
                    adminPembersih.setTotalMember(Integer.parseInt(data.child("total_member").getValue().toString()));
                    adminPembersih.setRating(Integer.parseInt(data.child("rating").getValue().toString()));
                    adminPembersihList.add(adminPembersih);
                }
                adminPembersihAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PembersihActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void got(View view) {
        Intent i = new Intent(this, AddPembersihActivity.class);
        finish();
        startActivity(new Intent(this, PembersihActivity.class));
        startActivity(i);
    }

    @Override
    public void btnEditClicked(int position) {
        Toast.makeText(this, adminPembersihList.get(position).getName() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void btnDeleteClicked(int position) {
        // get tim reference
        DatabaseReference teamsRef = FirebaseDatabase.getInstance().getReference()
                .child("teams");
        teamsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pos = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (pos == position) {
                        // delete value
                        teamsRef.child(data.getKey()).removeValue();
                    }
                    pos++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PembersihActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(PembersihActivity.this, PembersihActivity.class));
        finish();
    }
}