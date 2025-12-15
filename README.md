# Cigarette Smokers Problem / Sigara İçenler Problemi

[English](#english) | [Türkçe](#turkish)

---

## English

### Overview
This project is a Java implementation of the classic **Cigarette Smokers Problem**, a synchronization problem in concurrent programming. It demonstrates the use of semaphores to solve a coordination challenge between multiple threads. 

### The Problem
The Cigarette Smokers Problem involves:
- **One Agent**: Places two random ingredients on a table from {tobacco, paper, matches}
- **Three Smokers**: Each has an infinite supply of one ingredient: 
  - Smoker 1 has tobacco
  - Smoker 2 has paper
  - Smoker 3 has matches

To make and smoke a cigarette, a smoker needs all three ingredients. When the agent places two ingredients on the table, the smoker with the third ingredient can make and smoke a cigarette, then signal the agent to continue.

### Solution Architecture
This implementation uses **three pusher threads** to solve the problem elegantly: 

1. **Agent Thread**: Randomly selects and places two ingredients on the table
2. **Pusher Threads** (3): Monitor for ingredient combinations
   - `PusherTobacco`: Watches for tobacco on the table
   - `PusherPaper`: Watches for paper on the table
   - `PusherMatch`: Watches for matches on the table
3. **Smoker Threads** (3): Wait for their required ingredients and then smoke
4. **Scoreboard**: Tracks which ingredients are currently waiting on the table
5. **Semaphores**: Used for synchronization between all threads

### Project Structure
```
CigarattesSmokersProblem/
├── Main.java              # Entry point, initializes all threads and semaphores
├── Agent.java             # Agent that places ingredients
├── Smoker.java            # Smoker thread implementation
├── SmokerType. java        # Enum for smoker types
├── PusherTobacco.java     # Pusher for tobacco ingredient
├── PusherPaper.java       # Pusher for paper ingredient
├── PusherMatch.java       # Pusher for matches ingredient
└── Scoreboard.java        # Shared state for ingredient tracking
```

### How It Works
1. The agent acquires permission and places two random ingredients
2. Pusher threads detect the ingredients and update the scoreboard
3. When two ingredients are available, the corresponding smoker is signaled
4. The smoker makes a cigarette, smokes it, and signals the agent
5. The cycle repeats

### Running the Program
```bash
# Compile
javac *.java

# Run
java Main
```

### Example Output
```
[Agent] puts TOBACCO + PAPER
[Smoker-Match] makes cigarette and smokes
[Agent] puts TOBACCO + MATCH
[Smoker-Paper] makes cigarette and smokes
[Agent] puts PAPER + MATCH
[Smoker-Tobacco] makes cigarette and smokes
```

### Technologies
- Java
- Concurrent Programming with Semaphores
- Multi-threading

---

## Turkish

### Genel Bakış
Bu proje, eşzamanlı programlamada klasik bir senkronizasyon problemi olan **Sigara İçenler Problemi**'nin Java implementasyonudur. Birden fazla thread arasındaki koordinasyon zorluğunu semaforlar kullanarak çözmeyi gösterir.

### Problem
Sigara İçenler Problemi şunları içerir:
- **Bir Temsilci (Agent)**: Masaya {tütün, kağıt, kibrit} setinden rastgele iki malzeme koyar
- **Üç İçici (Smoker)**: Her birinin bir malzemeden sonsuz arzı vardır:
  - İçici 1'in tütünü var
  - İçici 2'nin kağıdı var
  - İçici 3'ün kibriti var

Sigara yapmak ve içmek için, bir içicinin üç malzemeye de ihtiyacı vardır. Temsilci masaya iki malzeme koyduğunda, üçüncü malzemeye sahip olan içici sigara yapıp içebilir, ardından temsilciye devam etmesi için sinyal gönderir.

### Çözüm Mimarisi
Bu implementasyon, problemi zarif bir şekilde çözmek için **üç pusher thread** kullanır:

1. **Agent Thread**: Rastgele iki malzeme seçer ve masaya koyar
2. **Pusher Thread'leri** (3): Malzeme kombinasyonlarını izler
   - `PusherTobacco`: Masadaki tütünü izler
   - `PusherPaper`: Masadaki kağıdı izler
   - `PusherMatch`: Masadaki kibriti izler
3. **Smoker Thread'leri** (3): İhtiyaç duydukları malzemeleri bekler ve sonra içerler
4. **Scoreboard**:  Masada şu anda bekleyen malzemeleri takip eder
5. **Semaforlar**:  Tüm thread'ler arası senkronizasyon için kullanılır

### Proje Yapısı
```
CigarattesSmokersProblem/
├── Main.java              # Giriş noktası, tüm thread'leri ve semaforları başlatır
├── Agent.java             # Malzeme koyan temsilci
├── Smoker.java            # İçici thread implementasyonu
├── SmokerType.java        # İçici tipleri için enum
├── PusherTobacco.java     # Tütün malzemesi için pusher
├── PusherPaper.java       # Kağıt malzemesi için pusher
├── PusherMatch.java       # Kibrit malzemesi için pusher
└── Scoreboard.java        # Malzeme takibi için paylaşılan durum
```

### Nasıl Çalışır
1. Temsilci izin alır ve rastgele iki malzeme koyar
2. Pusher thread'leri malzemeleri algılar ve scoreboard'u günceller
3. İki malzeme hazır olduğunda, ilgili içiciye sinyal gönderilir
4. İçici sigara yapar, içer ve temsilciye sinyal gönderir
5. Döngü tekrarlanır

### Programı Çalıştırma
```bash
# Derleme
javac *.java

# Çalıştırma
java Main
```

### Örnek Çıktı
```
[Agent] puts TOBACCO + PAPER
[Smoker-Match] makes cigarette and smokes
[Agent] puts TOBACCO + MATCH
[Smoker-Paper] makes cigarette and smokes
[Agent] puts PAPER + MATCH
[Smoker-Tobacco] makes cigarette and smokes
```

### Teknolojiler
- Java
- Semaforlar ile Eşzamanlı Programlama
- Çoklu Thread Programlama

---

## License / Lisans
This project is open source and available for educational purposes.

Bu proje açık kaynaklıdır ve eğitim amaçlı kullanılabilir. 
