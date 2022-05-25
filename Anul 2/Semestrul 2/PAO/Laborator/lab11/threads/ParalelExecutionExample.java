package threads;

public class ParalelExecutionExample {

    public static void main(String[] args) {

        Thread newThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        });

        newThread.start();

        for(int i=500; i<600;i++){
            System.out.println(Thread.currentThread().getName() + ": " + i);
        }

    }
}
