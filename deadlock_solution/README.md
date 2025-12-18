# Naif (Deadlock'lı) Çözüm

Bu klasörde klasik hatalı yaklaşım gösteriliyor. Her içici iki malzemeyi ayrı ayrı semaforlarla alıyor. Aşağıdaki senaryoda deadlock oluşur:

- Agent `TOBACCO + PAPER` koyar.
- `Smoker-Tobacco` kağıdı kapar ve kibriti bekler.
- `Smoker-Match` tütünü kapar ve kağıdı bekler.
- Her ikisi de ikinci malzemeyi beklerken `tableEmpty` sinyali verilmez; agent yeni tur başlatamaz ve sistem kilitlenir.

## Çalıştırma
```bash
javac *.java
java Main
```

Çıktıda bir tur tamamlanır ve ardından bazen thread'ler takılı kalır; bu durum sunumda deadlock örneği olarak gösterilebilir.
