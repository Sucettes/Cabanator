package ca.ulaval.glo2004.gui.Dialogs;

import ca.ulaval.glo2004.Utilitaires.RessourceMap;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypeSauvegarde;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.GestionFichiersCabanon;
import ca.ulaval.glo2004.gui.Navigateur;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.*;

public class OuvrirProjetDialog {
    private final String NOM_DIALOGUE = "Ouvrir un projet";
    private final String NOM_BTN_REINITIALISER = "Réinitialiser";
    private JButton btnOuvrir = new JButton("Ouvrir");
    private JButton btnReinitialiser = new JButton(this.NOM_BTN_REINITIALISER);
    private BtnSelecteurFichier btnSFO;
    private JDialog dialogue = new JDialog(null, this.NOM_DIALOGUE, Dialog.ModalityType.APPLICATION_MODAL);
    private JTree jTree;

    private static WindowListener fermer = new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            e.getWindow().dispose();
        }
    };

    private String obtenirNomProjetNoeudSelectionne(DefaultMutableTreeNode noeud) throws NoSuchFieldException, IllegalAccessException {
        if (noeud != null) {
            Object objectNoeud = noeud.getUserObject();
            try {
                return (String) objectNoeud.getClass().getField("nomProjet").get(objectNoeud);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                System.out.println("Erreur : " + ex.getMessage());
                throw ex;
            }
        }
        throw new InvalidParameterException("le noeud est null");
    }

    private String[] obtenirInfosProjetNoeudSelectionne(DefaultMutableTreeNode noeud) throws NoSuchFieldException, IllegalAccessException {
        if (noeud != null) {
            Object objectNoeud = noeud.getUserObject();
            try {
                return (String[]) objectNoeud.getClass().getField("infosProjet").get(objectNoeud);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                System.out.println("Erreur : " + ex.getMessage());
                throw ex;
            }
        }
        throw new InvalidParameterException("le noeud est null");
    }

    private final ActionListener ouvrir = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultMutableTreeNode noeud = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
            if (noeud != null) {
                try {
                    String nomProjet = obtenirNomProjetNoeudSelectionne(noeud);
                    String[] infosProjet = obtenirInfosProjetNoeudSelectionne(noeud);
                    dialogue.dispose();
                    Navigateur.afficherFenetrePrincipale(new StringBuilder(nomProjet), infosProjet);
                } catch (NoSuchFieldException | IllegalAccessException | InvalidParameterException ex) {
                    System.out.println("Erreur : " + ex.getMessage());
                }
            } else if (btnSFO.obtenirCheminFichier().contains(GestionFichiersCabanon.EXTENSION_FICHIER)) {
                String nomFichier = new File(btnSFO.obtenirCheminFichier()).getName().replace(GestionFichiersCabanon.EXTENSION_FICHIER, "");
                nomFichier = String.join(GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_NOM_SAUVEGARDE, new String[]{nomFichier, btnSFO.obtenirCheminFichier()});
                String[] partiesNomFichier = nomFichier.split(GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_NOM_SAUVEGARDE);
                dialogue.dispose();
                Navigateur.afficherFenetrePrincipale(new StringBuilder(nomFichier), partiesNomFichier);
            }
        }
    };

    private final ActionListener reinitialiser = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            btnSFO.reinitialiser("");
            dialogue.pack();
        }
    };

    private final ActionListener choisirFichier = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jTree.clearSelection();
            dialogue.pack();
        }
    };

    private final TreeSelectionListener selectionnerNoeud = new TreeSelectionListener() {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode noeud = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
            if (noeud != null) {
                try {
                    String[] infosProjet = obtenirInfosProjetNoeudSelectionne(noeud);
                    btnSFO.getLblCheminFichier().setText(infosProjet[3]);
                    dialogue.pack();
                } catch (NoSuchFieldException | IllegalAccessException | InvalidParameterException ex) {
                    System.out.println("Erreur : " + ex.getMessage());
                }
            }
        }
    };

    private DefaultMutableTreeNode genererNoeudsDeJTree() {
        DefaultMutableTreeNode racine = new DefaultMutableTreeNode("Projets");

        HashMap<String, ArrayList<String>> nomsProjet = GestionFichiersCabanon.obtenirNomProjets();
        for (Map.Entry<String, ArrayList<String>> entry : nomsProjet.entrySet()) {
            String nom = entry.getKey();
            ArrayList<String> versions = entry.getValue();

            DefaultMutableTreeNode projet;

            if (versions.size() > 0) {
                if (versions.size() > 1) {
                    projet = new DefaultMutableTreeNode(nom);

                    for (String version : versions) {
                        projet.add(new DefaultMutableTreeNode(new Object() {
                            public final String nomProjet = nom;
                            public final String[] infosProjet = version
                                    .split("\\" + GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_INFOS_PROJETS);

                            @Override
                            public String toString() {
                                String affichage = "";
                                if (this.nomProjet != this.infosProjet[0]) {
                                    affichage += this.nomProjet + " | ";
                                }
                                return affichage + this.infosProjet[1] + " | Date de modification  : " + this.infosProjet[2];
                            }
                        }));
                    }
                } else {
                    projet = new DefaultMutableTreeNode(new Object() {
                        public final String nomProjet = nom;
                        public final String[] infosProjet = versions.get(0)
                                .split("\\" + GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_INFOS_PROJETS);

                        @Override
                        public String toString() {
                            return nomProjet
                                    + " | " + this.infosProjet[1]
                                    + " | Date de modification  : " + this.infosProjet[2];
                        }
                    });
                }

                racine.add(projet);
            }
        }

        return racine;
    }

    private void initialiserDialogue() {
        btnOuvrir.addActionListener(ouvrir);
        dialogue.addWindowListener(fermer);
        this.btnSFO = new BtnSelecteurFichier(new SelecteurFichierOuvrir());
        this.btnReinitialiser.addActionListener(this.reinitialiser);
        this.btnSFO.addActionListener(this.choisirFichier);
        this.jTree = new JTree(this.genererNoeudsDeJTree());
        // https://stackoverflow.com/questions/20389178/selecting-only-leafs-in-jtree
        this.jTree.setSelectionModel(new DefaultTreeSelectionModel() {
            private TreePath[] obtenirCheminsPourSelection(TreePath[] chemins)
            {
                ArrayList<TreePath> cheminsArbre = new ArrayList<>();
                for (TreePath cheminArbre : chemins) {
                    if (cheminArbre.getLastPathComponent() instanceof DefaultMutableTreeNode noeud && noeud.isLeaf()) {
                        cheminsArbre.add(cheminArbre);
                    }
                }

                return cheminsArbre.toArray(chemins);
            }

            @Override
            public void setSelectionPaths(TreePath[] chemins)
            {
                super.setSelectionPaths(obtenirCheminsPourSelection(chemins));
            }

            @Override
            public void addSelectionPaths(TreePath[] chemins)
            {
                super.addSelectionPaths(obtenirCheminsPourSelection(chemins));
            }
        });

        this.jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        ImageIcon leafIcon = new ImageIcon(Objects.requireNonNull(
                getClass().getResource(RessourceMap.ICONE_JTREE_LEAF_ICON.obtenirChemin())));
        ImageIcon openIcon = new ImageIcon(Objects.requireNonNull
                (getClass().getResource(RessourceMap.ICONE_JTREE_OPEN_ICON.obtenirChemin())));
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
//        renderer.setLeafIcon(leafIcon); // mettre en commentaire si trop laid
        renderer.setLeafIcon(null); // décommenter si trop laid
        renderer.setOpenIcon(openIcon);
        renderer.setClosedIcon(openIcon);
        renderer.setIcon(openIcon);
        renderer.setBorder(new EmptyBorder(3, 2, 3, 2));
        this.jTree.setCellRenderer(renderer);
        JScrollPane treeView = new JScrollPane(this.jTree);

        Dimension minimumSize = new Dimension(500, 200);
        treeView.setMinimumSize(minimumSize);
        treeView.setPreferredSize(minimumSize);
        treeView.setVerticalScrollBar(new JScrollBar(Adjustable.VERTICAL));
        treeView.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        treeView.setHorizontalScrollBar(new JScrollBar(Adjustable.HORIZONTAL));
        treeView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.jTree.addTreeSelectionListener(this.selectionnerNoeud);
        Container conteneur = dialogue.getContentPane();
        conteneur.setLayout(new BorderLayout());

        JPanel panneau = new JPanel(new BorderLayout());
        panneau.setBorder(new EmptyBorder(10, 10, 10, 10));

        panneau.add(treeView, BorderLayout.NORTH);

        JPanel conteneurCheminFichier = new JPanel(new GridBagLayout());
        conteneurCheminFichier.setBorder(new EmptyBorder(5, 5, 5, 5));

        GridBagConstraints grille = new GridBagConstraints();
        grille.fill = GridBagConstraints.BOTH;
        grille.gridwidth = GridBagConstraints.REMAINDER;
        grille.gridy = 0;
        conteneurCheminFichier.add(this.btnSFO.getLblCheminFichier(), grille);

        grille.gridy++;
        JPanel pSFO = new JPanel();
        pSFO.setBorder(new EmptyBorder(5, 0, 0, 0));
        pSFO.add(this.btnSFO);
        conteneurCheminFichier.add(pSFO, grille);

        grille.gridy++;
        JPanel pReinitialiser = new JPanel();
        pReinitialiser.setBorder(new EmptyBorder(5, 0, 0, 0));
        pReinitialiser.add(this.btnReinitialiser);
        conteneurCheminFichier.add(pReinitialiser, grille);

        conteneur.add(panneau, BorderLayout.NORTH);
        panneau.add(conteneurCheminFichier, BorderLayout.CENTER);
        panneau.add(this.btnOuvrir, BorderLayout.SOUTH);

        dialogue.pack();
        dialogue.setResizable(false);
        dialogue.setLocationRelativeTo(null);
    }

    public void AfficherDialogue() {
        this.initialiserDialogue();
        dialogue.setVisible(true);
    }
}