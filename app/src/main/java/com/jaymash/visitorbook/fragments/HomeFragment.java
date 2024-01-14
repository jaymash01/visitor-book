package com.jaymash.visitorbook.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jaymash.visitorbook.R;
import com.jaymash.visitorbook.activities.MainActivity;
import com.jaymash.visitorbook.data.AppDatabase;
import com.jaymash.visitorbook.utils.DateUtils;
import com.jaymash.visitorbook.utils.NumberUtils;

import java.util.Date;

public class HomeFragment extends Fragment {

    private Activity activity;
    private Context context;

    private TextView txtToday, txtThisWeek, txtThisMonth;

    public HomeFragment() {
        // required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        context = getContext();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
    }

    private void setUpViews(View view) {
        txtToday = (TextView) view.findViewById(R.id.txt_today);
        txtThisWeek = (TextView) view.findViewById(R.id.txt_this_week);
        txtThisMonth = (TextView) view.findViewById(R.id.txt_this_month);

        setOnClickListeners(view);
        loadData();
    }

    public void loadData() {
        String currentDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        String currentWeekDate = DateUtils.formatDate(DateUtils.getStartOfCurrentWeekDate(), "yyyy-MM-dd");
        String currentMonthDate = DateUtils.formatDate(DateUtils.getStartOfCurrentMonthDate(), "yyyy-MM-dd");
        AppDatabase database = AppDatabase.getInstance(context);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int todayTotal = database.visitorDao().countWhereVisitDateFrom(currentDate);
                int thisWeekTotal = database.visitorDao().countWhereVisitDateFrom(currentWeekDate);
                int thisMonthTotal = database.visitorDao().countWhereVisitDateFrom(currentMonthDate);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtToday.setText(NumberUtils.numberFormat(todayTotal));
                        txtThisWeek.setText(NumberUtils.numberFormat(thisWeekTotal));
                        txtThisMonth.setText(NumberUtils.numberFormat(thisMonthTotal));
                    }
                });
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void setOnClickListeners(View view) {
        view.findViewById(R.id.btn_navigation_create_visitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) activity).goToCreateVisitor();
            }
        });
    }
}