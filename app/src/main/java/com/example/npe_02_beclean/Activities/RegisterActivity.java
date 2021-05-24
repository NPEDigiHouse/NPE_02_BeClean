package com.example.npe_02_beclean.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    // widgets
    private CircleImageView civPhotoProfile;
    private TextInputEditText etNama, etAlamat, etEmail, etPassword;
    private CheckBox cbSetuju;
    private Button btnDaftar;
    private TextView btnMasuk;

    // attributes
    private Uri photoLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialize all attributes
        initializeWidgets();

        // if button clicked
        civPhotoProfile.setOnClickListener(this);
        btnDaftar.setOnClickListener(this);
        btnMasuk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.civ_photo_profile_register:
                openImageIntent();
                break;
            case R.id.btn_daftar_register:
                if (cbSetuju.isChecked()) {
                    // change state to loading
                    btnMasuk.setEnabled(false);
                    btnDaftar.setEnabled(false);
                    btnDaftar.setText("Loading...");

                    saveUserData();
                } else {
                    Toast.makeText(this, "Tolong centang persyaratan kami :)", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_masuk_register:
                finish();
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
                    .into(civPhotoProfile);
        }
    }

    private void initializeWidgets() {
        civPhotoProfile = findViewById(R.id.civ_photo_profile_register);
        etNama = findViewById(R.id.et_nama_register);
        etAlamat = findViewById(R.id.et_alamat_register);
        etEmail = findViewById(R.id.et_email_register);
        etPassword = findViewById(R.id.et_password_register);
        btnDaftar = findViewById(R.id.btn_daftar_register);
        btnMasuk = findViewById(R.id.tv_masuk_register);
        cbSetuju = findViewById(R.id.cb_setuju_register);
    }

    private void openImageIntent() {
        Intent goToFindImage = new Intent();
        goToFindImage.setType("image/*");
        goToFindImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(goToFindImage, 1);
    }

    private void saveUserData() {
        // get user data from textview
        String nama = etNama.getText().toString();
        String alamat = etAlamat.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String userId = generateRandomNumber().toString();

        // check if user don't upload a photo then use default image
        if (photoLocation == null) {
            photoLocation = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.example_face);
        }

        // store user photo profile to storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("users")
                .child(userId + "_pp"); // pp stands for photo profile
        storageRef.putFile(photoLocation).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // get url photo profile
                        String urlPhotoProfile = uri.toString();

                        // make user data map
                        Map<String, String> userDataMap = new HashMap<String, String>() {{
                            put("name", nama);
                            put("address", alamat);
                            put("email", email);
                            put("password", password);
                            put("url_photo_profile", urlPhotoProfile);
                        }};

                        // add user data to firebase
                        FirebaseDatabase.getInstance().getReference()
                                .child("users")
                                .child(userId)
                                .setValue(userDataMap);
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        // save user id to local (user's device)
                        Util.saveUserIdToLocal(RegisterActivity.this, userId);

                        // move to MainActivity
                        Intent goToMain = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(goToMain);
                        finishAffinity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();

                        // change back the state
                        btnMasuk.setEnabled(true);
                        btnDaftar.setEnabled(true);
                        btnDaftar.setText("Masuk");
                    }
                });
            }
        });





    }

    private Integer generateRandomNumber() {
        return new Random().nextInt(99999) + 1;
    }


}