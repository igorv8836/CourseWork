package com.example.coursework.ui.fragment.CookingFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.coursework.ui.adapter.BakeryProductionAdapter;
import com.example.coursework.ui.viewmodel.CookingViewModel;

import java.util.ArrayList;


public class CookingFragment extends Fragment {
    FragmentCookingBinding binding;
    CookingViewModel viewModel;
    BakeryProductionAdapter adapter;
    NavController navController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCookingBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(CookingViewModel.class);
        viewModel.getProductions();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_home_fragment);
        adapter = new BakeryProductionAdapter(new ArrayList<>(), id -> {
            viewModel.deleteProduction(id);
        });
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration divider = new DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
        );
        binding.recyclerView.addItemDecoration(divider);

        binding.fab.setOnClickListener(t -> {
            navController.navigate(R.id.nav_editing_production);
        });

        viewModel.bakeryProductions.observe(getViewLifecycleOwner(), bakeryProductions -> {
            adapter.setData(bakeryProductions);
        });

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.bakeryProductions.observe(getViewLifecycleOwner(), bakeryProductions -> {
            adapter.setData(bakeryProductions);
        });
    }
}