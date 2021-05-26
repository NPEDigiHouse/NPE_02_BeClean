package com.example.npe_02_beclean.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.npe_02_beclean.Adapters.EdukasiKebersihanAdapter;
import com.example.npe_02_beclean.Models.EdukasiKebersihanDataset;
import com.example.npe_02_beclean.R;

public class EdukasiActivity extends AppCompatActivity {

    // widgets
    private ImageButton btnBack;

    // recyclerview attributes
    private RecyclerView rvEdukasi;
    private EdukasiKebersihanAdapter edukasiKebersihanAdapter;
    private EdukasiKebersihanDataset edukasiKebersihanDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edukasi);

        // initialization
        edukasiKebersihanDataset = new EdukasiKebersihanDataset(this);
        edukasiKebersihanAdapter = new EdukasiKebersihanAdapter(edukasiKebersihanDataset.getDataset());
        btnBack = findViewById(R.id.ib_back_edukasi);
        rvEdukasi = findViewById(R.id.rv_edukasi);

        // set recyclerview
        rvEdukasi.setHasFixedSize(true);
        rvEdukasi.setLayoutManager(new LinearLayoutManager(this));
        rvEdukasi.setAdapter(edukasiKebersihanAdapter);

        // if button clicked
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}