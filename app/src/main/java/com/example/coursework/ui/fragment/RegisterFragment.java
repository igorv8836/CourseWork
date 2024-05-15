package com.example.coursework.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentLoginBinding;
import com.example.coursework.databinding.FragmentRegisterBinding;
import com.example.coursework.ui.viewmodel.AuthViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;
    NavController navController;
    AuthViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        Toolbar toolbar = binding.toolbar;
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());

        viewModel.loggedUser.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                navController.navigate(R.id.mainFragment);
            }
        });

        binding.buttonRegister.setOnClickListener(v -> {
            String name = binding.editTextNameInputText.getText().toString();
            String email = binding.editTextEmailInputText.getText().toString();
            String password = binding.editTextPasswordInputText.getText().toString();
            viewModel.createAccount(name, email, password);
        });

        viewModel.helpText.observe(getViewLifecycleOwner(), helpText -> {
            if (helpText != null) {
                Snackbar.make(view, helpText, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}