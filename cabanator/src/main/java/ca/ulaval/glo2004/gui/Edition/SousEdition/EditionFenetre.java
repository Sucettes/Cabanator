package ca.ulaval.glo2004.gui.Edition.SousEdition;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
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

public class EditionFenetre {
    private EditionMur editionMur;
    private PropertiesWindow propertiesWindow;

    public EditionFenetre(EditionMur editionMur, PropertiesWindow propertiesWindow) {
        this.editionMur = editionMur;
        this.propertiesWindow = propertiesWindow;
    }

    private final TableModelListener listenerProprietesFenetre = e -> {
        if (e.getType() == TableModelEvent.UPDATE) {
            final TableModel donnees = (TableModel) e.getSource();

            final Optional<String> donneeLigne0 = obtenirDonnee(donnees, 0);
            final Optional<String> donneeLigne1 = obtenirDonnee(donnees, 1);
            final Optional<String> donneeLigne2 = obtenirDonnee(donnees, 2);
            final Optional<String> donneeLigne3 = obtenirDonnee(donnees, 3);
            final Optional<String> donneeLigne4 = obtenirDonnee(donnees, 4);

            if (donneeLigne0.isEmpty() || donneeLigne1.isEmpty() || donneeLigne2.isEmpty()
                    || donneeLigne3.isEmpty() || donneeLigne4.isEmpty()) return;

            final PointDTO anciennePosition = App.getControleurCabanon().obtenirConfigurationFenetre(
                    propertiesWindow.idComposantEnEdition, propertiesWindow.positionMurEnEdition).position;

            final PointDTO nouvellePosition = new PointDTO(
                    convertirStringEnDouble(donneeLigne0.get()),
                    convertirStringEnDouble(donneeLigne1.get()),
                    anciennePosition.z.getDistanceDouble());

            switch (FonctionnalitePanneauConfigAccessoire.obtenirFonctionnalite(e.getFirstRow())) {
                case CONFIGURER_POSITION -> App.getControleurCabanon().positionnerFenetre(nouvellePosition,
                        propertiesWindow.positionMurEnEdition, propertiesWindow.idComposantEnEdition);
                /*case CONFIGURER_DISTANCE_ENTRE_MONTANTS -> App.getControleurCabanon().configurerDistanceEntreMontantsFenetre(
                        propertiesWindow.idComposantEnEdition,
                        convertirStringEnDouble(donneeLigne4.get()),
                        propertiesWindow.positionMurEnEdition);
                case CONFIGURER_HAUTEUR, CONFIGURER_LARGEUR -> App.getControleurCabanon().configurerDimensionsAccessoire(
                        propertiesWindow.idComposantEnEdition,
                        convertirStringEnDouble(donneeLigne2.get()),
                        convertirStringEnDouble(donneeLigne3.get()),
                        propertiesWindow.positionMurEnEdition);*/
            }
            App.getControleurCabanon().configurerDistanceEntreMontantsFenetre(
                    propertiesWindow.idComposantEnEdition,
                    convertirStringEnDouble(donneeLigne4.get()),
                    propertiesWindow.positionMurEnEdition);
            App.getControleurCabanon().configurerDimensionsAccessoire(
                    propertiesWindow.idComposantEnEdition,
                    convertirStringEnDouble(donneeLigne2.get()),
                    convertirStringEnDouble(donneeLigne3.get()),
                    propertiesWindow.positionMurEnEdition);
            if (nouvellePosition != anciennePosition) {
                App.getControleurCabanon().positionnerFenetre(nouvellePosition,
                        propertiesWindow.positionMurEnEdition, propertiesWindow.idComposantEnEdition);
            }

            propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            editionMur.mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
        }
    };

    public final MouseListener listenerSupprimerFenetre = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            App.getControleurCabanon().supprimerFenetre(propertiesWindow.idComposantEnEdition, propertiesWindow.positionMurEnEdition);
            propertiesWindow.tableauProprietes.getModel().removeTableModelListener(listenerProprietesFenetre);
            propertiesWindow.cacherEditionAccessoire();

            propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            editionMur.mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
        }
    };

    public final ActionListener listenerTypeLinteauFenetre = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            App.getControleurCabanon().configurerTypeLinteauFenetre(propertiesWindow.idComposantEnEdition,
                    (TypePlanche) propertiesWindow.selectionTypeLinteau.getSelectedItem(), propertiesWindow.positionMurEnEdition);

            propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            editionMur.mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
        }
    };

    public void afficherProprietesFenetre(FenetreDTO fenetre, PointCardinal positionMur) {
        propertiesWindow.composantEnEdition = Composant.FENETRE;
        propertiesWindow.texteProprietes.setText(propertiesWindow.obtenirTitre("ProprietesFenetre"));
        propertiesWindow.tableauProprietes.creerModele(obtenirProprietesFenetre(fenetre.id, positionMur));
        propertiesWindow.boutonSupprimer.addMouseListener(listenerSupprimerFenetre);

        final Optional<Integer> ordinalTypePlanche = TypePlanche.obtenirOrdinalTypePlanche(fenetre.typeLinteau);
        if (ordinalTypePlanche.isPresent()) {
            propertiesWindow.selectionTypeLinteau.setSelectedIndex(ordinalTypePlanche.get() - 1);
            propertiesWindow.selectionTypeLinteau.addActionListener(listenerTypeLinteauFenetre);
        }

        propertiesWindow.afficherProprietesAccessoire(fenetre.id);

        propertiesWindow.tableauProprietes.definirLignesModifiables(new int[]{0, 1, 2, 3, 4});
        propertiesWindow.tableauProprietes.getModel().addTableModelListener(listenerProprietesFenetre);
    }

    private Object[][] obtenirProprietesFenetre(UUID idFenetre, PointCardinal positionMur) {
        final FenetreDTO fenetre = App.getControleurCabanon().obtenirConfigurationFenetre(idFenetre, positionMur);

        return new Object[][]{
                {propertiesWindow.obtenirTitre("PositionX"),
                        fenetre.position.x.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("PositionY"),
                        fenetre.position.y.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("Largeur"),
                        fenetre.largeur.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("Hauteur"),
                        fenetre.hauteur.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("DistanceEntreMontants"),
                        fenetre.distanceEntreMontants.getDistanceString()
                }
        };
    }
}