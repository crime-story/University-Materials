package streams;

public class VarArgsExample {
    public static void main(String[] args) {
        method("1");
        method("1", "2");
        method();

        String[] array = new String[5];
        method(array);
    }

    public static void method(String... params) { // String[] params
        for (int i = 0; i < params.length; i++) {
            System.out.println(params[i]);
        }
    }
}
