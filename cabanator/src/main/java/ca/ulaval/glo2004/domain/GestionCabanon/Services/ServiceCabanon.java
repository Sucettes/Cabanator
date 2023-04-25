package ca.ulaval.glo2004.domain.GestionCabanon.Services;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.CabanonAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancheAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Cabanon;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.CoutTypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypeSauvegarde;
import ca.ulaval.glo2004.domain.GestionCabanon.Factories.CabanonFactory;
import ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces.IServiceCabanon;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.GestionFichiersCabanon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class ServiceCabanon implements IServiceCabanon {
    private Cabanon cabanonCourant;
    private Stack<Cabanon> cabanonsSuivants = new Stack<>();
    private Stack<Cabanon> cabanonsPrecedents = new Stack<>();

    @Override
    public Cabanon creerProjet(Dimension3D dimension3D) {
        this.cabanonCourant = new Cabanon(dimension3D);
        return this.cabanonCourant;
    }

    @Override
    public Cabanon ouvrirProjet(String cheminFichier) {
        try {
            Object[] serviceInfos = GestionFichiersCabanon.lireProjet(cheminFichier);
            this.cabanonCourant = (Cabanon) serviceInfos[0];
            this.cabanonsPrecedents = (Stack<Cabanon>) serviceInfos[1];
            this.cabanonsSuivants = (Stack<Cabanon>) serviceInfos[2];
            return this.obtenirCabanonCourant();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean enregistrerProjet(String cheminFichier, String nomProjet, String versionSauvegarde, boolean renommerVersionsProjet, TypeSauvegarde typeSauvegarde) {
        try {
            return GestionFichiersCabanon.enregistrerProjet(cheminFichier, nomProjet, versionSauvegarde, this.cabanonCourant, renommerVersionsProjet, typeSauvegarde, this.cabanonsPrecedents, this.cabanonsSuivants);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exporterPiecesProjet(String cheminFichier) {
        try {
            return GestionFichiersCabanon.exporterProjet(cheminFichier, this.obtenirCabanonCourant());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double calculerCout() {
        double cout = 0;
        ArrayList<PlancheAffichage> planches = obtenirAffichageCabanon().getPlanches();
        for (PlancheAffichage p : planches) {
            if (p.getPositionEstValide()) {
                cout += (CoutTypePlanche.getCoutPiedPlanche(p.getType()) * (p.getLongueur() / 12));
            }
        }
        return cout;
    }

    @Override
    public void configurerCoutPlanche(TypePlanche type, double cout) {
        CoutTypePlanche.setCoutPiedPlanche(type, cout);
    }

    @Override
    public void configurerLimitePlanche(TypePlanche type, double limite) {
        TypePlanche.setLimitePlanche(type, limite);
        this.obtenirCabanonCourant().regenererStructure();
    }

    @Override
    public Cabanon obtenirConfigurationCabanon() {
        return CabanonFactory.FromCabanon(this.obtenirCabanonCourant());
    }

    @Override
    public CabanonAffichage obtenirAffichageCabanon() {
        return new CabanonAffichage(this.obtenirCabanonCourant());
    }

    @Override
    public void mettreAJourCabanon(Cabanon cabanon) {
        this.cabanonCourant = cabanon;
    }

    @Override
    public Cabanon obtenirCabanonCourant() {
        return this.cabanonCourant;
    }

    @Override
    public void annulerActionCabanon() {
        if (this.cabanonsPrecedents.empty()) return;
        this.cabanonsSuivants.push(this.obtenirCabanonCourant().cloner());
        this.cabanonCourant = this.cabanonsPrecedents.pop();
    }

    @Override
    public void retablirActionCabanon() {
        if (cabanonsSuivants.empty()) return;
        this.cabanonsPrecedents.push(this.obtenirCabanonCourant().cloner());
        this.cabanonCourant = this.cabanonsSuivants.pop();
    }

    @Override
    public void sauvegarderInstanceCourante() {
        this.cabanonsPrecedents.push(this.cabanonCourant.cloner());
        this.cabanonsSuivants.clear();
    }
}