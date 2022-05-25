package syncronised;

public class SyncronisedExample2 {

    public static void main(String[] args) throws InterruptedException {

        BankAccount2 bankAccount = new BankAccount2(1000);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bankAccount.decrease(600);
                System.out.println(bankAccount.getAmount());
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        bankAccount.anotherSyncronisedMethod();



    }
}
