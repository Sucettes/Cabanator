package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.LigneEntremisesAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancheAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancherAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Plancher;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.gui.Couleurs;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Plancher3D extends Objet3D implements IObjet3DCompose, IComposanteCabanon3D {
    ArrayList<Planche3D> planches3D;
    private Dimension3D dimension;

    public Plancher3D(PlancherAffichage plancher, Color couleur) {
        super();
        this.id = plancher.getId();
        this.couleur = couleur;
        this.position = new Point3D();
        this.dimension = new Dimension3D(plancher.getLongueur(), plancher.getLargeur(), 0.2);
        this.planches3D = new ArrayList<>();

        ArrayList<PlancheAffichage> planches = plancher.getPlanchesAffichage();
        ArrayList<LigneEntremisesAffichage> entremises = plancher.getLigneEntremises();
        entremises.forEach(entremise -> planches.addAll(entremise.getEntremises()));

        planches.forEach(planche -> planches3D.add(new Planche3D(planche, couleur)));

        this.polygons = planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new); //On récupère tous les polygones des planches3D et on les met dans un tableau.
    }

    public Plancher3D(Plancher3D plancher3D) {
        super();
        this.id = plancher3D.id;
        this.couleur = plancher3D.couleur;
        this.position = plancher3D.position;
        this.dimension = plancher3D.dimension;
        this.planches3D = plancher3D.planches3D.stream().map(Planche3D::new).collect(Collectors.toCollection(ArrayList::new));

        this.polygons = planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new); //On récupère tous les polygones des planches3D et on les met dans un tableau.
    }


    @Override
    public Objet3D getComposante(Polygon3D poly) {
        return this.planches3D
                .stream()
                .filter(planche -> planche.contient(poly)) //On filtre les planches3D pour ne garder que celles qui contiennent le poly.
                .findFirst()//On prend le premier élément de la liste (car il n'y en a qu'un seul (normalement))
                .orElse(null); //Si une planche a été trouvée, on la retourne. Sinon, on retourne null.
    }

    @Override
    public boolean contient(Objet3D objet) {
        if (objet instanceof Planche3D) {
            return this.planches3D.contains(objet);
        } else if (objet instanceof Polygon3D) {
            return Arrays.asList(this.polygons).contains(objet);
        }
        return false;
    }
    @Override
    public Vecteur getNormal(Polygon3D polygon3D) {
        for (Planche3D planche3D : planches3D) {
            if (planche3D.contient(polygon3D)) {
                return planche3D.getNormal(polygon3D);
            }
        }
        return null;
    }

    @Override
    public void rotate(double xAngle, double yAngle, double zAngle) {

    }

    @Override
    public void rotateAroundCorner(double xAngle, double yAngle, double zAngle) {

    }

    @Override
    public void rotateAroundPoint(Point3D pivot, double xAngle, double yAngle, double zAngle) {

        // Mise à jour des angles de rotation
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        // Rotation des points de chaque polygon
        for (Planche3D planche : planches3D) {
            planche.rotateAroundPoint(pivot, xAngle, yAngle, zAngle);
        }
    }

    @Override
    public void faceTo(Point3D point) {

    }

    public Plancher3D scale(double scale) {
        Plancher3D plancher3D = new Plancher3D(this);
        for (int i = 0; i < plancher3D.planches3D.size(); i++) {
            plancher3D.planches3D.set(i, planches3D.get(i).scale(scale));
        }
        plancher3D.polygons = plancher3D.planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new);
        plancher3D.dimension = new Dimension3D(dimension.longueur * scale, dimension.largeur * scale, dimension.hauteur * scale);
        plancher3D.position = position.multiplier(scale);
        return plancher3D;

//        for (Planche3D planche : this.planches3D) {
//           planche.scale(scale);
//        }
//        return this;
    }

    @Override
    void update(Controleur3D controleur3D) {
        for (Planche3D planche : planches3D) {
            planche.update(controleur3D);
        }
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + id + "'\n" +
                ", position='" + getPosition() + "'" +
                ", dimension=" + dimension.longueur + " " + dimension.largeur + " " + dimension.hauteur +
                ", couleur='" + getCouleur() + "'" +
                ", rX=" + String.format("%.3f", rotationX) +
                ", rY=" + String.format("%.3f", rotationY) +
                ", rZ=" + String.format("%.3f", rotationZ) +
                ", visible='" + isVisible() + "'" +
                "}";
    }
}
