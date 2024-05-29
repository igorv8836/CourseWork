package com.example.coursework.ui.fragment.adminFragment;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentAdminBinding;
import com.example.coursework.databinding.UserEditingDialogBinding;
import com.example.coursework.ui.adapter.UserAdapter;
import com.example.coursework.ui.entities.User;
import com.example.coursework.ui.viewmodel.AdminViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class AdminFragment extends Fragment {
    FragmentAdminBinding binding;
    AdminViewModel viewModel;
    UserAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminViewModel.class);

        adapter = new UserAdapter(viewModel, this::showCreatingDialog);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        DividerItemDecoration divider = new DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
        );
        binding.recyclerView.addItemDecoration(divider);

        viewModel.users.observe(getViewLifecycleOwner(), users -> {
            adapter.updateList(users);
        });
        return binding.getRoot();
    }

    private void showCreatingDialog(User user) {
        LayoutInflater inflater = this.getLayoutInflater();
        UserEditingDialogBinding dialogBinding = UserEditingDialogBinding.bind(inflater.inflate(R.layout.user_editing_dialog, null));

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(dialogBinding.getRoot());

        dialogBinding.editName.setText(user.getUsername());
        dialogBinding.editLogin.setText(user.getEmail());

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String name = dialogBinding.editName.getText().toString();
            String email = dialogBinding.editLogin.getText().toString();
            String password = dialogBinding.editPassword.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                dialog.dismiss();
                Snackbar.make(binding.getRoot(), "Заполните все поля", Snackbar.LENGTH_SHORT).show();
                return;
            }

            user.setUsername(name);
            user.setEmail(email);

            viewModel.changeUser(user);

            dialog.dismiss();
        });
        builder.setNegativeButton("Отменить", (dialog, which) -> dialog.dismiss());

        builder.setTitle("Редактирование данных пользователя");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}