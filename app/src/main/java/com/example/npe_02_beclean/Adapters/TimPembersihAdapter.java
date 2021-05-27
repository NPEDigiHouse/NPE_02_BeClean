package com.example.npe_02_beclean.Adapters;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.npe_02_beclean.Models.TimPembersih;
import com.example.npe_02_beclean.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;


public class TimPembersihAdapter extends RecyclerView.Adapter<TimPembersihAdapter.ViewHolder> {
    private OnItemClick onItemClick;
    private List<TimPembersih> timPembersihanList;

    public TimPembersihAdapter(List<TimPembersih> timPembersihanList, OnItemClick onItemClick) {
        this.timPembersihanList = timPembersihanList;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cleaner_tim, parent, false);

        return new ViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // set data to widgets on recyclerview item
        Picasso.with(holder.itemView.getContext())
                .load(timPembersihanList.get(position).getImageUrl())
                .fit()
                .into(holder.ivImage);
        holder.tvName.setText(timPembersihanList.get(position).getName());
        holder.tvCost.setText(String.valueOf(timPembersihanList.get(position).getCost()));
    }

    @Override
    public int getItemCount() {
        return timPembersihanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClick onItemClick;
        TextView tvName, tvQuantity, tvCost;
        Button btnOrder;
        ImageButton btnMinus, btnPlus;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);

            this.onItemClick = onItemClick;

            // initialize widgets
            ivImage = itemView.findViewById(R.id.iv_image_item_cleaner_tim);
            tvName = itemView.findViewById(R.id.tv_name_item_cleaner_tim);
            tvQuantity = itemView.findViewById(R.id.tv_quantity_item_cleaner_tim);
            tvCost = itemView.findViewById(R.id.tv_cost_item_cleaner_tim);
            btnOrder = itemView.findViewById(R.id.btn_order_item_cleaner_tim);
            btnMinus = itemView.findViewById(R.id.ib_minus_item_cleaner_tim);
            btnPlus = itemView.findViewById(R.id.ib_plus_item_cleaner_tim);

            // if button clicked
            btnOrder.setOnClickListener(this);
            btnPlus.setOnClickListener(this);
            btnMinus.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_order_item_cleaner_tim:
                    onItemClick.btnOrderClicked(getAbsoluteAdapterPosition());
                    break;
                case R.id.ib_plus_item_cleaner_tim:
                    onItemClick.btnPlusClicked(getAbsoluteAdapterPosition());
                    break;
                case R.id.ib_minus_item_cleaner_tim:
                    onItemClick.btnMinusCLicked(getAbsoluteAdapterPosition());
                    break;
            }
        }
    }

    public interface OnItemClick {
        void btnPlusClicked(int position);
        void btnMinusCLicked(int position);
        void btnOrderClicked(int position);
    }

}
