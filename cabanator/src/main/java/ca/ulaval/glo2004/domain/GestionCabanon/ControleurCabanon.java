package ca.ulaval.glo2004.domain.GestionCabanon;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.CabanonAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.MurAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancherAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Cabanon;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypeSauvegarde;
import ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces.IServiceCabanon;
import ca.ulaval.glo2004.domain.GestionCabanon.Services.ServiceMurs;
import ca.ulaval.glo2004.domain.GestionCabanon.Services.ServicePlancher;
import ca.ulaval.glo2004.domain.GestionCabanon.Services.ServiceToit;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.*;

import java.util.UUID;

public class ControleurCabanon {
    private final IServiceCabanon serviceCabanon;
    private ServicePlancher servicePlancher;
    private ServiceMurs serviceMurs;
    private ServiceToit serviceToit;

    public ControleurCabanon(IServiceCabanon serviceCabanon) {
        this.serviceCabanon = serviceCabanon;
    }

    //#region MÉTHODES CABANON

    public boolean creerProjet(Dimension3D dimension3D) {
        final Cabanon cabanon = this.serviceCabanon.creerProjet(dimension3D);

        this.servicePlancher = new ServicePlancher(cabanon.getPlancher());
        this.serviceMurs = new ServiceMurs(cabanon.getMurs());
        this.serviceToit = new ServiceToit(cabanon.getToit());

        return true;
    }

    public boolean ouvrirProjet(String cheminFichier) {
        try {
            final Cabanon cabanon = this.serviceCabanon.ouvrirProjet(cheminFichier);
            this.servicePlancher = new ServicePlancher(cabanon.getPlancher());
            this.serviceMurs = new ServiceMurs(cabanon.getMurs());
            this.serviceToit = new ServiceToit(cabanon.getToit());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean enregistrerProjet(String cheminFichier, String nomProjet, String versionSauvegarde, boolean renommerVersionsProjet, TypeSauvegarde typeSauvegarde) {
        return this.serviceCabanon.enregistrerProjet(cheminFichier, nomProjet, versionSauvegarde, renommerVersionsProjet, typeSauvegarde);
    }

    public boolean exporterPiecesProjet(String cheminFichier) {
        return this.serviceCabanon.exporterPiecesProjet(cheminFichier);
    }

    public double calculerCout() {
        return this.serviceCabanon.calculerCout();
    }

    public void annulerActionCabanon() {
        this.serviceCabanon.annulerActionCabanon();
        mettreAJourServices();
    }

    public void retablirActionCabanon() {
        this.serviceCabanon.retablirActionCabanon();
        mettreAJourServices();
    }

    private void mettreAJourServices() {
        Cabanon cabanon = this.serviceCabanon.obtenirCabanonCourant();

        this.serviceCabanon.mettreAJourCabanon(cabanon);
        this.servicePlancher.mettreAJourPlancher(cabanon.getPlancher());
        this.serviceMurs.mettreAJourMurs(cabanon.getMurs());
        this.serviceToit.mettreAJourToit(cabanon.getToit());
    }

//    public void regenererCabanon() {
//        this.servicePlancher.regenererStructure();
//        this.serviceMurs.regenererStructure();
//        this.serviceToit.regenererStructure();
//    }

    public void configurerCoutPlanche(TypePlanche type, double cout) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceCabanon.configurerCoutPlanche(type, cout);
        this.serviceCabanon.calculerCout();
    }

    public void configurerLimitePlanche(TypePlanche type, double limite) {
        System.out.println("Limite : " + type + " limite :" + limite);
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceCabanon.configurerLimitePlanche(type, limite);
    }

    //#endregion

    //#region MÉTHODES PLANCHER

    public void configurerLongueur(double longueur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.servicePlancher.configurerLongueur(longueur);
        this.serviceMurs.configurerLongueur(longueur);
        this.serviceToit.configurerLongueur(longueur);
    }

    public void configurerLargeur(double largeur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.servicePlancher.configurerLargeur(largeur);
        this.serviceMurs.configurerLargeur(largeur);
        this.serviceToit.configurerLargeur(largeur);
    }

    public void configurerDistanceEntreSolivesPlancher(double distance) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.servicePlancher.configurerDistanceEntreSolives(distance);
    }

    public void ajouterLigneEntremises(PointDTO positionCurseur, double espacement) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.servicePlancher.ajouterLigneEntremises(positionCurseur.FromDTO(), espacement);
    }

    public void supprimerLigneEntremises(UUID id) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.servicePlancher.supprimerLigneEntremises(id);
    }

    public void positionnerLigneEntremises(UUID id, PointDTO positionCurseur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.servicePlancher.positionnerLigneEntremises(id, positionCurseur.FromDTO());
    }

    public void configurerDecalageEntreEntremises(UUID id, double decalage) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.servicePlancher.configurerDecalageEntreEntremises(id, decalage);
    }

    //#endregion

    //#region MÉTHODES MURS

    public void configurerHauteur(double hauteur) {
        System.out.println("configurer Hauteur cabanon :" + hauteur);
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.configurerHauteur(hauteur);

        double pY = this.serviceMurs.obtenirAffichageMur(PointCardinal.OUEST).getPosition().getY();
        this.serviceToit.mettreAJourToit(pY + hauteur);
    }

    public void ajouterEntremise(PointDTO positionCurseur, PointCardinal positionMur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.ajouterEntremise(positionCurseur.FromDTO(), positionMur);
    }

    public void ajouterEntremiseAccessoire(PointDTO positionCurseur, PointCardinal positionMur, UUID accessoireId) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.ajouterEntremiseAccessoire(positionCurseur.FromDTO(), positionMur, accessoireId);
    }

    public void positionnerEntremise(UUID id, PointDTO positionCurseur, PointCardinal positionMur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.positionnerEntremise(id, positionCurseur.FromDTO(), positionMur);
    }

    public void positionnerEntremiseAccessoire(UUID id, PointDTO positionCurseur, PointCardinal positionMur, UUID accessoireId) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.positionnerEntremiseAccessoire(id, positionCurseur.FromDTO(), positionMur, accessoireId);
    }

    public void supprimerEntremise(UUID id, PointCardinal positionMur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.supprimerEntremise(id, positionMur);
    }

    public void supprimerEntremiseAccessoire(UUID id, PointCardinal positionMur, UUID accessoireId) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.supprimerEntremiseAccessoire(id, positionMur, accessoireId);
    }

    public void ajouterPorte(PointDTO positionCurseur, PointCardinal positionMur, double longueurTrou, double hauteurTrou) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.ajouterPorte(positionCurseur.FromDTO(), positionMur, longueurTrou, hauteurTrou);
    }

    public void ajouterFenetre(PointDTO positionCurseur, PointCardinal positionMur, double longueurTrou, double hauteurTrou) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.ajouterFenetre(positionCurseur.FromDTO(), positionMur, longueurTrou, hauteurTrou);
    }

    public void positionnerPorte(PointDTO positionCurseur, PointCardinal positionMur, UUID accessoireId) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.positionnerPorte(positionCurseur.FromDTO(), positionMur, accessoireId);
    }

    public void positionnerFenetre(PointDTO positionCurseur, PointCardinal positionMur, UUID accessoireId) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.positionnerFenetre(positionCurseur.FromDTO(), positionMur, accessoireId);
    }
    public void positionnerEntremise(PointDTO positionCurseur, PointCardinal positionMur, UUID accessoireId) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.positionnerEntremise(accessoireId, positionCurseur.FromDTO(), positionMur);
    }

    public void supprimerPorte(UUID id, PointCardinal positionMur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.supprimerPorte(id, positionMur);
    }

    public void supprimerFenetre(UUID id, PointCardinal positionMur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.supprimerFenetre(id, positionMur);
    }

    public void configurerDimensionsAccessoire(UUID accessoireId, double longueur, double largeur, PointCardinal positionMur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.configurerDimensionsAccessoire(longueur, largeur, accessoireId, positionMur);
    }

    public void configurerTypeLinteauPorte(UUID id, TypePlanche type, PointCardinal positionMur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.configurerTypeLinteauPorte(id, type, positionMur);
    }

    public void configurerTypeLinteauFenetre(UUID id, TypePlanche type, PointCardinal positionMur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.configurerTypeLinteauFenetre(id, type, positionMur);
    }

    public void configurerDistanceEntreMontantsPorte(UUID id, double distance, PointCardinal positionMur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.configurerDistanceEntreMontantsPorte(id, distance, positionMur);
    }

    public void configurerDistanceEntreMontantsFenetre(UUID id, double distance, PointCardinal positionMur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.configurerDistanceEntreMontantsFenetre(id, distance, positionMur);
    }

    public void configurerDistanceEntreMontantsMurs(double distance, PointCardinal positionMur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceMurs.configurerDistanceEntreMontantsMurs(distance, positionMur);
    }

    //#endregion

    //#region MÉTHODES TOIT

    public void configurerAngle(double angle) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceToit.configurerAngle(angle);
    }

    public void configurerDistanceEntreFermes(double distance) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceToit.configurerDistanceEntreFermes(distance);
    }

    public void configurerDistanceEntreFermesSupp(double distance) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceToit.configurerDistanceEntreFermesSupp(distance);
    }

    public void configurerLongueurPorteAFaux(double longueur) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceToit.configurerLongueurPorteAFaux(longueur);
    }

    public void configurerDistanceEntreEntremisesToit(double distance) {
        this.serviceCabanon.sauvegarderInstanceCourante();
        this.serviceToit.configurerDistanceEntreEntremisesToit(distance);
    }

    //#endregion

    //#region MÉTHODES POUR OBTENIR CONFIGURATION

    public CabanonDTO obtenirConfigurationCabanon() {
        return new CabanonDTO(this.serviceCabanon.obtenirConfigurationCabanon());
    }

    public PlancherDTO obtenirConfigurationPlancher() {
        return new PlancherDTO(this.servicePlancher.obtenirConfigurationPlancher());
    }

    public LigneEntremisesDTO obtenirConfigurationLigneEntremises(UUID id) {
        return new LigneEntremisesDTO(this.servicePlancher.obtenirConfigurationLigneEntremises(id));
    }

    public MurDTO obtenirConfigurationMur(PointCardinal positionMur) {
        return new MurDTO(this.serviceMurs.obtenirConfigurationMur(positionMur));
    }
    public EntremiseDTO obtenirConfigurationEntremise(UUID id, PointCardinal positionMur) {
        return new EntremiseDTO(this.serviceMurs.obtenirConfigurationEntremise(id, positionMur));
    }
    public PorteDTO obtenirConfigurationPorte(UUID id, PointCardinal positionMur) {
        return new PorteDTO(this.serviceMurs.obtenirConfigurationPorte(id, positionMur));
    }

    public FenetreDTO obtenirConfigurationFenetre(UUID id, PointCardinal positionMur) {
        return new FenetreDTO(this.serviceMurs.obtenirConfigurationFenetre(id, positionMur));
    }

    public ToitDTO obtenirConfigurationToit() {
        return new ToitDTO(this.serviceToit.obtenirConfigurationToit());
    }

    //#endregion

    //#region MÉTHODES POUR AFFICHAGE

    public CabanonAffichage obtenirAffichageCabanon() {
        return this.serviceCabanon.obtenirAffichageCabanon();
    }

    public PlancherAffichage obtenirAffichagePlancher() {
        return this.servicePlancher.obtenirAffichagePlancher();
    }

    public MurAffichage obtenirAffichageMur(PointCardinal positionMur) {
        return this.serviceMurs.obtenirAffichageMur(positionMur);
    }

    //#endregion
}