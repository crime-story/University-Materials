package com.company;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    static String method(String sir1, String sir2) {
        // Varianta 1 cu .join()
        String newString = String.join("", sir1, sir2);

        // Varianta 2 cu +
        // String newString = sir1 + sir2;

        // Varianta 3 cu .concat()
        // String newString = sir1.concat(sir2);

        return newString;
    }

    static boolean substring(String sir) {
        String substring = "si";
        if (sir.contains(substring)) {
            return true;
        }
        return false;
    }

    static StringBuilder reversedString(StringBuilder inputString) {
        return new StringBuilder(inputString).reverse();
    }

    static String middleString(String inputString) {
        int middleOfString = inputString.length() / 2;
        if (inputString.length() % 2 == 0) {
            char ch1 = inputString.charAt(middleOfString - 1);
            char ch2 = inputString.charAt(middleOfString);
            return ch1 + "" + ch2;
        }
        return inputString.substring(middleOfString, middleOfString + 1);
    }

    static String deleteChar(String inputString, int position) {
        return inputString.substring(0, position) + inputString.substring(position + 1);
    }

    static String removeDuplicates(String inputString) {
        char[] chars = inputString.toCharArray();
        Set<Character> charSet = new LinkedHashSet<Character>();
        for (char c : chars) {
            charSet.add(c);
        }
        StringBuilder sb = new StringBuilder();
        for (Character character : charSet) {
            sb.append(character);
        }
        return sb.toString();
    }

    static String reversedPhrase(String inputString) {
        if (inputString.equals(""))
            return "";
        else {
            String words[] = inputString.split("\\s");
            String reverseWord = "";
            for (String w : words) {
                StringBuilder sb = new StringBuilder(w);
                sb.reverse();
                reverseWord += sb.toString() + " ";
            }
            return reverseWord.trim();
        }
    }

    static String upperPhrase(String inputString) {
        char[] chars = inputString.toCharArray();
        String newString = Character.toUpperCase(chars[0]) + "";
        for (int i = 1; i < chars.length; i++) {
            if (chars[i - 1] == ' ') {
                newString += Character.toUpperCase(chars[i]);
            }
            else {
                newString += chars[i];
            }
        }
        return newString;
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Numarul exercitiului: ");
        int numarExercitiu = keyboard.nextInt();

        switch (numarExercitiu) {
            case 1:
                // 1. Scrieti o metoda care primeste ca argumente 2 Stringuri, si intoarce un String ce le contine pe cele 2 concatenate.
                // Ex: method("ab","cd") intoarce "abcd"
                System.out.print("Introduceti sir1: ");
                String sir1 = keyboard.next();
                System.out.print("Introduceti sir2: ");
                String sir2 = keyboard.next();
                System.out.println(method(sir1, sir2));
                break;
            case 2:
                // 2. Scrieti o metoda care sa verifice daca un String contine Stringul "si"
                System.out.print("Introduceti un sir pentru a verifica daca acesta contine subsirul 'si': ");
                String sir = keyboard.next();
                boolean ok = substring(sir);
                if (ok) {
                    System.out.println("DA! Sirul contine subsirul 'si'.");
                } else {
                    System.out.println("NU! Sirul nu contine subsirul 'si'.");
                }
                break;
            case 3:
                // 3. Scrieti o metoda care primeste ca argument un String si intoarce Stringul inversat ("abcd" devine "dcba")
                System.out.print("Introduceti un sir: ");
                StringBuilder inputString = new StringBuilder(keyboard.next());
                System.out.println("Sirul inversat este: " + reversedString(inputString));
                break;
            case 4:
                // 4. Scrieti o metoda care extrage mijlocul unui String (1 sau 2 caractere in functie de lungimea Stringului).
                // Ex: "abcd" devine "bc",  "abcde" devine "c".
                System.out.print("Introduceti un sir: ");
                String string = keyboard.next();
                System.out.println("Mijlocul sirului este: " + middleString(string));
                break;
            case 5:
                // 5. Scrieti o metoda care primeste un String si un int ca argumente. Eliminati caracterul de pe pozitia argumentului al doilea.
                // Ex: method("donkey",2) va intoarce "dokey"
                System.out.print("Introduceti un sir: ");
                String sirCitit = keyboard.next();
                System.out.print("Introduceti pozitia: ");
                int pozitie = keyboard.nextInt();
                System.out.println("Sirul fara caracterul de pe pozitia " + pozitie + " este " + deleteChar(sirCitit, pozitie));
                break;
            case 6:
                // 6. Scrieti un program care elimina caracterele duplicate dintr-un String. Ex: "mercenar" devine "mercna"
                System.out.print("Introduceti un sir: ");
                String string1 = keyboard.next();
                System.out.println("Sirul fara duplicate este: " + removeDuplicates(string1));
                break;
            case 7:
                // 7. Scrieti o metoda care primeste o propozitie (intr-un String), si intoarce aceeasi propozitie, cu toate cuvintele inversate.
                // ("stop smoking" devine "pots gnikoms")
                System.out.print("Introduceti o propozitie: ");
                keyboard.nextLine();
                String str = keyboard.nextLine();
                System.out.println("Propozitia inversate este: " + reversedPhrase(str));
                break;
            case 8:
                // 8. Scrieti o metoda care primeste o propozitie (intr-un String), si intoarce aceeasi propozitie, cu toate cuvintele incepand cu majuscula.
                System.out.print("Introduceti o propozitie: ");
                keyboard.nextLine();
                String str1 = keyboard.nextLine();
                System.out.println("Propozitia inversate este: " + upperPhrase(str1));
                break;
        }
    }
}
