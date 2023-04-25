package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;

import java.awt.*;
import java.awt.geom.Point2D;

public class Calculateur3D {
    static public double distance = 0;

    //Calculer la position du point sur l'écran (Retourne un Point 3D mais on ne s'en sert que pour les x et y)
    static Point3D calculerPosition(Point3D vueCamera, Point3D vueCible, Point3D point) {
        return calculPosition(vueCamera, vueCible, point);
    }

    static Point3D calculerPositionFocus(Point3D vueCamera, Point3D vueCible, double zoom) {
        Point3D focus = calculPosition(vueCamera, vueCible, vueCible);
        focus.x = zoom * focus.getX();
        focus.y = zoom * focus.getY();
        return focus;
    }

    static double calculerDistanceDeP(Point3D pointA, Point3D pointB) {
        return Math.sqrt(
                (pointB.getX() - pointA.getX()) * (pointB.getX() - pointA.getX()) +
                        (pointB.getY() - pointA.getY()) * (pointB.getY() - pointA.getY()) +
                        (pointB.getZ() - pointA.getZ()) * (pointB.getZ() - pointA.getZ())
        );
    }

    //Calculs pour trouver la position du point sur l'écran
    static Point3D calculPosition(Point3D vueCamera, Point3D vueCible, Point3D point) {
        Vecteur vecteurVue = vueCible.substract(vueCamera).toVecteur().normaliser();
        Vecteur vecteurDirection = new Vecteur(new Point3D(1, 1, 1));
        Vecteur vecteurPlan1 = vecteurVue.produitVectoriel(vecteurDirection);

        Vecteur vecteurPlan2 = vecteurVue.produitVectoriel(vecteurPlan1);
        Plan plan = new Plan(vecteurPlan1, vecteurPlan2, vueCible);

        Vecteur vecteurRotation = obtenirVecteurRotation(vueCamera, vueCible);
        Vecteur vecteurHorizontal = vecteurVue.produitVectoriel(vecteurRotation); //Direction horizontale de la caméra
        Vecteur vecteurVectical = vecteurVue.produitVectoriel(vecteurHorizontal); //Direction verticale de la caméra

        Vecteur vueVersPoint = point.substract(vueCamera).toVecteur();

        //Distance entre la caméra et le point du plan
        distance = (plan.vecteurNormal.produitScalaire(plan.getPoint())
                - (plan.vecteurNormal.produitScalaire(vueCamera)))
                / (plan.vecteurNormal.produitScalaire(vueVersPoint));

        //Point du plan
        Point3D p1 = vueCamera.add(vueVersPoint.multiplier(distance));

        Point3D pt = new Point3D(vecteurVectical.produitScalaire(p1), vecteurHorizontal.produitScalaire(p1), 0);
        return pt;
    }

    static Vecteur obtenirVecteurRotation(Point3D vueCamera, Point3D vueCible) {
        //Calculer le vecteur de rotation
        double dX = Math.abs(vueCamera.getX() - vueCible.getX()); //Distance entre les deux points sur l'axe X
        double dZ = Math.abs(vueCamera.getZ() - vueCible.getZ()); //Distance entre les deux points sur l'axe Z
        double rotationX, rotationZ;
        rotationX = dZ / (dX + dZ); // Coefficient de rotation sur l'axe X
        rotationZ = dX / (dX + dZ); // Coefficient de rotation sur l'axe Z
        if (vueCamera.getX() < vueCible.getX()) { //Si la caméra est à gauche de la cible
            rotationZ = -rotationZ; //On inverse le vecteur de rotation sur l'axe Z
        }
        if (vueCamera.getZ() > vueCible.getZ()) { //Si la caméra est en haut de la cible
            rotationX = -rotationX; //On inverse le vecteur de rotation sur l'axe X
        }
        return new Vecteur(new Point3D(rotationX, 0, rotationZ)).normaliser(); //On retourne le vecteur de rotation
    }

    public static int FOVToZoom(double FOV, Dimension size) {
        return (int) (size.getHeight() / 2f / Math.tan(Math.toRadians(FOV / 2)));
    }

    public static double zoomToFOV(double zoom, Dimension size) {
        return (double) Math.toDegrees(Math.atan(size.getHeight() / 2f / zoom)) * 2;
    }

    public static PointCardinal angleToPointCardinal(double angle) {
        if (angle >= -45 && angle < 45) {
            return PointCardinal.EST;
        } else if (angle >= 45 && angle < 135) {
            return PointCardinal.NORD;
        } else if (angle >= 135 && angle <= 180 || angle >= -180 && angle < -135) {
            return PointCardinal.OUEST;
        } else if (angle < -45 && angle >= -135) {
            return PointCardinal.SUD;
        } else {
            return PointCardinal.EST;
        }
    }

    public static Vecteur angleToVecteur(double angleHorizontal, double angleVertical) {
        double yaw2 = Math.toRadians(angleHorizontal);
        double pitch2 = Math.toRadians(angleVertical);
        Point3D pt = new Point3D(Math.cos(yaw2) * Math.cos(pitch2), Math.sin(pitch2), Math.sin(yaw2) * Math.cos(pitch2));
        Vecteur toVector = new Vecteur(pt).normaliser();

        return toVector;
    }


    /**
     * Convertit un vecteur en angle de rotation (Yaw (horizontal), Pitch (vertical)) / (sur Y, sur X)
     * @param vecteur
     * @return Un tableau de 3 double contenant les angles de rotation en degrés
     */
    public static double[] vecteurToAngle(Vecteur vecteur) {
        double yaw = Math.atan2(vecteur.getX(), vecteur.getZ());
        double pitch = Math.atan2(vecteur.getY(), Math.sqrt(Math.pow(vecteur.getX(), 2) + Math.pow( vecteur.getZ(), 2)));
        return new double[]{Math.toDegrees(yaw), Math.toDegrees(pitch)};
    }

    static Point3D get3DPointFromProjection(Point2D point2D, Controleur3D controleur3D) {
        Dimension tailleEcran = controleur3D.tailleEcran;
        Point3D focusPoint = controleur3D.getFocusPoint();
        double zoom = controleur3D.getZoom();

        double x2D = point2D.getX();
        double y2D = point2D.getY();

        double x3D = (x2D - (tailleEcran.getWidth() / 2) + focusPoint.getX()) / zoom;
        double y3D = (y2D - (tailleEcran.getHeight() / 2) + focusPoint.getY()) / zoom;

        Point3D pt3D = new Point3D(x3D, y3D, 0);

        Point3D vueCamera = controleur3D.getPositionCamera();
        Point3D vueCible = controleur3D.getPositionCible();

        Vecteur vecteurVue = vueCible.substract(vueCamera).toVecteur().normaliser();
        Vecteur vecteurDirection = new Vecteur(new Point3D(1, 1, 1));
        Vecteur vecteurPlan1 = vecteurVue.produitVectoriel(vecteurDirection);

        Vecteur vecteurPlan2 = vecteurVue.produitVectoriel(vecteurPlan1);
        Plan plan = new Plan(vecteurPlan1, vecteurPlan2, vueCible);

        Vecteur vecteurRotation = obtenirVecteurRotation(vueCamera, vueCible);
        Vecteur vecteurHorizontal = vecteurVue.produitVectoriel(vecteurRotation); // Camera horizontal direction
        Vecteur vecteurVertical = vecteurVue.produitVectoriel(vecteurHorizontal); // Camera vertical direction

        double distanceToPlane = plan.vecteurNormal.produitScalaire(plan.getPoint()) - plan.vecteurNormal.produitScalaire(vueCamera);
        distanceToPlane /= plan.vecteurNormal.produitScalaire(vecteurVue);

        Point3D intersection = vueCamera.add(vecteurHorizontal.multiplier(pt3D.getX()))
                .add(vecteurVertical.multiplier(pt3D.getY()))
                .add(vecteurVue.multiplier(distanceToPlane));

        return intersection;
    }

    public static Point3D getInverseProjection(Controleur3D controleur3D, Point2D screenPoint) {
        Point3D cameraPosition = controleur3D.getPositionCamera();
        Point3D targetPosition = controleur3D.getPositionCible();
        double zoom = controleur3D.getZoom();
        Dimension screenSize = controleur3D.tailleEcran;
        Point3D focusPoint = controleur3D.getFocusPoint();

        double screenX = screenPoint.getX();
        double screenY = screenPoint.getY();

        double worldX = (screenX - (screenSize.getWidth() / 2) + focusPoint.getX()) / zoom;
        double worldY = (screenY - (screenSize.getHeight() / 2) + focusPoint.getY()) / zoom;

        Vecteur viewVector = targetPosition.substract(cameraPosition).toVecteur().normaliser();
        Vecteur rotationVector = obtenirVecteurRotation(cameraPosition, targetPosition);
        Vecteur horizontalVector = viewVector.produitVectoriel(rotationVector);
        Vecteur verticalVector = viewVector.produitVectoriel(horizontalVector);

        Point3D planarPoint = cameraPosition.add(horizontalVector.multiplier(worldX)).add(verticalVector.multiplier(worldY));

        double fov = controleur3D.getFOV();
        double tanRadFOV = Math.tan(Math.toRadians(fov / 2.0));
        double distanceToPlane = 1.0 / tanRadFOV; // distance du plan proche

        Vecteur cameraToPlanarPoint = planarPoint.substract(cameraPosition).toVecteur();

        Point3D actualPoint = cameraPosition.add(cameraToPlanarPoint.multiplier(distanceToPlane / cameraToPlanarPoint.longueur));

        return actualPoint;
    }
}