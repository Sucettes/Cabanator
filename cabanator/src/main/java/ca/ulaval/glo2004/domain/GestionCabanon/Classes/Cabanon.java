package ca.ulaval.glo2004.domain.GestionCabanon.Classes;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Interfaces.IClonable;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

public class Cabanon implements IClonable<Cabanon>, java.io.Serializable {
    private String nom;
    private Plancher plancher;
    private Murs murs;
    private Toit toit;

    public Cabanon(Plancher plancher, Murs murs, Toit toit, String nom) {
        this.setPlancher(plancher);
        this.setMurs(murs);
        this.setToit(toit);
        this.nom = nom;
    }

    public Cabanon(Dimension3D dimension) {
        this.nom = "";
        Plancher plancher = new Plancher(dimension.longueur, dimension.largeur);
        this.setPlancher(plancher);
        this.setMurs(new Murs(
                plancher.getOrigine().getY() + plancher.getEpaisseurPlancher(),
                dimension.hauteur,
                this.plancher.getLongueur(),
                this.plancher.getLargeur()));
        Murs murs = this.getMurs();
        Point pt = this.getMurs().getMur(PointCardinal.OUEST).getOrigine();
        pt = pt.configurerY(pt.getY() + murs.getHauteur());
        this.setToit(new Toit(
                pt,
                dimension.longueur,
                dimension.largeur
        ));
    }

    //#region PROPRIÉTÉS ET PROPRIÉTÉS ET INDEXEURS

    public Plancher getPlancher() {
        return this.plancher;
    }

    private void setPlancher(Plancher plancher) {
        this.plancher = plancher;
    }

    public Murs getMurs() {
        return this.murs;
    }

    private void setMurs(Murs murs) {
        this.murs = murs;
    }

    public Toit getToit() {
        return this.toit;
    }

    private void setToit(Toit toit) {
        this.toit = toit;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    //#endregion

    //#region MÉTHODES

    public Dimension3D getDimensions() {
        return new Dimension3D(plancher.getLongueur(), plancher.getLargeur(), plancher.getEpaisseurPlancher() + murs.getHauteur() + toit.getHauteur());
    }

    public void regenererStructure() {
        this.getPlancher().regenererStructure();
        this.getMurs().regenererStructureMurs();
        this.getToit().regenererStructure();
    }

    @Override
    public Cabanon cloner() {
        return new Cabanon(
                this.getPlancher().cloner(),
                this.getMurs().cloner(),
                this.getToit().cloner(),
                String.copyValueOf(this.getNom().toCharArray()));
    }

    //#endregion
}