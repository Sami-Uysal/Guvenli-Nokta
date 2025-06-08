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

public class Sel extends BaseActivity {
    private RecyclerView recyclerView;
    private AfetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afet);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sel Bilgisi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        WebView youtubeWebView = findViewById(R.id.youtubeWebView);
        youtubeWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String videoHtml = "<iframe width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/OKYgIFOiur8\" frameborder=\"0\" " +
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
        cardList.add(new CardModel( "Sellere Karşı Genel Önlemler",
                "Sel felaketine karşı alabileceğiniz temel önlemler."));
        cardList.add(new CardModel( "Sel Sırasında Yapılacaklar",
                "Sel anında can güvenliğiniz için yapmanız gerekenler."));
        cardList.add(new CardModel( "Sel Sırasında Araç Kullanıyorsanız",
                "Sel sırasında araç içindeyseniz dikkat etmeniz gerekenler."));
        cardList.add(new CardModel( "Sel Sonrası Yapılacaklar",
                "Sel felaketi bittikten sonra yapılması gerekenler."));
        return cardList;
    }
    private void openDetailActivity(int position) {
        Intent intent = new Intent(Sel.this, DetailCardActivity.class);
        intent.putExtra("card_title", createCardList().get(position).getTitle());
        intent.putExtra("card_position", 0);

        ArrayList<CardModel> detailCards = new ArrayList<>(createDetailCards(position));
        intent.putExtra("detail_cards", detailCards);

        startActivity(intent);
    }

    private List<CardModel> createDetailCards(int cardType) {
        List<CardModel> detailCards = new ArrayList<>();

        switch (cardType) {
            case 0: // "Sellere Karşı Genel Önlemler"
                detailCards.add(new CardModel( "Erken Uyarı Sistemleri",
                        "Meteorolojik afetler için radar ve uydu verileriyle çalışan erken uyarı birimleri oluşturulmalı, bu birimlerle koordineli kurtarma ekipleri kurulmalıdır."));
                detailCards.add(new CardModel( "Bilgilendirme ve Koordinasyon",
                        "Bölgesel radyolar tehlike anında halkı bilgilendirmeli, yerel yönetimler dere yataklarına yerleşimi önlemeli ve denetlemelidir."));
                detailCards.add(new CardModel( "Dere Yatakları ve Kanallar",
                        "Dere yatakları, drenaj kanalları ve denizle birleşme noktaları düzenli temizlenmeli, yerleşim yerlerinden geçen dere yatakları ıslah edilmelidir."));
                detailCards.add(new CardModel( "Erozyon Kontrolü ve Yapısal Önlemler",
                        "Yeşil alanlar korunarak erozyon önlenmeli, eğimli yamaçlarda teraslama ve ağaçlandırma yapılmalıdır. Çukur alanlarda bodrum katı yapılmamalı, su basman kotu yüksek tutulmalıdır."));
                detailCards.add(new CardModel( "Altyapı ve Bilinçlendirme",
                        "Şehir içlerinde yeterli yağmur suyu kanalı olmalı ve bakımları yapılmalıdır. Sel uyarı işaretleri ve sistemleri öğrenilmeli, konutlar sele karşı sigortalatılmalıdır."));
                break;

            case 1: // "Sel Sırasında Yapılacaklar"
                detailCards.add(new CardModel( "Evde Güvenlik Önlemleri",
                        "Pencere ve kapıları korumak için taşınabilir engeller (kum torbaları vb.) yerleştirin. Suyla sürüklenen enkazın yönünü kum torbalarıyla değiştirin."));
                detailCards.add(new CardModel( "Su Akışına İzin Verme",
                        "Bazı durumlarda, su basıncının yapıya zarar vermemesi için kapıları açarak suyun binanın içinden akmasına izin vermek daha iyi olabilir."));
                detailCards.add(new CardModel( "Güvenli Alanlara Geçiş",
                        "Su yatağı ve çukur bölgeleri hemen terk ederek yüksek ve güvenli bölgelere gidin. Asla sudan karşıdan karşıya geçmeye çalışmayın; su aniden derinleşebilir."));
                detailCards.add(new CardModel( "Elektrik Tehlikesi",
                        "Elektrik kaynaklarından uzak durun, elektrik çarpabilir! Özellikle geceleri sel tehlikelerini görmek zorlaşacağından daha dikkatli olun."));
                detailCards.add(new CardModel( "Sel Sularına Girmeyin!",
                        "Selden ölümlerin çoğu sel sularına girilmesinden kaynaklanır. Ayak bileği seviyesindeki su sizi, diz seviyesindeki su araçları sürükleyebilir. SEL SULARINA GİRMEYİN!"));
                detailCards.add(new CardModel( "Sel Sularının Riskleri",
                        "Sel suları kanalizasyon ve zehirli kimyasallar içerebilir. Çocukların sel suları ile oynamasına izin vermeyin."));
                detailCards.add(new CardModel( "Evden Ayrılırken",
                        "Konutu terk ederken elektrik ve su vanalarını kapatın. Binada gaz sızıntısı şüphesi varsa elektrikli alet ve ışık kullanmayın, pilli fener kullanın."));
                detailCards.add(new CardModel( "Temiz Su İhtiyacı",
                        "Evinizdeki küvet ve bidonları, şebeke suyunun kirlenme ihtimaline karşı temiz su ile doldurun."));
                break;

            case 2: // "Sel Sırasında Araç Kullanıyorsanız"
                detailCards.add(new CardModel( "Su Kaplı Yollardan Uzak Durun",
                        "Asla su ile kaplı yoldan gitmeye çalışmayın. Ani sellerin neden olduğu ölümlerin yarısı araç içindedir. Sel sularının bulunduğu bölgelerde araç kullanmayın."));
                detailCards.add(new CardModel( "Araç Arızalanırsa",
                        "Araçta herhangi bir arıza oluştuysa hemen terk ederek yüksek bir yere çıkın. Araç 60 cm yükseklikteki hareket eden suda kalmışsa, su onu kaldırıp sürükleyebilir."));
                break;

            case 3: // "Sel Sonrası Yapılacaklar"
                detailCards.add(new CardModel( "Güvenli Dönüş",
                        "Otoritelerin 'geri dönün' uyarısı olmadan ve hasar kontrolü yapılmadan binalara kesinlikle girmeyin. Binaları kontrol ederken su geçirmez ayakkabı ve pilli el feneri kullanın."));
                detailCards.add(new CardModel( "Hasar Tespiti ve Sigorta",
                        "Duvarlarda, zeminlerde, pencerelerde hasar olup olmadığını, tavan ve sıva dökülme riskini kontrol edin. Sigorta işlemleri için zarar gören yerlerin fotoğrafını çekin."));
                detailCards.add(new CardModel( "İkincil Tehlikeler",
                        "Sel sonrası yangın çıkabilir. Evde gaz sızıntısı, ıslanmış elektrik aksamı, fırın, ocak olmadığını kontrol edin. Sel sırasında evinizde kalmış yiyecekleri (konserveler dahil) kesinlikle kullanmayın."));
                detailCards.add(new CardModel( "Suların Boşaltılması ve Hijyen",
                        "Konuttaki sel sularını, binanın daha fazla zarar görmemesi için yavaş yavaş boşaltın. Lağım çukurları, mikroplu tanklar ve atık su sistemlerini yetkililere kontrol ettirin."));
                detailCards.add(new CardModel( "Diğer Riskler ve Yardım",
                        "Konuta sel sırasında yılan vb. zararlı hayvanlar girebilir, dikkatli olun. Özel ilgiye ihtiyacı olan afetzedelere (yaşlılar, bebekler, engelliler) yardımcı olun."));
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