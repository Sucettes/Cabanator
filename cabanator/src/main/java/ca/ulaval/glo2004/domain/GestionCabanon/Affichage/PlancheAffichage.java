package ca.ulaval.glo2004.domain.GestionCabanon.Affichage;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.AngleDeCoupe;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.UUID;

public class PlancheAffichage {
    public String debug_name = "PlancheAffichage";
    private UUID id;
    private Point position;
    private double epaisseur;
    private double largeur;
    private double longueur;
    private double direction;
    private Vecteur vecteurDirection;
    private boolean positionEstValide;
    private Sens sens;
    private TypePlanche type;
    private AngleDeCoupe angleDeCoupe;

    public PlancheAffichage(Planche planche) {
        this.id = planche.getId();
        this.position = planche.getPosition();
        this.epaisseur = planche.getEpaisseur();
        this.largeur = planche.getLargeur();
        this.longueur = planche.getLongueur();
        this.direction = planche.getDirection();
        this.vecteurDirection = planche.getVecteurDirection();
        this.positionEstValide = planche.getPositionEstValide();
        this.sens = planche.getSens();
        this.type = planche.getType();
        this.debug_name = planche.debug_name;
        this.angleDeCoupe = planche.getAngleDeCoupe();
    }

    public UUID getId() {
        return this.id;
    }

    public Point getPosition() {
        return this.position;
    }

    public double getEpaisseur() {
        return this.epaisseur;
    }

    public double getLargeur() {
        return this.largeur;
    }

    public double getLongueur() {
        return this.longueur;
    }

    public Sens getSens() {
        return this.sens;
    }

    public TypePlanche getType() {
        return this.type;
    }

    public double getDirection() {
        return this.direction;
    }

    public Vecteur getVecteurDirection() {
        return this.vecteurDirection;
    }

    public boolean getPositionEstValide() {
        return this.positionEstValide;
    }
    public AngleDeCoupe getAngleDeCoupe() {
        return this.angleDeCoupe;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Planche planche)
            return this.id == planche.getId();
        return this.id == ((PlancheAffichage) obj).getId();
    }
}