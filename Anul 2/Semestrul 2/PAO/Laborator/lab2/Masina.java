public class Masina {

    int anFabricatie;
    String culoare;
    boolean noua;

    public void start() {
        System.out.println("Start! Culoarea:" + culoare);
    }

    public void schimbaCuloarea(String culoareNoua) {
        System.out.println("Se schimba culoarea in " + culoareNoua);
        culoare = culoareNoua;
    }

    public String obtineInformatii(){
        return anFabricatie + " " + culoare + " " + noua;
    }

}
