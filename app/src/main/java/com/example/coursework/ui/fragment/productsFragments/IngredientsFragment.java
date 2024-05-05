package com.example.coursework.ui.fragment.productsFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentCookingBinding;
import com.example.coursework.databinding.FragmentIngredientsBinding;

public class IngredientsFragment extends Fragment {
    FragmentIngredientsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}