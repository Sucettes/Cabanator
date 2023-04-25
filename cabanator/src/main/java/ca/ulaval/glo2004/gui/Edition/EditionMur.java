package ca.ulaval.glo2004.gui.Edition;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.*;
import ca.ulaval.glo2004.gui.Couleurs;
import ca.ulaval.glo2004.gui.Edition.SousEdition.EditionEntremise;
import ca.ulaval.glo2004.gui.Edition.SousEdition.EditionFenetre;
import ca.ulaval.glo2004.gui.Edition.SousEdition.EditionPorte;
import ca.ulaval.glo2004.gui.PropertiesWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditionMur {
    public MouseListener listenerPortes;
    public MouseListener listenerFenetres;
    public MouseListener listenerEntremises;

    public final EditionPorte editionPorte;
    public final EditionFenetre editionFenetre;
    public final EditionEntremise editionEntremise;

    private final PropertiesWindow propertiesWindow;

    public EditionMur(PropertiesWindow propertiesWindow) {
        this.propertiesWindow = propertiesWindow;

        editionPorte = new EditionPorte(this, propertiesWindow);
        editionFenetre = new EditionFenetre(this, propertiesWindow);
        editionEntremise = new EditionEntremise(this, propertiesWindow);

        listenerPortes = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                App.getControleurCabanon().ajouterPorte(new PointDTO(0, 0, 0), propertiesWindow.positionMurEnEdition, 0, 0);
                mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
                propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            }
        };

        listenerFenetres = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                App.getControleurCabanon().ajouterFenetre(new PointDTO(0, 0, 0), propertiesWindow.positionMurEnEdition, 0, 0);
                mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
                propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            }
        };

        listenerEntremises = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                App.getControleurCabanon().ajouterEntremise(new PointDTO(0, 0, 0), propertiesWindow.positionMurEnEdition);
                mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
                propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
            }
        };
    }

    public void majListener() {
        propertiesWindow.panneauPortes.setVisible(true);
        propertiesWindow.panneauFenetres.setVisible(true);
        propertiesWindow.panneauEntremises.setVisible(true);

        propertiesWindow.textePortes.removeMouseListener(listenerPortes);
        propertiesWindow.texteFenetres.removeMouseListener(listenerFenetres);
        propertiesWindow.texteEntremises.removeMouseListener(listenerEntremises);

        propertiesWindow.textePortes.addMouseListener(listenerPortes);
        propertiesWindow.texteFenetres.addMouseListener(listenerFenetres);
        propertiesWindow.texteEntremises.addMouseListener(listenerEntremises);
    }

    public void mettreAJourListesComposantsMur(PointCardinal positionMur) {
        propertiesWindow.panneauListePortes.removeAll();
        propertiesWindow.panneauListeFenetres.removeAll();
        propertiesWindow.panneauListeEntremises.removeAll();

        final MurDTO mur = App.getControleurCabanon().obtenirConfigurationMur(positionMur);

        int numeroPorte = 1;
        int numeroFenetre = 1;
        int numeroEntremise = 1;

        for (AccessoireDTO accessoire : mur.accessoires) {
            if (accessoire instanceof PorteDTO) {
                final JLabel texte = creerElementListe(propertiesWindow.obtenirTitre("Porte")
                        + " " + numeroPorte + " " + accessoire.position.toString());

                texte.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        editionPorte.afficherProprietesPorte((PorteDTO) accessoire, positionMur);
                        propertiesWindow.panneauDessin.setElementEnModification(accessoire.id);
                    }
                });

                propertiesWindow.panneauListePortes.add(texte);
                numeroPorte++;
            } else if (accessoire instanceof FenetreDTO) {
                final JLabel texte = creerElementListe(propertiesWindow.obtenirTitre("Fenetre")
                        + " " + numeroFenetre + " " + accessoire.position.toString());

                texte.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        editionFenetre.afficherProprietesFenetre((FenetreDTO) accessoire, positionMur);
                        propertiesWindow.panneauDessin.setElementEnModification(accessoire.id);
                    }
                });

                propertiesWindow.panneauListeFenetres.add(texte);
                numeroFenetre++;
            }
        }

        for (EntremiseDTO entremise : mur.entremises) {
            final JLabel texte = creerElementListe(propertiesWindow.obtenirTitre("Entremise")
                    + " " + numeroEntremise + " " + entremise.position.toString());

            texte.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (SwingUtilities.isRightMouseButton(e)) {
                        App.getControleurCabanon().supprimerEntremise(entremise.id, positionMur);
                        propertiesWindow.panneauDessin.regenerateVue(propertiesWindow.vue);
                        mettreAJourListesComposantsMur(propertiesWindow.positionMurEnEdition);
                    }
                    super.mouseClicked(e);
                    editionEntremise.afficherProprietesEntremises(entremise, positionMur);
                    propertiesWindow.panneauDessin.setElementEnModification(entremise.id);
                }
            });

            propertiesWindow.panneauListeEntremises.add(texte);
            numeroEntremise++;
        }

        propertiesWindow.panneauListePortes.revalidate();
        propertiesWindow.panneauListeFenetres.revalidate();
        propertiesWindow.panneauListeEntremises.revalidate();

        propertiesWindow.panneauListePortes.repaint();
        propertiesWindow.panneauListeFenetres.repaint();
        propertiesWindow.panneauListeEntremises.repaint();
    }

    private JLabel creerElementListe(String element) {
        final JLabel texte = new JLabel(element);
        final Font texteFont = propertiesWindow.obtenirPolice("Dubai", Font.PLAIN, 16, texte.getFont());
        if (texteFont != null) texte.setFont(texteFont);
        texte.setForeground(Couleurs.GRIS_FONCE);
        return texte;
    }

    public Object[][] obtenirProprietesMur(PointCardinal pointCardinal) {
        final MurDTO mur = App.getControleurCabanon().obtenirConfigurationMur(pointCardinal);

        return new Object[][]{
                {propertiesWindow.obtenirTitre("Longueur"),
                        mur.longueur.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("Hauteur"),
                        mur.hauteur.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("DistanceEntreMontants"),
                        mur.distanceEntreMontants.getDistanceString()
                }
        };
    }

    public void afficherPanneauxComposantsMur() {
        propertiesWindow.panneauPortes.setVisible(true);
        propertiesWindow.panneauFenetres.setVisible(true);
        propertiesWindow.panneauEntremises.setVisible(true);
    }

    public void cacherPanneauxComposantsMur() {
        propertiesWindow.panneauPortes.setVisible(false);
        propertiesWindow.panneauFenetres.setVisible(false);
        propertiesWindow.panneauEntremises.setVisible(false);
    }

    public enum FonctionnalitePanneauConfigMur {
        CONFIGURER_HAUTEUR,
        CONFIGURER_DISTANCE_ENTRE_MONTANTS;

        public static FonctionnalitePanneauConfigMur obtenirFonctionnalite(int positionLigne) {
            if (positionLigne == 1)
                return CONFIGURER_HAUTEUR;
            else
                return CONFIGURER_DISTANCE_ENTRE_MONTANTS;
        }
    }
}