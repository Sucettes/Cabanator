package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.*;
import ca.ulaval.glo2004.Utilitaires.Dimension3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Fenetre3D extends Objet3D {
    public Dimension3D dimension;
    public ArrayList<Planche3D> planches3D;
    public Fenetre3D(FenetreAffichage fenetreAffichage, Color color) {
        super();
        this.id = fenetreAffichage.getId();
        this.dimension = new Dimension3D(fenetreAffichage.getLargeur(), 0.2, fenetreAffichage.getHauteur());
        this.position = new Point3D(fenetreAffichage.getPosition().getX(), fenetreAffichage.getPosition().getY(), fenetreAffichage.getPosition().getZ());
        this.couleur = color;
        planches3D = new ArrayList<>();
        ArrayList<PlancheAffichage> planches = fenetreAffichage.getPlanchesAffichage();

        planches.forEach(planche -> planches3D.add(new Planche3D(planche, color)));

        this.polygons = planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new); //On récupère tous les polygones des planches3D et on les met dans un tableau.
    }

    public Fenetre3D(Fenetre3D fenetre3D) {
        super();
        this.id = fenetre3D.id;
        this.dimension = fenetre3D.dimension;
        this.position = fenetre3D.position;
        this.couleur = fenetre3D.couleur;
        this.planches3D = fenetre3D.planches3D.stream().map(Planche3D::new).collect(Collectors.toCollection(ArrayList::new));
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
    public Fenetre3D scale(double scale) {
        Fenetre3D fenetre3D = new Fenetre3D(this);
        for (int i = 0; i < fenetre3D.planches3D.size(); i++) {
            fenetre3D.planches3D.set(i, fenetre3D.planches3D.get(i).scale(scale));
        }
        fenetre3D.polygons = fenetre3D.planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new);
        fenetre3D.position = fenetre3D.position.multiplier(scale);
        return fenetre3D;
    }

    @Override
    public String toString() {
        return "Fenetre3D{" +
                "position='" + getPosition() + "'" +
                ", couleur='" + getCouleur() + "'" +
                ", rotationY='" + getRotationY() + "'" +
                ", rotationX='" + getRotationX() + "'" +
                ", rotationZ='" + getRotationZ() + "'" +
                ", visible='" + isVisible() + "'" +
                "}";
    }
}
