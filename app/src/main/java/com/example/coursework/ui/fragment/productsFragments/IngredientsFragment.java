package com.example.coursework.ui.fragment.productsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursework.databinding.FragmentIngredientsBinding;
import com.example.coursework.ui.adapter.IngredientAdapter;
import com.example.coursework.ui.viewmodel.ProductsViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class IngredientsFragment extends Fragment {
    FragmentIngredientsBinding binding;
    ProductsViewModel viewModel;
    IngredientAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false);

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(
                adapter,
                getContext(),
                position -> {
            showSnackbar(
                    binding.getRoot(),
                    Objects.requireNonNull(viewModel.ingredients.getValue()).get(position).getName());
            viewModel.removeIngredient(position);
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(binding.ingredientsRecyclerView);

        DividerItemDecoration divider = new DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
        );
        binding.ingredientsRecyclerView.addItemDecoration(divider);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new IngredientAdapter(viewModel.ingredients.getValue());
        binding.ingredientsRecyclerView.setAdapter(adapter);
        binding.ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.ingredients.observe(getViewLifecycleOwner(), adapter::updateList);
    }

    private void showSnackbar(View view, String text) {
        Snackbar snackbar = Snackbar.make(
                view,
                "Ингредиент " + text + " удален",
                Snackbar.LENGTH_LONG
        );
        snackbar.setAction("Вернуть", t -> {
            viewModel.cancelDeleting();
        });
        snackbar.show();
    }
}