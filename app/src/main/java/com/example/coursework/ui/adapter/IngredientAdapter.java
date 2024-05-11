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
import com.example.coursework.ui.entities.Ingredient;
import com.example.coursework.ui.fragment.ingredientFragment.IngredientDiffUtilCallback;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngridientViewHolder>{
    private final ArrayList<Ingredient> ingredients;
    private final View.OnClickListener listener;

    public IngredientAdapter(List<Ingredient> ingredients, View.OnClickListener listener) {
        this.ingredients = new ArrayList<>(ingredients);
        this.listener = listener;
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
        Ingredient ingredient = ingredients.get(position);
        holder.name.setText(ingredient.getName());
        holder.measurement.setText(ingredient.getMeasurementText());
        holder.price.setText(ingredient.getPrice().toString() + " р/" + ingredient.getMeasurementText());
    }

    public void updateList(List<Ingredient> newList) {
        IngredientDiffUtilCallback diffCallback = new IngredientDiffUtilCallback(this.ingredients, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.ingredients.clear();
        this.ingredients.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class IngridientViewHolder extends RecyclerView.ViewHolder {
        IngredientItemBinding binding;
        ImageView imageView;
        TextView name;
        TextView measurement;
        TextView price;
        public IngridientViewHolder(View itemView) {
            super(itemView);
            binding = IngredientItemBinding.bind(itemView);
            name = binding.name;
            measurement = binding.measurement;
            price = binding.price;
        }
    }


}
