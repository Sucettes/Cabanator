package ca.ulaval.glo2004.gui.Tableau;

public class EditeurCelluleArgent extends EditeurCellule {
    private static final ChampArgent CHAMP_ARGENT = new ChampArgent();

    public EditeurCelluleArgent() {
        super(CHAMP_ARGENT);
    }

    @Override
    public boolean stopCellEditing() {
        CHAMP_ARGENT.terminerEdition();
        return super.stopCellEditing();
    }
}