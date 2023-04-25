package ca.ulaval.glo2004.gui.Tableau;

public class EditeurCelluleAngle extends EditeurCellule {
    private static final ChampAngle CHAMP_ANGLE = new ChampAngle();

    public EditeurCelluleAngle() {
        super(CHAMP_ANGLE);
    }

    @Override
    public boolean stopCellEditing() {
        CHAMP_ANGLE.terminerEdition();
        return super.stopCellEditing();
    }
}