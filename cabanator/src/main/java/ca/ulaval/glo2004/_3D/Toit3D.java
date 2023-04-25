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
import java.util.stream.Collectors;

public class Toit3D extends Objet3D implements IObjet3DCompose, IComposanteCabanon3D {
    private static final Color[] COLORS = {Couleurs.BLEU, Couleurs.VERT, Couleurs.JAUNE, Couleurs.ORANGE};// N E S O
//    private static final Color[] COLORS = {Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE}; // N E S O
    //        private static final Color[] COLORS = {Color.decode("#cd9e6e"), Color.decode("#cd9e6e"), Color.decode("#cd9e6e"), Color.decode("#cd9e6e")}; // N E S O
    ArrayList<Planche3D> planches3D;
    private Dimension3D dimension;

    ArrayList<Ferme3D> fermes3D;

    public Toit3D(ToitAffichage toit, Color couleur) {
        super();
        this.couleur = couleur;
        this.position = new Point3D();
        this.dimension = new Dimension3D(toit.getLongueur(), toit.getLargueur(), toit.getHauteur());
        this.planches3D = new ArrayList<>();
        this.fermes3D = new ArrayList<>();

        ArrayList<PlancheAffichage> planches = toit.getPlanchesAffichage();
        ArrayList<FermeAffichage> fermes = toit.getFermesAffichage();

        fermes.forEach(ferme -> {
            Ferme3D ferme3D = new Ferme3D(ferme, toit, couleur);
            this.fermes3D.add(ferme3D);
            planches3D.addAll(ferme3D.planches3D);
        });
//        fermes.forEach(ferme -> planches.addAll(ferme.getPlanchesAffichage()));
        ArrayList<FermeSuppAffichage> fermesSupp = toit.getFermesSuppAffichage();
        fermesSupp.forEach(ferme -> {
            Ferme3D ferme3D = new Ferme3D(ferme, toit, couleur);
            this.fermes3D.add(ferme3D);
            planches3D.addAll(ferme3D.planches3D);
        });

//        fermesSupp.forEach(fermeSupp -> planches.addAll(fermeSupp.getPlanchesAffichage()));

//        planches.forEach(planche -> planches3D.add(new Planche3D(planche, COLORS[planches.indexOf(planche) % COLORS.length])));

        this.polygons = planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new); //On récupère tous les polygones des planches3D et on les met dans un tableau.
    }

    public Toit3D(Toit3D toit3D) {
        this.id = toit3D.id;
        this.couleur = toit3D.couleur;
        this.position = toit3D.position;
        this.dimension = toit3D.dimension;
        this.planches3D = toit3D.planches3D
                .stream().map(Planche3D::new)
                .collect(Collectors.toCollection(ArrayList::new));
        this.fermes3D = toit3D.fermes3D
                .stream().map(Ferme3D::new)
                .collect(Collectors.toCollection(ArrayList::new));
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

    @Override
    void update(Controleur3D controleur3D) {
        for (Planche3D planche : planches3D) {
            planche.update(controleur3D);
        }
    }

    @Override
    public Toit3D scale(double scale) {

        Toit3D toit3D = new Toit3D(this);
        for (int i = 0; i < toit3D.planches3D.size(); i++) {
            toit3D.planches3D.set(i, toit3D.planches3D.get(i).scale(scale));
        }
        toit3D.polygons = toit3D.planches3D
                .stream()
                .flatMap(planche -> Arrays.stream(planche.polygons))
                .toArray(Polygon3D[]::new);
        toit3D.position = toit3D.position.multiplier(scale);
        return toit3D;
    }

    public ArrayList<Ferme3D> getFermes() { return this.fermes3D; }
}
