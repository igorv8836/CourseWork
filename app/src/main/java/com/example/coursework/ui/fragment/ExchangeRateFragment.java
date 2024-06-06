package com.example.coursework.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.coursework.databinding.FragmentExchangeRateBinding;
import com.example.coursework.ui.viewmodel.ExchangeRateViewModel;

public class ExchangeRateFragment extends Fragment {
    FragmentExchangeRateBinding binding;
    ExchangeRateViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExchangeRateBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ExchangeRateViewModel.class);

        viewModel.rates.observe(getViewLifecycleOwner(), currencyResponse -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, currencyResponse);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.choosingValuteSpinner.setAdapter(adapter);
        });

        binding.choosingValuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.getRate(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewModel.rate.observe(getViewLifecycleOwner(), currency -> {
            binding.valute.setText(currency.getNominal() + " " + currency.getCharCode());
            binding.rusValute.setText(currency.getValue() + " RUB");
        });



        return binding.getRoot();
    }
}