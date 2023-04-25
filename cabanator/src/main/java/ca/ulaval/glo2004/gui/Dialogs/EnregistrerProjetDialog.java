package ca.ulaval.glo2004.gui.Dialogs;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypeSauvegarde;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.GestionFichiersCabanon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class EnregistrerProjetDialog {
    private final String NOM_DIALOGUE = "Enregistrer sous le projet";
    private final String LBL_NOM_PROJET = "Nom du projet";
    private final String LBL_RENOMMER_PROJET = "Renommer les autres versions du projet";
    private final String LBL_TYPE_SAUVEGARDE = "Version => ";
    private final String NOM_BTN_REINITIALISER = "Réinitialiser";
    private final String NOM_PROJET_INVALIDE = "Le nom du projet est invalide";
    private JTextField jtxtNomProjet = new JTextField();
    private JCheckBox jckbRenommerProjet = new JCheckBox(this.LBL_RENOMMER_PROJET);
    private JLabel lblVersionProjet = new JLabel();
    private BtnSelecteurFichier btnSFE;
    private JButton btnReinitialiser = new JButton(this.NOM_BTN_REINITIALISER);
    private JButton btnEnregistrer = new JButton("Enregistrer");
    private JButton btnAnnuler = new JButton("Annuler");
    private JDialog dialogue = new JDialog(null, this.NOM_DIALOGUE, Dialog.ModalityType.APPLICATION_MODAL);
    public JComboBox jcbxTypeSauvegarde;
    private StringBuilder nomProjet;
    private StringBuilder versionProjet;
    private StringBuilder cheminFichier;

    private StringBuilder getNomProjet() {
        return this.nomProjet;
    }

    private void setNomProjet(String nomProjet) {
        ArrayList<String> msgsErr = new ArrayList<>();
        if (nomProjet.length() < 1) {
            msgsErr.add("Le nom du projet doit contenir plus d'un caractère.");
        } else if (nomProjet.equals("null")) {
            msgsErr.add("Le nom du projet ne peut être 'null'.");
        }
//        else if (!nomProjet.matches("[a-zA-Z]|[0-9]")) {
//            msgsErr.add("Le nom du projet ne peut contenir que des caractères alpha numérique.");
//        }

        if (msgsErr.size() > 0) {
            throw new InvalidParameterException(String.join("\r;", msgsErr));
        }

        this.nomProjet.replace(0, this.nomProjet.length(), nomProjet);
    }

    private StringBuilder getVersionProjet() {
        return this.versionProjet;
    }

    private StringBuilder getCheminFichier() {
        return this.cheminFichier;
    }

    private void setCheminFichier(String cheminFichier) {
        this.cheminFichier.replace(0, this.cheminFichier.length(), cheminFichier);
    }

    private boolean getRenommerProjet() {
        return this.jckbRenommerProjet.isSelected();
    }

    public final ActionListener jbxTypeSauvegardeActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            lblVersionProjet.setText(LBL_TYPE_SAUVEGARDE + GestionFichiersCabanon.obtenirVersionUlterieureProjet(
                    nomProjet.toString(), versionProjet.toString(), (TypeSauvegarde) jcbxTypeSauvegarde.getSelectedItem()));
            if (jcbxTypeSauvegarde.getSelectedIndex() == TypeSauvegarde.ECRASER_SAUVEGARDE.ordinal()) {
                btnSFE.setEnabled(false);
            } else {
                btnSFE.setEnabled(true);
            }
        }
    };

    private static WindowListener fermer = new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            e.getWindow().dispose();
        }
    };

    private final ActionListener reinitialiser = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jtxtNomProjet.setText(getNomProjet().toString());
            jckbRenommerProjet.setSelected(true);
            jcbxTypeSauvegarde.setSelectedIndex(0);
            btnSFE.reinitialiser(getCheminFichier().toString());
        }
    };

    private final ActionListener enregistrer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (champsValides()) {
                if (btnSFE.isEnabled()) {
                    setCheminFichier(btnSFE.obtenirCheminFichier());
                }
                App.getControleurCabanon().enregistrerProjet(
                        getCheminFichier().toString(),
                        getNomProjet().toString(),
                        getVersionProjet().toString(),
                        getRenommerProjet(),
                        (TypeSauvegarde) jcbxTypeSauvegarde.getSelectedItem());

                dialogue.dispose();
            }
        }
    };

    private final ActionListener annuler = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dialogue.dispose();
        }
    };

    private void initialiserDialogue() {
        this.jckbRenommerProjet.setSelected(true);

        this.btnReinitialiser.addActionListener(this.reinitialiser);
        this.btnEnregistrer.addActionListener(this.enregistrer);
        this.btnAnnuler.addActionListener(this.annuler);

        this.dialogue.addWindowListener(fermer);

        Container conteneur = this.dialogue.getContentPane();
        conteneur.setLayout(new BorderLayout());

        JPanel panneau = new JPanel(new GridBagLayout());
        panneau.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints grille = new GridBagConstraints();
        grille.fill = GridBagConstraints.BOTH;
        grille.gridwidth = GridBagConstraints.REMAINDER;

        grille.gridy = 0;
        panneau.add(new JLabel(this.LBL_NOM_PROJET), grille);
        grille.gridy++;
        jtxtNomProjet.setText(getNomProjet().toString());
        panneau.add(jtxtNomProjet, grille);

        if (!nomProjet.toString().equals("") && GestionFichiersCabanon.obtenirNomProjets().get(this.nomProjet.toString()).size() > 0) {
            this.btnSFE = new BtnSelecteurFichier(
                    new SelecteurFichierEnregistrer(new File(this.cheminFichier.toString())));
//            grille.gridy++;
//            panneau.add(this.jckbRenommerProjet, grille);
            grille.gridy++;
            panneau.add(this.lblVersionProjet, grille);
            grille.gridy++;
            this.jcbxTypeSauvegarde = new JComboBox<>(TypeSauvegarde.values());
            this.jcbxTypeSauvegarde.addActionListener(this.jbxTypeSauvegardeActionListener);
            this.jcbxTypeSauvegarde.setSelectedIndex(0);
            panneau.add(this.jcbxTypeSauvegarde, grille);

            grille.fill = GridBagConstraints.NONE;
            grille.gridy++;
            panneau.add(this.btnSFE.getLblCheminFichier(), grille);

            grille.gridy++;
            JPanel pSfe = new JPanel();
            pSfe.setBorder(new EmptyBorder(5, 0, 0, 0));
            pSfe.add(this.btnSFE);
            panneau.add(pSfe, grille);

            grille.gridy++;
            JPanel pReinitialiser = new JPanel();
            pReinitialiser.setBorder(new EmptyBorder(5, 0, 0, 0));
            pReinitialiser.add(this.btnReinitialiser);
            panneau.add(pReinitialiser, grille);

        } else {
            this.jcbxTypeSauvegarde = new JComboBox<>(new TypeSauvegarde[]{TypeSauvegarde.ECRASER_SAUVEGARDE});
            this.jcbxTypeSauvegarde.setSelectedIndex(0);

            this.btnSFE = new BtnSelecteurFichier(new SelecteurFichierEnregistrer());
            grille.fill = GridBagConstraints.NONE;
            grille.gridy++;
            panneau.add(this.btnSFE.getLblCheminFichier(), grille);

            grille.gridy++;
            JPanel pSfe = new JPanel();
            pSfe.setBorder(new EmptyBorder(5, 0, 0, 0));
            pSfe.add(this.btnSFE);
            panneau.add(pSfe, grille);
        }

        grille.fill = GridBagConstraints.BOTH;
        grille.gridy++;
        JPanel panneauActions = new JPanel();
        panneauActions.add(this.btnEnregistrer, grille);
        panneauActions.add(this.btnAnnuler, grille);

        panneau.add(panneauActions);
        conteneur.add(panneau, BorderLayout.CENTER);

        this.dialogue.setResizable(false);
        this.dialogue.setLocationRelativeTo(null);
        this.dialogue.pack();
    }

    public void AfficherDialogue(StringBuilder nomProjet, StringBuilder versionProjet, StringBuilder cheminFichier) {
        this.nomProjet = nomProjet;
        this.versionProjet = versionProjet;
        this.cheminFichier = cheminFichier;

        this.initialiserDialogue();
        this.dialogue.setVisible(true);
    }

    private boolean champsValides() {
        this.jtxtNomProjet.setBorder(new JTextField().getBorder());
        try {
            this.setNomProjet(this.jtxtNomProjet.getText());
        } catch (Exception e) {
            this.jtxtNomProjet.setBorder(new LineBorder(Color.RED));
            JOptionPane.showConfirmDialog(
                    null,
                    e.getMessage(),
                    this.NOM_PROJET_INVALIDE,
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}