package com.guvenlinokta.app.ilkyardim;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guvenlinokta.app.R;
import com.guvenlinokta.app.ilkyardim.FirstAidTopic;
import com.guvenlinokta.app.ui.BaseActivity;

import java.util.Locale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstAidActivity extends BaseActivity {

    private EditText editTextSearch;
    private RecyclerView recyclerViewTopics;
    private TextView textViewNoResults;

    private FirstAidTopicAdapter topicAdapter;
    private List<FirstAidTopic> allFirstAidTopics = new ArrayList<>();
    private List<FirstAidTopic> displayedFirstAidTopics = new ArrayList<>();
    private Map<String, FirstAidTopic> firstAidTopicsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);

        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerViewTopics = findViewById(R.id.recyclerViewFirstAidTopics);
        textViewNoResults = findViewById(R.id.textViewNoResults);

        recyclerViewTopics.setLayoutManager(new LinearLayoutManager(this));
        topicAdapter = new FirstAidTopicAdapter(displayedFirstAidTopics, this::openDetailActivity);
        recyclerViewTopics.setAdapter(topicAdapter);

        loadFirstAidData();
        setupSearchListener();
        filterTopics("");
    }

    private void loadFirstAidData() {
        firstAidTopicsMap.put("kemik_kiriklari", new FirstAidTopic(
                "Kırık, Çıkık ve Burkulmalarda İlkyardım",
                "Kırık Belirtileri:\n- Hareketle artan ağrı\n- Şekil bozukluğu\n- Hareket kaybı\n- Ödem, kanama ve morarma\n\n" +
                        "Çıkık Belirtileri:\n- Eklemde yoğun ağrı ve şişlik\n- Şekil bozukluğu\n- Hareket kısıtlılığı veya kaybı\n\n" +
                        "Burkulma Belirtileri:\n- Eklemin etkilenen bölgesinde ağrı, şişlik, kızarıklık ve morarma\n- İşlev kaybı\n\n" +
                        "Genel İlkyardım:\n1. Hasta/yaralıyı sakinleştirin, hareket ettirmeyin.\n" +
                        "2. Kırık/çıkık/burkulma olan bölgeyi tespit malzemeleri (örn: tahta, karton, üçgen sargı) ile sabitleyin. Eklem olduğu gibi sabitlenir.\n" +
                        "3. Açık kırık varsa, yara üzerine temiz bir bez kapatın, kanama kontrolü yapın.\n" +
                        "4. Bölgeye soğuk uygulama yapın (buz torbası vb.).\n" +
                        "5. Bölgeyi kalp seviyesinden yukarıda tutun.\n" +
                        "6. Tıbbi yardım (112) isteyin."
        ));
        firstAidTopicsMap.put("yaniklar", new FirstAidTopic(
                "Yanık, Donma ve Sıcak Çarpmasında İlkyardım",
                "Yanık Türleri:\n- 1. Derece: Deride kızarıklık, ağrı, ödem. Örnek: güneş yanığı.\n- 2. Derece: Deride içi su dolu kabarcıklar (bül), ağrı. Kabarcıklar patlatılmamalıdır.\n- 3. Derece: Derinin tüm katmanları etkilenir, beyaz ve kara yaradan, sinirler zarar gördüğü için ağrı olmayabilir.\n\n" +
                        "Yanıklarda İlkyardım:\n1. Kişi hala yanıyorsa paniğe engel olunur, koşması engellenir.\n" +
                        "2. Yanık bölgeyi hemen en az 20 dakika soğuk su altında (çeşme suyu) tutun. Buz doğrudan temas ettirilmemelidir.\n" +
                        "3. Ödem oluşabileceği düşünülerek yüzük, bilezik, saat gibi takılar çıkarılmalıdır.\n" +
                        "4. Yanık üzerine diş macunu, yoğurt, salça gibi maddeler sürmeyin.\n" +
                        "5. Yanık bölgedeki giysiler dikkatlice çıkarılır. Yapışmışsa zorlanmaz, etrafından kesilir.\n" +
                        "6. Su toplamış kabarcıkları patlatmayın.\n" +
                        "7. Yanık üzeri temiz ve nemli bir bezle örtülür.\n" +
                        "8. Geniş yanıklarda ve 2./3. derece yanıklarda, elektrik ve kimyasal yanıklarda tıbbi yardım (112) isteyin.\n\n" +
                        "Donmada İlkyardım:\n- Hastayı ılık bir ortama alın, ıslak giysilerini çıkarın.\n- Donmuş bölgeyi ovmayın, yavaşça ısıtın (ılık suya batırma veya ılık bezler).\n- Sıcak içecekler verin (bilinç yerindeyse).\n- Tıbbi yardım (112) isteyin.\n\n" +
                        "Sıcak Çarpmasında İlkyardım:\n- Hastayı serin ve havadar bir yere alın.\n- Giysilerini gevşetin.\n- Vücut ısısını düşürmek için soğuk, nemli bezler uygulayın.\n- Bilinç yerindeyse ve bulantı yoksa tuzlu ayran gibi sıvılar verin.\n- Tıbbi yardım (112) isteyin."
        ));
        firstAidTopicsMap.put("kanamalar", new FirstAidTopic(
                "Kanamalarda İlkyardım",
                "Dış Kanamalarda İlkyardım:\n1. Yaralı bölgeyi kalp seviyesinden yukarıda tutun.\n" +
                        "2. Yara üzerine temiz bir bezle veya elle doğrudan baskı uygulayın (5-10 dakika).\n" +
                        "3. Kanama durmazsa, ilk bez kaldırılmadan üzerine ikinci bir bez koyarak baskıyı artırın.\n" +
                        "4. Gerekirse bandaj ile sararak baskıyı devam ettirin.\n" +
                        "5. Kanayan bölgeye en yakın basınç noktasına baskı uygulayın.\n" +
                        "6. Şok belirtileri varsa (soluk cilt, hızlı ve zayıf nabız, terleme, huzursuzluk) hastanın ayaklarını 30 cm yukarı kaldırın, üzerini örtün.\n" +
                        "7. Tıbbi yardım (112) isteyin.\n\n" +
                        "İç Kanamalarda İlkyardım (Belirtiler: karın ağrısı, şişlik, morarma, bulantı, kusma, baş dönmesi, soğuk terleme):\n1. Hastanın bilincini ve ABC'sini değerlendirin.\n2. Üzerini örterek şok pozisyonu verin (ayaklar 30 cm yukarıda).\n3. Ağızdan hiçbir şey vermeyin.\n4. Tıbbi yardım (112) isteyin."
        ));
        firstAidTopicsMap.put("yaralanmalar_genel", new FirstAidTopic(
                "Yaralanmalarda İlkyardım",
                "Yaralanma Çeşitleri (Kesik, Ezik, Delici, Parçalı Yaralar vb.)\n\n" +
                        "Genel İlkyardım Adımları:\n1. Yaşam bulguları değerlendirilir (ABC).\n" +
                        "2. Yara yeri değerlendirilir (Oluş şekli, Süresi, Yabancı cisim varlığı, Kanama vb.).\n" +
                        "3. Kanama varsa durdurulur (yukarıdaki 'Kanamalarda İlkyardım' bölümüne bakınız).\n" +
                        "4. Yara üzeri temiz bir bezle (mümkünse steril) kapatılır.\n" +
                        "5. Yaradaki yabancı cisimlere dokunulmamalıdır, çıkarılmaya çalışılmamalıdır. Cisim sabitlenir.\n" +
                        "6. Ciddi yaralanmalarda (derin, kenarları ayrık, yoğun kanamalı, yabancı cisim batmış, hayvan ısırığı, enfeksiyon belirtili) tıbbi yardım (112) istenir.\n" +
                        "7. Tetanos aşısı konusunda uyarıda bulunulur."
        ));
        firstAidTopicsMap.put("bilinc_bozukluklari", new FirstAidTopic(
                "Bilinç Bozukluklarında İlkyardım",
                "Bilinç Kaybı Nedenleri: Bayılma, kafa travması, şeker hastalığı, zehirlenme, kalp krizi vb.\n\n" +
                        "Bayılmada İlkyardım:\n1. Kişi başının döneceğini hissederse sırt üstü yatırılır, ayakları 30 cm yukarı kaldırılır.\n2. Sıkan giysiler gevşetilir.\n3. Ortam havalandırılır.\n4. Kusma varsa yan pozisyona getirilir.\n5. Kendine geldikten sonra ani kalkmasına izin verilmez.\n\n" +
                        "Bilinç Kapalıysa (Solunum Var):\n1. Koma pozisyonu (yan yatış) verilir.\n2. Solunum düzenli olarak kontrol edilir.\n3. Tıbbi yardım (112) istenir.\n\n" +
                        "Bilinç Kapalıysa (Solunum Yok):\n1. Derhal Temel Yaşam Desteği'ne (Kalp Masajı ve Suni Solunum) başlanır.\n2. Tıbbi yardım (112) istenir."
        ));
        firstAidTopicsMap.put("zehirlenmeler", new FirstAidTopic(
                "Zehirlenmelerde İlkyardım",
                "Zehirlenme Yolları: Sindirim, Solunum, Cilt.\n\n" +
                        "Genel İlkyardım:\n1. Zehirlenmeye neden olan maddeyi ve maruziyet şeklini belirlemeye çalışın.\n2. Yaşam bulgularını (ABC) kontrol edin.\n3. Ulusal Zehir Danışma Merkezi (114) veya 112'yi arayarak bilgi alın ve talimatlara uyun.\n4. Hastayı kusturmaya çalışmayın (doktor tavsiyesi olmadıkça).\n\n" +
                        "Sindirim Yoluyla Zehirlenmeler:\n- Bilinç yerindeyse ağzı su ile çalkalatın.\n- Özellikle yakıcı madde alındıysa kesinlikle kusturulmaz.\n\n" +
                        "Solunum Yoluyla Zehirlenmeler:\n- Hastayı derhal temiz havaya çıkarın.\n- Ortamı havalandırın.\n\n" +
                        "Cilt Yoluyla Zehirlenmeler:\n- Zehirli madde bulaşmış giysileri çıkarın.\n- Bulaşan bölgeyi bol suyla (15-20 dk) yıkayın."
        ));
        firstAidTopicsMap.put("hayvan_isirmalari", new FirstAidTopic(
                "Hayvan Isırmalarında İlkyardım",
                "Kedi/Köpek Isırıklarında İlkyardım:\n1. Yara 5 dakika sabun ve soğuk suyla yıkanır.\n2. Temiz bir bezle kapatılır.\n3. Kanama varsa kontrol altına alınır.\n4. Kuduz ve tetanos riski için sağlık kuruluşuna başvurulur.\n\n" +
                        "Arı Sokmasında İlkyardım:\n1. Arının iğnesi görünüyorsa cımbızla veya tırnakla kazıyarak çıkarılır.\n2. Soğuk uygulama yapılır.\n3. Alerji (nefes darlığı, yaygın kızarıklık, şişme) belirtileri varsa derhal 112 aranır.\n\n" +
                        "Yılan Sokmasında İlkyardım:\n1. Hasta sakinleştirilir, hareket etmesi engellenir.\n2. Isırılan bölge kalp seviyesinin altında tutulur.\n3. Yara yıkanır, emilmez, kesilmez, turnike uygulanmaz.\n4. En yakın sağlık kuruluşuna ulaştırılır.\n\n" +
                        "Akrep Sokmasında İlkyardım:\n1. Isırılan bölge hareket ettirilmez.\n2. Soğuk uygulama yapılır.\n3. Kan dolaşımını engelleyecek sıkı bandaj uygulanmaz.\n4. En yakın sağlık kuruluşuna başvurulur."
        ));
        firstAidTopicsMap.put("yabanci_cisim_kacmasi", new FirstAidTopic(
                "Yabancı Cisim Kaçmasında İlkyardım",
                "Göze Yabancı Cisim Kaçması:\n- Göz ovuşturulmaz.\n- Toz veya kirpik gibi küçük cisimler için göz bol suyla yıkanır veya nemli temiz bir bezle çıkarılmaya çalışılır.\n- Cisim batmışsa veya çıkmıyorsa, göz kapatılır ve sağlık kuruluşuna gidilir.\n\n" +
                        "Kulağa Yabancı Cisim Kaçması:\n- Cisim çıkarılmaya çalışılmaz, sivri alet sokulmaz.\n- Sağlık kuruluşuna başvurulur.\n\n" +
                        "Buruna Yabancı Cisim Kaçması:\n- Diğer burun deliği kapatılarak sümkürülmeye çalışılır.\n- Çıkmazsa sağlık kuruluşuna gidilir.\n\n" +
                        "Solunum Yoluna Yabancı Cisim Kaçması (Boğulma - Heimlich Manevrası):\n- Kişi öksürebiliyorsa öksürmeye teşvik edilir.\n- Tam Tıkanma (Kişi konuşamaz, öksüremez, morarır):\n  - Bilinçli Yetişkin/Çocuk: Heimlich manevrası uygulanır (Arkasından sarılıp, göğüs kemiğinin alt kısmına yumrukla 5 kez içe ve yukarı doğru baskı).\n  - Bilinçsiz: Temel Yaşam Desteği'ne başlanır, ağız içi kontrol edilir.\n  - Bebekler (1 yaş altı): Sırta 5 vuruş, göğse 5 bası şeklinde uygulanır."
        ));
        firstAidTopicsMap.put("bogulmalar", new FirstAidTopic(
                "Boğulmalarda İlkyardım",
                "Suda Boğulmalarda İlkyardım:\n1. Güvenliğinizden emin olmadan suya girmeyin. Uzun bir cisim (ip, sopa) uzatarak veya can simidi atarak yardım edin.\n2. Sudan çıkarılan kişinin bilinci ve solunumu kontrol edilir.\n3. Bilinç kapalı, solunum yoksa Temel Yaşam Desteği'ne (Kalp Masajı ve Suni Solunum) başlanır.\n4. Islak giysileri çıkarılır, üzeri örtülür.\n5. Tıbbi yardım (112) istenir.\n\n" +
                        "Solunum Yolu Tıkanıklığına Bağlı Boğulmalar:\n- 'Yabancı Cisim Kaçmasında İlkyardım' başlığındaki Heimlich Manevrası adımlarına bakınız."
        ));
        firstAidTopicsMap.put("hasta_yarali_tasima", new FirstAidTopic(
                "Hasta/Yaralı Taşıma Teknikleri",
                "Genel Taşıma Kuralları:\n- Hasta/yaralıya yakın mesafede çalışılmalı.\n- Sırt düz tutulmalı, dizler bükülerek ağırlık kaldırılmalı.\n- Ani hareketlerden kaçınılmalı.\n- Mümkünse en az iki kişi ile taşınmalı.\n- Baş-boyun-gövde ekseni korunmalı, özellikle omurga yaralanması şüphesi varsa.\n\n" +
                        "Acil Taşıma Teknikleri (Tehlikeli ortamdan uzaklaştırma):\n- Sürükleme Yöntemleri (Ayak bileklerinden, koltuk altından, battaniye ile).\n- Rentek Manevrası (Araç içinden çıkarma).\n\n" +
                        "Kısa Mesafede Süratli Taşıma Teknikleri:\n- Kucakta Taşıma.\n- İtfaiyeci Yöntemi (Omuzda taşıma).\n- Sırtta Taşıma.\n\n" +
                        "Sedye İle Taşıma Teknikleri:\n- Kaşık Tekniği.\n- Köprü Tekniği.\n- Karşılıklı Durarak Kaldırma."
        ));
        firstAidTopicsMap.put("genel_ilkyardim_bilgileri", new FirstAidTopic(
                "Genel İlkyardım Bilgileri",
                "İlkyardım Nedir?:\n- Herhangi bir kaza veya yaşamı tehlikeye düşüren durumda, sağlık görevlileri gelene kadar hayatın kurtarılması ya da durumun kötüleşmesini önlemek amacıyla olay yerinde, tıbbi araç gereç olmadan, mevcut imkanlarla yapılan ilaçsız uygulamalardır.\n\n" +
                        "İlkyardımın Öncelikli Amaçları:\n- Hayati tehlikeyi ortadan kaldırmak.\n- Yaşamsal fonksiyonların sürdürülmesini sağlamak.\n- Hasta/yaralının durumunun kötüleşmesini önlemek.\n- İyileşmeyi kolaylaştırmak.\n\n" +
                        "İlkyardımın Temel Uygulamaları (KBK):\n- Koruma: Olay yerinde olası tehlikeleri belirleyerek güvenli bir çevre oluşturmak.\n- Bildirme: En hızlı şekilde 112'yi aramak.\n- Kurtarma (Müdahale): Olay yerinde hasta/yaralılara hızlı ancak sakin bir şekilde müdahale etmek."
        ));

        firstAidTopicsMap.put("olay_yeri_degerlendirme", new FirstAidTopic(
                "Hasta/Yaralının ve Olay Yerinin Değerlendirilmesi",
                "Olay Yerinin Değerlendirilmesi:\n- Tekrar kaza olma riskini ortadan kaldırmak.\n- Olay yerindeki hasta/yaralı sayısını ve türlerini belirlemek.\n- Olay yerini hızla değerlendirerek yapılacak müdahaleleri planlamak.\n\n" +
                        "Hasta/Yaralının Değerlendirilmesi (Birinci Değerlendirme - ABC):\n- Bilinç durumunun değerlendirilmesi.\n- (A) Hava yolu açıklığının değerlendirilmesi.\n- (B) Solunumun değerlendirilmesi (Bak-Dinle-Hisset).\n- (C) Dolaşımın değerlendirilmesi (Nabız, kanama kontrolü).\n\n" +
                        "İkinci Değerlendirme:\n- Baştan aşağı kontrol yapılarak kırık, kanama, yara vb. olup olmadığının kontrol edilmesi."
        ));

        firstAidTopicsMap.put("temel_yasam_destegi", new FirstAidTopic(
                "Temel Yaşam Desteği (TYD)",
                "Tanım:\n- Solunumu veya kalbi durmuş kişiye yapay solunum ile akciğerlerine oksijen gitmesini, dış kalp masajı ile de kalpten kan pompalanmasını sağlamak üzere yapılan ilaçsız müdahalelerdir.\n\n" +
                        "Yetişkinlerde TYD:\n1. Güvenliği sağlayın, bilinç kontrolü yapın.\n2. 112'yi aratın.\n3. Ağız içini kontrol edin, hava yolunu açın (Baş-Geri Çene-Yukarı).\n4. Solunumu kontrol edin (Bak-Dinle-Hisset, 10 sn).\n5. Solunum yoksa 2 kurtarıcı nefes verin.\n6. Kalp masajı için göğüs merkezini belirleyin.\n7. 30 kalp masajı uygulayın (Dakikada 100-120 bası, 5-6 cm çöktürerek).\n8. 30 kalp masajı ve 2 yapay solunumu 5 tur tekrarlayın.\n\n" +
                        "Çocuklarda (1-8 Yaş) ve Bebeklerde (0-1 Yaş) TYD benzerdir, ancak bası gücü ve tekniği farklıdır (Çocukta tek el, bebekte iki parmak)."
        ));

        firstAidTopicsMap.put("bilinc_bozukluklari", new FirstAidTopic(
                "Bilinç Bozukluklarında İlkyardım",
                "Bayılma (Senkop):\n- Kişi sırtüstü yatırılır, ayakları 30 cm kaldırılır (Şok Pozisyonu).\n- Sıkan giysiler gevşetilir.\n- Ortamın havalandırılması sağlanır.\n- Kusma varsa yan pozisyonda tutulur.\n- Duyu organları (koklatma vb.) uyarılmaz.\n\n" +
                        "Koma:\n- Kişinin bilinci kapalıdır ancak solunum ve nabız vardır.\n- Solunum yolunu açık tutmak ve kusmuğun solunum yoluna kaçmasını engellemek için Koma (Yarı Yüzükoyun-Yan) Pozisyonu verilir.\n- 2-3 dakikada bir solunum ve nabız kontrol edilir.\n- Tıbbi yardım gelene kadar yalnız bırakılmaz.\n\n" +
                        "Havale (Sara Krizi/Epilepsi):\n- Olayla ilgili güvenlik önlemleri alınır (Kişinin kendine zarar vermesini engellemek).\n- Kriz kendi sürecini tamamlamaya bırakılır.\n- Kilitlenmiş çene açılmaya çalışılmaz, soğan vb. koklatılmaz.\n- Kriz sonrası koma pozisyonu verilir ve 112 aranır."
        ));
        firstAidTopicsMap.put("hayvan_isirmalari", new FirstAidTopic(
                "Hayvan Isırmalarında İlkyardım",
                "Kedi, Köpek Isırmaları:\n- Yara 5 dakika sabunlu su ile yıkanır.\n- Yaranın üzeri temiz bir bezle kapatılır.\n- Kuduz ve/veya tetanoz aşısı için sağlık kuruluşuna başvurulması önerilir.\n\n" +
                        "Arı Sokması:\n- Görünen iğne varsa çıkarılır.\n- Soğuk uygulama yapılır.\n- Eğer ağızdan sokmuşsa ve solunumu güçleştiriyorsa buz emmesi sağlanır.\n- Alerji öyküsü olanlar en yakın sağlık kuruluşuna gitmelidir.\n\n" +
                        "Akrep/Yılan Sokması:\n- Isırılan bölge hareket ettirilmez, sakin kalınır.\n- Yara su ile yıkanır.\n- Isırılan bölgeye soğuk uygulama yapılır.\n- Kan dolaşımını engelleyecek takı, sıkı giysi vb. çıkarılır.\n- Yara emilmez, kesilmez. Hemen 112 aranır."
        ));

        firstAidTopicsMap.put("yabanci_cisim_kacmasi", new FirstAidTopic(
                "Göz, Kulak ve Buruna Yabancı Cisim Kaçmasında İlkyardım",
                "Göze Yabancı Cisim Kaçması:\n- Toz gibi küçük bir cisimse: Göz bol su ile yıkanır veya alt göz kapağı aşağı çekilerek, üst göz kapağı ise üzerine çevrilerek cisim aranır. Temiz ve nemli bir bezle çıkarılmaya çalışılır.\n- Batan bir cisimse: Cisme dokunulmaz. Göz hareket etmeyecek şekilde iki göz birden kapatılır ve tıbbi yardım istenir.\n\n" +
                        "Kulağa Yabancı Cisim Kaçması:\n- Kesinlikle sivri ve delici bir cisimle müdahale edilmez.\n- Su değdirilmez.\n- Tıbbi yardım istenir.\n\n" +
                        "Buruna Yabancı Cisim Kaçması:\n- Burun duvarına bastırılarak kuvvetli bir nefes verme ile cismin atılması sağlanır.\n- Çıkmazsa tıbbi yardım istenir."
        ));
        firstAidTopicsMap.put("hasta_yarali_tasima", new FirstAidTopic(
                "Hasta/Yaralı Taşıma Teknikleri",
                "Genel Taşıma Kuralları:\n- Hasta/yaralıya yakın mesafede çalışılmalı.\n- Sırt düz tutulmalı, dizler bükülerek ağırlık kaldırılmalı.\n- Ani hareketlerden kaçınılmalı.\n- Mümkünse en az iki kişi ile taşınmalı.\n- Baş-boyun-gövde ekseni korunmalı, özellikle omurga yaralanması şüphesi varsa.\n\n" +
                        "Acil Taşıma Teknikleri (Tehlikeli ortamdan uzaklaştırma):\n- Sürükleme Yöntemleri (Ayak bileklerinden, koltuk altından, battaniye ile).\n- Rentek Manevrası (Araç içinden çıkarma).\n\n" +
                        "Kısa Mesafede Süratli Taşıma Teknikleri:\n- Kucakta Taşıma.\n- İtfaiyeci Yöntemi (Omuzda taşıma).\n- Sırtta Taşıma.\n\n" +
                        "Sedye İle Taşıma Teknikleri:\n- Kaşık Tekniği.\n- Köprü Tekniği.\n- Karşılıklı Durarak Kaldırma."
        ));

        allFirstAidTopics.clear();
        allFirstAidTopics.addAll(firstAidTopicsMap.values());
        Collections.sort(allFirstAidTopics, Comparator.comparing(FirstAidTopic::getTitle));
    }

    private void setupSearchListener() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTopics(s.toString().toLowerCase().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterTopics(String searchText) {
        displayedFirstAidTopics.clear();
        if (searchText.isEmpty()) {
            displayedFirstAidTopics.addAll(allFirstAidTopics);
        } else {
            for (FirstAidTopic topic : allFirstAidTopics) {
                String titleLower = topic.getTitle().toLowerCase(new Locale("tr", "TR"));
                String descriptionLower = topic.getDescription().toLowerCase(new Locale("tr", "TR"));
                if (titleLower.contains(searchText) || descriptionLower.contains(searchText)) {
                    displayedFirstAidTopics.add(topic);
                }
            }
        }
        topicAdapter.notifyDataSetChanged();

        if (displayedFirstAidTopics.isEmpty()) {
            recyclerViewTopics.setVisibility(View.GONE);
            textViewNoResults.setVisibility(View.VISIBLE);
        } else {
            recyclerViewTopics.setVisibility(View.VISIBLE);
            textViewNoResults.setVisibility(View.GONE);
        }
    }
    private void openDetailActivity(FirstAidTopic topic) {
        if (topic == null) {
            Toast.makeText(this, "Konu detayı bulunamadı.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, FirstAidDetailActivity.class);
        intent.putExtra(FirstAidDetailActivity.EXTRA_TOPIC_TITLE, topic.getTitle());
        intent.putExtra(FirstAidDetailActivity.EXTRA_TOPIC_DESCRIPTION, topic.getDescription());
        startActivity(intent);
    }
}