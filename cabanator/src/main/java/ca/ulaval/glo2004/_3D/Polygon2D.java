package ca.ulaval.glo2004._3D;

import java.awt.*;

/// Classe qui représente un simple polygone 2D + qui contient sa couleur
public class Polygon2D {
    Polygon polygon;
    Color couleur;
    Vecteur normalVecteur;
    public Line2D normal;
    Color contour = Color.BLACK;
    double distanceMoyenne = 0;
    boolean visible = true;
    boolean estValide = true;
    boolean selectable = true;
    public Polygon2D(double[] xPoints, double[] yPoints, Color color) {
        polygon = new Polygon();
        for (int i = 0; i < xPoints.length; i++)
            polygon.addPoint((int) xPoints[i], (int) yPoints[i]);
        this.couleur = color;
    }
    public Polygon2D(double[] xPoints, double[] yPoints, Color color, boolean selectable) {
        this (xPoints, yPoints, color);
        this.selectable = selectable;
    }
    public Polygon2D(Point[] points, Color color, boolean selectable, boolean visible) {
        this (points, color);
        this.selectable = selectable;
        this.visible = visible;
        if (getPolygon().npoints < 3) {
            setVisible(false);
            selectable = false;
        }
    }
    public Polygon2D(Polygon2D polygon2D) {
        int[] xPoints = polygon2D.getPolygon().xpoints;
        int[] yPoints = polygon2D.getPolygon().ypoints;
        polygon = new Polygon();
        for (int i = 0; i < xPoints.length; i++)
            polygon.addPoint(xPoints[i], yPoints[i]);
        this.couleur = polygon2D.getCouleur();
        this.contour = polygon2D.getContour();
        this.selectable = polygon2D.isSelectable();
        this.visible = polygon2D.isVisible();
        this.distanceMoyenne = polygon2D.getDistanceMoyenne();
        if (getPolygon().npoints < 3) {
            setVisible(false);
            selectable = false;
        }
        this.normal = new Line2D(polygon2D.normal);
    }
    public Polygon2D(Point[] points, Color color) {
        polygon = new Polygon();
        for (Point point : points) polygon.addPoint(point.x, point.y);
        this.couleur = color;
        if (getPolygon().npoints < 3) {
            setVisible(false);
        }
    }

    public Polygon2D(Point[] points, Color color, boolean selectable) {
        polygon = new Polygon();
        for (Point point : points) polygon.addPoint(point.x, point.y);
        this.couleur = color;
        this.selectable = selectable;
        if (getPolygon().npoints < 3) {
            couleur = Color.BLACK;
            setVisible(false);
            selectable = false;
        }
    }
    public boolean isSelectable() { return selectable; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public double getDistanceMoyenne() { return distanceMoyenne; }
    public void setDistanceMoyenne(double distanceMoyenne) { this.distanceMoyenne = distanceMoyenne; }
    public Color getCouleur() { return couleur; }
    public void setCouleur(Color couleur) { this.couleur = couleur; }
    public Color getContour() { return contour; }
    public void setContour(Color contour) { this.contour = contour; }
    public Polygon getPolygon() { return polygon; }
    public void setPolygon(Polygon polygon) { this.polygon = polygon; }

    public void dessinerPolygon(Graphics g) {
        if (!visible) return;
        g.setColor(couleur);
        g.fillPolygon(polygon);
        g.setColor(contour);
        g.drawPolygon(polygon);
    }

    public boolean canDraw(Controleur3D controleur3D) {
        if (!visible) return false;
        Vecteur vue = controleur3D.controleurCamera.positionCible.substract(controleur3D.controleurCamera.positionCamera).toVecteur();
        Point3D positionCameraOffset = new Point3D(controleur3D.controleurCamera.positionCamera);
//        Point3D positionCameraOffset = new Point3D(controleur3D.controleurCamera.positionCamera).add(vue.multiplier(controleur3D.seuilCulling3));
        Vecteur viewVector = new Vecteur(positionCameraOffset, vue.getPoint());
        Vecteur normal = normalVecteur;
        double threshold_cosine = Math.cos(Math.toRadians(controleur3D.seuilCulling));
        double longeur = normal.longueur * viewVector.longueur;
        if (longeur == 0)
            longeur = 1;
        double polygon_cosine = Math.abs(normal.produitScalaire(viewVector)) / (longeur);

        double dotProduct = normal.normaliser().produitScalaire(viewVector.normaliser());
        boolean faceArriere = dotProduct > 0 && polygon_cosine < threshold_cosine;
        return visible && !faceArriere;
    }


    boolean estPointe(Point pointCurseur)
    {
        return polygon.contains(pointCurseur);
    }

    @Override
    public String toString() {
        return "Polygon2D{" +
                "polygon=" + polygon +
                ", couleur=" + couleur +
                ", distanceMoyenne=" + distanceMoyenne +
                ", visible=" + visible +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polygon2D that = (Polygon2D) o;
        return Double.compare(that.distanceMoyenne, distanceMoyenne) == 0 &&
                visible == that.visible &&
                polygon.equals(that.polygon) &&
                couleur.equals(that.couleur);
    }

}
