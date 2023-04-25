package ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.UUID;

public class Entremise extends Planche {
    /**
     * Constructeur pour le dto
     *
     * @param id
     * @param position
     */
    public Entremise(UUID id, Point position) {
        super(id, position);
        this.setTypeParDefaut();
    }

    /**
     * Constructeur pour une entremise existante
     *
     * @param id
     * @param position
     * @param sens
     * @param direction
     * @param longueur
     */
    public Entremise(UUID id, Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        super(id, position, sens, direction, longueur, positionValide);
    }

    /**
     * Constructeur pour la création d'une entremise
     *
     * @param position
     * @param sens
     * @param direction
     * @param longueur
     */
    public Entremise(Point position, Sens sens, double direction, double longueur) {
        super(position, sens, direction, longueur);
    }

    /**
     * Constructeur pour la création d'une entremise en lui spécifiant si sa position est valide
     *
     * @param position
     * @param sens
     * @param direction
     * @param longueur
     */
    public Entremise(Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        super(position, sens, direction, longueur, positionValide);
    }

    public Entremise(Point position, Sens sens, Vecteur direction, double longueur) {
        super(position, sens, direction, longueur);
    }

    public Entremise(Point pointEntremise, Sens sens, double direction, double longueur, TypePlanche type) {
        super(pointEntremise, sens, direction, longueur, type);
    }

    @Override
    protected void setTypeParDefaut() {
        this.type = TypePlanche.P_2X4;
    }

    public void reconfigurerPosition(double ratioPY) {
        this.getPosition().setY(this.getPosition().getY() * ratioPY);
    }

    @Override
    public Entremise cloner() {
        Entremise clone = new Entremise(
                this.getId(),
                this.getPosition().cloner(),
                this.getSens(),
                this.getDirection(),
                this.getLongueur(),
                this.getPositionEstValide());

        clone.vecteurDirection = this.vecteurDirection;
        return clone;
    }
}