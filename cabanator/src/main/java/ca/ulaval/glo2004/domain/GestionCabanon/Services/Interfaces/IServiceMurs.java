package ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.MurAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Fenetre;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Porte;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Mur;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Murs;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.UUID;

public interface IServiceMurs {
    void configurerHauteur(double hauteur);

    void configurerLongueur(double longueur);

    void configurerLargeur(double largeur);

    void ajouterEntremise(Point positionCurseur, PointCardinal positionMur);

    void ajouterEntremiseAccessoire(Point positionCurseur, PointCardinal positionMur, UUID accessoireId);

    void positionnerEntremise(UUID id, Point positionCurseur, PointCardinal positionMur);

    void positionnerEntremiseAccessoire(UUID id, Point positionCurseur, PointCardinal positionMur, UUID accessoireId);

    void supprimerEntremise(UUID id, PointCardinal positionMur);

    void supprimerEntremiseAccessoire(UUID id, PointCardinal positionMur, UUID accessoireId);

    void ajouterPorte(Point positionCurseur, PointCardinal positionMur, double longueurTrou, double hauteurTrou);

    void ajouterFenetre(Point positionCurseur, PointCardinal positionMur, double longueurTrou, double hauteurTrou);

    void positionnerPorte(Point positionCurseur, PointCardinal positionMur, UUID accessoireId);

    void positionnerFenetre(Point positionCurseur, PointCardinal positionMur, UUID accessoireId);

    void supprimerPorte(UUID id, PointCardinal positionMur);

    void supprimerFenetre(UUID id, PointCardinal positionMur);

    void configurerDimensionsAccessoire(double longueur, double largeur, UUID accessoireId, PointCardinal positionMur);

    void configurerTypeLinteauPorte(UUID id, TypePlanche type, PointCardinal positionMur);

    void configurerTypeLinteauFenetre(UUID id, TypePlanche type, PointCardinal positionMur);

    void configurerDistanceEntreMontantsPorte(UUID id, double distance, PointCardinal positionMur);

    void configurerDistanceEntreMontantsFenetre(UUID id, double distance, PointCardinal positionMur);

    Mur obtenirConfigurationMur(PointCardinal pointCardinal);

    Porte obtenirConfigurationPorte(UUID id, PointCardinal positionMur);

    Fenetre obtenirConfigurationFenetre(UUID id, PointCardinal positionMur);

    MurAffichage obtenirAffichageMur(PointCardinal positionMur);

    void configurerDistanceEntreMontantsMurs(double distance, PointCardinal positionMur);

    void mettreAJourMurs(Murs murs);
}