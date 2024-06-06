package com.example.coursework.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework.R;
import com.example.coursework.databinding.UserElementBinding;
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.entities.User;
import com.example.coursework.ui.fragment.adminFragment.OnEditClickListener;
import com.example.coursework.ui.fragment.adminFragment.UserDiffUtilCallback;
import com.example.coursework.ui.viewmodel.AdminViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final ArrayList<User> data;
    private final AdminViewModel viewModel;
    private final OnEditClickListener listener;

    public UserAdapter(AdminViewModel viewModel, OnEditClickListener listener) {
        this.viewModel = viewModel;
        this.listener = listener;
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_element, parent, false);
        return new UserViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User element = data.get(position);

        holder.name.setText(element.getUsername());
        holder.email.setText(element.getEmail());
        switch (element.getRole()) {
            case CREATOR:
                holder.creatorRadioButton.setChecked(true);
                holder.type.setText("Создатель");
                break;
            case ADMIN:
                holder.adminRadioButton.setChecked(true);
                holder.type.setText("Администратор");
                break;
            case USER:
                holder.userRadioButton.setChecked(true);
                holder.type.setText("Пользователь");
                break;
        }

        holder.adminRadioButton.setOnClickListener(v -> viewModel.changeType(element, UserType.ADMIN));

        holder.creatorRadioButton.setOnClickListener(v -> viewModel.changeType(element, UserType.CREATOR));

        holder.userRadioButton.setOnClickListener(v -> viewModel.changeType(element, UserType.USER));

        holder.editButton.setOnClickListener(v -> listener.onEditClick(element));

        holder.mainLayout.setOnClickListener(v -> {
            if (holder.addInfo.getVisibility() == View.GONE) {
                holder.addInfo.setVisibility(View.VISIBLE);
            } else {
                holder.addInfo.setVisibility(View.GONE);
            }
        });


    }

    public void updateList(List<User> newList) {
        UserDiffUtilCallback diffCallback = new UserDiffUtilCallback(this.data, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.data.clear();
        this.data.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        UserElementBinding binding;
        View addInfo;
        View mainLayout;
        TextView name;
        TextView email;
        TextView type;
        RadioGroup radioGroup;
        RadioButton creatorRadioButton;
        RadioButton adminRadioButton;
        RadioButton userRadioButton;

        MaterialButton editButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            binding = UserElementBinding.bind(itemView);
            addInfo = binding.addInfo;
            mainLayout = binding.getRoot();
            name = binding.name;
            email = binding.email;
            type = binding.type;
            radioGroup = binding.radioGroup;
            creatorRadioButton = binding.creator;
            adminRadioButton = binding.admin;
            userRadioButton = binding.user;
            editButton = binding.changeType;

        }
    }


}
