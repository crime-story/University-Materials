public class Exercitiul7 {

    public static void main(String[] args) {

        // initializare
        Masina masina = new Masina();
        masina.culoare = "Alba";
        masina.anFabricatie= 2000;

        Masina masina2 = new Masina();
        System.out.println("masina:" + masina.obtineInformatii());
        System.out.println("masina2:" + masina2.obtineInformatii());

        // ambele referinte catre acelasi obiect
        masina2 = masina;
        System.out.println();
        System.out.println("masina:" + masina.obtineInformatii());
        System.out.println("masina2:" + masina2.obtineInformatii());


        // modificarile obiectului se pot face folosind ambele referinte
        System.out.println("Modificari");
        masina.noua=true;
        System.out.println("masina:" + masina.obtineInformatii());
        System.out.println("masina2:" + masina2.obtineInformatii());

        // =null e folosit pentru a sterge legatura dintre o referinta si un obiect de pe HEAP
        masina = null;
        System.out.println("masina2:" + masina2.obtineInformatii());
        System.out.println("masina:" + masina.obtineInformatii());
    }
}
