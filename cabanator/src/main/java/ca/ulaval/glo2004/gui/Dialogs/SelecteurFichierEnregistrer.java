package ca.ulaval.glo2004.gui.Dialogs;

import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.GestionFichiersCabanon;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import java.io.File;
import java.util.Objects;

public class SelecteurFichierEnregistrer extends SelecteurFichier {
    private String cheminFichierOriginel = "";

    public SelecteurFichierEnregistrer() {
        super();
        this.setFileSelectionMode(DIRECTORIES_ONLY);
        this.setSelectedFile(new File(GestionFichiersCabanon.getCheminAccess()));
    }

    public SelecteurFichierEnregistrer(File file) {
        this();
        this.setSelectedFile(file);
        super.cheminFichier = file.getAbsolutePath();
        this.cheminFichierOriginel = super.cheminFichier;
    }

    @Override
    public int afficher() {
        return this.showSaveDialog(null);
    }

    @Override
    public void reinitialiser() {
        if (!Objects.equals(this.cheminFichierOriginel, "")) {
            this.setSelectedFile(new File(this.cheminFichierOriginel));
        }
    }
}