package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypeSauvegarde;
import ca.ulaval.glo2004.gui.Dialogs.BtnSelecteurFichier;
import ca.ulaval.glo2004.gui.Dialogs.EnregistrerProjetDialog;
import ca.ulaval.glo2004.gui.Dialogs.SelecteurFichierEnregistrer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/* MenuLookDemo.java requires images/middle.gif. */

/*
 * This class exists solely to show you what menus look like.
 * It has no menu-related event handling.
 */
public class MenuPrincipal {
    private final String MENU_FICHIER = "Fichier";
    private final String MENU_FICHIER_OUVRIR_PROJET = "Ouvrir...";
    private final String MENU_FICHIER_ENREGISTRER = "Enregistrer";
    private final String MENU_FICHIER_ENREGISTRER_SOUS = "Enregistrer sous...";
    private final String MENU_FICHIER_EXPORTER = "Exporter";
    private final String MENU_FICHIER_QUITTER = "Quitter";
    private final String MENU_EDITION = "Édition";
    private final String MENU_EDITION_ANNULER = "Annuler";
    private final String MENU_EDITION_RETABLIR = "Rétablir";
    private StringBuilder nomProjet;
    private StringBuilder versionSauvegarde;
    private StringBuilder cheminFichier;
    private final MenuItem itemEnregistrerProjet = new MenuItem(this.MENU_FICHIER_ENREGISTRER);

    private final ActionListener actionEnregistrerSous = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EnregistrerProjetDialog sauvegarderProjetDialog = new EnregistrerProjetDialog();
            String nomProjetAvantSauvegarde = nomProjet.toString();
            sauvegarderProjetDialog.AfficherDialogue(nomProjet, versionSauvegarde, cheminFichier);
            if (!nomProjet.toString().equals(nomProjetAvantSauvegarde)) {
                itemEnregistrerProjet.setEnabled(true);
                MainWindow.getInstance().MAJTexteNomProjet(nomProjet.toString());
            }
        }
    };

    private final ActionListener actionEnregistrer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            App.getControleurCabanon().enregistrerProjet(
                    cheminFichier.toString(),
                    nomProjet.toString(),
                    versionSauvegarde.toString(),
                    false,
                    TypeSauvegarde.ECRASER_SAUVEGARDE);
        }
    };

    private final ActionListener exporter = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            SelecteurFichierEnregistrer sfe = new SelecteurFichierEnregistrer();
            int returnVal = sfe.afficher();

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                App.getControleurCabanon().exporterPiecesProjet(sfe.obtenirCheminFichier());
            }
        }
    };

    public MenuBar creerMenuPrincipal(StringBuilder nomProjet, StringBuilder versionSauvegarde, StringBuilder cheminFichier) {
        System.out.println("Créer menu principal");

        this.nomProjet = nomProjet;
        this.versionSauvegarde = versionSauvegarde;
        this.cheminFichier = cheminFichier;

        MenuBar menuPrincipal = new MenuBar();
        menuPrincipal.add(this.creerMenuFichier());
        menuPrincipal.add(this.creerMenuEdition());

        return menuPrincipal;
    }

    private Menu creerMenuFichier() {
        Menu menuFichier = new Menu(this.MENU_FICHIER);

        MenuItem itemOuvrirProjet = new MenuItem(this.MENU_FICHIER_OUVRIR_PROJET);

        if (Objects.equals(this.nomProjet.toString(), "")
                && Objects.equals(this.versionSauvegarde.toString(), "")) {
            this.itemEnregistrerProjet.setEnabled(false);
        }
        this.itemEnregistrerProjet.addActionListener(this.actionEnregistrer);

        MenuItem itemEnregistrerSousProjet = new MenuItem(this.MENU_FICHIER_ENREGISTRER_SOUS);
        itemEnregistrerSousProjet.addActionListener(this.actionEnregistrerSous);

        MenuItem itemExporter = new MenuItem(this.MENU_FICHIER_EXPORTER);
        itemExporter.addActionListener(this.exporter);

        MenuItem itemQuitter = new MenuItem(this.MENU_FICHIER_QUITTER);
        itemQuitter.addActionListener(Navigateur.ACTION_QUITTER);

        menuFichier.add(itemOuvrirProjet);
        menuFichier.add(this.itemEnregistrerProjet);
        menuFichier.add(itemEnregistrerSousProjet);
        menuFichier.addSeparator();
        menuFichier.add(itemExporter);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);

        return menuFichier;
    }

    private Menu creerMenuEdition() {
        Menu menuEdition = new Menu(this.MENU_EDITION);

        MenuItem itemAnnuler = new MenuItem(this.MENU_EDITION_ANNULER);
        MenuItem itemRetablir = new MenuItem(this.MENU_EDITION_RETABLIR);

        itemAnnuler.addActionListener(Navigateur.ACTION_ANNULER);
        itemRetablir.addActionListener(Navigateur.ACTION_RETABLIR);

        menuEdition.add(itemAnnuler);
        menuEdition.add(itemRetablir);

        return menuEdition;
    }
}