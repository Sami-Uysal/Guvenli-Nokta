package com.guvenlinokta.app;

import android.os.Bundle;
import android.text.Html; // HTML formatlı metinler için
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FirstAidActivity extends AppCompatActivity {

    private TextView textViewFirstAidContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("İlk Yardım Bilgileri");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewFirstAidContent = findViewById(R.id.textViewFirstAidContent);

        String firstAidText = "<h2>Genel İlk Yardım Kuralları</h2>"
                + "<p>1. Sakin olun ve çevrenin güvenli olduğundan emin olun.</p>"
                + "<p>2. Yaralının bilincini kontrol edin.</p>"
                + "<p>3. 112 Acil Servis'i arayın veya aratın.</p>"
                + "<br>"
                + "<h2>Kanamalarda İlk Yardım</h2>"
                + "<p><b>Adım 1:</b> Yaralı bölgeyi kalp seviyesinden yukarı kaldırın.</p>"
                + "<p><b>Adım 2:</b> Temiz bir bez veya gazlı bez ile kanayan yere doğrudan ve sıkıca baskı uygulayın.</p>"
                + "<p><b>Adım 3:</b> Kanama durmazsa, baskıyı artırın ve gerekirse ikinci bir bez ekleyin.</p>"
                + "<p><b>Adım 4:</b> Turnike uygulaması son çare olarak düşünülmeli ve sadece eğitimli kişilerce yapılmalıdır.</p>"
                + "<br>"
                + "<h2>Yanıklarda İlk Yardım</h2>"
                + "<p><b>Adım 1:</b> Yanık bölgeyi hemen soğuk (buzlu olmayan) su altında en az 10-15 dakika tutun.</p>"
                + "<p><b>Adım 2:</b> Yanık üzerine yapışmış giysileri çıkarmaya çalışmayın.</p>"
                + "<p><b>Adım 3:</b> Yanık bölgesine diş macunu, yoğurt, yağ gibi maddeler sürmeyin.</p>"
                + "<p><b>Adım 4:</b> Büyük veya ciddi yanıklarda hemen tıbbi yardım isteyin.</p>"
                + "<br>"
                + "<h2>Kırık ve Çıkıklarda İlk Yardım</h2>"
                + "<p><b>Adım 1:</b> Kırık veya çıkık olan bölgeyi hareket ettirmeyin.</p>"
                + "<p><b>Adım 2:</b> Açık yara varsa üzerini temiz bir bezle örtün.</p>"
                + "<p><b>Adım 3:</b> Bölgeyi destekleyerek (örneğin bir yastıkla) sabit tutmaya çalışın.</p>"
                + "<p><b>Adım 4:</b> Şişliği azaltmak için soğuk kompres uygulayabilirsiniz (direkt cilde değil, bir beze sararak).</p>"
                + "<p><b>Adım 5:</b> En kısa sürede tıbbi yardım alın.</p>";


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textViewFirstAidContent.setText(Html.fromHtml(firstAidText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textViewFirstAidContent.setText(Html.fromHtml(firstAidText));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}