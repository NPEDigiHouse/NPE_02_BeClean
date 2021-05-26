package com.example.npe_02_beclean.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.npe_02_beclean.Activities.CategoryActivity;
import com.example.npe_02_beclean.Models.KategoriPembersihan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Util {
    // shared preferences attr
    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String USER_ID_KEY = "user_id_key";

    // list category adapter
    public static List<KategoriPembersihan> categoryList;

    public static void setKategoriPembersihanList(String category) {
        List<KategoriPembersihan> tempList = new ArrayList<>();
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
                    tempList.add(kategoriPembersihan);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        categoryList = tempList;
    }


    public static void saveUserIdToLocal(Activity activity, String userId) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_ID_KEY, userId);
        editor.apply();
    }

    public static String getUserIdLocal(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID_KEY, "");
    }

    public static String convertToRupiah(String money) {
        String temp = "";
        for (int i = money.length() - 1, j = 1; i >= 0; i--, j++) {
            temp += money.charAt(i);
            if (j % 3 == 0 && i > 0) temp += ".";
        }

        String ans = "";
        for (int i = temp.length() - 1; i >= 0; i--) {
            ans += temp.charAt(i);
        }

        return "Rp. " + ans;
    }
}
