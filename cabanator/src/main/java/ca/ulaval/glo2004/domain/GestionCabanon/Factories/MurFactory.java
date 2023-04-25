package ca.ulaval.glo2004.domain.GestionCabanon.Factories;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Fenetre;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Porte;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Mur;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MurFactory extends Mur {
    private MurFactory(Mur mur) {
        super(
                mur.getPositionMur(),
                mur.getPointCardinal(),
                mur.getOrigine(),
                mur.getHauteur(),
                mur.getLongueur(),
                mur.getDistanceEntreMontants(),
                mur.getEntremises()
                        .stream().map(EntremiseFactory::FromEntremise)
                        .collect(Collectors.toCollection(ArrayList::new)),
                mur.getAccessoires()
                        .stream().map(accessoire -> {
                            if (accessoire instanceof Porte porte)
                                return PorteFactory.FromPorte(porte);
                            else if (accessoire instanceof Fenetre fenetre)
                                return FenetreFactory.FromFenetre(fenetre);
                            return null;
                        })
                        .collect(Collectors.toCollection(ArrayList::new)));
    }

    public static MurFactory FromMur(Mur mur) {
        return new MurFactory(mur);
    }
}