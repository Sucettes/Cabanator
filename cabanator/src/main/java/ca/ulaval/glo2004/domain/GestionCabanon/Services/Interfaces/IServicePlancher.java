package ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancherAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.LigneEntremises;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Plancher;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.UUID;

public interface IServicePlancher {
    void configurerLongueur(double longueur);

    void configurerLargeur(double largeur);

    void configurerDistanceEntreSolives(double distance);


    void ajouterLigneEntremises(Point positionCurseur, double espacement);

    void supprimerLigneEntremises(UUID id);

    void positionnerLigneEntremises(UUID id, Point positionCurseur);

    void configurerDecalageEntreEntremises(UUID id, double decalage);

    Plancher obtenirConfigurationPlancher();

    LigneEntremises obtenirConfigurationLigneEntremises(UUID id);

    PlancherAffichage obtenirAffichagePlancher();

    void mettreAJourPlancher(Plancher plancher);
}