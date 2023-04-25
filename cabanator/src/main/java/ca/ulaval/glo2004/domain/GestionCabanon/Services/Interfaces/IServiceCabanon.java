package ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.CabanonAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Cabanon;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypeSauvegarde;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;

import java.util.Stack;

public interface IServiceCabanon {
    Cabanon creerProjet(Dimension3D dimension3D);

    Cabanon ouvrirProjet(String cheminFichier);

    boolean enregistrerProjet(String cheminFichier, String nomProjet, String versionSauvegarde, boolean renommerVersionsProjet, TypeSauvegarde typeSauvegarde);

    boolean exporterPiecesProjet(String cheminFichier);

    double calculerCout();

    void configurerCoutPlanche(TypePlanche type, double cout);

    void configurerLimitePlanche(TypePlanche type, double limite);

    Cabanon obtenirConfigurationCabanon();

    CabanonAffichage obtenirAffichageCabanon();

    void mettreAJourCabanon(Cabanon cabanon);

    Cabanon obtenirCabanonCourant();

    void annulerActionCabanon();

    void retablirActionCabanon();

    void sauvegarderInstanceCourante();
}