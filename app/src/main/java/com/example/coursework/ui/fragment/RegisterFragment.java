package com.example.coursework.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentRegisterBinding;
import com.example.coursework.ui.viewmodel.AuthViewModel;

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

        viewModel.nextScreen.observe(getViewLifecycleOwner(), t -> {
            if (t != null) {
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.mainFragment, true)
                        .build();

                navController.navigate(R.id.mainFragment, null, navOptions);
            }
        });

        binding.buttonRegister.setOnClickListener(v -> {
            String name = Objects.requireNonNull(binding.editTextNameInputText.getText()).toString();
            String email = Objects.requireNonNull(binding.editTextEmailInputText.getText()).toString();
            String password = Objects.requireNonNull(binding.editTextPasswordInputText.getText()).toString();
            viewModel.createAccount(name, email, password);
        });

        viewModel.helpText.observe(getViewLifecycleOwner(), t -> {
            if (t == null) return;
            String text = t.getContentIfNotHandled();
            if (text != null)
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
        });

        viewModel.isActive.observe(getViewLifecycleOwner(), t -> {
            if (t != null) {
                binding.buttonRegister.setEnabled(t);
            }
        });
    }
}