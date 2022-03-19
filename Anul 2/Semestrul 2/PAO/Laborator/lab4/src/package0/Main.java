package package0;

public class Main {

    public static void main(String[] args) {

        Animal a1 = new Dog();
        Animal a2 = new Cat();

        Animal[] animals = new Animal[2];
        animals[0] = a1;
        animals[1] = a2;

        for (int i = 0; i < animals.length; i++) {
            if(animals[i] instanceof Dog){
                ((Dog) animals[i]).bark();
            } else if (animals[i] instanceof Cat){
                ((Cat) animals[i]).meow();
            }
        }

        System.out.println(a1 instanceof Animal);
        System.out.println(a1 instanceof Object);
    }
}
