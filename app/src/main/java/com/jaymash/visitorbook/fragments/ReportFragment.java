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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jaymash.visitorbook.R;
import com.jaymash.visitorbook.adapters.VisitorsAdapter;
import com.jaymash.visitorbook.data.AppDatabase;
import com.jaymash.visitorbook.data.Visitor;
import com.jaymash.visitorbook.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportFragment extends Fragment {

    private Activity activity;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private VisitorsAdapter adapter;

    private final int pageSize = 15;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String startDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");

    public ReportFragment() {
        // required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        context = getContext();
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
    }

    private void setUpViews(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(activity);
        adapter = new VisitorsAdapter(activity, context, new ArrayList<>());

        adapter.add(new Visitor());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        setTabsListener(view);
        setSwipeRefreshListener();
        setOnScrollListener();
    }

    public void loadData(int offset) {
        isLoading = true;
        AppDatabase database = AppDatabase.getInstance(activity);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<Visitor> visitors = database.visitorDao().getWhereVisitDateFrom(startDate, pageSize, offset);
                isLoading = false;

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.hideFooter();
                        adapter.addAll(visitors);

                        if (visitors.size() >= pageSize) {
                            adapter.showFooterProgressBar();
                        } else {
                            isLastPage = true;

                            if (adapter.getItemCount() - 1 >= pageSize) {
                                adapter.showFooterMessage(R.string.end_of_results);
                            }
                        }
                    }
                });
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void refresh() {
        isLoading = false;
        isLastPage = false;
        adapter = new VisitorsAdapter(activity, context, new ArrayList<>());
        adapter.add(new Visitor());
        recyclerView.setAdapter(adapter);
        loadData(0);
    }

    private void setTabsListener(View view) {
        TextView txtToday = (TextView) view.findViewById(R.id.txt_today);
        TextView txtTodayActive = (TextView) view.findViewById(R.id.txt_today_active);
        TextView txtThisWeek = (TextView) view.findViewById(R.id.txt_this_week);
        TextView txtThisWeekActive = (TextView) view.findViewById(R.id.txt_this_week_active);
        TextView txtThisMonth = (TextView) view.findViewById(R.id.txt_this_month);
        TextView txtThisMonthActive = (TextView) view.findViewById(R.id.txt_this_month_active);

        txtToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtToday.setVisibility(View.GONE);
                txtTodayActive.setVisibility(View.VISIBLE);
                txtThisWeek.setVisibility(View.VISIBLE);
                txtThisWeekActive.setVisibility(View.GONE);
                txtThisMonth.setVisibility(View.VISIBLE);
                txtThisMonthActive.setVisibility(View.GONE);

                startDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
                refresh();
            }
        });

        txtThisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtThisWeek.setVisibility(View.GONE);
                txtThisWeekActive.setVisibility(View.VISIBLE);
                txtToday.setVisibility(View.VISIBLE);
                txtTodayActive.setVisibility(View.GONE);
                txtThisMonth.setVisibility(View.VISIBLE);
                txtThisMonthActive.setVisibility(View.GONE);

                startDate = DateUtils.formatDate(DateUtils.getStartOfCurrentWeekDate(), "yyyy-MM-dd");
                refresh();
            }
        });

        txtThisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtThisMonth.setVisibility(View.GONE);
                txtThisMonthActive.setVisibility(View.VISIBLE);
                txtToday.setVisibility(View.VISIBLE);
                txtTodayActive.setVisibility(View.GONE);
                txtThisWeek.setVisibility(View.VISIBLE);
                txtThisWeekActive.setVisibility(View.GONE);

                startDate = DateUtils.formatDate(DateUtils.getStartOfCurrentMonthDate(), "yyyy-MM-dd");
                refresh();
            }
        });

        txtToday.callOnClick();
    }

    private void setSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setOnScrollListener() {
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && (totalItemCount - 1) >= pageSize) {
                        loadData(totalItemCount - 1);
                    }
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }
}