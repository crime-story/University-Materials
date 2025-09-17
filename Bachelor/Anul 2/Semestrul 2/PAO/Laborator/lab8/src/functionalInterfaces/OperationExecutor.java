package functionalInterfaces;

public class OperationExecutor {

    public void executeOperation(Integer n1, Integer n2, Operation operation) {
        System.out.println("Executing operation...");
        System.out.println(operation.execute(n1, n2));
    }

}
