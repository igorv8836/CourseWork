package com.example.coursework.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.R;
import com.example.coursework.databinding.IngredientItemBinding;
import com.example.coursework.ui.entities.ChosenIngredient;
import com.example.coursework.ui.entities.Ingredient;
import com.example.coursework.ui.fragment.ingredientFragment.IngredientDiffUtilCallback;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ChoosingIngredientAdapter extends RecyclerView.Adapter<ChoosingIngredientAdapter.IngridientViewHolder> {
    private ArrayList<ChosenIngredient> ingredients;
    private boolean isUnableToChoose = true;

    public ChoosingIngredientAdapter(List<ChosenIngredient> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }

    @NonNull
    @Override
    public IngridientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new IngridientViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IngridientViewHolder holder, int position) {
        ChosenIngredient ingredient = ingredients.get(position);
        holder.name.setText(ingredient.getName());
        holder.measurement.setText(ingredient.getMeasurementText());
        holder.price.setText(ingredient.getPrice().toString() + " р/" + ingredient.getMeasurementText());
        holder.countTextInputLayout.setVisibility(View.VISIBLE);
        holder.count.setFocusable(isUnableToChoose);
        if (ingredient.isChosen())
            holder.count.setText(ingredient.getCount().toString());

        if (ingredient.isChosen()) {
            holder.count.setText(ingredient.getCount().toString());
            holder.mainLayout.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.mainLayout.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Double cnt;
                    if (s.toString().equals(""))
                        cnt = 0.0;
                    else
                        cnt = Double.valueOf(s.toString());
                    ingredients.get(position).setCount(Double.parseDouble(s.toString()));
                    if (cnt != 0) {
                        holder.mainLayout.setBackgroundColor(Color.LTGRAY);
                    } else
                        holder.mainLayout.setBackgroundColor(Color.TRANSPARENT);
                } catch (Exception e) {
                    ingredients.get(position).setCount(0.0);
                    holder.mainLayout.setBackgroundColor(Color.TRANSPARENT);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public List<ChosenIngredient> getData() {
        return ingredients;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<ChosenIngredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUnableToChoose(boolean unableToChoose) {
        isUnableToChoose = unableToChoose;
        notifyDataSetChanged();
    }

    class IngridientViewHolder extends RecyclerView.ViewHolder {
        IngredientItemBinding binding;
        ImageView imageView;
        TextView name;
        TextView measurement;
        TextView price;
        EditText count;
        TextInputLayout countTextInputLayout;
        View mainLayout;

        public IngridientViewHolder(View itemView) {
            super(itemView);
            binding = IngredientItemBinding.bind(itemView);
            name = binding.name;
            measurement = binding.measurement;
            price = binding.price;
            count = binding.editCount;
            countTextInputLayout = binding.editTextCount;
            mainLayout = binding.mainLayout;
        }
    }


}
