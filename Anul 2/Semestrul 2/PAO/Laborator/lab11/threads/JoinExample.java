package threads;

public class JoinExample {
    public static void main(String[] args) throws InterruptedException {

        Thread newThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        });

        newThread.start();
        System.out.println("New Thread started");

        System.out.println("Waiting for Thread 0 to finish");
        newThread.join();

        for (int i = 500; i < 600; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
        }
    }
}
