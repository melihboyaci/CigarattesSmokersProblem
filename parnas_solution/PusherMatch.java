package parnas_solution;

import java.util.concurrent.Semaphore;

// Match pusher: masaya kibrit konduğunu görür ve eksik malzemeyi tamamlatır.
public class PusherMatch implements Runnable {
    private final Semaphore match;
    private final Semaphore mutex;
    private final Semaphore paperSmoker;
    private final Semaphore tobaccoSmoker;
    private final Scoreboard scoreboard;

    public PusherMatch(Semaphore match, Semaphore mutex, Semaphore paperSmoker,
                       Semaphore tobaccoSmoker, Scoreboard scoreboard) {
        this.match = match;
        this.mutex = mutex;
        this.paperSmoker = paperSmoker;
        this.tobaccoSmoker = tobaccoSmoker;
        this.scoreboard = scoreboard;
    }

    @Override
    public void run() {
        try {
            while (true) {
                match.acquire();
                mutex.acquire();

                if (scoreboard.isTobacco()) {
                    scoreboard.setTobacco(false);
                    paperSmoker.release();
                } else if (scoreboard.isPaper()) {
                    scoreboard.setPaper(false);
                    tobaccoSmoker.release();
                } else {
                    scoreboard.setMatch(true);
                }

                mutex.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
