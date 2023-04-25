package ca.ulaval.glo2004.domain.GestionCabanon.Classes;


import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;

public class CoutTypePlanche {
    public static double COUT_PIED_2X4 = 0.53;
    public static double COUT_PIED_2X6 = 1.34;
    public static double COUT_PIED_2X8 = 1.20;
    public static double COUT_PIED_2X10 = 1.68;

    /**
     * Permet de set le cout d'une planche pour 1 pied
     *
     * @param type le type de la planche
     * @param cout le cout de la planche pour 1 pied
     */
    public static void setCoutPiedPlanche(TypePlanche type, double cout) {
        switch (type) {
            case P_2X4 -> COUT_PIED_2X4 = cout;
            case P_2X6 -> COUT_PIED_2X6 = cout;
            case P_2X8 -> COUT_PIED_2X8 = cout;
            case P_2X10 -> COUT_PIED_2X10 = cout;
        }
    }

    /**
     * Permet d'obtenir le cout par pied d'une planche en fonction de sont type.
     *
     * @param type Type de la planche
     * @return prix pour 1 pied
     */
    public static double getCoutPiedPlanche(TypePlanche type) {
        switch (type) {
            case P_2X4 -> {
                return COUT_PIED_2X4;
            }
            case P_2X6 -> {
                return COUT_PIED_2X6;
            }
            case P_2X8 -> {
                return COUT_PIED_2X8;
            }
            case P_2X10 -> {
                return COUT_PIED_2X10;
            }
        }
        return 0;
    }
}