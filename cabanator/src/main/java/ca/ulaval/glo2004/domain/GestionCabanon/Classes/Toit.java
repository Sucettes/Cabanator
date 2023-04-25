package ca.ulaval.glo2004.domain.GestionCabanon.Classes;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Interfaces.IClonable;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class Toit implements IClonable<Toit>, java.io.Serializable {

    //#region ATTRIBUTS
    private final TypePlanche T_PLANCHES_ENTRAIT = TypePlanche.P_2X4;
    private Point origin;
    private double angle;
    private ArrayList<Ferme> fermes;
    private ArrayList<FermeSupp> fermesSupp;
    private double distanceEntreFermes;
    private double distanceEntreFermesSupp;
    private double longueurPorteAFaux;
    private double largeur;
    private double longueur;
    private double distanceEntreEntremise;

    //#endregion

    //#region CONSTRUCTEURS

    /**
     * Constructeur de base
     */
    private Toit(double angle, double distanceEntreFermes, double distanceEntreFermesSupp, double longueurPorteAFaux) {
        this.setAngle(angle);
        this.setDistanceEntreFermes(distanceEntreFermes);
        this.setDistanceEntreFermesSupp(distanceEntreFermesSupp);
        this.setLongueurPorteAFaux(longueurPorteAFaux);
    }

    /**
     * Constructeur de base pour la création ou modification
     */
    private Toit(double longueur, double largeur, double angle, double distanceEntreFermes,
                 double distanceEntreFermesSupp, double longueurPorteAFaux) {
        this(angle, distanceEntreFermes, distanceEntreFermesSupp, longueurPorteAFaux);
        this.setLongeur(longueur);
        this.setLargeur(largeur);
    }

    /**
     * Constructeur pour DTO
     */
    protected Toit(double angle, double distanceEntreFermes, double distanceEntreFermesSupp, double longueurPorteAFaux,
                   double distanceEntreEntremise) {
        this(angle, distanceEntreFermes, distanceEntreFermesSupp, longueurPorteAFaux);
        this.setDistanceEntreEntremise(distanceEntreEntremise);
    }

    /**
     * Constructeur pour un toit existant
     */
    private Toit(Point origine, double longueur, double largeur, double angle, double distanceEntreFermes,
                 double distanceEntreFermesSupp, ArrayList<Ferme> fermes, ArrayList<FermeSupp> fermesSupp,
                 double longueurPorteAFaux, double distanceEntreEntremise) {
        this(longueur, largeur, angle, distanceEntreFermes, distanceEntreFermesSupp, longueurPorteAFaux);
        this.setOrigin(origine);
        this.setFermes(fermes);
        this.setFermesSupp(fermesSupp);
        this.configurerDistanceEntreEntremisesToit(distanceEntreEntremise);
    }

    /**
     * Constructeur pour la creation
     */
    public Toit(Point origine, double longueur, double largeur) {
        this(longueur, largeur, 45, 0, 0, 0);
        this.setOrigin(origine);

        double distanceEntremiseDefault = 96; // 96 pouces
        this.setDistanceEntreEntremise(distanceEntremiseDefault);

        this.fermes = new ArrayList<>();
        this.fermesSupp = new ArrayList<>();

        this.configurerDistanceEntreFermes(this.getLongueur() > TypePlanche.P_2X4.getEpaisseur() * 2
                ? this.getLongueur() / 2
                : 100);

        this.configurerDistanceEntreFermesSupp(6);
    }

    //#endregion

    //#region PROPRIÉTÉS ET INDEXEURS

    public Point getOrigin() {
        return this.origin;
    }

    private void setOrigin(Point origin) {
        this.origin = origin;
    }

    public double getLargeur() {
        return largeur;
    }

    void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    public double getLongueur() {
        return longueur;
    }

    void setLongeur(double longueur) {
        this.longueur = longueur;
    }

    public double getAngle() {
        return this.angle;
    }

    void setAngle(double angle) {
        this.angle = angle;
    }

    public double getHauteur() {
        return Math.tan(Math.toRadians(this.getAngle())) * (this.getLargeur() / 2);
    }

    public double getDistanceEntreFermes() {
        return this.distanceEntreFermes;
    }

    void setDistanceEntreFermes(double distance) {
        this.distanceEntreFermes = distance;
    }

    public double getDistanceEntreFermesSupp() {
        return this.distanceEntreFermesSupp;
    }

    void setDistanceEntreFermesSupp(double distance) {
        this.distanceEntreFermesSupp = distance;
    }

    public double getLongueurPorteAFaux() {
        return this.longueurPorteAFaux;
    }

    void setLongueurPorteAFaux(double longueur) {
        this.longueurPorteAFaux = longueur;
    }

    public ArrayList<Ferme> getFermes() {
        return this.fermes;
    }

    public void setFermes(ArrayList<Ferme> fermes) {
        this.fermes = fermes;
    }

    public ArrayList<FermeSupp> getFermesSupp() {
        return fermesSupp;
    }

    public void setFermesSupp(ArrayList<FermeSupp> fermesSupp) {
        this.fermesSupp = fermesSupp;
    }

    public double getDistanceEntreEntremise() {
        return this.distanceEntreEntremise;
    }

    private void setDistanceEntreEntremise(double distance) {
        this.distanceEntreEntremise = distance;
    }

    //#endregion

    //#region MÉTHODES

    public void MAJOrigineToit(double pY) {
        this.setOrigin(this.getOrigin().configurerY(pY));

        this.fermes = new ArrayList<>();
        this.fermesSupp = new ArrayList<>();

        this.configurerDistanceEntreFermes(this.getLongueur() > TypePlanche.P_2X4.getEpaisseur() * 2
                ? this.getLongueur() / 2
                : 100);

        this.configurerDistanceEntreFermesSupp(6);
    }

    public void configurerLongueur(double longueur) {
        this.setLongeur(longueur);
        this.regenererStructure();
    }

    /**
     * Configurer la largeur du toit (ouest -> est)
     *
     * @param largeur largeur en pied
     */
    public void configurerLargeur(double largeur) {
        this.setLargeur(largeur);
        this.regenererStructure();
    }

    /**
     * l'angle du toi dans le coin du bas. Affect la hauteur du toit automatiquement.
     *
     * @param angleInclinaison angle du toit.
     */
    public void configurerAngle(double angleInclinaison) {
        this.setAngle(angleInclinaison);
        this.regenererStructure();
    }

    public void configurerDistanceEntreFermes(double distanceEntreFermes) {
        this.setDistanceEntreFermes(distanceEntreFermes);
        this.regenererStructure();
    }

    public void configurerDistanceEntreFermesSupp(double distanceEntreFermesSupp) {
        this.setDistanceEntreFermesSupp((distanceEntreFermesSupp));
        this.regenererStructure();
    }

    public void configurerLongueurPorteAFaux(double longueurPorteAFaux) {
        this.setLongueurPorteAFaux(longueurPorteAFaux);
        this.regenererStructure();
    }

    public void configurerDistanceEntreEntremisesToit(double distance) {
        this.setDistanceEntreEntremise(distance);
        this.getFermesSupp().forEach(ferme -> ferme.configurerDistanceEntreEntremise(distance));
        this.regenererStructure();
    }

    protected void regenererStructure() {
        this.genererStructure();
    }

    protected void genererStructure() {
        this.setFermes(this.genererFermes());
        this.setFermesSupp(this.genererFermesSupp());
    }

    private ArrayList<Ferme> genererFermes() {
        ArrayList<Ferme> fermes = new ArrayList<>();

        // creation ferme nord
        Point pFermeN = this.getOrigin().cloner();
        pFermeN.setX(pFermeN.getX() - this.T_PLANCHES_ENTRAIT.getEpaisseur());
        Ferme fermeNord = new Ferme(
                pFermeN,
                this.getAngle(),
                this.getLargeur(),
                this.getLongueurPorteAFaux(),
                this.getHauteur(),
                true
        );

        // creation ferme sud
        Point pFermeS = this.getOrigin().cloner();
        pFermeS.setX(pFermeS.getX() - this.getLongueur());
        Ferme fermeSud = new Ferme(
                pFermeS,
                this.getAngle(),
                this.getLargeur(),
                this.getLongueurPorteAFaux(),
                this.getHauteur(),
                true
        );

        fermes.add(fermeNord);
        fermes.add(fermeSud);
        fermes.addAll(this.genererListFermeInterieur(fermeNord, fermeSud));

        return fermes;
    }

    private ArrayList<Ferme> genererListFermeInterieur(Ferme fermeNord, Ferme fermeSud) {
        ArrayList<Ferme> fermesInterieur = new ArrayList<>();

        double pXOrigine = fermeNord.getOrigine().getX();
        double epaisseurFerme = T_PLANCHES_ENTRAIT.getEpaisseur();

        double pXPrecedent = pXOrigine + epaisseurFerme;
        double pXMax = (pXOrigine + epaisseurFerme) - (this.getLongueur() - epaisseurFerme * 2);
        double pXDernierFerme = (pXOrigine + epaisseurFerme) - (this.getLongueur() - epaisseurFerme);
        double espaceRestant = this.getLongueur();
        boolean pXValide = true;

        do {
            double pXCourant = pXPrecedent;
            double pXSuivant = pXCourant - this.getDistanceEntreFermes();

            pXValide = pXSuivant >= pXMax;
            boolean ajouterFerme = -pXDernierFerme + pXPrecedent > this.getDistanceEntreFermes()
                    && pXSuivant >= pXDernierFerme;

            if (pXValide || (ajouterFerme && pXSuivant > pXDernierFerme)) {
                pXCourant = pXSuivant;
            }

            if (ajouterFerme) {
                Point pFerme = this.getOrigin().cloner();
                pFerme.setX(pXCourant - epaisseurFerme);

                fermesInterieur.add(new Ferme(
                        pFerme,
                        this.getAngle(),
                        this.getLargeur(), this.getLongueurPorteAFaux(),
                        this.getHauteur(),
                        pXValide
                ));

                pXPrecedent = pXCourant;
                espaceRestant -= this.getDistanceEntreFermes();
            }
        } while (espaceRestant > 0 && pXValide);

        return fermesInterieur;
    }

    private ArrayList<FermeSupp> genererFermesSupp() {
        ArrayList<FermeSupp> fermesSupp = new ArrayList<>();

        // creation ferme supp nord
        Point pFermeSuppN = this.getOrigin().cloner();
        pFermeSuppN.setX(pFermeSuppN.getX() + this.getDistanceEntreFermesSupp() - this.T_PLANCHES_ENTRAIT.getEpaisseur());
        FermeSupp fermeSuppNord = new FermeSupp(
                pFermeSuppN,
                this.getAngle(),
                this.getLargeur(),
                this.getLongueurPorteAFaux(),
                this.getHauteur(),
                true,
                this.getDistanceEntreFermesSupp(),
                true,
                this.getDistanceEntreEntremise()
        );

        // creation ferme supp sud
        Point pFermeSuppS = this.getOrigin().cloner();
        pFermeSuppS.setX(pFermeSuppS.getX() - this.getLongueur() - this.getDistanceEntreFermesSupp());
        FermeSupp fermeSuppSud = new FermeSupp(
                pFermeSuppS,
                this.getAngle(),
                this.getLargeur(),
                this.getLongueurPorteAFaux(),
                this.getHauteur(),
                true,
                this.getDistanceEntreFermesSupp(),
                false,
                this.getDistanceEntreEntremise()
        );

        fermesSupp.add(fermeSuppNord);
        fermesSupp.add(fermeSuppSud);

        return fermesSupp;
    }

    public ArrayList<Planche> getPlanches() {
        ArrayList<Planche> planches = new ArrayList<>();

        this.getFermes().forEach(ferme -> planches.addAll(ferme.getPlanches()));
        this.getFermesSupp().forEach(fermeSupp -> planches.addAll(fermeSupp.getPlanches()));

        return planches;
    }

    @Override
    public Toit cloner() {
        Toit toitCloner = new Toit(
                this.getOrigin(),
                this.getLongueur(),
                this.getLargeur(),
                this.getAngle(),
                this.getDistanceEntreFermes(),
                this.getDistanceEntreFermesSupp(),
                this.getFermes().stream().map(Ferme::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.getFermesSupp().stream().map(FermeSupp::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.getLongueurPorteAFaux(),
                this.getDistanceEntreEntremise()
        );
        return toitCloner;
    }

    //#endregion
}