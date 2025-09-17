public class Exercitiul6 {

    public static void main(String[] args) {

        Masina masina = new Masina();
        masina.culoare = "Rosu";
        masina.anFabricatie = 2000;
        masina.noua = true;

        System.out.println(masina.obtineInformatii());

        Masina masina2 = new Masina();
        masina2.culoare = "Alba";
        masina2.anFabricatie = 2010;
        masina2.noua = false;

        masina2.schimbaCuloarea("Neagra");

        System.out.println(masina2.obtineInformatii());

        masina.start();
        masina2.start();
    }
}
