package ca.ulaval.glo2004.domain.GestionCabanon.Factories;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.LigneEntremises;

public class LigneEntremisesFactory extends LigneEntremises {
    private LigneEntremisesFactory(LigneEntremises ligneEntremises) {
        super(ligneEntremises.getId(),
                PointFactory.FromPoint(ligneEntremises.getCentre()),
                ligneEntremises.getDecalageEntreEntremises());
    }

    public static LigneEntremisesFactory FromLigneEntremises(LigneEntremises ligneEntremises) {
        return new LigneEntremisesFactory(ligneEntremises);
    }
}