package com.example.coursework.ui.fragment.CookingFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentAddingProductionBinding;
import com.example.coursework.ui.viewmodel.CookingViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddingProductionFragment extends Fragment {
    FragmentAddingProductionBinding binding;
    CookingViewModel viewModel;
    NavController navController;

    Pair<Long, Long> startDateTime;
    Pair<Long, Long> endDateTime;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pair<Long, Long> currentDateTime = getCurrentDateTime();
        startDateTime = currentDateTime;
        endDateTime = currentDateTime;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddingProductionBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(CookingViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_home_fragment);

        Calendar calendar = Calendar.getInstance();
        String nowDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.getTime());
        String nowTime = String.format(Locale.getDefault(), "%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        binding.editTextEndDateInputText.setText(nowDate);
        binding.editTextDateInputText.setText(nowDate);
        binding.editTextTimeInputText.setText(nowTime);
        binding.editTextEndTimeInputText.setText(nowTime);

        binding.editTextTimeInputText.setOnClickListener(v -> selectTime((hour, minute) -> {
            startDateTime = new Pair<>(startDateTime.first, (hour * 60L + minute) * 60 * 1000L);
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
            binding.editTextTimeInputText.setText(formattedTime);
        }));

        binding.editTextDateInputText.setOnClickListener(v -> selectDate(date -> {
            startDateTime = new Pair<>(date, startDateTime.second);
            calendar.setTimeInMillis(date);
            String formattedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.getTime());
            binding.editTextDateInputText.setText(formattedDate);
        }));

        binding.editTextEndTimeInputText.setOnClickListener(v -> selectTime((hour, minute) -> {
            endDateTime = new Pair<>(endDateTime.first, (hour * 60L + minute) * 60 * 1000L);
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
            binding.editTextEndTimeInputText.setText(formattedTime);
        }));

        binding.editTextEndDateInputText.setOnClickListener(v -> selectDate(date -> {
            endDateTime = new Pair<>(date, endDateTime.second);
            calendar.setTimeInMillis(date);
            String formattedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.getTime());
            binding.editTextEndDateInputText.setText(formattedDate);
        }));


        binding.saveButton.setOnClickListener(v -> {
            int selectedProductIndex = binding.choosingProductSpinner.getSelectedItemPosition();
            int count = 0;
            try {
                count = Integer.parseInt(Objects.requireNonNull(binding.editTextCountInputText.getText()).toString());
            } catch (Exception ignored) {
            }

            viewModel.addProduction(selectedProductIndex, count, startDateTime.first + startDateTime.second, endDateTime.first + endDateTime.second);
            navController.popBackStack();
        });

        viewModel.products.observe(getViewLifecycleOwner(), products -> {
            List<String> productNames = new ArrayList<>();
            for (int i = 0; i < products.size(); i++) {
                productNames.add(products.get(i).getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, productNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.choosingProductSpinner.setAdapter(adapter);
        });

        binding.choosingProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return binding.getRoot();
    }

    private void selectTime(TimePickerListener listener) {
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

        timePicker.addOnPositiveButtonClickListener(t -> listener.onTimeSelected(timePicker.getHour(), timePicker.getMinute()));

        timePicker.show(getParentFragmentManager(), "fragment_tag");
    }

    private void selectDate(DatePickerListener listener) {
        Calendar currentDate = Calendar.getInstance();
        long today = currentDate.getTimeInMillis();

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setSelection(today)
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