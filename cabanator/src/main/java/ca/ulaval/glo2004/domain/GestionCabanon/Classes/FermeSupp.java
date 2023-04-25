package ca.ulaval.glo2004.domain.GestionCabanon.Classes;


import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.*;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class FermeSupp extends Ferme {
    private final TypePlanche T_PLANCHES_ENTREMISE = TypePlanche.P_2X4;
    private final Sens SENS_PLANCHES_ENTREMISE = Sens.VOIR_EPAISSEUR;

    private ArrayList<Entremise> entremises;
    private double distanceFermeSupp;

    private boolean estFermeSuppNord;

    private double distanceEntreEntremise;


    /**
     * Constructeur pour DTO
     */
    public FermeSupp(UUID id, Point centre, Entrait entrait, ArrayList<Chevron> chevrons,
                     ArrayList<Poutre> poutres, ArrayList<Entremise> entremises, double longueurPorteAFaux,
                     boolean estFermeSuppNord, double distanceEntreEntremise, double distanceFermeSupp, double longueur, double angle) {
        super(id, centre, entrait, chevrons, poutres, longueurPorteAFaux, longueur, angle);
        this.setEntremises(entremises);
        this.setEstFermeSuppNord(estFermeSuppNord);
        this.setDistanceEntreEntremise(distanceEntreEntremise);
        this.setDistanceFermeSupp(distanceFermeSupp);
    }

    /**
     * Constructeur pour la creation
     */
    FermeSupp(Point origine, double angle, double longueur, double longueurPorteAFaux, double hauteur,
              boolean estValide, double distanceFermeSupp, boolean estFermeSuppNord, double distanceEntreEntremise) {
        super(origine, angle, longueur, longueurPorteAFaux, hauteur, estValide);
        this.entremises = new ArrayList<>();
        this.setDistanceFermeSupp(distanceFermeSupp);
        this.setEstFermeSuppNord(estFermeSuppNord);
        this.setDistanceEntreEntremise(distanceEntreEntremise);
        this.genererEntremises();
    }

    public double getDistanceFermeSupp() {
        return distanceFermeSupp;
    }

    public void setDistanceFermeSupp(double distanceFermeSupp) {
        this.distanceFermeSupp = distanceFermeSupp;
    }

    private void genererEntremises() {
        this.entremises.clear();

        double demiLongeur = this.getLongueurBase() / 2;
        double positionCurrZ = 0;
        int i = 0;
        while (demiLongeur > positionCurrZ) {
            double positionGauche = this.getLongueurBase() - positionCurrZ;
            double positionDrotie = positionCurrZ;

            Point pointEntremiseDroite = this.getOrigine().cloner();
            pointEntremiseDroite.setX(this.getEstFermeSuppNord()
                    ? pointEntremiseDroite.getX() - super.T_PLANCHES_POUTRE.getEpaisseur()
                    : pointEntremiseDroite.getX() - super.T_PLANCHES_POUTRE.getEpaisseur() + this.getDistanceFermeSupp()
            );
            pointEntremiseDroite.setZ(positionDrotie);
            pointEntremiseDroite.setY(
                    pointEntremiseDroite.getY()
                            + this.getPositionYEntremise(pointEntremiseDroite.getZ()
                    ) + (this.getLongueurPorteAFaux() > 0 ? 0 : i == 0 ? this.T_PLANCHES_ENTREMISE.getLargeur() : 0)
            );

            Entremise entremiseDroite = new Entremise(
                    pointEntremiseDroite,
                    this.getLongueurPorteAFaux() > 0 ? Sens.VOIR_LARGEUR : i == 0 ? SENS_PLANCHES_ENTREMISE : Sens.VOIR_LARGEUR,
                    new Vecteur(
                            1,
                            this.getLongueurPorteAFaux() > 0 ?
                                    Math.tan(Math.toRadians(this.getAngle())) : i == 0 ?
                                    -Math.tan(Math.toRadians(90)) : Math.tan(Math.toRadians(this.getAngle())),
                            0
                    ),
                    this.getDistanceFermeSupp() - super.T_PLANCHES_POUTRE.getEpaisseur()
            );

            Point pointEntremiseGauche = this.getOrigine().cloner();
            pointEntremiseGauche.setX(this.getEstFermeSuppNord()
                    ? pointEntremiseGauche.getX() - super.T_PLANCHES_POUTRE.getEpaisseur()
                    : pointEntremiseGauche.getX() - super.T_PLANCHES_POUTRE.getEpaisseur() + this.getDistanceFermeSupp()
            );
            pointEntremiseGauche.setZ(positionGauche - (this.getLongueurPorteAFaux() > 0 ?
                    0 : i == 0 ? this.T_PLANCHES_ENTREMISE.getEpaisseur() : 0));
            pointEntremiseGauche.setY(pointEntremiseDroite.getY() - (this.getLongueurPorteAFaux() > 0 ?
                    0 : i == 0 ? this.T_PLANCHES_ENTREMISE.getLargeur() : 0));

            Entremise entremiseGauche = new Entremise(
                    pointEntremiseGauche,
                    this.getLongueurPorteAFaux() > 0 ? SENS_PLANCHES_ENTREMISE : i == 0 ? Sens.VOIR_LARGEUR : SENS_PLANCHES_ENTREMISE,
                    new Vecteur(
                            1,
                            this.getLongueurPorteAFaux() > 0 ?
                                    Math.tan(Math.toRadians(90 - this.getAngle())) : i == 0
                                    ? -Math.tan(Math.toRadians(180)) : Math.tan(Math.toRadians(90 - this.getAngle())),
                            0
                    ),
                    this.getDistanceFermeSupp() - super.T_PLANCHES_POUTRE.getEpaisseur()
            );

            this.entremises.add(entremiseDroite);
            this.entremises.add(entremiseGauche);

            positionCurrZ += this.getDistanceEntreEntremise();
            i++;
        }
    }

    public ArrayList<Entremise> getEntremises() {
        return entremises;
    }

    void setEntremises(ArrayList<Entremise> entremises) {
        this.entremises = entremises;
    }

    public boolean getEstFermeSuppNord() {
        return estFermeSuppNord;
    }

    public void setEstFermeSuppNord(boolean estFermeSuppNord) {
        this.estFermeSuppNord = estFermeSuppNord;
    }

    public double getDistanceEntreEntremise() {
        return distanceEntreEntremise;
    }

    private void setDistanceEntreEntremise(double distanceEntreEntremise) {
        this.distanceEntreEntremise = distanceEntreEntremise;
    }

    public void configurerDistanceEntreEntremise(double distance) {
        this.setDistanceEntreEntremise(distance);
        this.genererEntremises();
    }

    /**
     * Obtenir la position y de l'entremise en fonction de ca position dans le triangle.
     *
     * @param positionZ position en z
     * @return la hauteur en y
     */
    private double getPositionYEntremise(double positionZ) {
        return Math.tan(Math.toRadians(this.getAngle())) * (this.getOrigine().getZ() + positionZ);
    }

    public double getLongueurBase() {
        return super.getLongueurBase();
    }

    @Override
    public ArrayList<Planche> getPlanches() {
        ArrayList<Planche> planches = new ArrayList<>();

        planches.add(this.getEntrait());
        planches.addAll(this.getChevrons());
        planches.addAll(this.getPoutres());
        planches.addAll(this.getEntremises());

        return planches;
    }

    @Override
    public FermeSupp cloner() {
        return new FermeSupp(
                this.getId(),
                this.getOrigine().cloner(),
                this.getEntrait().cloner(),
                this.getChevrons()
                        .stream().map(Chevron::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.getPoutres()
                        .stream().map(Poutre::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.getEntremises()
                        .stream().map(Entremise::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)),
                super.getLongueurPorteAFaux(),
                this.getEstFermeSuppNord(),
                this.getDistanceEntreEntremise(),
                this.getDistanceFermeSupp(),
                this.getLongueurBase(),
                this.getAngle()
        );
    }
}
