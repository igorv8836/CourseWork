package com.example.coursework.ui.fragment.side_main_fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentCookingBinding;
import com.example.coursework.databinding.FragmentSalesBinding;

public class SalesFragment extends Fragment {
    FragmentSalesBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSalesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}