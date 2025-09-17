package functionalInterfaces;

public class Sum implements Operation {

    @Override
    public int execute(Integer n1, Integer n2) {
        return n1 + n2;
    }
}
