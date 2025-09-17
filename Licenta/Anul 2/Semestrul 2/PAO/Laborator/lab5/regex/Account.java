package regex;

public class Account {

    private String username;
    private String email;

    public void setEmail(String email) {

        if (email.matches("[a-zA-Z0-9]+@(gmail|yahoo)\\.[a-z]+")) {
            System.out.println("Email valid:" + email);
            this.email = email;
        } else {
            System.out.println("Email invalid: " + email);
        }

    }
}
