package com.guvenlinokta.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import com.guvenlinokta.app.ui.BaseActivity;

public class ProfilActivity extends BaseActivity {

    private static final int RESIM_SECME_ISTEGI = 1;
    private static final long TITRESIM_SURESI_MS = 50;

    private ShapeableImageView profilResmiImageView;
    private TextView kullaniciAdiMetniProfil;
    private TextView epostaMetniProfil;
    private FirebaseAuth kimlikDogrulama;
    private FirebaseStorage depolama;
    private StorageReference depolamaReferansi;
    private Uri resimYoluUri;
    private RelativeLayout profiliDuzenleYerlesimi;
    private RelativeLayout pinlerimYerlesimi;
    private Vibrator titresimServisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        kimlikDogrulama = FirebaseAuth.getInstance();
        depolama = FirebaseStorage.getInstance();
        depolamaReferansi = depolama.getReference();
        titresimServisi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
                startActivity(new Intent(ProfilActivity.this, EditProfile.class));
            }
        });

        pinlerimYerlesimi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilActivity.this, PinlerimActivity.class));
            }
        });
        cikisYapMetni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kimlikDogrulama.signOut();
                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        bildirimAnahtari.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (titresimServisi != null && titresimServisi.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    titresimServisi.vibrate(VibrationEffect.createOneShot(TITRESIM_SURESI_MS, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    titresimServisi.vibrate(TITRESIM_SURESI_MS);
                }
            }

            if (isChecked) {
                Toast.makeText(this, "Bildirimler açıldı.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bildirimler kapatıldı.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        kullaniciVerileriniYukle();
    }

    private void kullaniciVerileriniYukle() {
        FirebaseUser mevcutKullanici = kimlikDogrulama.getCurrentUser();
        if (mevcutKullanici != null) {
            String goruntulenecekAd = mevcutKullanici.getDisplayName();
            String eposta = mevcutKullanici.getEmail();
            Uri fotografAdresi = mevcutKullanici.getPhotoUrl();

            if (goruntulenecekAd != null && !goruntulenecekAd.isEmpty()) {
                kullaniciAdiMetniProfil.setText(goruntulenecekAd);
            } else {
                kullaniciAdiMetniProfil.setText("Kullanıcı Adı Yok");
            }

            if (eposta != null && !eposta.isEmpty()) {
                epostaMetniProfil.setText(eposta);
            } else {
                epostaMetniProfil.setText("E-posta Yok");
            }

            if (fotografAdresi != null) {
                Glide.with(this).load(fotografAdresi).placeholder(R.drawable.profil).into(profilResmiImageView);
            } else {
                Glide.with(this).load(R.drawable.profil).into(profilResmiImageView);
            }
        }
    }

    private void resimSeciciyiAc() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Profil Resmi Seç"), RESIM_SECME_ISTEGI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESIM_SECME_ISTEGI && resultCode == RESULT_OK && data != null && data.getData() != null) {
            resimYoluUri = data.getData();
            Glide.with(this).load(resimYoluUri).into(profilResmiImageView);
            profilResminiYukle();
        }
    }

    private void profilResminiYukle() {
        if (resimYoluUri != null) {
            FirebaseUser kullanici = kimlikDogrulama.getCurrentUser();
            if (kullanici == null) {
                Toast.makeText(this, "Profil resmini yüklemek için kullanıcı girişi yapılmalı.", Toast.LENGTH_SHORT).show();
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
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfilActivity.this, "Resim yükleme başarısız: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

        UserProfileChangeRequest profilGuncellemeleri = new UserProfileChangeRequest.Builder()
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