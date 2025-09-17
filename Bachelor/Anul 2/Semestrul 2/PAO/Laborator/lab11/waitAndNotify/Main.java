package waitAndNotify;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        PingPongTable pingPongTable = new PingPongTable();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        pingPongTable.sayPing();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread t = new Thread(runnable);
        t.start();

        while (true){
            pingPongTable.sayPong();
            Thread.sleep(1000);
        }

    }
}
