package com.guvenlinokta.app.profil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.view.LayoutInflater;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.guvenlinokta.app.MainActivity;
import com.guvenlinokta.app.R;

import java.util.ArrayList;
import java.util.List;

public class PinlerimActivity extends AppCompatActivity implements PinAdapter.OnPinClickListener {

    private static final String TAG = "PinlerimActivity";

    private RecyclerView pinlerRecyclerView;
    private PinAdapter pinAdapter;
    private List<Pin> pinListesi;
    private Button tumPinleriYukleButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinlerim);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pinlerim");
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        pinlerRecyclerView = findViewById(R.id.pinlerRecyclerView);
        tumPinleriYukleButton = findViewById(R.id.tumPinleriYukleButton);

        pinListesi = new ArrayList<>();
        pinAdapter = new PinAdapter(pinListesi, this);
        pinlerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pinlerRecyclerView.setAdapter(pinAdapter);

        kullaniciPinleriniGetir();

        tumPinleriYukleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinListesi.isEmpty()) {
                    Toast.makeText(PinlerimActivity.this, "Haritada gösterilecek kayıtlı pin bulunmamaktadır.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(PinlerimActivity.this, MainActivity.class);
                intent.putExtra("pinleriYukle", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void kullaniciPinleriniGetir() {
        db.collection("users").document(currentUser.getUid()).collection("pins")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            pinListesi.clear();
                            if (task.getResult().isEmpty()) {
                                Toast.makeText(PinlerimActivity.this, "Kayıtlı pin bulunamadı.", Toast.LENGTH_SHORT).show();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String ad = document.getString("ad");
                                Double lat = document.getDouble("lat");
                                Double lng = document.getDouble("lng");

                                if (ad != null && lat != null && lng != null) {
                                    pinListesi.add(new Pin(id, ad, lat, lng));
                                } else {
                                    Log.w(TAG, "Eksik veri içeren pin atlandı: " + id + " Ad: " + ad + " Lat: " + lat + " Lng: " + lng);
                                }
                            }
                            pinAdapter.notifyDataSetChanged();
                            if (pinListesi.isEmpty() && !task.getResult().isEmpty()){
                                Toast.makeText(PinlerimActivity.this, "Pinler yüklendi ancak bazıları eksik veri içeriyor olabilir.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.e(TAG, "Pinler getirilirken hata oluştu.", task.getException());
                            Toast.makeText(PinlerimActivity.this, "Pinler getirilirken bir hata oluştu: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPinClick(Pin pin, int position) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(pin.getAd() != null && !pin.getAd().isEmpty() ? pin.getAd() : "Pin Seçenekleri");
        String[] secenekler = {"Haritada Göster", "Düzenle", "Sil"};

        builder.setItems(secenekler, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) { // Haritada Göster
                    tekPiniHaritadaGoster(pin);
                } else if (which == 1) { // Düzenle
                    pinDuzenlemeDialoguGoster(pin, position);
                } else if (which == 2) { // Sil
                    pinSilmeOnayDialoguGoster(pin, position);
                }
            }
        });
        builder.setNegativeButton("İptal", null); // İptal butonu
        builder.show();
    }

    private void pinDuzenlemeDialoguGoster(Pin pin, int position) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Pin Adını Düzenle");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_pin_name, null);
        TextInputLayout pinNameLayout = dialogView.findViewById(R.id.pinNameLayout);
        TextInputEditText pinNameEditText = dialogView.findViewById(R.id.pinNameEditText);

        pinNameEditText.setText(pin.getAd());
        builder.setView(dialogView);

        builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String yeniAd = pinNameEditText.getText().toString().trim();
                if (yeniAd.isEmpty()) {
                    Toast.makeText(PinlerimActivity.this, "Pin adı boş bırakılamaz", Toast.LENGTH_SHORT).show();
                    return;
                }
                piniGuncelle(pin, yeniAd);
            }
        });
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void pinSilmeOnayDialoguGoster(Pin pin, int position) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Pini Sil")
                .setMessage("'" + pin.getAd() + "' adlı pini silmek istediğinizden emin misiniz?")
                .setPositiveButton("Sil", (dialog, which) -> piniSil(pin, position))
                .setNegativeButton("İptal", null)
                .show();
    }

    private void piniSil(Pin pin, int position) {
        if (currentUser == null || pin.getId() == null) {
            Toast.makeText(this, "Pin silinemedi. Kullanıcı veya pin ID bulunamadı.", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("users").document(currentUser.getUid()).collection("pins").document(pin.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(PinlerimActivity.this, "'" + pin.getAd() + "' pini silindi.", Toast.LENGTH_SHORT).show();
                    pinListesi.remove(position);
                    pinAdapter.notifyItemRemoved(position);
                    pinAdapter.notifyItemRangeChanged(position, pinListesi.size());
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Pin silinirken hata", e);
                    Toast.makeText(PinlerimActivity.this, "Pin silinirken hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void piniGuncelle(Pin pin, String yeniAd) {
        if (currentUser == null || pin.getId() == null) {
            Toast.makeText(this, "Pin güncellenemedi. Kullanıcı veya pin ID bulunamadı.", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("users").document(currentUser.getUid()).collection("pins").document(pin.getId())
                .update("ad", yeniAd)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(PinlerimActivity.this, "Pin adı güncellendi.", Toast.LENGTH_SHORT).show();
                    kullaniciPinleriniGetir();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PinlerimActivity.this, "Pin güncellenirken hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Pin güncellenirken hata", e);
                });
    }

    private void tekPiniHaritadaGoster(Pin pin) {
        Intent intent = new Intent(PinlerimActivity.this, MainActivity.class);
        intent.putExtra("tekPinYukle", true);
        intent.putExtra("pinId", pin.getId());
        intent.putExtra("pinAd", pin.getAd());
        intent.putExtra("pinLat", pin.getLat());
        intent.putExtra("pinLng", pin.getLng());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}