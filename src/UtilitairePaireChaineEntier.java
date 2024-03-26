import java.util.ArrayList;
public class UtilitairePaireChaineEntier{
    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        if (listePaires.isEmpty() || listePaires.get(listePaires.size()-1).getChaine().compareTo(chaine) < 0){
            return -1;
        }else {
            int m,sup = listePaires.size()-1,inf = 0;
            while (inf < sup){
                m = (sup+inf)/2;
                if (listePaires.get(m).getChaine().compareTo(chaine) >= 0){
                    sup = m;
                }else {
                    inf = m+1;
                }
            }
            return listePaires.get(sup).getChaine().equals(chaine) ? sup : -1;
        }
    }
    public static int indicePourChaineString(ArrayList<String> listePaires, String chaine) {
        if (listePaires.isEmpty() || listePaires.get(listePaires.size()-1).compareTo(chaine) < 0){
            return -1;
        }else{
            int m,sup = listePaires.size()-1,inf = 0;
            while (inf < sup){
                m = (sup+inf)/2;
                if (listePaires.get(m).compareTo(chaine) >= 0){
                    sup = m;
                }else{
                    inf = m+1;
                }
            }
            return listePaires.get(sup).equals(chaine) ? sup : -1;
        }
    }
    public static int indicePourChaineIter(ArrayList<PaireChaineEntier> listePaires, String chaine){
            int i = 0;
            while (i < listePaires.size() && !listePaires.get(i).getChaine().equals(chaine)){
                i++;
            }
            return i < listePaires.size() ? i: -1;
    }
    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        if (listePaires.isEmpty() || listePaires.get(listePaires.size()-1).getChaine().compareTo(chaine) < 0){
            return -1;
        }else {
            int m,sup = listePaires.size(),inf = 0;
            while (inf < sup){
                m = (sup+inf)/2;
                if (listePaires.get(m).getChaine().compareTo(chaine) >= 0){
                    sup = m;
                }else {
                    inf = m+1;
                }
            }
            return listePaires.get(sup).getChaine().equals(chaine) ? listePaires.get(sup).getEntier() : 0;
        }
    }
    public static int entierPourChaineIter(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        int i = 0;
        while (i < listePaires.size() && !listePaires.get(i).getChaine().equals(chaine)){
            i++;
        }
        return i < listePaires.size() ? listePaires.get(i).getEntier() : 0;
    }
    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        int max = 0;
        for (int i = 1; i < listePaires.size();i++){
            if (listePaires.get(i).getEntier() > listePaires.get(max).getEntier()){
                max = i;
            }
        }
        return listePaires.get(max).getChaine();
    }
    private static void insereTri(ArrayList<PaireChaineEntier> paireChaineEntiers, PaireChaineEntier score){
        int i = 0;
        while (i < paireChaineEntiers.size() && paireChaineEntiers.get(i).getEntier() < score.getEntier()){
            i++;
        }
        paireChaineEntiers.add(i, score);
    }
    private static PaireChaineEntier ressemblence(Depeche depeche1, Depeche depeche2){
        int ressem = 0;
        for (String mot1: depeche1.getMots()){
            for (String mot2: depeche2.getMots()){
                if (indicePourChaineString(Classification.getMotVide(),mot1) == -1 && mot2.equals(mot1))ressem++;
            }
        }
        return new PaireChaineEntier(depeche2.getCategorie(),ressem);
    }
    public static String ClassementKNNDepeche(ArrayList<Depeche> lesDepeches, int indiceDepecheCours, int k){
        ArrayList<PaireChaineEntier> ScoreCategorieDep = new ArrayList<>();
        for (int i = indiceDepecheCours+1;i < lesDepeches.size();i++){
            insereTri(ScoreCategorieDep,ressemblence(lesDepeches.get(indiceDepecheCours), lesDepeches.get(i)));
        }for (int j = indiceDepecheCours-1;j >= 0;j--){
            insereTri(ScoreCategorieDep,ressemblence(lesDepeches.get(indiceDepecheCours), lesDepeches.get(j)));
        }
        return classement(vecteurScore(ScoreCategorieDep, k));
    }
    private static ArrayList<PaireChaineEntier> vecteurScore (ArrayList<PaireChaineEntier> ScoreCatDep, int k){
        ArrayList<PaireChaineEntier> score = new ArrayList<>();
        for (int i = ScoreCatDep.size()-k;i < ScoreCatDep.size();i++){
            score.add(ScoreCatDep.get(i));
        }
        return score;
    }
    private static String classement(ArrayList<PaireChaineEntier> meilleurs){
        if(meilleurs.size() == 1){
            return meilleurs.get(0).getChaine();
        }else if(meilleurs.size() == 2){
            if(meilleurs.get(meilleurs.size()-2).getChaine().equals(meilleurs.get(meilleurs.size()-1).getChaine())){
                return meilleurs.get(meilleurs.size()-2).getChaine();
            }else{
                return meilleurs.get(meilleurs.size()-1).getChaine();
            }
        }else{
            return meilleurClassement(meilleurs);
        }
    }
    private static String meilleurClassement(ArrayList<PaireChaineEntier> meilleur){
        ArrayList<PaireChaineEntier> scoreChoix = new ArrayList<>();
        scoreChoix.add(new PaireChaineEntier(meilleur.get(0).getChaine(),0));int indice;
        for (int i = 1; i < meilleur.size();i++){
            indice = indicePourChaineIter(scoreChoix, meilleur.get(i).getChaine());
            if (indice == -1){
                scoreChoix.add(new PaireChaineEntier(meilleur.get(i).getChaine(), 0));
            }else{
                scoreChoix.get(indice).setEntier(scoreChoix.get(indice).getEntier()+1);
            }
        }
        int entierMax = EntierMaxBestClas(scoreChoix);
        if (tout0(scoreChoix)){
            return chaineMax(meilleur);
        }else if(scoreChoix.size() == 1){
            return scoreChoix.get(0).getChaine();
        }else if(!doublon(scoreChoix,entierMax)){
            return chaineMax(scoreChoix);
        }else{
            return egaliteValueScoreDepartagée(egaliteValue(scoreChoix,entierMax), meilleur);
        }
    }
    private static boolean tout0(ArrayList<PaireChaineEntier> liste){
        int i = 0;
        while (i < liste.size() && liste.get(i).getEntier() == 0){
            i++;
        }
        return i == liste.size();
    }

    private static boolean doublon(ArrayList<PaireChaineEntier> scoreChoix, int entMax){
        int i = 0, j = 1;
        boolean doublon = false;
        while (i < scoreChoix.size()-2 && !doublon){
            doublon = (scoreChoix.get(i).getEntier() == scoreChoix.get(j).getEntier() && scoreChoix.get(i).getEntier() == entMax);
            if (j == scoreChoix.size()-1){
                i++;
                j = i;
            }
            j++;
        }
        return doublon;
    }
    private static int EntierMaxBestClas(ArrayList<PaireChaineEntier> meilleursClassement){
        int max = 0;
        for (int i = 1; i < meilleursClassement.size();i++){
            if (meilleursClassement.get(i).getEntier() > meilleursClassement.get(max).getEntier()){
                max = i;
            }
        }
        return meilleursClassement.get(max).getEntier();
    }
    private static ArrayList<PaireChaineEntier> egaliteValue(ArrayList<PaireChaineEntier> bestClasse, int entMax){
        ArrayList<PaireChaineEntier> egalite = new ArrayList<>();
        for (int i = 0; i < bestClasse.size();i++){
            if (bestClasse.get(i).getEntier() == entMax) egalite.add(new PaireChaineEntier(bestClasse.get(i).getChaine(),0));
        }
        return egalite;
    }
    private static String egaliteValueScoreDepartagée(ArrayList<PaireChaineEntier> egaliteValue,ArrayList<PaireChaineEntier> meilleur){
        for (int i = 0; i< egaliteValue.size();i++){
            for (int j = 0; j < meilleur.size();j++){
                if (meilleur.get(j).getChaine().equals(egaliteValue.get(i).getChaine())){
                    egaliteValue.get(i).setEntier(egaliteValue.get(i).getEntier()+meilleur.get(j).getEntier());
                }
            }
        }
        return chaineMax(egaliteValue);
    }
}