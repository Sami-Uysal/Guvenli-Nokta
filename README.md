
# 📱 Güvenli Nokta

**Güvenli Nokta**, doğal afetler ve acil durumlar için bilgilendirme, konum paylaşımı ve ilk yardım rehberliği sağlayan bir Android uygulamasıdır.

---

## 🚨 Uygulama Amacı

Bu uygulama, kullanıcıların afet anında bilinçli ve hızlı hareket edebilmesi için gerekli bilgileri ve araçları sunmayı amaçlamaktadır.

---

## 🧩 Özellikler

- 📌 **Güvenli Nokta İşaretleme**  
  Kullanıcılar, harita üzerinden güvenli noktaları işaretleyebilir ve kaydedebilir.

- 🧭 **Konum Takibi**  
  Gerçek zamanlı konum takibi ile en yakın güvenli alanlara yönlendirme.

- 📚 **Bilgilendirme Modülü**  
  Deprem, sel, yangın, çığ, heyelan, hortum gibi afetlerle ilgili bilgi kartları.

- 🩹 **İlk Yardım Rehberi**  
  Temel ilkyardım bilgileri ve talimatları.

- 📢 **Acil Durum Sireni**  
  Tehlike anında kullanılabilecek yüksek sesli uyarı özelliği.

- 👤 **Profil Yönetimi**  
  Kullanıcı bilgilerini güncelleme ve kişiselleştirme.

- 📍 **Pinlerim**  
  Kullanıcının kendi güvenli noktalarını kaydettiği ve yönettiği sistem.

- 🎓 **Eğitim Modülü**  
  Uygulamanın kullanımını anlatan öğretici ekranlar.

---

## ⚙️ Teknik Bilgiler

- Android (Java/Kotlin) ile geliştirildi  
- Google Maps API kullanıyor  
- Konum izinleri: ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION  
- İnternet bağlantısı gereklidir  
- Bildirim ve titreşim desteği  
- Material Design 3 arayüzü

---

## 🐞 Bilinen Sorunlar

- Bazı cihazlarda konum hizmetleri hatalı çalışabilir  
- Büyük ekranlı cihazlarda görüntü optimizasyonu geliştirilmeli  

---

## 🚧 Yol Haritası

- [ ] Çevrimdışı veri desteği  
- [ ] Acil durum kişileri tanımlama  
- [ ] Daha fazla afet türü bilgisi  
- [ ] Performans ve UX iyileştirmeleri

---
## 📥 İndirme

Uygulamanın son sürümünü doğrudan indirmek için:

➡️ [GitHub Releases Sayfasına Git](https://github.com/Sami-Uysal/Guvenli-Nokta/releases)
📱 APK dosyasını Android cihazınıza indirip yükleyebilirsiniz.

> **Not:** Bilinmeyen kaynaklardan yükleme iznini cihazınızdan manuel olarak açmanız gerekebilir.


---

## 📦 Geliştiriciler İçin Kurulum

1. Bu repoyu klonlayın:
   

bash
   git clone https://github.com/Sami-Uysal/Guvenli-Nokta.git

2. Android Studio ile açın.

3. `gradle.properties` dosyasına kendi API anahtarınızı girin.

bash
   API_KEY=...
   WEATHER_API_KEY=...

5. Gerekli izinleri AndroidManifest.xml dosyasında kontrol edin.

6. Uygulamayı emülatör veya fiziksel cihazda çalıştırın.

---
## 🔑 API Anahtarları Nasıl Alınır?

**Güvenli Nokta**, harita ve hava durumu verileri için iki farklı API kullanmaktadır. Uygulamayı geliştirmek veya test etmek için aşağıdaki adımları izleyerek kendi API anahtarlarınızı almanız gerekmektedir:

### 🗺️ Google Maps API Anahtarı

1. [Google Cloud Console](https://console.cloud.google.com/) adresine gidin.
2. Bir proje oluşturun veya mevcut bir projeyi seçin.
3. Sol menüden **“APIs & Services” > “Credentials”** sekmesine gidin.
4. **“Create Credentials” > “API Key”** seçeneği ile bir API anahtarı oluşturun.
5. Harita işlevleri için şu API’leri etkinleştirin:

   * **Maps SDK for Android**
   * **Places API** (isteğe bağlı)

📌 *API anahtarınızı güvenli bir şekilde saklayın ve gerekli sınırlamaları (IP veya uygulama bazlı) yapılandırın.*

---

### ☁️ OpenWeather API Anahtarı

1. [OpenWeather](https://openweathermap.org/api) sitesine gidin.
2. Ücretsiz bir hesap oluşturun veya giriş yapın.
3. Kullanmak istediğiniz API servisini seçin (örneğin: Current Weather Data).
4. Hesap panelinizde “API keys” sekmesine gidin.
5. Yeni bir API anahtarı oluşturun veya mevcut anahtarınızı kopyalayın.

📌 *Ücretsiz plan günlük sınırlamalara sahiptir, yoğun kullanım için ücretli planlara göz atabilirsiniz.*

---

## 🗣️ Katkıda Bulunun

Katkıda bulunmak isterseniz:

* Hataları [issue](https://github.com/kullaniciadi/guvenli-nokta/issues) olarak bildirin
* Yeni özellik önerilerinizi paylaşın
* Pull request gönderin 🎉

---

## 📄 Lisans

Bu proje MIT Lisansı ile lisanslanmıştır. Ayrıntılar için LICENSE dosyasına göz atabilirsiniz.