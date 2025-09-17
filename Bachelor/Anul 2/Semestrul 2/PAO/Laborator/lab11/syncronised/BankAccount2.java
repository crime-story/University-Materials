package syncronised;

public class BankAccount2 {

    private int amount;
    private Object monitor = new Object();

    public BankAccount2(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void decrease(int amountToDecrease) {

        System.out.println("Attempting to decrease: " + amountToDecrease);

        synchronized (monitor) {
            if (amount < amountToDecrease) {
                System.out.println("Failed!");
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                amount = amount - amountToDecrease;
                System.out.println("Success!");
            }
        }
    }

    public void anotherSyncronisedMethod() throws InterruptedException {

        System.out.println("Starting another sync block");

        synchronized (this){
            System.out.println("Inside sync block");
            Thread.sleep(3000);
            System.out.println("Finished sync block");
        }
    }
}
