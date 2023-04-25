package ca.ulaval.glo2004.domain.GestionCabanon.dtos;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Fenetre;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;

public class FenetreDTO extends AccessoireDTO {
    public String typeLinteau;
    public ValeurImperiale distanceEntreMontants;

    public FenetreDTO(Fenetre fenetre) {
        super(fenetre);

        this.typeLinteau = fenetre.getTypeLinteau().toString();
        this.distanceEntreMontants = new ValeurImperiale(fenetre.getDistanceEntreMontants());
    }
}