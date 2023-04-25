package ca.ulaval.glo2004.domain.GestionCabanon.dtos;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.LigneEntremises;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;

import java.util.UUID;

public class LigneEntremisesDTO {
    public UUID id;
    public PointDTO centre;
    public ValeurImperiale decalageEntreEntremises;

    public LigneEntremisesDTO(LigneEntremises ligneEntremises) {
        this.id = ligneEntremises.getId();
        this.centre = new PointDTO(ligneEntremises.getCentre());
        this.decalageEntreEntremises = new ValeurImperiale(ligneEntremises.getDecalageEntreEntremises());
    }
}