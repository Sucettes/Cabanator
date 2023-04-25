package ca.ulaval.glo2004.gui.Tableau;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ChampArgent extends JTextField {
    private static final String UNITE = "$";

    private boolean enEdition;
    private String ancienneValeur;

    public static String obtenirArgent(double valeur) {
        return String.format("%.2f%s", valeur, UNITE);
    }

    public static double obtenirArgent(String valeur) {
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
                if (!getText().matches("\\d{0,2}(\\.\\d{0,2})?"))
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

    public ChampArgent() {
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

        if (getText().isBlank())
            definirTexte(ancienneValeur);
        else
            definirTexte(getText() + UNITE);

        enEdition = false;
        ancienneValeur = "";
    }

    private void definirTexte(String texte) {
        getDocument().removeDocumentListener(listener);
        setText(texte);
        getDocument().addDocumentListener(listener);
    }
}