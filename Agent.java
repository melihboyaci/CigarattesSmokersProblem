import java.util.concurrent.Semaphore;

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
                // 1) Bir önceki sigara bitmeden yeni malzeme koymasın.
                agentSem.acquire();

                // 2) Rastgele iki malzeme seçip masaya "koy" (semafor release)
                int r = (int) (Math.random() * 3);

                if (r == 0) {
                    // Tobacco + Paper
                    System.out.println("[Agent] puts TOBACCO + PAPER");
                    tobacco.release();
                    paper.release();
                } else if (r == 1) {
                    // Tobacco + Match
                    System.out.println("[Agent] puts TOBACCO + MATCH");
                    tobacco.release();
                    match.release();
                } else {
                    // Paper + Match
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
