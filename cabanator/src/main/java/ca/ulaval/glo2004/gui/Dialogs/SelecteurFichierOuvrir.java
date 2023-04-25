package ca.ulaval.glo2004.gui.Dialogs;

import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.GestionFichiersCabanon;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

public class SelecteurFichierOuvrir extends SelecteurFichier {
    private String cheminFichierOriginel = " ";

    public SelecteurFichierOuvrir() {
        super();
        this.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().contains(GestionFichiersCabanon.EXTENSION_FICHIER);
            }

            @Override
            public String getDescription() {
                return GestionFichiersCabanon.EXTENSION_FICHIER;
            }
        });
        super.cheminFichier = this.cheminFichierOriginel;
    }

    @Override
    public int afficher() {
        return this.showOpenDialog(null);
    }

    @Override
    public void reinitialiser() {
        this.setSelectedFile(null);
        super.cheminFichier = this.cheminFichierOriginel;
    }
}