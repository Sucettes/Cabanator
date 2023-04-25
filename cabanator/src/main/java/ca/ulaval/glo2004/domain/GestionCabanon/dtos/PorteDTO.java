package ca.ulaval.glo2004.domain.GestionCabanon.dtos;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Porte;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;

public class PorteDTO extends AccessoireDTO {
    public String typeLinteau;
    public ValeurImperiale distanceEntreMontants;

    public PorteDTO(Porte porte) {
        super(porte);

        this.typeLinteau = porte.getTypeLinteau().toString();
        this.distanceEntreMontants = new ValeurImperiale(porte.getDistanceEntreMontants());
    }
}