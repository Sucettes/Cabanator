package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.gui.Dialogs.EnregistrerProjetDialog;
import ca.ulaval.glo2004.gui.Dialogs.SelecteurFichierEnregistrer;
import ca.ulaval.glo2004.gui.Tableau.Champ;
import ca.ulaval.glo2004.gui.Tableau.TableauDeuxColonnes;
import ca.ulaval.glo2004.gui.Tableau.TableauIconeColonne;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale.convertirEnFraction;
import static ca.ulaval.glo2004.gui.Tableau.ChampArgent.obtenirArgent;

public class MainWindow {
    private static MainWindow instance;
    public JPanel panneauPrincipal;
    private PanneauDessin panneauDessin;
    private JPanel panneauConfiguration;
    private JPanel panneauVueEnsemble;
    private JLabel texteNomProjet;
    private JPanel panneauEntete;
    private JLabel texteVueEnsemble;
    private JPanel panneauTitre;
    private JPanel panneauBoutons;
    private JLabel iconeAccueil;
    private JLabel iconeEnregistrer;
    private JLabel iconeExporter;
    private JLabel iconeAnnuler;
    private JLabel iconeRetablir;
    private JPanel panneauProprietes;
    private JPanel panneauDimensions;
    private JLabel texteDimensions;
    private JPanel panneauPlanches;
    private JLabel textePlanches;
    private TableauDeuxColonnes tableauDimensions;
    private TableauIconeColonne tableauPlanches;
    private TableauDeuxColonnes tableauCoutTotal;
    private JPanel tableauxPlanches;
    private JLabel iconeMateriaux;
    private StringBuilder versionProjet;
    private StringBuilder cheminFichier;
    private final String NOM_PROJET_CLIENT_PROPERTY_KEY = "nomProjet";

    private MainWindow() {
        $$$setupUI$$$();

        iconeMateriaux.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        iconeMateriaux.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Navigateur.afficherFenetreProprietes(Vue.CONFIG_MATERIAUX);
            }
        });

        iconeAccueil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        iconeAccueil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                final int reponse = JOptionPane.showConfirmDialog(e.getComponent(),
                        $$$getMessageFromBundle$$$("titres", "ConfirmationRetourAccueil"),
                        Navigateur.TITRE_CONFIRMATION, JOptionPane.YES_NO_OPTION);
                if (reponse == JOptionPane.YES_OPTION) {
                    Navigateur.afficherFenetreAccueil();
                }
            }
        });

        iconeEnregistrer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                EnregistrerProjetDialog sauvegarderProjetDialog = new EnregistrerProjetDialog();
                StringBuilder nomProjetReference = (StringBuilder) texteNomProjet.getClientProperty(NOM_PROJET_CLIENT_PROPERTY_KEY);
                String nomProjet = nomProjetReference.toString();
                sauvegarderProjetDialog.AfficherDialogue(nomProjetReference, versionProjet, cheminFichier);
                if (!nomProjetReference.toString().equals(nomProjet)) {
                    MAJTexteNomProjet(nomProjet);
                }
            }
        };
        iconeEnregistrer.addMouseListener(adapter);

        iconeExporter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        iconeExporter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                SelecteurFichierEnregistrer sfe = new SelecteurFichierEnregistrer();
                int returnVal = sfe.afficher();

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    App.getControleurCabanon().exporterPiecesProjet(sfe.obtenirCheminFichier());
                }
            }
        });

        iconeAnnuler.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        iconeAnnuler.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                App.getControleurCabanon().annulerActionCabanon();
                mettreAJourTableauDimensions();
                mettreAJourTableauCoutTotal();
            }
        });

        iconeRetablir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        iconeRetablir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                App.getControleurCabanon().retablirActionCabanon();
                mettreAJourTableauDimensions();
                mettreAJourTableauCoutTotal();
            }
        });

        //Quand le panneauDessin est rendu visible
        panneauDessin.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                // Component added somewhere
                panneauDessin.requestFocusInWindow();
//                panneauDessin.init3D();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    //#region CONTROLEUR

    /**
     * Permet de préparer l'affiche en provenance de la création d'un cabanon.
     */
    public void preparerAffichage(StringBuilder nomProjet, StringBuilder versionSauvegarde, Dimension3D dimensionCabanon) {
        initialiserControleur(nomProjet, versionSauvegarde, dimensionCabanon);
        mettreAJourTableauDimensions();
        mettreAJourTableauCoutTotal();

        panneauDessin.requestFocusInWindow();
        panneauDessin.init3D(App.getControleurCabanon());
    }

    /**
     * Permet de préparer l'affiche en provenance de l'ouverture d'un projet de cabanon.
     */
    public void preparerAffichage(StringBuilder nomProjet, String[] infosProjet) {
        initialiserControleur(nomProjet, infosProjet);
        mettreAJourTableauDimensions();
        mettreAJourTableauCoutTotal();

        panneauDessin.requestFocusInWindow();
        panneauDessin.init3D(App.getControleurCabanon());
    }

    //#endregion

    public void mettreAJourTableaux() {
        mettreAJourTableauDimensions();
        mettreAJourTableauCoutTotal();
    }

    private void mettreAJourTableauDimensions() {
        final Dimension3D dimensions = App.getControleurCabanon().obtenirConfigurationCabanon().dimension;

        final Object[][] donneesDimensions = {
                {$$$getMessageFromBundle$$$("titres", "Longueur"),
                        convertirEnFraction(dimensions.longueur)
                },
                {$$$getMessageFromBundle$$$("titres", "Largeur"),
                        convertirEnFraction(dimensions.largeur)
                },
                {$$$getMessageFromBundle$$$("titres", "Hauteur"),
                        convertirEnFraction(dimensions.hauteur)
                }
        };

        tableauDimensions.creerModele(donneesDimensions);
        panneauDessin.regenerateCabanon();
    }

    private void mettreAJourTableauCoutTotal() {
        final Object[][] coutTotal = {
                {$$$getMessageFromBundle$$$("titres", "CoutTotal"),
                        obtenirArgent(App.getControleurCabanon().calculerCout())
                }
        };

        tableauCoutTotal.creerModele(coutTotal, new Champ[]{Champ.ARGENT});
    }

    public void MAJTexteNomProjet(String nomProjet) {
        texteNomProjet.setText(nomProjet);
    }

    private void initialiserControleur(StringBuilder nomProjet, StringBuilder versionSauvegarde, Dimension3D dimensionCabanon) {
        App.getControleurCabanon().creerProjet(dimensionCabanon);
        this.MAJTexteNomProjet($$$getMessageFromBundle$$$("titres", "NouveauProjet"));
        this.texteNomProjet.putClientProperty(this.NOM_PROJET_CLIENT_PROPERTY_KEY, nomProjet);
        this.versionProjet = versionSauvegarde;
        this.cheminFichier = new StringBuilder("");
//        controleurCabanon.ajouterPorte(new PointDTO(9, 9, 9), PointCardinal.EST, 0, 0);
    }

    private void initialiserControleur(StringBuilder nomProjet, String[] infosProjet) {
        if (App.getControleurCabanon().ouvrirProjet(infosProjet[3])) {
            this.MAJTexteNomProjet(nomProjet.toString());
            this.texteNomProjet.putClientProperty(this.NOM_PROJET_CLIENT_PROPERTY_KEY, nomProjet);
            this.versionProjet = new StringBuilder(infosProjet[1]);
            this.cheminFichier = new StringBuilder(infosProjet[3]);
//        controleurCabanon.ajouterPorte(new PointDTO(9, 9, 9), PointCardinal.EST, 0, 0);
        } else {
            JOptionPane.showConfirmDialog(
                    null,
                    "Une erreur s'est produite. L'ouverture du projet sélectionné n'a pas été en mesure d'être effectuée",
                    "Erreur à l'ouverture du projet",
                    JOptionPane.OK_OPTION);
        }
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
        panneauConfiguration.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 20, 20, 20), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panneauTitre = new JPanel();
        panneauTitre.setLayout(new BorderLayout(0, 0));
        panneauTitre.setBackground(new Color(-7566196));
        panneauConfiguration.add(panneauTitre, BorderLayout.NORTH);
        texteNomProjet = new JLabel();
        Font texteNomProjetFont = this.$$$getFont$$$("Dubai", Font.BOLD, 36, texteNomProjet.getFont());
        if (texteNomProjetFont != null) texteNomProjet.setFont(texteNomProjetFont);
        texteNomProjet.setForeground(new Color(-14869219));
        texteNomProjet.setText("Nom du projet ouvert ici");
        panneauTitre.add(texteNomProjet, BorderLayout.NORTH);
        panneauBoutons = new JPanel();
        panneauBoutons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panneauBoutons.setBackground(new Color(-7566196));
        panneauTitre.add(panneauBoutons, BorderLayout.SOUTH);
        panneauBoutons.add(iconeAccueil);
        panneauBoutons.add(iconeEnregistrer);
        panneauBoutons.add(iconeExporter);
        panneauBoutons.add(iconeMateriaux);
        panneauBoutons.add(iconeAnnuler);
        panneauBoutons.add(iconeRetablir);
        panneauProprietes = new JPanel();
        panneauProprietes.setLayout(new BorderLayout(0, 0));
        panneauProprietes.setBackground(new Color(-7566196));
        panneauConfiguration.add(panneauProprietes, BorderLayout.CENTER);
        panneauDimensions = new JPanel();
        panneauDimensions.setLayout(new BorderLayout(0, 0));
        panneauDimensions.setBackground(new Color(-7566196));
        panneauProprietes.add(panneauDimensions, BorderLayout.NORTH);
        panneauDimensions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        texteDimensions = new JLabel();
        Font texteDimensionsFont = this.$$$getFont$$$("Dubai", Font.BOLD, 24, texteDimensions.getFont());
        if (texteDimensionsFont != null) texteDimensions.setFont(texteDimensionsFont);
        texteDimensions.setForeground(new Color(-14869219));
        this.$$$loadLabelText$$$(texteDimensions, this.$$$getMessageFromBundle$$$("titres", "DimensionsCabanon"));
        panneauDimensions.add(texteDimensions, BorderLayout.NORTH);
        tableauDimensions = new TableauDeuxColonnes();
        Font tableauDimensionsFont = this.$$$getFont$$$("Dubai", Font.PLAIN, 22, tableauDimensions.getFont());
        if (tableauDimensionsFont != null) tableauDimensions.setFont(tableauDimensionsFont);
        tableauDimensions.setForeground(new Color(-14869219));
        tableauDimensions.setGridColor(new Color(-3421237));
        panneauDimensions.add(tableauDimensions, BorderLayout.CENTER);
        panneauPlanches = new JPanel();
        panneauPlanches.setLayout(new BorderLayout(0, 0));
        panneauPlanches.setBackground(new Color(-7566196));
        panneauProprietes.add(panneauPlanches, BorderLayout.CENTER);
        panneauPlanches.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        textePlanches = new JLabel();
        Font textePlanchesFont = this.$$$getFont$$$("Dubai", Font.BOLD, 24, textePlanches.getFont());
        if (textePlanchesFont != null) textePlanches.setFont(textePlanchesFont);
        textePlanches.setForeground(new Color(-14869219));
        this.$$$loadLabelText$$$(textePlanches, this.$$$getMessageFromBundle$$$("titres", "Planches"));
        panneauPlanches.add(textePlanches, BorderLayout.NORTH);
        tableauxPlanches = new JPanel();
        tableauxPlanches.setLayout(new BorderLayout(0, 0));
        panneauPlanches.add(tableauxPlanches, BorderLayout.CENTER);
        tableauPlanches.setBackground(new Color(-7566196));
        Font tableauPlanchesFont = this.$$$getFont$$$("Dubai", Font.PLAIN, 22, tableauPlanches.getFont());
        if (tableauPlanchesFont != null) tableauPlanches.setFont(tableauPlanchesFont);
        tableauPlanches.setForeground(new Color(-14869219));
        tableauPlanches.setGridColor(new Color(-3421237));
        tableauxPlanches.add(tableauPlanches, BorderLayout.NORTH);
        tableauCoutTotal = new TableauDeuxColonnes();
        tableauCoutTotal.setBackground(new Color(-7566196));
        Font tableauCoutTotalFont = this.$$$getFont$$$("Dubai", Font.PLAIN, 22, tableauCoutTotal.getFont());
        if (tableauCoutTotalFont != null) tableauCoutTotal.setFont(tableauCoutTotalFont);
        tableauCoutTotal.setForeground(new Color(-14869219));
        tableauCoutTotal.setGridColor(new Color(-3421237));
        tableauxPlanches.add(tableauCoutTotal, BorderLayout.CENTER);
        panneauVueEnsemble.setLayout(new BorderLayout(0, 0));
        panneauPrincipal.add(panneauVueEnsemble, BorderLayout.CENTER);
        panneauEntete = new JPanel();
        panneauEntete.setLayout(new BorderLayout(0, 0));
        panneauEntete.setBackground(new Color(-7566196));
        panneauVueEnsemble.add(panneauEntete, BorderLayout.NORTH);
        texteVueEnsemble = new JLabel();
        texteVueEnsemble.setEnabled(true);
        Font texteVueEnsembleFont = this.$$$getFont$$$("Dubai", Font.PLAIN, 30, texteVueEnsemble.getFont());
        if (texteVueEnsembleFont != null) texteVueEnsemble.setFont(texteVueEnsembleFont);
        texteVueEnsemble.setForeground(new Color(-14869219));
        this.$$$loadLabelText$$$(texteVueEnsemble, this.$$$getMessageFromBundle$$$("titres", "VueEnsemble"));
        panneauEntete.add(texteVueEnsemble, BorderLayout.NORTH);
        panneauDessin = new PanneauDessin();
        panneauDessin.setBackground(new Color(-14869219));
        panneauVueEnsemble.add(panneauDessin, BorderLayout.CENTER);
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
    public JComponent $$$getRootComponent$$$() {
        return panneauPrincipal;
    }

    private void createUIComponents() {
        creerPanneauBoutons();
        creerPanneauPlanches();
        creerPanneauVueEnsemble();
    }

    private void creerPanneauBoutons() {
        iconeAccueil = new JLabel(creerIcone("/icons/home.png", 60));
        iconeEnregistrer = new JLabel(creerIcone("/icons/save.png", 65));
        iconeExporter = new JLabel(creerIcone("/icons/export.png", 60));
        iconeMateriaux = new JLabel(creerIcone("/icons/settings.png", 65));
        iconeAnnuler = new JLabel(creerIcone("/icons/undo.png", 65));
        iconeRetablir = new JLabel(creerIcone("/icons/redo.png", 65));
    }

    private void creerPanneauPlanches() {
        final ImageIcon iconeParametres = creerIcone("/icons/settings.png", 40);

        final Object[][] planches = {
                {iconeParametres, $$$getMessageFromBundle$$$("titres", "Toit")},
                {iconeParametres, $$$getMessageFromBundle$$$("titres", "MurNord")},
                {iconeParametres, $$$getMessageFromBundle$$$("titres", "MurSud")},
                {iconeParametres, $$$getMessageFromBundle$$$("titres", "MurEst")},
                {iconeParametres, $$$getMessageFromBundle$$$("titres", "MurOuest")},
                {iconeParametres, $$$getMessageFromBundle$$$("titres", "Plancher")}
        };

        final Color[] couleursLignes = new Color[]{
                Couleurs.MAUVE,
                Couleurs.BLEU,
                Couleurs.JAUNE,
                Couleurs.VERT,
                Couleurs.ORANGE,
                Couleurs.ROUGE
        };

        tableauPlanches = new TableauIconeColonne(couleursLignes);
        tableauPlanches.creerModele(planches);

        tableauPlanches.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tableauPlanches.columnAtPoint(e.getPoint()) > 0)
                    return;

                final Vue vue = switch (tableauPlanches.rowAtPoint(e.getPoint())) {
                    case 0 -> Vue.TOIT;
                    case 1 -> Vue.MUR_NORD;
                    case 2 -> Vue.MUR_SUD;
                    case 3 -> Vue.MUR_EST;
                    case 4 -> Vue.MUR_OUEST;
                    case 5 -> Vue.PLANCHER;
                    default -> throw new IllegalArgumentException();
                };

                Navigateur.afficherFenetreProprietes(vue);
            }
        });
    }

    private void creerPanneauVueEnsemble() {
        panneauVueEnsemble = new JPanel();
        panneauVueEnsemble.setBorder(BorderFactory.createLineBorder(Couleurs.GRIS_MOYEN, 10));
    }

    private ImageIcon creerIcone(String fichier, int hauteur) {
        final ImageIcon iconeOriginale = new ImageIcon(Objects.requireNonNull(getClass().getResource(fichier)));
        final Image imageOriginale = iconeOriginale.getImage();
        final Image imageEchelle = imageOriginale.getScaledInstance(-1, hauteur, Image.SCALE_SMOOTH);
        return new ImageIcon(imageEchelle);
    }

    private static MainWindow creerInstance() {
        MainWindow.instance = new MainWindow();
        return MainWindow.instance;
    }

    public static MainWindow obtenirNouvelleInstance() {
        return MainWindow.creerInstance();
    }

    public static MainWindow getInstance() {
        if (MainWindow.instance == null) {
            System.out.println("creer instance");
            MainWindow.instance = new MainWindow();
        }

        return MainWindow.instance;
    }
}