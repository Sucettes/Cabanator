package ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.UUID;

public class Solive extends Planche {
    public Solive(UUID id, Point position, Sens sens, double direction, double longueur) {
        super(id, position, sens, direction, longueur);
    }

    public Solive(UUID id, Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        super(id, position, sens, direction, longueur, positionValide);
    }

    public Solive(Point position, Sens sens, double direction, double longueur) {
        super(position, sens, direction, longueur);
    }

    public Solive(Point position, Sens sens, double direction, double longueur, TypePlanche type) {
        super(position, sens, direction, longueur, type);
    }

    public Solive(Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        super(position, sens, direction, longueur, positionValide);
    }

    public Solive(Point position, Sens sens, double direction, double longueur, boolean positionValide, TypePlanche type) {
        super(position, sens, direction, longueur, positionValide, type);
    }

    public Solive(Point position, Sens sens, Vecteur direction, double longueur) {
        super(position, sens, direction, longueur);
    }

    public Solive(UUID id, Point position, Sens sens, double direction, double longueur, boolean positionValide,
                  String debug_name, TypePlanche type) {
        super(id, position, sens, direction, longueur, positionValide, debug_name, type);
    }

    @Override
    protected void setTypeParDefaut() {
        this.type = TypePlanche.P_2X4;
    }

    @Override
    public Solive cloner() {
        Solive clone = new Solive(
                this.getId(),
                this.getPosition().cloner(),
                this.getSens(),
                this.getDirection(),
                this.getLongueur(),
                this.getPositionEstValide(),
                this.debug_name,
                this.getType()
        );
        clone.vecteurDirection = this.vecteurDirection;
        clone.debug_name = this.debug_name;
        return clone;
    }
}