package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.FermeAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.FermeSuppAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancheAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.ToitAffichage;
import ca.ulaval.glo2004.gui.Couleurs;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Ferme3D extends Objet3D{
    public ArrayList<Planche3D> planches3D;
    public double angle;
    private static final Color[] COLORS = {Couleurs.BLEU, Couleurs.VERT, Couleurs.JAUNE, Couleurs.ORANGE}; // N E S O
//    private static final Color[] COLORS = {Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE}; // N E S O
    public Ferme3D(FermeAffichage fermeAffichage, ToitAffichage toitAffichage, Color color) {
        super();
        this.id = fermeAffichage.getId();
        this.planches3D = new ArrayList<>();
        this.couleur = color;
        this.angle = toitAffichage.getAngle();

        fermeAffichage.getPlanchesAffichage().forEach(planche -> planches3D.add(new Planche3D(planche, color, Ferme3D.class)));
        this.position = new Point3D(fermeAffichage.getPoint().getX(), fermeAffichage.getPoint().getY(), fermeAffichage.getPoint().getZ());
        this.polygons = planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new); //On récupère tous les polygones des planches3D et on les met dans un tableau.

    }

    public Ferme3D(FermeSuppAffichage fermeAffichage, ToitAffichage toitAffichage, Color color) {
        super();
        this.id = fermeAffichage.getId();
        this.planches3D = new ArrayList<>();
        this.couleur = color;
        this.angle = toitAffichage.getAngle();

        fermeAffichage.getPlanchesAffichage().forEach(planche -> planches3D.add(new Planche3D(planche,  color, Ferme3D.class)));
        this.position = new Point3D(fermeAffichage.getPoint().getX(), fermeAffichage.getPoint().getY(), fermeAffichage.getPoint().getZ());
        this.polygons = planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new); //On récupère tous les polygones des planches3D et on les met dans un tableau.
    }

    public Ferme3D(Ferme3D ferme3D) {
        super();
        this.id = ferme3D.id;
        this.planches3D = new ArrayList<>();
        this.couleur = ferme3D.couleur;
        this.angle = ferme3D.angle;
        ferme3D.planches3D.forEach(planche -> planches3D.add(new Planche3D(planche)));
        this.position = new Point3D(ferme3D.position);
        this.polygons = planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new); //On récupère tous les polygones des planches3D et on les met dans un tableau.
    }

    @Override
    public void rotate(double xAngle, double yAngle, double zAngle) {

    }

    @Override
    public void rotateAroundCorner(double xAngle, double yAngle, double zAngle) {

    }

    @Override
    public Vecteur getNormal(Polygon3D polygon3D) {
        return null;
    }

    @Override
    public void rotateAroundPoint(Point3D pivot, double xAngle, double yAngle, double zAngle) {

    }

    @Override
    public void faceTo(Point3D point) {

    }

    @Override
    void update(Controleur3D controleur3D) {

    }

    @Override
    public <T extends Objet3D> T scale(double scale) {
        return null;
    }

    @Override
    public String toString() {
        return "Ferme{" +
                "position=" + position +
                ", rX=" + String.format("%.3f", rotationX) +
                ", rY=" + String.format("%.3f", rotationY) +
                ", rZ=" + String.format("%.3f", rotationZ) +
                '}';
    }
}
