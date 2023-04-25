package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

public abstract class Objet3D {
    public UUID id;
    public Polygon3D[] polygons;
    public Point3D position;
    public Color couleur;
    public double scale = 1;
    public double rotationY;
    public double rotationX;
    public double rotationZ;
    public boolean visible = true;
    public boolean estValide = true;
    public boolean selectable = true;

    public Objet3D(Point3D position, Polygon3D[] polygons, Color color) {
        this.position = position;
        this.polygons = polygons;
        this.couleur = color;
//        id = UUID.randomUUID();
    }
    public Objet3D(Point3D position, Polygon3D[] polygons, Color color, boolean selectable) {
        this.position = position;
        this.polygons = polygons;
        this.couleur = color;
        this.selectable = selectable;
//        id = UUID.randomUUID();

    }
    public Objet3D(){
        this.position = new Point3D(0,0,0);
        this.polygons = new Polygon3D[0];
        this.couleur = Color.BLACK;
//        id = UUID.randomUUID();
    }

    public Point3D getCenter() {
        double x = 0;
        double y = 0;
        double z = 0;
        for (Polygon3D polygon : polygons) {
            x += polygon.getCenter().x;
            y += polygon.getCenter().y;
            z += polygon.getCenter().z;
        }
        return new Point3D(x / polygons.length, y / polygons.length, z / polygons.length);
    }

    void setEstValide(boolean estValide) {
        this.estValide = estValide;
        if (polygons == null) return;
        for (Polygon3D polygon : polygons) {
            polygon.estValide = estValide;
        }
    }

    public Polygon3D[] getPolygons() { return this.polygons; }

    public void setPolygons(Polygon3D[] polygons) { this.polygons = polygons; }

    public Point3D getPosition() { return this.position; }

    public void setPosition(Point3D position) { this.position = position; }

    public Color getCouleur() { return this.couleur; }

    public void setCouleur(Color couleur) { this.couleur = couleur; }

    public double getRotationY() { return this.rotationY; }

    public double getRotationX() { return this.rotationX; }

    public double getRotationZ() { return this.rotationZ; }

    public boolean isVisible() { return this.visible; }
    public UUID getId() { return this.id; }

    public void setVisible(boolean visible) {
        this.visible = visible;
        Arrays.stream(polygons).forEach(polygon -> polygon.setVisible(visible));
    }

    /**
     * Rotation de l'objet autour de son centre
     * @param xAngle Angle de rotation autour de l'axe X (en radians)
     * @param yAngle Angle de rotation autour de l'axe Y (en radians)
     * @param zAngle Angle de rotation autour de l'axe Z (en radians)
     *
    */
    public abstract void rotate(double xAngle, double yAngle, double zAngle);

    public abstract void rotateAroundCorner(double xAngle, double yAngle, double zAngle);

    public void rotateDeg(double xAngle, double yAngle, double zAngle) {
        rotate(Math.toRadians(xAngle), Math.toRadians(yAngle), Math.toRadians(zAngle));
    }

    public double getRayon() {
        Point3D center = getCenter();
        double max = 0;
        for (Polygon3D polygon : polygons) {
            double distance = Calculateur3D.calculerDistanceDeP(center, polygon.getCenter());
            if (distance > max) {
                max = distance;
            }
        }
        return max;
    }

    public abstract Vecteur getNormal(Polygon3D polygon3D);

    public void setRotationY(double rotationY) {
        double angleY = rotationY - this.rotationY;
        rotate(0, angleY, 0);
    }

    public void setRotationX(double rotationX) {
        double angleX = rotationX - this.rotationX;
        rotate(angleX, 0, 0);
    }

    public void translate(double x, double y, double z) {
        // Implémentation de la translation
        for (Polygon3D polygon : this.polygons) {
            // Implémentation de la translation
           polygon.translate(new Point3D(x, y, z));
        }
        position = position.translate(new Point3D(x,y,z));
    }
    public void translate(Point3D point) {
        // Implémentation de la translation
        for (Polygon3D polygon : this.polygons) {
            // Implémentation de la translation
            polygon.translate(point);
        }
        position = position.translate(point);
    }

    public abstract void rotateAroundPoint(Point3D pivot, double xAngle, double yAngle, double zAngle);

    public abstract void faceTo(Point3D point);

    public void setRotationZ(double rotationZ) {
        double angleZ = rotationZ - this.rotationZ;
        rotate(0, 0, angleZ);
    }

    public void orienter(PointCardinal pointCardinal) {
        double angle = pointCardinal.getDirection();
        rotate(0, Math.toRadians(angle), 0);
    }

    public void orienterAroundCorner(PointCardinal pointCardinal) {
//        double angle = Calculateur3D.pointCardinalToAngle(pointCardinal);
        double angle = pointCardinal.getDirection();
        rotateAroundCorner(0, Math.toRadians(angle), 0);
    }

    abstract void update(Controleur3D controleur3D);

    boolean contient(Polygon3D polygon){
        for (Polygon3D poly : polygons) {
            if (poly.equals(polygon)) {
                return true;
            }
        }
        return false;
    }

    boolean contient(Polygon2D polygon){
        for (Polygon3D poly : polygons) {
            if (poly.projection.equals(polygon)) {
                return true;
            }
        }
        return false;
    }
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
        Arrays.stream(this.polygons).forEach(p -> p.selectable = selectable);
    }

    public abstract <T extends Objet3D> T scale(double scale);


    @Override
    public String toString() {
        return "{" +
            " id='" + id + "'\n" +
            ", position='" + getPosition() + "'" +
            ", couleur='" + getCouleur() + "'" +
            ", rotationY='" + getRotationY() + "'" +
            ", rotationX='" + getRotationX() + "'" +
            ", rotationZ='" + getRotationZ() + "'" +
            ", visible='" + isVisible() + "'" +
            "}";
    }
}
