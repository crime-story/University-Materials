package functionalInterfaces;

import java.util.function.Consumer;

public class ConsumerExample {

    //in: Object T
    //out: nothing

    public static void main(String[] args) {

        Consumer<String> printerConsumer = (word) -> System.out.println(word);
        printerConsumer.accept("de afisat");
    }

}
