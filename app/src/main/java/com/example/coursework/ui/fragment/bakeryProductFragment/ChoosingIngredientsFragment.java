package com.example.coursework.ui.fragment.bakeryProductFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentChoosingIngredientsBinding;
import com.example.coursework.ui.adapter.ChoosingIngredientAdapter;
import com.example.coursework.ui.viewmodel.ProductsViewModel;

import java.util.ArrayList;

public class ChoosingIngredientsFragment extends Fragment {
    FragmentChoosingIngredientsBinding binding;
    ProductsViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChoosingIngredientsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ChoosingIngredientAdapter adapter = new ChoosingIngredientAdapter(new ArrayList<>());

        viewModel.chosenIngredients.observe(getViewLifecycleOwner(), t->{
            adapter.setData(new ArrayList<>(t));
        });
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}