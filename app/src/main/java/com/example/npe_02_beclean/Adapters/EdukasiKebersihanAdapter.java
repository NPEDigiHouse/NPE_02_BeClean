package com.example.npe_02_beclean.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.npe_02_beclean.Models.EdukasiKebersihan;
import com.example.npe_02_beclean.R;

import java.util.List;

public class EdukasiKebersihanAdapter extends RecyclerView.Adapter<EdukasiKebersihanAdapter.ViewHolder> {

    private List<EdukasiKebersihan> edukasiKebersihanList;

    public EdukasiKebersihanAdapter(List<EdukasiKebersihan> edukasiKebersihanList) {
        this.edukasiKebersihanList = edukasiKebersihanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_educate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivImage.setImageResource(edukasiKebersihanList.get(position).getImg());
        holder.tvTitle.setText(edukasiKebersihanList.get(position).getTitle());
        holder.tvDesc.setText(edukasiKebersihanList.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return edukasiKebersihanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_image_edu);
            tvTitle = itemView.findViewById(R.id.tv_title_edu);
            tvDesc = itemView.findViewById(R.id.tv_desc_edu);
        }
    }
}
