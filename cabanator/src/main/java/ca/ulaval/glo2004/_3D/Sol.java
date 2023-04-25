package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;

import java.awt.*;
import java.util.ArrayList;

public class Sol extends Objet3D{

    public Sol(Point3D position, Dimension3D dimension, Color couleur) {
        super();
        ArrayList<Polygon3D> polys = new ArrayList<>();
        //Refait la boucle en utilisant les dimensions
        double largeur = dimension.largeur;
        double longueur = dimension.longueur;
        for (int i = (int)position.x; i<dimension.largeur; i++)
            for (int j = (int)position.z; j<dimension.longueur; j++)
                polys.add(new Polygon3D(new Point3D[]{
                        new Point3D(i,0, j),
                        new Point3D(i, 0, j+1),
                        new Point3D(i+1, 0, j+1),
                        new Point3D(i+1, 0, j)
                }, couleur));

        this.polygons = polys.toArray(new Polygon3D[0]);
        this.couleur = couleur;
        this.position = position;
    }

    public Sol(Point3D position, Dimension3D dimension, Color couleur, boolean selectable) {
        this (position, dimension, couleur);
        setSelectable(selectable);
    }


    public Vecteur getNormal(Polygon3D polygon3D) {

        return null;
    }

    @Override
    public void rotate(double xAngle, double yAngle, double zAngle) {
        for (Polygon3D polygon : polygons) {
            polygon.rotate(xAngle, yAngle, zAngle);
        }
    }

    public void rotateAroundCorner(double xAngle, double yAngle, double zAngle) {
        for (Polygon3D polygon : polygons) {
            polygon.rotateAroundCorner(xAngle, yAngle, zAngle);
        }
    }

    @Override
    public void rotateAroundPoint(Point3D pivot, double xAngle, double yAngle, double zAngle) {

    }

    @Override
    public void faceTo(Point3D point) {

    }

    @Override
    void update(Controleur3D controleur3D) {
        for (Polygon3D polygon : polygons) {
            polygon.update(controleur3D);
        }
    }

    @Override
    public <T extends Objet3D> T scale(double scale) {
        return null;
    }
}
