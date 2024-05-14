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
import android.widget.AdapterView;
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
        saleDateTime = new Pair<>(0L, 0L);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddingSaleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SalesViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_home_fragment);


        binding.editTextTimeInputText.setOnClickListener(v -> selectTime((hour, minute) -> {
            saleDateTime = new Pair<>(saleDateTime.first, (hour * 60L + minute) * 60 * 1000L);
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
            binding.editTextTimeInputText.setText(formattedTime);
        }));

        binding.editTextDateInputText.setOnClickListener(v -> selectDate(date -> {
            saleDateTime = new Pair<>(date, saleDateTime.second);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            String formattedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.getTime());
            binding.editTextDateInputText.setText(formattedDate);
        }));


        binding.saveButton.setOnClickListener(v -> {
//            String name = binding.choosingProductSpinner.getPrompt().toString();
//            String date = binding.editTextDateInputText.getText().toString();
//            String time = binding.editTextTimeInputText.getText().toString();
//            int count = Integer.parseInt(binding.editTextCountInputText.getText().toString());
//
//            viewModel.addProduction(1, count, saleDateTime.first + saleDateTime.second, endDateTime.first + endDateTime.second);
            navController.popBackStack();
        });

        List<String> products = new ArrayList<>();
        products.add("Хлеб");
        products.add("Торт");
        products.add("Печенье");
        products.add("Круассан");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, products);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.choosingProductSpinner.setAdapter(adapter);

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
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .build();

        datePicker.addOnPositiveButtonClickListener(listener::onDateSelected);
        datePicker.show(getParentFragmentManager(), "datePicker");
    }
}