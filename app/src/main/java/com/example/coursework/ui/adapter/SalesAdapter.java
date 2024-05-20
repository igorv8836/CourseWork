package com.example.coursework.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.R;
import com.example.coursework.databinding.ProductSaleBinding;
import com.example.coursework.ui.entities.ProductSale;
import com.example.coursework.ui.addClasses.OnClickListener;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SalesViewHolder>{
    private ArrayList<ProductSale> data;
    private final OnClickListener listener;
    private boolean showDeleteButton = true;

    public SalesAdapter(List<ProductSale> data, OnClickListener listener) {
        this.data = new ArrayList<>(data);
        this.listener = listener;
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_sale, parent, false);
        return new SalesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SalesViewHolder holder, int position) {
        ProductSale element = data.get(position);
        holder.name.setText(element.getProduct().getName());
        holder.price.setText(element.getSalePrice() + " руб за единицу");
        holder.count.setText(element.getCount() + " шт");
        holder.revenue.setText("+" + element.getRevenue() + " руб");
        if (element.getRevenue() > 0)
            holder.revenue.setTextColor(holder.revenue.getContext().getResources().getColor(R.color.green));
        else
            holder.revenue.setTextColor(holder.revenue.getContext().getResources().getColor(R.color.light_red));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd.MM", Locale.getDefault());
        holder.date.setText(sdf.format(new Date(element.getSaleDate())));
        holder.ingredientsPrice.setText("Себестоимость: " + element.getIngredientsPrice() + " руб");
        holder.profit.setText("Прибыль: " + element.getProfit() + " руб");
        holder.deleteButton.setVisibility(showDeleteButton ? View.VISIBLE : View.GONE);

        holder.mainInfo.setOnClickListener(t -> {
            if (holder.additionalInfo.getVisibility() == View.GONE) {
                Animation slideDown = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_down);
                holder.additionalInfo.startAnimation(slideDown);
                holder.additionalInfo.setVisibility(View.VISIBLE);
            } else if (holder.additionalInfo.getVisibility() == View.VISIBLE) {
                Animation slideUp = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_up);
                slideUp.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.additionalInfo.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                holder.additionalInfo.startAnimation(slideUp);
            }
        });

        holder.deleteButton.setOnClickListener(t -> listener.onClick(element.getId()));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<ProductSale> newList) {
        data = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public void setShowDeleteButton(boolean showDeleteButton) {
        this.showDeleteButton = showDeleteButton;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class SalesViewHolder extends RecyclerView.ViewHolder {
        ProductSaleBinding binding;
        View mainInfo;
        TextView name;
        TextView price;
        TextView count;
        TextView revenue;
        TextView date;
        View additionalInfo;
        TextView ingredientsPrice;
        TextView profit;
        MaterialButton deleteButton;

        public SalesViewHolder(View itemView) {
            super(itemView);
            binding = ProductSaleBinding.bind(itemView);
            mainInfo = binding.mainInfo;
            name = binding.name;
            price = binding.price;
            count = binding.count;
            revenue = binding.revenue;
            date = binding.date;
            additionalInfo = binding.additionalInfo;
            ingredientsPrice = binding.ingredientsPrice;
            profit = binding.profit;
            deleteButton = binding.deleteButton;
        }
    }


}
