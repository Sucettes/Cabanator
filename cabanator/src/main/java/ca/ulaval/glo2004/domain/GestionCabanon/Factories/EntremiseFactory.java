package ca.ulaval.glo2004.domain.GestionCabanon.Factories;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Entremise;

public class EntremiseFactory extends Entremise {
    private EntremiseFactory(Entremise entremise) {
        super(entremise.getId(), PointFactory.FromPoint(entremise.getPosition()));
    }

    public static EntremiseFactory FromEntremise(Entremise entremise) {
        return new EntremiseFactory(entremise);
    }
}