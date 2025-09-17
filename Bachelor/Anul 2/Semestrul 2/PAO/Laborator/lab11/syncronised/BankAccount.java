package syncronised;

public class BankAccount {

    private int amount;

    public BankAccount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public synchronized void decrease(int amountToDecrease) {

        System.out.println("Attempting to decrease: " + amountToDecrease);

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
