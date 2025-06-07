package com.guvenlinokta.app.ilkyardim;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.guvenlinokta.app.R;

public class FirstAidDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TOPIC_TITLE = "com.guvenlinokta.app.ilkyardim.EXTRA_TOPIC_TITLE";
    public static final String EXTRA_TOPIC_DESCRIPTION = "com.guvenlinokta.app.ilkyardim.EXTRA_TOPIC_DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_detail);

        TextView textViewDetailTitle = findViewById(R.id.textViewDetailTitle);
        TextView textViewDetailDescription = findViewById(R.id.textViewDetailDescription);

        String title = getIntent().getStringExtra(EXTRA_TOPIC_TITLE);
        String description = getIntent().getStringExtra(EXTRA_TOPIC_DESCRIPTION);

        if (title != null) {
            textViewDetailTitle.setText(title);
        }
        if (description != null) {
            textViewDetailDescription.setText(description);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (title != null) {
                getSupportActionBar().setTitle(title);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}