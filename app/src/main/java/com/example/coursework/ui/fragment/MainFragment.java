package com.example.coursework.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.coursework.domain.utils.UserType;
import com.example.coursework.ui.viewmodel.HomeViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;
import java.util.Set;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    HomeViewModel viewModel;
    Toolbar toolbar;
    private boolean isNavigationInitialized = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

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

        Set<Integer> menuItems = new HashSet<>();
        menuItems.add(R.id.nav_products);
        menuItems.add(R.id.nav_cooking);
        menuItems.add(R.id.nav_sales);
        menuItems.add(R.id.nav_report);
        menuItems.add(R.id.nav_admin_menu);
        menuItems.add(R.id.nav_settings);
        menuItems.add(R.id.nav_currency);
        menuItems.add(R.id.nav_info);

        AppBarConfiguration mAppBarConfiguration = new AppBarConfiguration.Builder(menuItems)
                .setOpenableLayout(drawerLayout).build();
        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_admin_menu);
        menuItem.setEnabled(false);

        View headerView = navigationView.getHeaderView(0);
        TextView titleTextView = headerView.findViewById(R.id.name);
        TextView subtitleTextView = headerView.findViewById(R.id.email);
        Button logoutButton = headerView.findViewById(R.id.logout);
        ImageView settingsButton = headerView.findViewById(R.id.settings);

        settingsButton.setOnClickListener(t -> {
            navController.navigate(R.id.nav_account);
            drawerLayout.closeDrawer(navigationView);
        });

        viewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                titleTextView.setText(user.getUsername());
                subtitleTextView.setText(user.getEmail());
                menuItem.setEnabled(user.getRole().equals(UserType.CREATOR));
                menuItem.setVisible(user.getRole().equals(UserType.CREATOR));
            }
        });

        logoutButton.setOnClickListener(v -> viewModel.logout());

        viewModel.toLoginScreen.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                viewModel.resetToLoginScreen();
                mainNavController.navigate(R.id.loginFragment);
            }
        });

        if (!isNavigationInitialized) {
            isNavigationInitialized = true;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            String type = sharedPreferences.getString("sync_frequency", "0");

            int startDestinationId;
            switch (type) {
                case "1":
                    startDestinationId = R.id.nav_cooking;
                    break;
                case "2":
                    startDestinationId = R.id.nav_sales;
                    break;
                case "3":
                    startDestinationId = R.id.nav_report;
                    break;
                case "4":
                    startDestinationId = R.id.nav_settings;
                    break;
                default:
                    startDestinationId = R.id.nav_products;
                    break;
            }
            navController.navigate(startDestinationId);
            navigationView.setCheckedItem(startDestinationId);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();
            if (item.getItemId() != navController.getCurrentDestination().getId()) {
                navController.navigate(item.getItemId());
            }
            return true;
        });
    }
}
