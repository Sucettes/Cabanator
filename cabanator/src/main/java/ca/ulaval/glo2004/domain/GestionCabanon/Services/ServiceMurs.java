package ca.ulaval.glo2004.domain.GestionCabanon.Services;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.MurAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Fenetre;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Porte;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Mur;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Murs;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Entremise;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Factories.EntremiseFactory;
import ca.ulaval.glo2004.domain.GestionCabanon.Factories.FenetreFactory;
import ca.ulaval.glo2004.domain.GestionCabanon.Factories.MurFactory;
import ca.ulaval.glo2004.domain.GestionCabanon.Factories.PorteFactory;
import ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces.IServiceMurs;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.UUID;

public class ServiceMurs implements IServiceMurs {
    private Murs murs;

    public ServiceMurs(Murs murs) {
        this.murs = murs;
    }

    @Override
    public void configurerHauteur(double hauteur) {
        murs.configurerHauteur(hauteur);
    }

    @Override
    public void configurerLongueur(double longueur) {
        murs.configurerLongueur(longueur);
    }

    @Override
    public void configurerLargeur(double largeur) {
        murs.configurerLargeur(largeur);
    }

    @Override
    public void ajouterEntremise(Point positionCurseur, PointCardinal positionMur) {
        murs.getMur(positionMur).ajouterEntremise(positionCurseur);
    }

    @Override
    public void ajouterEntremiseAccessoire(Point positionCurseur, PointCardinal positionMur, UUID accessoireId) {

    }

    @Override
    public void positionnerEntremise(UUID id, Point positionCurseur, PointCardinal positionMur) {
        murs.getMur(positionMur).positionnerEntremise(id, positionCurseur);
    }

    @Override
    public void positionnerEntremiseAccessoire(UUID id, Point positionCurseur, PointCardinal positionMur, UUID accessoireId) {

    }

    @Override
    public void supprimerEntremise(UUID id, PointCardinal positionMur) {
        murs.getMur(positionMur).supprimerEntremise(id);
    }

    @Override
    public void supprimerEntremiseAccessoire(UUID id, PointCardinal positionMur, UUID accessoireId) {

    }

    @Override
    public void ajouterPorte(Point positionCurseur, PointCardinal positionMur, double longueurTrou, double hauteurTrou) {
        murs.getMur(positionMur).ajouterPorte(positionCurseur, longueurTrou, hauteurTrou);
    }

    @Override
    public void ajouterFenetre(Point positionCurseur, PointCardinal positionMur, double longueurTrou, double hauteurTrou) {
        murs.getMur(positionMur).ajouterFenetre(positionCurseur, longueurTrou, hauteurTrou);
    }

    @Override
    public void positionnerPorte(Point positionCurseur, PointCardinal positionMur, UUID accessoireId) {
        murs.getMur(positionMur).positionnerPorte(accessoireId, positionCurseur);
    }

    @Override
    public void positionnerFenetre(Point positionCurseur, PointCardinal positionMur, UUID accessoireId) {
        murs.getMur(positionMur).positionnerFenetre(accessoireId, positionCurseur);
    }

    @Override
    public void supprimerPorte(UUID id, PointCardinal positionMur) {
        murs.getMur(positionMur).supprimerPorte(id);
    }

    @Override
    public void supprimerFenetre(UUID id, PointCardinal positionMur) {
        murs.getMur(positionMur).supprimerFenetre(id);
    }

    @Override
    public void configurerDimensionsAccessoire(double longueur, double largeur, UUID accessoireId, PointCardinal positionMur) {
        murs.getMur(positionMur).configurerDimensionsAccessoire(accessoireId, longueur, largeur);
    }

    @Override
    public void configurerTypeLinteauPorte(UUID id, TypePlanche type, PointCardinal positionMur) {
        murs.getMur(positionMur).configurerTypeLinteauPorte(id, type);
    }

    @Override
    public void configurerTypeLinteauFenetre(UUID id, TypePlanche type, PointCardinal positionMur) {
        murs.getMur(positionMur).configurerTypeLinteauFenetre(id, type);
    }

    @Override
    public void configurerDistanceEntreMontantsPorte(UUID id, double distance, PointCardinal positionMur) {
        murs.getMur(positionMur).configurerDistanceEntreMontantsPorte(id, distance);
    }

    @Override
    public void configurerDistanceEntreMontantsFenetre(UUID id, double distance, PointCardinal positionMur) {
        murs.getMur(positionMur).configurerDistanceEntreMontantsFenetre(id, distance);
    }

    @Override
    public Mur obtenirConfigurationMur(PointCardinal pointCardinal) {
        return MurFactory.FromMur(this.murs.getMur(pointCardinal));
    }

    @Override
    public Porte obtenirConfigurationPorte(UUID id, PointCardinal positionMur) {
        return PorteFactory.FromPorte((Porte) this.murs.getMur(positionMur).obtenirAccessoire(id));
    }

    @Override
    public Fenetre obtenirConfigurationFenetre(UUID id, PointCardinal positionMur) {
        return FenetreFactory.FromFenetre((Fenetre) this.murs.getMur(positionMur).obtenirAccessoire(id));
    }

    @Override
    public MurAffichage obtenirAffichageMur(PointCardinal positionMur) {
        return new MurAffichage(this.murs.getMur(positionMur));
    }

    public Entremise obtenirConfigurationEntremise(UUID id, PointCardinal positionMur) {
        return EntremiseFactory.FromEntremise((Entremise) this.murs.getMur(positionMur).obtenirEntremise(id));
    }

    @Override
    public void configurerDistanceEntreMontantsMurs(double distance, PointCardinal positionMur) {
        murs.getMur(positionMur).configurerDistanceEntreMontants(distance);
    }

    @Override
    public void mettreAJourMurs(Murs murs) {
        this.murs = murs;
    }
}