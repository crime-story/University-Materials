package package3;

/**
 * compozitie
 */
public class Person {

    private Wallet wallet;

    public Person(float amount){
        wallet = new Wallet();
        wallet.setAmount(amount);
    }

    public float getAmount(){
        return wallet.getAmount();
    }

    public String getProducer(){
        return wallet.getProducer();
    }
}
