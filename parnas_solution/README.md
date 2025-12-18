# Parnas Pushers Çözümü

Üç pusher + scoreboard ile yarış koşulları önlenir, deadlock engellenir.

## Nasıl Çalışır
- Agent iki malzemeyi bırakır, `agentSem` sayesinde bir sonraki tura kadar bekler.
- Pushers gelen malzemeyi scoreboard'a işler ve doğru içiciyi uyandırır.
- İçici sigarasını yapar, `agentSem.release()` ile yeni turun başlamasını sağlar.

## Çalıştırma
```bash
javac *.java
java Main
```

Her turda doğru içici uyanır ve sistem sonsuza kadar deadlock olmadan çalışır.
