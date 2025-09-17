package collections;

import java.util.ArrayList;
import java.util.List;

public class ArrayListExample {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("string1");
        list.add("string2");
        /*
        System.out.println(list); // afiseaza [string1, string2]
        */

        for (String element : list) {
            System.out.println(element);
        }
        System.out.println("List size is: " + list.size());

        list.add(1, "string3");
        System.out.println("Lista dupa inserare string3: " + list); // afiseaza [string1, string3, string2]

        List<String> list2 = new ArrayList<>();
        list2.add("list2OriginalElement");
        list2.addAll(list);
        System.out.println("List2: " + list2); // afiseaza [list2OriginalElement, string1, string3, string2]

        list2.clear();
        System.out.println("List2 after clear method: " + list2);

        System.out.println("List contains string1? " + list.contains("string1")); // true
        System.out.println("List contains someOtherString? " + list.contains("someOtherString")); // false

        list.remove(0);
        System.out.println("List after removing index = 0: " + list);
        list.remove("string3");
        System.out.println("List after removing string3: " + list);

    }
}
