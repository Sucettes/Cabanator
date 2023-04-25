package ca.ulaval.glo2004.domain.GestionCabanon.Enums;

import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;

import java.util.Optional;

public enum TypePlanche implements java.io.Serializable {
    P_2X4("2x4", 2, 4),
    P_2X6("2x6", 2, 6),
    P_2X8("2x8", 2, 8),
    P_2X10("2x10", 2, 10);

    private static final int LIMITE_DEFAUT = 192; // 16 pieds par défaut
    private static ValeurImperiale LIMITE_2X4 = new ValeurImperiale(LIMITE_DEFAUT);
    private static ValeurImperiale LIMITE_2X6 = new ValeurImperiale(LIMITE_DEFAUT);
    private static ValeurImperiale LIMITE_2X8 = new ValeurImperiale(LIMITE_DEFAUT);
    private static ValeurImperiale LIMITE_2X10 = new ValeurImperiale(LIMITE_DEFAUT);
    private final String type;
    private final double epaisseur;
    private final double largeur;

    TypePlanche(String type, double epaisseur, double largeur) {
        this.type = type;
        this.epaisseur = epaisseur;
        this.largeur = largeur;
    }

    /**
     * Permet de set la limite d'une planche en pouce
     *
     * @param type   le type de la planche
     * @param limite la limite en pouces
     */
    public static void setLimitePlanche(TypePlanche type, double limite) {
        switch (type) {
            case P_2X4 -> LIMITE_2X4.setDistanceDouble(limite);
            case P_2X6 -> LIMITE_2X6.setDistanceDouble(limite);
            case P_2X8 -> LIMITE_2X8.setDistanceDouble(limite);
            case P_2X10 -> LIMITE_2X10.setDistanceDouble(limite);
        }
    }

    /**
     * Permet d'obtenir la limite d'une planche en fonction de son type.
     *
     * @param type Type de la planche
     * @return limite en pouces
     */
    public static ValeurImperiale getLimitePlanche(TypePlanche type) {
        return switch (type) {
            case P_2X4 -> LIMITE_2X4;
            case P_2X6 -> LIMITE_2X6;
            case P_2X8 -> LIMITE_2X8;
            case P_2X10 -> LIMITE_2X10;
        };
    }

    public static Optional<Integer> obtenirOrdinalTypePlanche(String typePlanche) {
        for (TypePlanche type : TypePlanche.values()) {
            if (type.type.equals(typePlanche))
                return Optional.of(type.ordinal());
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        return this.type;
    }

    public double getEpaisseur() {
        return this.epaisseur;
    }

    public double getLargeur() {
        return this.largeur;
    }
}