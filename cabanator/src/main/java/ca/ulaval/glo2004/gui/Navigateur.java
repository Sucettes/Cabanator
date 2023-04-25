package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.Utilitaires.Dimension3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Navigateur {
    public static final String TITRE_CONFIRMATION = "Confirmation";
    private static final String OUI = "Oui";
    private static final String NON = "Non";
    private static final String TITRE = "Cabanator";
    private static final String MESSAGE_CONFIRMATION = "Voulez-vous vraiment quitter l'application ?\nLes modifications non enregistrées seront perdues.";

    private static final JFrame FRAME = new JFrame(TITRE);
    private static final HomeWindow FENETRE_ACCUEIL = new HomeWindow();
    private static final PropertiesWindow FENETRE_PROPRIETES = new PropertiesWindow();

    public static final Action ACTION_ANNULER = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (projetOuvert) {
                App.getControleurCabanon().annulerActionCabanon();
                MainWindow.getInstance().mettreAJourTableaux();
                FENETRE_PROPRIETES.mettreAJourAffichage();
            }
        }
    };

    public static final Action ACTION_RETABLIR = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (projetOuvert) {
                App.getControleurCabanon().retablirActionCabanon();
                MainWindow.getInstance().mettreAJourTableaux();
                FENETRE_PROPRIETES.mettreAJourAffichage();
            }
        }
    };

    public static final Action ACTION_QUITTER = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            quitter();
        }
    };

    private static boolean projetOuvert = false;

    public static void creerFrame() {
        UIManager.put("OptionPane.yesButtonText", OUI);
        UIManager.put("OptionPane.noButtonText", NON);

        FRAME.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                quitter();
            }
        });

        final KeyStroke toucheAnnuler = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        FRAME.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(toucheAnnuler, "annuler");
        FRAME.getRootPane().getActionMap().put("annuler", ACTION_ANNULER);

        final KeyStroke toucheRetablir = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        FRAME.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(toucheRetablir, "retablir");
        FRAME.getRootPane().getActionMap().put("retablir", ACTION_RETABLIR);

        FRAME.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        FRAME.setExtendedState(JFrame.MAXIMIZED_BOTH);
        FRAME.pack();
        FRAME.setVisible(true);
    }

    public static void afficherFenetreAccueil() {
        projetOuvert = false;
        FENETRE_ACCUEIL.preparerAffichage();
        Navigateur.setFrame(FENETRE_ACCUEIL.panneauPrincipal);
        FRAME.setMenuBar(null);
        MainWindow.obtenirNouvelleInstance();
    }

    public static void afficherFenetrePrincipale(Dimension3D dimensionCabanon) {
        projetOuvert = true;
        StringBuilder nomProjet = new StringBuilder("");
        StringBuilder versionSauvegarde = new StringBuilder("");
        StringBuilder cheminFichier = new StringBuilder("");
        FRAME.setMenuBar(new MenuPrincipal().creerMenuPrincipal(nomProjet, versionSauvegarde,cheminFichier));
        MainWindow.getInstance().preparerAffichage(nomProjet, versionSauvegarde, dimensionCabanon);
        Navigateur.setFrame(MainWindow.getInstance().panneauPrincipal);
    }

    public static void afficherFenetrePrincipale(StringBuilder nomProjet, String[] infosProjet) {
        projetOuvert = true;
        FRAME.setMenuBar(new MenuPrincipal().creerMenuPrincipal(
                nomProjet,
                new StringBuilder(infosProjet[1]),
                new StringBuilder(infosProjet[3])));
        MainWindow.getInstance().preparerAffichage(nomProjet, infosProjet);
        Navigateur.setFrame(MainWindow.getInstance().panneauPrincipal);
    }

    public static void retourFenetrePrincipale() {
        MainWindow.getInstance().mettreAJourTableaux();
        Navigateur.setFrame(MainWindow.getInstance().panneauPrincipal);
    }

    public static void afficherFenetreProprietes(Vue vue) {
        FENETRE_PROPRIETES.preparerAffichage(vue);
        Navigateur.setFrame(FENETRE_PROPRIETES.panneauPrincipal);
    }

    private static void setFrame(JPanel panneau) {
        FRAME.setContentPane(panneau);
        FRAME.validate();
    }

    private static void quitter() {
        boolean quitter = true;

        if (projetOuvert) {
            final int reponse = JOptionPane.showConfirmDialog(FRAME,
                    MESSAGE_CONFIRMATION, TITRE_CONFIRMATION, JOptionPane.YES_NO_OPTION);
            quitter = reponse == JOptionPane.YES_OPTION;
        }

        if (quitter) System.exit(0);
    }
}