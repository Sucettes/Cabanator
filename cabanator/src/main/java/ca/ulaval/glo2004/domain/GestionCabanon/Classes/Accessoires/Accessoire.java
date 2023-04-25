package ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Bloc;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.FormeDeBase;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Interfaces.IClonable;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public abstract class Accessoire extends FormeDeBase implements IClonable<Accessoire>, java.io.Serializable {

    //#region ATTRIBUTS

    private final TypePlanche T_PLANCHES_CONTOUR = TypePlanche.P_2X4;

    protected UUID id;
    protected double hauteurParent; // hauteur du mur
    protected double longueurTotale; // longueur du trou + les montants
    protected double longueurTrou; // longueur du trou de la fenetre ou de la porte
    protected double hauteurTrou; // hauteur du trou de la fenetre ou de la porte
    protected Bloc structureInterne;
    private Point origineParent;
    private Point centre;

    //#endregion

    //#region CONSTRUCTEURS

    // dto constructeur
    Accessoire(UUID id, Point centre, double longueurTrou, double hauteurTrou, Point origineParent) {
        this.id = id;
        this.setLongueurTrou(longueurTrou);
        this.setHauteurTrou(hauteurTrou);
        this.setOrigineParent(origineParent);
        this.setOrigine(centre);
    }

    Accessoire(UUID id, Point centre, double longueurTrou, double hauteurTrou, double hauteurParent, Point origineParent, Bloc structureInterne) {
        this.id = id;
        this.setLongueurTrou(longueurTrou);
        this.setHauteurTrou(hauteurTrou);
        this.setHauteurParent(hauteurParent);
        this.setOrigineParent(origineParent);
        this.setOrigine(centre);
        this.structureInterne = structureInterne;
    }

    /**
     * Constructeur pour la création
     *
     * @param centre
     * @param longueurTrou
     * @param hauteurTrou
     */
    Accessoire(Point centre, double longueurTrou, double hauteurTrou, double hauteurParent, Point origineParent) {
        this.id = UUID.randomUUID();
        this.setLongueurTrou(longueurTrou);
        this.setHauteurTrou(hauteurTrou);
        this.setHauteurParent(hauteurParent);
        this.setOrigineParent(origineParent);
        this.setOrigine(centre);
    }

    //#endregion

    //#region PROPRIÉTÉS ET INDEXEURS

    public UUID getId() {
        return this.id;
    }

    public double getLongueurTotale() {
        return this.longueurTrou + (T_PLANCHES_CONTOUR.getEpaisseur() * 2);
    }

    /**
     * DO NOT USE !!!!!
     *
     * @param longueur
     */
    void setLongueurTotale(double longueur) {
        this.longueurTotale = longueur;
    }

    public double getLongueurTrou() {
        return this.longueurTrou;
    }

    void setLongueurTrou(double longueur) {
        this.longueurTrou = longueur;
    }

    public double getHauteurParent() {
        return this.hauteurParent;
    }

    public void setHauteurParent(double hauteur) {
        this.hauteurParent = hauteur;
    }

    public double getHauteurTrou() {
        return this.hauteurTrou;
    }

    void setHauteurTrou(double hauteur) {
        this.hauteurTrou = hauteur;
    }

    protected abstract double getHauteurTotale();

    public boolean getPositionAccessoireEstValide() {
        if (structureInterne == null) {
            return true;
        }
        Planche planche = this.structureInterne.getAnyPlanche(p -> p.getPositionEstValide() == false);
        return planche == null;
    }

    public void setPositionAccessoireEstValide(boolean positionValide) {
        this.structureInterne.getPlanches().forEach(planche -> planche.setPositionEstValide(positionValide));
    }

    public Bloc getStructureInterne() {
        return this.structureInterne;
    }

    public Point getOrigineParent() {
        return origineParent;
    }

    public void setOrigineParent(Point origine) {
        this.origineParent = origine;
    }

    public Point getCentre() {
        return this.centre;
    }

    public void setCentre(Point positionCurseur) {
        this.centre = positionCurseur;
    }

    //#endregion

    //#region MÉTHODES

    public void configurerLongueurTrou(double longueur) {
        double epaisseurPlancheNordSud = T_PLANCHES_CONTOUR.getEpaisseur();
        this.setLongueurTotale(longueur + (epaisseurPlancheNordSud * 2));
        this.setLongueurTrou(longueur);
        this.configurerPosition(this.getCentre());
    }

    public void configurerHauteurTrou(double hauteur) {
        this.setHauteurTrou(hauteur);
        this.configurerPosition(this.getCentre());
    }

    public void regenererStructure() {
//        System.out.println("RegenererStructure " + this.getClass().getSimpleName());
        this.genererStructure(this.getLongueurTotale(), this.getHauteurParent());
    }

    protected void genererStructure(double longueurTotale, double hauteurParent) {
        this.structureInterne = this.genererBloc(longueurTotale, hauteurParent);
    }

    public abstract void configurerPosition(Point origine);

    public void reconfigurerPosition(double ratioPX, double ratioPY) {
        this.configurerPosition(new Point(
                this.getCentre().getX() * ratioPX,
                this.getCentre().getY() * ratioPY,
                this.getOrigine().getZ()
        ));
    }

    protected abstract Bloc genererBloc(double longueurTotale, double hauteurParent);

    protected <T extends Planche> T creerPlanche(
            Class<T> cPlanche,
            Point position,
            Sens sens,
            Vecteur direction,
            double longueur,
            boolean positionValide,
            TypePlanche tPlanche) {
        try {
            Constructor<T> constructor = cPlanche.getConstructor(Point.class, Sens.class, Vecteur.class, double.class, TypePlanche.class, boolean.class);
            return constructor.newInstance(position, sens, direction, longueur, tPlanche, positionValide);
        } catch (NoSuchMethodException
                 | InvocationTargetException
                 | InstantiationException
                 | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean contientPoint(Point point, double longueurTotale) {
        double pX = this.getOrigine().getX();
        double pXMin = pX + longueurTotale; // 0 + 10
        double pXMax = pX - this.getLongueurTotale(); // 0 - 20
        return point.getX() <= pXMin && point.getX() >= pXMax;
    }

    public boolean contientEnX(double pointEnX, double longueurTotale) {
        double pX = this.getOrigine().getX();
        double pXMin = pX + longueurTotale; // 0 + 10
        double pXMax = pX - this.getLongueurTotale(); // 0 - 20
        return pointEnX <= pXMin && pointEnX >= pXMax;
    }

    //#endregion
}