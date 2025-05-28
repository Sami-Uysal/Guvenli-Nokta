package com.guvenlinokta.app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class ProfilActivity extends AppCompatActivity {

    private DatePicker tarihSecici;
    private TextView kalanSureText;
    private ProgressBar ilerlemeCubugu;
    private CountDownTimer geriSayim;
    private long secilenZamanMillis;
    private long toplamSureMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        tarihSecici = findViewById(R.id.tarihSecici);
        kalanSureText = findViewById(R.id.kalanSureText);
        ilerlemeCubugu = findViewById(R.id.ilerlemeCubugu);

        Calendar bugun = Calendar.getInstance();
        tarihSecici.init(
                bugun.get(Calendar.YEAR),
                bugun.get(Calendar.MONTH),
                bugun.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int yil, int ay, int gun) {
                        Calendar secilenTarih = Calendar.getInstance();
                        secilenTarih.set(yil, ay, gun, 0, 0, 0);
                        secilenZamanMillis = secilenTarih.getTimeInMillis();
                        toplamSureMillis = secilenZamanMillis - System.currentTimeMillis();
                        baslatGeriSayim();
                    }
                }
        );
        Button btnPinleriYukle = findViewById(R.id.btnPinleriYukle);
        ProgressBar ilerlemeCubugu = findViewById(R.id.ilerlemeCubugu);

        btnPinleriYukle.setOnClickListener(v -> {
            ilerlemeCubugu.setVisibility(View.VISIBLE);
            btnPinleriYukle.setEnabled(false);
            new android.os.Handler().postDelayed(() -> {
                ilerlemeCubugu.setVisibility(View.GONE);
                btnPinleriYukle.setEnabled(true);
                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                intent.putExtra("pinleriYukle", true);
                startActivity(intent);
                finish();
            }, 3000);
        });
        Button btnCikis = findViewById(R.id.btnCikis);
        btnCikis.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Çıkış yapıldı.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void baslatGeriSayim() {
        if (geriSayim != null) geriSayim.cancel();

        long kalanMillis = secilenZamanMillis - System.currentTimeMillis();
        if (kalanMillis <= 0) {
            kalanSureText.setText("Süre doldu!");
            ilerlemeCubugu.setProgress(0);
            return;
        }

        geriSayim = new CountDownTimer(kalanMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long saniye = millisUntilFinished / 1000;
                long saat = saniye / 3600;
                long dakika = (saniye % 3600) / 60;
                long sn = saniye % 60;
                kalanSureText.setText(String.format("Kalan Süre: %02d:%02d:%02d", saat, dakika, sn));

                int ilerleme = (int) (100 * millisUntilFinished / toplamSureMillis);
                ilerlemeCubugu.setProgress(ilerleme);
            }

            @Override
            public void onFinish() {
                kalanSureText.setText("Süre doldu!");
                ilerlemeCubugu.setProgress(0);
            }
        }.start();
    }
}