package ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches;

import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.UUID;

public class Linteau extends Planche {
    /**
     * Constructeur pour le DTO
     *
     * @param id
     * @param point
     * @param direction
     * @param longueur
     * @param type
     */
    public Linteau(UUID id, Point point, double direction, double longueur, TypePlanche type) {
        super(id, point, Sens.VOIR_LARGEUR, direction, longueur);
        this.setType(type);
    }

    /**
     * Constructeur pour un linteau existant
     *
     * @param id
     * @param point
     * @param longueur
     * @param type
     * @param positionValide
     */
    public Linteau(UUID id, Point point, double longueur, TypePlanche type, boolean positionValide) {
        this(id, point, PointCardinal.EST.getDirection(), longueur, type);
        this.positionEstValide = positionValide;
    }

    /**
     * Constructeur pour la création d'un linteau
     *
     * @param point
     * @param longueur
     * @param type
     */
    public Linteau(Point point, double longueur, TypePlanche type) {
        super(point, Sens.VOIR_LARGEUR, PointCardinal.EST.getDirection(), longueur, type);
    }

    /**
     * Constructeur pour la création d'un linteau
     *
     * @param point
     * @param longueur
     * @param type
     */
    public Linteau(Point point, double longueur, boolean positionValide, TypePlanche type) {
        super(point, Sens.VOIR_LARGEUR, PointCardinal.EST.getDirection(), longueur, positionValide, type);
    }

    public Linteau(Planche x) {
        super(x.getPosition(), x.getSens(), x.getDirection(), x.getLongueur());
        this.setType(x.getType());
        this.id = x.getId();
    }

    @Override
    protected void setTypeParDefaut() {
        this.type = TypePlanche.P_2X4;
    }

    public void setType(TypePlanche type) {
        super.type = type;
    }

    @Override
    public Linteau cloner() {
        return new Linteau(
                this.getId(),
                this.getPosition().cloner(),
                this.getLongueur(),
                this.getType(),
                this.getPositionEstValide());
    }
}