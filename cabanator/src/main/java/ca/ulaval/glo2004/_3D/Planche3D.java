package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancheAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.Utilitaires.Dimension3D;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class Planche3D extends Cube {
    Sens sens;
    Vecteur orientation;
    double orient;
    private boolean useVecteurOrientation = false;
    double direction;
    public String debug_name = "Planche3D";
    Class originClass;
    public Planche3D(Planche3D planche3D) {
        super(planche3D.position, planche3D.dimension, planche3D.couleur);
        this.id = planche3D.id;
        this.sens = planche3D.sens;
        this.orientation = planche3D.orientation;
        this.direction = planche3D.direction;
        this.useVecteurOrientation = planche3D.useVecteurOrientation;
        this.debug_name = planche3D.debug_name;
        this.polygons = new Polygon3D[planche3D.polygons.length];
        for (int i = 0; i < polygons.length; i++) {
            this.polygons[i] = new Polygon3D(planche3D.polygons[i]);
        }
        setEstValide(planche3D.estValide);

        this.orient = planche3D.orient;
        this.originClass = planche3D.originClass;
    }


    void orienterDansLeBonSens() {
        if (useVecteurOrientation) {
            double[] angles = Calculateur3D.vecteurToAngle(orientation); // [0] = horizontal, [1] = vertical, [2] = sur Z
            this.rotateAroundCorner(0, Math.toRadians(angles[0]), 0);
            this.rotateAroundCorner(-Math.toRadians(angles[1]), 0, 0); // X inverse
        } else {
            this.rotateAroundCorner(0, Math.toRadians(direction), 0);

        }

    }

    public Planche3D(PlancheAffichage planche, Color color, Class originClass) {
        super(
                new Point3D(planche.getPosition().getX(), planche.getPosition().getY(), planche.getPosition().getZ()),
                new Dimension3D(planche.getLongueur(), planche.getLargeur(), planche.getEpaisseur()),
                color,
                planche.getAngleDeCoupe()
        );
        this.id = planche.getId();
        if (!Objects.equals(planche.getVecteurDirection(), new Vecteur())) {
            useVecteurOrientation = true;
            this.orientation = planche.getVecteurDirection();
            this.direction = 0;
        } else {
            this.orientation = new Vecteur();
            this.direction = planche.getDirection();
        }
        this.sens = planche.getSens();
        this.originClass = originClass;
        mettreDansLeBonSens();
        orienterDansLeBonSens();
        setEstValide(planche.getPositionEstValide());

        debug_name = planche.debug_name;
    }
    public Planche3D(PlancheAffichage planche, Color color) {
        super(
                new Point3D(planche.getPosition().getX(), planche.getPosition().getY(), planche.getPosition().getZ()),
                new Dimension3D(planche.getLongueur(), planche.getLargeur(), planche.getEpaisseur()),
                color,
                planche.getAngleDeCoupe()
        );
        this.id = planche.getId();
        if (!Objects.equals(planche.getVecteurDirection(), new Vecteur())) {
            useVecteurOrientation = true;
            this.orientation = planche.getVecteurDirection();
            this.direction = 0;
        } else {
            this.orientation = new Vecteur();
            this.direction = planche.getDirection();
        }
        this.sens = planche.getSens();
        mettreDansLeBonSens();
        orienterDansLeBonSens();
        setEstValide(planche.getPositionEstValide());

        debug_name = planche.debug_name;
    }

    private void mettreDansLeBonSens() {
        if (sens == Sens.VOIR_LARGEUR) {
            this.rotateAroundCorner(0, 0, Math.toRadians(-90));
            //Translation pour que la planche soit bien centrée
            if (direction == PointCardinal.NORD.getDirection() || direction == PointCardinal.SUD.getDirection())
                this.translate(dimension.hauteur, 0, 0);
            if (direction == PointCardinal.EST.getDirection() || direction == PointCardinal.OUEST.getDirection())
                this.translate(0, 0, dimension.hauteur);

        }
        if (sens== Sens.VOIR_EPAISSEUR)
        {
            if (originClass!=null && originClass.equals(Ferme3D.class)) {
                this.translate(dimension.hauteur, 0, 0);
            }
        }

        if (this.useVecteurOrientation) {
            double[] angles = Calculateur3D.vecteurToAngle(orientation);
            if (angles[1] == 90) { // Pointe vers le haut
                rotateAroundCorner(0, 0, Math.toRadians(90));

            }
        }
    }

    @Override
    public Vecteur getNormal(Polygon3D face) {
        Vecteur normal = face.toPlan().vecteurNormal;
        //Si la normale est dans le mauvais sens, on la retourne
        if (normal.produitScalaire(face.points[0].substract(getCenter())) < 0) {
            normal = normal.multiplier(-1);
        }
        return normal;
    }

    public Planche3D scale(double scale) {
        Planche3D planche3D = new Planche3D(this);
        for (int i = 0; i < polygons.length; i++) {
            planche3D.polygons[i] = planche3D.polygons[i].scale(scale);
        }
        planche3D.position = planche3D.position.multiplier(scale);
        return planche3D;
    }

    @Override
    public String toString() {
        return "Planche3D{" +
                "position=" + position +
                ", class=" + originClass +
                ", DEBUG_NAME=" + debug_name +
                ", direction=" + direction +
                ", sens=" + sens +
                ", dimension=" + dimension.longueur + " " + dimension.largeur + " " + dimension.hauteur +
                ", rX=" + String.format("%.3f", rotationX) +
                ", rY=" + String.format("%.3f", rotationY) +
                ", rZ=" + String.format("%.3f", rotationZ) +
                '}';
    }

}