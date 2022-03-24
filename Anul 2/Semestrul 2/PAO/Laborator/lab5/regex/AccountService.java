package regex;

import java.util.Scanner;

public class AccountService {

    public static void main(String[] args) {

        Account account = new Account();

        Scanner scanner = new Scanner(System.in);
        String email = null;
        while (true) {
            email = scanner.nextLine();
            account.setEmail(email);
        }
    }
}
