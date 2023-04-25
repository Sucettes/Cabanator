package ca.ulaval.glo2004.gui.Tableau;

public class EditeurCelluleImperial extends EditeurCellule {
    private static final ChampImperial CHAMP_IMPERIAL = new ChampImperial();

    public EditeurCelluleImperial() {
        super(CHAMP_IMPERIAL);
    }

    @Override
    public boolean stopCellEditing() {
        CHAMP_IMPERIAL.terminerEdition();
        return super.stopCellEditing();
    }
}