// Hortum.java
package com.guvenlinokta.app.info;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.guvenlinokta.app.R;

public class Hortum extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hortum);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hortum Bilgisi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}