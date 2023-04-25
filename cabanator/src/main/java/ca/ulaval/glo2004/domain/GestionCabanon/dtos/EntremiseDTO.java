package ca.ulaval.glo2004.domain.GestionCabanon.dtos;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Entremise;

import java.util.UUID;

public class EntremiseDTO {
    public UUID id;
    public PointDTO position;

    public EntremiseDTO(Entremise entremise) {
        this.id = entremise.getId();
        this.position = new PointDTO(entremise.getPosition());
    }
}