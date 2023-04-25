package ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.UUID;

public class Montant extends Planche {
    public Montant(UUID id, Point position, Sens sens, double direction, double longueur) {
        super(id, position, sens, direction, longueur);
    }

    public Montant(UUID id, Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        super(id, position, sens, direction, longueur, positionValide);
    }

    public Montant(Point position, Sens sens, double direction, double longueur) {
        super(position, sens, direction, longueur);
    }

    public Montant(Point position, Sens sens, double direction, double longueur, TypePlanche type) {
        super(position, sens, direction, longueur, type);
    }

    public Montant(Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        super(position, sens, direction, longueur, positionValide);
    }

    public Montant(Point position, Sens sens, double direction, double longueur, boolean positionValide, TypePlanche type) {
        super(position, sens, direction, longueur, positionValide, type);
    }

    public Montant(Point position, Sens sens, Vecteur direction, double longueur) {
        super(position, sens, direction, longueur);
    }

    public Montant(Point position, Sens sens, Vecteur direction, double longueur, boolean positionValide) {
        super(position, sens, direction, longueur, positionValide);
    }

    public Montant(Point position, Sens sens, Vecteur direction, double longueur, TypePlanche type, boolean positionValide) {
        super(position, sens, direction, longueur, type, positionValide);
    }

    public Montant(Planche planche) {
        super(planche.getPosition(), planche.getSens(), planche.getDirection(), planche.getLongueur());
        this.id = planche.getId();
        this.setType(planche.getType());
    }


    @Override
    protected void setTypeParDefaut() {
        this.type = TypePlanche.P_2X4;
    }

    @Override
    public Montant cloner() {
        Montant clone = new Montant(
                this.getId(),
                this.getPosition().cloner(),
                this.getSens(),
                this.getDirection(),
                this.getLongueur(),
                this.getPositionEstValide());
        clone.vecteurDirection = this.vecteurDirection;
        clone.debug_name = this.debug_name;

        return clone;
    }
}