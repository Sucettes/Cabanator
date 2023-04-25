package ca.ulaval.glo2004.domain.GestionCabanon.Classes;

import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Interfaces.IClonable;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;

public class Murs implements IClonable<Murs>, java.io.Serializable {

    //#region ATTRIBUTS

    private double hauteur;
    private ArrayList<Mur> murs;

    //#endregion

    //#region CONSTRUCTEUR

    private Murs(double hauteur, ArrayList<Mur> murs) {
        this.hauteur = hauteur;
        this.murs = murs;
    }

    public Murs(double pY, double hauteur, double longueur, double largeur) {
        this.setHauteur(hauteur);
        this.setMurs(pY, longueur, largeur);
    }

    //#endregion

    //#region PROPRIÉTÉS ET INDEXEURS

    public double getHauteur() {
        return this.hauteur;
    }

    private void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    private void setMurs(double pY, double longueur, double largeur) {
        ArrayList<Mur> murs = new ArrayList<>();
        double largeurInterieure = this.getLargeurInterieure(largeur);

        murs.add(new Mur(
                PointCardinal.SUD,
                PointCardinal.NORD,
                this.getPositionMurSud(longueur, pY),
                largeurInterieure,
                this.hauteur));

        murs.add(new Mur(
                PointCardinal.EST,
                PointCardinal.OUEST,
                this.getPositionMurEst(pY, largeur),
                longueur,
                this.hauteur));

        murs.add(new Mur(
                PointCardinal.NORD,
                PointCardinal.NORD,
                this.getPositionMurNord(pY),
                largeurInterieure,
                this.hauteur));

        murs.add(new Mur(
                PointCardinal.OUEST,
                PointCardinal.OUEST,
                this.getPositionMurOuest(pY),
                longueur,
                this.hauteur));

        this.murs = murs;
    }

    //#endregion

    //#region MÉTHODES

    public void configurerHauteur(double hauteur) {
        this.hauteur = hauteur;
        for (Mur mur : this.murs) {
            mur.configurerLargeur(hauteur);
        }
    }

    public void configurerLongueur(double longueur) {
        Mur murO = this.getMur(PointCardinal.OUEST);
        murO.configurerLongueur(longueur);
        this.getMur(PointCardinal.EST).configurerLongueur(longueur);
        this.getMur(PointCardinal.SUD)
                .setOrigine(this.getPositionMurSud(longueur, murO.getOrigine().getY()));
    }

    public void configurerLargeur(double largeur) {
        Mur murN = this.getMur(PointCardinal.NORD);
        double largeurInterieure = this.getLargeurInterieure(largeur);
        murN.configurerLongueur(largeurInterieure);
        this.getMur(PointCardinal.SUD).configurerLongueur(largeurInterieure);

        this.getMur(PointCardinal.EST)
                .setOrigine(this.getPositionMurEst(murN.getOrigine().getY(), largeur));
    }

    public Mur getMur(PointCardinal point) {
        return this.murs.stream()
                .filter(mur -> mur.getPositionMur() == point)
                .findFirst()
                .orElse(null);
    }

    public void regenererStructureMurs() {
        this.murs.forEach(Mur::regenererStructureMur);
    }

    @Override
    public Murs cloner() {
        ArrayList<Mur> murs = new ArrayList<>();

        for (PointCardinal pointCardinal : PointCardinal.values()) {
            murs.add(this.getMur(pointCardinal).cloner());
        }

        return new Murs(this.hauteur, murs);
    }

    private Point getPositionMurNord(double pY) {
        return new Point(
                0 - Mur.T_PLANCHE_EST_OUEST.getLargeur(),
                pY,
                0 + Mur.T_PLANCHE_EST_OUEST.getLargeur());

    }

    private Point getPositionMurSud(double longueur, double pY) {
        return new Point(-longueur, pY, 0 + Mur.T_PLANCHE_EST_OUEST.getLargeur());
    }

    private Point getPositionMurEst(double pY, double largeur) {
        return new Point(0, pY, largeur - Mur.T_PLANCHE_EST_OUEST.getLargeur());
    }

    private Point getPositionMurOuest(double pY) {
        return new Point(0, pY, 0);
    }

    private double getLargeurInterieure(double largeur) {
        return largeur - (Mur.T_PLANCHE_EST_OUEST.getLargeur() * 2);
    }

    //#endregion
}