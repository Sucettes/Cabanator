package ca.ulaval.glo2004.domain.GestionCabanon.dtos;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Fenetre;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Porte;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Mur;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MurDTO {
    public PointCardinal positionMur;
    public ValeurImperiale hauteur;
    public ValeurImperiale longueur;
    public ValeurImperiale distanceEntreMontants;
    public ArrayList<EntremiseDTO> entremises;
    public ArrayList<AccessoireDTO> accessoires;
    public PointDTO position;

    public MurDTO(Mur mur) {
        this.positionMur = mur.getPositionMur();
        this.hauteur = new ValeurImperiale(mur.getHauteur());
        this.longueur = new ValeurImperiale(mur.getLongueur());
        this.distanceEntreMontants = new ValeurImperiale(mur.getDistanceEntreMontants());
        this.position = new PointDTO(mur.getOrigine());
        this.entremises = mur.getEntremises()
                .stream().map(EntremiseDTO::new)
                .collect(Collectors.toCollection(ArrayList::new));
        if (mur.getAccessoires() != null)
            this.accessoires = mur.getAccessoires()
                    .stream().map(accessoire -> {
                        if (accessoire instanceof Porte porte)
                            return new PorteDTO(porte);
                        else if (accessoire instanceof Fenetre fenetre)
                            return new FenetreDTO(fenetre);
                        return null;
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
    }
}