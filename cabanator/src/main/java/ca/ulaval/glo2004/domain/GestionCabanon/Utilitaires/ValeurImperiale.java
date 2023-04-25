package ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires;

public class ValeurImperiale {
    private static final String UNITE_POUCE = "\"";
    private static final String UNITE_PIED = "'";
    private static final double DIVISEUR = 32;
    private double distance;

    /**
     * Prend en parametre une distance en pouce.
     *
     * @param distance distance en pouce
     */
    public ValeurImperiale(double distance) {
        this.distance = distance;
    }

    /**
     * Permet d'obtenir la distance (pouces) en double
     *
     * @return return distance en pouce (double)
     */
    public double getDistanceDouble() {
        return this.distance;
    }

    /**
     * NE PAS UTILISER EN DEHORS DU DOMAINE.
     *
     * @param distance distance
     */
    public void setDistanceDouble(double distance) {
        this.distance = distance;
    }

    /**
     * Permet d'obtenir la distance sous forme de string. (pied' pouce x/x")
     *
     * @return String de la valeur avec les pieds pouces et fraction.
     */
    public String getDistanceString() {
        return convertirEnFraction(this.distance);
    }

    /**
     * Permet de set la distance avec un string.
     * Doit respecter ce format : x' x x/x"
     * <p>
     * ex : 20' 3 1/4" ou 20' 3/4" ou 3/4" etc...
     * </p>
     *
     * @param distance La distance en string formater (pied pouce et fraction)
     */
    public void setDistanceString(String distance) {
        this.distance = convertirStringEnDouble(distance);
    }

    public static double convertirStringEnDouble(String valeur) {
        double resultat = 0;
        String valeurTrim = valeur.trim();

        // contien des pieds
        if (valeurTrim.contains("'") && valeurTrim.length() > 1) {
            int indexPied = valeurTrim.indexOf("'");
            String entier = valeurTrim.substring(0, indexPied).trim();
            resultat += Double.parseDouble(entier) * 12;

            // retire la partie en pied
            valeurTrim = valeurTrim.substring(indexPied + 1).trim();
        }

        // contien des pouces et/ou des fractions de pouces
        if (valeurTrim.contains("\"") && valeurTrim.length() > 1) {
            String strPouce = valeurTrim.trim();

            int espaceIndex = strPouce.indexOf(" ");
            int slashIndex = strPouce.indexOf("/");
            int coteIndex = strPouce.indexOf("\"");
            if (slashIndex > 0) { // il y a une fraction
                if (espaceIndex > 0) { // il y a un entier
                    String entier = strPouce.substring(0, coteIndex);
                    resultat += Double.parseDouble(entier);

                    strPouce = strPouce.substring(espaceIndex).trim();
                }
                String[] fractionSeparer = strPouce.split("/");

                resultat += (Double.parseDouble(fractionSeparer[0]) / Double.parseDouble(fractionSeparer[1]));
            } else { // entier
                resultat += Double.parseDouble(strPouce.substring(0, strPouce.length() -1));
            }
        }

        return resultat;
    }

    public static String convertirEnFraction(double valeur) {
        double poucesArrondis = Math.round(valeur * DIVISEUR) / DIVISEUR;
        int entier = (int) poucesArrondis;
        int numerateur = (int) ((poucesArrondis - entier) * DIVISEUR);
        int denominateur = 32;

        int restePouces = entier % 12;
        int pied = (entier - restePouces) / 12;

        String resultat = "";

        if (pied > 0) {
            resultat = String.format("%d%s", pied, UNITE_PIED);
        } else {
            resultat = String.format("0%s ", UNITE_PIED);
        }

        resultat = String.format("%s%s", resultat, pied > 0 ? " " : "");

        if (numerateur == 0) {
            if (pied <= 0 || restePouces != 0) {
                resultat = String.format("%s%d%s", resultat, restePouces, UNITE_POUCE);
            }
        } else if (numerateur == 16) {
            if (restePouces == 0) {
                resultat = String.format("%s0%s 1/2", resultat, UNITE_POUCE);
            } else {
                resultat = String.format("%s%d%s 1/2", resultat, restePouces, UNITE_POUCE);
            }
        } else {
            int pgdc = getPlusGrandDiviseurCommun(numerateur, denominateur);
            numerateur /= pgdc;
            denominateur /= pgdc;

            if (restePouces == 0) {
                resultat = String.format("%s0%s %d/%d",
                        resultat, UNITE_POUCE, numerateur, denominateur);
            } else {
                resultat = String.format("%s%d%s %d/%d",
                        resultat, restePouces, UNITE_POUCE, numerateur, denominateur);
            }
        }

        return resultat.trim();
    }

    private static int getPlusGrandDiviseurCommun(int numerateur, int denominateur) {
        int diviseurCommun = 1;
        for (int i = 1; i <= numerateur && i <= denominateur; i++)
            if (numerateur % i == 0 && denominateur % i == 0) diviseurCommun = i;
        return diviseurCommun;
    }
}
