import java.util.ArrayList;
public class Depeche {
    private String id;
    private String date;
    private String categorie;
    private String contenu;
    private ArrayList<String> mots;
    public Depeche(String id, String date, String categorie, String contenu) {
        setId(id);
        setDate(date);
        setCategorie(categorie);
        setContenu(contenu);
        setMots(decoupeEnMots(contenu));
    }
    private ArrayList<String> decoupeEnMots(String contenu) {
        String chaine = contenu.toLowerCase();
        chaine = chaine.replace('\n', ' ');
        chaine = chaine.replace('.', ' ');
        chaine = chaine.replace(',', ' ');
        chaine = chaine.replace(':', ' ');
        chaine = chaine.replace('\'', ' ');
        chaine = chaine.replace('\"', ' ');
        chaine = chaine.replace('(', ' ');
        chaine = chaine.replace(')', ' ');
        String[] tabchaine = chaine.split(" ");
        ArrayList<String> resultat = new ArrayList<>();
        for (int i = 0; i < tabchaine.length; i++) {
            if (!tabchaine[i].equals("")) {
                resultat.add(tabchaine[i]);
            }
        }
        return resultat;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public ArrayList<String> getMots() {
        return mots;
    }

    public void setMots(ArrayList<String> mots) {
        this.mots = mots;
    }
}

