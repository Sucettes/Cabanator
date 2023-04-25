package ca.ulaval.glo2004.domain.GestionCabanon.Affichage;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Fenetre;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Porte;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Mur;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class MurAffichage {
    private final UUID id;
    private final PointCardinal positionMur;
    private final PointCardinal pointCardinal;
    private final Point position;
    private final double hauteur;
    private final double longueur;
    private final ArrayList<PlancheAffichage> planchesAffichage;
    private final ArrayList<AccessoireAffichage> accessoires;

    public MurAffichage(Mur mur) {
        mur = mur.cloner();
        this.id = mur.getId();
        this.positionMur = mur.getPositionMur();
        this.pointCardinal = mur.getPointCardinal();
        this.position = mur.getOrigine();
        this.hauteur = mur.getHauteur();
        this.longueur = mur.getLongueur();
        this.planchesAffichage = mur.getPlanches()
                .stream().map(PlancheAffichage::new)
                .collect(Collectors.toCollection(ArrayList::new));
        this.accessoires = mur.getAccessoires()
                .stream().map(accessoire -> {
                    if (accessoire instanceof Porte porte)
                        return new PorteAffichage(porte);
                    else if (accessoire instanceof Fenetre fenetre)
                        return new FenetreAffichage(fenetre);
                    return null;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public UUID getId() {
        return this.id;
    }

    public PointCardinal getPointCardinal() {
        return this.pointCardinal;
    }

    public Point getPosition() {
        return this.position;
    }

    public double getHauteur() {
        return this.hauteur;
    }

    public double getLongueur() {
        return this.longueur;
    }

    public ArrayList<PlancheAffichage> getPlanchesAffichage() {
        return this.planchesAffichage;
    }

    public ArrayList<AccessoireAffichage> getAccessoires() {
        return this.accessoires;
    }

    public PointCardinal getPositionMur() {
        return this.positionMur;
    }

    public ArrayList<PlancheAffichage> getExportationPlanches() {
        ArrayList<PlancheAffichage> plancheAffichages = new ArrayList<>();
        plancheAffichages.addAll(this.planchesAffichage);
        this.getAccessoires().forEach(a -> plancheAffichages.addAll(a.getPlanchesAffichage()));
        return plancheAffichages;
    }
}