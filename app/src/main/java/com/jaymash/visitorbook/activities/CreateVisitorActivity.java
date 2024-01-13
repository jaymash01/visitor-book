package com.jaymash.visitorbook.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jaymash.visitorbook.R;
import com.jaymash.visitorbook.data.AppDatabase;
import com.jaymash.visitorbook.data.Visitor;
import com.jaymash.visitorbook.utils.DateUtils;

import java.util.Date;

public class CreateVisitorActivity extends BaseActivity {

    private TextInputEditText edtName, edtPhone, edtWhereFrom, edtWhereTo, edtHost, edtVisitReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_visitor);
        setUpViews();
    }

    private void setUpViews() {
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.create_visitor);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        edtName = (TextInputEditText) findViewById(R.id.edt_name);
        edtPhone = (TextInputEditText) findViewById(R.id.edt_phone);
        edtWhereFrom = (TextInputEditText) findViewById(R.id.edt_where_from);
        edtWhereTo = (TextInputEditText) findViewById(R.id.edt_where_to);
        edtHost = (TextInputEditText) findViewById(R.id.edt_host);
        edtVisitReason = (TextInputEditText) findViewById(R.id.edt_visit_reason);

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void validate() {
        if (edtName.getText().toString().isEmpty() ||
                edtWhereFrom.getText().toString().isEmpty() ||
                edtWhereTo.getText().toString().isEmpty() ||
                edtVisitReason.getText().toString().isEmpty()) {
            alert(R.string.all_fields_required_message);
        } else {
            saveVisitor();
        }
    }

    private void saveVisitor() {
        AppDatabase database = AppDatabase.getInstance(this);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Visitor visitor = new Visitor();
                visitor.setName(edtName.getText().toString());
                visitor.setPhone(edtPhone.getText().toString());
                visitor.setPhone(edtPhone.getText().toString());
                visitor.setWhereFrom(edtWhereFrom.getText().toString());
                visitor.setWhereTo(edtWhereTo.getText().toString());
                visitor.setHost(edtHost.getText().toString());
                visitor.setVisitReason(edtVisitReason.getText().toString());

                String currentDate = DateUtils.formatDate(new Date());

                visitor.setVisitDate(currentDate.substring(0, 10));
                visitor.setTimeIn(currentDate.substring(11));
                visitor.setTimeOut(null);
                database.visitorDao().insert(visitor);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
