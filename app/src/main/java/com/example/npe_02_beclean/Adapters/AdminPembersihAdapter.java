package com.example.npe_02_beclean.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.npe_02_beclean.Models.AdminPembersih;
import com.example.npe_02_beclean.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminPembersihAdapter extends RecyclerView.Adapter<AdminPembersihAdapter.ViewHolder> {
    private OnItemClick onItemClick;
    private List<AdminPembersih> adminPembersihList;

    public AdminPembersihAdapter(List<AdminPembersih> adminPembersihList, OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
        this.adminPembersihList = adminPembersihList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pembersih, parent, false);

        return new AdminPembersihAdapter.ViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // set data to widgets on recyclerview item
        Picasso.with(holder.itemView.getContext())
                .load(adminPembersihList.get(position).getImageUrl())
                .fit()
                .into(holder.ivImage);
        holder.tvName.setText(adminPembersihList.get(position).getName());
        holder.tvTotalMember.setText(String.valueOf(adminPembersihList.get(position).getTotalMember()));
    }

    @Override
    public int getItemCount() {
        return adminPembersihList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClick onItemClick;
        ImageView ivImage;
        TextView tvName, tvTotalMember;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            this.onItemClick = onItemClick;

            // initialize widgets
            ivImage = itemView.findViewById(R.id.iv_image_item_pembersih);
            tvName = itemView.findViewById(R.id.tv_team_name_item_pembersih);
            tvTotalMember = itemView.findViewById(R.id.tv_total_member_item_pembersih);
            btnDelete = itemView.findViewById(R.id.ib_delete_item_pembersih);

            // if button clicked
            btnDelete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ib_delete_item_pembersih) {
                onItemClick.btnDeleteClicked(getAbsoluteAdapterPosition());
            }
        }
    }

    public interface OnItemClick {
        void btnDeleteClicked(int position);
    }
}
