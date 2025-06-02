package com.guvenlinokta.app.info;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.guvenlinokta.app.R;

public class Sel extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sel Bilgisi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}