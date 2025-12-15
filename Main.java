import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        // Agent bir tur malzeme koyduktan sonra tekrar koymadan önce bekleyecek.
        Semaphore agentSem = new Semaphore(1);

        // Agent masaya malzeme koyunca ilgili "ingredient semaphore" release edilir.
        Semaphore tobacco = new Semaphore(0);
        Semaphore paper = new Semaphore(0);
        Semaphore match = new Semaphore(0);

        // Pushers doğru kombinasyonu görünce ilgili smoker'a sinyal verir.
        Semaphore tobaccoSmoker = new Semaphore(0); // (tobacco sahibi smoker) => paper+match geldiğinde uyanır
        Semaphore paperSmoker = new Semaphore(0);   // (paper sahibi smoker)   => tobacco+match geldiğinde uyanır
        Semaphore matchSmoker = new Semaphore(0);   // (match sahibi smoker)   => tobacco+paper geldiğinde uyanır

        // Scoreboard'a (isTobacco/isPaper/isMatch) aynı anda sadece 1 pusher erişsin diye mutex.
        Semaphore mutex = new Semaphore(1);

        // Scoreboard: masada şu an "tek başına" bekleyen malzeme var mı?
        Scoreboard scoreboard = new Scoreboard();

        // Agent
        new Thread(new Agent(agentSem, tobacco, paper, match), "Agent").start();

        // Pushers (her malzeme için bir tane)
        new Thread(new PusherTobacco(tobacco, mutex, matchSmoker, paperSmoker, scoreboard), "Pusher-Tobacco").start();
        new Thread(new PusherPaper(paper, mutex, matchSmoker, tobaccoSmoker, scoreboard), "Pusher-Paper").start();
        new Thread(new PusherMatch(match, mutex, paperSmoker, tobaccoSmoker, scoreboard), "Pusher-Match").start();

        // Smokers (her biri 1 malzemeye sonsuz sahip)
        new Thread(new Smoker(SmokerType.TOBACCO, tobaccoSmoker, paperSmoker, matchSmoker, agentSem), "Smoker-Tobacco").start();
        new Thread(new Smoker(SmokerType.PAPER, tobaccoSmoker, paperSmoker, matchSmoker, agentSem), "Smoker-Paper").start();
        new Thread(new Smoker(SmokerType.MATCH, tobaccoSmoker, paperSmoker, matchSmoker, agentSem), "Smoker-Match").start();
    }
}
