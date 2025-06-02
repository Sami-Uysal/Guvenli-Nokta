package com.guvenlinokta.app.info;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.guvenlinokta.app.R;

public class Heyelan extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heyelan);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Heyelan Bilgisi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}