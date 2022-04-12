package collections;

import java.util.LinkedList;

public class LinkedListExample {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        list.add("1");
        list.addFirst("0");
        list.addLast("2");

        System.out.println("list: " + list);
        System.out.println("First: " + list.getFirst());
        System.out.println("Last: " + list.getLast());

        list.removeFirst();
        list.removeLast();

        System.out.println("List after removal: " + list);
    }
}
