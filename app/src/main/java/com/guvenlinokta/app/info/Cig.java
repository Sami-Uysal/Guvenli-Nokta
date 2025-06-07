// Cig.java
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

public class Cig extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AfetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afet);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Çığ Bilgisi"); // Başlık güncellendi
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        WebView youtubeWebView = findViewById(R.id.youtubeWebView);
        youtubeWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String videoHtml = "<iframe width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/1f6-6j7FlAo\" frameborder=\"0\" " +
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
        cardList.add(new CardModel("Çığ Öncesinde Alınacak Önlemler",
                "Çığ riskine karşı hazırlıklı olmak ve korunmak için yapılması gerekenler."));
        cardList.add(new CardModel("Çığ Oluşabilecek Alanlarda Dikkat Edilmesi Gerekenler",
                "Riskli bölgelerde bulunuyorsanız dikkat etmeniz gerekenler."));
        cardList.add(new CardModel("Çığ Sırasında Yapılacaklar",
                "Çığ anında hayatta kalma şansınızı artıracak davranışlar."));
        cardList.add(new CardModel("Çığ Başladığında Araç İçindeyseniz",
                "Araç içinde çığa yakalanırsanız yapmanız gerekenler."));
        cardList.add(new CardModel("Çığa Maruz Kalırsanız",
                "Doğrudan çığ altında kalırsanız yapmanız gerekenler."));
        cardList.add(new CardModel("Çığ Sonrasında Yapılacaklar",
                "Çığ felaketi sonrası dikkat edilmesi ve yapılması gerekenler."));
        return cardList;
    }

    private void openDetailActivity(int position) {
        Intent intent = new Intent(Cig.this, DetailCardActivity.class);
        intent.putExtra("card_title", createCardList().get(position).getTitle());
        intent.putExtra("card_position", 0); // Detay kartlarında ilk gösterilecek kart

        ArrayList<CardModel> detailCards = new ArrayList<>(createDetailCards(position));
        intent.putExtra("detail_cards", detailCards);

        startActivity(intent);
    }

    private List<CardModel> createDetailCards(int cardType) {
        List<CardModel> detailCards = new ArrayList<>();

        switch (cardType) {
            case 0: // "Çığ Öncesinde Alınacak Önlemler"
                detailCards.add(new CardModel("Eğitim Alın",
                        "Çığ riskine karşı nasıl hazırlanacağınızı ve korunacağınızı öğrenebileceğiniz eğitim programlarına katılın. İlk yardım vb. tamamlayıcı eğitimleri de alın."));
                detailCards.add(new CardModel("Yerleşim Yeri Seçimi",
                        "Yeni yerleşim yeri olarak, çığ riski taşıyan bölgeleri seçmeyin."));
                detailCards.add(new CardModel("Sigorta Yaptırın",
                        "Mevcut yapılarınızı sigortalatın."));
                detailCards.add(new CardModel("Doğal Örtüyü Koruyun",
                        "Çığ tehlikesinin artmasını engellemek için, yamaçlardaki ağaçları, bitki örtüsünü ve ormanları koruyun."));
                detailCards.add(new CardModel("Bilgi Takibi",
                        "Kar yağan aylarda hava ve yol durumu raporlarını dikkatlice izleyin."));
                detailCards.add(new CardModel("Aile Afet Planı",
                        "Aile Afet Planınızı hazırlarken çığ riskini göz önünde bulundurun."));
                break;

            case 1: // "Çığ Oluşabilecek Alanlarda Dikkat Edilmesi Gerekenler"
                detailCards.add(new CardModel("Riskli Alanlardan Sakının",
                        "Rüzgar altı alanlardan, kornişlerin altındaki yamaçlardan ve özellikle içbükey (konveks) profilli ve rüzgarla sürüklenme sonucu oluşmuş kalın depolama alanlarından sakınılmalıdır."));
                detailCards.add(new CardModel("Güvenli Geçiş",
                        "Yüksek riskli bölgeleri geçerken grubun emniyetli yerde beklemesi ve birer birer geçilmesi doğrudur."));
                detailCards.add(new CardModel("Yoğun Kar Yağışı ve Rüzgar",
                        "Yoğun kar yağışı ve şiddetli rüzgarın uzaması yüksek çığ riskini doğurduğundan tehlikeli alanlardan uzak durulmalıdır."));
                detailCards.add(new CardModel("Eğimli Yamaçlar",
                        "En tehlikeli çığların 30°- 40° eğimli yamaçlarda oluşmasından dolayı bu tür yamaçlara daha fazla dikkat edilmelidir."));
                detailCards.add(new CardModel("Uyarı Sesleri",
                        "Eğer arazide iken çökme sesi, kırılma ve oturma sesi benzeri sesler duyuyorsanız, çığ oluşumu anına çok yakınsınız demektir."));
                break;

            case 2: // "Çığ Sırasında Yapılacaklar"
                detailCards.add(new CardModel("Soğukkanlı Olun",
                        "Soğukkanlılığınızı muhafaza etmeye çalışın."));
                detailCards.add(new CardModel("Riskli Alanı Terk Edin",
                        "Çığın büyüklüğüne, hızına, patikanın genişliğine ve etrafta bulunan araçlara bakarak en kısa sürede riskli alanı terk edin ve daha güvenli yerlere ulaşmaya çalışın."));
                detailCards.add(new CardModel("Sırt Çantasını Çıkarmayın",
                        "Sırt çantası taşıyanların çığın topuğu civarında yüzeyde kalma şansı daha fazladır; bu nedenle sırt çantanızı çıkarmayın."));
                detailCards.add(new CardModel("Kenar Kısımlara Ulaşın",
                        "Çığın daha yavaş, yüksekliğinin az olduğu kenar kısımlarına ulaşmaya çalışın."));
                detailCards.add(new CardModel("Çevrenizdekileri Uyarın",
                        "Bağırarak veya başka ses kaynakları (korna, çan, ıslık vb.) kullanarak çevrenizdekileri uyarmaya çalışın."));
                detailCards.add(new CardModel("Kayak Yapıyorsanız",
                        "Kayak yaparken çığın önünde kalırsanız çığın rotası dışına doğru kaymaya çalışın. Eğer kayak yaparken çığa yakalanmak kesin ise kayak sopalarını ve kayakları çıkarıp atın, sabit ağaç gibi bir cisme tutunmaya çalışın."));
                break;

            case 3: // "Çığ Başladığında Araç İçindeyseniz"
                detailCards.add(new CardModel("Motoru Durdurun ve Işıkları Söndürün",
                        "Motoru durdurun ve ışıkları söndürün."));
                detailCards.add(new CardModel("Oksijeni Koruyun",
                        "Araçtaki oksijen miktarını korumak için sigara içmeyin, ateş yakmayın."));
                detailCards.add(new CardModel("İletişim Kurun",
                        "Telsiz varsa çağrı yapın ve telsizi alıcı konumunda sürekli açık tutun."));
                break;

            case 4: // "Çığa Maruz Kalırsanız"
                detailCards.add(new CardModel("Yüzeyde Kalmaya Çalışın",
                        "Yerden destek alarak ve geniş yüzme hareketleri yaparak akan karın üstünde ve mümkünse kenarında kalmaya çalışın."));
                detailCards.add(new CardModel("Nefesinizi Tutun ve Ağzınızı Kapatın",
                        "Ağzınızı sıkıca kapatın; kafanız kar altında kaldığı anda mümkünse uzun süre nefesinizi tutmaya çalışın."));
                detailCards.add(new CardModel("Oturma Pozisyonu Alın",
                        "Akışa kapılırsanız bacaklarınızı ve kollarınızı birbirine yapıştırarak oturma pozisyonu alın."));
                detailCards.add(new CardModel("Kalkmaya Çalışın",
                        "Mümkünse çığ durmadan kısa süre önce bacaklarınızla yeri sertçe iterek kalkmaya çalışın."));
                detailCards.add(new CardModel("Nefes Boşluğu Oluşturun",
                        "Mümkünse çığ durmadan önce mutlaka bir elinizi yüzün önünde (ağzınızı ve burnunuzu kapatacak şekilde), diğer elinizi de başınızın üzerinde (yüzeye doğru uzatarak) tutun ve kar altında kaldığınız zaman boyunca hayati önem taşıyacak olan nefes boşluğunu genişletin. Başınızı sağa sola çevirerek boşluğu büyütmeye çalışın."));
                break;

            case 5: // "Çığ Sonrasında Yapılacaklar"
                detailCards.add(new CardModel("Sesinizi Duyurun",
                        "Karda ses iletimi az olmasına rağmen eğer yüzeye yakın olduğunuzu hissediyorsanız bağırın."));
                detailCards.add(new CardModel("Enerjinizi Koruyun",
                        "Enerjinizi dikkatli kullanın."));
                detailCards.add(new CardModel("Araç İçindeyseniz (Sonrası)",
                        "Dışarıya ses (korna) ve ışık verecek herhangi bir alet (fener vb.) kullanın. Araçta bir çubuk veya benzeri bir alet varsa kar içinde yukarı doğru batırın; kurtarmaya gelecek olanların çubuğu görmelerini sağlayın. Aracı çevreleyen karı kazmaya çalışın; ancak kazarken kendinizi güvende hissetmiyorsanız emniyetiniz için araç içinde kalın."));
                detailCards.add(new CardModel("Yetkililere Bildirin",
                        "Mümkünse 155 Polis ve 156 Jandarma hatlarını arayarak durumu bildirin."));
                detailCards.add(new CardModel("İlk Yardım",
                        "İlk yardım eğitiminiz yoksa ve zorunlu olmadıkça, çığdan kurtarılan kişileri hareket ettirmeyin. Çığdan etkilenen kişilerin öncelikle üzerini örtün; doğrudan sıcak bir ortama kesinlikle sokmayın."));
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