package com.guvenlinokta.app.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.guvenlinokta.app.R;

public class BaseActivity extends AppCompatActivity {

    private ImageButton customBackButtonInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        addCustomBackButtonToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCustomBackButtonFromWindow();
    }

    private void addCustomBackButtonToWindow() {
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();

        if (decorView.findViewById(R.id.custom_back_button_id) != null) {
            customBackButtonInstance = decorView.findViewById(R.id.custom_back_button_id);
            customBackButtonInstance.setOnClickListener(v -> onBackPressed());
            customBackButtonInstance.setAlpha(0.7f);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        customBackButtonInstance = (ImageButton) inflater.inflate(R.layout.geri_butonu_view, decorView, false);

        customBackButtonInstance.setAlpha(0.7f);
        customBackButtonInstance.setOnClickListener(v -> onBackPressed());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.TOP | Gravity.START;

        int marginTopDp = 30;
        int marginRightDp = 16;
        params.topMargin = (int) (marginTopDp * getResources().getDisplayMetrics().density);
        params.rightMargin = (int) (marginRightDp * getResources().getDisplayMetrics().density);

        customBackButtonInstance.setLayoutParams(params);
        decorView.addView(customBackButtonInstance);
    }

    private void removeCustomBackButtonFromWindow() {
        if (customBackButtonInstance != null && customBackButtonInstance.getParent() != null) {
            ((ViewGroup) customBackButtonInstance.getParent()).removeView(customBackButtonInstance);
        }
        customBackButtonInstance = null;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected void setActivityTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}