import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Classification {
    private static ArrayList<String> motVide = new ArrayList<>();
    public static ArrayList<String> getMotVide() {
        return motVide;
    }
    private static void InitMotVide() {
        try{
            FileInputStream file = new FileInputStream("./MotVide.txt");
            Scanner scanner = new Scanner(file);
            String chaine;
            while (scanner.hasNextLine()){
                chaine = scanner.nextLine();
                MotInsere(chaine);
            }
            scanner.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void MotInsere(String mot) {
        int i = 0;
        while (i < motVide.size() && motVide.get(i).compareTo(mot) < 0) {
            i++;
        }
        motVide.add(i, mot);
    }
    private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
        //creation d'un tableau de dépêches
        ArrayList<Depeche> depeches = new ArrayList<>();
        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);
            String ligne, id, date, categorie, lignes;
            while (scanner.hasNextLine()) {
                ligne = scanner.nextLine();
                id = ligne.substring(3);
                ligne = scanner.nextLine();
                date = ligne.substring(3);
                ligne = scanner.nextLine();
                categorie = ligne.substring(3);
                ligne = scanner.nextLine();
                lignes = ligne.substring(3);
                while (scanner.hasNextLine() && !ligne.equals("")) {
                    ligne = scanner.nextLine();
                    if (!ligne.equals("")){
                        lignes = lignes + '\n' + ligne;
                    }
                }
                depeches.add(new Depeche(id, date, categorie, lignes));
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return depeches;
    }
    public static void classementDepechesKNN(ArrayList<Depeche> depeches, String nomFichier, int k) {
        float scoreTrueFalse;
        float scoreCulture = 0, verifCulture = 0,
                scoreEconomie = 0, verifEconomie = 0,
                scoreScience = 0, verifScience = 0,
                scorePolitique = 0, verifPolitique = 0,
                scoreSport = 0, verifSport = 0;
        String catDepeche;
        try {
            FileWriter file = new FileWriter(nomFichier);
            for (int i = 0; i < depeches.size(); i++) {
                catDepeche = UtilitairePaireChaineEntier.ClassementKNNDepeche(depeches, i, k);
                if (depeches.get(i).getCategorie().equals(catDepeche)) {
                    scoreTrueFalse = 1;
                } else {
                    scoreTrueFalse = 0;
                }
                switch (depeches.get(i).getCategorie()) {
                    case "SPORTS":
                        scoreSport += scoreTrueFalse;
                        verifSport++;
                        break;
                    case "POLITIQUE":
                        scorePolitique += scoreTrueFalse;
                        verifPolitique++;
                        break;
                    case "ECONOMIE":
                        scoreEconomie += scoreTrueFalse;
                        verifEconomie++;
                        break;
                    case "ENVIRONNEMENT-SCIENCES":
                        scoreScience += scoreTrueFalse;
                        verifScience++;
                        break;
                    case "CULTURE":
                        scoreCulture += scoreTrueFalse;
                        verifCulture++;
                        break;
                }
                file.write((i + 1) + ":" + catDepeche + "\n");
            }
            file.write("\n CULTURE :                   " + (scoreCulture * 100.0f) / verifCulture + "%");
            file.write("\n ECONOMIE :                  " + (scoreEconomie * 100.0f) / verifEconomie + "%");
            file.write("\n ENVIRONNEMENT-SCIENCES :    " + (scoreScience * 100.0f) / verifScience + "%");
            file.write("\n POLITIQUE :                 " + (scorePolitique * 100.0f) / verifPolitique + "%");
            file.write("\n SPORTS :                    " + (scoreSport * 100.0f) / verifSport + "%");
            file.write("\n MOYENNE :                   " + (((scoreCulture * 100.0f) / verifCulture) + ((scoreEconomie * 100.0f) / verifEconomie)
                    + ((scoreScience * 100.0f) / verifScience) + ((scorePolitique * 100.0f) / verifPolitique) + ((scoreSport * 100.0f) / verifSport)) / 5 + "%");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
        float scoreTrueFalse;
        ArrayList<PaireChaineEntier> lesScores = new ArrayList<>();
        float scoreCulture = 0, verifCulture = 0,
                scoreEconomie = 0, verifEconomie = 0,
                scoreScience = 0, verifScience = 0,
                scorePolitique = 0, verifPolitique = 0,
                scoreSport = 0, verifSport = 0;
        String catDepeche;
        try {
            FileWriter file = new FileWriter(nomFichier);
            for (int i = 0; i < depeches.size(); i++) {
                for (Categorie cat : categories) {
                    lesScores.add(new PaireChaineEntier(cat.getNom(), cat.score(depeches.get(i))));
                }
                catDepeche = UtilitairePaireChaineEntier.chaineMax(lesScores);
                if (depeches.get(i).getCategorie().equals(catDepeche)) {
                    scoreTrueFalse = 1;
                } else {
                    scoreTrueFalse = 0;
                }
                switch (depeches.get(i).getCategorie()) {
                    case "SPORTS":
                        scoreSport += scoreTrueFalse;
                        verifSport++;
                        break;
                    case "POLITIQUE":
                        scorePolitique += scoreTrueFalse;
                        verifPolitique++;
                        break;
                    case "ECONOMIE":
                        scoreEconomie += scoreTrueFalse;
                        verifEconomie++;
                        break;
                    case "ENVIRONNEMENT-SCIENCES":
                        scoreScience += scoreTrueFalse;
                        verifScience++;
                        break;
                    case "CULTURE":
                        scoreCulture += scoreTrueFalse;
                        verifCulture++;
                        break;
                }
                file.write((i + 1) + ":" + catDepeche + "\n");
                lesScores.clear();
            }
            file.write("\n CULTURE :                   " + (scoreCulture * 100.0f) / verifCulture + "%");
            file.write("\n ECONOMIE :                  " + (scoreEconomie * 100.0f) / verifEconomie + "%");
            file.write("\n ENVIRONNEMENT-SCIENCES :    " + (scoreScience * 100.0f) / verifScience + "%");
            file.write("\n POLITIQUE :                 " + (scorePolitique * 100.0f) / verifPolitique + "%");
            file.write("\n SPORTS :                    " + (scoreSport * 100.0f) / verifSport + "%");
            file.write("\n MOYENNE :                   " + (((scoreCulture * 100.0f) / verifCulture) + ((scoreEconomie * 100.0f) / verifEconomie)
                    + ((scoreScience * 100.0f) / verifScience) + ((scorePolitique * 100.0f) / verifPolitique) + ((scoreSport * 100.0f) / verifSport)) / categories.size() + "%");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void classementDepechesManual(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
        float scoreTrueFalse;
        ArrayList<PaireChaineEntier> lesScores = new ArrayList<>();
        float scoreCulture = 0, verifCulture = 0, scoreEconomie = 0, verifEconomie = 0,
                scoreScience = 0, verifScience = 0, scorePolitique = 0, verifPolitique = 0,
                scoreSport = 0, verifSport = 0;
        int i = 0;
        String catDepeche;
        try {
            FileWriter file = new FileWriter(nomFichier);
            for (Depeche depeche : depeches) {
                for (Categorie cat : categories) {
                    lesScores.add(new PaireChaineEntier(cat.getNom(), cat.scoreNonTrie(depeche)));
                }
                catDepeche = UtilitairePaireChaineEntier.chaineMax(lesScores);
                if (depeche.getCategorie().equals(catDepeche)) {
                    scoreTrueFalse = 1;
                } else {
                    scoreTrueFalse = 0;
                }
                switch (depeche.getCategorie()) {
                    case "SPORTS":
                        scoreSport += scoreTrueFalse;
                        verifSport++;
                        break;
                    case "POLITIQUE":
                        scorePolitique += scoreTrueFalse;
                        verifPolitique++;
                        break;
                    case "ECONOMIE":
                        scoreEconomie += scoreTrueFalse;
                        verifEconomie++;
                        break;
                    case "ENVIRONNEMENT-SCIENCES":
                        scoreScience += scoreTrueFalse;
                        verifScience++;
                        break;
                    case "CULTURE":
                        scoreCulture += scoreTrueFalse;
                        verifCulture++;
                        break;
                }
                file.write((i + 1) + ":" + catDepeche + "\n");
                i++;
                lesScores.clear();
            }
            file.write("\n CULTURE :                   " + (scoreCulture * 100.0f) / verifCulture + "%");
            file.write("\n ECONOMIE :                  " + (scoreEconomie * 100.0f) / verifEconomie + "%");
            file.write("\n ENVIRONNEMENT-SCIENCES :    " + (scoreScience * 100.0f) / verifScience + "%");
            file.write("\n POLITIQUE :                 " + (scorePolitique * 100.0f) / verifPolitique + "%");
            file.write("\n SPORTS :                    " + (scoreSport * 100.0f) / verifSport + "%");
            file.write("\n MOYENNE :                   " + (((scoreCulture * 100.0f) / verifCulture) + ((scoreEconomie * 100.0f) / verifEconomie)
                    + ((scoreScience * 100.0f) / verifScience) + ((scorePolitique * 100.0f) / verifPolitique) + ((scoreSport * 100.0f) / verifSport)) / categories.size() + "%");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        for (Depeche depeche : depeches) {
            if (depeche.getCategorie().equals(categorie)) {
                for (String mots : depeche.getMots()) {
                    if (UtilitairePaireChaineEntier.indicePourChaineString(motVide, mots) == -1 && UtilitairePaireChaineEntier.indicePourChaine(resultat, mots) == -1) {
                        insereTrie(resultat, new PaireChaineEntier(mots, 0));
                    }
                }
            }
        }
        calculScores(depeches, categorie, resultat);
        return resultat;
    }
    private static void insereTrie(ArrayList<PaireChaineEntier> lexique, PaireChaineEntier insere) {
        int i = 0;
        while (i < lexique.size() && insere.getChaine().compareTo(lexique.get(i).getChaine()) > 0) {
            i++;
        }
        lexique.add(i, insere);
    }
    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        int indice;
        for (Depeche lesDepeches : depeches) {
            for (String mots : lesDepeches.getMots()) {
                indice = UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, mots);
                if (indice != -1){
                    if (lesDepeches.getCategorie().equals(categorie)) {
                        dictionnaire.get(indice).setEntier(dictionnaire.get(indice).getEntier() + 1);
                    } else {
                        dictionnaire.get(indice).setEntier(dictionnaire.get(indice).getEntier() - 1);
                    }
                }
            }
        }
    }
    public static int poidsPourScore(int score) {
        if (score < 2) {
            return 1;
        } else if (score < 4) {
            return 2;
        } else {
            return 3;
        }
    }
    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        try {
            FileWriter lexique = new FileWriter(nomFichier);
            for (PaireChaineEntier motsPoids : initDico(depeches, categorie)) {
                lexique.write(motsPoids.getChaine() + ":" + poidsPourScore(motsPoids.getEntier()) + "\n");
            }
            lexique.close();
            System.out.println("Le lexique " + categorie + " a bien été écrits !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void execManual(ArrayList<Categorie> Categorie, String NomFichierResult) {
        System.out.println("***********MODE MANUEL**************\n");
        long startTime1 = System.currentTimeMillis();
        for (Categorie categorie : Categorie) {
            categorie.initLexique("./LexiqueManual/" + categorie.getNom() + ".txt");
            System.out.println("Le lexique " + categorie.getNom() + " chargée avec succès !");
        }
        long endTime1 = System.currentTimeMillis();
        long startTime2 = System.currentTimeMillis();
        classementDepechesManual(lectureDepeches("./test.txt"), Categorie, NomFichierResult);
        long endTime2 = System.currentTimeMillis();
        System.out.println("\nLe chargement des lexiques a durée : " + (endTime1 - startTime1) + " ms");
        System.out.println("Le classement du lexique a durée : " + (endTime2 - startTime2) + " ms\n");
        System.out.println("**********FIN MODE MANUEL***********");
    }
    private static void execAutomatique(ArrayList<Depeche> depeches, ArrayList<Categorie> Categorie, String NomFichierResult) {
        System.out.println("\n\n**********MODE AUTOMATIQUE***********\n");
        long startTime1 = System.currentTimeMillis();
        for (Categorie categorie : Categorie) {
            generationLexique(depeches, categorie.getNom(), "./LexiqueAuto/" + categorie.getNom() + "-Automatique.txt");
            categorie.initLexique("./LexiqueAuto/" + categorie.getNom() + "-Automatique.txt");
        }
        long endTime1 = System.currentTimeMillis();
        depeches = lectureDepeches("./test.txt");
        long startTime2 = System.currentTimeMillis();
        classementDepeches(depeches, Categorie, NomFichierResult);
        long endTime2 = System.currentTimeMillis();
        System.out.println("\nLe chargement, et l'initialisation des lexiques a durée : " + (endTime1 - startTime1) + " ms");
        System.out.println("Le classement du lexique a durée : " + (endTime2 - startTime2) + " ms\n");
        System.out.println("*********FIN MODE AUTOMATIQUE*********");
    }
    private static void execKNN(String nomFichAppr, String NomFichierResult, int k) {
        System.out.println("\n**********MODE APPRENTISSAGE KNN*************");
        long startTime2 = System.currentTimeMillis();
        classementDepechesKNN(lectureDepeches(nomFichAppr), NomFichierResult, k);
        long endTime2 = System.currentTimeMillis();
        System.out.println("\n\nLe classement KNN a durée : " + (endTime2 - startTime2) + " ms\n\n");
        System.out.println("*******************FIN SAE*******************");
    }

    public static void main(String[] args) {
        Scanner lecteur = new Scanner(System.in);
        InitMotVide();
        System.out.println("chargement des dépêches");
        ArrayList<Categorie> Categorie = new ArrayList<>();
        Categorie.add(new Categorie("SPORTS"));
        Categorie.add(new Categorie("POLITIQUE"));
        Categorie.add(new Categorie("ECONOMIE"));
        Categorie.add(new Categorie("ENVIRONNEMENT-SCIENCES"));
        Categorie.add(new Categorie("CULTURE"));
        execManual(Categorie, "./LexiqueManual/ResultatMan.txt");
        System.out.print("\n\nTapez une lettre pour continuer.....  ");
        lecteur.nextLine();
        execAutomatique(lectureDepeches("./depeches.txt"), Categorie, "./LexiqueAuto/ResultatAuto.txt");
        System.out.print("\n\nVoulez vous voir les résultats de l'execution KNN ? : (Tapez Y si vous voulez continuer ! ) : ");
        if (lecteur.next().charAt(0) == 'y') {
            lecteur.nextLine();
            System.out.print("Entrez pour quelle valeur de k voulez vous faire une demo : ");
            execKNN("./test.txt", "./ResultatKNN", lecteur.nextInt());
        }
    }
}