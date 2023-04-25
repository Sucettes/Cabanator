package ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches;

import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.UUID;

public class Entrait extends Planche {

    /**
     * Constructeur pour le dto
     *
     * @param id       id
     * @param position position
     */
    public Entrait(UUID id, Point position) {
        super(id, position);
        this.setTypeParDefaut();
    }

    /**
     * Constructeur pour un Entrait existant.
     *
     * @param id             id
     * @param position       postion
     * @param sens           sens
     * @param direction      direction
     * @param longueur       longueur
     * @param positionValide position valide
     */
    public Entrait(UUID id, Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        super(id, position, sens, direction, longueur, positionValide);
    }

    /**
     * Constructeur pour la creation d'un entrait
     *
     * @param position  position
     * @param sens      sens
     * @param direction direction
     * @param longueur  longueur
     */
    public Entrait(Point position, Sens sens, double direction, double longueur, TypePlanche type) {
        super(position, sens, direction, longueur, type);
    }

    @Override
    protected void setTypeParDefaut() {
        this.type = TypePlanche.P_2X4;
    }

    @Override
    public Entrait cloner() {
        return new Entrait(
                this.getId(),
                this.getPosition().cloner(),
                this.getSens(),
                this.getDirection(),
                this.getLongueur(),
                this.getPositionEstValide());
    }
}