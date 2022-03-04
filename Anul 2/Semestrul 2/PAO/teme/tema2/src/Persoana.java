public class Persoana {
    private String nume;
    private int varsta;

    public void setData(String numeDat, int varstaData) {
        nume = numeDat;
        varsta = varstaData;
    }

    public String obtine_informatii() {
        return nume + " " + varsta;
    }
}
