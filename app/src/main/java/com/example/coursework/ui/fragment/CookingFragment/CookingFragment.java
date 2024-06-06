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
import android.widget.Toast;

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

            if (bakeryProductions.isEmpty()) {
                binding.emptyIndicator.emptyIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.emptyIndicator.emptyIndicator.setVisibility(View.GONE);
            }
        });

        viewModel.showAdminFunctions.observe(getViewLifecycleOwner(), showAdminFunctions -> {
            adapter.setDeleteButtonVisible(showAdminFunctions);
            if (showAdminFunctions) {
                binding.fab.setVisibility(View.VISIBLE);
            } else {
                binding.fab.setVisibility(View.GONE);
            }
        });

        viewModel.helpText.observe(getViewLifecycleOwner(), t -> {
            if (t == null) return;
            String text = t.getContentIfNotHandled();
            if (text != null)
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
        });



        return binding.getRoot();
    }
}