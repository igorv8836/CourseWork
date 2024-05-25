package com.example.coursework.ui.fragment;

import static androidx.core.app.ActivityCompat.invalidateOptionsMenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentMainBinding;
import com.example.coursework.ui.viewmodel.HomeViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    HomeViewModel viewModel;
    Toolbar toolbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding = FragmentMainBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        toolbar = binding.toolbar;
        toolbar.setTitle("Основное меню");

        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_home_fragment);
        NavController navController = navHostFragment.getNavController();
        NavController mainNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        viewModel.isCreator.observe(getViewLifecycleOwner(), isCreator -> {
            Menu menu = navigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.nav_admin_menu);
            Set<Integer> menuItems = new HashSet<>();
            menuItems.add(R.id.nav_products);
            menuItems.add(R.id.nav_cooking);
            menuItems.add(R.id.nav_sales);
            menuItems.add(R.id.nav_report);
            menuItems.add(R.id.nav_settings);
            if (isCreator) {
                menuItems.add(R.id.nav_admin_menu);
                if (menuItem != null) {
                    menuItem.setVisible(true);
                }
            } else {
                if (menuItem != null) {
                    menuItem.setVisible(false);
                }
            }

            AppBarConfiguration mAppBarConfiguration = new AppBarConfiguration.Builder(menuItems)
                    .setOpenableLayout(drawerLayout).build();
            NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        String type = sharedPreferences.getString("sync_frequency", "0");
        switch (type){
            case "0":
                navController.navigate(R.id.nav_products);
                break;
            case "1":
                navController.navigate(R.id.nav_cooking);
                break;
            case "2":
                navController.navigate(R.id.nav_sales);
                break;
            case "3":
                navController.navigate(R.id.nav_report);
                break;
            case "4":
                navController.navigate(R.id.nav_settings);
                break;
        }

        View headerView = navigationView.getHeaderView(0);
        TextView titleTextView = headerView.findViewById(R.id.name);
        TextView subtitleTextView = headerView.findViewById(R.id.email);
        Button logoutButton = headerView.findViewById(R.id.logout);

        viewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                titleTextView.setText(user.getUsername());
                subtitleTextView.setText(user.getEmail());
            }
        });

        logoutButton.setOnClickListener(v -> {
            viewModel.logout();
            mainNavController.navigate(R.id.loginFragment);
        });

    }
}