package ca.ulaval.glo2004.gui.Edition.SousEdition;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.EntremiseDTO;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.FenetreDTO;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.PointDTO;
import ca.ulaval.glo2004.gui.Composant;
import ca.ulaval.glo2004.gui.Edition.EditionMur;
import ca.ulaval.glo2004.gui.PropertiesWindow;
import ca.ulaval.glo2004.gui.PropertiesWindow.FonctionnalitePanneauConfigAccessoire;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.Optional;
import java.util.UUID;

import static ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale.convertirStringEnDouble;
import static ca.ulaval.glo2004.gui.Tableau.TableauDeuxColonnes.obtenirDonnee;

public class EditionEntremise {
    private EditionMur editionMur;
    private PropertiesWindow propertiesWindow;

    public EditionEntremise(EditionMur editionMur, PropertiesWindow propertiesWindow) {
        this.editionMur = editionMur;
        this.propertiesWindow = propertiesWindow;
    }

    private final TableModelListener listenerProprietesEntremise = e -> {
        if (e.getType() == TableModelEvent.UPDATE) {
            final TableModel donnees = (TableModel) e.getSource();

            final Optional<String> donneeLigne0 = obtenirDonnee(donnees, 0);
            final Optional<String> donneeLigne1 = obtenirDonnee(donnees, 1);

            if (donneeLigne0.isEmpty() || donneeLigne1.isEmpty()) return;

            final PointDTO anciennePosition = App.getControleurCabanon().obtenirConfigurationEntremise(
                    propertiesWindow.idComposantEnEdition, propertiesWindow.positionMurEnEdition).position;

            final PointDTO nouvellePosition = new PointDTO(
                    convertirStringEnDouble(donneeLigne0.get()),
                    convertirStringEnDouble(donneeLigne1.get()),
                    anciennePosition.z.getDistanceDouble());

            switch (FonctionnalitePanneauConfigAccessoire.obtenirFonctionnalite(e.getFirstRow())) {
                case CONFIGURER_POSITION, CONFIGURER_LARGEUR -> App.getControleurCabanon().positionnerEntremise(nouvellePosition,
                        propertiesWindow.positionMurEnEdition, propertiesWindow.idComposantEnEdition);
            }

            propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            editionMur.mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
        }
    };

    public final MouseListener listenerSupprimerEntremise = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            App.getControleurCabanon().supprimerEntremise(propertiesWindow.idComposantEnEdition, propertiesWindow.positionMurEnEdition);
            propertiesWindow.tableauProprietes.getModel().removeTableModelListener(listenerProprietesEntremise);
            propertiesWindow.cacherEditionAccessoire();

            propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            editionMur.mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
        }
    };


    public void afficherProprietesEntremises(EntremiseDTO entremise, PointCardinal positionMur) {
        propertiesWindow.composantEnEdition = Composant.ENTREMISE;
        propertiesWindow.texteProprietes.setText(propertiesWindow.obtenirTitre("ProprietesEntremise"));
        propertiesWindow.tableauProprietes.creerModele(obtenirProprietesEntremise(entremise.id, positionMur));
        propertiesWindow.boutonSupprimer.addMouseListener(listenerSupprimerEntremise);

        propertiesWindow.afficherProprietesEntremise(entremise.id);

        propertiesWindow.tableauProprietes.definirLignesModifiables(new int[]{0, 1, /*2, 3, 4*/});
        propertiesWindow.tableauProprietes.getModel().addTableModelListener(listenerProprietesEntremise);
    }


    private Object[][] obtenirProprietesEntremise(UUID idEntremise, PointCardinal positionMur) {
        final EntremiseDTO entremise = App.getControleurCabanon().obtenirConfigurationEntremise(idEntremise, positionMur);

        return new Object[][]{
                {propertiesWindow.obtenirTitre("PositionX"),
                        entremise.position.x.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("PositionY"),
                        entremise.position.y.getDistanceString()
                },
        };
    }
}