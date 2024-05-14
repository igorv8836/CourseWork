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
import com.example.coursework.ui.addClasses.OnClickListener;
import com.example.coursework.ui.fragment.ingredientFragment.IngredientDiffUtilCallback;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>{
    private final ArrayList<Ingredient> ingredients;
    OnClickListener listener;

    public IngredientAdapter(OnClickListener listener) {
        this.ingredients = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.name.setText(ingredient.getName());
        holder.measurement.setText(ingredient.getMeasurementText());
        holder.price.setText(ingredient.getPrice().toString() + " р/" + ingredient.getMeasurementText());

        holder.mainLayout.setOnClickListener(v -> {
            listener.onClick(ingredient.getId());
        });
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

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        IngredientItemBinding binding;
        ImageView imageView;
        View mainLayout;
        TextView name;
        TextView measurement;
        TextView price;
        public IngredientViewHolder(View itemView) {
            super(itemView);
            binding = IngredientItemBinding.bind(itemView);
            mainLayout = binding.mainLayout;
            name = binding.name;
            measurement = binding.measurement;
            price = binding.price;
        }
    }


}
