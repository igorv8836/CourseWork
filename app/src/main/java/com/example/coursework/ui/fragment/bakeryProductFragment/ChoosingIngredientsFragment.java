package com.example.coursework.ui.fragment.bakeryProductFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursework.databinding.FragmentChoosingIngredientsBinding;
import com.example.coursework.ui.adapter.ChoosingIngredientAdapter;
import com.example.coursework.ui.viewmodel.ProductsViewModel;

import java.util.ArrayList;

public class ChoosingIngredientsFragment extends Fragment {
    FragmentChoosingIngredientsBinding binding;
    ProductsViewModel viewModel;
    NavController navController;
    Integer productId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChoosingIngredientsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);
        navController = Navigation.findNavController(requireActivity(), com.example.coursework.R.id.nav_host_home_fragment);

        if (getArguments() != null) {
            if (getArguments().containsKey("id")) {
                productId = getArguments().getInt("id");
            }
        }
        viewModel.getChosenIngredients(productId);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ChoosingIngredientAdapter adapter = new ChoosingIngredientAdapter(new ArrayList<>());

        DividerItemDecoration divider = new DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
        );
        binding.recyclerView.addItemDecoration(divider);

        binding.saveButton.setOnClickListener(v -> {
            viewModel.updateChosenIngredients(adapter.getData());
            navController.popBackStack();
        });

        viewModel.chosenIngredients.observe(getViewLifecycleOwner(), t -> {
            if (t != null)
                adapter.setData(new ArrayList<>(t));
        });

        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}