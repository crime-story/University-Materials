package functionalInterfaces;

import java.util.function.Predicate;

public class PredicateExample {

    // in: Object T
    // out: boolean

    public static void main(String[] args) {

        Predicate<String> isEmptyPredicate = (word) -> word.isEmpty();
        Predicate<String> isEmptyPredicate2 = word -> word.isEmpty();
        Predicate<String> isValidLength = (String cnp)-> cnp.length() == 14;

        usesPredicate("cuvant", isEmptyPredicate);
        usesPredicate("", isEmptyPredicate);

        System.out.println(isValidLength.test("1920504061235"));

    }

    public static void usesPredicate(String string, Predicate<String> predicate){
        System.out.println(predicate.test(string));
    }
}
