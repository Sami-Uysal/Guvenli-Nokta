package com.guvenlinokta.app.info;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guvenlinokta.app.R;

import java.util.ArrayList;
import java.util.List;

public class Yangin extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AfetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afet);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Yangın Bilgisi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        WebView youtubeWebView = findViewById(R.id.youtubeWebView);
        youtubeWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String videoHtml = "<iframe width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/yQjUhzNMNe8\" frameborder=\"0\" " +
                "allowfullscreen></iframe>";

        youtubeWebView.loadData(videoHtml, "text/html", "utf-8");

        recyclerView = findViewById(R.id.recyclerViewDeprem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<CardModel> cardList = createCardList();
        adapter = new AfetAdapter(cardList, this::openDetailActivity);

        recyclerView.setAdapter(adapter);
    }

    private List<CardModel> createCardList() {
        List<CardModel> cardList = new ArrayList<>();
        cardList.add(new CardModel( "Yangın Anında Yapılacaklar",
                "Yangın sırasında uygulanması gereken temel adımlar."));
        cardList.add(new CardModel( "Yangın Söndürme Aşamaları (Tüple)",
                "Yangın tüpü kullanarak yangına müdahale etme adımları."));
        cardList.add(new CardModel( "Yangın Sınıfları ve Türleri",
                "Farklı yangın türleri ve sınıflandırmaları."));
        cardList.add(new CardModel( "Sınıflara Göre Söndürme Yöntemleri",
                "Her yangın sınıfı için uygun söndürme teknikleri."));
        cardList.add(new CardModel( "Genel Yangın Güvenliği ve Önlemler",
                "Yangınlara karşı alınabilecek genel güvenlik tedbirleri."));
        return cardList;
    }

    private void openDetailActivity(int position) {
        Intent intent = new Intent(Yangin.this, DetailCardActivity.class);
        intent.putExtra("card_title", createCardList().get(position).getTitle());
        intent.putExtra("card_position", 0);

        ArrayList<CardModel> detailCards = new ArrayList<>(createDetailCards(position));
        intent.putExtra("detail_cards", detailCards);

        startActivity(intent);
    }

    private List<CardModel> createDetailCards(int cardType) {
        List<CardModel> detailCards = new ArrayList<>();

        switch (cardType) {
            case 0: // "Yangın Anında Yapılacaklar"
                detailCards.add(new CardModel("Sakin Kalın ve Hızlı Düşünün",
                        "Panik yapmayın. Durumu değerlendirin ve hızlıca doğru kararları alın."));
                detailCards.add(new CardModel("Haber Verin (112)",
                        "Derhal itfaiyeye (112) ve çevrenizdekilere haber verin."));
                detailCards.add(new CardModel("Tahliye Edin",
                        "Güvenli bir şekilde binayı terk edin. Asansörleri kullanmayın, merdivenleri tercih edin."));
                detailCards.add(new CardModel("Dumanlı Alanlarda Dikkat",
                        "Dumanın yoğun olduğu bölgelerde yere yakın hareket edin, ağzınızı ve burnunuzu ıslak bir bezle kapatın."));
                detailCards.add(new CardModel("Kıyafetler Tutuşursa",
                        "Kıyafetleriniz alev alırsa DUR-YAT-YUVARLAN yöntemini uygulayın."));
                detailCards.add(new CardModel("Mahsur Kalırsanız",
                        "Mahsur kalırsanız, kapı altlarını ıslak bezlerle tıkayarak duman girişini engelleyin ve pencereden yardım isteyin."));
                detailCards.add(new CardModel("Müdahaleden Kaçının (Eğitimsizseniz)",
                        "Gerekli eğitiminiz ve ekipmanınız yoksa yangına müdahale etmeye çalışmayın, kendi güvenliğinizi riske atmayın."));
                break;

            case 1: // "Yangın Söndürme Aşamaları (Tüple)"
                detailCards.add(new CardModel("Güvenliği Sağlayın",
                        "Kendi güvenliğinizi ve çevrenizdekilerin güvenliğini sağlayın."));
                detailCards.add(new CardModel("Yangın Tüpünü Hazırlayın",
                        "Yangın tüpünün pimini çekin."));
                detailCards.add(new CardModel("Doğru Mesafeden Müdahale Edin",
                        "Yangına uygun mesafeden (genellikle 2-3 metre) yaklaşın. Rüzgarı arkanıza alın."));
                detailCards.add(new CardModel("Alevlerin Kaynağına Doğru Tutun",
                        "Tüpün hortumunu (nozulunu) alevlerin dibine, yanmakta olan maddeye doğru yöneltin."));
                detailCards.add(new CardModel("Tetiği Sıkın ve Süpürme Hareketi Yapın",
                        "Tetiği sıkarak söndürücü maddeyi boşaltın. Alevleri geniş bir alana yayarak süpürme hareketiyle söndürün."));
                detailCards.add(new CardModel("Kontrol Edin",
                        "Yangının tamamen söndüğünden emin olun. Gerekirse tekrar müdahale edin."));
                break;

            case 2: // "Yangın Sınıfları ve Türleri"
                detailCards.add(new CardModel("A Sınıfı Yangınlar",
                        "Katı madde yangınlarıdır (örn: odun, kömür, kağıt, kumaş). Soğutma ve yanıcı maddenin uzaklaştırılması ile söndürülür."));
                detailCards.add(new CardModel("B Sınıfı Yangınlar",
                        "Sıvı yanıcı madde yangınlarıdır (örn: benzin, mazot, yağlar, boyalar). Boğma (oksijeni kesme) yöntemiyle söndürülür."));
                detailCards.add(new CardModel("C Sınıfı Yangınlar",
                        "Gaz halindeki yanıcı madde yangınlarıdır (örn: metan, propan, LPG, doğalgaz). Öncelikle gaz akışı kesilmeli, sonra söndürülmelidir."));
                detailCards.add(new CardModel("D Sınıfı Yangınlar",
                        "Yanabilen metal yangınlarıdır (örn: alüminyum, magnezyum, titanyum). Özel D tipi söndürme tozları kullanılır. Su KESİNLİKLE kullanılmamalıdır."));
                detailCards.add(new CardModel("E Sınıfı Yangınlar (Elektrik)",
                        "Elektrikli ekipman ve tesisat yangınlarıdır. Öncelikle elektrik akımı kesilmelidir. Karbondioksit veya kuru kimyevi tozlu söndürücüler kullanılır."));
                detailCards.add(new CardModel("F (K) Sınıfı Yangınlar (Mutfak)",
                        "Bitkisel ve hayvansal pişirme yağlarının yangınlarıdır. Özel F (veya K) tipi söndürücüler kullanılır."));
                break;

            case 3: // "Sınıflara Göre Söndürme Yöntemleri"
                detailCards.add(new CardModel("A Sınıfı Söndürme",
                        "Su (soğutma), Kuru Kimyevi Toz (ABC veya A), Köpük."));
                detailCards.add(new CardModel("B Sınıfı Söndürme",
                        "Kuru Kimyevi Toz (ABC veya BC), Köpük, Karbondioksit (CO2), Halokarbon gazları. Su genellikle etkisizdir veya yangını yayabilir (yağ yangınları hariç özel tekniklerle)."));
                detailCards.add(new CardModel("C Sınıfı Söndürme",
                        "Kuru Kimyevi Toz (ABC veya BC). Önce gaz kaynağını kesin."));
                detailCards.add(new CardModel("D Sınıfı Söndürme",
                        "Özel D sınıfı söndürme tozları. Su, CO2, köpük KULLANILMAZ."));
                detailCards.add(new CardModel("E Sınıfı (Elektrik) Söndürme",
                        "Elektriği kesin! Karbondioksit (CO2), Kuru Kimyevi Toz (C etkili). İletken olmayan söndürücüler kullanılmalıdır."));
                detailCards.add(new CardModel("F (K) Sınıfı (Mutfak) Söndürme",
                        "Özel F (K) sınıfı sıvı kimyasal söndürücüler (potasyum bazlı). Su KESİNLİKLE kullanılmamalıdır (parlama ve yayılma riski)."));
                break;

            case 4: // "Genel Yangın Güvenliği ve Önlemler"
                detailCards.add(new CardModel("Yangın Dedektörleri",
                        "Ev ve iş yerlerinde duman ve ısı dedektörleri bulundurun ve düzenli kontrol edin."));
                detailCards.add(new CardModel("Yangın Söndürme Cihazları",
                        "Uygun tipte ve yeterli sayıda yangın söndürme cihazı bulundurun, periyodik bakımlarını yaptırın ve kullanmayı öğrenin."));
                detailCards.add(new CardModel("Kaçış Yolları",
                        "Kaçış yollarını her zaman açık ve engelsiz tutun. Acil çıkış kapılarını kilitlemeyin."));
                detailCards.add(new CardModel("Yangın Eğitimi ve Tatbikatları",
                        "Yangın güvenliği konusunda eğitim alın ve düzenli olarak yangın tatbikatları yapın."));
                detailCards.add(new CardModel("Elektrik Tesisatı Kontrolü",
                        "Elektrik tesisatınızın periyodik kontrollerini yetkili kişilere yaptırın. Eskimiş veya hasarlı kabloları değiştirin. Prizlere aşırı yüklenmeyin."));
                detailCards.add(new CardModel("Yanıcı Maddelerin Depolanması",
                        "Yanıcı ve parlayıcı maddeleri güvenli yerlerde, ısı kaynaklarından uzakta depolayın."));
                break;
        }
        return detailCards;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}