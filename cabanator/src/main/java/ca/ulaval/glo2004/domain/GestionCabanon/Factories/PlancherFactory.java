package ca.ulaval.glo2004.domain.GestionCabanon.Factories;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Plancher;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlancherFactory extends Plancher {
    private PlancherFactory(Plancher plancher) {
        super(plancher.getLongueur(),
                plancher.getLargeur(),
                plancher.getDistanceEntreSolives(),
                plancher.getLigneEntremises()
                        .stream().map(LigneEntremisesFactory::FromLigneEntremises)
                        .collect(Collectors.toCollection(ArrayList::new)));
    }

    public static PlancherFactory FromPlancher(Plancher plancher) {
        return new PlancherFactory(plancher);
    }
}