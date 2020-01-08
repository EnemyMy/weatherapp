package com.example.app_18_weatherapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolderWeather>{

    private List<HourlyWeatherEntity> list = new ArrayList<>();
    private Context context;

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerHolderWeather onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new RecyclerHolderWeather(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_weather, parent, false));
        return new RecyclerHolderDayAndWeather(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_day_and_weather, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolderWeather holder, int position) {
        if (holder instanceof RecyclerHolderDayAndWeather) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd yyyy", Locale.US);
            try {
                ((RecyclerHolderDayAndWeather) holder).date.setText(dateFormat.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(list.get(position).getDate())));
            } catch (ParseException e) {
                Log.i("ParseException", "onBindViewHolder: " + e.getMessage());
            }
        }
        holder.time.setText(list.get(position).getDate().substring(11, 16));
        holder.clouds.setBackgroundResource(context.getResources().getIdentifier("mipmap/ic_" + list.get(position).getWeatherIcon(), null, context.getPackageName()));
        holder.temperature.setText(new StringBuilder().append(list.get(position).getTemperature()).append("°"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 || list.get(position).getDate().substring(11, 13).equals("00")) ? 1 : 0;
    }

    public void updateRecycler(List<HourlyWeatherEntity> entities) {
        this.list = entities;
        notifyDataSetChanged();
    }

    class RecyclerHolderWeather extends RecyclerView.ViewHolder {
        TextView time;
        ImageView clouds;
        TextView temperature;

        public RecyclerHolderWeather(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.recycler_item_time);
            clouds = itemView.findViewById(R.id.recycler_item_clouds);
            temperature = itemView.findViewById(R.id.recycler_item_temperature);
        }
    }

    class RecyclerHolderDayAndWeather extends RecyclerHolderWeather {
        TextView date;

        public RecyclerHolderDayAndWeather(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.recycler_item_date);
            time = itemView.findViewById(R.id.recycler_item_time);
            clouds = itemView.findViewById(R.id.recycler_item_clouds);
            temperature = itemView.findViewById(R.id.recycler_item_temperature);
        }
    }

}
