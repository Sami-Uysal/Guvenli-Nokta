package com.guvenlinokta.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EmergencyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmergencyNumbersAdapter adapter;
    private List<EmergencyNumber> emergencyNumberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Acil Numaralar");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerViewEmergencyNumbers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        emergencyNumberList = new ArrayList<>();

        emergencyNumberList.add(new EmergencyNumber("Ambulans", "112"));
        emergencyNumberList.add(new EmergencyNumber("Polis İmdat", "155"));
        emergencyNumberList.add(new EmergencyNumber("İtfaiye", "110"));
        emergencyNumberList.add(new EmergencyNumber("Jandarma İmdat", "156"));
        emergencyNumberList.add(new EmergencyNumber("AFAD", "122"));
        emergencyNumberList.add(new EmergencyNumber("Sahil Güvenlik", "158"));
        emergencyNumberList.add(new EmergencyNumber("Orman Yangını İhbar", "177"));

        adapter = new EmergencyNumbersAdapter(this, emergencyNumberList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}