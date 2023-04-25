package ca.ulaval.glo2004.domain.GestionCabanon.Affichage;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Toit;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ToitAffichage {
    private final double angle;
    private final ArrayList<FermeAffichage> fermesAffichage;
    private final ArrayList<FermeSuppAffichage> fermesSuppAffichage;
    private final ArrayList<PlancheAffichage> planchesAffichage;
    private final double distanceEntreFermes;
    private final double distanceEntreFermesSupp;
    private final double longueurPorteAFaux;
    private final double longueur;
    private final double largueur;
    private final double hauteur;

    public ToitAffichage(Toit toit) {
        toit = toit.cloner();
        this.angle = toit.getAngle();
        this.fermesAffichage = toit.getFermes().stream().map(FermeAffichage::new)
                .collect(Collectors.toCollection(ArrayList::new));
        this.fermesSuppAffichage = toit.getFermesSupp().stream().map(FermeSuppAffichage::new)
                .collect(Collectors.toCollection(ArrayList::new));
        this.planchesAffichage = toit.getPlanches()
                .stream().map(PlancheAffichage::new)
                .collect(Collectors.toCollection(ArrayList::new));
        this.distanceEntreFermes = toit.getDistanceEntreFermes();
        this.distanceEntreFermesSupp = toit.getDistanceEntreFermesSupp();
        this.longueurPorteAFaux = toit.getLongueurPorteAFaux();
        this.longueur = toit.getLongueur();
        this.largueur = toit.getLargeur();
        this.hauteur = toit.getHauteur();
    }

    public double getAngle() {
        return this.angle;
    }

    public ArrayList<FermeAffichage> getFermesAffichage() {
        return this.fermesAffichage;
    }

    public ArrayList<FermeSuppAffichage> getFermesSuppAffichage() {
        return this.fermesSuppAffichage;
    }

    public ArrayList<PlancheAffichage> getPlanchesAffichage() {
        return this.planchesAffichage;
    }

    public double getDistanceEntreFermes() {
        return this.distanceEntreFermes;
    }

    public double getDistanceEntreFermesSupp() {
        return this.distanceEntreFermesSupp;
    }

    public double getLongueurPorteAFaux() {
        return this.longueurPorteAFaux;
    }

    public double getLongueur() {
        return this.longueur;
    }

    public double getLargueur() {
        return this.largueur;
    }

    public double getHauteur() {
        return this.hauteur;
    }

    public ArrayList<PlancheAffichage> getExportationPlanches() {
        ArrayList<PlancheAffichage> plancheAffichages = new ArrayList<>();
        plancheAffichages.addAll(this.planchesAffichage);
        this.getFermesAffichage().forEach(f -> plancheAffichages.addAll(f.getPlanchesAffichage()));
        this.getFermesSuppAffichage().forEach(fs -> plancheAffichages.addAll(fs.getPlanchesAffichage()));
        return plancheAffichages;
    }
}