package com.example.coursework.ui.fragment.bakeryProductFragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.coursework.R;
import com.example.coursework.databinding.FragmentEditingBakeryProductsBinding;
import com.example.coursework.ui.viewmodel.ProductsViewModel;


public class EditingBakeryProductsFragment extends Fragment {
    FragmentEditingBakeryProductsBinding binding;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    ActivityResultLauncher<String> requestPermissionLauncher;
    NavController navController;
    ProductsViewModel viewModel;
    Integer productId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);

        if (getArguments() != null) {
            productId = getArguments().getInt("id");
            viewModel.getProduct(productId);
        }

        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Glide.with(binding.getRoot()).load(uri).centerCrop().into(binding.image);
                        binding.editTextUriInputText.setText(uri.toString());
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        getPhoto();
                    } else {
                        Log.d("PhotoPicker", "Permission denied");
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEditingBakeryProductsBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_home_fragment);

        binding.chooseIngredients.setOnClickListener(t -> {
            Bundle bundle = new Bundle();
            if (productId != null)
                bundle.putInt("id", productId);
            navController.navigate(R.id.nav_choosing_ingredients, bundle);
        });

        if (productId != null) {
            viewModel.bakeryProduct.observe(getViewLifecycleOwner(), product -> {
                binding.editTextNameInputText.setText(product.getName());
                binding.editTextDescriptionInputText.setText(product.getDescription());
                binding.editTextPriceInputText.setText(String.valueOf(product.getPrice()));
                binding.editTextUriInputText.setText(product.getImageUri());
                Glide.with(binding.getRoot()).load(product.getImageUri()).centerCrop().into(binding.image);
            });
        }

        binding.saveButton.setOnClickListener(t -> {
            String name = binding.editTextNameInputText.getText().toString();
            String description = binding.editTextDescriptionInputText.getText().toString();
            double price = Double.parseDouble(binding.editTextPriceInputText.getText().toString());
            String imageUri = binding.editTextUriInputText.getText().toString();
            if (productId == null) {
                viewModel.createProduct(name, description, price, imageUri);
            } else {
                viewModel.updateProduct(productId, name, description, price, imageUri);
            }
            navController.popBackStack();
        });

        binding.deleteButton.setOnClickListener(t -> {
            if (productId != null) {
                viewModel.deleteProduct(productId);
                navController.popBackStack();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.image.setOnClickListener(t -> {
            getPermission(requireContext());
        });

    }

    public void getPhoto() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }


    public void getPermission(Context context) {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_IMAGES) ==
                PackageManager.PERMISSION_GRANTED)
        || (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED)){
            getPhoto();
        } else {
            requestPermissionLauncher.launch(
                    permission);
        }
    }
}