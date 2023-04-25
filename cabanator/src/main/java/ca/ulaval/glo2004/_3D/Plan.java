package ca.ulaval.glo2004._3D;

import java.awt.*;
public class Plan {
    public Vecteur vecteur1, vecteur2, vecteurNormal;
    public Point3D point;
    public float distance;
    public Color color = Color.GREEN;
    public boolean isFacingOutward = true;
    public Plan(Polygon3D polygon3D)
    {
        point = new Point3D(polygon3D.points[0].getX(), polygon3D.points[0].getY(), polygon3D.points[0].getZ());

        Point3D point1 = new Point3D(polygon3D.points[1].getX() - polygon3D.points[0].getX(),
                polygon3D.points[1].getY() - polygon3D.points[0].getY(),
                polygon3D.points[1].getZ() - polygon3D.points[0].getZ());
        vecteur1 = new Vecteur(point1).normaliser();
        Point3D point2 = new Point3D(polygon3D.points[2].getX() - polygon3D.points[0].getX(),
                polygon3D.points[2].getY() - polygon3D.points[0].getY(),
                polygon3D.points[2].getZ() - polygon3D.points[0].getZ());
        vecteur2 = new Vecteur(point2).normaliser();
        vecteurNormal = vecteur1.produitVectoriel(vecteur2).normaliser();
        color = polygon3D.couleur;
        distance = (float) (vecteurNormal.getX() * point.getX() + vecteurNormal.getY() * point.getY() + vecteurNormal.getZ() * point.getZ());
    }
    public Plan(Plan plan){
        this.point = plan.point;
        this.vecteur1 = plan.vecteur1;
        this.vecteur2 = plan.vecteur2;
        this.vecteurNormal = plan.vecteurNormal;
        this.distance = plan.distance;
        this.color = plan.color;
        this.isFacingOutward = plan.isFacingOutward;
    }

    public Plan(Polygon3D polygon3D, Point3D cameraPosition) {
        point = new Point3D(polygon3D.points[0].getX(), polygon3D.points[0].getY(), polygon3D.points[0].getZ());

        Point3D point1 = new Point3D(polygon3D.points[1].getX() - polygon3D.points[0].getX(),
                polygon3D.points[1].getY() - polygon3D.points[0].getY(),
                polygon3D.points[1].getZ() - polygon3D.points[0].getZ());
        vecteur1 = new Vecteur(point1).normaliser();
        Point3D point2 = new Point3D(polygon3D.points[2].getX() - polygon3D.points[0].getX(),
                polygon3D.points[2].getY() - polygon3D.points[0].getY(),
                polygon3D.points[2].getZ() - polygon3D.points[0].getZ());
        vecteur2 = new Vecteur(point2).normaliser();

        vecteurNormal = vecteur1.produitVectoriel(vecteur2).normaliser();
        color = polygon3D.couleur;
        distance = (float) (vecteurNormal.getX() * point.getX() + vecteurNormal.getY() * point.getY() + vecteurNormal.getZ() * point.getZ());


        //Si l'angle entre le vecteur 1 et le vecteur 2 est positif, la face est orientée vers l'intérieur
        // cos^-1 ((a . b) / (||a|| ||b||))
//        double angle = Math.acos(vecteur1.produitScalaire(vecteur2) / (vecteur1.longueur * vecteur2.longueur));
//        isFacingOutward = angle >= 0;
        // Vérifier l'orientation de la face
        Point3D faceCenter = polygon3D.getCenter(); // Centre de la face
        Vecteur cameraToCenter = new Vecteur(faceCenter, cameraPosition); // Vecteur de la position de la caméra vers le centre de la face
        double dotProduct = cameraToCenter.produitScalaire(vecteurNormal); // Produit scalaire entre le vecteur de la caméra et la normale de la face
        isFacingOutward = dotProduct >= 0; // La face est orientée vers l'extérieur si le produit scalaire est positif, sinon elle est orientée vers l'intérieur
    }

    public Plan(Vecteur VE1, Vecteur VE2, Point3D point)
    {
        this.point = point;
        vecteur1 = VE1;
        vecteur2 = VE2;
        vecteurNormal = vecteur1.produitVectoriel(vecteur2);
    }

    public Point3D getPoint() { return this.point; }
    public void setPoint(Point3D point) { this.point = point; }
    public double getX() { return point.getX(); }
    public void setX(double x) { point.setX(x); }
    public double getY() { return point.getY(); }
    public void setY(double y) { point.setY(y); }
    public double getZ() { return point.getZ(); }
    public void setZ(double z) { point.setZ(z); }
    public Vecteur getVecteurNormal() { return this.vecteurNormal; }

    public Point3D[] getPoints(){
        Vecteur v1 = new Vecteur(new Point3D(1, 0, -vecteurNormal.getX() / vecteurNormal.getZ()));
        v1 = v1.normaliser();

        // calcul du deuxième vecteur orthogonal
        Vecteur v2 = vecteurNormal.produitVectoriel(v1);
        v2 = v2.normaliser();

        // calcul des sommets du polygone 3D
        Point3D p1 = new Point3D(point.getX() - v1.getX() + v2.getX(), point.getY() - v1.getY() + v2.getY(), point.getZ() - v1.getZ() + v2.getZ()); // en haut à gauche
        Point3D p2 = new Point3D(point.getX() + v1.getX() + v2.getX(), point.getY() + v1.getY() + v2.getY(), point.getZ() + v1.getZ() + v2.getZ()); // en haut à droite
        Point3D p3 = new Point3D(point.getX() + v1.getX() - v2.getX(), point.getY() + v1.getY() - v2.getY(), point.getZ() + v1.getZ() - v2.getZ()); // en bas à droite
        Point3D p4 = new Point3D(point.getX() - v1.getX() - v2.getX(), point.getY() - v1.getY() - v2.getY(), point.getZ() - v1.getZ() - v2.getZ()); // en bas à gauche

        Point3D[] points = new Point3D[4];
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        points[3] = p4;
        return points;
    }

    public Polygon3D toPolygon3D() {
        Point3D[] points = getPoints();
        return new Polygon3D(points, color);
    }

    public double distanceToPoint(Point3D point) {
        double distance = 0;
        Point3D ptV = new Point3D(point.getX() - getX(), point.getY() - getY(), point.getZ() - getZ());
        // Calcul de la distance entre le point et le plan
        distance = Math.abs(vecteurNormal.produitScalaire(new Vecteur(ptV)));
        return distance;
    }

    public Plan scale(double scale) {
        //scale the plane by a factor from its center
        Point3D[] points = getPoints();
        Point3D center = new Point3D((points[0].getX() + points[1].getX() + points[2].getX() + points[3].getX()) / 4,
                (points[0].getY() + points[1].getY() + points[2].getY() + points[3].getY()) / 4,
                (points[0].getZ() + points[1].getZ() + points[2].getZ() + points[3].getZ()) / 4);
        Vecteur v1 = new Vecteur(this.vecteur1);
        Vecteur v2 = new Vecteur(this.vecteur2);
        v1 = v1.normaliser();
        v2 = v2.normaliser();
        v1 = v1.multiplier(scale);
        v2 = v2.multiplier(scale);
        return new Plan(v1, v2, center);
    }
}
