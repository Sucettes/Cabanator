package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;

public enum Vue {
    TOIT,
    MUR_NORD,
    MUR_SUD,
    MUR_EST,
    MUR_OUEST,
    PLANCHER,
    CONFIG_MATERIAUX;

    public static PointCardinal getPointCardinal(Vue vue) {
        if (vue.ordinal() == 1) return PointCardinal.NORD;
        if (vue.ordinal() == 2) return PointCardinal.SUD;
        if (vue.ordinal() == 3) return PointCardinal.EST;
        if (vue.ordinal() == 4) return PointCardinal.OUEST;
        return null;
    }
}