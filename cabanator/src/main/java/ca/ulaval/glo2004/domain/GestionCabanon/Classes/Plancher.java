package ca.ulaval.glo2004.domain.GestionCabanon.Classes;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Solive;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Interfaces.IClonable;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class Plancher extends Contour implements IClonable<Plancher> {
    private final UUID id = UUID.randomUUID();
    private final Class<? extends Planche> CLASSE_PLANCHES_CONTOUR = Solive.class;
    private final Sens SENS_PLANCHES_CONTOUR = Sens.VOIR_LARGEUR;

    private final TypePlanche T_PLANCHES_CONTOUR = TypePlanche.P_2X6;

    //#region ATTRIBUTS
    private double distanceEntreSolives;
    private ArrayList<LigneEntremises> ligneEntremises;
    private Point centre;

    //#endregion

    //#region CONSTRUCTEURS

    /**
     * Constructeur pour DTO
     *
     * @param longueur             Longueur du plancher
     * @param largeur              Largeur du plancher
     * @param distanceEntreSolives Distance entre les solives du plancher
     * @param lignesEntremises
     */
    protected Plancher(double longueur, double largeur, double distanceEntreSolives, ArrayList<LigneEntremises> lignesEntremises) {
        super(longueur, largeur);
        this.setDistanceEntreSolives(distanceEntreSolives);
        this.setLigneEntremises(lignesEntremises);
    }

    /**
     * Constructeur pour la modification
     */
    public Plancher(Point point, double longueur, double largeur, double distanceEntreSolives,
                    ArrayList<LigneEntremises> lignesEntremises,
                    ArrayList<ArrayList<Bloc>> structureInterne) {
        super(point, longueur, largeur, structureInterne);
        this.setDistanceEntreSolives(distanceEntreSolives);
        this.setLigneEntremises(lignesEntremises);
    }

    /**
     * Constructeur pour la création
     *
     * @param longueur
     * @param largeur
     */
    public Plancher(double longueur, double largeur) {
        super(new Point(0, 0, 0), longueur, largeur);

        this.ligneEntremises = new ArrayList<>();

        this.configurerDistanceEntreSolives(30);
    }

    /**
     * Constructeur pour les tests
     *
     * @param longueur
     * @param largeur
     */
    public Plancher(double longueur, double largeur, double distanceEntreSolives) {
        super(new Point(longueur / 2, 0, largeur / 2), longueur, largeur);

        this.ligneEntremises = new ArrayList<>();

        this.configurerDistanceEntreSolives(distanceEntreSolives);
    }

    //#endregion

    //#region PROPRIÉTÉS ET INDEXEURS
    public UUID getId() {
        return this.id;
    }

    public double getDistanceEntreSolives() {
        return this.distanceEntreSolives;
    }

    private void setDistanceEntreSolives(double distance) {
        if (distance < 0) {
            throw new InvalidParameterException("distance est negative!");
        }
        this.distanceEntreSolives = distance;
    }

    public double getEpaisseurPlancher() {
        return this.getStructureInterne()
                .get(0).get(0)
                .getFirstPlanche(PointCardinal.OUEST.getDirection())
                .getLargeur();
    }

    public ArrayList<LigneEntremises> getLigneEntremises() {
        return this.ligneEntremises;
    }

    private void setLigneEntremises(ArrayList<LigneEntremises> lignesEntremises) {
        this.ligneEntremises = lignesEntremises;
    }

    private Point getCentre() {
        return this.centre;
    }

    private Point getNouveauCentre() {
        Point origine = this.getOrigine();
        this.centre = new Point(
                origine.getX() - (this.getLongueur() / 2),
                origine.getY(),
                origine.getZ() + (this.getLargeur() / 2));
        return this.centre;
    }
    //#endregion

    //#region MÉTHODES

    public void configurerDistanceEntreSolives(double distance) {
        if (distance < 0) {
            throw new InvalidParameterException("distance est negative!");
        }
        this.setDistanceEntreSolives(distance);

        this.regenererStructure();
    }

    public void regenererStructurePlancher() {
        this.regenererStructure();
    }

    //#region Lignes d'entremises

    /**
     * Permet de configurer le décalage à la verticale entre chaque entremise d'une ligne d'entremises.
     *
     * @param id       Id de la ligne d'entremise à configurer le décalage
     * @param decalage Décalage (en pouces) à configurer pour la ligne d'entremises
     */
    public void configurerDecalageEntreEntremises(UUID id, double decalage) {
        if (id == null) {
            throw new InvalidParameterException("id est null!");
        }
        if (decalage < 0) {
            throw new InvalidParameterException("decalage est negative!");
        }
        this.obtenirLigneEntremises(id).configurerDistanceEntreEntremises(decalage);
    }

    public LigneEntremises obtenirLigneEntremises(UUID id) {
        if (id == null) {
            throw new InvalidParameterException("id est null!");
        }
        Optional<LigneEntremises> ligneEntremises = this.ligneEntremises.stream()
                .filter(le -> le.getId() == id)
                .findFirst();

        if (ligneEntremises.isEmpty()) {
            throw new InvalidParameterException("La ligne d'entremises est introuvable pour l'id : '" + id + "'");
        }

        return ligneEntremises.get();
    }

    /**
     * Creation d'un object : ligne entremise et leur entremises.
     *
     * @param decalageEntreEntremises Decalage des entremises de la ligne.
     * @param position                Position de la ligne.
     * @return Une ligne d'entremise
     */
    private LigneEntremises genererLigneEntremises(UUID id, double decalageEntreEntremises, Point position) {
        if (decalageEntreEntremises < 0) {
            throw new InvalidParameterException("decalage entre entremises est negative!");
        }
        if (position == null) {
            throw new InvalidParameterException("position est null!");
        }
        int ligneIndex = trouverIndexLigneBloc(position, false);
        if (ligneIndex == -1) {
            return null;
        }

        ArrayList<ArrayList<Bloc>> structureInterne = this.getStructureInterne();
        Planche soliveEst = structureInterne.get(ligneIndex).get(0)
                .getFirstPlanche(PointCardinal.EST.getDirection());
        Planche soliveOuest = structureInterne.get(ligneIndex).get(0)
                .getFirstPlanche(PointCardinal.OUEST.getDirection());

        double epaisseur = soliveOuest.getLargeur();
        double demiEpaisseurLignes = ((decalageEntreEntremises / 2) + (epaisseur / 2));

        double maxZBloc = soliveEst.getPosition().getZ();
        double minZBloc = soliveOuest.getPosition().getZ() + epaisseur;
        boolean hauteurMaxValide = (position.getZ() + demiEpaisseurLignes) <= (maxZBloc - epaisseur);
        boolean hauteurMinValide = (position.getZ() - demiEpaisseurLignes) >= (minZBloc + epaisseur);


        LigneEntremises ligneEntremise;
        if (id == null) {
            ligneEntremise = new LigneEntremises(position.cloner(), decalageEntreEntremises);
        } else {
            ligneEntremise = new LigneEntremises(position.cloner(), decalageEntreEntremises, id);
        }
        ligneEntremise.genererEntremisesPlancher(
                structureInterne.get(ligneIndex),
                epaisseur,
                soliveOuest.getSens(),
                soliveOuest.getDirection()
        );
        return ligneEntremise;
    }

    /**
     * Regenerer les lignes d'entremises et leur entremise.
     */
    @Override
    protected void regenererAccessoiresEtComposants() {
        Point centre = this.getCentre();
        Point nouveauCentre = this.getNouveauCentre();
        boolean centreMurDiffere = centre != null && !centre.equals(nouveauCentre);
        double ratioPZ = 1;

        if (centreMurDiffere) {
            double pZOrigine = this.getOrigine().getZ();

            double ancienPZ = centre.getZ() - pZOrigine;
            double nouveauPZ = nouveauCentre.getZ() - pZOrigine;
            ratioPZ = nouveauPZ / ancienPZ;
        }

        for (int i = 0; i < this.getLigneEntremises().size(); i++) {
            LigneEntremises le = this.ligneEntremises.get(i);
            if (centreMurDiffere) {
                le.reconfigurerPosition(ratioPZ);
            }

            this.ligneEntremises.set(i, this.genererLigneEntremises(
                    le.getId(),
                    le.getDecalageEntreEntremises(),
                    le.getCentre().cloner()
            ));
        }
    }

    /**
     * Permet d'ajouter une ligne d'entremises en fonction de la position du curseur et
     * du decalage.
     *
     * @param positionCurseur         Position du curseur (le Y est le plus important)
     * @param decalageEntreEntremises Decalage entre les entremises en pouces.
     */
    public void ajouterLigneEntremises(Point positionCurseur, double decalageEntreEntremises) {
        if (positionCurseur == null) {
            throw new InvalidParameterException("position curseur est null!");
        }
        if (decalageEntreEntremises < 0) {
            throw new InvalidParameterException("decalage entre entremises est negative!");
        }

        // todo : temporaire pour metre valeur par defaut a changer.
        if (positionCurseur.getX() == 0 && positionCurseur.getY() == 0 && positionCurseur.getZ() == 0) {
            positionCurseur.setZ(this.getLargeur() < TypePlanche.getLimitePlanche(this.T_PLANCHES_CONTOUR).getDistanceDouble() ? this.getLargeur() / 3 : 50);
            if (decalageEntreEntremises == 0)
                decalageEntreEntremises = 2;
        }

        LigneEntremises l = genererLigneEntremises(null, decalageEntreEntremises, positionCurseur);
        if (l != null) {
            this.ligneEntremises.add(l);
        }
    }

    public void supprimerLigneEntremises(UUID id) {
        if (id == null) {
            throw new InvalidParameterException("id est null!");
        }
        this.ligneEntremises.remove(this.obtenirLigneEntremises(id));
        this.regenererStructure();
    }

    public void positionnerLigneEntremises(UUID id, Point positionCurseur) {
        if (id == null) {
            throw new InvalidParameterException("id est null!");
        }
        if (positionCurseur == null) {
            throw new InvalidParameterException("position curseur est null!");
        }
        this.obtenirLigneEntremises(id).configurerPosition(positionCurseur);
        this.regenererStructure();
    }

    //#endregion

    @Override
    protected ArrayList<Bloc> genererLigneBlocs(double positionX, double positionY, double positionZ, double longueur, double largeurRestante) {
        if (longueur < 0) {
            throw new InvalidParameterException("longueur est negative!");
        }
        ArrayList<Bloc> ligneBlocs = new ArrayList<>();

        double positionXCourante = positionX;

        while (longueur > 0) {
            double positionZCourante = positionZ;

            ArrayList<Planche> blocPlanches = new ArrayList<>();

            double longueurBlocCourant = TypePlanche.getLimitePlanche(this.T_PLANCHES_CONTOUR).getDistanceDouble();

            if ((longueur - TypePlanche.getLimitePlanche(this.T_PLANCHES_CONTOUR).getDistanceDouble()) < 0) {
                longueurBlocCourant = longueur;
            }

            // Obtention de la largeur intérieur du bloc
            double largeurTotaleBloc = TypePlanche.getLimitePlanche(this.T_PLANCHES_CONTOUR).getDistanceDouble() + (T_PLANCHES_CONTOUR.getEpaisseur() * 2);
            if ((largeurRestante - largeurTotaleBloc < 0)) {
                largeurTotaleBloc = largeurRestante;
            }

            double largeurInterieurBloc = largeurTotaleBloc - (T_PLANCHES_CONTOUR.getEpaisseur() * 2);

            // Création de la planche à la planche sud
            Point positionPlancheN = new Point(
                    positionXCourante - T_PLANCHES_CONTOUR.getEpaisseur(),
                    positionY,
                    positionZCourante + T_PLANCHES_CONTOUR.getEpaisseur());
            Planche plancheN = this.creerPlanche(
                    this.CLASSE_PLANCHES_CONTOUR,
                    positionPlancheN,
                    this.SENS_PLANCHES_CONTOUR,
                    PointCardinal.NORD.getDirection(),
                    largeurInterieurBloc,
                    T_PLANCHES_CONTOUR);
            plancheN.debug_name = "planche nord";

            positionZCourante += (plancheN.getLongueur() + plancheN.getEpaisseur());

            Planche plancheE = this.creerPlanche(
                    this.CLASSE_PLANCHES_CONTOUR,
                    new Point(positionXCourante, positionY, positionZCourante),
                    this.SENS_PLANCHES_CONTOUR,
                    PointCardinal.EST.getDirection(),
                    longueurBlocCourant,
                    T_PLANCHES_CONTOUR
            );
            plancheE.debug_name = "Planche est";

            // Création de la planche à l'est
            positionXCourante -= plancheE.getLongueur();

            Point positionPlancheS = new Point(
                    positionXCourante,
                    positionY,
                    positionPlancheN.getZ()
            );
            Planche plancheS = this.creerPlanche(
                    this.CLASSE_PLANCHES_CONTOUR,
                    positionPlancheS,
                    this.SENS_PLANCHES_CONTOUR,
                    PointCardinal.NORD.getDirection(),
                    largeurInterieurBloc,
                    TypePlanche.P_2X6);
            plancheS.debug_name = "Planche sud";

//             Création de la planche au sud
            positionXCourante += plancheE.getLongueur();
            Point pointPlancheO = new Point(
                    positionXCourante,
                    plancheE.getPosition().getY(),
                    plancheN.getPosition().getZ() - T_PLANCHES_CONTOUR.getEpaisseur());

            Planche plancheO = this.creerPlanche(
                    this.CLASSE_PLANCHES_CONTOUR,
                    pointPlancheO,
                    this.SENS_PLANCHES_CONTOUR,
                    PointCardinal.EST.getDirection(),
                    longueurBlocCourant,
                    TypePlanche.P_2X6);
            plancheO.debug_name = "Planche ouest";

            positionXCourante -= longueurBlocCourant;

            blocPlanches.add(plancheN);
            blocPlanches.add(plancheS);
            blocPlanches.add(plancheO);
            blocPlanches.add(plancheE);

            blocPlanches.addAll(genererInterieurBloc(plancheN, longueurBlocCourant));

            ligneBlocs.add(new Bloc(blocPlanches, longueurBlocCourant, largeurTotaleBloc, plancheN.getPosition()));

            longueur -= longueurBlocCourant;
        }

        return ligneBlocs;
    }

    protected ArrayList<Planche> genererInterieurBloc(Planche soliveOriginelle, double longueur) {
        if (soliveOriginelle == null) {
            throw new InvalidParameterException("soliveOriginelle est null!");
        }
        if (longueur < 0) {
            throw new InvalidParameterException("longueur est negative!");
        }
        ArrayList<Planche> interieurBloc = new ArrayList<>();

//        double distanceEntreSolives = 60; // Juste pour le moment, si on veut débugger

        Point positionSoliveOriginelle = soliveOriginelle.getPosition();
        double epaisseurSolive = soliveOriginelle.getEpaisseur();

        double positionXPrecedente = positionSoliveOriginelle.getX() + epaisseurSolive;
        double positionXMax = (positionSoliveOriginelle.getX() + epaisseurSolive) - (longueur - epaisseurSolive * 2);
        double positionXDerniereSolive = (positionSoliveOriginelle.getX() + epaisseurSolive) - (longueur - epaisseurSolive);
        double longueurRestante = longueur;
        boolean positionXValide;

        do {
            double positionXCourante = positionXPrecedente;
            double positionXSuivante = positionXCourante - this.getDistanceEntreSolives();

            positionXValide = positionXSuivante >= positionXMax;
            boolean ajouterPlanche = -positionXDerniereSolive + positionXPrecedente > this.getDistanceEntreSolives()
                    && positionXSuivante >= positionXDerniereSolive;

            if (positionXValide || (ajouterPlanche && positionXSuivante > positionXDerniereSolive)) {
                positionXCourante = positionXSuivante;
            }

            if (ajouterPlanche) {
                Solive s = new Solive(
                        new Point(positionXCourante - epaisseurSolive, positionSoliveOriginelle.getY(), positionSoliveOriginelle.getZ()),
                        soliveOriginelle.getSens(),
                        soliveOriginelle.getDirection(),
                        soliveOriginelle.getLongueur(),
                        positionXValide,
                        T_PLANCHES_CONTOUR
                );
                s.debug_name = "Solive interieur";
                interieurBloc.add(s);

                positionXPrecedente = positionXCourante;
                longueurRestante -= this.getDistanceEntreSolives();
            }
        } while (longueurRestante > 0 && positionXValide);

        return interieurBloc;
    }

    @Override
    protected void genererStructure(double longueur, double largeur) {
        if (longueur < 0) {
            throw new InvalidParameterException("longueur est negative!");
        }
        if (largeur < 0) {
            throw new InvalidParameterException("largeur est negative!");
        }
        this.structureInterne = new ArrayList<>();

        Point pointOrigine = this.getOrigine();

        double positionXCourante = pointOrigine.getX();
        double positionY = pointOrigine.getY();
        double positionZCourante = pointOrigine.getZ();

        double largeurRestante = largeur;

        while (largeurRestante > 0) {
            ArrayList<Bloc> ligneBlocs = this.genererLigneBlocs(
                    positionXCourante,
                    positionY,
                    positionZCourante,
                    longueur,
                    largeurRestante);

            this.structureInterne.add(ligneBlocs);

            double largeurBlocCourant = this.obtenirLargeurTotaleBloc(ligneBlocs.get(0));

            largeurRestante -= largeurBlocCourant;
            positionZCourante += largeurBlocCourant;
        }
    }

    @Override
    public Plancher cloner() {
        Plancher clone = new Plancher(
                this.getOrigine().cloner(),
                this.getLongueur(),
                this.getLargeur(),
                this.getDistanceEntreSolives(),
                this.getLigneEntremises().stream()
                        .map(LigneEntremises::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.obtenirCloneStructureInterne());

        return clone;
    }

    //#endregion
}