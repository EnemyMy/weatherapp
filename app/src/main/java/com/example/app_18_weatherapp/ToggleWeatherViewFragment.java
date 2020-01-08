package com.example.app_18_weatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ToggleWeatherViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ToggleWeatherViewFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ExecutorService dateService = Executors.newSingleThreadExecutor();
    private Future dateServiceTask;
    private Fragment currentFragment;

    public ToggleWeatherViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_toggle_weather_view, container, false);
    }

    private void onWeatherTypeChanged(String fragmentTag) {
        if (mListener != null) {
            mListener.updateFragmentLiveData(fragmentTag);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        View root = getView();
        final BottomNavigationView bottomNavigation = root.findViewById(R.id.toggle_weather_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                currentFragment = new CurrentWeatherFragment();
                String fragmentTag = "";
                switch (menuItem.getItemId()){
                    case R.id.bottom_navigation_item_current:
                        fragmentTag = "current_weather_fragment";
                        break;
                    case R.id.bottom_navigation_item_hourly:
                        currentFragment = new HourlyWeatherFragment();
                        fragmentTag = "hourly_weather_fragment";
                }
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.toggle_weather_fragment_holder, currentFragment, fragmentTag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commitNow();
                if (fragmentTag.equals("current_weather_fragment"))
                    submitDateServiceTask();
                onWeatherTypeChanged(fragmentTag);
                return true;
            }
        });
        currentFragment = new CurrentWeatherFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.toggle_weather_fragment_holder, currentFragment, "current_weather_fragment")
                .commitNow();
        onWeatherTypeChanged("current_weather_fragment");
        submitDateServiceTask();
    }

    private void submitDateServiceTask() {
        if (dateServiceTask != null)
            dateServiceTask.cancel(true);
        dateServiceTask = dateService.submit(new Runnable() {
            @Override
            public void run() {
                final CurrentWeatherFragment currentWeatherFragment = (CurrentWeatherFragment) currentFragment;
                try {
                    while(true) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                currentWeatherFragment.dateView.setText(new SimpleDateFormat("E.dd/MM/yyyy", Locale.US).format(new Date()));
                                currentWeatherFragment.timeView.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));
                            }
                        });
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    Log.i("InterruptedException", "run: " + e.getMessage());
                }
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void updateFragmentLiveData(String fragmentTag);
    }
}
