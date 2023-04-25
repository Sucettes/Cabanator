package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancheAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PorteAffichage;
import ca.ulaval.glo2004.Utilitaires.Dimension3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Porte3D extends Objet3D  {
    public Dimension3D dimension;
    public ArrayList<Planche3D> planches3D;
    public Porte3D(PorteAffichage porteAffichage, Color color) {
        super();
        this.id = porteAffichage.getId();
        this.dimension = new Dimension3D(porteAffichage.getLargeur(), 0.2, porteAffichage.getHauteur());
        this.position = new Point3D(porteAffichage.getPosition().getX(), porteAffichage.getPosition().getY(), porteAffichage.getPosition().getZ());
        this.couleur = color;
        planches3D = new ArrayList<>();
        ArrayList<PlancheAffichage> planches = porteAffichage.getPlanchesAffichage();

        planches.forEach(planche -> planches3D.add(new Planche3D(planche, color)));

        this.polygons = planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new); //On récupère tous les polygones des planches3D et on les met dans un tableau.
    }

    public Porte3D(Porte3D porte3D) {
        super();
        this.id = porte3D.id;
        this.dimension = porte3D.dimension;
        this.position = porte3D.position;
        this.couleur = porte3D.couleur;
        this.planches3D = porte3D.planches3D.stream().map(Planche3D::new).collect(Collectors.toCollection(ArrayList::new));
        this.polygons = planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new);

    }

    @Override
    public void rotate(double xAngle, double yAngle, double zAngle) {
        // Mise à jour des angles de rotation
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        // Rotation des points de chaque polygon
        for (Polygon3D polygon : polygons) {
            for (Point3D point : polygon.points) {
                // Translation du point pour qu'il soit centré sur l'origine
                point.x -= position.x + dimension.largeur / 2;
                point.y -= position.y + dimension.hauteur / 2;
                point.z -= position.z + dimension.longueur / 2;

                // Rotation autour de l'axe x
                double tempY = point.y;
                point.y = point.y * Math.cos(xAngle) - point.z * Math.sin(xAngle);
                point.z = tempY * Math.sin(xAngle) + point.z * Math.cos(xAngle);

                // Rotation autour de l'axe y
                double tempX = point.x;
                point.x = point.x * Math.cos(yAngle) - point.z * Math.sin(yAngle);
                point.z = tempX * Math.sin(yAngle) + point.z * Math.cos(yAngle);

                // Rotation autour de l'axe z
                tempX = point.x;
                point.x = point.x * Math.cos(zAngle) - point.y * Math.sin(zAngle);
                point.y = tempX * Math.sin(zAngle) + point.y * Math.cos(zAngle);

                // Translation du point pour le ramener à sa position d'origine
                point.x += position.x + dimension.largeur / 2;
                point.y += position.y + dimension.hauteur / 2;
                point.z += position.z + dimension.longueur / 2;
            }
        }
    }

    public Vecteur getNormal(Polygon3D polygon3D) {
        for (Planche3D planche3D : planches3D) {
            if (planche3D.contient(polygon3D)) {
                return planche3D.getNormal(polygon3D);
            }
        }
        return null;
    }

    @Override
    public void rotateAroundCorner(double xAngle, double yAngle, double zAngle) {

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
    public Porte3D scale(double scale) {
        Porte3D porte3D = new Porte3D(this);
        for (int i = 0; i < porte3D.planches3D.size(); i++) {
            porte3D.planches3D.set(i, porte3D.planches3D.get(i).scale(scale));
        }
        porte3D.polygons = porte3D.planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new);
        porte3D.position = porte3D.position.multiplier(scale);
        return porte3D;

    }

    @Override
    public String toString() {
        return "Porte3D{" +
                "position='" + getPosition() + "'" +
                ", couleur='" + getCouleur() + "'" +
                ", rotationY='" + getRotationY() + "'" +
                ", rotationX='" + getRotationX() + "'" +
                ", rotationZ='" + getRotationZ() + "'" +
                ", visible='" + isVisible() + "'" +
                "}";
    }
}
