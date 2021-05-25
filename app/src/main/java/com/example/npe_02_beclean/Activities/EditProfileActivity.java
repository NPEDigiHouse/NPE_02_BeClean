package com.example.npe_02_beclean.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    // widgets
    private CircleImageView civPhotoProfile;
    private TextInputEditText etNama, etAlamat, etEmail, etPassword;
    private Button btnSimpan;
    private ImageButton btnBack;

    // attributes
    private Uri photoLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // initialize widgets
        initializeWidgets();

        // set user data
        setUserData();

        // if button clicked
        btnBack.setOnClickListener(this);
        btnSimpan.setOnClickListener(this);
        civPhotoProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_photo_profile_edit_profile:
                openImageIntent();
                break;
            case R.id.btn_simpan_edit_profile:
                updateUserData();
                break;
            case R.id.ib_back_edit_profile:
                finish();
                break;
        }
    }

    private void initializeWidgets() {
        civPhotoProfile = findViewById(R.id.civ_photo_profile_edit_profile);
        etNama = findViewById(R.id.et_nama_edit_profile);
        etAlamat = findViewById(R.id.et_alamat_edit_profile);
        etEmail = findViewById(R.id.et_email_edit_profile);
        etPassword = findViewById(R.id.et_password_edit_profile);
        btnSimpan = findViewById(R.id.btn_simpan_edit_profile);
        btnBack = findViewById(R.id.ib_back_edit_profile);
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
                    .into(civPhotoProfile);
        }
    }

    private void openImageIntent() {
        Intent goToFindImage = new Intent();
        goToFindImage.setType("image/*");
        goToFindImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(goToFindImage, 1);
    }

    private void setUserData() {
        // get user data from firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(Util.getUserIdLocal(this));
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // set data to widgets
                etNama.setText(snapshot.child("name").getValue().toString());
                etAlamat.setText(snapshot.child("address").getValue().toString());
                etEmail.setText(snapshot.child("email").getValue().toString());
                etPassword.setText(snapshot.child("password").getValue().toString());

                Picasso.with(EditProfileActivity.this)
                        .load(snapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop()
                        .fit()
                        .into(civPhotoProfile);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUserData() {
        // change state to loading
        civPhotoProfile.setEnabled(false);
        btnBack.setEnabled(false);
        btnSimpan.setEnabled(false);
        btnSimpan.setText("Loading...");

        // get user data from edit text
        String nama = etNama.getText().toString();
        String alamat = etAlamat.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        // get user database reference
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(Util.getUserIdLocal(this));
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // update user data
                userRef.child("name").setValue(nama);
                userRef.child("address").setValue(alamat);
                userRef.child("email").setValue(email);
                userRef.child("password").setValue(password);

                // check if user update photo profile
                if (photoLocation != null) {
                    // get user storage reference
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                            .child("users")
                            .child(Util.getUserIdLocal(EditProfileActivity.this) + "_pp");

                    // store new photo profile to storage
                    storageRef.putFile(photoLocation).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // get url photo profile
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // update url photo profile
                                    userRef.child("url_photo_profile").setValue(uri.toString());
                                }
                            });
                        }
                    });
                }

                // after success update user data then go back to edit profile
                Toast.makeText(EditProfileActivity.this, "Profil anda berhasil diperbaharui.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
                // change back the state
                civPhotoProfile.setEnabled(true);
                btnBack.setEnabled(true);
                btnSimpan.setEnabled(true);
                btnSimpan.setText("Simpan");
            }
        });

    }

}