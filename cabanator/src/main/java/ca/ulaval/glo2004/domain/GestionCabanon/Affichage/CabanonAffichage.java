package ca.ulaval.glo2004.domain.GestionCabanon.Affichage;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Cabanon;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Murs;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Plancher;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;

import java.util.ArrayList;

public class CabanonAffichage {
    private final Dimension3D dimension;
    private final PlancherAffichage plancher;
    private final ArrayList<MurAffichage> murs;
    private final ToitAffichage toit;

    public CabanonAffichage(Cabanon cabanon) {
        this.dimension = cabanon.getDimensions();
        this.plancher = new PlancherAffichage(cabanon.getPlancher());

        this.murs = new ArrayList<>();

        for (PointCardinal pointCardinal : PointCardinal.values()) {
            this.murs.add(new MurAffichage(cabanon.getMurs().getMur(pointCardinal)));
        }

        this.toit = new ToitAffichage(cabanon.getToit());
    }

    public Dimension3D getDimension() {
        return this.dimension;
    }

    public PlancherAffichage getPlancher() {
        return this.plancher;
    }

    public ArrayList<MurAffichage> getMurs() {
        return this.murs;
    }

    public ToitAffichage getToit() {
        return this.toit;
    }

    public ArrayList<PlancheAffichage> getPlanches() {
        ArrayList<PlancheAffichage> planches = new ArrayList<>();

        planches.addAll(this.getPlancher().getPlanchesAffichage());
        this.getMurs().forEach(mur -> planches.addAll(mur.getPlanchesAffichage()));
        planches.addAll(this.getToit().getPlanchesAffichage());

        return planches;
    }

    public ArrayList<PlancheAffichage> getExportationplanches() {
        ArrayList<PlancheAffichage> planches = new ArrayList<>();
        planches.addAll(this.getPlancher().getExportationPlanches());
        this.getMurs().forEach(mur -> planches.addAll(mur.getExportationPlanches()));
        planches.addAll(this.getToit().getExportationPlanches());

        return planches;
    }
}