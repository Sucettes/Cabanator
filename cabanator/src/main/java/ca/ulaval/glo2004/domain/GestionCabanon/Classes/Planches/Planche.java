package ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancheAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Interfaces.IClonable;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.AngleDeCoupe;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.security.InvalidParameterException;
import java.util.UUID;

public abstract class Planche implements IClonable<Planche>, java.io.Serializable {
    public String debug_name = "Planche";
    protected UUID id;
    protected Point position;
    protected TypePlanche type;
    protected Sens sens;
    protected double direction;
    protected Vecteur vecteurDirection;
    protected double longueur;
    protected boolean positionEstValide = true;
    protected AngleDeCoupe angleDeCoupe;

    public Planche(UUID id, Point position) {
        this.id = id;
        this.setPosition(position);
        this.vecteurDirection = new Vecteur();
        this.setAngleDeCoupe(new AngleDeCoupe());
    }

    public Planche(UUID id, Point position, Sens sens, double direction, double longueur) {
        this(id, position);
        this.setSens(sens);
        this.setDirection(direction);
        this.setLongueur(longueur);
        this.setTypeParDefaut();
    }
    public Planche(UUID id, Point position, Sens sens, double direction, double longueur, AngleDeCoupe angleDeCoupe) {
        this(id, position);
        this.setSens(sens);
        this.setDirection(direction);
        this.setLongueur(longueur);
        this.setTypeParDefaut();
        this.setAngleDeCoupe(angleDeCoupe);
    }

    public Planche(UUID id, Point position, Sens sens, double direction, double longueur, boolean positionValide,
                   String debug_name, TypePlanche type) {
        this(id, position);
        this.setSens(sens);
        this.setDirection(direction);
        this.setLongueur(longueur);
        this.positionEstValide = positionValide;
        this.debug_name = debug_name;
        this.setType(type);
    }

    public Planche(UUID id, Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        this(id, position, sens, direction, longueur);
        this.positionEstValide = positionValide;
    }

    public Planche(Point position, Sens sens, double direction, double longueur) {
        this(UUID.randomUUID(), position, sens, direction, longueur);
    }

    public Planche(Point position, Sens sens, Vecteur direction, double longueur) {
        this(UUID.randomUUID(), position);
        this.setSens(sens);
        this.vecteurDirection = direction;
        this.setLongueur(longueur);
        this.setTypeParDefaut();
        this.debug_name = debug_name;
    }

    public Planche(Point position, Sens sens, Vecteur direction, double longueur, TypePlanche type) {
        this(UUID.randomUUID(), position);
        this.setSens(sens);
        this.vecteurDirection = direction;
        this.setLongueur(longueur);
        this.setType(type);
        this.debug_name = debug_name;
    }
    public Planche(Point position, Sens sens, Vecteur direction, double longueur, TypePlanche type, AngleDeCoupe angleDeCoupe) {
        this(UUID.randomUUID(), position);
        this.setSens(sens);
        this.vecteurDirection = direction;
        this.setLongueur(longueur);
        this.setType(type);
        this.setAngleDeCoupe(angleDeCoupe);
        this.debug_name = debug_name;
    }

    public Planche(Point position, Sens sens, double direction, double longueur, TypePlanche type) {
        this(UUID.randomUUID(), position, sens, direction, longueur);
        this.setType(type);
    }

    public Planche(Point position, Sens sens, double direction, double longueur, boolean positionValide) {
        this(position, sens, direction, longueur);
        this.positionEstValide = positionValide;
    }

    public Planche(Point position, Sens sens, double direction, double longueur, boolean positionValide, TypePlanche type) {
        this(position, sens, direction, longueur);
        this.positionEstValide = positionValide;
        this.setType(type);
    }

    public Planche(Point position, Sens sens, Vecteur direction, double longueur, boolean positionValide) {
        this(UUID.randomUUID(), position);
        this.setSens(sens);
        this.vecteurDirection = direction;
        this.setLongueur(longueur);
        this.positionEstValide = positionValide;
    }

    public Planche(Point position, Sens sens, Vecteur direction, double longueur, TypePlanche type, boolean positionValide) {
        this(position, sens, direction, longueur, positionValide);
        this.setType(type);
    }

    /**
     * Permet d'obtenir la limite de l'objet planche.
     *
     * @return limite en pouce
     */
    public double getLimite() {
        return TypePlanche.getLimitePlanche(type).getDistanceDouble();
    }

    public UUID getId() {
        return this.id;
    }

    public Sens getSens() {
        return this.sens;
    }

    protected void setSens(Sens sens) {
        if (sens == null) {
            throw new InvalidParameterException("Sens est null");
        }
        this.sens = sens;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        if (position == null) {
            throw new InvalidParameterException("Position est null!");
        }
        this.position = position;
    }

    public TypePlanche getType() {
        return this.type;
    }

    public void setType(TypePlanche type) {
        if (type == null) {
            throw new InvalidParameterException("type est null!");
        }
        this.type = type;
    }

    protected abstract void setTypeParDefaut();

    public double getEpaisseur() {
        return this.type.getEpaisseur();
    }

    public double getLargeur() {
        return this.type.getLargeur();
    }

    public double getDirection() {
        return this.direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public Vecteur getVecteurDirection() {
        return this.vecteurDirection;
    }

    public void setVecteurDirection(Vecteur vecteurDirection) {
        this.vecteurDirection = vecteurDirection;
    }

    public double getLongueur() {
        return this.longueur;
    }

    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }

    public boolean getPositionEstValide() {
        return this.positionEstValide;
    }

    public void setPositionEstValide(boolean positionValide) {
        this.positionEstValide = positionValide;
    }

    public void setAngleDeCoupe(AngleDeCoupe angleDeCoupe) {
        this.angleDeCoupe = angleDeCoupe;
    }
    public AngleDeCoupe getAngleDeCoupe() {
        return this.angleDeCoupe;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlancheAffichage planche)
            return this.id == planche.getId();
        return this.id == ((Planche) obj).getId();
    }
}