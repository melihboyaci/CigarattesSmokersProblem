package parnas_solution;
import java.util.concurrent.Semaphore;

// Parnas yaklaşımı: Agent her tur iki malzeme koyar, pushers ve scoreboard kombinasyonu çözer.
public class Agent implements Runnable {
    private final Semaphore agentSem;
    private final Semaphore tobacco;
    private final Semaphore paper;
    private final Semaphore match;

    public Agent(Semaphore agentSem, Semaphore tobacco, Semaphore paper, Semaphore match) {
        this.agentSem = agentSem;
        this.tobacco = tobacco;
        this.paper = paper;
        this.match = match;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Bir önceki sigara tamamlanmadan yeni malzeme koyma.
                agentSem.acquire();

                // Rastgele iki malzemeyi masaya "bırak" (semafor release).
                int r = (int) (Math.random() * 3);
                if (r == 0) {
                    System.out.println("[Agent] puts TOBACCO + PAPER");
                    tobacco.release();
                    paper.release();
                } else if (r == 1) {
                    System.out.println("[Agent] puts TOBACCO + MATCH");
                    tobacco.release();
                    match.release();
                } else {
                    System.out.println("[Agent] puts PAPER + MATCH");
                    paper.release();
                    match.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
