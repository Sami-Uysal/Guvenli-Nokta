package com.guvenlinokta.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        showDisasterAlert();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng nokta1 = new LatLng(41.015137, 28.979530);
        LatLng nokta2 = new LatLng(39.920770, 32.854110);

        mMap.addMarker(new MarkerOptions().position(nokta1).title("Toplanma Alanı - İstanbul"));
        mMap.addMarker(new MarkerOptions().position(nokta2).title("Toplanma Alanı - Ankara"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nokta1, 10f));
    }

    private void showDisasterAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Acil Durum Uyarısı")
                .setMessage("Bölgenizde doğal afet bildirimi alındı. Lütfen en yakın güvenli toplanma alanına gidin.")
                .setPositiveButton("Tamam", (dialog, which) -> {
                    Toast.makeText(this, "Lütfen güvenliğinizi sağlayın!", Toast.LENGTH_LONG).show();
                })
                .setCancelable(false)
                .show();
    }
}
