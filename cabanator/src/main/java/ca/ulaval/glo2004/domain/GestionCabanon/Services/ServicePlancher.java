package ca.ulaval.glo2004.domain.GestionCabanon.Services;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancherAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.LigneEntremises;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Plancher;
import ca.ulaval.glo2004.domain.GestionCabanon.Factories.LigneEntremisesFactory;
import ca.ulaval.glo2004.domain.GestionCabanon.Factories.PlancherFactory;
import ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces.IServicePlancher;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.security.InvalidParameterException;
import java.util.UUID;

public class ServicePlancher implements IServicePlancher {
    private Plancher plancher;

    public ServicePlancher(Plancher plancher) {
        this.plancher = plancher;
    }

    @Override
    public void configurerLongueur(double longueur) {
        this.plancher.configurerLongueur(longueur);
    }

    @Override
    public void configurerLargeur(double largeur) {
        this.plancher.configurerLargeur(largeur);
    }

    @Override
    public void configurerDistanceEntreSolives(double distance) {
        this.plancher.configurerDistanceEntreSolives(distance);
    }

    @Override
    public void ajouterLigneEntremises(Point positionCurseur, double espacement) {
        this.plancher.ajouterLigneEntremises(positionCurseur, espacement);
    }

    @Override
    public void supprimerLigneEntremises(UUID id) {
        this.plancher.supprimerLigneEntremises(id);
    }

    @Override
    public void positionnerLigneEntremises(UUID id, Point positionCurseur) {
        try {
            this.plancher.positionnerLigneEntremises(id, positionCurseur);
        } catch (InvalidParameterException e) {
            throw e;
        }
    }

    @Override
    public void configurerDecalageEntreEntremises(UUID id, double decalage) {
        try {
            this.plancher.configurerDecalageEntreEntremises(id, decalage);
        } catch (InvalidParameterException e) {
            throw e;
        }
    }

    @Override
    public Plancher obtenirConfigurationPlancher() {
        return PlancherFactory.FromPlancher(this.plancher);
    }

    @Override
    public LigneEntremises obtenirConfigurationLigneEntremises(UUID id) {
        try {
            return LigneEntremisesFactory.FromLigneEntremises(this.plancher.obtenirLigneEntremises(id));
        } catch (InvalidParameterException e) {
            throw e;
        }
    }

    @Override
    public PlancherAffichage obtenirAffichagePlancher() {
        return new PlancherAffichage(this.plancher);
    }

    @Override
    public void mettreAJourPlancher(Plancher plancher) {
        this.plancher = plancher;
    }
}