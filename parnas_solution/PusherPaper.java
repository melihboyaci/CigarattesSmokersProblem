package parnas_solution;

import java.util.concurrent.Semaphore;

// Paper pusher: masaya kağıt konduğunu görür ve doğru içiciyi uyandırır.
public class PusherPaper implements Runnable {
    private final Semaphore paper;
    private final Semaphore mutex;
    private final Semaphore matchSmoker;
    private final Semaphore tobaccoSmoker;
    private final Scoreboard scoreboard;

    public PusherPaper(Semaphore paper, Semaphore mutex, Semaphore matchSmoker,
                       Semaphore tobaccoSmoker, Scoreboard scoreboard) {
        this.paper = paper;
        this.mutex = mutex;
        this.matchSmoker = matchSmoker;
        this.tobaccoSmoker = tobaccoSmoker;
        this.scoreboard = scoreboard;
    }

    @Override
    public void run() {
        try {
            while (true) {
                paper.acquire();
                mutex.acquire();

                if (scoreboard.isTobacco()) {
                    scoreboard.setTobacco(false);
                    matchSmoker.release();
                } else if (scoreboard.isMatch()) {
                    scoreboard.setMatch(false);
                    tobaccoSmoker.release();
                } else {
                    scoreboard.setPaper(true);
                }

                mutex.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
