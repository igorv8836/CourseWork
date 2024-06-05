package com.example.coursework.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coursework.R;
import com.example.coursework.databinding.BakeryProductionBinding;
import com.example.coursework.ui.entities.BakeryProduction;
import com.example.coursework.ui.addClasses.OnClickListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BakeryProductionAdapter extends RecyclerView.Adapter<BakeryProductionAdapter.BakeryProductionViewHolder>{
    private ArrayList<BakeryProduction> data;
    private final OnClickListener listener;
    private boolean isDeleteButtonVisible = true;

    public BakeryProductionAdapter(List<BakeryProduction> data, OnClickListener listener) {
        this.data = new ArrayList<>(data);
        this.listener = listener;
    }

    @NonNull
    @Override
    public BakeryProductionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bakery_production, parent, false);
        return new BakeryProductionViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull BakeryProductionViewHolder holder, int position) {
        BakeryProduction element = data.get(position);
        Glide.with(holder.binding.getRoot()).load(element.getProduct().getImageUri()).centerCrop().into(holder.imageView);
        holder.name.setText(element.getProduct().getName());
        holder.price.setText(element.getProduct().getPrice() + " руб.");
        holder.count.setText(element.getCount() + " шт.");
        holder.button.setVisibility(isDeleteButtonVisible ? View.VISIBLE : View.GONE);

        long currentTime = System.currentTimeMillis();

        long startTime = element.getStartTime();
        long endTime = element.getEndTime();

        if (currentTime < startTime) {
            holder.status.setText("Не начато");
            holder.status.setTextColor(R.color.light_red);
            holder.progress.setProgress(0);
        } else if (currentTime <= endTime) {
            holder.status.setText("В производстве");
            int totalDuration = (int) (endTime - startTime);
            int passedDuration = (int) (currentTime - startTime);
            int progress = (int) ((passedDuration / (float) totalDuration) * 100);
            holder.status.setTextColor(R.color.orange);
            holder.progress.setProgress(progress);
        } else {
            holder.status.setText("Завершено");
            holder.status.setTextColor(R.color.light_green);
            holder.progress.setProgress(100);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm", Locale.getDefault());
        holder.startTime.setText(sdf.format(new Date(startTime)));
        holder.endTime.setText(sdf.format(new Date(endTime)));

        holder.button.setOnClickListener(v -> {
            listener.onClick(element.getId());
        });

    }

    public void setData(List<BakeryProduction> data) {
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public void setDeleteButtonVisible(boolean visible) {
        isDeleteButtonVisible = visible;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BakeryProductionViewHolder extends RecyclerView.ViewHolder {
        BakeryProductionBinding binding;
        ImageView imageView;
        TextView name;
        TextView price;
        TextView count;
        TextView status;
        LinearProgressIndicator progress;
        TextView startTime;
        TextView endTime;
        ImageButton button;
        public BakeryProductionViewHolder(View itemView) {
            super(itemView);
            binding = BakeryProductionBinding.bind(itemView);
            imageView = binding.image;
            name = binding.name;
            price = binding.price;
            count = binding.count;
            status = binding.status;
            progress = binding.progressBar;
            startTime = binding.startTime;
            endTime = binding.endTime;
            button = binding.deleteButton;
        }
    }


}
