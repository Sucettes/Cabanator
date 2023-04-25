package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.AngleDeCoupe;

import java.awt.*;

public class Cube extends Objet3D {
    Dimension3D dimension;
    public double rotationY = 0;
    public double rotationX = 0;
    public double rotationZ = 0;

    public Cube(Point3D position, Dimension3D dimension, Color color) {
        super(position, new Polygon3D[6], color);
        this.dimension = dimension;
;
        this.position = position;
        this.couleur = color;
        polygons[0] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y, position.z),
                new Point3D(position.x + dimension.largeur, position.y, position.z),
                new Point3D(position.x + dimension.largeur, position.y, position.z + dimension.longueur),
                new Point3D(position.x, position.y, position.z + dimension.longueur)
        }, color); // Bas
        polygons[1] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y + dimension.hauteur, position.z),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z + dimension.longueur),
                new Point3D(position.x, position.y + dimension.hauteur, position.z + dimension.longueur)
        }, color); // Haut
        polygons[2] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y, position.z),
                new Point3D(position.x, position.y + dimension.hauteur, position.z),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z),
                new Point3D(position.x + dimension.largeur, position.y, position.z)
        }, color); // Face (quand on regarde vers l'est)
        polygons[3] = new Polygon3D(new Point3D[]{
                new Point3D(position.x + dimension.largeur, position.y, position.z),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z + dimension.longueur),
                new Point3D(position.x + dimension.largeur, position.y, position.z + dimension.longueur)
        }, color); //Gauche
        polygons[4] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y, position.z + dimension.longueur),
                new Point3D(position.x, position.y + dimension.hauteur, position.z + dimension.longueur),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z + dimension.longueur),
                new Point3D(position.x + dimension.largeur, position.y, position.z + dimension.longueur)
        }, color); // Arrière
        polygons[5] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y, position.z),
                new Point3D(position.x, position.y + dimension.hauteur, position.z),
                new Point3D(position.x, position.y + dimension.hauteur, position.z + dimension.longueur),
                new Point3D(position.x, position.y, position.z + dimension.longueur)
        }, color); // Droite
    }
    /*public Cube(Point3D position, Dimension3D dimension, Color color, AngleDeCoupe angleDeCoupe) {
        super(position, new Polygon3D[6], color);
        this.dimension = dimension;
        this.position = position;
        this.couleur = color;

        // Calculate the tangent of each angle in the AngleDeCoupe object
        double tanOrigine = Math.tan(Math.toRadians(angleDeCoupe.getAngleOrigine()));
        double tanBasDroite = Math.tan(Math.toRadians(angleDeCoupe.getAngleBasDroite()));
        double tanHautDroite = Math.tan(Math.toRadians(angleDeCoupe.getAngleHautDroite()));
        double tanHautGauche = Math.tan(Math.toRadians(angleDeCoupe.getAngleHautGauche()));

        // Calculate the height of the cut for each side of the cube
        double hauteurOrigine = dimension.hauteur - position.y;
        double hauteurBasDroite = dimension.hauteur - position.y - dimension.largeur * tanBasDroite;
        double hauteurHautDroite = position.y + dimension.hauteur - dimension.largeur * tanHautDroite;
        double hauteurHautGauche = position.y + dimension.hauteur - dimension.longueur * tanHautGauche;

        // Modify the vertices of each side of the cube to create the cut
        polygons[0] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y + hauteurOrigine, position.z),
                new Point3D(position.x + dimension.largeur, position.y + hauteurBasDroite, position.z),
                new Point3D(position.x + dimension.largeur, position.y, position.z + dimension.longueur),
                new Point3D(position.x, position.y, position.z + dimension.longueur)
        }, color); // Bas
        polygons[1] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y + hauteurHautGauche, position.z),
                new Point3D(position.x + dimension.largeur, position.y + hauteurHautDroite, position.z),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z + dimension.longueur),
                new Point3D(position.x, position.y + dimension.hauteur, position.z + dimension.longueur)
        }, color); // Haut
        polygons[2] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y + hauteurOrigine, position.z),
                new Point3D(position.x, position.y + dimension.hauteur - hauteurHautGauche, position.z),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur - hauteurHautDroite, position.z),
                new Point3D(position.x + dimension.largeur, position.y + hauteurBasDroite, position.z)
        }, color); // Face (quand on reg
        polygons[3] = new Polygon3D(new Point3D[]{
                new Point3D(position.x + dimension.largeur, position.y + hauteurOrigine, position.z),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur - hauteurHautDroite, position.z),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z + dimension.longueur),
                new Point3D(position.x + dimension.largeur, position.y + hauteurBasDroite, position.z + dimension.longueur)
        }, color); // Gauche
        polygons[4] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y + hauteurOrigine, position.z + dimension.longueur),
                new Point3D(position.x, position.y + dimension.hauteur - hauteurHautGauche, position.z + dimension.longueur),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur - hauteurHautDroite, position.z + dimension.longueur),
                new Point3D(position.x + dimension.largeur, position.y + hauteurBasDroite, position.z + dimension.longueur)
        }, color); // Arrière
        polygons[5] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y + hauteurOrigine, position.z),
                new Point3D(position.x, position.y + dimension.hauteur - hauteurHautGauche, position.z),
                new Point3D(position.x, position.y + dimension.hauteur, position.z + dimension.longueur - dimension.longueur * tanHautGauche),
                new Point3D(position.x, position.y + hauteurBasDroite, position.z + dimension.longueur - dimension.longueur * tanBasDroite)
        }, color); // Droite
    }*/

    public Cube(Point3D position, Dimension3D dimension, Color color, AngleDeCoupe angleDeCoupe) {
        super(position, new Polygon3D[6], color);
        this.dimension = dimension;

        this.position = position;
        this.couleur = color;
        double tanOrigine = angleDeCoupe.getAngleOrigine()!=0 && angleDeCoupe.getAngleOrigine()!=90 ? Math.tan(Math.toRadians(angleDeCoupe.getAngleOrigine())) : 0;
        double largeurOrigine = tanOrigine!=0 ? dimension.largeur/tanOrigine : 0;
        double tanBasDroite = angleDeCoupe.getAngleBasDroite()!=0 && angleDeCoupe.getAngleBasDroite()!=90 ? Math.tan(Math.toRadians(angleDeCoupe.getAngleBasDroite())) : 0;
        double largeurBasDroite = tanBasDroite!=0 ? dimension.largeur/tanBasDroite : 0;
        double tanHautDroite = angleDeCoupe.getAngleHautDroite()!=0 && angleDeCoupe.getAngleHautDroite()!=90 ? Math.tan(Math.toRadians(angleDeCoupe.getAngleHautDroite())) : 0;
        double largeurHautDroite = tanHautDroite!=0 ? dimension.largeur/tanHautDroite : 0;
        double tanHautGauche = angleDeCoupe.getAngleHautGauche()!=0 && angleDeCoupe.getAngleHautGauche()!=90 ? Math.tan(Math.toRadians(angleDeCoupe.getAngleHautGauche())) : 0;
        double largeurHautGauche = tanHautGauche!=0 ? dimension.largeur/tanHautGauche : 0;

        polygons[0] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y, position.z+largeurBasDroite),
                new Point3D(position.x + dimension.largeur, position.y, position.z+largeurOrigine),
                new Point3D(position.x + dimension.largeur, position.y, position.z + dimension.longueur - largeurHautDroite),
                new Point3D(position.x, position.y, position.z + dimension.longueur - largeurHautGauche)
        }, color); // Bas
        polygons[1] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y + dimension.hauteur, position.z + largeurBasDroite),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z + largeurOrigine),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z + dimension.longueur - largeurHautDroite),
                new Point3D(position.x, position.y + dimension.hauteur, position.z + dimension.longueur - largeurHautGauche)
        }, color); // Haut
        polygons[2] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y, position.z+largeurBasDroite),
                new Point3D(position.x, position.y + dimension.hauteur, position.z+largeurBasDroite),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z+largeurOrigine),
                new Point3D(position.x + dimension.largeur, position.y, position.z+largeurOrigine)
        }, color); // Face (quand on regarde vers l'est)
        polygons[3] = new Polygon3D(new Point3D[]{
                new Point3D(position.x + dimension.largeur, position.y, position.z+largeurOrigine),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z+ largeurOrigine),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z + dimension.longueur - largeurHautDroite),
                new Point3D(position.x + dimension.largeur, position.y, position.z + dimension.longueur - largeurHautDroite)
        }, color); //Gauche
        polygons[4] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y, position.z + dimension.longueur-largeurHautGauche),
                new Point3D(position.x, position.y + dimension.hauteur, position.z + dimension.longueur-largeurHautGauche),
                new Point3D(position.x + dimension.largeur, position.y + dimension.hauteur, position.z + dimension.longueur - largeurHautDroite),
                new Point3D(position.x + dimension.largeur, position.y, position.z + dimension.longueur - largeurHautDroite)
        }, color); // Arrière
        polygons[5] = new Polygon3D(new Point3D[]{
                new Point3D(position.x, position.y, position.z + largeurBasDroite),
                new Point3D(position.x, position.y + dimension.hauteur, position.z + largeurBasDroite),
                new Point3D(position.x, position.y + dimension.hauteur, position.z + dimension.longueur - largeurHautGauche),
                new Point3D(position.x, position.y, position.z + dimension.longueur - largeurHautGauche)
        }, color); // Droite
    }

    private double calculateLengthCoupe(double angle){
        double length = 0;
        length = dimension.hauteur / Math.tan(angle);
        return length;
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

    public Cube(Cube cube) {
        super(cube.position, cube.polygons.clone(), cube.couleur);
        this.dimension = cube.dimension;
        this.position = cube.position;
        this.couleur = cube.couleur;
    }

    public void rotate(double xAngle, double yAngle, double zAngle) {
        // Mise à jour des angles de rotation
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        // Rotation des points de chaque polygon
        for (Polygon3D polygon : polygons) {
            for (Point3D point : polygon.points) {
                // Translation du point pour qu'il soit centré sur l'origine
                point.x -= position.x + dimension.largeur / 2;
                point.y -= position.y + dimension.hauteur / 2;
                point.z -= position.z + dimension.longueur / 2;

                // Rotation autour de l'axe x
                double tempY = point.y;
                point.y = point.y * Math.cos(xAngle) - point.z * Math.sin(xAngle);
                point.z = tempY * Math.sin(xAngle) + point.z * Math.cos(xAngle);

                // Rotation autour de l'axe y
                double tempX = point.x;
                point.x = point.x * Math.cos(yAngle) - point.z * Math.sin(yAngle);
                point.z = tempX * Math.sin(yAngle) + point.z * Math.cos(yAngle);

                // Rotation autour de l'axe z
                tempX = point.x;
                point.x = point.x * Math.cos(zAngle) - point.y * Math.sin(zAngle);
                point.y = tempX * Math.sin(zAngle) + point.y * Math.cos(zAngle);

                // Translation du point pour le ramener à sa position d'origine
                point.x += position.x + dimension.largeur / 2;
                point.y += position.y + dimension.hauteur / 2;
                point.z += position.z + dimension.longueur / 2;
            }
        }
    }

    public void rotateAroundCorner(double xAngle, double yAngle, double zAngle) {
        // Mise à jour des angles de rotation
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        // Rotation des points de chaque polygon
        for (Polygon3D polygon : polygons) {
            for (Point3D point : polygon.points) {
                // Translation du point pour qu'il soit centré sur l'origine
                point.x -= position.x;
                point.y -= position.y;
                point.z -= position.z;

                // Rotation autour de l'axe x
                double tempY = point.y;
                point.y = point.y * Math.cos(xAngle) - point.z * Math.sin(xAngle);
                point.z = tempY * Math.sin(xAngle) + point.z * Math.cos(xAngle);

                // Rotation autour de l'axe y
                double tempX = point.x;
//                point.x = point.x * Math.cos(yAngle) - point.z * Math.sin(yAngle);
                point.x = point.z * Math.sin(yAngle) - point.x * Math.cos(yAngle);
                point.z = tempX * Math.sin(yAngle) + point.z * Math.cos(yAngle);

                // Rotation autour de l'axe z
                tempX = point.x;
                point.x = point.x * Math.cos(zAngle) - point.y * Math.sin(zAngle);
                point.y = tempX * Math.sin(zAngle) + point.y * Math.cos(zAngle);

                // Translation du point pour le ramener à sa position d'origine
                point.x += position.x;
                point.y += position.y;
                point.z += position.z;
            }
            polygon.position = polygon.points[0];
        }
    }
    public void rotateDeg(double xAngle, double yAngle, double zAngle) {
        rotate(Math.toRadians(xAngle), Math.toRadians(yAngle), Math.toRadians(zAngle));
    }

    @Override
    public void rotateAroundPoint(Point3D pivot, double xAngle, double yAngle, double zAngle) {
        // Mise à jour des angles de rotation
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        // Rotation des points de chaque polygon
        for (Polygon3D polygon : polygons) {
            for (Point3D point : polygon.points) {
               point.rotateAroundPoint(pivot, xAngle, yAngle, zAngle);
            }
        }
        position = position.rotateAroundPoint(pivot, xAngle, yAngle, zAngle);
    }

    @Override
    public void faceTo(Point3D point) {

    }

    @Override
    public void update(Controleur3D controleur) {
        for (Polygon3D polygon : polygons) {
            polygon.update(controleur);
        }
    }

    @Override
    public Cube scale(double scale) {
        Cube cube = new Cube(this);
        for (int i = 0; i < polygons.length; i++) {
            cube.polygons[i] = cube.polygons[i].scale(scale);
        }
        return cube;
    }

}

