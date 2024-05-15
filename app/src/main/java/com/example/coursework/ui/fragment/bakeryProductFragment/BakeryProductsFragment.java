package com.example.coursework.ui.fragment.bakeryProductFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentBakeryProductsBinding;
import com.example.coursework.ui.adapter.BakeryProductAdapter;
import com.example.coursework.ui.viewmodel.ProductsViewModel;


public class BakeryProductsFragment extends Fragment {
    FragmentBakeryProductsBinding binding;
    ProductsViewModel viewModel;
    BakeryProductAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBakeryProductsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_home_fragment);

        adapter = new BakeryProductAdapter(navController);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        viewModel.bakeryProducts.observe(getViewLifecycleOwner(), bakeryProducts -> {
            adapter.setBakeryProducts(bakeryProducts);
        });

        binding.fab.setOnClickListener(t -> {
            navController.navigate(R.id.nav_editing_bakery_products);
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.clearChosenIngredients();
    }
}