package threads;

public class ThreadInterruptExample {

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(
                () -> {
                    for (int i = 0; i < 10000; i++) {
                        if(Thread.interrupted()){
                            System.out.println(Thread.currentThread().getName()+": Got interrupted. stopping...");
                            break;
                        }
                        System.out.println(Thread.currentThread().getName() + ": " + i);
                    }
                }
        );

        thread.start();

        Thread.sleep(10);
        System.out.println("Interrupting other thread...");
        thread.interrupt();
    }
}
