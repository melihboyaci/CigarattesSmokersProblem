package parnas_solution;
import java.util.concurrent.Semaphore;

// Parnas yaklaşımı: pushers + scoreboard ile yarış durumları engellenir.
public class Main {
    public static void main(String[] args) {
        Semaphore agentSem = new Semaphore(1); // Agent, her sigara sonrası tekrar koymak için bekler.

        Semaphore tobacco = new Semaphore(0);
        Semaphore paper = new Semaphore(0);
        Semaphore match = new Semaphore(0);

        // Smokers'ı uyandıracak sinyaller.
        Semaphore tobaccoSmoker = new Semaphore(0);
        Semaphore paperSmoker = new Semaphore(0);
        Semaphore matchSmoker = new Semaphore(0);

        Semaphore mutex = new Semaphore(1); // Scoreboard kritik bölge koruması.
        Scoreboard scoreboard = new Scoreboard();

        new Thread(new Agent(agentSem, tobacco, paper, match), "Agent").start();

        new Thread(new PusherTobacco(tobacco, mutex, matchSmoker, paperSmoker, scoreboard), "Pusher-Tobacco").start();
        new Thread(new PusherPaper(paper, mutex, matchSmoker, tobaccoSmoker, scoreboard), "Pusher-Paper").start();
        new Thread(new PusherMatch(match, mutex, paperSmoker, tobaccoSmoker, scoreboard), "Pusher-Match").start();

        new Thread(new Smoker(SmokerType.TOBACCO, tobaccoSmoker, paperSmoker, matchSmoker, agentSem), "Smoker-Tobacco").start();
        new Thread(new Smoker(SmokerType.PAPER, tobaccoSmoker, paperSmoker, matchSmoker, agentSem), "Smoker-Paper").start();
        new Thread(new Smoker(SmokerType.MATCH, tobaccoSmoker, paperSmoker, matchSmoker, agentSem), "Smoker-Match").start();
    }
}
