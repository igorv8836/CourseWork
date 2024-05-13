package com.example.coursework.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.R;
import com.example.coursework.databinding.IngredientItemBinding;
import com.example.coursework.databinding.ReportElementBinding;
import com.example.coursework.ui.entities.Ingredient;
import com.example.coursework.ui.entities.ReportElement;
import com.example.coursework.ui.fragment.ingredientFragment.IngredientDiffUtilCallback;

import java.util.ArrayList;
import java.util.List;

public class ReportInfoAdapter extends RecyclerView.Adapter<ReportInfoAdapter.ReportInfoViewHolder>{
    private ArrayList<ReportElement> data;

    public ReportInfoAdapter(List<ReportElement> data) {
        this.data = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public ReportInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_element, parent, false);
        return new ReportInfoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReportInfoViewHolder holder, int position) {
        ReportElement element = data.get(position);
        holder.name.setText(element.getName());
        holder.count.setText(element.getCount() + " шт");
        if (element.isProduction()) {
            holder.priceLayout.setVisibility(View.GONE);
        } else {
            holder.price.setText(element.getRevenue() + "/" + element.getProfit() + " руб");
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<ReportElement> newList) {
        data = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ReportInfoViewHolder extends RecyclerView.ViewHolder {
        ReportElementBinding binding;
        TextView name;
        TextView count;
        TextView price;
        View priceLayout;
        public ReportInfoViewHolder(View itemView) {
            super(itemView);
            binding = ReportElementBinding.bind(itemView);
            name = binding.name;
            count = binding.count;
            price = binding.price;
            priceLayout = binding.priceLayout;
        }
    }


}
