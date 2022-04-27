package functionalInterfaces;

public class Main {

    public static void main(String[] args) {

        OperationExecutor operationExecutor = new OperationExecutor();
        operationExecutor.executeOperation(1,2, new Sum());

        operationExecutor.executeOperation(1, 2, new Operation() {
            @Override
            public int execute(Integer n1, Integer n2) {
                return n1-n2;
            }
        });

    }
}
