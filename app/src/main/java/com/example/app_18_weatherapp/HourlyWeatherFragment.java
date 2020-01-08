package com.example.app_18_weatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HourlyWeatherFragment extends Fragment {

    RecyclerAdapter adapter;

    public HourlyWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hourly_weather, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View root = getView();
        RecyclerView recycler = root.findViewById(R.id.hourly_weather_fragment_recycler);
        final TextView city = root.findViewById(R.id.hourly_weather_fragment_current_city);
        adapter = new RecyclerAdapter(getContext());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recycler.setHasFixedSize(true);
        recycler.setNestedScrollingEnabled(false);
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 10)
                    city.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                else
                    city.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
