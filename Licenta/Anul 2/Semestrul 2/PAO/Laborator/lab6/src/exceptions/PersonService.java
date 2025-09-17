package exceptions;

public class PersonService {

    public static void main(String[] args) {

        System.out.println("Person service start");

        Person person = new Person(null, 25);
        System.out.println(person.getUpperName());

        System.out.println("Person sevice end");

    }
}
