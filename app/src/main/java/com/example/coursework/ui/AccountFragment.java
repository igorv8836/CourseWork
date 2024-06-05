package com.example.coursework.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentAccountBinding;
import com.example.coursework.ui.viewmodel.AuthViewModel;

import java.util.Objects;

public class AccountFragment extends Fragment {
    FragmentAccountBinding binding;
    AuthViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        viewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.editTextNameInputText.setText(user.getUsername());
            }
        });

        binding.saveButton.setOnClickListener(v -> {
            viewModel.saveUser(
                    Objects.requireNonNull(binding.editTextLastPasswordInputText.getText()).toString(),
                    Objects.requireNonNull(binding.editTextNewPasswordInputText.getText()).toString(),
                    Objects.requireNonNull(binding.editTextNameInputText.getText()).toString()
            );
        });

        viewModel.helpText.observe(getViewLifecycleOwner(), event -> {
            if (event != null) {
                String text = event.getContentIfNotHandled();
                if (text != null) {
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.isActive.observe(getViewLifecycleOwner(), isActive -> {
            binding.saveButton.setEnabled(isActive);
        });

        return binding.getRoot();
    }
}