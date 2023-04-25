package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.gui.Edition.EditionConfigMateriaux;
import ca.ulaval.glo2004.gui.Edition.EditionConfigMateriaux.FonctionnalitePanneauConfigMateriaux;
import ca.ulaval.glo2004.gui.Edition.EditionMur;
import ca.ulaval.glo2004.gui.Edition.EditionMur.FonctionnalitePanneauConfigMur;
import ca.ulaval.glo2004.gui.Edition.EditionPlancher;
import ca.ulaval.glo2004.gui.Edition.EditionPlancher.FonctionnalitePanneauConfigPlancher;
import ca.ulaval.glo2004.gui.Edition.EditionToit;
import ca.ulaval.glo2004.gui.Edition.EditionToit.FonctionnalitePanneauConfigToit;
import ca.ulaval.glo2004.gui.Tableau.Champ;
import ca.ulaval.glo2004.gui.Tableau.TableauDeuxColonnes;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.TableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.*;

import static ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale.convertirStringEnDouble;
import static ca.ulaval.glo2004.gui.Tableau.ChampAngle.obtenirAngle;
import static ca.ulaval.glo2004.gui.Tableau.ChampArgent.obtenirArgent;
import static ca.ulaval.glo2004.gui.Tableau.TableauDeuxColonnes.obtenirDonnee;

public class PropertiesWindow {
    private final EditionPlancher editionPlancher = new EditionPlancher(this);
    private final EditionMur editionMur = new EditionMur(this);
    private final EditionToit editionToit = new EditionToit(this);
    private final EditionConfigMateriaux editionConfigMateriaux = new EditionConfigMateriaux(this);
    public JPanel panneauPrincipal;
    public PanneauDessin panneauDessin;
    public JLabel texteRetour;
    public JLabel texteProprietes;
    public TableauDeuxColonnes tableauProprietes;
    public JLabel textePortes;
    public JLabel texteFenetres;
    public JPanel panneauAccessoires;
    public JPanel panneauPortes;
    public JPanel panneauFenetres;
    public JPanel panneauEntremises;
    public JLabel texteEntremises;
    public JPanel panneauListeEntremises;
    public JPanel panneauListeFenetres;
    public JPanel panneauListePortes;
    public JButton boutonSupprimer;
    public JComboBox selectionTypeLinteau;
    public JPanel panneauLignesEntremises;
    public JLabel texteLignesEntremises;
    public JPanel panneauListeLignesEntremises;
    public Vue vue;
    public UUID idComposantEnEdition;
    public Composant composantEnEdition;
    public PointCardinal positionMurEnEdition;
    public boolean proprietesComposantAffichees;
    private JPanel panneauConfiguration;
    private JPanel panneauVue;
    private JPanel panneauProprietes;
    private JPanel panneauEntete;
    private JLabel texteVue;
    private JPanel panneauEdition;
    private JScrollPane listePortes;
    private JScrollPane listeFenetres;
    private JScrollPane listeEntremises;
    private JPanel panneauCentral;
    private JPanel panneauTypeLinteau;
    private JLabel texteTypeLinteau;
    private JScrollPane listeLignesEntremises;
    private JLabel texteSupprimerEntremises;
    private JButton boutonVueFaceToit;
    private JButton boutonVueDessusToit;
    private JPanel panneauBoutons;

    public PropertiesWindow() {
        $$$setupUI$$$();

        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addAll(Arrays.stream(TypePlanche.values()).toList().subList(1, TypePlanche.values().length));
        selectionTypeLinteau.setModel(defaultComboBoxModel1);
        texteRetour.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (proprietesComposantAffichees) {
                    switch (composantEnEdition) {
                        case PORTE, FENETRE, ENTREMISE -> cacherEditionAccessoire();
                        case LIGNE_ENTREMISES -> editionPlancher.editionLigneEntremises.cacherEditionLignesEntremises();
                    }

                } else {
                    vue = null;
                    positionMurEnEdition = null;
                    textePortes.removeMouseListener(editionMur.listenerPortes);
                    texteFenetres.removeMouseListener(editionMur.listenerFenetres);
                    texteEntremises.removeMouseListener(editionMur.listenerEntremises);
                    texteLignesEntremises.removeMouseListener(
                            editionPlancher.editionLigneEntremises.listenerLignesEntremises);
                    tableauProprietes.getModel().removeTableModelListener(listenerProprietes);
                    Navigateur.retourFenetrePrincipale();
                }
            }
        });

        texteRetour.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        textePortes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        texteFenetres.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        texteEntremises.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        texteLignesEntremises.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        boutonVueDessusToit.addActionListener(e -> {
            if (vue == Vue.TOIT) {
                panneauDessin.requestFocusInWindow();
                panneauDessin.changerVueToit(false);
            }
        });

        boutonVueFaceToit.addActionListener(e -> {
            if (vue == Vue.TOIT) {
                panneauDessin.requestFocusInWindow();
                panneauDessin.changerVueToit(true);
            }
        });
    }

    public final TableModelListener listenerProprietes = e -> {
        if (e.getType() == TableModelEvent.UPDATE) {
            final TableModel donnees = (TableModel) e.getSource();

            final Optional<String> donneeLigne0 = obtenirDonnee(donnees, 0);
            final Optional<String> donneeLigne1 = obtenirDonnee(donnees, 1);
            final Optional<String> donneeLigne2 = obtenirDonnee(donnees, 2);
            final Optional<String> donneeLigne3 = obtenirDonnee(donnees, 3);
            final Optional<String> donneeLigne4 = obtenirDonnee(donnees, 4);
            final Optional<String> donneeLigne5 = obtenirDonnee(donnees, 5);
            final Optional<String> donneeLigne6 = obtenirDonnee(donnees, 6);
            final Optional<String> donneeLigne7 = obtenirDonnee(donnees, 7);

            switch (vue) {
                case PLANCHER -> {
                    if (donneeLigne0.isEmpty() || donneeLigne1.isEmpty() || donneeLigne2.isEmpty())
                        return;

                    switch (FonctionnalitePanneauConfigPlancher.obtenirFonctionnalite(e.getFirstRow())) {
                        case CONFIGURER_LONGUEUR ->
                                App.getControleurCabanon().configurerLongueur(convertirStringEnDouble(donneeLigne0.get()));
                        case CONFIGURER_LARGEUR ->
                                App.getControleurCabanon().configurerLargeur(convertirStringEnDouble(donneeLigne1.get()));
                        case CONFIGURER_DISTANCE_ENTRE_SOLIVES ->
                                App.getControleurCabanon().configurerDistanceEntreSolivesPlancher(
                                        convertirStringEnDouble(donneeLigne2.get()));
                    }

                    editionPlancher.editionLigneEntremises.mettreAJourListeLignesEntremises();
                }
                case MUR_NORD, MUR_SUD, MUR_EST, MUR_OUEST -> {
                    if (donneeLigne1.isEmpty() || donneeLigne2.isEmpty()) return;

                    final FonctionnalitePanneauConfigMur fonctionnalite =
                            FonctionnalitePanneauConfigMur.obtenirFonctionnalite(e.getFirstRow());

                    if (fonctionnalite == FonctionnalitePanneauConfigMur.CONFIGURER_HAUTEUR)
                        App.getControleurCabanon().configurerHauteur(convertirStringEnDouble(donneeLigne1.get()));
                    else if (fonctionnalite == FonctionnalitePanneauConfigMur.CONFIGURER_DISTANCE_ENTRE_MONTANTS)
                        App.getControleurCabanon().configurerDistanceEntreMontantsMurs(
                                convertirStringEnDouble(donneeLigne2.get()), Vue.getPointCardinal(vue));

                    editionMur.mettreAJourListesComposantsMur(Vue.getPointCardinal(vue));
                }
                case TOIT -> {
                    if (donneeLigne0.isEmpty() || donneeLigne1.isEmpty() || donneeLigne2.isEmpty()
                            || donneeLigne3.isEmpty() || donneeLigne4.isEmpty()) return;

                    switch (FonctionnalitePanneauConfigToit.obtenirFonctionnalite(e.getFirstRow())) {
                        case CONFIGURER_ANGLE ->
                                App.getControleurCabanon().configurerAngle(obtenirAngle(donneeLigne0.get()));
                        case CONFIGURER_DISTANCE_ENTRE_FERMES -> App.getControleurCabanon()
                                .configurerDistanceEntreFermes(convertirStringEnDouble(donneeLigne1.get()));
                        case CONFIGURER_DISTANCE_ENTRE_FERMES_SUPP -> App.getControleurCabanon()
                                .configurerDistanceEntreFermesSupp(convertirStringEnDouble(donneeLigne2.get()));
                        case CONFIGURER_DISTANCE_PORTE_A_FAUX -> App.getControleurCabanon()
                                .configurerLongueurPorteAFaux(convertirStringEnDouble(donneeLigne3.get()));
                        case CONFIGURER_DISTANCE_ENTRE_ENTREMISES -> App.getControleurCabanon()
                                .configurerDistanceEntreEntremisesToit(convertirStringEnDouble(donneeLigne4.get()));
                    }
                }
                case CONFIG_MATERIAUX -> {
                    if (donneeLigne0.isEmpty() || donneeLigne1.isEmpty() || donneeLigne2.isEmpty()
                            || donneeLigne3.isEmpty() || donneeLigne4.isEmpty() || donneeLigne5.isEmpty()
                            || donneeLigne6.isEmpty() || donneeLigne7.isEmpty()) return;

                    switch (FonctionnalitePanneauConfigMateriaux.obtenirFonctionnalite(e.getFirstRow())) {
                        case CONFIGURER_LONGUEUR_MAX_2X4 -> App.getControleurCabanon()
                                .configurerLimitePlanche(TypePlanche.P_2X4, convertirStringEnDouble(donneeLigne0.get()));
                        case CONFIGURER_LONGUEUR_MAX_2X6 -> App.getControleurCabanon()
                                .configurerLimitePlanche(TypePlanche.P_2X6, convertirStringEnDouble(donneeLigne1.get()));
                        case CONFIGURER_LONGUEUR_MAX_2X8 -> App.getControleurCabanon()
                                .configurerLimitePlanche(TypePlanche.P_2X8, convertirStringEnDouble(donneeLigne2.get()));
                        case CONFIGURER_LONGUEUR_MAX_2X10 -> App.getControleurCabanon()
                                .configurerLimitePlanche(TypePlanche.P_2X10, convertirStringEnDouble(donneeLigne3.get()));
                        case CONFIGURER_COUT_PIED_2X4 -> App.getControleurCabanon()
                                .configurerCoutPlanche(TypePlanche.P_2X4, obtenirArgent(donneeLigne4.get()));
                        case CONFIGURER_COUT_PIED_2X6 -> App.getControleurCabanon()
                                .configurerCoutPlanche(TypePlanche.P_2X6, obtenirArgent(donneeLigne5.get()));
                        case CONFIGURER_COUT_PIED_2X8 -> App.getControleurCabanon()
                                .configurerCoutPlanche(TypePlanche.P_2X8, obtenirArgent(donneeLigne6.get()));
                        case CONFIGURER_COUT_PIED_2X10 -> App.getControleurCabanon()
                                .configurerCoutPlanche(TypePlanche.P_2X10, obtenirArgent(donneeLigne7.get()));
                    }
                }
            }

            creerTableauProprietes();
            panneauDessin.regenerateVue(vue);
        }
    };

    public void preparerAffichage(Vue vue) {
        this.vue = vue;

        creerTableauProprietes();

        switch (vue) {
            case TOIT -> {
                editionMur.cacherPanneauxComposantsMur();
                panneauLignesEntremises.setVisible(false);
                boutonVueFaceToit.setVisible(true);
                boutonVueDessusToit.setVisible(true);
            }
            case CONFIG_MATERIAUX -> {
                editionMur.cacherPanneauxComposantsMur();
                panneauLignesEntremises.setVisible(false);
                boutonVueFaceToit.setVisible(false);
                boutonVueDessusToit.setVisible(false);
            }
            case MUR_NORD -> {
                positionMurEnEdition = PointCardinal.NORD;
                panneauLignesEntremises.setVisible(false);
                boutonVueFaceToit.setVisible(false);
                boutonVueDessusToit.setVisible(false);
            }
            case MUR_SUD -> {
                positionMurEnEdition = PointCardinal.SUD;
                panneauLignesEntremises.setVisible(false);
                boutonVueFaceToit.setVisible(false);
                boutonVueDessusToit.setVisible(false);
            }
            case MUR_EST -> {
                positionMurEnEdition = PointCardinal.EST;
                panneauLignesEntremises.setVisible(false);
                boutonVueFaceToit.setVisible(false);
                boutonVueDessusToit.setVisible(false);
            }
            case MUR_OUEST -> {
                positionMurEnEdition = PointCardinal.OUEST;
                panneauLignesEntremises.setVisible(false);
                boutonVueFaceToit.setVisible(false);
                boutonVueDessusToit.setVisible(false);
            }
            case PLANCHER -> {
                editionMur.cacherPanneauxComposantsMur();
                panneauLignesEntremises.setVisible(true);
                editionPlancher.editionLigneEntremises.ajouterListenerLignesEntremises();
                editionPlancher.editionLigneEntremises.mettreAJourListeLignesEntremises();
                boutonVueFaceToit.setVisible(false);
                boutonVueDessusToit.setVisible(false);
            }
        }

        if (positionMurEnEdition != null) {
            editionMur.afficherPanneauxComposantsMur();
            editionMur.majListener();
            editionMur.mettreAJourListesComposantsMur(positionMurEnEdition);
        }

        final String texte = switch (vue) {
            case TOIT -> obtenirTitre("VueToit");
            case MUR_NORD -> obtenirTitre("VueMurNord");
            case MUR_SUD -> obtenirTitre("VueMurSud");
            case MUR_EST -> obtenirTitre("VueMurEst");
            case MUR_OUEST -> obtenirTitre("VueMurOuest");
            case PLANCHER -> obtenirTitre("VuePlancher");
            case CONFIG_MATERIAUX -> obtenirTitre("VueEnsemble");
        };

        texteVue.setText(texte);
        panneauDessin.clearAll();

        if (vue == Vue.MUR_NORD || vue == Vue.MUR_SUD
                || vue == Vue.MUR_EST || vue == Vue.MUR_OUEST)
            panneauDessin.configurerPopup(vue, editionMur);
        else
            panneauDessin.retirerPopup();

        if (vue == Vue.CONFIG_MATERIAUX) {
            panneauDessin.requestFocusInWindow();
            panneauDessin.init3D(App.getControleurCabanon());
        } else {
            panneauDessin.executeWhenVisible(() -> {
                panneauDessin.requestFocusInWindow();
                panneauDessin.display3DObject(vue, App.getControleurCabanon());
            });
        }
    }

    public void mettreAJourAffichage() {
        if (vue == null) return;
        preparerAffichage(vue);
    }

    public void cacherEditionAccessoire() {
        panneauTypeLinteau.setVisible(false);
        selectionTypeLinteau.removeActionListener(editionMur.editionPorte.listenerTypeLinteauPorte);
        selectionTypeLinteau.removeActionListener(editionMur.editionFenetre.listenerTypeLinteauFenetre);
        boutonSupprimer.removeMouseListener(editionMur.editionPorte.listenerSupprimerPorte);
        boutonSupprimer.removeMouseListener(editionMur.editionFenetre.listenerSupprimerFenetre);
        boutonSupprimer.removeMouseListener(editionMur.editionEntremise.listenerSupprimerEntremise);
        retournerVueProprietes();
    }

    public void retournerVueProprietes() {
        composantEnEdition = null;
        idComposantEnEdition = null;
        proprietesComposantAffichees = false;
        panneauAccessoires.setVisible(true);
        boutonSupprimer.setVisible(false);
        texteRetour.setText(obtenirTitre("RetourVueEnsemble"));
        texteProprietes.setText(obtenirTitre("Proprietes"));
        creerTableauProprietes();
    }

    public void afficherProprietesAccessoire(UUID idAccessoire) {
        idComposantEnEdition = idAccessoire;
        proprietesComposantAffichees = true;
        panneauAccessoires.setVisible(false);
        boutonSupprimer.setVisible(true);
        panneauTypeLinteau.setVisible(true);

        texteRetour.setText(obtenirTitre("RetourProprietesMur"));

        tableauProprietes.getModel().removeTableModelListener(listenerProprietes);
    }

    public void afficherProprietesEntremise(UUID idAccessoire) {
        idComposantEnEdition = idAccessoire;
        proprietesComposantAffichees = true;
        panneauAccessoires.setVisible(false);
        boutonSupprimer.setVisible(true);
        panneauTypeLinteau.setVisible(false);

        texteRetour.setText(obtenirTitre("RetourProprietesMur"));

        tableauProprietes.getModel().removeTableModelListener(listenerProprietes);
    }

    public String obtenirTitre(String cle) {
        return $$$getMessageFromBundle$$$("titres", cle);
    }

    private void creerTableauProprietes() {
        final int[] lignesModifiables = switch (vue) {
            case TOIT -> new int[]{0, 1, 2, 3, 4};
            case MUR_NORD, MUR_SUD, MUR_EST, MUR_OUEST -> new int[]{1, 2};
            case PLANCHER -> new int[]{0, 1, 2};
            case CONFIG_MATERIAUX -> new int[]{0, 1, 2, 3, 4, 5, 6, 7};
        };

        final Object[][] proprietes = switch (vue) {
            case TOIT -> editionToit.obtenirProprietesToit();
            case MUR_NORD -> editionMur.obtenirProprietesMur(PointCardinal.NORD);
            case MUR_SUD -> editionMur.obtenirProprietesMur(PointCardinal.SUD);
            case MUR_EST -> editionMur.obtenirProprietesMur(PointCardinal.EST);
            case MUR_OUEST -> editionMur.obtenirProprietesMur(PointCardinal.OUEST);
            case PLANCHER -> editionPlancher.obtenirProprietesPlancher();
            case CONFIG_MATERIAUX -> editionConfigMateriaux.obtenirProprietesConfigMateriaux();
        };

        switch (vue) {
            case TOIT -> tableauProprietes.creerModele(proprietes, new Champ[]{
                    Champ.ANGLE, Champ.IMPERIAL, Champ.IMPERIAL, Champ.IMPERIAL, Champ.IMPERIAL});
            case CONFIG_MATERIAUX -> tableauProprietes.creerModele(proprietes, new Champ[]{
                    Champ.IMPERIAL, Champ.IMPERIAL, Champ.IMPERIAL, Champ.IMPERIAL,
                    Champ.ARGENT, Champ.ARGENT, Champ.ARGENT, Champ.ARGENT});
            default -> tableauProprietes.creerModele(proprietes);
        }

        tableauProprietes.definirLignesModifiables(lignesModifiables);
        tableauProprietes.getModel().removeTableModelListener(listenerProprietes);
        tableauProprietes.getModel().addTableModelListener(listenerProprietes);
    }

    public Font obtenirPolice(String fontName, int style, int size, Font currentFont) {
        return $$$getFont$$$(fontName, style, size, currentFont);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new BorderLayout(0, 0));
        panneauConfiguration = new JPanel();
        panneauConfiguration.setLayout(new BorderLayout(0, 0));
        panneauConfiguration.setBackground(new Color(-7566196));
        panneauPrincipal.add(panneauConfiguration, BorderLayout.WEST);
        panneauConfiguration.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        texteRetour = new JLabel();
        Font texteRetourFont = this.$$$getFont$$$("Dubai", Font.BOLD, 26, texteRetour.getFont());
        if (texteRetourFont != null) texteRetour.setFont(texteRetourFont);
        texteRetour.setForeground(new Color(-14869219));
        texteRetour.setIcon(new ImageIcon(getClass().getResource("/icons/back-50px.png")));
        this.$$$loadLabelText$$$(texteRetour, this.$$$getMessageFromBundle$$$("titres", "RetourVueEnsemble"));
        panneauConfiguration.add(texteRetour, BorderLayout.NORTH);
        panneauEdition = new JPanel();
        panneauEdition.setLayout(new BorderLayout(0, 0));
        panneauEdition.setBackground(new Color(-7566196));
        panneauConfiguration.add(panneauEdition, BorderLayout.CENTER);
        panneauProprietes = new JPanel();
        panneauProprietes.setLayout(new BorderLayout(0, 0));
        panneauProprietes.setBackground(new Color(-7566196));
        panneauEdition.add(panneauProprietes, BorderLayout.NORTH);
        texteProprietes = new JLabel();
        Font texteProprietesFont = this.$$$getFont$$$("Dubai", Font.BOLD, 24, texteProprietes.getFont());
        if (texteProprietesFont != null) texteProprietes.setFont(texteProprietesFont);
        texteProprietes.setForeground(new Color(-14869219));
        this.$$$loadLabelText$$$(texteProprietes, this.$$$getMessageFromBundle$$$("titres", "Proprietes"));
        panneauProprietes.add(texteProprietes, BorderLayout.NORTH);
        tableauProprietes = new TableauDeuxColonnes();
        tableauProprietes.setBackground(new Color(-7566196));
        Font tableauProprietesFont = this.$$$getFont$$$("Dubai", Font.PLAIN, 22, tableauProprietes.getFont());
        if (tableauProprietesFont != null) tableauProprietes.setFont(tableauProprietesFont);
        tableauProprietes.setForeground(new Color(-14869219));
        tableauProprietes.setGridColor(new Color(-3421237));
        panneauProprietes.add(tableauProprietes, BorderLayout.CENTER);
        panneauCentral = new JPanel();
        panneauCentral.setLayout(new BorderLayout(0, 0));
        panneauCentral.setBackground(new Color(-7566196));
        panneauCentral.setEnabled(true);
        panneauCentral.setVisible(true);
        panneauEdition.add(panneauCentral, BorderLayout.CENTER);
        panneauAccessoires = new JPanel();
        panneauAccessoires.setLayout(new GridBagLayout());
        panneauAccessoires.setBackground(new Color(-7566196));
        panneauAccessoires.setVisible(true);
        panneauCentral.add(panneauAccessoires, BorderLayout.CENTER);
        panneauEntremises = new JPanel();
        panneauEntremises.setLayout(new BorderLayout(0, 0));
        panneauEntremises.setBackground(new Color(-7566196));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panneauAccessoires.add(panneauEntremises, gbc);
        panneauEntremises.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        texteEntremises = new JLabel();
        Font texteEntremisesFont = this.$$$getFont$$$("Dubai", Font.BOLD, 24, texteEntremises.getFont());
        if (texteEntremisesFont != null) texteEntremises.setFont(texteEntremisesFont);
        texteEntremises.setForeground(new Color(-14869219));
        texteEntremises.setIcon(new ImageIcon(getClass().getResource("/icons/plus-50px.png")));
        this.$$$loadLabelText$$$(texteEntremises, this.$$$getMessageFromBundle$$$("titres", "Entremises"));
        panneauEntremises.add(texteEntremises, BorderLayout.NORTH);
        listeEntremises = new JScrollPane();
        listeEntremises.setHorizontalScrollBarPolicy(31);
        listeEntremises.setMinimumSize(new Dimension(0, 80));
        listeEntremises.setPreferredSize(new Dimension(0, 150));
        panneauEntremises.add(listeEntremises, BorderLayout.CENTER);
        panneauListeEntremises.setBackground(new Color(-3421237));
        listeEntremises.setViewportView(panneauListeEntremises);
        panneauListeEntremises.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        texteSupprimerEntremises = new JLabel();
        Font texteSupprimerEntremisesFont = this.$$$getFont$$$("Dubai", Font.PLAIN, 16, texteSupprimerEntremises.getFont());
        if (texteSupprimerEntremisesFont != null) texteSupprimerEntremises.setFont(texteSupprimerEntremisesFont);
        texteSupprimerEntremises.setForeground(new Color(-14869219));
        this.$$$loadLabelText$$$(texteSupprimerEntremises, this.$$$getMessageFromBundle$$$("titres", "SupprimerEntremise"));
        panneauEntremises.add(texteSupprimerEntremises, BorderLayout.SOUTH);
        panneauFenetres = new JPanel();
        panneauFenetres.setLayout(new BorderLayout(0, 0));
        panneauFenetres.setBackground(new Color(-7566196));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panneauAccessoires.add(panneauFenetres, gbc);
        panneauFenetres.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        texteFenetres = new JLabel();
        Font texteFenetresFont = this.$$$getFont$$$("Dubai", Font.BOLD, 24, texteFenetres.getFont());
        if (texteFenetresFont != null) texteFenetres.setFont(texteFenetresFont);
        texteFenetres.setForeground(new Color(-14869219));
        texteFenetres.setIcon(new ImageIcon(getClass().getResource("/icons/plus-50px.png")));
        this.$$$loadLabelText$$$(texteFenetres, this.$$$getMessageFromBundle$$$("titres", "Fenetres"));
        panneauFenetres.add(texteFenetres, BorderLayout.NORTH);
        listeFenetres = new JScrollPane();
        listeFenetres.setHorizontalScrollBarPolicy(31);
        listeFenetres.setMinimumSize(new Dimension(0, 80));
        listeFenetres.setPreferredSize(new Dimension(0, 150));
        panneauFenetres.add(listeFenetres, BorderLayout.CENTER);
        panneauListeFenetres.setBackground(new Color(-3421237));
        listeFenetres.setViewportView(panneauListeFenetres);
        panneauListeFenetres.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panneauPortes = new JPanel();
        panneauPortes.setLayout(new BorderLayout(0, 0));
        panneauPortes.setBackground(new Color(-7566196));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panneauAccessoires.add(panneauPortes, gbc);
        panneauPortes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        textePortes = new JLabel();
        Font textePortesFont = this.$$$getFont$$$("Dubai", Font.BOLD, 24, textePortes.getFont());
        if (textePortesFont != null) textePortes.setFont(textePortesFont);
        textePortes.setForeground(new Color(-14869219));
        textePortes.setIcon(new ImageIcon(getClass().getResource("/icons/plus-50px.png")));
        this.$$$loadLabelText$$$(textePortes, this.$$$getMessageFromBundle$$$("titres", "Portes"));
        panneauPortes.add(textePortes, BorderLayout.NORTH);
        listePortes = new JScrollPane();
        listePortes.setHorizontalScrollBarPolicy(31);
        listePortes.setMinimumSize(new Dimension(0, 80));
        listePortes.setPreferredSize(new Dimension(0, 150));
        panneauPortes.add(listePortes, BorderLayout.CENTER);
        panneauListePortes.setBackground(new Color(-3421237));
        listePortes.setViewportView(panneauListePortes);
        panneauListePortes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panneauLignesEntremises = new JPanel();
        panneauLignesEntremises.setLayout(new BorderLayout(0, 0));
        panneauLignesEntremises.setBackground(new Color(-7566196));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panneauAccessoires.add(panneauLignesEntremises, gbc);
        panneauLignesEntremises.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        texteLignesEntremises = new JLabel();
        Font texteLignesEntremisesFont = this.$$$getFont$$$("Dubai", Font.BOLD, 24, texteLignesEntremises.getFont());
        if (texteLignesEntremisesFont != null) texteLignesEntremises.setFont(texteLignesEntremisesFont);
        texteLignesEntremises.setForeground(new Color(-14869219));
        texteLignesEntremises.setIcon(new ImageIcon(getClass().getResource("/icons/plus-50px.png")));
        this.$$$loadLabelText$$$(texteLignesEntremises, this.$$$getMessageFromBundle$$$("titres", "LignesEntremises"));
        panneauLignesEntremises.add(texteLignesEntremises, BorderLayout.NORTH);
        listeLignesEntremises = new JScrollPane();
        listeLignesEntremises.setHorizontalScrollBarPolicy(31);
        listeLignesEntremises.setMinimumSize(new Dimension(0, 430));
        listeLignesEntremises.setPreferredSize(new Dimension(0, 640));
        panneauLignesEntremises.add(listeLignesEntremises, BorderLayout.CENTER);
        panneauListeLignesEntremises.setBackground(new Color(-3421237));
        listeLignesEntremises.setViewportView(panneauListeLignesEntremises);
        panneauListeLignesEntremises.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panneauTypeLinteau = new JPanel();
        panneauTypeLinteau.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panneauTypeLinteau.setBackground(new Color(-3421237));
        panneauTypeLinteau.setVisible(false);
        panneauCentral.add(panneauTypeLinteau, BorderLayout.NORTH);
        panneauTypeLinteau.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        texteTypeLinteau = new JLabel();
        Font texteTypeLinteauFont = this.$$$getFont$$$("Dubai", Font.PLAIN, 22, texteTypeLinteau.getFont());
        if (texteTypeLinteauFont != null) texteTypeLinteau.setFont(texteTypeLinteauFont);
        texteTypeLinteau.setForeground(new Color(-14869219));
        this.$$$loadLabelText$$$(texteTypeLinteau, this.$$$getMessageFromBundle$$$("titres", "TypeLinteau"));
        panneauTypeLinteau.add(texteTypeLinteau);
        selectionTypeLinteau = new JComboBox();
        selectionTypeLinteau.setBackground(new Color(-3421237));
        Font selectionTypeLinteauFont = this.$$$getFont$$$("Dubai", Font.PLAIN, 22, selectionTypeLinteau.getFont());
        if (selectionTypeLinteauFont != null) selectionTypeLinteau.setFont(selectionTypeLinteauFont);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("2x4");
        defaultComboBoxModel1.addElement("2x6");
        defaultComboBoxModel1.addElement("2x8");
        defaultComboBoxModel1.addElement("2x10");
        selectionTypeLinteau.setModel(defaultComboBoxModel1);
        panneauTypeLinteau.add(selectionTypeLinteau);
        panneauBoutons = new JPanel();
        panneauBoutons.setLayout(new BorderLayout(0, 0));
        panneauBoutons.setBackground(new Color(-7566196));
        panneauEdition.add(panneauBoutons, BorderLayout.SOUTH);
        boutonSupprimer = new JButton();
        boutonSupprimer.setBackground(new Color(-3421237));
        Font boutonSupprimerFont = this.$$$getFont$$$("Dubai", Font.BOLD, 24, boutonSupprimer.getFont());
        if (boutonSupprimerFont != null) boutonSupprimer.setFont(boutonSupprimerFont);
        boutonSupprimer.setForeground(new Color(-14869219));
        this.$$$loadButtonText$$$(boutonSupprimer, this.$$$getMessageFromBundle$$$("titres", "Supprimer"));
        boutonSupprimer.setVisible(false);
        panneauBoutons.add(boutonSupprimer, BorderLayout.CENTER);
        boutonVueFaceToit = new JButton();
        boutonVueFaceToit.setBackground(new Color(-3421237));
        Font boutonVueFaceToitFont = this.$$$getFont$$$("Dubai", Font.BOLD, 24, boutonVueFaceToit.getFont());
        if (boutonVueFaceToitFont != null) boutonVueFaceToit.setFont(boutonVueFaceToitFont);
        boutonVueFaceToit.setForeground(new Color(-14869219));
        this.$$$loadButtonText$$$(boutonVueFaceToit, this.$$$getMessageFromBundle$$$("titres", "VueFace"));
        boutonVueFaceToit.setVisible(false);
        panneauBoutons.add(boutonVueFaceToit, BorderLayout.WEST);
        boutonVueDessusToit = new JButton();
        boutonVueDessusToit.setBackground(new Color(-3421237));
        Font boutonVueDessusToitFont = this.$$$getFont$$$("Dubai", Font.BOLD, 24, boutonVueDessusToit.getFont());
        if (boutonVueDessusToitFont != null) boutonVueDessusToit.setFont(boutonVueDessusToitFont);
        boutonVueDessusToit.setForeground(new Color(-14869219));
        this.$$$loadButtonText$$$(boutonVueDessusToit, this.$$$getMessageFromBundle$$$("titres", "VueDessus"));
        boutonVueDessusToit.setVisible(false);
        panneauBoutons.add(boutonVueDessusToit, BorderLayout.EAST);
        panneauVue.setLayout(new BorderLayout(0, 0));
        panneauPrincipal.add(panneauVue, BorderLayout.CENTER);
        panneauDessin = new PanneauDessin();
        panneauDessin.setBackground(new Color(-14869219));
        panneauVue.add(panneauDessin, BorderLayout.CENTER);
        panneauEntete = new JPanel();
        panneauEntete.setLayout(new BorderLayout(0, 0));
        panneauEntete.setBackground(new Color(-7566196));
        panneauVue.add(panneauEntete, BorderLayout.NORTH);
        texteVue = new JLabel();
        texteVue.setEnabled(true);
        Font texteVueFont = this.$$$getFont$$$("Dubai", Font.PLAIN, 30, texteVue.getFont());
        if (texteVueFont != null) texteVue.setFont(texteVueFont);
        texteVue.setForeground(new Color(-14869219));
        panneauEntete.add(texteVue, BorderLayout.NORTH);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panneauPrincipal;
    }

    private void createUIComponents() {
        panneauVue = new JPanel();
        panneauVue.setBorder(BorderFactory.createLineBorder(Couleurs.GRIS_MOYEN, 10));

        panneauListePortes = creerPanneauListe();
        panneauListeFenetres = creerPanneauListe();
        panneauListeEntremises = creerPanneauListe();
        panneauListeLignesEntremises = creerPanneauListe();
    }

    private JPanel creerPanneauListe() {
        final JPanel panneau = new JPanel();
        panneau.setLayout(new BoxLayout(panneau, BoxLayout.Y_AXIS));
        return panneau;
    }

    public enum FonctionnalitePanneauConfigAccessoire {
        CONFIGURER_POSITION,
        CONFIGURER_LARGEUR,
        CONFIGURER_HAUTEUR,
        CONFIGURER_DISTANCE_ENTRE_MONTANTS;

        public static FonctionnalitePanneauConfigAccessoire obtenirFonctionnalite(int positionLigne) {
            return switch (positionLigne) {
                case 0 -> CONFIGURER_POSITION;
                case 1 -> CONFIGURER_LARGEUR;
                case 2 -> CONFIGURER_HAUTEUR;
                default -> CONFIGURER_DISTANCE_ENTRE_MONTANTS;
            };
        }
    }
}