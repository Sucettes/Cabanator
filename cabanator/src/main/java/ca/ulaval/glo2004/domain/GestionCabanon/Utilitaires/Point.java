package ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires;

import ca.ulaval.glo2004.domain.GestionCabanon.Interfaces.IClonable;

public class Point implements IClonable<Point>, java.io.Serializable {
    private double x;
    private double y;
    private double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    /**
     * Configure la position en Y de la copie de l'instance courante.
     */
    public Point configurerY(double y) {
        Point point = this.cloner();
        point.setY(y);
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point point)
            return this.getX() == point.getX()
                    && this.getY() == point.getY()
                    && this.getZ() == point.getZ();
        return false;
    }

    @Override
    public String toString() {
        return this.getX() + ", " + this.getY() + ", " + this.getZ();
    }

    @Override
    public Point cloner() {
        return new Point(this.getX(), this.getY(), this.getZ());
    }
}