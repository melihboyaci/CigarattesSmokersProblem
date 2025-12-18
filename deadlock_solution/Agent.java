package deadlock_solution;

import java.util.concurrent.Semaphore;

// Naif (deadlock'lı) çözüm: Agent iki malzemeyi bırakır, masa boşalana dek bekler.
public class Agent implements Runnable {
    private final Semaphore tableEmpty;
    private final Semaphore tobacco;
    private final Semaphore paper;
    private final Semaphore match;

    public Agent(Semaphore tableEmpty, Semaphore tobacco, Semaphore paper, Semaphore match) {
        this.tableEmpty = tableEmpty;
        this.tobacco = tobacco;
        this.paper = paper;
        this.match = match;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Masa boş değilse yeni tur başlatma.
                tableEmpty.acquire();

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
