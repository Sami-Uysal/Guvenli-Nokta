// Cig.java
package com.guvenlinokta.app.info;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.guvenlinokta.app.R;

public class Cig extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cig);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Çığ Bilgisi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}