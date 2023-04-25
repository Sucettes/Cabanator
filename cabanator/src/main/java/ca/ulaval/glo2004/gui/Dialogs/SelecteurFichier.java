package ca.ulaval.glo2004.gui.Dialogs;

import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.GestionFichiersCabanon;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public abstract class SelecteurFichier extends JFileChooser {
    protected String cheminFichier;

    public SelecteurFichier() {
        super();
        this.cheminFichier = "";
        this.setMultiSelectionEnabled(false);
    }

    public void onApproved() {
        this.cheminFichier = this.getSelectedFile().getAbsolutePath();
    }

    public String obtenirCheminFichier() {
        return this.cheminFichier;
    }

    public abstract int afficher();
    protected abstract void reinitialiser();
}