package deadlock_solution;

import java.util.concurrent.Semaphore;

// Deadlock ihtimali gösteren naif çözüm: iki içici ayrı malzemeleri kapınca masa boşalmaz.
public class DeadlockSolution {
    public static void main(String[] args) {
        Semaphore tableEmpty = new Semaphore(1); // Başlangıçta masa boş.
        Semaphore tobacco = new Semaphore(0);
        Semaphore paper = new Semaphore(0);
        Semaphore match = new Semaphore(0);

        new Thread(new Agent(tableEmpty, tobacco, paper, match), "Agent").start();
        new Thread(new Smoker(SmokerType.TOBACCO, tobacco, paper, match, tableEmpty), "Smoker-Tobacco").start();
        new Thread(new Smoker(SmokerType.PAPER, tobacco, paper, match, tableEmpty), "Smoker-Paper").start();
        new Thread(new Smoker(SmokerType.MATCH, tobacco, paper, match, tableEmpty), "Smoker-Match").start();

        // Basit bir watchdog: ilerleme durursa semafor durumlarini ve thread state'lerini yazdir.
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(500);
                    boolean tableLocked = tableEmpty.availablePermits() == 0;
                    boolean allMaterialsTaken = tobacco.availablePermits() == 0
                            && paper.availablePermits() == 0
                            && match.availablePermits() == 0;

                    if (tableLocked && allMaterialsTaken) {
                        System.out.println("\n*** Ilerleme yok: masa dolu, malzeme semaforlari bekleniyor; deadlock ihtimali yuksek ***");
                        Thread.getAllStackTraces().forEach((t, st) ->
                                System.out.println(t.getName() + " -> " + t.getState()));
                        break;
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }, "Deadlock-Watchdog").start();
    }
}
