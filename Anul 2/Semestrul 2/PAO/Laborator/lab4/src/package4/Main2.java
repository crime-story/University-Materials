package package4;

public class Main2 {

    public static void main(String[] args) {

        String string = "abcdefghijklmn";
        int numarCaractere = 5;

        while(string.length() >= numarCaractere) {
            String substring = string.substring(0, numarCaractere);
            System.out.println(substring);
            string = string.substring(numarCaractere);
        }
        System.out.println(string);
    }
}
