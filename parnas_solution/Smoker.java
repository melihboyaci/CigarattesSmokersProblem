package parnas_solution;

import java.util.concurrent.Semaphore;

// Her içici elindeki malzeme dışındaki iki malzemeyi bekler.
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
                switch (type) {
                    case TOBACCO -> tobaccoSmoker.acquire();
                    case PAPER -> paperSmoker.acquire();
                    case MATCH -> matchSmoker.acquire();
                }

                System.out.println("[" + Thread.currentThread().getName() + "] makes cigarette and smokes");
                agentSem.release();
                Thread.sleep(400);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
