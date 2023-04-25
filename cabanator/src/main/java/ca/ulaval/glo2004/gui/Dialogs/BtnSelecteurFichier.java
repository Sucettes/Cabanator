package ca.ulaval.glo2004.gui.Dialogs;

import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.GestionFichiersCabanon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class BtnSelecteurFichier extends JButton {
    private SelecteurFichier sf;
    private JLabel lblCheminFichier;

    private static final String NOM_BTN_SFE = "Choisir un fichier";
    private static final String CHEMIN_FICHIER = "Chemin d'accès : ";

    private ActionListener ouvrirSelecteur = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            reinitialiserLblCheminFichier(" ");

            int returnVal = sf.afficher();

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                sf.onApproved();
                lblCheminFichier.setText(CHEMIN_FICHIER + sf.obtenirCheminFichier());
            }
        }
    };

    public BtnSelecteurFichier(SelecteurFichier sf) {
        super(BtnSelecteurFichier.NOM_BTN_SFE);
        this.sf = sf;
        this.lblCheminFichier = new JLabel();
        this.reinitialiserLblCheminFichier(sf.obtenirCheminFichier());
        this.addActionListener(this.ouvrirSelecteur);
    }

    public JLabel getLblCheminFichier() {
        return this.lblCheminFichier;
    }

    public String obtenirCheminFichier() {
        return this.sf.obtenirCheminFichier();
    }

    private void reinitialiserLblCheminFichier(String cheminFichier) {
        this.lblCheminFichier.setText(BtnSelecteurFichier.CHEMIN_FICHIER + (
                Objects.equals(cheminFichier, "")
                        ? GestionFichiersCabanon.getCheminAccess()
                        : cheminFichier));
    }

    public void reinitialiser(String cheminFichier) {
        this.reinitialiserLblCheminFichier(cheminFichier);
        this.sf.reinitialiser();
    }
}