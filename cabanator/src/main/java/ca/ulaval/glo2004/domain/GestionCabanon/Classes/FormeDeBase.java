package ca.ulaval.glo2004.domain.GestionCabanon.Classes;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.util.ArrayList;

public abstract class FormeDeBase implements java.io.Serializable {
    private Point origine;

    public Point getOrigine() {
        return this.origine;
    }

    protected void setOrigine(Point origine) {
        this.origine = origine;
    }

    protected abstract ArrayList<Planche> getPlanches();
}