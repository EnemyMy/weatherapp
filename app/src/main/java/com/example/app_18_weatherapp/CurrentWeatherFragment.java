package com.example.app_18_weatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CurrentWeatherFragment extends Fragment {

    TextView dateView;
    TextView timeView;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_current_weather, container, false);
        dateView = root.findViewById(R.id.current_weather_fragment_date);
        timeView = root.findViewById(R.id.current_weather_fragment_time);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        final TextView city = getView().findViewById(R.id.current_weather_fragment_current_city);
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 10)
                    city.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                else
                    city.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
