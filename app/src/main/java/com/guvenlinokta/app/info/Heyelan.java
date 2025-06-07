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

public class Heyelan extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AfetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afet);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Heyelan Bilgisi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        WebView youtubeWebView = findViewById(R.id.youtubeWebView);
        youtubeWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String videoHtml = "<iframe width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/c5If5Vo9hJs\" frameborder=\"0\" " +
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
        cardList.add(new CardModel("Heyelana Karşı Duyarlı ve Güvenli Alanlar",
                "Heyelan riski taşıyan ve taşımayan alanların özellikleri."));
        cardList.add(new CardModel("Heyelan Tehlikesi Durumunda Yapılacaklar",
                "Heyelan şüphesi olduğunda alınması gereken önlemler."));
        cardList.add(new CardModel("Heyelan Anında Yapılacaklar",
                "Heyelan sırasında kapalı ve açık alanlarda davranış şekilleri."));
        cardList.add(new CardModel("Heyelan Sonrasında Yapılacaklar",
                "Heyelan bittikten sonra dikkat edilmesi gerekenler ve ilk yardım."));
        cardList.add(new CardModel("Heyelan Zararlarını Azaltma Sorumlulukları",
                "Bireylerin ve yerel yönetimlerin heyelan riskini azaltma görevleri."));
        return cardList;
    }

    private void openDetailActivity(int position) {
        Intent intent = new Intent(Heyelan.this, DetailCardActivity.class);
        intent.putExtra("card_title", createCardList().get(position).getTitle());
        intent.putExtra("card_position", 0);

        ArrayList<CardModel> detailCards = new ArrayList<>(createDetailCards(position));
        intent.putExtra("detail_cards", detailCards);

        startActivity(intent);
    }

    private List<CardModel> createDetailCards(int cardType) {
        List<CardModel> detailCards = new ArrayList<>();

        switch (cardType) {
            case 0: // "Heyelana Karşı Duyarlı ve Güvenli Alanlar"
                detailCards.add(new CardModel("Heyelana Karşı Duyarlı Alanlar",
                        "Eski heyelan bölgeleri\n" +
                                "Doğal yamaçların üst ve topuk kesimleri\n" +
                                "Eski dolguların üst ve topuk kesimleri\n" +
                                "Çok dik ve derin yamaçların üst ve topuk kesimleri\n" +
                                "Atık sistemlerinin kullanıldığı ve yerleşimin geliştiği tepelik alanlar"));
                detailCards.add(new CardModel("Heyelan Açısından Güvenli Alanlar",
                        "Geçmişte herhangi bir hareketin meydana gelmediği sert ve masif kayaların oluşturduğu yamaçlar\n" +
                                "Yamaç eğiminde ani değişimlerin gözlenmediği nispeten düşük eğimli araziler\n" +
                                "Burun şeklinde çıkıntılı sırtların üstü veya çevresi"));
                break;

            case 1: // "Heyelan Tehlikesi Durumunda Yapılacaklar"
                detailCards.add(new CardModel("Yerel Yönetimle İletişim",
                        "Derhal yerel yönetimle temasa geçilmelidir."));
                detailCards.add(new CardModel("Çevreyi Bilgilendirme",
                        "Heyelandan etkilenebilecek çevre bilgilendirilmelidir."));
                detailCards.add(new CardModel("Yapıları Boşaltma",
                        "Yapılar boşaltılmalıdır."));
                break;

            case 2: // "Heyelan Anında Yapılacaklar"
                detailCards.add(new CardModel("Kapalı Alandaysanız",
                        "Binadan çıkmak ve heyelan bölgesinden uzaklaşmak için yeterli vaktiniz yoksa içeride kalın.\n" +
                                "Sağlam eşyaların altında ve ya yanında hayat üçgeni oluşturarak ÇÖK-KAPAN-TUTUN hareketini uygulayın."));
                detailCards.add(new CardModel("Açık Alandaysanız",
                        "Tehlike anında heyelan veya çamur akıntısının yolundan uzak durarak hemen mümkün olduğu kadar yükseklere doğru uzaklaşın ve çevrenizde yaşayan insanları toprak kaymasına karşı uyarın.\n" +
                                "Çamur ve moloz akmasından kaçabilecek zamanınız veya etrafınızda arkasına saklanacağınız sağlam bir yapı yoksa ÇÖK-KAPAN-TUTUN hareketi ile başınızı ve boynunuzu koruyun."));
                break;

            case 3: // "Heyelan Sonrasında Yapılacaklar"
                detailCards.add(new CardModel("Güvenliğinizi Sağlayın",
                        "Her şeyden önce güvencede olduğunuzdan emin olun. Gerekiyorsa tehlikeli bölgeden uzaklaşarak kendinizi güvenceye alın."));
                detailCards.add(new CardModel("Kaynakları Kapatın ve Aydınlatma",
                        "Yakınınızda bulunan elektrik, gaz ve su kaynaklarını hemen kapatın. Çevrenizde gaz kaçağı olmadığından emin olana kadar bulunduğunuz yeri kibrit veya diğer yanıcı maddeler ve ya elektrikli aletlerle aydınlatmaya çalışmayın. Fener kullanın."));
                detailCards.add(new CardModel("Yaralılara Yardım",
                        "Çevrenizde yaralı veya yardıma muhtaç kişiler varsa, yangın ve yeni bir heyelan gibi bir tehlike yoksa onları yerlerinden oynatmayın."));
                detailCards.add(new CardModel("Tehlikeli Yapılar",
                        "Tehlikeli duvarlar, çatılar ve bacalara karşı çevrenizdekileri uyarın ve bunların etrafında dolaşmayın."));
                detailCards.add(new CardModel("Bilgilendirmeleri Takip Edin",
                        "Radyo ve televizyon gibi kitle iletişim araçlarıyla size yapılacak uyarıları dinleyin."));
                detailCards.add(new CardModel("Yolları Açık Tutun",
                        "Cadde ve sokakların acil yardım araçları için boş bırakın."));
                detailCards.add(new CardModel("Hasarlı Binalara Girmeyin",
                        "Eşya almak için zarar görmüş binalara girmeyin."));
                break;

            case 4: // "Heyelan Zararlarını Azaltma Sorumlulukları"
                detailCards.add(new CardModel("Bireylerin Sorumlulukları",
                        "Yaşanılan bölgedeki potansiyel jeolojik afetler hakkında bilgi sahibi olmak,\n" +
                                "Heyelan açısından sorunlu alanlarla ilgili olarak yerbilimcilere ve mühendislere danışmak,\n" +
                                "Dik yamaçların topuk kesiminde desteği kaldırıcı kazı işlemlerinden ve dik yamaçların kenarında veya tabanında yapı inşasından kaçınmak, ayrıca eğimi fazla yamaçların üzerine dolgu malzemesi dökmemek,\n" +
                                "Arazi satın alınmasından, arazi paylaşımından ve inşaat işlemlerinden önce ilgili kurum ve kuruluşlardan konuyla ilgili bilgi almak ve buna göre karar vermek."));
                detailCards.add(new CardModel("Yerel Yönetimler ve Teknik Elemanların Sorumlulukları",
                        "Yerleşim alanlarında yeteri kadar yerbilimleri ve mühendislik araştırmaları yapılmalıdır.\n" +
                                "Yerel yönetimler yeni yerleşim alanlarını belirlerken arazi çalışmaları ve değerlendirmeler yaptırarak, heyelana ve diğer afetlere maruz kalmış ve afetlerin beklenebilecek alanların belirlenmesiyle hazırlanacak duyarlılık haritaları yaptırmalı, bu haritalar aracılığıyla mühendislere, kent planlamacılarına yüksek riske sahip alanlardan kaçınılması konusunda veriler sunulmalıdır."));
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