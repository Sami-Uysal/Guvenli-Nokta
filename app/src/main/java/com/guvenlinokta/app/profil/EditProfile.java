package com.guvenlinokta.app.profil;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.guvenlinokta.app.R;
import com.guvenlinokta.app.ui.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditProfile extends BaseActivity {

    private static final String ETIKET = "EditProfileActivity";
    private TextInputEditText kullaniciAdiMetinKutusu, yeniSifreMetinKutusu, mevcutSifreMetinKutusu;
    private TextView dogumTarihiMetni;
    private Button kaydetButonu;

    private FirebaseAuth kimlikDogrulama;
    private FirebaseFirestore veriTabani;
    private FirebaseUser mevcutKullanici;

    private Calendar secilenDogumTarihiTakvim;
    private String mevcutKayitliDogumTarihi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        kimlikDogrulama = FirebaseAuth.getInstance();
        veriTabani = FirebaseFirestore.getInstance();
        mevcutKullanici = kimlikDogrulama.getCurrentUser();

        kullaniciAdiMetinKutusu = findViewById(R.id.kullaniciAdiMetinKutusu);
        yeniSifreMetinKutusu = findViewById(R.id.yeniSifreMetinKutusu);
        mevcutSifreMetinKutusu = findViewById(R.id.mevcutSifreMetinKutusu);
        dogumTarihiMetni = findViewById(R.id.dogumTarihiMetni);
        kaydetButonu = findViewById(R.id.kaydetButonu);

        secilenDogumTarihiTakvim = Calendar.getInstance();

        if (mevcutKullanici == null) {
            Toast.makeText(this, "Kullanıcı bulunamadı. Lütfen tekrar giriş yapın.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        kullaniciProfiliniYukle();

        dogumTarihiMetni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarihSeciciDialogunuGoster();
            }
        });

        kaydetButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kullaniciProfiliDegisiklikleriniKaydet();
            }
        });
    }

    private void kullaniciProfiliniYukle() {
        if (mevcutKullanici.getDisplayName() != null) {
            kullaniciAdiMetinKutusu.setText(mevcutKullanici.getDisplayName());
        }

        DocumentReference kullaniciBelgeRef = veriTabani.collection("users").document(mevcutKullanici.getUid());
        kullaniciBelgeRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("dogumTarihi")) {
                String dogumTarihiDizgi = documentSnapshot.getString("dogumTarihi");
                mevcutKayitliDogumTarihi = dogumTarihiDizgi;
                dogumTarihiMetni.setText(dogumTarihiDizgi);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    secilenDogumTarihiTakvim.setTime(sdf.parse(dogumTarihiDizgi));
                } catch (Exception e) {
                    Log.e(ETIKET, "Doğum tarihi parse edilemedi", e);
                    secilenDogumTarihiTakvim = Calendar.getInstance();
                }
            } else {
                dogumTarihiMetni.setText("Seçilmedi");
                mevcutKayitliDogumTarihi = null;
            }
        }).addOnFailureListener(hata -> Log.w(ETIKET, "Kullanıcı verileri (doğum tarihi) alınamadı", hata));
    }

    private void tarihSeciciDialogunuGoster() {
        DatePickerDialog.OnDateSetListener tarihAyarlandiDinleyici = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yil, int ay, int gun) {
                secilenDogumTarihiTakvim.set(Calendar.YEAR, yil);
                secilenDogumTarihiTakvim.set(Calendar.MONTH, ay);
                secilenDogumTarihiTakvim.set(Calendar.DAY_OF_MONTH, gun);
                etiketiGuncelle();
            }
        };

        new DatePickerDialog(EditProfile.this, tarihAyarlandiDinleyici,
                secilenDogumTarihiTakvim.get(Calendar.YEAR),
                secilenDogumTarihiTakvim.get(Calendar.MONTH),
                secilenDogumTarihiTakvim.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void etiketiGuncelle() {
        String tarihFormati = "dd/MM/yyyy";
        SimpleDateFormat basitTarihFormati = new SimpleDateFormat(tarihFormati, Locale.getDefault());
        dogumTarihiMetni.setText(basitTarihFormati.format(secilenDogumTarihiTakvim.getTime()));
    }

    private void kullaniciProfiliDegisiklikleriniKaydet() {
        String yeniKullaniciAdi = kullaniciAdiMetinKutusu.getText().toString().trim();
        String yeniSifre = yeniSifreMetinKutusu.getText().toString().trim();
        String mevcutSifre = mevcutSifreMetinKutusu.getText().toString().trim();
        String dogumTarihiDizgiKayit = dogumTarihiMetni.getText().toString();

        boolean kullaniciAdiGuncellenecek = !TextUtils.isEmpty(yeniKullaniciAdi) && !yeniKullaniciAdi.equals(mevcutKullanici.getDisplayName());
        boolean sifreGuncellenecek = !TextUtils.isEmpty(yeniSifre);
        boolean dogumTarihiGuncellenecek = !dogumTarihiDizgiKayit.equals("Seçilmedi") &&
                (mevcutKayitliDogumTarihi == null || !dogumTarihiDizgiKayit.equals(mevcutKayitliDogumTarihi));

        if (!kullaniciAdiGuncellenecek && !sifreGuncellenecek && !dogumTarihiGuncellenecek) {
            Toast.makeText(EditProfile.this, "Kaydedilecek değişiklik yok.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Task<?>> tumGorevler = new ArrayList<>();
        AtomicBoolean genelHataOldu = new AtomicBoolean(false);

        if (kullaniciAdiGuncellenecek) {
            UserProfileChangeRequest profilGuncellemeleri = new UserProfileChangeRequest.Builder()
                    .setDisplayName(yeniKullaniciAdi)
                    .build();
            Task<Void> kullaniciAdiGorevi = mevcutKullanici.updateProfile(profilGuncellemeleri)
                    .addOnFailureListener(e -> {
                        genelHataOldu.set(true);
                        Toast.makeText(EditProfile.this, "Kullanıcı adı güncellenemedi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(ETIKET, "Kullanıcı adı güncelleme hatası", e);
                    });
            tumGorevler.add(kullaniciAdiGorevi);
        }

        if (sifreGuncellenecek) {
            if (TextUtils.isEmpty(mevcutSifre)) {
                Toast.makeText(this, "Şifre değişikliği için mevcut şifrenizi girmelisiniz.", Toast.LENGTH_LONG).show();
                mevcutSifreMetinKutusu.setError("Mevcut şifre gerekli");
                mevcutSifreMetinKutusu.requestFocus();
                return;
            }

            if (mevcutKullanici.getEmail() == null || mevcutKullanici.getEmail().isEmpty()) {
                Toast.makeText(this, "Şifre değişikliği için kullanıcının geçerli bir e-postası olmalıdır. Lütfen destek ile iletişime geçin.", Toast.LENGTH_LONG).show();
                Log.e(ETIKET, "Şifre güncelleme denemesi ancak kullanıcının e-postası yok.");
                return;
            }


            AuthCredential credential = EmailAuthProvider.getCredential(mevcutKullanici.getEmail(), mevcutSifre);
            Task<Void> yenidenKimlikDogrulamaVeGuncellemeGorevi = mevcutKullanici.reauthenticate(credential)
                    .continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            genelHataOldu.set(true);
                            Exception e = task.getException();
                            Log.e(ETIKET, "Yeniden kimlik doğrulama başarısız", e);
                            Toast.makeText(EditProfile.this, "Mevcut şifre yanlış. Yeniden kimlik doğrulama başarısız.", Toast.LENGTH_LONG).show();
                            mevcutSifreMetinKutusu.setError("Şifre yanlış");
                            mevcutSifreMetinKutusu.requestFocus();
                            return Tasks.forException(e != null ? e : new Exception("Yeniden kimlik doğrulama başarısız"));
                        }

                        Log.d(ETIKET, "Kullanıcı başarıyla yeniden doğrulandı.");
                        List<Task<Void>> hassasGuncellemeGorevleri = new ArrayList<>();

                        if (sifreGuncellenecek) {
                            hassasGuncellemeGorevleri.add(
                                    mevcutKullanici.updatePassword(yeniSifre)
                                            .addOnFailureListener(e -> {
                                                genelHataOldu.set(true);
                                                Toast.makeText(EditProfile.this, "Şifre güncellenemedi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                Log.e(ETIKET, "Şifre güncelleme hatası", e);
                                            })
                            );
                        }
                        if (hassasGuncellemeGorevleri.isEmpty()) {
                            return Tasks.forResult(null);
                        }
                        return Tasks.whenAll(hassasGuncellemeGorevleri);
                    });
            tumGorevler.add(yenidenKimlikDogrulamaVeGuncellemeGorevi);
        }

        if (dogumTarihiGuncellenecek) {
            DocumentReference kullaniciBelgeRef = veriTabani.collection("users").document(mevcutKullanici.getUid());
            Map<String, Object> kullaniciVerileri = new HashMap<>();
            kullaniciVerileri.put("dogumTarihi", dogumTarihiDizgiKayit);
            Task<Void> dogumTarihiGorevi = kullaniciBelgeRef.set(kullaniciVerileri, SetOptions.merge())
                    .addOnFailureListener(e -> {
                        genelHataOldu.set(true);
                        Toast.makeText(EditProfile.this, "Doğum tarihi kaydedilemedi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.w(ETIKET, "Doğum tarihi Firestore'a kaydedilemedi", e);
                    });
            tumGorevler.add(dogumTarihiGorevi);
        }

        if (tumGorevler.isEmpty()) {
            if (!genelHataOldu.get()) {
                Toast.makeText(EditProfile.this, "Değişiklikler başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
                finish();
            }
            return;
        }

        Tasks.whenAllComplete(tumGorevler).addOnCompleteListener(task -> {
            if (genelHataOldu.get()) {
                Log.d(ETIKET, "Bir veya daha fazla güncelleme işlemi başarısız oldu.");
            } else {
                boolean tumGorevlerBasarili = true;
                for (Task<?> tamamlananGorev : tumGorevler) {
                    if (!tamamlananGorev.isSuccessful()) {
                        tumGorevlerBasarili = false;
                        if (tamamlananGorev.getException() != null) {
                            Log.e(ETIKET, "Bir görev başarısız oldu: ", tamamlananGorev.getException());
                        }
                        break;
                    }
                }

                if (tumGorevlerBasarili) {
                    Toast.makeText(EditProfile.this, "Değişiklikler başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (!genelHataOldu.get()) {
                    Toast.makeText(EditProfile.this, "Bazı güncellemeler sırasında hata oluştu. Lütfen tekrar deneyin.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}