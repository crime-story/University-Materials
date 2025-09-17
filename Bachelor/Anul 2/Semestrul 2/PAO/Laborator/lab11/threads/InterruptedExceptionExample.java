package threads;

public class InterruptedExceptionExample {

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(
                () -> {
                    for (int i = 0; i < 10; i++) {
                        if(Thread.interrupted()){
                            System.out.println(Thread.currentThread().getName()+": Got interrupted. stopping...");
                            break;
                        }
                        System.out.println(Thread.currentThread().getName() + ": " + i);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println("Was sleeping, got interrupted");
                            break;
                        }
                    }
                }
        );

        thread.start();

        Thread.sleep(5000);
        System.out.println("Interrupting other thread...");
        thread.interrupt();
    }
}
