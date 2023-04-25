package ca.ulaval.glo2004.gui.Tableau;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

public class TableauDeuxColonnes extends Tableau {
    private final EditeurCelluleAngle editeurCelluleAngle = new EditeurCelluleAngle();
    private final EditeurCelluleArgent editeurCelluleArgent = new EditeurCelluleArgent();
    private final EditeurCelluleImperial editeurCelluleImperial = new EditeurCelluleImperial();

    private Champ[] champs;

    public TableauDeuxColonnes() {
        super(false);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                if (columnAtPoint(e.getPoint()) == 1)
                    setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                else
                    setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    public void creerModele(Object[][] donnees) {
        this.champs = null;
        preparerTableau(donnees);
    }

    public void creerModele(Object[][] donnees, Champ[] champs) {
        this.champs = champs;
        preparerTableau(donnees);
    }

    private void preparerTableau(Object[][] donnees) {
        creerModele(donnees, String.class, String.class);
        getColumnModel().getColumn(0).setMinWidth(280);
        getColumnModel().getColumn(1).setMinWidth(160);
    }

    public void definirLignesModifiables(int[] lignesModifiables) {
        this.lignesModifiables = lignesModifiables;
    }

    public void arreterEdition() {
        editeurCelluleAngle.stopCellEditing();
        editeurCelluleImperial.stopCellEditing();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0) return false;

        for (int ligneModifiable : lignesModifiables)
            if (row == ligneModifiable) return true;

        return false;
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        if (champs == null)
            return editeurCelluleImperial;

        return switch (champs[row]) {
            case ANGLE -> editeurCelluleAngle;
            case ARGENT -> editeurCelluleArgent;
            case IMPERIAL -> editeurCelluleImperial;
        };
    }

    public static Optional<String> obtenirDonnee(TableModel donnees, int ligne) {
        try {
            return Optional.of((String) donnees.getValueAt(ligne, 1));
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }
}