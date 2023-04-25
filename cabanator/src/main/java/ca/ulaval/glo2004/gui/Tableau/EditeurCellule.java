package ca.ulaval.glo2004.gui.Tableau;

import javax.swing.*;
import java.awt.*;

public abstract class EditeurCellule extends DefaultCellEditor {
    public EditeurCellule(JTextField champTexte) {
        super(champTexte);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JTextField champTexte = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        champTexte.setFont(new Font("Dubai", Font.PLAIN, 22));
        return champTexte;
    }
}