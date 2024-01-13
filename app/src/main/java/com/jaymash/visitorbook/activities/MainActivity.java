package com.jaymash.visitorbook.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.jaymash.visitorbook.R;
import com.jaymash.visitorbook.fragments.HomeFragment;
import com.jaymash.visitorbook.fragments.VisitorsFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
    }

    private void setUpViews() {
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                displayFragment(item.getItemId(), null);
                return true;
            }
        });

        navigationView.setSelectedItemId(R.id.navigation_home);
    }

    public void displayFragment(@IdRes int navigationItemId, Bundle args) {
        Fragment fragment = null;
        String tag = null;

        if (navigationItemId == R.id.navigation_home) {
            fragment = new HomeFragment();
            tag = "HomeFragment";
        } else if (navigationItemId == R.id.navigation_visitors) {
            fragment = new VisitorsFragment();
            tag = "VisitorsFragment";
        } else if (navigationItemId == R.id.navigation_report) {
            fragment = new VisitorsFragment();
            tag = "ReportFragment";
        }

        if (fragment != null) {
            if (args != null) {
                fragment.setArguments(args);
            }

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment, tag);
            transaction.commit();
        }
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }
}
