package ca.ulaval.glo2004.domain.GestionCabanon.Services;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.ToitAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Toit;
import ca.ulaval.glo2004.domain.GestionCabanon.Factories.ToitFactory;
import ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces.IServiceToit;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;

public class ServiceToit implements IServiceToit {
    private Toit toit;

    public ServiceToit(Toit toit) {
        this.toit = toit;
    }

    @Override
    public void configurerLongueur(double longueur) {
        this.toit.configurerLongueur(longueur);
    }

    @Override
    public void configurerLargeur(double largeur) {
        this.toit.configurerLargeur(largeur);
    }

    @Override
    public void configurerAngle(double angle) {
        this.toit.configurerAngle(angle);
    }

    @Override
    public void configurerDistanceEntreFermes(double distanceEmtreFerme) {
        this.toit.configurerDistanceEntreFermes(distanceEmtreFerme);
    }

    @Override
    public void configurerDistanceEntreFermesSupp(double distanceEmtreFermeSupp) {
        this.toit.configurerDistanceEntreFermesSupp(distanceEmtreFermeSupp);
    }

    @Override
    public void configurerLongueurPorteAFaux(double longueurPorteAFaux) {
        this.toit.configurerLongueurPorteAFaux(longueurPorteAFaux);
    }

    @Override
    public void configurerDistanceEntreEntremisesToit(double distance) {
        this.toit.configurerDistanceEntreEntremisesToit(distance);
    }

    @Override
    public Toit obtenirConfigurationToit() {
        return ToitFactory.FromToit(this.toit);
    }

    @Override
    public ToitAffichage obtenirAffichageToit() {
        return new ToitAffichage(this.toit);
    }

    @Override
    public ArrayList<Planche> getPlanches() {
        return this.toit.getPlanches();
    }

    @Override
    public void mettreAJourToit(Toit toit) {
        this.toit = toit;
    }

    @Override
    public void mettreAJourToit(double pY) {
        this.toit.MAJOrigineToit(pY);
    }
}