package com.guvenlinokta.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Spinner sehirSpinner, ilceSpinner, mahalleSpinner;
    private Map<String, Map<String, List<String>>> sehirdenIlcedenMahalleye = new HashMap<>();
    private boolean isSehirSpinnerInitialized = false;
    private boolean isIlceSpinnerInitialized = false;
    private boolean isMahalleSpinnerInitialized = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sehirSpinner = findViewById(R.id.sehirSpinner);
        ilceSpinner = findViewById(R.id.ilceSpinner);
        mahalleSpinner = findViewById(R.id.mahalleSpinner);

        sehirSpinner.setEnabled(false);
        ilceSpinner.setEnabled(false);
        mahalleSpinner.setEnabled(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        sehirIlceMahalleYapisiYukle();

        ArrayAdapter<String> sehirAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(sehirdenIlcedenMahalleye.keySet()));
        sehirSpinner.setAdapter(sehirAdapter);

        sehirSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSehirSpinnerInitialized) {
                    isSehirSpinnerInitialized = true;
                    return;
                }

                String seciliSehir = (String) sehirSpinner.getSelectedItem();
                Map<String, List<String>> ilceMap = sehirdenIlcedenMahalleye.get(seciliSehir);
                List<String> ilceler = new ArrayList<>(ilceMap.keySet());
                ArrayAdapter<String> ilceAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, ilceler);
                ilceSpinner.setAdapter(ilceAdapter);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });


        ilceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isIlceSpinnerInitialized) {
                    isIlceSpinnerInitialized = true;
                    return;
                }

                String seciliSehir = (String) sehirSpinner.getSelectedItem();
                String seciliIlce = (String) ilceSpinner.getSelectedItem();
                List<String> mahalleler = sehirdenIlcedenMahalleye.get(seciliSehir).get(seciliIlce);
                ArrayAdapter<String> mahalleAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, mahalleler);
                mahalleSpinner.setAdapter(mahalleAdapter);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });


        mahalleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isMahalleSpinnerInitialized) {
                    isMahalleSpinnerInitialized = true;
                    return;
                }

                String seciliSehir = (String) sehirSpinner.getSelectedItem();
                String seciliIlce = (String) ilceSpinner.getSelectedItem();
                String seciliMahalle = (String) mahalleSpinner.getSelectedItem();
                if (seciliSehir != null && seciliIlce != null && seciliMahalle != null) {
                    mMap.clear();
                    String path = "pins/" + seciliSehir + "/" + seciliIlce + "/" + seciliMahalle + "/veri.csv";
                    LatLng ilkPinYeri = CSVdenPinYukle(path);

                    if (ilkPinYeri != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ilkPinYeri, 15f));
                    } else {
                        Toast.makeText(MainActivity.this, seciliMahalle + " için gösterilecek konum bulunamadı.", Toast.LENGTH_SHORT).show();
                        LatLng defaultLocation = new LatLng(41.0165, 28.9640);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f));
                    }
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    private void sehirIlceMahalleYapisiYukle() {
        try {
            InputStream is = getAssets().open("tr.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String satir;
            boolean ilkSatirMi = true;

            while ((satir = reader.readLine()) != null) {
                if (ilkSatirMi) {
                    ilkSatirMi = false;
                    continue;
                }

                String[] parts = satir.split(",", -1);
                if (parts.length >= 3) {
                    String sehir = parts[0].trim();
                    String ilce = parts[1].trim();
                    String mahalle = parts[2].trim();

                    sehirdenIlcedenMahalleye.putIfAbsent(sehir, new HashMap<>());
                    Map<String, List<String>> ilceMap = sehirdenIlcedenMahalleye.get(sehir);

                    ilceMap.putIfAbsent(ilce, new ArrayList<>());
                    List<String> mahalleListe = ilceMap.get(ilce);

                    if (!mahalleListe.contains(mahalle)) {
                        mahalleListe.add(mahalle);
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "tr.csv okunamadı", Toast.LENGTH_SHORT).show();
        }
    }
    private LatLng CSVdenPinYukle(String assetYolu) {
        if (mMap == null) {
            Toast.makeText(this, "Harita henüz yüklenmedi", Toast.LENGTH_SHORT).show();
            return null;
        }
        LatLng ilkPinYeri = null;
        try {
            InputStream is = getAssets().open(assetYolu);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String satir;
            boolean ilkSatir = true;

            while ((satir = reader.readLine()) != null) {
                if (ilkSatir) {
                    ilkSatir = false;
                    continue;
                }

                String[] parts = satir.split(",", -1);
                if (parts.length >= 6) {
                    String title = parts[3];
                    double lat = Double.parseDouble(parts[5]);
                    double lng = Double.parseDouble(parts[4]);

                    LatLng location = new LatLng(lat, lng);
                    if (ilkPinYeri == null) {
                        ilkPinYeri = location;
                    }
                    mMap.addMarker(new MarkerOptions().position(location).title(title));
                }
            }

            reader.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "veri.csv okunamadı: " + assetYolu, Toast.LENGTH_SHORT).show();
        }
        return ilkPinYeri;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng baslangıcNoktası = new LatLng(41.0165, 28.9640);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(baslangıcNoktası, 15f));
        sehirSpinner.setEnabled(true);
        ilceSpinner.setEnabled(true);
        mahalleSpinner.setEnabled(true);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info) {
            Toast.makeText(this, "Bilgilendirme sayfası açılacak", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_first_aid) {
            Toast.makeText(this, "İlk Yardım sayfası açılacak", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_emergency) {
            Toast.makeText(this, "Acil Numaralar sayfası açılacak", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}