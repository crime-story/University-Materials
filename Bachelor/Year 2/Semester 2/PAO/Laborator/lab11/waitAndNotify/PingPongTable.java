package waitAndNotify;

public class PingPongTable {

    private String lastCall = null;

    public synchronized void sayPing() throws InterruptedException {

        System.out.println("I want to say PING");

        if ("PING".equals(lastCall)) {
            wait();
        }
        System.out.println("PING");
        lastCall = "PING";

        notify();
    }

    public synchronized void sayPong() throws InterruptedException {

        System.out.println("I want to say PONG");

        if ("PONG".equals(lastCall)) {
            wait();
        }
        System.out.println("PONG");
        lastCall = "PONG";

        notify();
    }
}
