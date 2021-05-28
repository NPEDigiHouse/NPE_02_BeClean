package com.example.npe_02_beclean.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.npe_02_beclean.Activities.MainActivity;
import com.example.npe_02_beclean.Activities.RegisterActivity;
import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddPembersihActivity extends AppCompatActivity implements View.OnClickListener {

    // widgets
    private ImageView ivPicCleaner;
    private Button btnPicCleaner, btnTambahkan;
    private EditText etNamaTim, etTarifPerorang, etTambahAnggota;

    // attr
    private Uri photoLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pembersih);

        // initialize widgets
        initializeWidgets();

        // if button clicked
        btnPicCleaner.setOnClickListener(this);
        btnTambahkan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pic_cleaner:
                openImageIntent();
                break;
            case R.id.btn_tambahkan:
                // change state to loading
                btnPicCleaner.setEnabled(false);
                btnTambahkan.setEnabled(false);
                btnTambahkan.setText("Loading...");
                saveTeamData();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // get uri photo
            photoLocation = data.getData();

            // set to widgets
            Picasso.with(this).load(photoLocation)
                    .centerCrop().fit()
                    .into(ivPicCleaner);
        }
    }

    private void initializeWidgets() {
        ivPicCleaner = findViewById(R.id.iv_pic_cleaner);
        btnPicCleaner = findViewById(R.id.btn_pic_cleaner);
        btnTambahkan = findViewById(R.id.btn_tambahkan);
        etNamaTim = findViewById(R.id.et_nama_tim);
        etTarifPerorang = findViewById(R.id.et_tarif_perorang);
        etTambahAnggota = findViewById(R.id.et_tambah_anggota);
    }

    private void openImageIntent() {
        Intent goToFindImage = new Intent();
        goToFindImage.setType("image/*");
        goToFindImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(goToFindImage, 1);
    }

    private void saveTeamData() {
        // set user data
        String teamId = generateRandomNumber().toString();
        String namaTim = etNamaTim.getText().toString();
        String tarifPerorang = etTarifPerorang.getText().toString();
        String jumlahAnggota = etTambahAnggota.getText().toString();
        int rating = 4;

        // check if user don't upload a photo then use default image
        if (photoLocation == null) {
            photoLocation = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.example_face);
        }

        // store user photo profile to storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("teams")
                .child(teamId + "_img");
        storageRef.putFile(photoLocation).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // get url photo profile
                        String urlPhotoProfile = uri.toString();

                        // make user data map
                        Map<String, Object> teamDataMap = new HashMap<String, Object>() {{
                            put("name", namaTim);
                            put("cost", tarifPerorang);
                            put("total_member", jumlahAnggota);
                            put("rating", rating);
                            put("url_img", urlPhotoProfile);
                        }};

                        // add user data to firebase
                        FirebaseDatabase.getInstance().getReference()
                                .child("teams")
                                .child(teamId)
                                .setValue(teamDataMap);
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        // back to pembersih activity
                        Toast.makeText(AddPembersihActivity.this, "Tim berhasil ditambahkan.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPembersihActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private Integer generateRandomNumber() {
        return new Random().nextInt(99999) + 1;
    }


}