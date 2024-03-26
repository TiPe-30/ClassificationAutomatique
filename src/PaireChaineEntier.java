public class PaireChaineEntier {
    private String chaine;
    private int entier;
    public PaireChaineEntier(String chaine, int entier) {
        this.chaine = chaine;
        setEntier(entier);
    }
    public int getEntier() {
        return entier;
    }
    public String getChaine() {
        return chaine;
    }
    public void setEntier(int entier) {
        this.entier = entier;
    }
}