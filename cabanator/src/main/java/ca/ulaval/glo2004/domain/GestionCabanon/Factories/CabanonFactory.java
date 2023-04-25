package ca.ulaval.glo2004.domain.GestionCabanon.Factories;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Cabanon;

public class CabanonFactory extends Cabanon {
    private CabanonFactory(Cabanon cabanon) {
        super(cabanon.getPlancher().cloner(), cabanon.getMurs().cloner(), cabanon.getToit(), cabanon.getNom());
    }

    public static CabanonFactory FromCabanon(Cabanon cabanon) {
        return new CabanonFactory(cabanon);
    }
}