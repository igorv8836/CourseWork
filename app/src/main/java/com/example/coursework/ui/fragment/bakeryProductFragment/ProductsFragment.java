package com.example.coursework.ui.fragment.bakeryProductFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.coursework.databinding.FragmentProductsBinding;
import com.example.coursework.ui.adapter.ProductsPagerAdapter;
import com.example.coursework.ui.fragment.ingredientFragment.IngredientsFragment;
import com.example.coursework.ui.viewmodel.ProductsViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class ProductsFragment extends Fragment {
    List<String> tabElements;
    FragmentProductsBinding binding;
    ProductsViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabElements = new ArrayList<>();
        tabElements.add("Готовые изделия");
        tabElements.add("Ингредиенты");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);


        ViewPager2 viewPager = binding.viewPager;
        TabLayout tabLayout = binding.tabs;

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new BakeryProductsFragment());
        fragments.add(new IngredientsFragment());

        ProductsPagerAdapter adapter = new ProductsPagerAdapter(this.getActivity(), fragments);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabElements.get(position))
        ).attach();

        viewModel.helpText.observe(getViewLifecycleOwner(), t -> {
            if (t == null) return;
            String text = t.getContentIfNotHandled();
            if (text != null)
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
        });


        return binding.getRoot();
    }
}