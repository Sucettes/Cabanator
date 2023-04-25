package ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.AngleDeCoupe;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.UUID;

public class Chevron extends Planche {
    /**
     * Constructeur pour le dto
     *
     * @param id       id
     * @param position position
     */
    public Chevron(UUID id, Point position) {
        super(id, position);
        this.setTypeParDefaut();
    }

    /**
     * constructeur pour un chevron existant
     *
     * @param id             id
     * @param position       position
     * @param sens           sens
     * @param direction      direction
     * @param longueur       longueur
     * @param positionValide positionValide
     */
    public Chevron(UUID id, Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        super(id, position, sens, direction, longueur, positionValide);
    }

    /**
     * Constructeur pour la creation d'un chevron.
     *
     * @param position  position
     * @param sens      sens
     * @param direction direction
     * @param longueur  longueur
     */
    public Chevron(Point position, Sens sens, double direction, double longueur) {
        super(position, sens, direction, longueur);
    }

    public Chevron(Point position, Sens sens, Vecteur direction, double longueur, TypePlanche type, AngleDeCoupe angleDeCoupe) {
        super(position, sens, direction, longueur, type, angleDeCoupe);
    }

    /**
     * Constructeur pour la création d'un chevron en lui spécifiant si sa position est valide
     *
     * @param position       position
     * @param sens           sens
     * @param direction      direction
     * @param longueur       longueur
     * @param positionValide positionValide
     */
    public Chevron(Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        super(position, sens, direction, longueur, positionValide);
    }

    @Override
    protected void setTypeParDefaut() {
        this.type = TypePlanche.P_2X4;
    }

    @Override
    public Chevron cloner() {
        Chevron clone = new Chevron(
                this.getId(),
                this.getPosition().cloner(),
                this.getSens(),
                this.getDirection(),
                this.getLongueur(),
                this.getPositionEstValide());
        clone.angleDeCoupe = this.angleDeCoupe;
        clone.vecteurDirection = this.vecteurDirection;
        clone.debug_name = this.debug_name;
        return clone;
    }
}