package ca.ulaval.glo2004.domain.GestionCabanon.Affichage;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.LigneEntremises;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class LigneEntremisesAffichage {
    private final UUID id;
    private final Point position;
    private final ArrayList<PlancheAffichage> entremises;

    public LigneEntremisesAffichage(LigneEntremises ligneEntremises) {
        ligneEntremises = ligneEntremises.cloner();

        this.id = ligneEntremises.getId();
        this.position = ligneEntremises.getCentre();
        this.entremises = ligneEntremises.getPlanches()
                .stream().map(PlancheAffichage::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public UUID getId() {
        return this.id;
    }

    public Point getPosition() {
        return this.position;
    }

    public ArrayList<PlancheAffichage> getEntremises() {
        return this.entremises;
    }
}