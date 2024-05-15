package com.example.coursework.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentLoginBinding;
import com.example.coursework.ui.viewmodel.AuthViewModel;


public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    NavController navController;
    AuthViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.registerButton.setOnClickListener(v -> {
            navController.navigate(R.id.registerFragment);
        });

        binding.logInButton.setOnClickListener(v -> {
            String email = binding.editTextEmailInputText.getText().toString();
            String password = binding.editTextPasswordInputText.getText().toString();
            viewModel.login(email, password);
        });

        viewModel.loggedUser.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                navController.navigate(R.id.mainFragment);
            }
        });


    }
}