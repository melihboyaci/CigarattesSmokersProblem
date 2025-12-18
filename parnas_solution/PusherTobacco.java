package parnas_solution;

import java.util.concurrent.Semaphore;

// Tobacco pusher: masaya tütün konduğunu görür ve kombinasyonu tamamlar.
public class PusherTobacco implements Runnable {
    private final Semaphore tobacco;
    private final Semaphore mutex;
    private final Semaphore matchSmoker;
    private final Semaphore paperSmoker;
    private final Scoreboard scoreboard;

    public PusherTobacco(Semaphore tobacco, Semaphore mutex, Semaphore matchSmoker,
                         Semaphore paperSmoker, Scoreboard scoreboard) {
        this.tobacco = tobacco;
        this.mutex = mutex;
        this.matchSmoker = matchSmoker;
        this.paperSmoker = paperSmoker;
        this.scoreboard = scoreboard;
    }

    @Override
    public void run() {
        try {
            while (true) {
                tobacco.acquire();
                mutex.acquire();

                if (scoreboard.isPaper()) {
                    scoreboard.setPaper(false);
                    matchSmoker.release();
                } else if (scoreboard.isMatch()) {
                    scoreboard.setMatch(false);
                    paperSmoker.release();
                } else {
                    scoreboard.setTobacco(true);
                }

                mutex.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
