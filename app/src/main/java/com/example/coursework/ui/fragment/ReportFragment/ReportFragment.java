package com.example.coursework.ui.fragment.ReportFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentCookingBinding;
import com.example.coursework.databinding.FragmentReportBinding;
import com.example.coursework.ui.adapter.ReportInfoAdapter;
import com.example.coursework.ui.viewmodel.ReportViewModel;

import java.util.ArrayList;


public class ReportFragment extends Fragment {
    FragmentReportBinding binding;
    ReportViewModel viewModel;
    ReportInfoAdapter productionAdapter;
    ReportInfoAdapter salesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productionAdapter = new ReportInfoAdapter(new ArrayList<>());
        salesAdapter = new ReportInfoAdapter(new ArrayList<>());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);

        binding.productionRecyclerView.setAdapter(productionAdapter);
        binding.salesRecyclerView.setAdapter(salesAdapter);

        binding.productionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.salesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.productions.observe(getViewLifecycleOwner(), productionAdapter::updateList);
        viewModel.sales.observe(getViewLifecycleOwner(), salesAdapter::updateList);

        DividerItemDecoration divider = new DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
        );
        binding.salesRecyclerView.addItemDecoration(divider);
        binding.productionRecyclerView.addItemDecoration(divider);

        binding.timePeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.updateData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewModel.profit.observe(getViewLifecycleOwner(), profit -> {
            binding.totalProfitNumber.setText(profit + " руб");
        });
        viewModel.revenue.observe(getViewLifecycleOwner(), revenue -> {
            binding.totalRevenueNumber.setText(revenue + " руб");
        });
        viewModel.expenses.observe(getViewLifecycleOwner(), expenses -> {
            binding.totalCostsNumber.setText(expenses + " руб");
        });

        return binding.getRoot();
    }
}