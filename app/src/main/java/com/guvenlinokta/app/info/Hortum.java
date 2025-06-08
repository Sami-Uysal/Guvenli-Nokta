package com.guvenlinokta.app.info;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guvenlinokta.app.R;
import com.guvenlinokta.app.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class Hortum extends BaseActivity {
    private RecyclerView recyclerView;
    private AfetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afet);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hortum Bilgisi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        WebView youtubeWebView = findViewById(R.id.youtubeWebView);
        youtubeWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String videoHtml = "<iframe width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/cWy4CwFywLg\" frameborder=\"0\" " +
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
        cardList.add(new CardModel( "Hortum Öncesi Alınacak Önlemler",
                "Hortum tehlikesine karşı hazırlıklı olmak için yapılması gerekenler."));
        cardList.add(new CardModel( "Hortum Sırasında Yapılacaklar (Bina İçi)",
                "Bina içinde hortuma yakalanırsanız almanız gereken tedbirler."));
        cardList.add(new CardModel( "Hortum Sırasında Yapılacaklar (Dışarıda/Araçta)",
                "Açık alanda veya araçta hortuma yakalanırsanız yapmanız gerekenler."));
        cardList.add(new CardModel( "Hortum Sonrası Yapılacaklar",
                "Hortum felaketi geçtikten sonra dikkat edilmesi gerekenler."));
        return cardList;
    }

    private void openDetailActivity(int position) {
        Intent intent = new Intent(Hortum.this, DetailCardActivity.class);
        intent.putExtra("card_title", createCardList().get(position).getTitle());
        intent.putExtra("card_position", 0);

        ArrayList<CardModel> detailCards = new ArrayList<>(createDetailCards(position));
        intent.putExtra("detail_cards", detailCards);

        startActivity(intent);
    }

    private List<CardModel> createDetailCards(int cardType) {
        List<CardModel> detailCards = new ArrayList<>();

        switch (cardType) {
            case 0: // "Hortum Öncesi Alınacak Önlemler"
                detailCards.add(new CardModel("Afet ve Acil Durum Planı",
                        "Tüm afetleri kapsayacak şekilde afet ve acil durum aile planı ve afet çantası hazırlayın."));
                detailCards.add(new CardModel("Bilgi Takibi",
                        "TV, radyo veya sosyal medyadan Meteoroloji Genel Müdürlüğü ve ilgili kurumlardan gelecek hava durumu raporlarını takip edin."));
                detailCards.add(new CardModel("Yardım İhtiyacı",
                        "Yardıma ihtiyaç duyuyorsanız hemen 112’yi arayın."));
                detailCards.add(new CardModel("Olağandışı Hava Değişiklikleri",
                        "Havanın olağandışı değiştiğini fark ettiğinizde yetkili kurumlardan gelecek uyarıları takip edin. Gerekirse sığınacak bir yer planlayın."));
                break;

            case 1: // "Hortum Sırasında Yapılacaklar (Bina İçi)"
                detailCards.add(new CardModel("Güvenlik Önlemleri",
                        "Elektrik şalterini, su ve doğalgaz vanalarını kapatın!"));
                detailCards.add(new CardModel("Sığınak Kullanımı",
                        "Sığınağınız varsa hemen sığınağa gidin. Sığınağa ulaşmak için asansörleri kullanmayın!"));
                detailCards.add(new CardModel("Sığınak Yoksa",
                        "Sığınağınız yoksa penceresiz odalara (banyo, koridor gibi) sığının. Kapı ve pencereleri kapalı tutun!"));
                detailCards.add(new CardModel("Çök-Kapan-Tutun",
                        "Sağlam bir nesnenin yanında veya altında çök-kapan-tutun yaparak, hortum bitinceye kadar bekleyin. Vücudunuzu battaniye ya da benzeri kalın örtülerle koruyabilirsiniz."));
                detailCards.add(new CardModel("Baş Koruması",
                        "Uçuşabilecek parçalara karşı başınızı koruyun."));
                break;

            case 2: // "Hortum Sırasında Yapılacaklar (Dışarıda/Araçta)"
                detailCards.add(new CardModel("Açık Alandaysanız",
                        "Öncelikle sığınacak güvenli bir yer arayın. Uçuşabilecek parçalara karşı başınızı koruyun."));
                detailCards.add(new CardModel("Uzak Durulacak Yerler",
                        "Köprü, üstgeçitler ile enerji nakil hatlarından uzak durun!"));
                detailCards.add(new CardModel("Sığınma Alanları",
                        "Düz ve alçak alanları sığınmak için tercih edin!"));
                detailCards.add(new CardModel("Araç İçindeyseniz",
                        "Bir aracın içinde sığınmanız gerekirse, emniyet kemerini takarak başınızı bir örtüyle koruyun."));
                break;

            case 3: // "Hortum Sonrası Yapılacaklar"
                detailCards.add(new CardModel("Mahsur Kalırsanız",
                        "Duvara ya da musluk borularına vurup arama kurtarma ekiplerine yerinizi bildirebilirsiniz."));
                detailCards.add(new CardModel("Resmi Açıklamalar",
                        "Resmi açıklamaları ve uyarıları takip edin."));
                detailCards.add(new CardModel("İletişim",
                        "Telefon veya sosyal medya aracılığıyla ailenizi ve arkadaşlarınızı kontrol edin."));
                detailCards.add(new CardModel("Tehlikeli Alanlar",
                        "Hasarlı yapılardan ve enerji hatlarından uzak durun."));
                detailCards.add(new CardModel("Enkaz Kaldırma",
                        "Hortum sonrası enkaz parçalarını kaldırırken dikkat edin. Koruyucu kıyafetler giyin. Gerekiyorsa yardım çağırın."));
                detailCards.add(new CardModel("Hasar Tespiti",
                        "Sigorta işlemleriniz için evinizdeki, aracınızdaki hasarları fotoğraflayın."));
                detailCards.add(new CardModel("Aydınlatma",
                        "Evinizde elektrik yoksa olası bir yangına neden olmamak için mum yerine pilli fenerler kullanın."));
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