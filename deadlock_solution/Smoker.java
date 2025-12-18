package deadlock_solution;

import java.util.concurrent.Semaphore;
// Naif yaklaşım: içiciler iki malzemeyi ayrı ayrı kilitleyerek alıyor; sıradaki kombinde deadlock oluşabiliyor.
public class Smoker implements Runnable {
    private final SmokerType type;
    private final Semaphore tobacco;
    private final Semaphore paper;
    private final Semaphore match;
    private final Semaphore tableEmpty;

    public Smoker(SmokerType type, Semaphore tobacco, Semaphore paper, Semaphore match, Semaphore tableEmpty) {
        this.type = type;
        this.tobacco = tobacco;
        this.paper = paper;
        this.match = match;
        this.tableEmpty = tableEmpty;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Aynı anda iki farklı içici birer malzemeyi alıp diğerini beklerse masa boş sinyali gelmez ve deadlock yaşanır.
                switch (type) {
                    case TOBACCO -> {
                        paper.acquire();
                        System.out.println("[" + Thread.currentThread().getName() + "] aldi: PAPER, MATCH bekliyor");
                        match.acquire();
                    }
                    case PAPER -> {
                        tobacco.acquire();
                        System.out.println("[" + Thread.currentThread().getName() + "] aldi: TOBACCO, MATCH bekliyor");
                        match.acquire();
                    }
                    case MATCH -> {
                        tobacco.acquire();
                        System.out.println("[" + Thread.currentThread().getName() + "] aldi: TOBACCO, PAPER bekliyor");
                        paper.acquire();
                    }
                }

                System.out.println("[" + Thread.currentThread().getName() + "] rolls and smokes");
                tableEmpty.release(); // Masa boşaltıldı, agent yeni tur başlatabilir.
                Thread.sleep(400);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
