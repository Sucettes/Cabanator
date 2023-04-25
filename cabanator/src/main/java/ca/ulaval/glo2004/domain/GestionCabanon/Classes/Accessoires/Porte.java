package ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires;

import ca.ulaval.glo2004._3D.Plan;
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

public class Porte extends Accessoire {

    //#region ATTRIBUTS
    private final TypePlanche T_PLANCHES_CONTOUR = TypePlanche.P_2X4;
    private final Class<? extends Planche> T_PLANCHE_NORD_SUD = Montant.class;
    private final Sens SENS_PLANCHE_NORD_SUD = Sens.VOIR_EPAISSEUR;
    private TypePlanche typeLinteau;
    private double distanceEntreMontants;


    //#endregion

    //#region CONSTRUCTEURS

    /**
     * Constructeur pour DTO.
     */
    protected Porte(UUID id, Point centre, double longueurTrou, double hauteurTrou, TypePlanche typeLinteau, double distanceEntreMontants, Point origineParent) {
        super(id, centre, longueurTrou, hauteurTrou, origineParent);
        this.setTypeLinteau(typeLinteau);
        this.setDistanceEntreMontants(distanceEntreMontants);
    }

    /**
     * Constructeur pour une porte existante.
     */
    protected Porte(UUID id, Point centre, double longueurTrou, double hauteurTrou, double hauteurParent, TypePlanche typeLinteau, double distanceEntreMontants, Point origineParent, Bloc structureInterne) {
        super(id, centre, longueurTrou, hauteurTrou, hauteurParent, origineParent, structureInterne);
        this.setTypeLinteau(typeLinteau);
        this.setDistanceEntreMontants(distanceEntreMontants);
    }

    /**
     * Constructeur pour la création.
     */
    public Porte(Point centre, double longueurTrou, double hauteurTrou, double hauteurParent, Point origineParent) {
        super(centre, longueurTrou, hauteurTrou, hauteurParent, origineParent);
        this.setTypeLinteau(TypePlanche.P_2X6);
        this.setDistanceEntreMontants(longueurTrou);
        this.genererStructure(this.getLongueurTotale(), this.getHauteurParent());
    }

    //#endregion

    //#region PROPRIÉTÉS ET INDEXEURS

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
    protected double getHauteurTotale() {
        return this.hauteurTrou + this.getTypeLinteau().getLargeur();
    }

    @Override
    protected void setOrigine(Point positionCurseur) {
        double pX = positionCurseur.getX() + (this.getLongueurTotale() / 2) + TypePlanche.P_2X4.getEpaisseur();
        super.setOrigine(new Point(
                pX,
                this.getOrigineParent().getY(),
                this.getOrigineParent().getZ()));

        positionCurseur.setZ(this.getOrigineParent().getZ());
        positionCurseur.setY(this.getOrigineParent().getY() + (this.getHauteurTrou() / 2));
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
        Point pOrigineMontant = new Point(
                positionLinteau.getX(),
                positionLinteau.getY() + linteau.getLargeur(),
                positionLinteau.getZ()
        );
        // Affiche seulement les montants si la porte est valide.
        if (this.getPositionAccessoireEstValide()) {
            planchesContour.addAll(this.genererMontants(pOrigineMontant, linteau.getLongueur()));
        }

        return structurePorte;
    }

    private ArrayList<Planche> genererContourBloc() {
        boolean positionEstValide = this.getPositionAccessoireEstValide();
        Point pLinteau = new Point(
                this.getOrigine().getX(),
                this.getOrigine().getY() + this.getHauteurTrou(),
                this.getOrigine().getZ());
        Linteau linteau = new Linteau(
                pLinteau,
                this.getLongueurTrou() + (2 * this.T_PLANCHES_CONTOUR.getEpaisseur()),
                positionEstValide,
                this.getTypeLinteau());

        Point pLinteau2 = new Point(
                pLinteau.getX(),
                pLinteau.getY(),
                pLinteau.getZ() + this.T_PLANCHES_CONTOUR.getEpaisseur());
        Linteau linteau2 = new Linteau(
                pLinteau2,
                this.getLongueurTrou() + (2 * this.T_PLANCHES_CONTOUR.getEpaisseur()),
                positionEstValide,
                linteau.getType());

        // creation montant nord
        Point pMontantN = new Point(
                pLinteau.getX(),
                this.getOrigine().getY(),
                this.getOrigine().getZ());
        Planche montantNord = this.creerPlanche(
                this.T_PLANCHE_NORD_SUD,
                pMontantN,
                this.SENS_PLANCHE_NORD_SUD,
                new Vecteur(0, 1, 0),
                this.getHauteurTrou(),
                positionEstValide,
                TypePlanche.P_2X4);
        montantNord.debug_name = "montant Nord";

        Point pointMontantSud = new Point(
                pLinteau.getX() - linteau.getLongueur() + this.T_PLANCHES_CONTOUR.getEpaisseur(),
                pMontantN.getY(),
                this.getOrigine().getZ());
        Planche montantSud = this.creerPlanche(
                this.T_PLANCHE_NORD_SUD,
                pointMontantSud,
                this.SENS_PLANCHE_NORD_SUD,
                new Vecteur(0, 1, 0),
                this.getHauteurTrou(),
                positionEstValide,
                TypePlanche.P_2X4);
        montantSud.debug_name = "montant Sud";

        ArrayList<Planche> planches = new ArrayList<>();
        planches.add(linteau);
        planches.add(linteau2);
        planches.add(montantNord);
        planches.add(montantSud);

        return planches;
    }

    private ArrayList<Planche> genererMontants(Point pOrigine, double longueurStructure) {
        ArrayList<Planche> montants = new ArrayList<>();

        double maxY = this.getOrigineParent().getY() + this.getHauteurParent() - T_PLANCHES_CONTOUR.getEpaisseur();
        double pY = pOrigine.getY();

        double longueurMontant = maxY - pY;

        Point pMontantNord = new Point(
                pOrigine.getX(),
                pOrigine.getY(),
                pOrigine.getZ()
        );
        Planche montantNord = this.creerPlanche(
                T_PLANCHE_NORD_SUD,
                pMontantNord,
                this.SENS_PLANCHE_NORD_SUD,
                new Vecteur(0, 1, 0),
                longueurMontant,
                true,
                TypePlanche.P_2X4
        );

        Point pMontantSud = new Point(
                pMontantNord.getX() - longueurStructure + montantNord.getEpaisseur(),
                pMontantNord.getY(),
                pMontantNord.getZ()
        );
        Planche montantSud = this.creerPlanche(
                this.T_PLANCHE_NORD_SUD,
                pMontantSud,
                this.SENS_PLANCHE_NORD_SUD,
                new Vecteur(0, 1, 0),
                longueurMontant,
                true,
                TypePlanche.P_2X4
        );

        montants.add(montantNord);
        montants.add(montantSud);

        // Generer montants interieurs.
        double pXOrigine = pMontantNord.getX();
        double epaisseurMt = montantNord.getEpaisseur();

        double pXPrecedent = pXOrigine + epaisseurMt;
        double pXMax = (pXOrigine + epaisseurMt) - (longueurStructure - epaisseurMt * 2);
        double pXDernierM = (pXOrigine + epaisseurMt) - (longueurStructure - epaisseurMt);
        double espaceRestant = longueurStructure;
        boolean pXValide = true;

        do {
            double pXCourant = pXPrecedent;
            double pXSuivant = pXCourant - this.getDistanceEntreMontants();

            pXValide = pXSuivant >= pXMax;
            boolean ajouterMt = -pXDernierM + pXPrecedent > this.getDistanceEntreMontants()
                    && pXSuivant >= pXDernierM;

            if (pXValide || (ajouterMt && pXSuivant > pXDernierM)) {
                pXCourant = pXSuivant;
            }

            if (ajouterMt) {
                Point pMontant = new Point(
                        pXCourant - epaisseurMt,
                        pY,
                        pMontantNord.getZ());
                montants.add(this.creerPlanche(
                        this.T_PLANCHE_NORD_SUD,
                        pMontant,
                        this.SENS_PLANCHE_NORD_SUD,
                        new Vecteur(0, 1, 0),
                        longueurMontant,
                        pXValide,
                        TypePlanche.P_2X4
                ));

                pXPrecedent = pXCourant;
                espaceRestant -= this.getDistanceEntreMontants();
            }
        } while (espaceRestant > 0 && pXValide);

        return montants;
    }

    @Override
    public ArrayList<Planche> getPlanches() {
        return this.getStructureInterne().getPlanches();
    }

    @Override
    public Porte cloner() {
        return new Porte(
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