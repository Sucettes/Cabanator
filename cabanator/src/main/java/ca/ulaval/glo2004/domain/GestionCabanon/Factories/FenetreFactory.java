package ca.ulaval.glo2004.domain.GestionCabanon.Factories;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Fenetre;

public class FenetreFactory extends Fenetre {
    private FenetreFactory(Fenetre fenetre) {
        super(fenetre.getId(),
                PointFactory.FromPoint(fenetre.getCentre()),
                fenetre.getLongueurTrou(),
                fenetre.getHauteurTrou(),
                fenetre.getTypeLinteau(),
                fenetre.getDistanceEntreMontants(),
                PointFactory.FromPoint(fenetre.getOrigineParent()));
    }

    public static FenetreFactory FromFenetre(Fenetre fenetre) {
        return new FenetreFactory(fenetre);
    }
}