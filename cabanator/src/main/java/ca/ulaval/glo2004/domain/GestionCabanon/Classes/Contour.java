package ca.ulaval.glo2004.domain.GestionCabanon.Classes;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Solive;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class Contour extends FormeDeBase {

    //#region ATTRIBUTS

    protected double longueur;
    protected double largeur;

    protected ArrayList<ArrayList<Bloc>> structureInterne;

    //#endregion

    //#region CONSTRUCTEURS

    /**
     * Constructeur pour DTO
     *
     * @param longueur Longueur du contour.
     * @param largeur  Largeur du contour.
     */
    protected Contour(double longueur, double largeur) {
        this.setLongueur(longueur);
        this.setLargeur(largeur);
    }

    /**
     * Constructeur pour un contour existant.
     */
    protected Contour(Point origine, double longueur, double largeur, ArrayList<ArrayList<Bloc>> structureInterne) {
        this(longueur, largeur);
        this.setOrigine(origine);
        this.structureInterne = structureInterne;
    }

    /**
     * Constructeur pour la création d'un contour.
     */
    protected Contour(Point origine, double longueur, double largeur) {
        this(longueur, largeur);
        this.setOrigine(origine);
        this.structureInterne = new ArrayList<>();
    }

    //#endregion

    //#region PROPRIÉTÉS ET INDEXEURS

    public double getLongueur() {
        return this.longueur;
    }

    private void setLongueur(double longueur) {
        this.longueur = longueur;
    }

    public double getLargeur() {
        return this.largeur;
    }

    private void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    public ArrayList<ArrayList<Bloc>> getStructureInterne() {
        return this.structureInterne;
    }

    //#endregion

    //#region MÉTHODES

    public void configurerLongueur(double longueur) {
        this.setLongueur(longueur);
        this.regenererStructure();
    }

    public void configurerLargeur(double largeur) {
        this.setLargeur(largeur);
        this.regenererStructure();
    }

    //#region MÉTHODES STRUCTURE

    protected void regenererStructure() {
        this.genererStructure(this.getLongueur(), this.getLargeur());
        this.regenererAccessoiresEtComposants();
    }

    protected abstract void genererStructure(double longueur, double largeur);

    protected abstract ArrayList<Bloc> genererLigneBlocs(double positionX, double positionY, double positionZ, double longueur, double largeurRestante);

    double obtenirLargeurTotaleBloc(Bloc bloc) {
        Planche plancheOuest = bloc.getFirstPlanche(PointCardinal.OUEST.getDirection());

        double largeurPlancheOuest = plancheOuest.getEpaisseur();

        double largeurPlancheEst = bloc
                .getFirstPlanche(PointCardinal.EST.getDirection())
                .getEpaisseur();

        double longueurPlancheNord = bloc
                .getFirstPlanche(PointCardinal.NORD.getDirection())
                .getLongueur();

        return largeurPlancheOuest + largeurPlancheEst + longueurPlancheNord;
    }

    //#endregion

    protected abstract void regenererAccessoiresEtComposants();

    @Override
    public ArrayList<Planche> getPlanches() {
        ArrayList<Planche> planches = new ArrayList<>();

        this.getStructureInterne().forEach(ligneBlocs ->
                ligneBlocs.forEach(bloc -> planches.addAll(bloc.getPlanches())));

        return planches;
    }

    //#region MÉTHODES UTILITAIRES

    /**
     * Permet de trouver l'index de la ligne de bloc ou la position z ou y du curseur.
     *
     * @param positionCurseur Position du curseur.
     * @param axeEstY         Soit z pour le plancher et y pour le murs : y = true, z = false
     * @return Retourne index ou -1 si pas trouve.
     */
    protected int trouverIndexLigneBloc(Point positionCurseur, boolean axeEstY) {
        ArrayList<ArrayList<Bloc>> structureInterne = this.getStructureInterne();

        if (positionCurseur == null) {
            throw new InvalidParameterException("positionCurseur est null!");
        }
        if (structureInterne.isEmpty()) {
            throw new InvalidParameterException("structureInterne est vide!");
        }
        int ligneIndex = 0;
        boolean positionTrouver = false;
        while (ligneIndex < structureInterne.size() && !positionTrouver) {
            Bloc bloc = structureInterne.get(ligneIndex).get(0);
            ArrayList<Planche> solive = bloc.getPlanches()
                    .stream().filter(Solive.class::isInstance)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (solive.size() >= 2) {
                Planche soliveEst;
                Planche soliveOuest;
                double pEnLargeur;
                double maxBlocEnLargeur;
                double minBlocEnLargeur;
                if (axeEstY) {
                    // position y -> pour le mur
                    Vecteur direction = new Vecteur(1, 0, 0);
                    soliveOuest = bloc.getFirstPlanche(direction);
                    soliveEst = bloc.getFirstPlanche(planche -> planche.getVecteurDirection().equals(direction) && planche != soliveOuest);
                    maxBlocEnLargeur = soliveEst.getPosition().getY();
                    minBlocEnLargeur = soliveOuest.getPosition().getY() + soliveOuest.getEpaisseur();
                    pEnLargeur = positionCurseur.getY();
                } else {
                    // position z -> pour le plancher
                    soliveEst = bloc.getFirstPlanche(PointCardinal.EST.getDirection());
                    soliveOuest = bloc.getFirstPlanche(PointCardinal.OUEST.getDirection());
                    maxBlocEnLargeur = soliveEst.getPosition().getZ();
                    minBlocEnLargeur = soliveOuest.getPosition().getZ() + soliveOuest.getEpaisseur();
                    pEnLargeur = positionCurseur.getZ();
                }

                if (pEnLargeur <= maxBlocEnLargeur && pEnLargeur >= minBlocEnLargeur) {
                    positionTrouver = true;
                } else {
                    ligneIndex++;
                }
            }
        }
        return positionTrouver ? ligneIndex : -1;
    }

    protected <T extends Planche> T creerPlanche(Class<T> cPlanche, Point centre, Sens sens, double direction, double longueur) {
        try {
            Constructor<T> constructor = cPlanche.getConstructor(Point.class, Sens.class, double.class, double.class);
            return constructor.newInstance(centre, sens, direction, longueur);
        } catch (NoSuchMethodException
                 | InvocationTargetException
                 | InstantiationException
                 | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends Planche> T creerPlanche(Class<T> cPlanche, Point centre, Sens sens, double direction, double longueur, TypePlanche tPlanche) {
        try {
            Constructor<T> constructor = cPlanche.getConstructor(Point.class, Sens.class, double.class, double.class, TypePlanche.class);
            return constructor.newInstance(centre, sens, direction, longueur, tPlanche);
        } catch (NoSuchMethodException
                 | InvocationTargetException
                 | InstantiationException
                 | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /* On utilise un Vecteur pour la direction, car impossible de donner une orientation en 3D avec un simple angle
         (on ne peut pas savoir si on veut une planche orientée vers le haut ou vers le bas)
     Tuto:
          Un vecteur peut être construit à partir d'un Point3D. Le point correspond à un point dans la direction du vecteur.

     Exemples (Les coordonnées données sont des exemple, et les nombre ne sont pas obligés d'être 1 ou -1) :
            Vertical: new Point3D(0, Y, 0) -> Y = 1/-1  (1 = vers le haut, -1 = vers le bas)
            Horizontal: new Point3D(X, 0, 0) -> X = 1/-1  (1 = vers la gauche, -1 = vers la droite) Les X sont inversés
            Diagonale: new Point3D(X, Y, 0) -> X = 1/-1  (1 = vers la gauche, -1 = vers la droite)
                         Y = 1/-1  (1 = vers le haut, -1 = vers le bas)
            N : new Point3D(0, 0, Z) -> Z = 1
            S : new Point3D(0, 0, Z) -> Z = -1
            E : new Point3D(X, 0, 0) -> X = -1   Les X sont inversés
            O : new Point3D(X, 0, 0) -> X = 1  Les X sont inversés
          */
    protected <T extends Planche> T creerPlanche(Class<T> tPlanche, Point centre, Sens sens, Vecteur direction, double longueur) {
        try {
            Constructor<T> constructor = tPlanche.getConstructor(Point.class, Sens.class, Vecteur.class, double.class);
            return constructor.newInstance(centre, sens, direction, longueur);
        } catch (NoSuchMethodException
                 | InvocationTargetException
                 | InstantiationException
                 | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ArrayList<Bloc>> obtenirCloneStructureInterne() {
        ArrayList<ArrayList<Bloc>> structureInterneClone = new ArrayList<>();
        int nbLignesBlocs = this.getStructureInterne().size();
        int i = 0;

        while (i < nbLignesBlocs) {
            structureInterneClone.add(this.obtenirBlocs(this.getStructureInterne().get(i)));
            i++;
        }

        return structureInterneClone;
    }

    private ArrayList<Bloc> obtenirBlocs(ArrayList<Bloc> blocs) {
        ArrayList<Bloc> ligneBlocsClone = new ArrayList<>();
        blocs.forEach(bloc -> ligneBlocsClone.add(bloc.cloner()));

        return ligneBlocsClone;
    }

    //#endregion

    //#endregion
}