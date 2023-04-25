package ca.ulaval.glo2004._3D;

import java.awt.*;

public class Point3D implements java.io.Serializable {
    public double x;
    public double y;
    public double z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Point3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Point3D(Point3D p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    public Point3D set(Point3D p){
        this.x=p.x;
        this.y=p.y;
        this.z=p.z;
        return this;
    }

    public Point3D add(Point3D p){
        double newx = x + p.x;
        double newy = y + p.y;
        double newz = z + p.z;
        return new Point3D(newx, newy, newz);
    }
    public Point3D add(Vecteur v){
        double newx = x + v.point.x;
        double newy = y + v.point.y;
        double newz = z + v.point.z;
        return new Point3D(newx, newy, newz);
    }
    public Point3D substract(Point3D p){
        double newx = x - p.x;
        double newy = y - p.y;
        double newz = z - p.z;
        return new Point3D(newx, newy, newz);
    }
    public Point3D substract(Vecteur v){
        double newx = x - v.point.x;
        double newy = y - v.point.y;
        double newz = z - v.point.z;
        return new Point3D(newx, newy, newz);
    }
    public Point3D translate(Point3D vector) {
        double newX = x + vector.getX();
        double newY = y + vector.getY();
        double newZ = z + vector.getZ();
        return new Point3D(newX, newY, newZ);
    }
    public Point3D translate(Vecteur vector) {
        double newX = x + vector.getX();
        double newY = y + vector.getY();
        double newZ = z + vector.getZ();
        return new Point3D(newX, newY, newZ);
    }

    public Point3D multiplier(double d){
        double newx = x * d;
        double newy = y * d;
        double newz = z * d;
        return new Point3D(newx, newy, newz);
    }
    public Vecteur toVecteur(){
        return new Vecteur(this);
    }

    @Override
    public String toString() {
        return "Point3D{" + String.format("x=%.3f, y=%.3f, z=%.3f", x, y, z)+ '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point3D point)) return false;
        return this.getX() == point.getX()
                && this.getY() == point.getY()
                && this.getZ() == point.getZ();
    }

    public Point getProjection(Controleur3D controleur3D) {
        Point3D pt = Calculateur3D.calculerPosition(controleur3D.getPositionCamera(), controleur3D.getPositionCible(), this);
        Point3D focusPoint = controleur3D.getFocusPoint();
        double zoom = controleur3D.getZoom();
        Dimension tailleEcran = controleur3D.tailleEcran;
        double x = (tailleEcran.getWidth()/2 - focusPoint.getX()) + pt.getX() * zoom;
        double y = (tailleEcran.getHeight()/2 - focusPoint.getY()) + pt.getY() * zoom;

        return new Point((int) x, (int) y);
    }

    public Point3D rotateAroundPoint(Point3D pivot, double xAngle, double yAngle, double zAngle) {
        this.x -= pivot.x;
        this.y -= pivot.y;
        this.z -= pivot.z;

        // Rotation autour de l'axe x
        double tempY = this.y;
        this.y = this.y * Math.cos(xAngle) - this.z * Math.sin(xAngle);
        this.z = tempY * Math.sin(xAngle) + this.z * Math.cos(xAngle);

        // Rotation autour de l'axe y
        double tempX = this.x;
        this.x = this.x * Math.cos(yAngle) - this.z * Math.sin(yAngle);
        this.z = tempX * Math.sin(yAngle) + this.z * Math.cos(yAngle);

        // Rotation autour de l'axe z
        tempX = this.x;
        this.x = this.x * Math.cos(zAngle) - this.y * Math.sin(zAngle);
        this.y = tempX * Math.sin(zAngle) + this.y * Math.cos(zAngle);

        // Translation du point pour le ramener à sa position d'origine
        this.x += pivot.x;
        this.y += pivot.y;
        this.z += pivot.z;

        return this;
    }

}