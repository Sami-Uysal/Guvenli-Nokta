package com.guvenlinokta.app;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    private Spinner spinnerDisasterType;
    private ImageView imageViewDisasterGif;
    private TextView textViewDisasterInfo;

    private List<DisasterInfo> disasterInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Afet Bilgilendirme");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        spinnerDisasterType = findViewById(R.id.spinnerDisasterType);
        imageViewDisasterGif = findViewById(R.id.imageViewDisasterGif);
        textViewDisasterInfo = findViewById(R.id.textViewDisasterInfo);

        loadDisasterData();

        ArrayAdapter<DisasterInfo> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, disasterInfoList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisasterType.setAdapter(adapter);

        spinnerDisasterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DisasterInfo selectedDisaster = (DisasterInfo) parent.getItemAtPosition(position);
                if (selectedDisaster.getGifResourceId() != 0) {
                    Glide.with(InfoActivity.this)
                            .asGif()
                            .load(selectedDisaster.getGifResourceId())
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .into(imageViewDisasterGif);
                    imageViewDisasterGif.setVisibility(View.VISIBLE);
                } else {
                    imageViewDisasterGif.setVisibility(View.GONE);
                }
                textViewDisasterInfo.setText(selectedDisaster.getDescription());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                imageViewDisasterGif.setVisibility(View.GONE);
                textViewDisasterInfo.setText("Lütfen bir afet türü seçin.");
            }
        });

        if (!disasterInfoList.isEmpty()) {
            spinnerDisasterType.setSelection(0);
        } else {
            textViewDisasterInfo.setText("Afet bilgisi bulunamadı.");
            imageViewDisasterGif.setVisibility(View.GONE);
        }
    }

    private void loadDisasterData() {
        disasterInfoList = new ArrayList<>();
        disasterInfoList.add(new DisasterInfo("Lütfen Seçiniz", "Afet türü hakkında bilgi almak için lütfen yukarıdan bir seçim yapınız.", 0)); // 0, GIF olmadığını belirtir
        disasterInfoList.add(new DisasterInfo("Deprem",
                "Deprem Anında Yapılması Gerekenler:\n" +
                        "1. Sakin olun, panik yapmayın.\n" +
                        "2. Sağlam bir masa altına veya iç duvara yakın bir yere ÇÖK-KAPAN-TUTUN pozisyonunda girin.\n" +
                        "3. Pencerelerden, cam eşyalardan ve devrilebilecek mobilyalardan uzak durun.\n" +
                        "4. Asansörü KULLANMAYIN.\n" +
                        "5. Sarsıntı durduktan sonra güvenli bir şekilde dışarı çıkın, artçı sarsıntılara karşı dikkatli olun.",
                R.drawable.deprem));

        disasterInfoList.add(new DisasterInfo("Sel",
                "Sel Anında ve Sonrasında Yapılması Gerekenler:\n" +
                        "1. Sel uyarısı yapıldığında hemen yüksek yerlere çıkın.\n" +
                        "2. Sel sularına kesinlikle girmeyin (araçla veya yaya olarak).\n" +
                        "3. Elektrik kaynaklarından uzak durun.\n" +
                        "4. Sel sonrası yetkililerin talimatlarını bekleyin ve güvenli olduğu belirtilmeden evinize dönmeyin.\n" +
                        "5. Sel sularıyla temas etmiş yiyecekleri tüketmeyin.",
                R.drawable.sel));

        disasterInfoList.add(new DisasterInfo("Yangın",
                "Yangın Anında Yapılması Gerekenler:\n" +
                        "1. Hemen 110'u arayın.\n" +
                        "2. Mümkünse yangın söndürücü ile müdahale edin (kendinizi riske atmadan).\n" +
                        "3. Duman varsa eğilerek veya sürünerek hareket edin.\n" +
                        "4. Kapıları açmadan önce elinizin tersiyle kontrol edin, sıcaksa açmayın.\n" +
                        "5. Asansörü kullanmayın, merdivenleri tercih edin.\n" +
                        "6. Binayı terk ettikten sonra güvenli bir toplanma alanına gidin.",
                R.drawable.yangin));

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}