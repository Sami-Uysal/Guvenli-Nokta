package com.guvenlinokta.app.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guvenlinokta.app.R;
import com.guvenlinokta.app.ui.BaseActivity;

import java.util.ArrayList;

public class DetailCardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_card);

        String cardTitle = getIntent().getStringExtra("card_title");

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText(cardTitle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(cardTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout cardsContainer = findViewById(R.id.cardsContainer);

        ArrayList<CardModel> detailCards = (ArrayList<CardModel>)
                getIntent().getSerializableExtra("detail_cards");

        if (detailCards != null && !detailCards.isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(this);

            for (CardModel card : detailCards) {
                View cardView = inflater.inflate(R.layout.item_card_detail, cardsContainer, false);

                TextView titleView = cardView.findViewById(R.id.cardTitle);
                TextView descriptionView = cardView.findViewById(R.id.cardDescription);

                titleView.setText(card.getTitle());
                descriptionView.setText(card.getDescription());

                cardsContainer.addView(cardView);
            }
        }
    }
}