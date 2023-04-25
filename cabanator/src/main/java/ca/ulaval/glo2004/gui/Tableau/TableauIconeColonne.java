package ca.ulaval.glo2004.gui.Tableau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TableauIconeColonne extends Tableau {
    public TableauIconeColonne(Color[] couleursLignes) {
        super(true, couleursLignes);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                if (columnAtPoint(e.getPoint()) == 0)
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                else
                    setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    public void creerModele(Object[][] donnees) {
        creerModele(donnees, ImageIcon.class, String.class);
        getColumnModel().getColumn(0).setMaxWidth(50);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}