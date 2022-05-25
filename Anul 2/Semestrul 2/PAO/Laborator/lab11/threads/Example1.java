package threads;

public class Example1 {

    public static void main(String[] args) {

        System.out.println("Instructiune1");
        System.out.println("Instructiune2");

        System.out.println(Thread.currentThread().getName());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("back");

    }
}
