package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.*;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.MurDTO;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Mur3D extends Objet3D implements IObjet3DCompose, IComposanteCabanon3D {
    public Dimension3D dimension;
    public ArrayList<Planche3D> planches3D;
    public ArrayList<Fenetre3D> fenetres3D;
    public ArrayList<Porte3D> portes3D;
    PointCardinal orientation;


    public Mur3D(Mur3D mur3D) {
        super();
        this.id = mur3D.id;
        this.dimension = mur3D.dimension;
        this.position = mur3D.position;
        this.couleur = mur3D.couleur;
        this.orientation = mur3D.orientation;
        this.planches3D = mur3D.planches3D.stream().map(Planche3D::new).collect(Collectors.toCollection(ArrayList::new));
        this.fenetres3D = mur3D.fenetres3D.stream().map(Fenetre3D::new).collect(Collectors.toCollection(ArrayList::new));
        this.portes3D = mur3D.portes3D.stream().map(Porte3D::new).collect(Collectors.toCollection(ArrayList::new));
        this.polygons = planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new);

        this.rotationX = mur3D.rotationX;
        this.rotationY = mur3D.rotationY;
        this.rotationZ = mur3D.rotationZ;
//        orienterAroundCorner(mur3D.orientation);

    }


    public Mur3D(MurAffichage murAffichage, Color color) {
        super();
        this.id = murAffichage.getId();
        this.dimension = new Dimension3D(murAffichage.getLongueur(), 0.2, murAffichage.getHauteur());
        this.position = new Point3D(murAffichage.getPosition().getX(), murAffichage.getPosition().getY(), murAffichage.getPosition().getZ());
        this.couleur = color;
        planches3D = new ArrayList<>();
        this.orientation = murAffichage.getPositionMur();
        ArrayList<PlancheAffichage> planches = murAffichage.getPlanchesAffichage();
        fenetres3D = new ArrayList<>();
        portes3D = new ArrayList<>();
        planches.forEach(planche -> planches3D.add(new Planche3D(planche, color)));

        for (AccessoireAffichage accessoire : murAffichage.getAccessoires()) {
            if (accessoire instanceof PorteAffichage porte) {
                Porte3D porte3D = new Porte3D(porte, color);
                portes3D.add(porte3D);
                planches3D.addAll(porte3D.planches3D);
            } else if (accessoire instanceof FenetreAffichage fenetre) {
                Fenetre3D fenetre3D = new Fenetre3D(fenetre, color);
                fenetres3D.add(fenetre3D);
                planches3D.addAll(fenetre3D.planches3D);
            }
        }

        this.polygons = planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new); //On récupère tous les polygones des planches3D et on les met dans un tableau.

        orienterAroundCorner(murAffichage.getPointCardinal());
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
    public void rotateAroundCorner(double xAngle, double yAngle, double zAngle) {
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        for (Planche3D planche : planches3D) {
            planche.rotateAroundPoint(position, xAngle, yAngle, zAngle);
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
    public void rotateAroundPoint(Point3D pivot, double xAngle, double yAngle, double zAngle) {

    }

    @Override
    public void faceTo(Point3D point) {

    }

    @Override
    public void orienterAroundCorner(PointCardinal pointCardinal) {
        double angle = pointCardinal.getDirection() - 90; // +90 pour que la face soit vers le point cardinal et non le coté
        rotateAroundCorner(0, Math.toRadians(angle), 0);
    }

    @Override
    void update(Controleur3D controleur3D) {
        for (Planche3D planche : planches3D) {
            planche.update(controleur3D);
        }
    }

    @Override
    public Mur3D scale(double scale) {
        Mur3D mur3D = new Mur3D(this);
        for (int i = 0; i < mur3D.planches3D.size(); i++) {
            mur3D.planches3D.set(i, mur3D.planches3D.get(i).scale(scale));
        }
        mur3D.polygons = mur3D.planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new);
        mur3D.position = mur3D.position.multiplier(scale);
        return mur3D;
    }


    @Override
    public String toString() {
        return "Mur{" +
                "position=" + position +
                ", orientation=" + this.orientation +
                ", dimension=" + dimension.longueur + " " + dimension.largeur + " " + dimension.hauteur +
                ", rX=" + String.format("%.3f", rotationX) +
                ", rY=" + String.format("%.3f", rotationY) +
                ", rZ=" + String.format("%.3f", rotationZ) +
                '}';
    }

    @Override
    public Objet3D getComposante(Polygon3D poly) {
        if (fenetres3D.stream().anyMatch(fenetre -> fenetre.contient(poly))) {
            return fenetres3D.stream().filter(fenetre -> fenetre.contient(poly)).findFirst().orElse(null);
        }
        if (portes3D.stream().anyMatch(porte -> porte.contient(poly))) {
            return portes3D.stream().filter(porte -> porte.contient(poly)).findFirst().orElse(null);
        }
        return this.planches3D
                .stream()
                .filter(planche -> planche.contient(poly)) //On filtre les planches3D pour ne garder que celles qui contiennent le poly.
                .findFirst()//On prend le premier élément de la liste (car il n'y en a qu'un seul (normalement))
                .orElse(null); //Si une planche a été trouvée, on la retourne. Sinon, on retourne null.
    }

    @Override
    public boolean contient(Objet3D objet3D) {
        if (objet3D instanceof Fenetre3D)
            return fenetres3D.contains(objet3D);
        if (objet3D instanceof Porte3D)
            return portes3D.contains(objet3D);
        else if (objet3D instanceof Planche3D)
            return planches3D.contains(objet3D);

        return false;
    }
}