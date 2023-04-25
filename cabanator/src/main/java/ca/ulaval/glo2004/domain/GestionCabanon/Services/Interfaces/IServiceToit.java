package ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.ToitAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Toit;

import java.util.ArrayList;

public interface IServiceToit {
    void configurerLongueur(double longueur);

    void configurerLargeur(double largeur);

    void configurerAngle(double angle);

    void configurerDistanceEntreFermes(double distance);

    void configurerDistanceEntreFermesSupp(double distance);

    void configurerLongueurPorteAFaux(double longueur);

    void configurerDistanceEntreEntremisesToit(double distance);

    Toit obtenirConfigurationToit();

    ToitAffichage obtenirAffichageToit();

    ArrayList<Planche> getPlanches();

    void mettreAJourToit(Toit toit);

    void mettreAJourToit(double pY);
}