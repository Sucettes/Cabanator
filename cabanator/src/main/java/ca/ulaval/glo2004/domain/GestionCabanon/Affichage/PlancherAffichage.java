package ca.ulaval.glo2004.domain.GestionCabanon.Affichage;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Plancher;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlancherAffichage {
    private final UUID id;
    private final double longueur;
    private final double largeur;
    private final ArrayList<LigneEntremisesAffichage> ligneEntremises;
    private final ArrayList<PlancheAffichage> planchesAffichage;

    public PlancherAffichage(Plancher plancher) {
        plancher = plancher.cloner();
        this.id = plancher.getId();
        this.longueur = plancher.getLongueur();
        this.largeur = plancher.getLargeur();
        this.ligneEntremises = plancher.getLigneEntremises()
                .stream().map(LigneEntremisesAffichage::new)
                .collect(Collectors.toCollection(ArrayList::new));
        this.planchesAffichage = plancher.getPlanches()
                .stream().map(PlancheAffichage::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public UUID getId() {
        return id;
    }

    public double getLongueur() {
        return longueur;
    }

    public double getLargeur() {
        return largeur;
    }

    public ArrayList<LigneEntremisesAffichage> getLigneEntremises() {
        return ligneEntremises;
    }

    public ArrayList<PlancheAffichage> getPlanchesAffichage() {
        return this.planchesAffichage;
    }

    public ArrayList<PlancheAffichage> getExportationPlanches() {
        ArrayList<PlancheAffichage> plancheAffichages = new ArrayList<>();
        plancheAffichages.addAll(this.planchesAffichage);
        this.getLigneEntremises().forEach(le -> plancheAffichages.addAll(le.getEntremises()));
        return plancheAffichages;
    }
}