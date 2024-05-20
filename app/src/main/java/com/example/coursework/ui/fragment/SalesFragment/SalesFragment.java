package com.example.coursework.ui.fragment.SalesFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentCookingBinding;
import com.example.coursework.databinding.FragmentSalesBinding;
import com.example.coursework.ui.adapter.SalesAdapter;
import com.example.coursework.ui.viewmodel.SalesViewModel;

import java.util.ArrayList;

public class SalesFragment extends Fragment {
    FragmentSalesBinding binding;
    SalesViewModel viewModel;
    SalesAdapter adapter;
    NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSalesBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SalesViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_home_fragment);

        viewModel.getSales();


        adapter = new SalesAdapter(new ArrayList<>(), id -> {
            viewModel.deleteSale(id);
        });

        binding.salesRecyclerView.setAdapter(adapter);
        binding.salesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        DividerItemDecoration divider = new DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
        );
        binding.salesRecyclerView.addItemDecoration(divider);

        viewModel.sales.observe(getViewLifecycleOwner(), sales -> {
            adapter.updateList(sales);

            if (sales.isEmpty()) {
                binding.emptyIndicator.emptyIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.emptyIndicator.emptyIndicator.setVisibility(View.GONE);
            }
        });

        binding.fab.setOnClickListener(v -> {
            navController.navigate(R.id.nav_adding_sale);
        });

        viewModel.showAdminFunctions.observe(getViewLifecycleOwner(), show -> {
            adapter.setShowDeleteButton(show);
            if (show) {
                binding.fab.setVisibility(View.VISIBLE);
            } else {
                binding.fab.setVisibility(View.GONE);
            }
        });


        return binding.getRoot();
    }
}