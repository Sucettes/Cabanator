package ca.ulaval.glo2004.domain.GestionCabanon.Affichage;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Accessoire;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;
import java.util.UUID;

public abstract class AccessoireAffichage {
    private final UUID id;
    private final Point position;
    private final double largeur;
    private final double hauteur;

    protected AccessoireAffichage(Accessoire accessoire) {
        accessoire = accessoire.cloner();

        this.id = accessoire.getId();
        this.position = accessoire.getCentre();
        this.largeur = accessoire.getLongueurTrou();
        this.hauteur = accessoire.getHauteurTrou();
    }

    public UUID getId() {
        return this.id;
    }

    public Point getPosition() {
        return this.position;
    }

    public double getLargeur() {
        return this.largeur;
    }

    public double getHauteur() {
        return this.hauteur;
    }

    public abstract ArrayList<PlancheAffichage> getPlanchesAffichage();
}