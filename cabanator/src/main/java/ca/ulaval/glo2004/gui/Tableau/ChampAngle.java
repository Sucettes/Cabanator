package ca.ulaval.glo2004.gui.Tableau;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ChampAngle extends JTextField {
    private static final String UNITE = "°";
    private static final double VALEUR_MINIMALE = 0;
    private static final double VALEUR_MAXIMALE = 180;

    private boolean enEdition;
    private String ancienneValeur;

    public static String obtenirAngle(double valeur) {
        return valeur + UNITE;
    }

    public static double obtenirAngle(String valeur) {
        return Double.parseDouble(valeur.replace(UNITE, ""));
    }

    private final DocumentListener listener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            if (!enEdition) {
                enEdition = true;
                return;
            }

            SwingUtilities.invokeLater(() -> {
                if (!getText().matches("\\d{0,3}(\\.\\d{0,3})?"))
                    setText("");
            });
        }

        @Override
        public void removeUpdate(DocumentEvent e) {

        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    };

    public ChampAngle() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                ancienneValeur = getText();
                definirTexte(getText().replace(UNITE, ""));
            }

            @Override
            public void focusLost(FocusEvent e) {
                terminerEdition();
            }
        });
    }

    public void terminerEdition() {
        if (getText().contains(UNITE)) return;

        if (getText().isBlank() || !angleValide(Double.parseDouble(getText())))
            definirTexte(ancienneValeur);
        else
            definirTexte(getText() + UNITE);

        enEdition = false;
        ancienneValeur = "";
    }

    private boolean angleValide(double angle) {
        return angle > VALEUR_MINIMALE && angle <= VALEUR_MAXIMALE;
    }

    private void definirTexte(String texte) {
        getDocument().removeDocumentListener(listener);
        setText(texte);
        getDocument().addDocumentListener(listener);
    }
}