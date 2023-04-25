package ca.ulaval.glo2004.gui.Tableau;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ChampImperial extends JTextField {
    // https://stackoverflow.com/a/54909902
    private static final String REGEX = "^\\d+(?:'(?: ?\\d+\\\"(?: ?\\d+\\/\\d+)?)?|\\\")$";

    private String ancienneValeur;

    public ChampImperial() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                ancienneValeur = getText();
            }

            @Override
            public void focusLost(FocusEvent e) {
                terminerEdition();
            }
        });
    }

    public void terminerEdition() {
        if (!getText().matches(REGEX))
            setText(ancienneValeur);

        ancienneValeur = "";
    }
}