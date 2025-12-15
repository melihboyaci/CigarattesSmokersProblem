import java.util.concurrent.Semaphore;

public class Smoker implements Runnable {
    private final SmokerType type;
    private final Semaphore tobaccoSmoker;
    private final Semaphore paperSmoker;
    private final Semaphore matchSmoker;
    private final Semaphore agentSem;

    public Smoker(SmokerType type, Semaphore tobaccoSmoker, Semaphore paperSmoker, 
                  Semaphore matchSmoker, Semaphore agentSem) {
        this.type = type;
        this.tobaccoSmoker = tobaccoSmoker;
        this.paperSmoker = paperSmoker;
        this.matchSmoker = matchSmoker;
        this.agentSem = agentSem;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 1) "Benim eksiğim tamamlandı" sinyalini bekle
                switch (type) {
                    case TOBACCO -> tobaccoSmoker.acquire(); // (paper+match geldi)
                    case PAPER   -> paperSmoker.acquire();   // (tobacco+match geldi)
                    case MATCH   -> matchSmoker.acquire();   // (tobacco+paper geldi)
                }

                // 2) Sigara yap + iç
                System.out.println("[" + Thread.currentThread().getName() + "] makes cigarette and smokes");

                // 3) Agent'a "devam edebilirsin" sinyali
                agentSem.release();

                // Demo için biraz bekletelim (gerçek senkronizasyonda şart değil)
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
