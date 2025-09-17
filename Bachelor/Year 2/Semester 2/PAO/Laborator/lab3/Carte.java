public class Carte {

    String autor;
    String domeniu;
    int numarPagini;

    public Carte(String autor, String domeniu, int numarpagini) {
        this.autor = autor;
        this.domeniu = domeniu;
        this.numarPagini = numarpagini;
    }

    public Carte() {

    }

    public Carte(String autor) {
        this.autor = autor;
    }
}
