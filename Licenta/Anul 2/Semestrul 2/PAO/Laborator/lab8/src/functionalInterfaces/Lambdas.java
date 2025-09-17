package functionalInterfaces;

public class Lambdas {

    public static void main(String[] args) {

        //  () -> {}
        // (n1,n2) -> n1+n2;

        OperationExecutor operationExecutor = new OperationExecutor();
        operationExecutor.executeOperation(1, 2, (n1, n2) -> n1 + n2);
        operationExecutor.executeOperation(1, 2, (n1, n2) -> {
                    System.out.println("Making a sum");
                    return n1 + n2;
                }
        );

    }
}
