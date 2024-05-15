package com.example.coursework.ui.fragment.adminFragment;

import androidx.recyclerview.widget.DiffUtil;

import com.example.coursework.ui.entities.Ingredient;
import com.example.coursework.ui.entities.User;

import java.util.List;

public class UserDiffUtilCallback extends DiffUtil.Callback {
    private final List<User> oldList;
    private final List<User> newList;

    public UserDiffUtilCallback(List<User> oldList, List<User> newList) {
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
        return oldList.get(oldItemPosition).getId() == (newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}

