package ca.ulaval.glo2004.Utilitaires;

public class Dimension3D {
    public double longueur;
    public double largeur;
    public double hauteur;

    public Dimension3D(double longueur, double largeur, double hauteur) {
        this.longueur = longueur;
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    @Override
    public boolean equals(Object obj) {
        Dimension3D dimensionACompare = (Dimension3D) obj;

        return this.longueur == dimensionACompare.longueur &&
                this.largeur == dimensionACompare.largeur &&
                this.hauteur == dimensionACompare.hauteur;
    }

    public Dimension3D multiplier(double multiplicateur) {
        return new Dimension3D(longueur * multiplicateur, largeur * multiplicateur, hauteur * multiplicateur);
    }

    public Dimension3D diviser(double diviseur) {
        return new Dimension3D(longueur / diviseur, largeur / diviseur, hauteur / diviseur);
    }
}