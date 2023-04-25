package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;

import java.awt.*;

public class Ligne extends Objet3D{
    Point3D[] points = new Point3D[2];
    public double epaisseur;

    public Ligne(Point3D point1, Point3D point2, Color color, double epaisseur) {
        super();
        this.points[0] = point1;
        this.points[1] = point2;
        this.couleur = color;
        this.epaisseur = epaisseur;
        this.position = new Point3D((point1.x+point2.x)/2, (point1.y+point2.y)/2, (point1.z+point2.z)/2); //Centre de la ligne
        this.polygons = new Polygon3D[]{new Polygon3D(new Point3D[]{point1, point2}, color)};
    }

    @Override
    public void rotate(double xAngle, double yAngle, double zAngle) {
        // Mise à jour des angles de rotation
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        Dimension3D dimension = new Dimension3D(0, 0, 0);
        for (Point3D point : points) {
            dimension.largeur = Math.max(dimension.largeur, point.x);
            dimension.hauteur = Math.max(dimension.hauteur, point.y);
            dimension.longueur = Math.max(dimension.longueur, point.z);
        }

        // Rotation des points de chaque polygon
        for (Point3D point : points) {
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
    public void rotateAroundCorner(double xAngle, double yAngle, double zAngle) {
        // Mise à jour des angles de rotation
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        Dimension3D dimension = new Dimension3D(0, 0, 0);
        for (Point3D point : points) {
            dimension.largeur = Math.max(dimension.largeur, point.x);
            dimension.hauteur = Math.max(dimension.hauteur, point.y);
            dimension.longueur = Math.max(dimension.longueur, point.z);
        }

        // Rotation des points de chaque polygon
        for (Point3D point : points) {
            // Translation du point pour qu'il soit centré sur l'origine
            point.x -= position.x + dimension.largeur;
            point.y -= position.y + dimension.hauteur;
            point.z -= position.z + dimension.longueur;

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
            point.x += position.x + dimension.largeur;
            point.y += position.y + dimension.hauteur;
            point.z += position.z + dimension.longueur;
        }

    }

    Line2D getProjection() {
        Polygon polygon = polygons[0].projection.polygon;
        Point pt1 = new Point(polygon.xpoints[0], polygon.ypoints[0]);
        Point pt2 = new Point(polygon.xpoints[1], polygon.ypoints[1]);
        return new Line2D(pt1, pt2, couleur, epaisseur);
    }

    @Override
    public Vecteur getNormal(Polygon3D polygon3D) {
        return null;
    }


    @Override
    public void rotateAroundPoint(Point3D pivot, double xAngle, double yAngle, double zAngle) {

    }

    @Override
    public void faceTo(Point3D point) {

    }

    @Override
    void update(Controleur3D controleur3D) {
        polygons[0].update(controleur3D);
    }
    void update(Controleur3D controleur3D, boolean rescale, boolean clip) {
        polygons[0].update(controleur3D, rescale, clip);
    }
    @Override
    public <T extends Objet3D> T scale(double scale) {
        return null;
    }
    public Ligne rallonger(double longueur){
        Point3D point1 = points[0];
        Point3D point2 = points[1];
        Point3D vecteur = new Point3D(point2.x-point1.x, point2.y-point1.y, point2.z-point1.z);
        vecteur = vecteur.multiplier(longueur);
        Point3D point3 = new Point3D(point2.x+vecteur.x, point2.y+vecteur.y, point2.z+vecteur.z);
        return new Ligne(point2, point3, couleur, epaisseur);
    }
}
