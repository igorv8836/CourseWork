package com.example.coursework.ui.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentLoginBinding;
import com.example.coursework.ui.viewmodel.AuthViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


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

        viewModel.nextScreen.observe(getViewLifecycleOwner(), t -> {
            if (t != null) {
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.mainFragment, true)
                        .build();

                navController.navigate(R.id.mainFragment, null, navOptions);
            }
        });

        viewModel.helpText.observe(getViewLifecycleOwner(), t -> {
            if (t == null) return;
            String text = t.getContentIfNotHandled();
            if (text != null)
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
        });


        binding.recoverButton.setOnClickListener(t -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
            builder.setTitle("Восстановить пароль");

            final EditText input = new EditText(requireContext());
            input.setHint("Введите ваш email");
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            builder.setView(input);

            builder.setPositiveButton("Восстановить", (dialog, which) -> {
                String email = input.getText().toString();
                viewModel.recoverPassword(email);
            });
            builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        viewModel.isActive.observe(getViewLifecycleOwner(), t -> {
            if (t != null) {
                binding.logInButton.setEnabled(t);
            }
        });

    }
}