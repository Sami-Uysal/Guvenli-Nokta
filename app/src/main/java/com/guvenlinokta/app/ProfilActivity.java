package com.guvenlinokta.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import com.guvenlinokta.app.profil.PinlerimActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guvenlinokta.app.profil.EditProfile;

public class ProfilActivity extends AppCompatActivity {

    private static final int RESIM_SECME_ISTEGI = 1;

    private ShapeableImageView profilResmiImageView;
    private TextView kullaniciAdiMetniProfil;
    private TextView epostaMetniProfil;
    private FirebaseAuth kimlikDogrulama;
    private FirebaseStorage depolama;
    private StorageReference depolamaReferansi;
    private Uri resimYoluUri;
    private RelativeLayout profiliDuzenleYerlesimi;
    private RelativeLayout pinlerimYerlesimi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        kimlikDogrulama = FirebaseAuth.getInstance();
        depolama = FirebaseStorage.getInstance();
        depolamaReferansi = depolama.getReference();

        profilResmiImageView = findViewById(R.id.profilResmiImageView);
        kullaniciAdiMetniProfil = findViewById(R.id.isimTextView);
        epostaMetniProfil = findViewById(R.id.emailTextView);
        TextView cikisYapMetni = findViewById(R.id.cikisYapTextView);
        SwitchMaterial bildirimAnahtari = findViewById(R.id.bildirimSwitch);
        profiliDuzenleYerlesimi = findViewById(R.id.profiliDuzenleLayout);
        pinlerimYerlesimi = findViewById(R.id.pinlerimLayout);

        kullaniciVerileriniYukle();

        profilResmiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSeciciyiAc();
            }
        });

        profiliDuzenleYerlesimi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, EditProfile.class);
                startActivity(intent);
            }
        });

        pinlerimYerlesimi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, PinlerimActivity.class);
                startActivity(intent);
            }
        });
        cikisYapMetni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kimlikDogrulama.signOut();
                Toast.makeText(ProfilActivity.this, "Çıkış yapıldı!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        bildirimAnahtari.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(ProfilActivity.this, "Bildirimler açıldı.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfilActivity.this, "Bildirimler kapandı.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        kullaniciVerileriniYukle();
    }

    private void kullaniciVerileriniYukle() { // Değiştirildi
        FirebaseUser mevcutKullanici = kimlikDogrulama.getCurrentUser(); // Değiştirildi
        if (mevcutKullanici != null) {
            String goruntulenecekAd = mevcutKullanici.getDisplayName(); // Değiştirildi
            String eposta = mevcutKullanici.getEmail(); // Değiştirildi
            Uri fotografAdresi = mevcutKullanici.getPhotoUrl(); // Değiştirildi

            if (goruntulenecekAd != null && !goruntulenecekAd.isEmpty()) { // Değiştirildi
                kullaniciAdiMetniProfil.setText(goruntulenecekAd); // Değiştirildi
            } else {
                kullaniciAdiMetniProfil.setText("Kullanıcı"); // Değiştirildi
            }

            if (eposta != null && !eposta.isEmpty()) { // Değiştirildi
                epostaMetniProfil.setText(eposta); // Değiştirildi
            } else {
                epostaMetniProfil.setText("E-posta bilgisi yok"); // Değiştirildi
            }

            if (fotografAdresi != null) { // Değiştirildi
                Glide.with(this)
                        .load(fotografAdresi) // Değiştirildi
                        .placeholder(R.drawable.profil)
                        .error(R.drawable.profil)
                        .into(profilResmiImageView);
            } else {
                Glide.with(this)
                        .load(R.drawable.profil)
                        .into(profilResmiImageView);
            }
        }
    }

    private void resimSeciciyiAc() { // Değiştirildi
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Profil Resmi Seç"), RESIM_SECME_ISTEGI); // Değiştirildi
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESIM_SECME_ISTEGI && resultCode == RESULT_OK && data != null && data.getData() != null) { // Değiştirildi
            resimYoluUri = data.getData(); // Değiştirildi
            Glide.with(this).load(resimYoluUri).into(profilResmiImageView); // Değiştirildi
            profilResminiYukle(); // Değiştirildi
        }
    }

    private void profilResminiYukle() { // Değiştirildi
        if (resimYoluUri != null) { // Değiştirildi
            FirebaseUser kullanici = kimlikDogrulama.getCurrentUser(); // Değiştirildi
            if (kullanici == null) { // Değiştirildi
                Toast.makeText(this, "Kullanıcı bulunamadı.", Toast.LENGTH_SHORT).show();
                return;
            }

            final StorageReference dosyaReferansi = depolamaReferansi.child("profile_images/" + kullanici.getUid() + "/profile.jpg");

            dosyaReferansi.putFile(resimYoluUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dosyaReferansi.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    kullaniciProfilResminiGuncelle(uri);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception hata) {
                            Toast.makeText(ProfilActivity.this, "Yükleme başarısız: " + hata.getMessage(), Toast.LENGTH_SHORT).show(); // Değiştirildi
                        }
                    });
        }
    }

    private void kullaniciProfilResminiGuncelle(Uri profilResimAdresi) {
        FirebaseUser kullanici = kimlikDogrulama.getCurrentUser();
        if (kullanici == null) {
            Toast.makeText(this, "Kullanıcı bulunamadı.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfileChangeRequest profilGuncellemeleri = new UserProfileChangeRequest.Builder() // Değiştirildi
                .setPhotoUri(profilResimAdresi)
                .build();

        kullanici.updateProfile(profilGuncellemeleri)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> gorev) {
                        if (gorev.isSuccessful()) {
                            Toast.makeText(ProfilActivity.this, "Profil resmi güncellendi.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfilActivity.this, "Profil resmi güncellenemedi.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}