package syncronised;

public class SyncronisedExample {

    public static void main(String[] args) {

        BankAccount bankAccount = new BankAccount(1000);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bankAccount.decrease(600);
                System.out.println(bankAccount.getAmount());
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        bankAccount.decrease(600);
        System.out.println(bankAccount.getAmount());



    }

}
