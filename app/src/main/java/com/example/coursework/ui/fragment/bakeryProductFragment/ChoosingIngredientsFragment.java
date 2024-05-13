package com.example.coursework.ui.fragment.bakeryProductFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursework.databinding.FragmentChoosingIngredientsBinding;
import com.example.coursework.ui.adapter.ChoosingIngredientAdapter;
import com.example.coursework.ui.viewmodel.ProductsViewModel;

import java.util.ArrayList;

public class ChoosingIngredientsFragment extends Fragment {
    FragmentChoosingIngredientsBinding binding;
    ProductsViewModel viewModel;
    Integer productId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChoosingIngredientsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        if (getArguments() != null) {
            if (getArguments().containsKey("id"))
                productId = getArguments().getInt("id");
        }

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ChoosingIngredientAdapter adapter = new ChoosingIngredientAdapter(new ArrayList<>());

        viewModel.chosenIngredients.observe(getViewLifecycleOwner(), t -> {
            adapter.setData(new ArrayList<>(t));
        });

        viewModel.ingredients.observe(getViewLifecycleOwner(), t -> {
            viewModel.getChosenIngredients(productId);
        });
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}