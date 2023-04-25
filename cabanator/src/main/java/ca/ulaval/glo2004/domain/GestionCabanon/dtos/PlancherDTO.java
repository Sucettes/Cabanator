package ca.ulaval.glo2004.domain.GestionCabanon.dtos;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Plancher;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlancherDTO {
    public ValeurImperiale longueur;
    public ValeurImperiale largeur;
    public ValeurImperiale distanceEntreSolives;
    public ArrayList<LigneEntremisesDTO> ligneEntremises;

    public PlancherDTO(Plancher plancher) {
        this.longueur = new ValeurImperiale(plancher.getLongueur());
        this.largeur = new ValeurImperiale(plancher.getLargeur());
        this.distanceEntreSolives = new ValeurImperiale(plancher.getDistanceEntreSolives());
        this.ligneEntremises = plancher.getLigneEntremises()
                .stream().map(LigneEntremisesDTO::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}