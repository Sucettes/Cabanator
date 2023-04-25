package ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires;

import java.io.Serializable;

/**
 * Angle pour pouvoir couper les planches. entre 0 et 90
 */
public class AngleDeCoupe implements Serializable {
    private double angleOrigine; // angle en bas a gauche (origine)
    private double angleBasDroite; // angle en bas a droite (a droit de l<origine)
    private double angleHautDroite; // Angle a droite en haut (oposer en diagonal de l'origine)
    private double angleHautGauche; // angle en haut de l'origine

    /**
     * Constructeur sans coupe. (Par defauts)
     */
    public AngleDeCoupe() {
        this.setAngleOrigine(0);
        this.setAngleBasDroite(0);
        this.setAngleHautDroite(0);
        this.setAngleHautGauche(0);
    }

    public AngleDeCoupe(double angleOrigine, double angleBasDroite, double angleHautDroite, double angleHautGauche) {
        this.setAngleOrigine(angleOrigine);
        this.setAngleBasDroite(angleBasDroite);
        this.setAngleHautDroite(angleHautDroite);
        this.setAngleHautGauche(angleHautGauche);
    }

    public double getAngleOrigine() {
        return angleOrigine;
    }

    public void setAngleOrigine(double angleOrigine) {
        this.angleOrigine = angleOrigine;
    }

    public double getAngleBasDroite() {
        return angleBasDroite;
    }

    public void setAngleBasDroite(double angleBasDroite) {
        this.angleBasDroite = angleBasDroite;
    }

    public double getAngleHautDroite() {
        return angleHautDroite;
    }

    public void setAngleHautDroite(double angleHautDroite) {
        this.angleHautDroite = angleHautDroite;
    }

    public double getAngleHautGauche() {
        return angleHautGauche;
    }

    public void setAngleHautGauche(double angleHautGauche) {
        this.angleHautGauche = angleHautGauche;
    }

    @Override
    public String toString() {
        return angleOrigine + ":" + angleBasDroite + ":" + angleHautDroite + ":" + angleHautGauche;
    }
}