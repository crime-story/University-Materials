public class Presedinte {

    private static Presedinte presedinte;

    private Presedinte() {

    }

    public static Presedinte getPresedinte() {

        if (presedinte == null) {
            presedinte = new Presedinte();
        }
        return presedinte;
    }


}
