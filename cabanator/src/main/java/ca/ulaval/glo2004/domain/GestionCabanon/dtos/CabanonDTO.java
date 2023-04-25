package ca.ulaval.glo2004.domain.GestionCabanon.dtos;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Cabanon;

public class CabanonDTO {
    public Dimension3D dimension;

    public CabanonDTO(Cabanon cabanon) {
        this.dimension = cabanon.getDimensions();
    }
}