package com.guvenlinokta.app;

import android.os.Bundle;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InfoActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    private RecyclerView recyclerViewDisasters;
    private DisasterItemAdapter adapter;
    private List<DisasterInfo> disasterList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Afet Bilgilendirme");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerViewDisasters = findViewById(R.id.recyclerViewDisasters);
        recyclerViewDisasters.setLayoutManager(new GridLayoutManager(this, 3));

        disasterList = getDisasterData();
        adapter = new DisasterItemAdapter(this, disasterList);
        recyclerViewDisasters.setAdapter(adapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else {
            getLocationAndFetchWeather();
        }
    }

    private List<DisasterInfo> getDisasterData() {
        List<DisasterInfo> list = new ArrayList<>();
        list.add(new DisasterInfo("Deprem", R.drawable.deprem));
        list.add(new DisasterInfo("Sel", R.drawable.sel));
        list.add(new DisasterInfo("Yangın", R.drawable.yangin));
        list.add(new DisasterInfo("Hortum", R.drawable.hortum));
        list.add(new DisasterInfo("Çığ", R.drawable.cig));
        list.add(new DisasterInfo("Heyelan", R.drawable.heyelan));
        return list;
    }
    private void getLocationAndFetchWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                fetchWeather(location.getLatitude(), location.getLongitude());
            } else {
                LocationRequest request = LocationRequest.create()
                        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                        .setInterval(1000);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
                    return;
                }
                fusedLocationClient.requestLocationUpdates(request, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        fusedLocationClient.removeLocationUpdates(this);
                        if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                            Location loc = locationResult.getLastLocation();
                            fetchWeather(loc.getLatitude(), loc.getLongitude());
                        }
                    }
                }, Looper.getMainLooper());
            }
        });
    }
    private void fetchWeather(double lat, double lon) {
        String apikey = BuildConfig.WEATHER_API_KEY;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TextView sehirismi = findViewById(R.id.textViewCityName);
        TextView havaDurumu = findViewById(R.id.textViewWeatherInfo);
        TextView nemView = findViewById(R.id.textViewHumidity);
        TextView ruzgarView = findViewById(R.id.textViewWind);

        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getWeatherByLocation(lat, lon, apikey, "metric", "tr");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String durum = ilkHarfiBuyut(translateDescription(response.body().weather.get(0).description));
                    int sicaklik = Math.round(response.body().main.temp);
                    String sehir = response.body().name;
                    String iconCode = response.body().weather.get(0).icon;
                    int nem = response.body().main.humidity;
                    float ruzgarhizi = response.body().wind != null ? response.body().wind.speed : 0;

                    runOnUiThread(() -> {
                        sehirismi.setText(sehir);
                        havaDurumu.setText(durum + ", " + formatSicaklik(sicaklik));
                        nemView.setText("Nem: %" + nem);
                        ruzgarView.setText("Rüzgar: " + formatRuzgarHizi(ruzgarhizi));
                        loadWeatherIcon(iconCode, findViewById(R.id.weatherIcon));
                    });
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                havaDurumu.setText("Hava durumu alınamadı. Lütfen internet bağlantınızı kontrol edin.");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocationAndFetchWeather();
        }
    }

    public String ilkHarfiBuyut(String metin) {
        if (metin == null || metin.isEmpty()) return metin;
        return metin.substring(0, 1).toUpperCase() + metin.substring(1);
    }
    private String formatSicaklik(float temp) {
        return String.format(Locale.getDefault(), "%.1f°C", temp);
    }

    private String formatRuzgarHizi(float speed) {
        return String.format(Locale.getDefault(), "%.1f km/s", speed * 3.6);
    }
    private void loadWeatherIcon(String iconCode, ImageView imageView) {
        Map<String, Integer> iconResMap = new HashMap<>();
        iconResMap.put("01d", R.drawable.acik_gunduz);
        iconResMap.put("01n", R.drawable.acik_gece);
        iconResMap.put("02d", R.drawable.bulutlu_gunduz);
        iconResMap.put("02n", R.drawable.bulutlu_gece);
        iconResMap.put("03d", R.drawable.bulutlu_gunduz);
        iconResMap.put("03n", R.drawable.bulutlu_gece);
        iconResMap.put("04d", R.drawable.parcali_bulutlu);
        iconResMap.put("04n", R.drawable.parcali_bulutlu);
        iconResMap.put("09d", R.drawable.rainy_1_day);
        iconResMap.put("09n", R.drawable.rainy_1_night);
        iconResMap.put("10d", R.drawable.rainy_3_day);
        iconResMap.put("10n", R.drawable.rainy_3_night);
        iconResMap.put("11d", R.drawable.firtina);
        iconResMap.put("11n", R.drawable.firtina);
        iconResMap.put("13d", R.drawable.kar);
        iconResMap.put("13n", R.drawable.kar);
        iconResMap.put("50d", R.drawable.fog);
        iconResMap.put("50n", R.drawable.fog);

        int resId = iconResMap.containsKey(iconCode) ? iconResMap.get(iconCode) : R.drawable.parcali_bulutlu;
        imageView.setImageResource(resId);

    }
    private String translateDescription(String description) {
        switch (description.toLowerCase()) {
            case "clear sky": return "Açık hava";
            case "few clouds": return "Az bulutlu";
            case "rain": return "Yağmurlu";
            default: return description;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
