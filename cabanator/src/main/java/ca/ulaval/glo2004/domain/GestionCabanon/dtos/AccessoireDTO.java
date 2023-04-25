package ca.ulaval.glo2004.domain.GestionCabanon.dtos;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Accessoire;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;

import java.util.UUID;

public abstract class AccessoireDTO {
    public UUID id;
    public PointDTO position;
    public ValeurImperiale largeur;
    public ValeurImperiale hauteur;

    public AccessoireDTO(Accessoire accessoire) {
        this.id = accessoire.getId();
        this.position = new PointDTO(accessoire.getCentre());
        this.largeur = new ValeurImperiale(accessoire.getLongueurTrou());
        this.hauteur = new ValeurImperiale(accessoire.getHauteurTrou());
    }
}