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

public class Deprem extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AfetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afet);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Deprem Bilgisi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        WebView youtubeWebView = findViewById(R.id.youtubeWebView);
        youtubeWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String videoHtml = "<iframe width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/Xkm1J0slZmQ\" frameborder=\"0\" " +
                "allowfullscreen></iframe>";

        youtubeWebView.loadData(videoHtml, "text/html", "utf-8");

        recyclerView = findViewById(R.id.recyclerViewDeprem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<CardModel> cardList = createCardList();
        adapter = new AfetAdapter(cardList, position -> {
            Intent intent = new Intent(Deprem.this, DetailCardActivity.class);
            intent.putExtra("card_position", 0);
            intent.putExtra("card_title", cardList.get(position).getTitle());
            intent.putExtra("card_type", position);
            openDetailActivity(position);
        });

        recyclerView.setAdapter(adapter);
    }

    private List<CardModel> createCardList() {
        List<CardModel> cardList = new ArrayList<>();
        cardList.add(new CardModel( "Bina içindeyseniz",
                "Deprem sırasında bina içindeyseniz yapmanız gerekenler"));
        cardList.add(new CardModel( "Bina dışında açık alandaysanız",
                "Açık alanda depreme yakalandığınızda yapmanız gerekenler"));
        cardList.add(new CardModel( "Araç kullanıyorsanız",
                "Araç kullanırken deprem olursa yapmanız gerekenler"));
        cardList.add(new CardModel( "Toplu taşıma araçlarındaysanız",
                "Toplu taşıma araçlarında depreme yakalanırsanız yapmanız gerekenler"));
        cardList.add(new CardModel( "Enkaz altında kaldıysanız",
                "Enkaz altında kaldığınızda hayatta kalmak için yapmanız gerekenler"));
        return cardList;
    }
    private void openDetailActivity(int position) {
        Intent intent = new Intent(Deprem.this, DetailCardActivity.class);
        intent.putExtra("card_position", 0);
        intent.putExtra("card_title", createCardList().get(position).getTitle());

        ArrayList<CardModel> detailCards = new ArrayList<>(createDetailCards(position));
        intent.putExtra("detail_cards", detailCards);

        startActivity(intent);
    }

    private List<CardModel> createDetailCards(int cardType) {
        List<CardModel> detailCards = new ArrayList<>();

        switch (cardType) {
            case 0: // "Bina içindeyseniz"
                detailCards.add(new CardModel( "Kesinlikle panik yapmayınız",
                        "Deprem anında panik yapmak karar verme yeteneğinizi zayıflatır. Sakin kalarak kendiniz ve etrafınızdakiler için doğru kararlar verebilirsiniz."));

                detailCards.add(new CardModel( "Sabitlenmemiş eşyalardan uzak durun",
                        "Sabitlenmemiş dolap, raf, pencere vb. eşyalardan uzak durunuz. Bu eşyalar deprem sırasında devrilebilir veya düşebilir ve yaralanmalara sebep olabilir."));

                detailCards.add(new CardModel( "Hayat üçgeni oluşturun",
                        "Varsa sağlam sandalyelerle desteklenmiş masa altına veya dolgun ve hacimli koltuk, kanepe, içi dolu sandık gibi koruma sağlayabilecek eşya yanına çömelerek veya uzanarak kendinize hayat üçgeni oluşturun. Başınızı iki elinizin arasına alarak veya bir koruyucu (yastık, kitap vb) malzeme ile koruyun."));

                detailCards.add(new CardModel( "ÇÖK-KAPAN-TUTUN",
                        "Güvenli bir yer bulup, diz üstü ÇÖK, baş ve enseyi koruyacak şekilde KAPAN, düşmemek için sabit bir yere TUTUN hareketini yapın. Deprem sırasında sarsıntı durana kadar olduğunuz yerde kalın."));

                detailCards.add(new CardModel( "Tehlikeli yerlerden uzak durun",
                        "Cam, pencere, dışarıya bakan duvar ve kapılardan, aydınlatma tesisatı veya armatürü gibi üzerinize düşecek her tür eşyadan uzak durun."));

                detailCards.add(new CardModel( "Yataktaysanız",
                        "Sarsıntı başladığında yataktaysanız orada kalın. Üzerinize düşecek ağır bir eşya yoksa bir yastıkla başınızı koruyun; varsa en yakındaki güvenli alana geçin."));

                detailCards.add(new CardModel( "Kapıları dikkatli kullanın",
                        "Size yakın çok sağlam ve yüke dayanıklı bildiğiniz bir kapı değilse, kapıyı kullanmayın. Çoğu iç mekan kapısı basitçe inşa edilmiştir ve sizi korumaktan uzaktır."));

                detailCards.add(new CardModel( "Bina içinde kalın",
                        "Merdivenlere ya da çıkışlara doğru koşmayın. Sarsıntı bitene kadar içeride kalın, ancak sarsıntı bitince dışarı çıkmak güvenlidir. Sarsıntı sırasında binayı terk etmeye çalışmayın."));

                detailCards.add(new CardModel( "Araştırma sonuçları",
                        "Araştırmalar, çoğu yaralanmanın bina içinde hareket ederken veya dışarı çıkmaya çalışırken oluştuğunu göstermektedir."));

                detailCards.add(new CardModel( "Balkona çıkmayın",
                        "Deprem sırasında balkona çıkmak tehlikelidir. Balkon çökebilir veya balkon korkulukları zarar görebilir."));

                detailCards.add(new CardModel( "Atlamayın",
                        "Balkonlardan ya da pencerelerden atlamayın. Bu durum ciddi yaralanmalara veya ölüme neden olabilir."));

                detailCards.add(new CardModel( "Asansör kullanmayın",
                        "Deprem sırasında asansörler durabilir, elektrik kesilebilir ve asansörde mahsur kalabilirsiniz."));

                detailCards.add(new CardModel( "Acil durumlar",
                        "ACİL DURUMLARI ve YANGINLARI bildirmek dışında telefonları KULLANMAYIN. Kibrit ve çakmak YAKMAYIN, elektrik düğmelerine DOKUNMAYIN."));

                detailCards.add(new CardModel( "Elektrik ve alarm sistemleri",
                        "Elektriklerin kesilebileceğinin; yangın alarmlarının çalışabileceğinin ve yangın söndürme sistemlerinin devreye girebileceğinin farkında olun."));

                detailCards.add(new CardModel( "Tekerlekli sandalyedeyseniz",
                        "Tekerlekli sandalyede iseniz tekerlekleri kilitleyerek başınızı ve boynunuzu korumaya alın."));

                detailCards.add(new CardModel( "Tehlikeli alanlarda",
                        "Mutfak, imalathane, laboratuvar gibi iş aletlerinin bulunduğu yerlerde; ocak, fırın vb. cihazları kapatın. Dökülebilecek malzeme ve maddelerden uzaklaşın."));

                detailCards.add(new CardModel( "Sarsıntı sonrası",
                        "Sarsıntı geçtikten sonra elektrik, gaz ve su vanalarını kapatın; soba ve ısıtıcıları söndürün. Diğer güvenlik önlemlerini alın; daha önceden hazırlanmış acil durum çantası ile gerekli olan eşya ve malzemeyi yanınıza alarak binayı daha önce tespit ettiğiniz yoldan derhal terk edip toplanma bölgesine gidin."));

                detailCards.add(new CardModel( "Artçı depremler",
                        "Her büyük depremden sonra mutlaka artçı deprem olur. Artçı depremler zaman içerisinde seyrekleşir ve büyüklükleri azalır. Artçı depremler hasarlı binalarda zarara yol açabilir. Bu nedenle sarsıntılar tamamen bitene kadar hasarlı binalara girmeyin."));

                detailCards.add(new CardModel( "Artçı depremler sırasında",
                        "Artçı depremler sırasında da ana depremde yapmanız gerekenleri yapın."));
                break;

            case 1: // "Bina dışında açık alandaysanız"
                detailCards.add(new CardModel( "Açık alanda kalın",
                        "Bina, ağaç, direk ve enerji hatlarından uzak durun. Açık alanda çömelerek veya dizler üzerine oturarak kendinizi koruyun."));

                detailCards.add(new CardModel( "Enerji hatlarından uzaklaşın",
                        "Kopan elektrik telleri tehlike yaratabilir, bunlardan en az 10 metre uzakta durun."));

                detailCards.add(new CardModel( "Deniz kıyısından uzaklaşın",
                        "Deniz kıyısında iseniz, tsunami tehlikesi olabileceğinden kıyıdan uzaklaşın ve yüksek yerlere çıkın."));
                break;

            case 2: // "Araç kullanıyorsanız"
                detailCards.add(new CardModel("Güvenli bir şekilde durun",
                        "Sarsıntı sırasında aracınızı güvenli bir yere çekin ve durun. Aracınızı köprü, viyadük, tünel, elektrik direkleri ve enerji hatlarından uzak bir yere park edin."));

                detailCards.add(new CardModel( "Araçta kalın",
                        "Pencereler kapalı ve emniyet kemeriniz bağlı olarak araç içinde kalın. Sarsıntı bitene kadar aracı terk etmeyin."));

                detailCards.add(new CardModel("Tehlikelerden uzak durun",
                        "Köprülerden, alt ve üst geçitlerden, bina yakınlarından ve elektrik hatlarından uzak durun."));
                break;

            case 3: // "Toplu taşıma araçlarındaysanız"
                detailCards.add(new CardModel( "Panik yapmayın",
                        "Güvenli bir şekilde tutunun ve sarsıntı geçene kadar sakin kalın. Diğer yolcuları da sakinleştirmeye çalışın."));

                detailCards.add(new CardModel( "Görevlileri dinleyin",
                        "Toplu taşıma aracında görevlilerin yönlendirmelerini takip edin."));
                break;

            case 4: // "Enkaz altında kaldıysanız"
                detailCards.add(new CardModel( "Enerjinizi koruyun",
                        "Gereksiz yere bağırmayın, enerjinizi verimli kullanın. Düzenli olarak ses çıkararak kurtarma ekiplerinin dikkatini çekmeye çalışın."));

                detailCards.add(new CardModel( "Çakmak kullanmayın",
                        "Gaz kaçağı olabilir, çakmak ve kibrit yakmayın."));

                detailCards.add(new CardModel( "Mümkünse hareket edin",
                        "Etrafınızda hareket edebileceğiniz bir alan varsa, keskin yüzeylerden sakınarak kendinizi güvenli bir yere çekmeye çalışın."));

                detailCards.add(new CardModel( "Su ve besin",
                        "Varsa su ve yiyecek tüketiminizi minimum düzeyde tutun. Mümkünse ağzınızı ve burnunuzu bir bezle kapatarak toz solumayı engelleyin."));
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