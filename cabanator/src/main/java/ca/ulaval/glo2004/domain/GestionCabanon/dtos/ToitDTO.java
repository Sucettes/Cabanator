package ca.ulaval.glo2004.domain.GestionCabanon.dtos;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Toit;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ToitDTO {
    public double angle;
    public ValeurImperiale distanceEntreFermes;
    public ValeurImperiale distanceEntreFermesSupp;
    public ValeurImperiale longueurPorteAFaux;
    public ValeurImperiale distanceEntreEntremises;

    public ToitDTO(Toit toit) {
        this.angle = toit.getAngle();
        this.distanceEntreFermes = new ValeurImperiale(toit.getDistanceEntreFermes());
        this.distanceEntreFermesSupp = new ValeurImperiale(toit.getDistanceEntreFermesSupp());
        this.longueurPorteAFaux = new ValeurImperiale(toit.getLongueurPorteAFaux());
        this.distanceEntreEntremises = new ValeurImperiale(toit.getDistanceEntreEntremise());
    }
}