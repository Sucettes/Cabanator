package ca.ulaval.glo2004.domain.GestionCabanon.Affichage;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Ferme;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.AngleDeCoupe;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class FermeAffichage {
    private final UUID id;
    private final ArrayList<PlancheAffichage> planchesAffichage;
    private final Point point;

    public FermeAffichage(Ferme ferme) {
        ferme = ferme.cloner();
        point = ferme.getOrigine();
        this.id = ferme.getId();
        this.planchesAffichage = ferme.getPlanches().stream().map(PlancheAffichage::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public UUID getId() {
        return this.id;
    }
    public ArrayList<PlancheAffichage> getPlanchesAffichage() {
        return this.planchesAffichage;
    }

    public Point getPoint() { return this.point; }
}
