package ca.ulaval.glo2004.domain.GestionCabanon.Classes;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Entremise;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Interfaces.IClonable;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class LigneEntremises extends FormeDeBase implements IClonable<LigneEntremises> {

    //#region ATTRIBUTS

    private UUID id;
    private double decalageEntreEntremises;
    private ArrayList<Entremise> entremises;
    private Point centre;

    //#endregion

    //#region CONSTRUCTEURS

    /**
     * Constructeur pour DTO
     *
     * @param id
     * @param centre
     * @param decalageEntreEntremises
     */
    public LigneEntremises(UUID id, Point centre, double decalageEntreEntremises) {
        if (decalageEntreEntremises < 0) {
            throw new InvalidParameterException("decalage entre entremises est negatif!");
        }
        if (centre == null) {
            throw new InvalidParameterException("centre est null");
        }
        this.id = id;
        this.setCentre(centre);
        this.setDecalageEntreEntremises(decalageEntreEntremises);
    }

    /**
     * Constructeur pour une ligne d'entremises existante
     *
     * @param id
     * @param centre
     * @param decalageEntreEntremises
     * @param entremises
     */
    LigneEntremises(UUID id, Point centre, double decalageEntreEntremises, ArrayList<Entremise> entremises) {
        if (decalageEntreEntremises < 0) {
            throw new InvalidParameterException("decalage entre entremises est negatif!");
        }
        if (centre == null) {
            throw new InvalidParameterException("centre est null");
        }
        this.id = id;
        this.setCentre(centre);
        this.setDecalageEntreEntremises(decalageEntreEntremises);
        this.setEntremises(entremises);
    }

    /**
     * Constructeur pour la création
     *
     * @param centre
     * @param decalageEntreEntremises
     */
    public LigneEntremises(Point centre, double decalageEntreEntremises) {
        if (decalageEntreEntremises < 0) {
            throw new InvalidParameterException("decalage entre entremises est negatif!");
        }
        if (centre == null) {
            throw new InvalidParameterException("centre est null");
        }
        this.id = UUID.randomUUID();
        this.setCentre(centre);
        this.setDecalageEntreEntremises(decalageEntreEntremises);
        this.entremises = new ArrayList<>();
    }

    public LigneEntremises(Point centre, double decalageEntreEntremises, UUID id) {
        if (decalageEntreEntremises < 0) {
            throw new InvalidParameterException("decalage entre entremises est negatif!");
        }
        if (centre == null) {
            throw new InvalidParameterException("centre est null");
        }
        this.id = id;
        this.setCentre(centre);
        this.setDecalageEntreEntremises(decalageEntreEntremises);
        this.entremises = new ArrayList<>();
    }

    //#endregion

    //#region PROPRIÉTÉS ET INDEXEURS

    public UUID getId() {
        return this.id;
    }

    public double getDecalageEntreEntremises() {
        return this.decalageEntreEntremises;
    }

    private void setDecalageEntreEntremises(double decalage) {
        if (decalage < 0) {
            throw new InvalidParameterException("decalage entre entremises est negatif!");
        }
        this.decalageEntreEntremises = decalage;
    }

    public ArrayList<Entremise> getEntremises() {
        return this.entremises;
    }

    private void setEntremises(ArrayList<Entremise> entremises) {
        this.entremises = entremises;
    }

    public Point getCentre() {
        return this.centre;
    }

    private void setCentre(Point positionCurseur) {
        this.centre = positionCurseur;
    }

    //#endregion

    //#region MÉTHODES

    public void configurerDistanceEntreEntremises(double distance) {
        if (distance < 0) {
            throw new InvalidParameterException("La distance est negative");
        }
        this.setDecalageEntreEntremises(distance);
        this.configurerPosition(this.getCentre());
    }

    public void ajouterEntremise(Entremise entremise) {
        if (entremise == null) {
            throw new InvalidParameterException("Entremise est null!");
        }
        this.entremises.add(entremise);
    }

    /**
     * Méthode utilisée pour positionner une ligne d'entremise une fois qu'elle est créée.
     *
     * @param positionCurseur
     */
    public void configurerPosition(Point positionCurseur) {
        if (positionCurseur == null) {
            throw new InvalidParameterException("position du curseur est null!");
        }
        this.centre.setZ(positionCurseur.getZ());
        double positionDecalage = (getDecalageEntreEntremises() / 2);
        for (Entremise entremise : this.entremises) {
            double positionZ = positionCurseur.getZ() + (positionDecalage - (entremise.getEpaisseur() / 2));
            entremise.getPosition().setZ(positionZ);
            positionDecalage *= -1;
        }
    }

    public void reconfigurerPosition(double ratioPZ) {
        this.configurerPosition(new Point(
                this.getCentre().getX(),
                this.getCentre().getY(),
                this.getCentre().getZ() * ratioPZ
        ));
    }

    public void genererEntremisesPlancher(ArrayList<Bloc> blocLigne, double epaisseur, Sens sens, double direction) {
        if (blocLigne.isEmpty()) {
            throw new InvalidParameterException("bloc ligne est vide!");
        }
        if (epaisseur < 0) {
            throw new InvalidParameterException("Epaisseur est negative!");
        }
        if (sens == null) {
            throw new InvalidParameterException("Sens est null!");
        }

        for (Bloc blocPlancher : blocLigne) {
            ArrayList<Planche> planchesEstOuest =
                    blocPlancher.getPlanches().stream()
                            .filter(p -> p.getDirection() == 0)
                            .sorted((p1, p2) -> p1.getPosition().getX() <= p2.getPosition().getX() ? 1 : -1)
                            .collect(Collectors.toCollection(ArrayList::new));

            double soliveEpaisseur = planchesEstOuest.get(0).getEpaisseur();
            Point positionEntremise = this.getCentre().cloner();
            positionEntremise.setX(planchesEstOuest.get(0).getPosition().getX());

            double positionDecalage = (this.decalageEntreEntremises / 2);

            int nbEntremises = planchesEstOuest.size() - 1;
            int j = 0;
            while (j < nbEntremises) {
                int indexSoliveGauche = j;
                int indexSoliveDroite = j + 1;

                if (indexSoliveDroite <= planchesEstOuest.size()) {
                    double longueurEntremise = planchesEstOuest.get(indexSoliveGauche).getPosition().getX()
                            - planchesEstOuest.get(indexSoliveDroite).getPosition().getX() - soliveEpaisseur;

                    Point pointEntremise = positionEntremise.cloner();
                    pointEntremise.setZ(positionEntremise.getZ() + (positionDecalage - (soliveEpaisseur / 2)));
                    Entremise entremise = new Entremise(pointEntremise, sens, direction, longueurEntremise, TypePlanche.P_2X6);
                    this.ajouterEntremise(entremise);

                    positionEntremise.setX(positionEntremise.getX() - longueurEntremise - soliveEpaisseur);
                }
                positionDecalage *= -1;
                j++;
            }
        }
    }

    @Override
    public ArrayList<Planche> getPlanches() {
        return new ArrayList<>(this.getEntremises());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public LigneEntremises cloner() {
        return new LigneEntremises(
                this.getId(),
                this.getCentre().cloner(),
                this.decalageEntreEntremises,
                this.getEntremises().stream()
                        .map(Entremise::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)));
    }

    //#endregion
}