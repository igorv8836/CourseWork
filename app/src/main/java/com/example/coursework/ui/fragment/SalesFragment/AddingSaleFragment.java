package com.example.coursework.ui.fragment.SalesFragment;

import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentAddingSaleBinding;
import com.example.coursework.ui.fragment.CookingFragment.DatePickerListener;
import com.example.coursework.ui.fragment.CookingFragment.TimePickerListener;
import com.example.coursework.ui.viewmodel.SalesViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddingSaleFragment extends Fragment {
    FragmentAddingSaleBinding binding;
    SalesViewModel viewModel;
    NavController navController;
    Pair<Long, Long> saleDateTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saleDateTime = getCurrentDateTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddingSaleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SalesViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_home_fragment);


        Calendar calendar = Calendar.getInstance();
        String nowDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.getTime());
        String nowTime = String.format(Locale.getDefault(), "%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        binding.editTextDateInputText.setText(nowDate);
        binding.editTextTimeInputText.setText(nowTime);

        binding.editTextTimeInputText.setOnClickListener(v -> selectTime((hour, minute) -> {
            saleDateTime = new Pair<>(saleDateTime.first, (hour * 60L + minute) * 60 * 1000L);
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
            binding.editTextTimeInputText.setText(formattedTime);
        }));

        binding.editTextDateInputText.setOnClickListener(v -> selectDate(date -> {
            saleDateTime = new Pair<>(date, saleDateTime.second);
            calendar.setTimeInMillis(date);
            String formattedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.getTime());
            binding.editTextDateInputText.setText(formattedDate);
        }));


        binding.saveButton.setOnClickListener(v -> {
            int selectedProductIndex = binding.choosingProductSpinner.getSelectedItemPosition();
            int count = 0;
            double price = 0.0;
            try{
                count = Integer.parseInt(binding.editTextCountInputText.getText().toString());
                price = Double.parseDouble(binding.editTextPriceInputText.getText().toString());
            } catch (Exception ignored){}

            viewModel.addSale(
                    selectedProductIndex,
                    count,
                    saleDateTime.first + saleDateTime.second,
                    price);
            navController.popBackStack();
        });

        viewModel.products.observe(getViewLifecycleOwner(), products -> {
            List<String> productNames = new ArrayList<>();
            for (int i = 0; i < products.size(); i++){
                productNames.add(products.get(i).getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, productNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.choosingProductSpinner.setAdapter(adapter);
        });

        return binding.getRoot();
    }

    private void selectTime(TimePickerListener listener){
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setTitleText("Выберите время")
                .build();

        timePicker.addOnPositiveButtonClickListener(t -> {
            listener.onTimeSelected(timePicker.getHour(), timePicker.getMinute());
        });

        timePicker.show(getParentFragmentManager(), "fragment_tag");
    }

    private void selectDate(DatePickerListener listener){
        Calendar currentDate = Calendar.getInstance();
        long today = currentDate.getTimeInMillis();
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату").setSelection(today)
                .build();

        datePicker.addOnPositiveButtonClickListener(listener::onDateSelected);
        datePicker.show(getParentFragmentManager(), "datePicker");
    }

    private Pair<Long, Long> getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();

        long dateInMillis = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfDayInMillis = calendar.getTimeInMillis();

        long timeInMillis = dateInMillis - startOfDayInMillis;

        return new Pair<>(startOfDayInMillis, timeInMillis);
    }
}