package collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ArrayListExample2 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("3");
        list.add("1");
        list.add("2");

        System.out.println("List before sort: " + list);

        list.sort(new StringComparator());

        System.out.println("List after sort: " + list);

        List<String> list2 = new ArrayList<>();
        list2.add("1");
        list2.add("2");
        list2.add("3");
        System.out.println("list.equals(list2): " + list.equals(list2)); // true

        System.out.println("Element on index 1: " + list.get(1)); // 2
        System.out.println("Index of 1: " + list.indexOf("1")); // 0
        System.out.println("Index of 10: " + list.indexOf("10")); // -1 pt ca nu se gaseste

        methodThatReceivesArrayAsParameter(list.toArray(new String[list.size()]));

        list.set(2, "100");
        System.out.println("List after set: " + list);
    }

    private static class StringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    private static void methodThatReceivesArrayAsParameter(String[] array) {
        // does something
        System.out.println("Print array: " + Arrays.toString(array));
    }
}
