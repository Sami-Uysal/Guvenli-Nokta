package com.guvenlinokta.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Spinner sehirSpinner, ilceSpinner, mahalleSpinner;
    private Map<String, Map<String, List<String>>> sehirdenIlcedenMahalleye = new HashMap<>();
    private boolean isSehirSpinnerInitialized = false;
    private boolean isIlceSpinnerInitialized = false;
    private boolean isMahalleSpinnerInitialized = false;
    private FloatingActionButton fabAddPin;
    private boolean pinleriYukleBekle = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tvToplanmaAlanSec).setOnClickListener(v -> {

            isSehirSpinnerInitialized = false;
            isIlceSpinnerInitialized = false;
            isMahalleSpinnerInitialized = false;

            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottomsheet_toplanma_alani, null);
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setContentView(bottomSheetView);

            sehirSpinner = bottomSheetView.findViewById(R.id.sehirSpinner);
            ilceSpinner = bottomSheetView.findViewById(R.id.ilceSpinner);
            mahalleSpinner = bottomSheetView.findViewById(R.id.mahalleSpinner);

            sehirIlceMahalleYapisiYukle();

            List<String> sehirler = new ArrayList<>(sehirdenIlcedenMahalleye.keySet());
            Collections.sort(sehirler);
            sehirler.add(0, "Lütfen bir şehir seçiniz");
            ArrayAdapter<String> sehirAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sehirler);
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
                    Collections.sort(ilceler);
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
                    Collections.sort(mahalleler);
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
                        dialog.dismiss();
                    }
                }

                @Override public void onNothingSelected(AdapterView<?> parent) {}
            });

            dialog.show();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabAddPin = findViewById(R.id.fabAddPin);
        fabAddPin.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                View bottomSheetView = getLayoutInflater().inflate(R.layout.bottomsheet_pin_ekleme, null);
                BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(bottomSheetView);

                TextInputEditText etName = bottomSheetView.findViewById(R.id.etName);
                MaterialButton btnAdd = bottomSheetView.findViewById(R.id.btnAdd);

                btnAdd.setOnClickListener(view -> {
                    String ad = etName.getText().toString().trim();
                    if (ad.isEmpty()) {
                        etName.setError("Alan adı zorunlu");
                        return;
                    }
                    Toast.makeText(this, "Lütfen haritada bir nokta seçin.", Toast.LENGTH_SHORT).show();
                    mMap.setOnMapClickListener(latLng -> {
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(ad));
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Map<String, Object> pin = new HashMap<>();
                        pin.put("lat", latLng.latitude);
                        pin.put("lng", latLng.longitude);
                        pin.put("ad", ad);

                        com.google.firebase.firestore.FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(userId)
                                .collection("pins")
                                .add(pin)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(this, "Pin kaydedildi!", Toast.LENGTH_SHORT).show();
                                    mMap.setOnMapClickListener(null);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    });
                    dialog.dismiss();
                });
                dialog.show();
            } else {
                Toast.makeText(this, "Pin eklemek için oturum açın.", Toast.LENGTH_SHORT).show();
            }
        });

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
    protected void onResume() {
        super.onResume();
        if (getIntent().getBooleanExtra("pinleriYukle", false)) {
            getIntent().removeExtra("pinleriYukle");
            if (mMap == null) {
                pinleriYukleBekle = true;
            } else {
                kullaniciPinleriniYukleVeGoster();
            }
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng baslangıcNoktası = new LatLng(41.0165, 28.9640);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(baslangıcNoktası, 15f));
        if (pinleriYukleBekle) {
            kullaniciPinleriniYukleVeGoster();
            pinleriYukleBekle = false;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(this, ProfilActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            return true;
        }else if (id == R.id.action_info) {
            Intent infoIntent = new Intent(MainActivity.this, InfoActivity.class);
            startActivity(infoIntent);
            return true;
        } else if (id == R.id.action_first_aid) {
            Intent firstAidIntent = new Intent(MainActivity.this, FirstAidActivity.class);
            startActivity(firstAidIntent);
            return true;
        } else if (id == R.id.action_emergency) {
            Intent emergencyIntent = new Intent(MainActivity.this, EmergencyActivity.class);
            startActivity(emergencyIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void kullaniciPinleriniYukleVeGoster() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "Kullanıcı oturumu yok.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mMap == null) {
            Toast.makeText(this, "Harita hazır değil.", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        com.google.firebase.firestore.FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("pins")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mMap.clear();
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "Kayıtlı pin bulunamadı.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Pinler başarıyla yüklendi.", Toast.LENGTH_SHORT).show();
                    }
                    for (com.google.firebase.firestore.DocumentSnapshot doc : queryDocumentSnapshots) {
                        Double lat = doc.getDouble("lat");
                        Double lng = doc.getDouble("lng");
                        String ad = doc.getString("ad");
                        if (lat != null && lng != null) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(ad != null ? ad : "Kaydedilmiş Pin"));
                        }
                        else {
                            Toast.makeText(this, "Geçersiz pin verisi: " + doc.getId(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Firestore hata: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

}