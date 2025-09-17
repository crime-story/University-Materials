package threads;

public class NewThreadExample2 {

    public static void main(String[] args) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+": Hello");
            }
        };

        Thread thread = new Thread(runnable);

        thread.start();

    }
}
