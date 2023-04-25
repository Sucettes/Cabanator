package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Grille extends Objet3D{
    private double distanceEntreLignes;
    ArrayList<Ligne> lignes = null;
    private Color couleurGrille = new Color(216, 216, 216, 82);

        public Grille(String distanceEntreLignes, Controleur3D controleur3D, Plan plan, double dist) {
            this.distanceEntreLignes = ValeurImperiale.convertirStringEnDouble(distanceEntreLignes) * controleur3D.pouceToPixel;
            this.lignes = genererGrille(controleur3D, dist, plan);
        }

        public Grille(double distanceEntreLignes, Controleur3D controleur3D, Plan plan, double dist) {
            this.distanceEntreLignes = distanceEntreLignes * controleur3D.pouceToPixel;
            this.lignes = genererGrille(controleur3D, dist, plan);
        }


    private ArrayList<Ligne> genererGrille(Controleur3D controleur3D, double distanceFromCamera, Plan plan) {
        Vecteur vecteurVue = new Vecteur(controleur3D.getPositionCamera(), controleur3D.getPositionCible());

        ArrayList<Point3D> nearPlane = controleur3D.controleurCamera.getPoints(plan, distanceFromCamera);
        ArrayList<Ligne> lignes = new ArrayList<>();
        vecteurVue = vecteurVue.normaliser().multiplier(distanceFromCamera*0.3);

        Point3D btLeft = nearPlane.get(2); //2
        Point3D topLeft = nearPlane.get(3); //4
        Point3D topRight = nearPlane.get(0);//1
        Point3D btRight = nearPlane.get(1);//3

        Vecteur vecteurHorizontal = new Vecteur(btLeft, btRight);
        Vecteur vecteurVertical = new Vecteur(btLeft, topLeft);

        double distanceVerticale = vecteurVertical.getLongueur();
        double distanceHorizontale = vecteurHorizontal.getLongueur();
        Point3D pointCurrent = new Point3D(btLeft.x, btLeft.y, btLeft.z);

        int nbLignesHorizontales = (int) Math.ceil(distanceHorizontale / distanceEntreLignes);
        int nbLignesVerticales = (int) Math.ceil(distanceVerticale / distanceEntreLignes);

        Vecteur vecteurVerticalUnit = vecteurVertical.normaliser();
        Vecteur vecteurHorizontalUnit = vecteurHorizontal.normaliser();

        for (int i = 1; i <= nbLignesVerticales; i++) {
            Point3D point1 = new Point3D(pointCurrent);
            Point3D point2 = point1.translate(vecteurHorizontalUnit.multiplier(distanceHorizontale));
            Ligne ligne = new Ligne(point1, point2, couleurGrille, 2);
            //Offset de la grille pour qu'elle soit au centre
            lignes.add(ligne);

            pointCurrent = pointCurrent.translate(vecteurVerticalUnit.multiplier(distanceEntreLignes));
        }

        pointCurrent = new Point3D(btLeft.x, btLeft.y, btLeft.z);

        for (int i = 0; i <= nbLignesHorizontales; i++) {
            Point3D point1 = new Point3D(pointCurrent);
            Point3D point2 = point1.translate(vecteurVerticalUnit.multiplier(distanceVerticale));
            Ligne ligne = new Ligne(point1, point2, couleurGrille, 2);
            lignes.add(ligne);

            pointCurrent = pointCurrent.translate(vecteurHorizontalUnit.multiplier(distanceEntreLignes));
        }
        return lignes;
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
    public void update(Controleur3D controleur) {
        for (Ligne ligne : lignes) {
            ligne.update(controleur, false, false);
        }
    }

    @Override
    public <T extends Objet3D> T scale(double scale) {
        return null;
    }
    public void translate(Point3D pt) {
        // Implémentation de la translation
        for (Polygon3D polygon : this.polygons) {
            // Implémentation de la translation
            polygon.translate(pt);
        }
        position = position.translate(pt);
    }

}
