import java.util.concurrent.Semaphore;

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
                match.acquire();       // Agent MATCH koyunca uyanır
                mutex.acquire();

                if (scoreboard.isTobacco()) {
                    // Masada tobacco vardı + şimdi match geldi => eksik PAPER
                    scoreboard.setTobacco(false);
                    paperSmoker.release();
                } else if (scoreboard.isPaper()) {
                    // Masada paper vardı + şimdi match geldi => eksik TOBACCO
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
