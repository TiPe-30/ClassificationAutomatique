import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
public class Categorie {

    private String nom; // le nom de la catégorie p.ex : sport, politique,...
    private ArrayList<PaireChaineEntier> lexique; //le lexique de la catégorie

    // constructeur
    public Categorie(String nom) {
        this.nom = nom;
        this.lexique = new ArrayList<>();
    }
    public String getNom() {
        return nom;
    }
    // initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
    public void initLexique(String nomFichier) {
        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file); String ligne, chaine;
            int entier, index;
            while (scanner.hasNextLine()){
                ligne = scanner.nextLine();
                index = ligne.indexOf(':');
                chaine = ligne.substring(0, index);
                entier = Integer.parseInt(ligne.substring(index+1));
                lexique.add(new PaireChaineEntier(chaine,entier));
            }
            scanner.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public int score(Depeche d) {
        int score = 0;
        for (int i = 0; i < d.getMots().size();i++){
            score += UtilitairePaireChaineEntier.entierPourChaine(lexique, d.getMots().get(i));
        }
        return score;
    }
    public int scoreNonTrie(Depeche d){
        int score = 0;
        for (int i = 0; i < d.getMots().size();i++){
            score += UtilitairePaireChaineEntier.entierPourChaineIter(lexique, d.getMots().get(i));
        }
        return score;
    }
}