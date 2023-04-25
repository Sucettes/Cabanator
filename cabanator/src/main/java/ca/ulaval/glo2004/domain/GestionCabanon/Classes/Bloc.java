package ca.ulaval.glo2004.domain.GestionCabanon.Classes;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Interfaces.IClonable;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Bloc implements IClonable<Bloc>, java.io.Serializable {

    //#region ATTRIBUTS

    private ArrayList<Planche> planches;
    private double longueur;
    private double hauteur;
    private Point origine;

    //#endregion

    //#region CONSTRUCTEURS

    public Bloc(ArrayList<Planche> planches, double longueur, double hauteur, Point origine) {
        this.setPlanches(planches);
        this.setLongueur(longueur);
        this.setHauteur(hauteur);
        this.setOrigine(origine);
    }

    //#endregion

    //#region PROPRIÉTÉS ET INDEXEURS

    public ArrayList<Planche> getPlanches() {
        return this.planches;
    }

    private void setPlanches(ArrayList<Planche> planches) {
        this.planches = planches;
    }

    double getLongueur() {
        return longueur;
    }

    private void setLongueur(double longueur) {
        this.longueur = longueur;
    }

    double getHauteur() {
        return hauteur;
    }

    private void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    Point getOrigine() {
        return this.origine;
    }

    private void setOrigine(Point origine) {
        this.origine = origine;
    }

    //#endregion

    //#region MÉTHODES

    Planche getFirstPlanche(double direction) {
        double directionAFiltre;
        int obtenirPremierePlanche = 0;

        if (direction == PointCardinal.SUD.getDirection()) {
            directionAFiltre = PointCardinal.NORD.getDirection();
            obtenirPremierePlanche = 1;
        } else if (direction == PointCardinal.OUEST.getDirection()) {
            directionAFiltre = PointCardinal.EST.getDirection();
        } else if (direction == PointCardinal.EST.getDirection()) {
            obtenirPremierePlanche = 1;
            directionAFiltre = direction;
        } else {
            directionAFiltre = direction;
        }

        ArrayList<Planche> resultats = this.getPlanches().stream()
                .filter(planche -> planche.getDirection() == directionAFiltre)
                .collect(Collectors.toCollection(ArrayList::new));

        if (resultats.size() < obtenirPremierePlanche) {
            return null;
        }
        return resultats.get(obtenirPremierePlanche);
    }

    public Planche getFirstPlanche(Vecteur direction) {
        return this.getPlanches()
                .stream()
                .filter(planche -> planche.getVecteurDirection().equals(direction))
                .findFirst()
                .orElse(null);
    }

    public Planche getFirstPlanche(Predicate<? super Planche> predicate) {
        return this.getPlanches()
                .stream()
                .filter(predicate)
                .findFirst().orElse(null);
    }

    public Planche getAnyPlanche(Predicate<? super Planche> predicate) {
        return this.getPlanches()
                .stream()
                .filter(predicate)
                .findAny().orElse(null);
    }

    public List<Planche> getPlanches(Predicate<? super Planche> predicate) {
        return this.getPlanches()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    void ajouterPlanches(List<Planche> planches) {
        this.planches.addAll(planches);
    }

    void ajouterPlanche(Planche planche) {
        this.planches.add(planche);
    }

    void supprimerPlanche(Planche planche) {
        this.planches.remove(planche);
    }

    @Override
    public Bloc cloner() {
        return new Bloc(
                this.getPlanches().stream()
                        .map(Planche::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.getLongueur(),
                this.getHauteur(),
                this.getOrigine().cloner());
    }

    //#endregion
}