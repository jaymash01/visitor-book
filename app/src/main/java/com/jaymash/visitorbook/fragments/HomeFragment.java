package com.jaymash.visitorbook.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jaymash.visitorbook.activities.MainActivity;
import com.jaymash.visitorbook.R;
import com.jaymash.visitorbook.data.AppDatabase;
import com.jaymash.visitorbook.data.Visitor;
import com.jaymash.visitorbook.utils.DateUtils;

import java.util.Date;

public class HomeFragment extends Fragment {

    private Activity activity;
    private Context context;
    private ProgressBar progressBar;

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
        //progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveVisitor();
            }
        });
    }

    private void saveVisitor() {
        MainActivity activity1 = (MainActivity) activity;
        AppDatabase database = AppDatabase.getInstance(activity);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String currentDate = DateUtils.formatDate(new Date());
                Visitor visitor = new Visitor();
                visitor.setName("Liam Cooper");
                visitor.setPhone("0784605179");
                visitor.setWhereFrom("Dar es Salaam");
                visitor.setWhereTo("Office AB13");
                visitor.setHost("Dr. X");
                visitor.setVisitReason("Official");
                visitor.setVisitDate(currentDate.substring(0, 10));
                visitor.setTimeIn(currentDate.substring(11));
                visitor.setTimeOut(null);
                database.visitorDao().insert(visitor);

//                    activity1.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            BottomNavigationView navigationView = activity1.getNavigationView();
//                            navigationView.setSelectedItemId(R.id.action_monitoring);
//                        }
//                    });
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}