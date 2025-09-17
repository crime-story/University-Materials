package interfete;

public class Cat extends Animal implements HaveRase{

    private String rase;

    @Override
    public void speak() {

    }

    @Override
    public String getRase() {
        return rase;
    }
}
