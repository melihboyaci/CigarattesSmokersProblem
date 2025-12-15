import java.util.concurrent.Semaphore;

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
                tobacco.acquire();     // Agent TOBACCO koyunca uyanır
                mutex.acquire();       // scoreboard kritik bölge

                if (scoreboard.isPaper()) {
                    // Masada paper zaten vardı + şimdi tobacco geldi => eksik MATCH
                    scoreboard.setPaper(false);
                    matchSmoker.release(); // match sahibi smoker uyanmalı
                } else if (scoreboard.isMatch()) {
                    // Masada match vardı + şimdi tobacco geldi => eksik PAPER
                    scoreboard.setMatch(false);
                    paperSmoker.release(); // paper sahibi smoker uyanmalı
                } else {
                    // Masada başka yok => tobacco'yu scoreboard'a yaz
                    scoreboard.setTobacco(true);
                }

                mutex.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
