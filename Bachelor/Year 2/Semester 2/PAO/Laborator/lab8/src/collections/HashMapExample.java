package collections;

import java.util.*;

public class HashMapExample {

    public static void main(String[] args) {

        Map<Integer, List<String>> map = new HashMap<>();

        Set<String> words = new HashSet<>();
        words.add("word");
        words.add("tree");
        words.add("and");

        for (String word : words) {
            int length = word.length();
            if (map.containsKey(length)) {
                List<String> list = map.get(length);
                list.add(word);
            } else {
                List<String> list = new ArrayList<>();
                list.add(word);
                map.put(length, list);
            }
        }

        System.out.println(map);
    }
}
