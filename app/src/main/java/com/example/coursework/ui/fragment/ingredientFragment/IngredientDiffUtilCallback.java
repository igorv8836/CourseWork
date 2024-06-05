package com.example.coursework.ui.fragment.ingredientFragment;

import androidx.recyclerview.widget.DiffUtil;

import com.example.coursework.ui.entities.Ingredient;

import java.util.List;
import java.util.Objects;

public class IngredientDiffUtilCallback extends DiffUtil.Callback {
    private final List<Ingredient> oldList;
    private final List<Ingredient> newList;

    public IngredientDiffUtilCallback(List<Ingredient> oldList, List<Ingredient> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldList.get(oldItemPosition).getId(), newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}

