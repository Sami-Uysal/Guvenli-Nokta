package com.guvenlinokta.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.net.Uri;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.guvenlinokta.app.ilkyardim.FirstAidActivity;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Map<String, Map<String, List<String>>> sehirdenIlcedenMahalleye = new HashMap<>();
    private boolean isSehirSpinnerInitialized = false;
    private boolean isIlceSpinnerInitialized = false;
    private boolean isMahalleSpinnerInitialized = false;
    private FloatingActionButton fabAddPin;
    private boolean pinleriYukleBekle = false;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    private boolean tekPinYukleBekle = false;
    private String bekleyenTekPinAdi;
    private double bekleyenTekPinLat;
    private double bekleyenTekPinLng;
    private boolean isPinCameraActionTaken = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();

        fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        FloatingActionButton fabMenu = findViewById(R.id.fabMenu);
        fabMenu.setOnClickListener(v ->{
            NavigationView navigationView = findViewById(R.id.navigation_view);
            Menu menu = navigationView.getMenu();
            MenuItem profileItem = menu.findItem(R.id.nav_profile);

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                profileItem.setTitle("Profil");
            } else {
                profileItem.setTitle("Giriş Yap");
            }
            drawerLayout.openDrawer(GravityCompat.START);
        });

        sehirIlceMahalleYapisiYukle();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_toplanma_alani) {
                showToplanmaAlaniDialog();
            }else if (id == R.id.nav_profile) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(this, ProfilActivity.class));
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
            } else if (id == R.id.nav_info) {
                startActivity(new Intent(this, InfoActivity.class));
            } else if (id == R.id.nav_first_aid) {
                startActivity(new Intent(this, FirstAidActivity.class));
            } else if (id == R.id.nav_emergency) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:112"));
                startActivity(intent);
            }else if (id == R.id.nav_siren) {
                startActivity(new Intent(this, SirenActivity.class));
            }
            drawerLayout.closeDrawers();
            return true;
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

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
    private void checkAndRequestPermissions() {
        List<String> permissionsNeeded = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (android.os.Build.VERSION.SDK_INT >= 33 &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(android.Manifest.permission.POST_NOTIFICATIONS);
        }

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsNeeded.toArray(new String[0]),
                    100
            );
        }
    }
    private void showToplanmaAlaniDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_toplanma_alani, null);

        isSehirSpinnerInitialized = false;
        isIlceSpinnerInitialized = false;
        isMahalleSpinnerInitialized = false;

        Spinner sehirSpinner = dialogView.findViewById(R.id.sehirSpinner);
        Spinner ilceSpinner = dialogView.findViewById(R.id.ilceSpinner);
        Spinner mahalleSpinner = dialogView.findViewById(R.id.mahalleSpinner);

        List<String> sehirler = new ArrayList<>(sehirdenIlcedenMahalleye.keySet());
        Collections.sort(sehirler);
        sehirler.add(0, "Lütfen bir şehir seçiniz");
        ArrayAdapter<String> sehirAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sehirler);
        sehirSpinner.setAdapter(sehirAdapter);

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        sehirSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSehirSpinnerInitialized) {
                    isSehirSpinnerInitialized = true;
                    return;
                }

                String seciliSehir = sehirSpinner.getSelectedItem().toString();
                Map<String, List<String>> ilceMap = sehirdenIlcedenMahalleye.get(seciliSehir);
                List<String> ilceler = new ArrayList<>(ilceMap.keySet());
                Collections.sort(ilceler);
                ilceler.add(0, "Lütfen bir ilçe seçiniz");
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

                String seciliSehir = sehirSpinner.getSelectedItem().toString();
                String seciliIlce = ilceSpinner.getSelectedItem().toString();
                List<String> mahalleler = sehirdenIlcedenMahalleye.get(seciliSehir).get(seciliIlce);
                Collections.sort(mahalleler);
                mahalleler.add(0, "Lütfen bir mahalle seçiniz");
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

                String seciliSehir = sehirSpinner.getSelectedItem().toString();
                String seciliIlce = ilceSpinner.getSelectedItem().toString();
                String seciliMahalle = mahalleSpinner.getSelectedItem().toString();
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
        Intent intent = getIntent();

        if (intent.getBooleanExtra("tekPinYukle", false)) {
            String pinAd = intent.getStringExtra("pinAd");
            double pinLat = intent.getDoubleExtra("pinLat", 0);
            double pinLng = intent.getDoubleExtra("pinLng", 0);

            intent.removeExtra("tekPinYukle");
            intent.removeExtra("pinAd");
            intent.removeExtra("pinLat");
            intent.removeExtra("pinLng");

            if (mMap == null) {
                tekPinYukleBekle = true;
                bekleyenTekPinAdi = pinAd;
                bekleyenTekPinLat = pinLat;
                bekleyenTekPinLng = pinLng;
            } else {
                haritadaTekPinGoster(pinAd, pinLat, pinLng);
            }
        } else if (intent.getBooleanExtra("pinleriYukle", false)) {
            intent.removeExtra("pinleriYukle");
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
        boolean isPinCameraActionTaken = false;

        if (tekPinYukleBekle) {
            haritadaTekPinGoster(bekleyenTekPinAdi, bekleyenTekPinLat, bekleyenTekPinLng);
            tekPinYukleBekle = false;
            bekleyenTekPinAdi = null;
            bekleyenTekPinLat = 0.0;
            bekleyenTekPinLng = 0.0;
            isPinCameraActionTaken = true;
        } else if (pinleriYukleBekle) {
            kullaniciPinleriniYukleVeGoster();
            pinleriYukleBekle = false;
            isPinCameraActionTaken = true;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            if (!isPinCameraActionTaken) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null && mMap != null) {
                        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 17f));
                    }
                });
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                    yaklasilamayanKonum();
                }
            }
        } else if (requestCode == 100) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                Toast.makeText(this, "Tüm izinler verildi, uygulama tam işlevsel çalışabilir", Toast.LENGTH_SHORT).show();

                if (mMap != null && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    yaklasilamayanKonum();
                }
            } else {
                Toast.makeText(this, "Bazı izinler reddedildi, uygulamanın bazı özellikleri çalışmayabilir", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void yaklasilamayanKonum() {
        if (!isPinCameraActionTaken && mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 17f));
                    } else {
                        Toast.makeText(MainActivity.this, "Konum bulunamadı. Konum servislerinizin açık olduğundan emin olun.", Toast.LENGTH_LONG).show();
                        LatLng defaultLocation = new LatLng(39.9334, 32.8597);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 6f));
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Konum alınamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    LatLng defaultLocation = new LatLng(39.9334, 32.8597);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 6f));
                });
            }
        }
    }
    private void kullaniciPinleriniYukleVeGoster() {
        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        if (mevcutKullanici == null) {
            Toast.makeText(this, "Pinleri görmek için giriş yapmalısınız.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mMap == null) {
            Toast.makeText(this, "Harita henüz hazır değil.", Toast.LENGTH_SHORT).show();
            pinleriYukleBekle = true;
            return;
        }

        mMap.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(mevcutKullanici.getUid()).collection("pins")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() == null || task.getResult().isEmpty()) {
                            Toast.makeText(this, "Kayıtlı pin bulunamadı.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        int pinSayisi = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Double lat = document.getDouble("lat");
                            Double lng = document.getDouble("lng");
                            String ad = document.getString("ad");

                            if (lat != null && lng != null) {
                                LatLng konum = new LatLng(lat, lng);
                                mMap.addMarker(new MarkerOptions().position(konum).title(ad != null ? ad : "Kaydedilmiş Pin"));
                                builder.include(konum);
                                pinSayisi++;
                            }
                        }
                        if (pinSayisi > 0) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
                            Toast.makeText(this, pinSayisi + " pin başarıyla yüklendi.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Geçerli koordinatlara sahip pin bulunamadı.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Pinler getirilirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void haritadaTekPinGoster(String ad, double lat, double lng) {
        if (mMap == null) {
            Toast.makeText(this, "Harita henüz hazır değil.", Toast.LENGTH_SHORT).show();
            tekPinYukleBekle = true;
            bekleyenTekPinAdi = ad;
            bekleyenTekPinLat = lat;
            bekleyenTekPinLng = lng;
            return;
        }
        mMap.clear();
        LatLng pinKonumu = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(pinKonumu).title(ad));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pinKonumu, 15f));
        Toast.makeText(this, "'" + ad + "' haritada gösteriliyor.", Toast.LENGTH_SHORT).show();
        isPinCameraActionTaken = true;
    }

}