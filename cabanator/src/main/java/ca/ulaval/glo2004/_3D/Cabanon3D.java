package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.CabanonAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.MurAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancherAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.CabanonDTO;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.MurDTO;
import ca.ulaval.glo2004.gui.Couleurs;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Cabanon3D extends Objet3D implements IObjet3DCompose {
    private Dimension3D dimension;
    private HashMap<PointCardinal, Mur3D> murs;
    private Plancher3D plancher3D;
    private Toit3D toit3D;

    private static final Color[] COLORS = {Couleurs.BLEU, Couleurs.VERT, Couleurs.JAUNE, Couleurs.ORANGE}; // N E S O
    public Cabanon3D(CabanonAffichage cabanonAffichage) {
        super(new Point3D(0, 0, 0), null, Color.BLACK, false);
        dimension = cabanonAffichage.getDimension();
        murs = new HashMap<>();
        ArrayList<Polygon3D> polygons = new ArrayList<>();
        for (MurAffichage mur : cabanonAffichage.getMurs()) {
            murs.put(mur.getPositionMur(), new Mur3D(mur, COLORS[mur.getPositionMur().ordinal()]));
            polygons.addAll(Arrays.stream(murs.get(mur.getPositionMur()).polygons).toList());
        }
        plancher3D = new Plancher3D(cabanonAffichage.getPlancher(), Couleurs.ROUGE);
        polygons.addAll(Arrays.stream(plancher3D.polygons).toList());
        toit3D = new Toit3D(cabanonAffichage.getToit(), Couleurs.MAUVE);
        polygons.addAll(Arrays.stream(toit3D.polygons).toList());
        this.polygons = polygons.toArray(Polygon3D[]::new);
    }

    public Cabanon3D(CabanonAffichage cabanonAffichage, PlancherAffichage plancherAffichage) {
        super(new Point3D(0, 0, 0), null, Color.BLACK, false);
        dimension = cabanonAffichage.getDimension();
        murs = new HashMap<>();
        ArrayList<Polygon3D> polygons = new ArrayList<>();
        for (MurAffichage mur : cabanonAffichage.getMurs()) {
            murs.put(mur.getPointCardinal(), new Mur3D(mur, COLORS[mur.getPointCardinal().ordinal()]));
            polygons.addAll(Arrays.stream(murs.get(mur.getPointCardinal()).polygons).toList());
        }
        plancher3D = new Plancher3D(plancherAffichage, Couleurs.ROUGE);
        polygons.addAll(Arrays.stream(plancher3D.polygons).toList());
        toit3D = new Toit3D(cabanonAffichage.getToit(), Couleurs.MAUVE);
        polygons.addAll(Arrays.stream(toit3D.polygons).toList());
        this.polygons = polygons.toArray(Polygon3D[]::new);
    }

    public Cabanon3D(Cabanon3D cabanon3D) {
        super(cabanon3D.position, cabanon3D.polygons, cabanon3D.couleur, cabanon3D.selectable);
        dimension = cabanon3D.dimension;
        murs = (HashMap<PointCardinal, Mur3D>) cabanon3D.murs.clone();
        plancher3D = cabanon3D.plancher3D;
        toit3D = cabanon3D.toit3D;
    }

    @Override
    public Cabanon3D scale(double scale) {
        Cabanon3D newCabanon = new Cabanon3D(this);
        newCabanon.plancher3D = plancher3D.scale(scale);
        murs.forEach((pointCardinal, mur3D) -> {
            newCabanon.murs.put(pointCardinal, mur3D.scale(scale));
        });
        newCabanon.toit3D.scale(scale);
        return newCabanon;
    }

    public Plancher3D getPlancher3D() {
        return plancher3D;
    }

    public Dimension3D getDimension() {
        return dimension;
    }

    public Mur3D getMur(PointCardinal pointCardinal) {
        return murs.get(pointCardinal);
    }

    public HashMap<PointCardinal, Mur3D> getMurs() {
        return murs;
    }

    public Toit3D getToit() {
        return toit3D;
    }


    Objet3D[] getComposants() {
        ArrayList<Objet3D> composants = new ArrayList<>();
        for (Mur3D mur : murs.values()) {
            if (mur.fenetres3D != null)
                composants.addAll(mur.fenetres3D);
            if (mur.portes3D != null)
                composants.addAll(mur.portes3D);
        }
        composants.addAll(murs.values());
        if (plancher3D != null)
            composants.add(plancher3D);
        if(toit3D != null)
            composants.add(toit3D);
        return composants.toArray(Objet3D[]::new);
    }

    public Vecteur getNormal(Polygon3D polygon3D) {
        for (Mur3D mur : murs.values()) {
            if (mur.contient(polygon3D))
                return mur.getNormal(polygon3D);
        }
        if (plancher3D.contient(polygon3D))
            return plancher3D.getNormal(polygon3D);
        if (toit3D.contient(polygon3D))
            return toit3D.getNormal(polygon3D);
        return null;
    }

    @Override
    public Objet3D getComposante(Polygon3D poly) {
        // Verifier si le poly est un mur
        for (Mur3D mur : murs.values()) {
            if (mur.contient(poly))
                return mur;
        }
        // Verifier si le poly est le plancher
        if (plancher3D.contient(poly))
            return plancher3D;

        // Verifier si le poly est le toit
        if(toit3D.contient(poly))
            return toit3D;

        return null;
    }

    @Override
    public boolean contient(Objet3D objet) {
        if (objet instanceof Mur3D) {
            return murs.containsValue(objet);
        } else if (objet instanceof Plancher3D) {
            return plancher3D.equals(objet);
        } else if(objet instanceof Toit3D) {
            return toit3D.equals(objet);
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
        // Mise à jour des angles de rotation
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        // Rotation des points de chaque polygon
        for (Polygon3D polygon : polygons) {
            for (Point3D point : polygon.points) {
                // Translation du point pour qu'il soit centré sur l'origine
                point.x -= position.x;
                point.y -= position.y;
                point.z -= position.z;

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
                point.x += position.x;
                point.y += position.y;
                point.z += position.z;
            }
        }
    }

    @Override
    public void rotateAroundPoint(Point3D pivot, double xAngle, double yAngle, double zAngle) {

    }

    @Override
    public void faceTo(Point3D point) {

    }

    @Override
    public void update(Controleur3D controleur) {
        for (Mur3D mur : murs.values()) {
            mur.update(controleur);
        }
        plancher3D.update(controleur);
        toit3D.update(controleur);
    }
}