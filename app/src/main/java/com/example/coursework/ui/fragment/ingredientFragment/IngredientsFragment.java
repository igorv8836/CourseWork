package com.example.coursework.ui.fragment.ingredientFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursework.R;
import com.example.coursework.databinding.CreatingDialogBinding;
import com.example.coursework.databinding.FragmentIngredientsBinding;
import com.example.coursework.ui.adapter.IngredientAdapter;
import com.example.coursework.ui.viewmodel.ProductsViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class IngredientsFragment extends Fragment {
    FragmentIngredientsBinding binding;
    ProductsViewModel viewModel;
    IngredientAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false);

        DividerItemDecoration divider = new DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
        );
        binding.ingredientsRecyclerView.addItemDecoration(divider);

        binding.fab.setOnClickListener(t -> {
            showCreatingDialog(true, null);
        });

        viewModel.showAdminFunctions.observe(getViewLifecycleOwner(), show -> {
            if (show) {
                binding.fab.setVisibility(View.VISIBLE);
                ItemTouchHelper itemTouchHelper = getItemTouchHelper();
                itemTouchHelper.attachToRecyclerView(binding.ingredientsRecyclerView);
            } else {
                binding.fab.setVisibility(View.GONE);
            }
        });

        return binding.getRoot();
    }

    @NonNull
    private ItemTouchHelper getItemTouchHelper() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(
                adapter,
                getContext(),
                position -> {
                    showSnackbar(
                            binding.getRoot(),
                            Objects.requireNonNull(viewModel.ingredients.getValue()).get(position).getName());
                    viewModel.removeIngredient(position);
                });
        return new ItemTouchHelper(swipeToDeleteCallback);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new IngredientAdapter(id -> {
            viewModel.getIngredient(id);
            if (Boolean.TRUE.equals(viewModel.showAdminFunctions.getValue()))
                showCreatingDialog(false, id);
        });
        binding.ingredientsRecyclerView.setAdapter(adapter);
        binding.ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.ingredients.observe(getViewLifecycleOwner(), data -> {
            adapter.updateList(data);
            if (data.isEmpty()) {
                binding.emptyIndicator.emptyIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.emptyIndicator.emptyIndicator.setVisibility(View.GONE);
            }

        });
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

    @SuppressLint("SetTextI18n")
    private void showCreatingDialog(boolean isCreating, Integer id) {
        LayoutInflater inflater = this.getLayoutInflater();
        CreatingDialogBinding dialogBinding = CreatingDialogBinding.bind(inflater.inflate(R.layout.creating_dialog, null));

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(dialogBinding.getRoot());

        if (!isCreating && id != null) {
            viewModel.ingredient.observe(getViewLifecycleOwner(), ingredient -> {
                if (ingredient != null) {
                    dialogBinding.getRoot().setTag(ingredient.getId());
                    dialogBinding.editName.setText(ingredient.getName());
                    dialogBinding.editMeasurement.setText(ingredient.getMeasurementText());
                    dialogBinding.editPrice.setText(ingredient.getPrice().toString());
                }
            });
        }

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            Integer ingredientId = (Integer) dialogBinding.getRoot().getTag();
            String name = Objects.requireNonNull(dialogBinding.editName.getText()).toString();
            String measurement = Objects.requireNonNull(dialogBinding.editMeasurement.getText()).toString();
            double price;
            if (dialogBinding.editPrice.getText().toString().isEmpty()){
                price = 0.0;
            } else {
                price = Double.parseDouble(dialogBinding.editPrice.getText().toString());
            }
            if (isCreating)
                viewModel.createIngredient(name, measurement, price);
            else
                viewModel.updateIngredient(ingredientId, name, measurement, price);
        });
        builder.setNegativeButton("Отменить", (dialog, which) -> dialog.dismiss());
        if (isCreating)
            builder.setTitle("Создание ингредиента");
        else
            builder.setTitle("Редактирование ингредиента");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}