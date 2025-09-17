package threads;

public class NewThreadExample {

    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName()+": Hello");

        MyThread myThread = new MyThread();
        myThread.start();

        System.out.println(Thread.currentThread().getName()+": Still here");

    }
}
