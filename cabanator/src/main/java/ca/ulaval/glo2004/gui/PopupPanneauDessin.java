package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.GestionCabanon.ControleurCabanon;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.PointDTO;

import javax.swing.*;
import java.awt.*;

import static ca.ulaval.glo2004.gui.Vue.getPointCardinal;

public class PopupPanneauDessin extends JPopupMenu {
    public PopupPanneauDessin(ControleurCabanon controleurCabanon, Point point, Vue vue, PanneauDessin panneauDessin) {
        final JMenuItem ajouterEntremise = new JMenuItem("Ajouter une entremise ici");

        ajouterEntremise.addActionListener(e -> {
            final PointDTO pointDTO = new PointDTO(point.getX(), point.getY(), 0);
            controleurCabanon.ajouterEntremise(pointDTO, getPointCardinal(vue));
            panneauDessin.regenerateVue(vue);
        });

        add(ajouterEntremise);
    }
}