package ca.ulaval.glo2004.domain.GestionCabanon.Affichage;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Fenetre;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FenetreAffichage extends AccessoireAffichage {
    private final ArrayList<PlancheAffichage> planchesAffichage;

    public FenetreAffichage(Fenetre fenetre) {
        super(fenetre);

        this.planchesAffichage = fenetre.getPlanches()
                .stream().map(PlancheAffichage::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<PlancheAffichage> getPlanchesAffichage() {
        return this.planchesAffichage;
    }
}