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

                LinearLayout headerLayout = cardView.findViewById(R.id.headerLayout);
                final LinearLayout expandableContent = cardView.findViewById(R.id.expandableContent);
                TextView titleView = cardView.findViewById(R.id.cardTitle);
                TextView descriptionView = cardView.findViewById(R.id.cardDescription);
                final View expandIcon = cardView.findViewById(R.id.expandIcon);

                titleView.setText(card.getTitle());
                descriptionView.setText(card.getDescription());

                headerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (expandableContent.getVisibility() == View.VISIBLE) {
                            expandableContent.setVisibility(View.GONE);
                            if (expandIcon != null) {
                                ((android.widget.ImageView) expandIcon).setImageResource(R.drawable.arrow_down);
                            }
                        } else {
                            expandableContent.setVisibility(View.VISIBLE);
                            if (expandIcon != null) {
                                ((android.widget.ImageView) expandIcon).setImageResource(R.drawable.arrow_up);
                            }
                        }
                    }
                });
                cardsContainer.addView(cardView);
            }
        }
    }
}