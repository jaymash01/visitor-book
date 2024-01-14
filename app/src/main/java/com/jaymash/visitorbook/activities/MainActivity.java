package com.jaymash.visitorbook.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.jaymash.visitorbook.R;
import com.jaymash.visitorbook.fragments.HomeFragment;
import com.jaymash.visitorbook.fragments.ReportFragment;
import com.jaymash.visitorbook.fragments.VisitorsFragment;

public class MainActivity extends BaseActivity {

    private static int activeNavigationItem = R.id.navigation_home;
    private BottomNavigationView navigationView;
    private ActivityResultLauncher<Intent> createVisitorActivityResultLauncher;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);

        createVisitorActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            String message = data.getStringExtra("message");
                            alert(message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    closeAlert();
                                    Fragment fragment = getCurrentFragment();

                                    if (fragment instanceof VisitorsFragment) {
                                        ((VisitorsFragment) fragment).refresh();
                                    } else {
                                        navigationView.setSelectedItemId(R.id.navigation_visitors);
                                    }
                                }
                            });
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
    }

    private void setUpViews() {
        navigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                int itemId = item.getItemId();
                activeNavigationItem = itemId;
                displayFragment(itemId, null);
                return true;
            }
        });

        navigationView.setSelectedItemId(activeNavigationItem);
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
            fragment = new ReportFragment();
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

    public void goToCreateVisitor() {
        Intent intent = new Intent(this, CreateVisitorActivity.class);
        createVisitorActivityResultLauncher.launch(intent);
    }
}
