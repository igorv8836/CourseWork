package com.example.coursework.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coursework.R;
import com.example.coursework.databinding.BakeryProductBinding;
import com.example.coursework.ui.entities.BakeryProduct;

import java.util.ArrayList;
import java.util.List;

public class BakeryProductAdapter extends RecyclerView.Adapter<BakeryProductAdapter.BakeryProductViewHolder>{
    private ArrayList<BakeryProduct> bakeryProducts;
    private NavController navController;

    public BakeryProductAdapter(NavController navController) {
        this.bakeryProducts = new ArrayList<>();
        this.navController = navController;
    }

    @NonNull
    @Override
    public BakeryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bakery_product, parent, false);
        return new BakeryProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BakeryProductViewHolder holder, int position) {
        BakeryProduct bakeryProduct = bakeryProducts.get(position);
        Glide.with(holder.binding.getRoot()).load(bakeryProduct.getImageUri()).into(holder.imageView);
        holder.name.setText(bakeryProduct.getName());
        holder.description.setText(bakeryProduct.getDescription());
        holder.price.setText(bakeryProduct.getPrice() + " руб.");

        holder.binding.mainLayout.setOnClickListener(t -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", bakeryProducts.get(position).getId());
            navController.navigate(R.id.nav_editing_bakery_products, bundle);
        });
    }

    public void setBakeryProducts(List<BakeryProduct> bakeryProducts) {
        this.bakeryProducts = new ArrayList<>(bakeryProducts);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return bakeryProducts.size();
    }

    class BakeryProductViewHolder extends RecyclerView.ViewHolder {
        BakeryProductBinding binding;
        ImageView imageView;
        TextView name;
        TextView description;
        TextView price;
        public BakeryProductViewHolder(View itemView) {
            super(itemView);
            binding = BakeryProductBinding.bind(itemView);
            imageView = binding.image;
            name = binding.name;
            description = binding.desc;
            price = binding.price;
        }
    }


}
