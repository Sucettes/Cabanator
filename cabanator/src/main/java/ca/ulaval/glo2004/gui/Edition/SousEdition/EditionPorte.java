package ca.ulaval.glo2004.gui.Edition.SousEdition;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.PointDTO;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.PorteDTO;
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

public class EditionPorte {
    private EditionMur editionMur;
    private PropertiesWindow propertiesWindow;

    public EditionPorte(EditionMur editionMur, PropertiesWindow propertiesWindow) {
        this.editionMur = editionMur;
        this.propertiesWindow = propertiesWindow;
    }

    private final TableModelListener listenerProprietesPorte = e -> {
        if (e.getType() == TableModelEvent.UPDATE) {
            final TableModel donnees = (TableModel) e.getSource();

            final Optional<String> donneeLigne0 = obtenirDonnee(donnees, 0);
            final Optional<String> donneeLigne1 = obtenirDonnee(donnees, 1);
            final Optional<String> donneeLigne2 = obtenirDonnee(donnees, 2);
            final Optional<String> donneeLigne3 = obtenirDonnee(donnees, 3);

            if (donneeLigne0.isEmpty() || donneeLigne1.isEmpty() || donneeLigne2.isEmpty() || donneeLigne3.isEmpty())
                return;

            final PointDTO anciennePosition = App.getControleurCabanon().obtenirConfigurationPorte(
                    propertiesWindow.idComposantEnEdition, propertiesWindow.positionMurEnEdition).position;

            final PointDTO nouvellePosition = new PointDTO(
                    convertirStringEnDouble(donneeLigne0.get()),
                    anciennePosition.y.getDistanceDouble(),
                    anciennePosition.z.getDistanceDouble());

            switch (FonctionnalitePanneauConfigAccessoire.obtenirFonctionnalite(e.getFirstRow())) {
                case CONFIGURER_POSITION -> App.getControleurCabanon().positionnerPorte(
                        nouvellePosition, propertiesWindow.positionMurEnEdition, propertiesWindow.idComposantEnEdition);
                case CONFIGURER_DISTANCE_ENTRE_MONTANTS -> App.getControleurCabanon().configurerDistanceEntreMontantsPorte(
                        propertiesWindow.idComposantEnEdition,
                        convertirStringEnDouble(donneeLigne3.get()),
                        propertiesWindow.positionMurEnEdition);
                case CONFIGURER_HAUTEUR, CONFIGURER_LARGEUR ->
                        App.getControleurCabanon().configurerDimensionsAccessoire(
                        propertiesWindow.idComposantEnEdition,
                        convertirStringEnDouble(donneeLigne1.get()),
                        convertirStringEnDouble(donneeLigne2.get()),
                        propertiesWindow.positionMurEnEdition);
            }

            propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            editionMur.mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
        }
    };

    public final MouseListener listenerSupprimerPorte = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            App.getControleurCabanon().supprimerPorte(propertiesWindow.idComposantEnEdition, propertiesWindow.positionMurEnEdition);
            propertiesWindow.tableauProprietes.getModel().removeTableModelListener(listenerProprietesPorte);
            propertiesWindow.cacherEditionAccessoire();

            propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            editionMur.mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
        }
    };

    public final ActionListener listenerTypeLinteauPorte = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            App.getControleurCabanon().configurerTypeLinteauPorte(propertiesWindow.idComposantEnEdition,
                    (TypePlanche) propertiesWindow.selectionTypeLinteau.getSelectedItem(), propertiesWindow.positionMurEnEdition);

            propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            editionMur.mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
        }
    };

    public void afficherProprietesPorte(PorteDTO porte, PointCardinal positionMur) {
        propertiesWindow.composantEnEdition = Composant.PORTE;
        propertiesWindow.texteProprietes.setText(propertiesWindow.obtenirTitre("ProprietesPorte"));
        propertiesWindow.tableauProprietes.creerModele(obtenirProprietesPorte(porte.id, positionMur));
        propertiesWindow.boutonSupprimer.addMouseListener(listenerSupprimerPorte);

        final Optional<Integer> ordinalTypePlanche = TypePlanche.obtenirOrdinalTypePlanche(porte.typeLinteau);
        if (ordinalTypePlanche.isPresent()) {
            propertiesWindow.selectionTypeLinteau.setSelectedIndex(ordinalTypePlanche.get() - 1);
            propertiesWindow.selectionTypeLinteau.addActionListener(listenerTypeLinteauPorte);
        }

        propertiesWindow.afficherProprietesAccessoire(porte.id);

        propertiesWindow.tableauProprietes.definirLignesModifiables(new int[]{0, 1, 2, 3});
        propertiesWindow.tableauProprietes.getModel().addTableModelListener(listenerProprietesPorte);
    }

    private Object[][] obtenirProprietesPorte(UUID idPorte, PointCardinal positionMur) {
        final PorteDTO porte = App.getControleurCabanon().obtenirConfigurationPorte(idPorte, positionMur);

        return new Object[][]{
                {propertiesWindow.obtenirTitre("Position"),
                        porte.position.x.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("Largeur"),
                        porte.largeur.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("Hauteur"),
                        porte.hauteur.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("DistanceEntreMontants"),
                        porte.distanceEntreMontants.getDistanceString()
                }
        };
    }
}