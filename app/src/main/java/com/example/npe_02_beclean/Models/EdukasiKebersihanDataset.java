package com.example.npe_02_beclean.Models;

import android.content.Context;
import android.content.res.TypedArray;

import com.example.npe_02_beclean.R;

import java.util.ArrayList;
import java.util.List;

public class EdukasiKebersihanDataset {
    private Context context;
    private String[] titles;
    private String[] descriptions;
    private TypedArray images;

    public EdukasiKebersihanDataset(Context context) {
        this.context = context;
        titles = context.getResources().getStringArray(R.array.edu_title_list);
        descriptions = context.getResources().getStringArray(R.array.edu_desc_list);
        images = context.getResources().obtainTypedArray(R.array.edu_image_list);
    }

    public List<EdukasiKebersihan> getDataset() {
        List<EdukasiKebersihan> list = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            EdukasiKebersihan edu = new EdukasiKebersihan();
            edu.setTitle(titles[i]);
            edu.setDesc(descriptions[i]);
            edu.setImg(images.getResourceId(i, -1));
            list.add(edu);
        }
        return list;
    }
}
