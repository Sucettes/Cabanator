package ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Bloc;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Linteau;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Montant;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;
import java.util.UUID;

public class Fenetre extends Accessoire {

    //#region ATTRIBUTS

    private final TypePlanche T_PLANCHES_CONTOUR = TypePlanche.P_2X4;
    private final Class<? extends Planche> T_PLANCHE_NORD_SUD_OUEST = Montant.class;
    private final Sens SENS_PLANCHE_NORD_SUD_OUEST = Sens.VOIR_EPAISSEUR;

    private double distanceEntreMontants;
    private TypePlanche typeLinteau;

    //#endregion

    //#region CONSTRUCTEURS

    /**
     * Constructeur pour DTO.
     */
    protected Fenetre(UUID id, Point centre, double longueurTrou, double hauteurTrou, TypePlanche typeLinteau, double distanceEntreMontants, Point origineParent) {
        super(id, centre, longueurTrou, hauteurTrou, origineParent);
        this.setTypeLinteau(typeLinteau);
        this.setDistanceEntreMontants(distanceEntreMontants);
    }

    /**
     * Constructeur pour une fenêtre existante.
     */
    Fenetre(UUID id, Point centre, double longueurTrou, double hauteurTrou, double hauteurParent, TypePlanche typeLinteau, double distanceEntreMontants, Point origineParent, Bloc structureInterne) {
        super(id, centre, longueurTrou, hauteurTrou, hauteurParent, origineParent, structureInterne);
        this.setTypeLinteau(typeLinteau);
        this.setDistanceEntreMontants(distanceEntreMontants);
    }

    /**
     * Constructeur pour la création.
     */
    public Fenetre(Point centre, double longueurTrou, double hauteurTrou, double hauteurParent, Point origineParent) {
        super(centre, longueurTrou, hauteurTrou, hauteurParent, origineParent);
        this.setTypeLinteau(TypePlanche.P_2X6);
        this.setDistanceEntreMontants(longueurTrou);
        this.genererStructure(this.getLongueurTotale(), this.getHauteurParent());
    }

    //#endregion

    //#region PROPRIÉTÉS ET INDEXEURS

    @Override
    public double getLongueurTotale() {
        return super.getLongueurTotale();
    }

    public double getDistanceEntreMontants() {
        return this.distanceEntreMontants;
    }

    void setDistanceEntreMontants(double distance) {
        this.distanceEntreMontants = distance;
    }

    public TypePlanche getTypeLinteau() {
        return this.typeLinteau;
    }

    private void setTypeLinteau(TypePlanche type) {
        this.typeLinteau = type;
    }

    @Override
    public double getHauteurTotale() {
        return this.hauteurTrou
                + this.getTypeLinteau().getLargeur()
                + this.structureInterne.getFirstPlanche(new Vecteur(1, 0, 0)).getEpaisseur();
    }

    @Override
    public void setOrigine(Point positionCurseur) {
        double pX = positionCurseur.getX() + (this.getLongueurTotale() / 2) + TypePlanche.P_2X4.getEpaisseur();
        double pY = positionCurseur.getY() - (this.getHauteurTrou() / 2) - TypePlanche.P_2X4.getEpaisseur();
        super.setOrigine(new Point(
                pX,
                pY,
                this.getOrigineParent().getZ()));
//        super.setOrigine(positionCurseur); //Temporaire, juste pour que ça fonctionne avec le panneau d'édition

        positionCurseur.setZ(this.getOrigineParent().getZ());
        this.setCentre(positionCurseur);
    }

    //#endregion

    //#region MÉTHODES

    @Override
    public void configurerPosition(Point positionCurseur) {
        this.setOrigine(positionCurseur);

    }

    public void configurerDistanceEntreMontants(double distance) {
        this.setDistanceEntreMontants(distance);
    }

    public void configurerTypeLinteau(TypePlanche type) {
        this.setTypeLinteau(type);
    }

    @Override
    protected Bloc genererBloc(double longueurTotale, double hauteurParent) {
        ArrayList<Planche> planchesContour = this.genererContourBloc();

        Bloc structurePorte = new Bloc(planchesContour, this.getLongueurTrou(), this.getHauteurTrou(), this.getOrigine());
        Planche linteau = structurePorte.getFirstPlanche((Planche p) -> p.getDirection() == PointCardinal.EST.getDirection()
                && p.getPosition().getZ() == this.getOrigine().getZ());
        Point positionLinteau = linteau.getPosition();
        Point pOrigineMontantAuDessus = new Point(
                positionLinteau.getX(),
                positionLinteau.getY() + linteau.getLargeur(),
                positionLinteau.getZ()
        );
        double pYMaxMontantEnDessous = structurePorte
                .getFirstPlanche(new Vecteur(1, 0, 0))
                .getPosition().getY();
        // Affiche seulement les montants si la porte est valide.
        if (this.getPositionAccessoireEstValide()) {
            planchesContour.addAll(genererMontants(pOrigineMontantAuDessus, pYMaxMontantEnDessous, linteau.getLongueur()));
        }

        return structurePorte;
    }

    private ArrayList<Planche> genererContourBloc() {
        boolean positionEstValide = this.getPositionAccessoireEstValide();
        // creation linteau dessus fenetre
        Point pointLinteau = new Point(
                this.getOrigine().getX(),
                this.getOrigine().getY() + this.getHauteurTrou() + this.T_PLANCHES_CONTOUR.getEpaisseur(),
                this.getOrigine().getZ());
        Linteau linteau = new Linteau(
                pointLinteau,
                this.getLongueurTrou() + (2 * this.T_PLANCHES_CONTOUR.getEpaisseur()),
                positionEstValide,
                this.getTypeLinteau());

        Point pointLinteau2 = new Point(
                pointLinteau.getX(),
                pointLinteau.getY(),
                pointLinteau.getZ() + this.T_PLANCHES_CONTOUR.getEpaisseur());
        Linteau linteau2 = new Linteau(
                pointLinteau2,
                this.getLongueurTrou() + (2 * this.T_PLANCHES_CONTOUR.getEpaisseur()),
                positionEstValide,
                linteau.getType());

        // creation planche bas de fenetre
        Point pointPlancheDessousFenetre = new Point(
                pointLinteau.getX(),
                this.getOrigine().getY(),
                this.getOrigine().getZ());
        Planche plancheDessousFenetre = this.creerPlanche(
                this.T_PLANCHE_NORD_SUD_OUEST,
                pointPlancheDessousFenetre,
                this.SENS_PLANCHE_NORD_SUD_OUEST,
                new Vecteur(1, 0, 0),
                this.getLongueurTrou() + (2 * T_PLANCHES_CONTOUR.getEpaisseur()),
                positionEstValide,
                TypePlanche.P_2X4);
        plancheDessousFenetre.debug_name = "planche dessous";

        // creation montant nord
        Point pointMontantNord = new Point(
                pointLinteau.getX(),
                pointPlancheDessousFenetre.getY() + T_PLANCHES_CONTOUR.getEpaisseur(),
                this.getOrigine().getZ());
        Planche montantNord = this.creerPlanche(
                this.T_PLANCHE_NORD_SUD_OUEST,
                pointMontantNord,
                this.SENS_PLANCHE_NORD_SUD_OUEST,
                new Vecteur(0, 1, 0),
                this.getHauteurTrou(),
                positionEstValide,
                TypePlanche.P_2X4);
        montantNord.debug_name = "montant Nord";

        // creation montant sud
        Point pointMontantSud = new Point(
                pointLinteau.getX() - linteau.getLongueur() + this.T_PLANCHES_CONTOUR.getEpaisseur(),
                pointMontantNord.getY(),
                this.getOrigine().getZ());
        Planche montantSud = this.creerPlanche(
                this.T_PLANCHE_NORD_SUD_OUEST,
                pointMontantSud,
                this.SENS_PLANCHE_NORD_SUD_OUEST,
                new Vecteur(0, 1, 0),
                this.getHauteurTrou(),
                positionEstValide,
                TypePlanche.P_2X4);

        // generation des montants
        ArrayList<Planche> planches = new ArrayList<>();
        planches.add(linteau);
        planches.add(linteau2);
        planches.add(montantNord);
        planches.add(montantSud);
        planches.add(plancheDessousFenetre);

        return planches;
    }


    private ArrayList<Planche> genererMontants(Point pOrigineMontantAuDessus, double pYMaxMontantEnDessous, double longueurStructure) {
        ArrayList<Planche> montants = new ArrayList<>();

        double maxY = this.getOrigineParent().getY() + this.getHauteurParent() - this.T_PLANCHES_CONTOUR.getEpaisseur();

        double pYMontantAuDessus = pOrigineMontantAuDessus.getY();
        double pYMontantEnDessous = this.getOrigineParent().getY() + this.T_PLANCHES_CONTOUR.getEpaisseur();

        double longueurMontantAuDessus = maxY - pYMontantAuDessus;
        double longueurMontantEnDessous = pYMaxMontantEnDessous - pYMontantEnDessous;

        // Ajout des montants nord
        montants.addAll(creerMontantsHautBas(
                pOrigineMontantAuDessus.getX(),
                pYMontantAuDessus,
                pYMontantEnDessous,
                pOrigineMontantAuDessus.getZ(),
                longueurMontantAuDessus,
                longueurMontantEnDessous,
                true
        ));

        // Ajout des montants sud
        montants.addAll(creerMontantsHautBas(
                pOrigineMontantAuDessus.getX() - longueurStructure + this.T_PLANCHES_CONTOUR.getEpaisseur(),
                pYMontantAuDessus,
                pYMontantEnDessous,
                pOrigineMontantAuDessus.getZ(),
                longueurMontantAuDessus,
                longueurMontantEnDessous,
                true)
        );

        // Generer montants interieurs.


        double epaisseurM = this.T_PLANCHES_CONTOUR.getEpaisseur();
        double pXOrigine = pOrigineMontantAuDessus.getX();

        double xPrecedent = pXOrigine + epaisseurM;
        double xMax = (pXOrigine + epaisseurM) - (longueurStructure - epaisseurM * 2);
        double xDernierM = (pXOrigine + epaisseurM) - (longueurStructure - epaisseurM);
        double espaceRestant = longueurStructure;
        boolean xValide = true;

        do {
            double xCourant = xPrecedent;
            double xSuivant = xCourant - this.getDistanceEntreMontants();

            xValide = xSuivant >= xMax;
            boolean ajouterMontant = -xDernierM + xPrecedent > this.getDistanceEntreMontants()
                    && xSuivant >= xDernierM;

            if (xValide || (ajouterMontant && xSuivant > xDernierM)) {
                xCourant = xSuivant;
            }

            if (ajouterMontant) {
                montants.addAll(creerMontantsHautBas(
                        xCourant - epaisseurM,
                        pYMontantAuDessus,
                        pYMontantEnDessous,
                        pOrigineMontantAuDessus.getZ(),
                        longueurMontantAuDessus,
                        longueurMontantEnDessous,
                        xValide)
                );

                xPrecedent = xCourant;
                espaceRestant -= this.getDistanceEntreMontants();
            }
        } while (espaceRestant > 0 && xValide);

        return montants;
    }

    private ArrayList<Planche> creerMontantsHautBas(
            double pX,
            double pYMontantAuDessus,
            double pYMontantEnDessous,
            double pZ,
            double longueurMontantAuDessus,
            double longueurMontantEnDessous,
            boolean pXValide) {
        ArrayList<Planche> montants = new ArrayList<>();

        if (longueurMontantAuDessus > 0) {
            montants.add(this.creerPlanche(
                    this.T_PLANCHE_NORD_SUD_OUEST,
                    new Point(pX, pYMontantAuDessus, pZ),
                    this.SENS_PLANCHE_NORD_SUD_OUEST,
                    new Vecteur(0, 1, 0),
                    longueurMontantAuDessus,
                    true,
                    TypePlanche.P_2X4
            ));
        }

        if (longueurMontantEnDessous > 0) {
            montants.add(this.creerPlanche(
                    this.T_PLANCHE_NORD_SUD_OUEST,
                    new Point(pX, pYMontantEnDessous, pZ),
                    this.SENS_PLANCHE_NORD_SUD_OUEST,
                    new Vecteur(0, 1, 0),
                    longueurMontantEnDessous,
                    pXValide,
                    TypePlanche.P_2X4
            ));
        }
        return montants;
    }

    @Override
    public ArrayList<Planche> getPlanches() {
        return this.getStructureInterne().getPlanches();
    }

    @Override
    public Fenetre cloner() {
        return new Fenetre(
                this.getId(),
                this.getCentre().cloner(),
                this.getLongueurTrou(),
                this.getHauteurTrou(),
                this.getHauteurParent(),
                this.getTypeLinteau(),
                this.getDistanceEntreMontants(),
                this.getOrigineParent().cloner(),
                this.getStructureInterne().cloner()
        );
    }

    //#endregion
}