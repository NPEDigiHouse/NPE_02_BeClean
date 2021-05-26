package com.example.npe_02_beclean.Adapters;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.npe_02_beclean.Models.KategoriPembersihan;
import com.example.npe_02_beclean.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class KategoriPembersihanAdapter extends RecyclerView.Adapter<KategoriPembersihanAdapter.ViewHolder> {
    private List<KategoriPembersihan> kategoriPembersihanList;
    private OnItemClick onItemClick;

    public KategoriPembersihanAdapter(List<KategoriPembersihan> kategoriPembersihanList, OnItemClick onItemClick) {
        this.kategoriPembersihanList = kategoriPembersihanList;
        this.onItemClick = onItemClick;
    }

    public KategoriPembersihanAdapter() {
        // empty constructor
    }

    public void setKategoriPembersihanList(List<KategoriPembersihan> kategoriPembersihanList) {
        this.kategoriPembersihanList = kategoriPembersihanList;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_pembersihan, parent, false);

        return new ViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // set data to widgets on recyclerview item
        Picasso.with(holder.itemView.getContext())
                .load(kategoriPembersihanList.get(position).getUrlImage())
                .centerCrop().fit()
                .into(holder.ivImage);
        holder.tvTitle.setText(kategoriPembersihanList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return kategoriPembersihanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClick onItemClick;
        ImageView ivImage;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);

            this.onItemClick = onItemClick;
            itemView.setOnClickListener(this);

            ivImage = itemView.findViewById(R.id.iv_image_item_category);
            tvTitle = itemView.findViewById(R.id.tv_title_item_category);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onClick(getAbsoluteAdapterPosition());
        }
    }

    public interface OnItemClick {
        void onClick(int position);
    }

}
