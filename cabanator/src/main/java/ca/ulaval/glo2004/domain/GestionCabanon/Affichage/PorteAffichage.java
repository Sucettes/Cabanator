package ca.ulaval.glo2004.domain.GestionCabanon.Affichage;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Porte;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PorteAffichage extends AccessoireAffichage {
    private final ArrayList<PlancheAffichage> planchesAffichage;

    public PorteAffichage(Porte porte) {
        super(porte);

        this.planchesAffichage = porte.getPlanches()
                .stream().map(PlancheAffichage::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<PlancheAffichage> getPlanchesAffichage() {
        return planchesAffichage;
    }
}