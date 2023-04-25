package ca.ulaval.glo2004._3D;

import java.awt.*;

public class Vecteur implements java.io.Serializable {
    Point3D point;
    double longueur;
    public Vecteur(){
        this.point = new Point3D(0, 0, 0);
        this.longueur = 0;
    }
    public Vecteur(double x, double y, double z) {
        this.point = new Point3D(x, y, z);
        this.longueur = calculerLongueur();
    }
    public Vecteur(Point3D point) {
        longueur = calculerLongueur(point);
        this.point = new Point3D(point);
    }

    public Vecteur(Vecteur vecteur) {
        this.point = new Point3D(vecteur.point);
        this.longueur = vecteur.longueur;
    }
    public Vecteur(Point3D point1, Point3D point2) {
        double x = point2.getX() - point1.getX();
        double y = point2.getY() - point1.getY();
        double z = point2.getZ() - point1.getZ();
        this.point = new Point3D(x, y, z);
        this.longueur = calculerLongueur();
    }
    public Vecteur set(Vecteur V){
        this.point.x=V.point.x;
        this.point.y=V.point.y;
        this.point.z=V.point.z;
        this.longueur=calculerLongueur();
        return this;
    }

    public Point3D getPoint() { return this.point; }
    public void setPoint(Point3D point) { this.point = point; }
    public double getLongueur() { return calculerLongueur(); }
    public double getX() { return point.getX(); }
    public void setX(double x) { point.setX(x); }
    public double getY() { return point.getY(); }
    public void setY(double y) { point.setY(y); }
    public double getZ() { return point.getZ(); }
    public void setZ(double z) { point.setZ(z); }

    private double calculerLongueur(){
        //Cours de math rapide:
        //La longueur d'un vecteur est la racine carrée de la somme des carrés de ses composantes (√(x² + y² + z²).)
        return Math.sqrt(getX()*getX() + getY()*getY() + getZ()*getZ());
    }
    private double calculerLongueur(Point3D point){
        //Cours de math rapide:
        //La longueur d'un vecteur est la racine carrée de la somme des carrés de ses composantes (√(x² + y² + z²).)
        return Math.sqrt(point.getX()*point.getX() + point.getY()*point.getY() + point.getZ()*point.getZ());
    }

    public Vecteur produitVectoriel(Vecteur vecteur){
        //Merci les cours de math :)
        return new Vecteur(new Point3D(
                getZ()*vecteur.getY() - getY()*vecteur.getZ(),
                getX()*vecteur.getZ() - getZ()*vecteur.getX(),
                getY()*vecteur.getX() - getX()*vecteur.getY()
        ));
    }

    public Vecteur normaliser(){
        if (getLongueur() == 0) return this;
        return new Vecteur(new Point3D(
                getX()/getLongueur(),
                getY()/getLongueur(),
                getZ()/getLongueur()
        ));
    }
    public Vecteur multiplier(double multiplicateur){
        return new Vecteur(new Point3D(
                getX()*multiplicateur,
                getY()*multiplicateur,
                getZ()*multiplicateur
        ));
    }
    public Vecteur diviser(double diviseur){
        return new Vecteur(new Point3D(
                getX()/diviseur,
                getY()/diviseur,
                getZ()/diviseur
        ));
    }
    public Vecteur additionner(Vecteur V){
        return new Vecteur(new Point3D(
                getX() + V.getX(),
                getY() + V.getY(),
                getZ() + V.getZ()
        ));
    }

    public Vecteur soustraire(Vecteur V) {
        return new Vecteur(new Point3D(
                getX() - V.getX(),
                getY() - V.getY(),
                getZ() - V.getZ()
        ));
    }

    public Vecteur soustraire(Point3D p) {
        return new Vecteur(new Point3D(
                getX() - p.getX(),
                getY() - p.getY(),
                getZ() - p.getZ()
        ));
    }

    public boolean isParallelTo(Vecteur vecteur){
        Vecteur thisNormalise = this.normaliser();
        return thisNormalise.produitVectoriel(vecteur.normaliser()).getLongueur() == 0;
    }

    Ligne obtenirLigne(Point3D pointDeDepart, double longueur, Color couleur, double epaisseur){
        Point3D ptNormal = pointDeDepart  // On crée un point à partir du centre du polygone
                .translate(  // On fait une translation du point (centre) avec le vecteur normal
                        getPoint().multiplier(longueur));  // On multiplie le vecteur normal par 2 (pour donner une longueur à la ligne)

        return new Ligne(pointDeDepart, ptNormal, couleur, epaisseur); // On crée la ligne
    }

    public double produitScalaire(Vecteur vecteur){
        return getX()*vecteur.getX() + getY()*vecteur.getY() + getZ()*vecteur.getZ();
    }
    public double produitScalaire(Point3D point){
        return getX()*point.getX() + getY()*point.getY() + getZ()*point.getZ();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Vecteur)) return false;
        Vecteur vecteur = (Vecteur) obj;
        return vecteur.point.equals(this.point) && vecteur.longueur == this.longueur;
    }

    @Override
    public String toString() {
        return "Vecteur{" +
                "point=" + point +
                ", longueur=" + longueur +
                '}';
    }
}