package com.jaymash.visitorbook.activities;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.jaymash.visitorbook.R;

abstract public class BaseActivity extends AppCompatActivity {

    private Dialog alertDialog, confirmationDialog;

    public void alert(String message) {
        alertDialog = new Dialog(this);
        alertDialog.setContentView(R.layout.dialog_alert);
        alertDialog.setCancelable(true);

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setWindowAnimations(R.style.DialogAnimation);

        ((TextView) alertDialog.findViewById(R.id.txt_message)).setText(message);
        alertDialog.findViewById(R.id.btn_okay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void alert(String message, View.OnClickListener onOkayButtonClickLister) {
        alertDialog = new Dialog(this);
        alertDialog.setContentView(R.layout.dialog_alert);
        alertDialog.setCancelable(false);

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setWindowAnimations(R.style.DialogAnimation);

        ((TextView) alertDialog.findViewById(R.id.txt_message)).setText(message);
        alertDialog.findViewById(R.id.btn_okay).setOnClickListener(onOkayButtonClickLister);
        alertDialog.show();
    }

    public void alert(@StringRes int message) {
        alert(getString(message));
    }

    public void alert(@StringRes int message, View.OnClickListener onCloseListener) {
        alert(getString(message), onCloseListener);
    }

    public void closeAlert() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void confirm(String title, String message, View.OnClickListener onOkayButtonClickListener) {
        confirmationDialog = new Dialog(this);
        confirmationDialog.setContentView(R.layout.dialog_confirm);
        confirmationDialog.setCancelable(false);

        Window window = confirmationDialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setWindowAnimations(R.style.DialogAnimation);

        ((TextView) confirmationDialog.findViewById(R.id.txt_title)).setText(title);
        ((TextView) confirmationDialog.findViewById(R.id.txt_message)).setText(message);
        confirmationDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog.dismiss();
            }
        });

        confirmationDialog.findViewById(R.id.btn_okay).setOnClickListener(onOkayButtonClickListener);
        confirmationDialog.show();
    }

    public void confirm(@StringRes int title, @StringRes int message,
                        View.OnClickListener onOkayButtonClickListener) {
        confirm(getString(title), getString(message), onOkayButtonClickListener);
    }

    public void closeConfirmation() {
        if (confirmationDialog != null) {
            confirmationDialog.dismiss();
        }
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
