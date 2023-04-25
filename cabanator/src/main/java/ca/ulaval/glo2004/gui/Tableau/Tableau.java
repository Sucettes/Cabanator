package ca.ulaval.glo2004.gui.Tableau;

import ca.ulaval.glo2004.gui.Couleurs;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public abstract class Tableau extends JTable {
    private final boolean colonneDroitePleine;
    private Color[] couleursLignes = null;
    int[] lignesModifiables = new int[]{};

    public Tableau(boolean colonneDroitePleine) {
        super();

        this.colonneDroitePleine = colonneDroitePleine;

        creerTableau();
    }

    public Tableau(boolean colonneDroitePleine, Color[] couleursLignes) {
        super();

        this.colonneDroitePleine = colonneDroitePleine;
        this.couleursLignes = couleursLignes;

        creerTableau();
    }

    private void creerTableau() {
        setRowHeight(50);
        setShowGrid(false);
        setRowSelectionAllowed(false);
        setCellSelectionEnabled(false);
        setColumnSelectionAllowed(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setIntercellSpacing(new Dimension(0, 0));
    }

    // https://stackoverflow.com/a/14771059
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        final JComponent composant = (JComponent) super.prepareRenderer(renderer, row, column);

        final Color couleurLigne = couleursLignes != null && row < couleursLignes.length ? couleursLignes[row] : Couleurs.GRIS_PALE;
        final Border bordureLigne = BorderFactory.createLineBorder(couleurLigne, 3);
        final Border bordureVide = BorderFactory.createEmptyBorder(0, 10, 0, 10);

        composant.setBorder(BorderFactory.createCompoundBorder(bordureLigne, bordureVide));
        composant.setBackground(column > 0 && !colonneDroitePleine ? Couleurs.GRIS_MOYEN : couleurLigne);

        if (column > 0 && !colonneDroitePleine) {
            boolean modifiable = false;

            for (int ligneModifiable : lignesModifiables) {
                if (row == ligneModifiable) {
                    modifiable = true;
                    break;
                }
            }

            if (!modifiable)
                composant.setBackground(Couleurs.GRIS_PALE);
        }

        return composant;
    }

    // https://stackoverflow.com/a/25378364
    public void creerModele(Object[][] donnees, final Class<?> colonneGauche, final Class<?> colonneDroite) {
        setModel(new DefaultTableModel(donnees, new String[]{"", ""}) {
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0 -> colonneGauche;
                    case 1 -> colonneDroite;
                    default -> Object.class;
                };
            }
        });
    }
}