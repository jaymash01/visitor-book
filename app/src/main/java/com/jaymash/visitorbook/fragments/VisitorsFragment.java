package com.jaymash.visitorbook.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jaymash.visitorbook.R;
import com.jaymash.visitorbook.activities.MainActivity;
import com.jaymash.visitorbook.adapters.VisitorsAdapter;
import com.jaymash.visitorbook.data.AppDatabase;
import com.jaymash.visitorbook.data.Visitor;
import com.jaymash.visitorbook.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VisitorsFragment extends Fragment {

    private Activity activity;
    private Context context;
    private TextInputLayout tilSearch;
    private TextInputEditText edtSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private VisitorsAdapter adapter;

    private final int pageSize = 15;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String searchQuery = "";

    public VisitorsFragment() {
        // required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        context = getContext();
        return inflater.inflate(R.layout.fragment_visitors, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
    }

    private void setUpViews(View view) {
        edtSearch = (TextInputEditText) view.findViewById(R.id.edt_search);
        tilSearch = (TextInputLayout) edtSearch.getParent().getParent();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(activity);
        adapter = new VisitorsAdapter(activity, context, new ArrayList<>());

        tilSearch.setEndIconVisible(false);
        adapter.add(new Visitor());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        setOnClickListeners(view);
        setSearchListener();
        setSwipeRefreshListener();
        setOnScrollListener();
        loadData(0);
    }

    public void loadData(int offset) {
        isLoading = true;
        String currentDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
        AppDatabase database = AppDatabase.getInstance(context);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<Visitor> visitors = database.visitorDao()
                        .getCheckedInVisitors("%" + searchQuery + "%", currentDate, pageSize, offset);
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

    public void signOutVisitor(Visitor visitor) {
        AppDatabase database = AppDatabase.getInstance(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String currentTime = DateUtils.formatDate(new Date(), "HH:mm:ss");
                visitor.setTimeOut(currentTime);
                database.visitorDao().update(visitor);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
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

    private void setOnClickListeners(View view) {
        view.findViewById(R.id.btn_navigation_create_visitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) activity).goToCreateVisitor();
            }
        });
    }

    private void setSearchListener() {
        tilSearch.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtSearch.setText("");
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchQuery = editable.toString();
                tilSearch.setEndIconVisible(!searchQuery.isEmpty());
                refresh();
            }
        };

        edtSearch.addTextChangedListener(textWatcher);
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

                int totalItemCount = layoutManager.getItemCount() - 1;
                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();

                if (!isLoading && !isLastPage && lastVisibleItemPosition >= totalItemCount && totalItemCount >= pageSize) {
                    loadData(totalItemCount);
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }
}