package com.guvenlinokta.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TutorialActivity extends AppCompatActivity {

    private static final int TOTAL_PAGES = 7;
    private int currentPage = 0;

    private ImageView ivTutorialImage;
    private TextView tvTutorialTitle;
    private TextView tvTutorialContent;
    private Button btnNext;
    private Button btnPrevious;

    private String[] tutorialTitles;
    private String[] tutorialDescriptions;
    private int[] tutorialImageResIds; // 0 if no image for a page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

        if (!isFirstRun) {
            startMainActivity();
            return;
        }

        setContentView(R.layout.activity_tutorial);

        ivTutorialImage = findViewById(R.id.ivTutorialImage);
        tvTutorialTitle = findViewById(R.id.tvTutorialTitle);
        tvTutorialContent = findViewById(R.id.tvTutorialContent);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        initializeTutorialData();
        loadPageContent(currentPage);

        btnNext.setOnClickListener(v -> {
            if (currentPage < TOTAL_PAGES - 1) {
                currentPage++;
                loadPageContent(currentPage);
            } else {
                markTutorialAsComplete();
                startMainActivity();
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                loadPageContent(currentPage);
            }
        });
    }

    private void initializeTutorialData() {
        tutorialTitles = new String[TOTAL_PAGES];
        tutorialDescriptions = new String[TOTAL_PAGES];
        tutorialImageResIds = new int[TOTAL_PAGES];

        tutorialTitles[0] = "Ana Sayfaya Hoş Geldiniz!";
        tutorialDescriptions[0] = "Bu ana ekranda harita üzerinde gezinebilir, önemli konumları görebilir, kendi pinlerinizi ekleyebilir ve sol üstteki menüden uygulamanın diğer özelliklerine (profil, bilgi kartları, ilk yardım, acil durum numarası, siren sesi) erişebilirsiniz.";
        tutorialImageResIds[0] = R.drawable.anasayfa;

        tutorialTitles[1] = "Toplanma Alanları";
        tutorialDescriptions[1] = "Menüden 'Toplanma Alanı Bul' seçeneği ile bulunduğunuz şehir, ilçe ve mahalleyi seçerek size en yakın resmi toplanma alanlarını haritada görebilirsiniz.";
        tutorialImageResIds[1] = R.drawable.toplanma;


        tutorialTitles[2] = "Kişisel Pin Ekleme";
        tutorialDescriptions[2] = "Harita üzerindeki '+' butonu ile kendi önemli noktalarınızı (ev, iş, okul vb.) haritaya kaydedebilirsiniz. Bu pinleri daha sonra profilinizden yönetebilirsiniz.";
        tutorialImageResIds[2] = R.drawable.yenitoplanma;

        tutorialTitles[3] = "Afet Bilgi Kartları";
        tutorialDescriptions[3] = "Menüdeki 'Bilgi Kartları' bölümünden deprem, sel, yangın gibi çeşitli afet türleri hakkında önemli bilgilere ve yapılması gerekenlere ulaşabilirsiniz.";
        tutorialImageResIds[3] = R.drawable.bilgilendirme;

        tutorialTitles[4] = "İlk Yardım Rehberi";
        tutorialDescriptions[4] = "Menüdeki 'İlk Yardım' seçeneği ile temel ilk yardım konularında (kanamalar, yanıklar, kırıklar vb.) pratik bilgilere erişebilirsiniz.";
        tutorialImageResIds[4] = R.drawable.ilkyardim;

        tutorialTitles[5] = "Acil Durum Araçları";
        tutorialDescriptions[5] = "Menüden tek tuşla 112 Acil Çağrı Merkezi'ni arayabilir ve 'Siren Sesi' özelliği ile dikkat çekebilirsiniz.";
        tutorialImageResIds[5] = R.drawable.siren;

        tutorialTitles[6] = "Profiliniz";
        tutorialDescriptions[6] = "Giriş yaparak profilinizi oluşturabilir, kişisel bilgilerinizi düzenleyebilir ve kaydettiğiniz pinleri 'Pinlerim' bölümünden görüntüleyebilirsiniz.";
        tutorialImageResIds[6] = R.drawable.profilsayfa;
    }

    private void loadPageContent(int pageIndex) {
        tvTutorialTitle.setText(tutorialTitles[pageIndex]);
        tvTutorialContent.setText(tutorialDescriptions[pageIndex]);

        if (tutorialImageResIds[pageIndex] != 0) {
            ivTutorialImage.setImageResource(tutorialImageResIds[pageIndex]);
            ivTutorialImage.setVisibility(View.VISIBLE);
        } else {
            ivTutorialImage.setVisibility(View.GONE);
        }

        btnPrevious.setVisibility(pageIndex == 0 ? View.INVISIBLE : View.VISIBLE);
        btnNext.setText(pageIndex == TOTAL_PAGES - 1 ? "Bitir" : "İleri");
    }

    private void markTutorialAsComplete() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isFirstRun", false);
        editor.apply();
    }

    private void startMainActivity() {
        Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}