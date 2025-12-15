# Sigara İçicileri Problemi

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Eşzamanlılık](https://img.shields.io/badge/Konu-Eşzamanlılık-blue.svg)]()
[![Lisans](https://img.shields.io/badge/Lisans-MIT-green.svg)]()

> 🇬🇧 [Click here for English README](README.md)

Eşzamanlı programlamada temel bir senkronizasyon problemi olan klasik **Sigara İçicileri Problemi**'nin kapsamlı bir Java uygulaması. Bu proje iki farklı yaklaşımı sergiler: kilitlenmeye açık naif bir çözüm ve zarif Parnas pushers çözümü.

## 📖 İçindekiler

- [Problem Tanımı](#-problem-tanımı)
- [Çözümlere Genel Bakış](#-çözümlere-genel-bakış)
- [Depo Yapısı](#-depo-yapısı)
- [Hızlı Başlangıç](#-hızlı-başlangıç)
- [Detaylı Çözümler](#-detaylı-çözümler)
  - [Naif Kilitlenme Çözümü](#1-naif-kilitlenme-çözümü)
  - [Parnas Pushers Çözümü](#2-parnas-pushers-çözümü)
- [Gösterim](#-gösterim)
- [Eğitsel Kullanım](#-eğitsel-kullanım)
- [Kaynaklar](#-kaynaklar)

## 🎯 Problem Tanımı

**Sigara İçicileri Problemi**, 1971'de Suhas Patil tarafından tanıtılan klasik bir süreç senkronizasyon problemidir. Semaforların belirli senkronizasyon problemlerini çözmedeki sınırlamalarını ve daha sofistike koordinasyon mekanizmalarının gerekliliğini gösterir.

### Problem Kurulumu

- **Üç içici**, her biri bir malzemenin sonsuz arzına sahip:
  - İçici 1'in **tütünü** var
  - İçici 2'nin **kağıdı** var
  - İçici 3'ün **kibriti** var
  
- Üç malzemenin hepsinin de sonsuz arzına sahip **bir ajan**

- Sigara içmek için bir içicinin üç malzemeye ihtiyacı vardır:
  - Tütün
  - Kağıt
  - Kibrit

### Problem Kuralları

1. Ajan rastgele **iki farklı malzemeyi** masaya koyar
2. Üçüncü malzemeye sahip olan içici iki öğeyi alır, sigara yapar ve içer
3. Ajan, başka iki malzeme koymadan önce içicinin bitmesini bekler
4. Bu süreç sonsuza kadar tekrar eder

### Zorluk

Zorluk, yalnızca semaforlar kullanarak içicileri ve ajanı koordine etmektir:
- Kilitlenme (deadlock) olmamalı
- Açlık (starvation) olmamalı
- Doğru senkronizasyon sağlanmalı

## 🔍 Çözümlere Genel Bakış

Bu depo iki farklı yaklaşım içerir:

| Çözüm | Durum | Açıklama | Kullanım Amacı |
|-------|-------|----------|----------------|
| **Deadlock Çözümü** | ⚠️ Kilitlenmeye Açık | Birden fazla içicinin malzemeleri ayrı ayrı alabileceği naif uygulama | Eğitsel - basit semafor kullanımının neden başarısız olduğunu gösterir |
| **Parnas Pushers** | ✅ Kilitlenmesiz | Doğru koordinasyon için ara pusher thread'leri ve scoreboard kullanır | Üretime hazır çözüm |

## 📁 Depo Yapısı

```
CigarattesSmokersProblem/
├── README.md                    # İngilizce dokümantasyon
├── README_TR.md                 # Bu dosya (Türkçe)
├── deadlock-solution/           # Naif, kilitlenmeye açık uygulama
│   ├── Agent.java              # Malzeme koyan ajan
│   ├── Smoker.java             # İçici thread'leri (naif yaklaşım)
│   ├── SmokerType.java         # İçici tipleri için enum
│   ├── DeadlockSolution.java   # Deadlock watchdog ile ana sınıf
│   └── README.md               # Çözüme özel dokümantasyon
└── parnas-solution/             # Parnas pushers yaklaşımı (kilitlenmesiz)
    ├── Agent.java              # Malzeme koyan ajan
    ├── Smoker.java             # İçici thread'leri
    ├── SmokerType.java         # İçici tipleri için enum
    ├── PusherTobacco.java      # Tütün için pusher
    ├── PusherPaper.java        # Kağıt için pusher
    ├── PusherMatch.java        # Kibrit için pusher
    ├── Scoreboard.java         # Paylaşılan durum takipçisi
    ├── Main.java               # Ana sınıf
    └── README.md               # Çözüme özel dokümantasyon
```

## 🚀 Hızlı Başlangıç

### Gereksinimler

- Java Development Kit (JDK) 17 veya üzeri
- Terminal veya Komut İstemi

### Deadlock Çözümünü Çalıştırma

```bash
cd deadlock-solution
javac *.java
java DeadlockSolution
```

**Beklenen Davranış:** Program birkaç iterasyon çalışır, ardından muhtemelen kilitlenir. Bir watchdog thread'i kilitlenmeyi tespit edip thread durumlarını yazdırır.

### Parnas Pushers Çözümünü Çalıştırma

```bash
cd parnas-solution
javac *.java
java Main
```

**Beklenen Davranış:** Program kilitlenme olmadan sonsuza kadar çalışır ve doğru senkronizasyonu gösterir.

### Windows'ta Çalıştırma (PowerShell)

```powershell
# Deadlock çözümü
cd deadlock-solution; javac *.java; java DeadlockSolution

# Parnas çözümü
cd parnas-solution; javac *.java; java Main
```

## 🔬 Detaylı Çözümler

### 1. Naif Kilitlenme Çözümü

**Konum:** `deadlock-solution/`

#### Nasıl Çalışır

Her içici, ayrı semaforlar kullanarak iki malzemeyi bağımsız olarak almaya çalışır:

```java
// Basitleştirilmiş konsept
if (hasTobacco) {
    acquire(paper);    // Kağıdı alabilir
    acquire(match);    // Kibriti bekliyor...
}
```

#### Neden Başarısız Olur

**Kilitlenme Senaryosu:**

1. Ajan masaya **TÜTÜN + KAĞIT** koyar
2. `Smoker-Tobacco` (Tütünü olan içici) **KAĞIT**'ı alır, **KİBRİT**'i bekler
3. `Smoker-Match` (Kibriti olan içici) **TÜTÜN**'ü alır, **KAĞIT**'ı bekler
4. İki içici de birbirinin kaynaklarını bekleyerek bloke olur
5. `tableEmpty` semaforu asla serbest bırakılmaz
6. Ajan yeni malzeme koyamaz → **KİLİTLENME**

#### Ana Bileşenler

- **Agent (Ajan):** Rastgele iki malzeme seçer ve koyar
- **Smokers (İçiciler):** Her biri ihtiyaç duyduğu iki malzemeyi almaya çalışır
- **Deadlock Watchdog:** Sistem durumunu izler ve kilitlenme oluştuğunda rapor eder

#### Örnek Çıktı

```
[Agent] puts TOBACCO + PAPER
[Smoker-Match] acquires TOBACCO
[Smoker-Tobacco] acquires PAPER

*** İlerleme yok: masa dolu, malzeme semaforları bekleniyor; deadlock ihtimali yüksek ***
Agent -> WAITING
Smoker-Tobacco -> WAITING
Smoker-Paper -> WAITING
Smoker-Match -> WAITING
```

### 2. Parnas Pushers Çözümü

**Konum:** `parnas-solution/`

#### Nasıl Çalışır

Bu çözüm, masada hangi malzemelerin bulunduğunu takip etmek için **ara pusher thread'leri** ve bir **scoreboard** tanıtır:

```
Ajan → Malzemeler → Pusher'lar → Scoreboard → Doğru İçici
```

#### Mimari

1. **Agent (Ajan):** İki rastgele malzeme koyar ve onay bekler
2. **Pushers (3 thread):** Her malzeme tipi için bir tane
   - Kendi malzeme semaforlarını izlerler
   - Bir malzeme geldiğinde scoreboard'u günceller
   - Her iki gerekli malzeme mevcut olduğunda doğru içiciyi uyandırır
3. **Scoreboard:** Hangi malzemelerin mevcut olduğunu takip eden thread-safe paylaşılan durum
4. **Smokers (3 thread):** Kendi özel sinyallerini bekler, ardından sigara yapar ve içer

#### Adım Adım Akış

```
1. Ajan agentSem'i alır → TÜTÜN + KAĞIT koyar
2. PusherTobacco uyanır → Scoreboard'u günceller (tobacco = true)
3. PusherPaper uyanır → Scoreboard'u günceller (paper = true)
4. PusherPaper görür: tobacco=true, paper=true → Smoker-Match'i uyandırır
5. Smoker-Match sigara yapar → agentSem'i serbest bırakır
6. Ajan şimdi yeni malzemeler koyabilir → Döngü kilitlenme olmadan devam eder
```

#### Ana Bileşenler

- **Semafor agentSem:** Ajanın yeni malzeme koyma yeteneğini kontrol eder
- **Malzeme semaforları (3):** Malzemeler geldiğinde pusher'ları bilgilendirir
- **İçici semaforları (3):** Belirli içiciye uyanma sinyali verir
- **Mutex:** Scoreboard'u yarış koşullarından korur
- **Scoreboard:** Mevcut masa durumunu takip eder

#### Örnek Çıktı

```
[Agent] puts TOBACCO + PAPER
[Pusher-Tobacco] updates scoreboard
[Pusher-Paper] sees combination → wakes Smoker-Match
[Smoker-Match] makes cigarette and smokes
[Agent] puts PAPER + MATCH
[Pusher-Paper] updates scoreboard
[Pusher-Match] sees combination → wakes Smoker-Tobacco
[Smoker-Tobacco] makes cigarette and smokes
...sonsuza kadar devam eder...
```

## 🎓 Gösterim

### Sunumlar veya Öğretim İçin

1. **Deadlock Çözümü ile Başlayın:**
   ```bash
   cd deadlock-solution && javac *.java && java DeadlockSolution
   ```
   - İlk iterasyonları gösteren çıktıyı gözlemleyin
   - Kilitlenme tespit mesajını bekleyin
   - Thread durumlarını ve kilitlenmenin neden oluştuğunu tartışın

2. **Parnas Çözümüne Geçin:**
   ```bash
   cd ../parnas-solution && javac *.java && java Main
   ```
   - Sürekli, kilitlenmesiz çalışmayı gösterin
   - Pusher'ların ve scoreboard'un rolünü vurgulayın
   - Doğru koordinasyonun kilitlenmeyi nasıl önlediğini açıklayın

3. **Önemli Tartışma Noktaları:**
   - Basit semafor tabanlı çözümlerin sınırlamaları
   - Ara koordinasyon mekanizmalarının önemi
   - Yarış koşulları ve mutex'in bunları nasıl önlediği
   - Deadlock ve starvation arasındaki fark

## 📚 Eğitsel Kullanım

Bu proje şunlar için idealdir:

- **İşletim Sistemleri dersleri** - Süreç senkronizasyonunu gösterir
- **Eşzamanlı Programlama dersleri** - Pratik semafor kullanımını gösterir
- **Yazılım Mühendisliği dersleri** - Koordinasyon için tasarım kalıplarını gösterir
- **Mülakat hazırlığı** - Teknik mülakatları sıkça sorulan klasik problem

### Öğrenme Hedefleri

Öğrenciler şunları anlayacak:
- Semaforlar pratikte nasıl çalışır
- Naif senkronizasyon neden kilitlenmeye yol açabilir
- Doğru kaynak koordinasyonunun önemi
- Thread iletişim kalıpları
- Mutex ile kritik bölge koruması

## 📖 Kaynaklar

1. Patil, S. S. (1971). "Limitations and capabilities of Dijkstra's semaphore primitives for coordination among processes"
2. Parnas, D. L. (1975). "On a solution to the cigarette smoker's problem (without conditional statements)"
3. [Little Book of Semaphores - Allen B. Downey](https://greenteapress.com/wp/semaphores/)
4. [Operating System Concepts - Silberschatz, Galvin, Gagne](https://www.os-book.com/)

## 📄 Lisans

Bu proje açık kaynaklıdır ve eğitim amaçlı kullanıma açıktır.

## 🤝 Katkıda Bulunma

İyileştirmeler veya hata düzeltmeleri için issue açmaktan veya pull request göndermekten çekinmeyin.

---

**Not:** Bu uygulama, eşzamanlılık kavramlarını göstermek için eğitim amaçlı tasarlanmıştır. Deadlock çözümü, üretim kodunda nelerin kaçınılması gerektiğini göstermek için kasıtlı olarak sorunlu davranış sergiler.
