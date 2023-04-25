package ca.ulaval.glo2004.domain.GestionCabanon.Factories;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Porte;

public class PorteFactory extends Porte {
    private PorteFactory(Porte porte) {
        super(porte.getId(),
                PointFactory.FromPoint(porte.getCentre()),
                porte.getLongueurTrou(),
                porte.getHauteurTrou(),
                porte.getTypeLinteau(),
                porte.getDistanceEntreMontants(),
                PointFactory.FromPoint(porte.getOrigineParent()));
    }

    public static Porte FromPorte(Porte porte) {
        return new PorteFactory(porte);
    }
}