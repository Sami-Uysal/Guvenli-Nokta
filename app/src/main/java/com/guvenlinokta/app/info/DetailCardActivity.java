package com.guvenlinokta.app.info;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.guvenlinokta.app.R;
import java.util.ArrayList;

public class DetailCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_card);

        String cardTitle = getIntent().getStringExtra("card_title");
        int startPosition = getIntent().getIntExtra("card_position", 0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(cardTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewPager2 viewPager = findViewById(R.id.viewPager);

        ArrayList<CardModel> detailCards = (ArrayList<CardModel>)
                getIntent().getSerializableExtra("detail_cards");

        if (detailCards != null) {
            CardPagerAdapter adapter = new CardPagerAdapter(detailCards);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(startPosition, false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}