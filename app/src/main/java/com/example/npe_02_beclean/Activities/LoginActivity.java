package com.example.npe_02_beclean.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.npe_02_beclean.Helpers.Util;
import com.example.npe_02_beclean.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends Activity implements View.OnClickListener {

    // widgets
    private Button btnMasuk;
    private TextView btnDaftar;
    private TextInputEditText etEmail, etPassword;

    // attributes
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize widgets
        etEmail = findViewById(R.id.et_email_login);
        etPassword = findViewById(R.id.et_password_login);
        btnMasuk = findViewById(R.id.btn_masuk_login);
        btnDaftar = findViewById(R.id.tv_daftar_login);

        // if button clicked
        btnMasuk.setOnClickListener(this);
        btnDaftar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_masuk_login:
                btnMasukClicked();
                break;
            case R.id.tv_daftar_login:
                Intent goToRegister = new Intent(this, RegisterActivity.class);
                startActivity(goToRegister);
                break;
        }
    }

    private void btnMasukClicked() {
        // change state to loading
        btnDaftar.setEnabled(false);
        btnMasuk.setEnabled(false);
        btnMasuk.setText("Loading...");

        // check account existence
        checkAccount();
    }

    private void checkAccount() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        // find user id on database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isUserExist = false;
                for (DataSnapshot data : snapshot.getChildren()) {
                    String tempEmail = data.child("email").getValue().toString();
                    if (tempEmail.equals(email)) {
                        isUserExist = true;
                        userId = data.getKey();

                        // validate user data
                        DatabaseReference tempUserRef = FirebaseDatabase.getInstance().getReference()
                                .child("users")
                                .child(userId);
                        tempUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // validate password
                                String tempPassword = snapshot.child("password").getValue().toString();
                                if (tempPassword.equals(password)) {
                                    // save user id to local (user's device)
                                    Util.saveUserIdToLocal(LoginActivity.this, userId);

                                    // move to main activity
                                    Intent goToMain = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(goToMain);
                                    finish();
                                } else {
                                    // show alert to user
                                    Toast.makeText(LoginActivity.this, "Password anda salah.", Toast.LENGTH_SHORT).show();

                                    // change back the state
                                    btnDaftar.setEnabled(true);
                                    btnMasuk.setEnabled(true);
                                    btnMasuk.setText("Masuk");
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }
                }
                if (!isUserExist) {
                    // show alert to user
                    Toast.makeText(LoginActivity.this, "Username anda tidak ditemukan.", Toast.LENGTH_SHORT).show();

                    // change back the state
                    btnDaftar.setEnabled(true);
                    btnMasuk.setEnabled(true);
                    btnMasuk.setText("Masuk");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Terjadi kesalahan pada database.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}