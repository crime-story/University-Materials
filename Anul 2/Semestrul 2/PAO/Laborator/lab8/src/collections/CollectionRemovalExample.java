package collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CollectionRemovalExample {

    public static void main(String[] args) {

        Set<String> set = new HashSet<>();
        set.add("1");
        set.add("2");
        set.add("3");

        // generates concurrentModificationExeption

//        for(String element: set){
//            System.out.println(element);
//            if (element.equals("2")) {
//                set.remove("2");
//            }
//        }

        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            if (element.equals("2")) {
                iterator.remove();
            }
        }
        System.out.println(set);
    }
}
