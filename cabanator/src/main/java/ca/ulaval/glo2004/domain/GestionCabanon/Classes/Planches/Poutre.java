package ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.AngleDeCoupe;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.UUID;

public class Poutre extends Planche {
    public Poutre(UUID id, Point position, Sens sens, double direction, double longueur) {
        super(id, position, sens, direction, longueur);
    }
    public Poutre(UUID id, Point position, Sens sens, double direction, double longueur, AngleDeCoupe angleDeCoupe) {
        super(id, position, sens, direction, longueur, angleDeCoupe);
    }

    public Poutre(UUID id, Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        super(id, position, sens, direction, longueur, positionValide);
    }

    public Poutre(Point position, Sens sens, Vecteur direction, double longueur, TypePlanche type) {
        super(position, sens, direction, longueur, type);
    }

    public Poutre(Point position, Sens sens, Vecteur direction, double longueur, TypePlanche type, AngleDeCoupe angleDeCoupe) {
        super(position, sens, direction, longueur, type, angleDeCoupe);
    }

    public Poutre(Point position, Sens sens, Vecteur direction, double longueur, boolean positionValide) {
        super(position, sens, direction, longueur, positionValide);
    }

    @Override
    protected void setTypeParDefaut() {
        this.type = TypePlanche.P_2X4;
    }

    @Override
    public Poutre cloner() {
        Poutre clone = new Poutre(
                this.getId(),
                this.getPosition().cloner(),
                this.getSens(),
                this.getDirection(),
                this.getLongueur(),
                this.getAngleDeCoupe());
        clone.vecteurDirection = this.vecteurDirection;
        return clone;
    }
}