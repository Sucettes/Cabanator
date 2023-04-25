package ca.ulaval.glo2004.gui.Edition.SousEdition;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.LigneEntremisesDTO;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.PlancherDTO;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.PointDTO;
import ca.ulaval.glo2004.gui.Composant;
import ca.ulaval.glo2004.gui.Couleurs;
import ca.ulaval.glo2004.gui.PropertiesWindow;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;
import java.util.UUID;

import static ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale.convertirStringEnDouble;
import static ca.ulaval.glo2004.gui.Tableau.TableauDeuxColonnes.obtenirDonnee;

public class EditionLigneEntremises {
    public MouseListener listenerLignesEntremises;

    private PropertiesWindow propertiesWindow;

    private boolean listenersAjoutes;

    public EditionLigneEntremises(PropertiesWindow propertiesWindow) {
        this.propertiesWindow = propertiesWindow;
    }

    private final TableModelListener listenerProprietesLignesEntremises = e -> {
        if (e.getType() == TableModelEvent.UPDATE) {
            final TableModel donnees = (TableModel) e.getSource();

            final Optional<String> donneeLigne0 = obtenirDonnee(donnees, 0);
            final Optional<String> donneeLigne1 = obtenirDonnee(donnees, 1);

            if (donneeLigne0.isEmpty() || donneeLigne1.isEmpty())
                return;

            final PointDTO anciennePosition = App.getControleurCabanon()
                    .obtenirConfigurationLigneEntremises(propertiesWindow.idComposantEnEdition).centre;

            final PointDTO nouvellePosition = new PointDTO(
                    anciennePosition.x.getDistanceDouble(),
                    anciennePosition.y.getDistanceDouble(),
                    convertirStringEnDouble(donneeLigne0.get()));

            App.getControleurCabanon().positionnerLigneEntremises(
                    propertiesWindow.idComposantEnEdition, nouvellePosition);

            App.getControleurCabanon().configurerDecalageEntreEntremises(
                    propertiesWindow.idComposantEnEdition, convertirStringEnDouble(donneeLigne1.get()));

            propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            mettreAJourListeLignesEntremises();
        }
    };

    public void cacherEditionLignesEntremises() {
        propertiesWindow.tableauProprietes.getModel().removeTableModelListener(listenerProprietesLignesEntremises);
        propertiesWindow.boutonSupprimer.removeMouseListener(listenerSupprimerLignesEntremises);
        propertiesWindow.retournerVueProprietes();
    }

    private final MouseListener listenerSupprimerLignesEntremises = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            App.getControleurCabanon().supprimerLigneEntremises(propertiesWindow.idComposantEnEdition);
            cacherEditionLignesEntremises();
            mettreAJourListeLignesEntremises();
            propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
        }
    };

    public void ajouterListenerLignesEntremises() {
        if (listenersAjoutes) return;
        listenersAjoutes = true;

        propertiesWindow.panneauLignesEntremises.setVisible(true);

        listenerLignesEntremises = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                App.getControleurCabanon().ajouterLigneEntremises(new PointDTO(0, 0, 0), 0);
                mettreAJourListeLignesEntremises();
                propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            }
        };

        propertiesWindow.texteLignesEntremises.addMouseListener(listenerLignesEntremises);
    }

    public void mettreAJourListeLignesEntremises() {
        propertiesWindow.panneauListeLignesEntremises.removeAll();

        final PlancherDTO plancher = App.getControleurCabanon().obtenirConfigurationPlancher();

        int numeroLigneEntremises = 1;

        for (LigneEntremisesDTO ligneEntremise : plancher.ligneEntremises) {
            final JLabel texte = creerElementListe(propertiesWindow.obtenirTitre("LigneEntremises")
                    + " " + numeroLigneEntremises + " " + ligneEntremise.centre.toString());

            texte.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    afficherProprietesLignesEntremises(ligneEntremise.id);
                    propertiesWindow.panneauDessin.setElementEnModification(ligneEntremise.id);
                }
            });

            propertiesWindow.panneauListeLignesEntremises.add(texte);
            numeroLigneEntremises++;
        }

        propertiesWindow.panneauListeLignesEntremises.revalidate();
        propertiesWindow.panneauListeLignesEntremises.repaint();
    }

    private JLabel creerElementListe(String element) {
        final JLabel texte = new JLabel(element);
        final Font texteFont = propertiesWindow.obtenirPolice("Dubai", Font.PLAIN, 16, texte.getFont());
        if (texteFont != null) texte.setFont(texteFont);
        texte.setForeground(Couleurs.GRIS_FONCE);
        return texte;
    }

    private void afficherProprietesLignesEntremises(UUID idLignesEntremises) {
        propertiesWindow.composantEnEdition = Composant.LIGNE_ENTREMISES;
        propertiesWindow.texteProprietes.setText(propertiesWindow.obtenirTitre("ProprietesLigneEntremises"));
        propertiesWindow.tableauProprietes.creerModele(obtenirProprietesLignesEntremises(idLignesEntremises));
        propertiesWindow.boutonSupprimer.addMouseListener(listenerSupprimerLignesEntremises);

        propertiesWindow.idComposantEnEdition = idLignesEntremises;
        propertiesWindow.proprietesComposantAffichees = true;
        propertiesWindow.panneauAccessoires.setVisible(false);
        propertiesWindow.boutonSupprimer.setVisible(true);

        propertiesWindow.texteRetour.setText(propertiesWindow.obtenirTitre("RetourProprietesPlancher"));

        propertiesWindow.tableauProprietes.definirLignesModifiables(new int[]{0, 1});
        propertiesWindow.tableauProprietes.getModel().removeTableModelListener(propertiesWindow.listenerProprietes);
        propertiesWindow.tableauProprietes.getModel().addTableModelListener(listenerProprietesLignesEntremises);
    }

    private Object[][] obtenirProprietesLignesEntremises(UUID idLignesEntremises) {
        final LigneEntremisesDTO ligneEntremises = App.getControleurCabanon().obtenirConfigurationLigneEntremises(idLignesEntremises);
        return new Object[][]{
                {propertiesWindow.obtenirTitre("Position"), ligneEntremises.centre.z.getDistanceString()},
                {propertiesWindow.obtenirTitre("DistanceEntreEntremises"), ligneEntremises.decalageEntreEntremises.getDistanceString()}
        };
    }
}