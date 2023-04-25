package ca.ulaval.glo2004.domain.GestionCabanon.Classes;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Chevron;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Entrait;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Poutre;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Interfaces.IClonable;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.AngleDeCoupe;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class Ferme extends FormeDeBase implements IClonable<Ferme> {
    protected final TypePlanche T_PLANCHES_POUTRE = TypePlanche.P_2X4;
    private final TypePlanche T_PLANCHES_ENTRAIT = TypePlanche.P_2X4;
    private final TypePlanche T_PLANCHES_CHEVRON = TypePlanche.P_2X4;
    private final Sens SENS_PLANCHES_ENTRAIT = Sens.VOIR_LARGEUR;
    private final Sens SENS_PLANCHES_CHEVRON = Sens.VOIR_LARGEUR;
    private final Sens SENS_PLANCHES_POUTRE = Sens.VOIR_EPAISSEUR;


    private UUID id;
    /**
     * entrait est aussi appelé base (le bas de la ferme).
     */
    private Entrait entrait;
    /**
     * chevrons est aussi appelé diagonales.
     */
    private ArrayList<Chevron> chevrons;
    private ArrayList<Poutre> poutres;
    private double longueurPorteAFaux;
    private double angle;
    private double longueurBase;

    private double hauteur;

    /**
     * Constructeur pour DTO
     */
    public Ferme(UUID id, Point origine, Entrait entrait, ArrayList<Chevron> chevrons, ArrayList<Poutre> poutres, double longueurPorteAFaux, double longueur, double angle) {
        this.id = id;
        this.setOrigine(origine);
        this.setEntrait(entrait);
        this.setChevrons(chevrons);
        this.setPoutres(poutres);
        this.setLongueurPorteAFaux(longueurPorteAFaux);
        this.setLongueurBase(longueur);
        this.setAngle(angle);
    }

    Ferme() {

    }

    /**
     * constructeur pour creation
     */
    Ferme(Point origine, double angle, double longueur, double longueurPorteAFaux, double hauteur, boolean estValide) {
        this.setAngle(angle);
        this.setLongueurPorteAFaux(longueurPorteAFaux);
        this.setOrigine(origine);
        this.setLongueurBase(longueur);
        this.setHauteur(hauteur);
        this.id = UUID.randomUUID();

        this.chevrons = new ArrayList<>();
        this.poutres = new ArrayList<>();

        this.genererStructureFerme();
    }

    public UUID getId() {
        return this.id;
    }

    double getAngle() {
        return this.angle;
    }

    void setAngle(double angle) {
        this.angle = angle;
    }

    public ArrayList<Chevron> getChevrons() {
        return this.chevrons;
    }

    void setChevrons(ArrayList<Chevron> chevrons) {
        this.chevrons = chevrons;
    }

    public ArrayList<Poutre> getPoutres() {
        return this.poutres;
    }

    private void setPoutres(ArrayList<Poutre> poutres) {
        this.poutres = poutres;
    }

    public Entrait getEntrait() {
        return this.entrait;
    }

    void setEntrait(Entrait entrait) {
        this.entrait = entrait;
    }

    public double getLongueurPorteAFaux() {
        return this.longueurPorteAFaux;
    }

    void setLongueurPorteAFaux(double longueur) {
        this.longueurPorteAFaux = longueur;
    }

    public double getLongueurBase() {
        return this.longueurBase;
    }

    void setLongueurBase(double longueur) {
        this.longueurBase = longueur;
    }

    double getHauteur() {
        return hauteur;
    }

    void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    private double getLongueurChevrons() {
        double b = Math.pow(this.getLongueurBase(), 2);
        double h = Math.pow(this.getHauteur(), 2);
        double longeur = Math.sqrt((h + (b / 4))) + this.T_PLANCHES_CHEVRON.getLargeur() * Math.tan(Math.toRadians(this.getAngle()));
        return longeur;
    }

    private ArrayList<Poutre> genererPoutresChevronOuestEst(Chevron chevron, boolean estChevronOuest) {
        ArrayList<Poutre> poutres = new ArrayList<>();
        double angleDeCoupe = 180 - 90 - this.getAngle();
        AngleDeCoupe angleDeCoupe1 = new AngleDeCoupe(0, 0, !estChevronOuest ? angleDeCoupe : 0, estChevronOuest ? angleDeCoupe : 0);
        double limitePlanche = TypePlanche.getLimitePlanche(this.T_PLANCHES_ENTRAIT).getDistanceDouble();
        double longueurChevronTotal = this.getLongueurChevrons();
        double longueurChevronRestant = longueurChevronTotal;
        int nbIteration = (int) Math.floor(longueurChevronTotal / limitePlanche);
        for (int i = 1; i <= nbIteration; i++) {
            longueurChevronRestant -= limitePlanche;
            // longueur du coté opposé du triangle formé par le chevron et la base. On en a besoin car les planche possède une largeur
            double smallTriangleLength = Math.tan(Math.toRadians(90 - this.getAngle())) * this.T_PLANCHES_POUTRE.getLargeur();

            double distanceParcourue = longueurChevronTotal - longueurChevronRestant;
            double postionZ = Math.cos(Math.toRadians(this.getAngle())) * distanceParcourue;
            double hauteur = Math.tan(Math.toRadians(this.getAngle())) * ((postionZ - smallTriangleLength) + this.T_PLANCHES_POUTRE.getLargeur());

            // creation point de la poutre
            Point ptPoutre = chevron.getPosition().cloner();
            ptPoutre.setY(ptPoutre.getY() + this.T_PLANCHES_POUTRE.getLargeur());

            // si est le chevron ouest (gauche)
            if (estChevronOuest) {
                ptPoutre.setX(ptPoutre.getX() + this.T_PLANCHES_POUTRE.getEpaisseur());
                ptPoutre.setZ(ptPoutre.getZ() + postionZ);
            } else {
                ptPoutre.setZ(ptPoutre.getZ() - postionZ - this.T_PLANCHES_POUTRE.getLargeur());
            }

            // verification que la poutre du centre ne soit pas chauvaucher.
            boolean ajouterPoutre = !estChevronOuest ?
                    ptPoutre.getZ() >= (this.getLongueurBase() / 2) + (this.T_PLANCHES_POUTRE.getLargeur() / 2) :
                    ptPoutre.getZ() + this.T_PLANCHES_POUTRE.getLargeur() <= (this.getLongueurBase() / 2) - (this.T_PLANCHES_POUTRE.getLargeur() / 2);

            // creation de la poutre.
            if (ajouterPoutre)
                poutres.add(new Poutre(
                        ptPoutre,
                        this.SENS_PLANCHES_POUTRE,
                        new Vecteur(0, 1, 0),
                        hauteur,
                        this.T_PLANCHES_POUTRE,
                        angleDeCoupe1
                ));
        }
        return poutres;
    }

    private void genererStructureFerme() {
        // creation de l'entrait
        Point pEntrait = this.getOrigine().cloner();
        this.setEntrait(new Entrait(
                pEntrait,
                this.SENS_PLANCHES_ENTRAIT,
                PointCardinal.NORD.getDirection(),
                this.getLongueurBase(),
                this.T_PLANCHES_ENTRAIT));

        // creation de la poutre central
        Point pMontantCentre = this.getOrigine().cloner();
        pMontantCentre.setX(pMontantCentre.getX());
        pMontantCentre.setY(pMontantCentre.getY() + this.T_PLANCHES_POUTRE.getLargeur());
        pMontantCentre.setZ(pMontantCentre.getZ() + (this.longueurBase / 2) - this.T_PLANCHES_POUTRE.getLargeur() / 2);
        this.poutres.add(new Poutre(
                pMontantCentre,
                this.SENS_PLANCHES_POUTRE,
                new Vecteur(0, 1, 0),
                this.getHauteur() - this.T_PLANCHES_ENTRAIT.getLargeur(),
                this.T_PLANCHES_POUTRE,
                new AngleDeCoupe()
        ));

        Point pChevronO = this.getOrigine().cloner();
        pChevronO.setX(pChevronO.getX() - this.T_PLANCHES_CHEVRON.getEpaisseur());
        pChevronO.setY(pChevronO.getY());
        Chevron chevronOuest = new Chevron(
                pChevronO,
                this.SENS_PLANCHES_CHEVRON,
                new Vecteur(
                        0,
                        Math.sin(Math.toRadians(this.getAngle())),
                        Math.cos(Math.toRadians(this.getAngle()))
                ),
                this.getLongueurChevrons(),
                this.T_PLANCHES_CHEVRON,
                new AngleDeCoupe(90 - angle, 0, 0, 90 - angle)
        );
        chevronOuest.debug_name = "Chevron OUEST";
        this.chevrons.add(chevronOuest);

        Point pChevronE = this.getOrigine().cloner();
        pChevronE.setX(pChevronE.getX());
        pChevronE.setY(pChevronE.getY());
        pChevronE.setZ(pChevronE.getZ() + this.getLongueurBase());
        Chevron chevronEst = new Chevron(
                pChevronE,
                this.SENS_PLANCHES_CHEVRON,
                new Vecteur(
                        0,
                        -Math.sin(Math.toRadians(this.getAngle())),
                        -Math.cos(Math.toRadians(this.getAngle()))
                ),
                this.getLongueurChevrons(),
                this.T_PLANCHES_CHEVRON,
                new AngleDeCoupe(90 - angle, 0, 0, 90 - angle)
        );
        chevronEst.debug_name = "Chevront EST";
        this.chevrons.add(chevronEst);

        // Creations des poutres de supports interne a la ferme.
        this.poutres.addAll(this.genererPoutresChevronOuestEst(chevronEst, false));
        this.poutres.addAll(this.genererPoutresChevronOuestEst(chevronOuest, true));

        // ajoutes la longeur du porte a faux et deplace le point du chevrons pour faire depasser le porte a faux.
        if (this.getLongueurPorteAFaux() > 0) {
            Point pDecalerChevronOuest = chevronOuest.getPosition().cloner();
            Point pDecalerChevronEst = chevronEst.getPosition().cloner();

            double longueurSuppHypotenuse = this.getLongueurPorteAFaux() / Math.cos(Math.toRadians(this.getAngle()));
            double hauteurOpposer = Math.tan(Math.toRadians(this.getAngle())) * this.getLongueurPorteAFaux();

            pDecalerChevronOuest.setY(pDecalerChevronOuest.getY() - hauteurOpposer);
            pDecalerChevronOuest.setZ(pDecalerChevronOuest.getZ() - this.getLongueurPorteAFaux());
            pDecalerChevronEst.setY(pDecalerChevronEst.getY() - hauteurOpposer);
            pDecalerChevronEst.setZ(pDecalerChevronEst.getZ() + this.getLongueurPorteAFaux());

            chevronOuest.setPosition(pDecalerChevronOuest);
            chevronEst.setPosition(pDecalerChevronEst);
            chevronOuest.setLongueur(chevronOuest.getLongueur() + longueurSuppHypotenuse);
            chevronEst.setLongueur(chevronEst.getLongueur() + longueurSuppHypotenuse);

            this.chevrons.clear();
            this.chevrons.add(chevronOuest);
            this.chevrons.add(chevronEst);
        }
    }

    @Override
    public ArrayList<Planche> getPlanches() {
        ArrayList<Planche> planches = new ArrayList<>();

        planches.add(this.getEntrait());
        planches.addAll(this.getChevrons());
        planches.addAll(this.getPoutres());

        return planches;
    }

    @Override
    public Ferme cloner() {
        return new Ferme(
                this.getId(),
                this.getOrigine().cloner(),
                this.getEntrait().cloner(),
                this.getChevrons()
                        .stream().map(Chevron::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.getPoutres()
                        .stream().map(Poutre::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.getLongueurPorteAFaux(),
                this.getLongueurBase(),
                this.getAngle()
        );
    }
}