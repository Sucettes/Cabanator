package ca.ulaval.glo2004._3D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ControleurCamera {
    Point3D positionCamera;
    Point3D positionCible; // Point vers lequel la caméra est orientée (Attention, pas la position de l'objet pointé)

    Point3D positionCameraInitiale;
    Point3D positionCibleInitiale; // Point vers lequel la caméra est orientée (Attention, pas la position de l'objet pointé)
    Point3D lastPositionCamera = null;
    Point3D lastPositionCible = null;

    Vecteur vecteurVertical;
    Vecteur vecteurVueCote;
    Vecteur vecteurVue;
    Point3D pointVecteurVue;
    Vecteur vecteurDroite;
    Vecteur vecteurGauche;
    Vecteur vecteurDessus;
    Vecteur vecteurDessous;
    double vitesse = 0.25; // Vitesse de déplacement de la caméra

    double zoomIncrement = 5;
    double zoom = 1000;
    double lastZoom = 0;
    double orientVerticale = -0.5, // En radians, 0 = vers le haut, PI/2 = vers l'avant, PI = vers le bas
            orientHorizontale = 0; // En radians, 0 = vers l'avant, PI/2 = vers la droite, PI = vers l'arrière

    double orientVerticaleInitiale = -0.5, // En radians, 0 = vers le haut, PI/2 = vers l'avant, PI = vers le bas
            orientHorizontaleInitiale = 0; // En radians, 0 = vers l'avant, PI/2 = vers la droite, PI = vers l'arrière
    double vitRotationHorizontale = 450, vitRotationVerticale = /*1100*/ 600;
    private boolean[] touches = new boolean[4];
    double zoomMin = 400, zoomMax = 2500;
    double zoomInitial = 1000;
    int renderDistance = 300;
    Dimension tailleEcran;
    public ControleurCamera(Point3D positionCamera, Point3D positionCible, Dimension tailleEcran) {
        this.positionCamera = positionCamera;
        this.positionCible = positionCible;
        this.positionCameraInitiale =new Point3D(positionCamera);
        this.positionCibleInitiale = new Point3D(positionCible);
        this.pointVecteurVue = new Point3D(positionCible.getX() - positionCamera.getX(), positionCible.getY() - positionCamera.getY(), positionCible.getZ() - positionCamera.getZ());
        this.vecteurVue = new Vecteur(pointVecteurVue);
        this.vecteurVertical = new Vecteur(new Point3D(0, 1, 0));
        this.vecteurVueCote = vecteurVue.produitVectoriel(vecteurVertical);
        this.tailleEcran = tailleEcran;
        this.zoomInitial = zoom;
    }
    Point3D getPositionCamera() { return positionCamera; }
    void setPositionCamera(Point3D positionCamera) {
        lastPositionCamera = this.positionCamera;
        this.positionCamera = positionCamera;
    }
    void resetCamera() {
        this.orientVerticale = orientVerticaleInitiale;
        this.orientHorizontale = orientHorizontaleInitiale;
        bougerEn(new Point3D(positionCameraInitiale.getX(), positionCameraInitiale.getY(), positionCameraInitiale.getZ()));
    }
    void resetZoom() {
        this.zoom = zoomInitial;
    }

    Point3D getPositionCible() { return positionCible; }
    void setPositionCible(Point3D positionCible) {
        lastPositionCible = this.positionCible;
        this.positionCible = positionCible;
    }
    double getZoom() { return this.zoom; }
    void setZoom(double zoom) {
        lastZoom = this.zoom = zoom;
    }
    double getVitesse() { return this.vitesse; }
    void setVitesse(double vitesse) { this.vitesse = vitesse; }
    double getVitRotationHorizontale() { return this.vitRotationHorizontale; }
    void setVitRotationHorizontale(double vitRotationHorizontale) { this.vitRotationHorizontale = vitRotationHorizontale; }
    double getVitRotationVerticale() { return this.vitRotationVerticale; }
    void setVitRotationVerticale(double vitRotationVerticale) { this.vitRotationVerticale = vitRotationVerticale; }
    double getZoomMin() { return this.zoomMin; }
    void setZoomMin(double zoomMin) { this.zoomMin = zoomMin; }
    double getZoomMax() { return this.zoomMax; }
    void setZoomMax(double zoomMax) { this.zoomMax = zoomMax; }
    double getOrientVerticale() { return this.orientVerticale; }
    void setOrientVerticale(double orientVerticale) { this.orientVerticale = orientVerticale; }
    double getOrientHorizontale() { return this.orientHorizontale; }
    void setOrientHorizontale(double orientHorizontale) { this.orientHorizontale = orientHorizontale; }
    double getFOV() { return Calculateur3D.zoomToFOV(zoom, tailleEcran); }
    void setTailleEcran(Dimension tailleEcran) { this.tailleEcran = tailleEcran; }

    void sourisDeplacee(double nouveauX, double nouveauY)
    {
        double difX = (nouveauX - tailleEcran.getWidth()/2);
        double difY = (nouveauY -tailleEcran.getHeight()/2);
        difY *= 6 - Math.abs(Math.sin(orientVerticale)) * 5;
        orientVerticale -= difY  / vitRotationVerticale;
        orientHorizontale -= difX / vitRotationHorizontale;

        if(orientVerticale >= Math.PI/1.999999)
            orientVerticale = Math.PI/1.999999;

        if(orientVerticale < -Math.PI/1.999999)
            orientVerticale = -Math.PI/1.999999;

        majVue();
    }


    void sourisScrolled(MouseWheelEvent e){
        if(e.getUnitsToScroll()>0)
        {
//            if(zoom > zoomMin)
                lastZoom = zoom -= zoomIncrement /* e.getUnitsToScroll()*/;
        }
        else
        {
//            if(zoom < zoomMax)
                lastZoom = zoom += zoomIncrement /* e.getUnitsToScroll()*/;
        }
    }

    void toucheAppuyee(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_W)
            touches[0] = true;
        if(e.getKeyCode() == KeyEvent.VK_A)
            touches[1] = true;
        if(e.getKeyCode() == KeyEvent.VK_S)
            touches[2] = true;
        if(e.getKeyCode() == KeyEvent.VK_D)
            touches[3] = true;
    }
    void toucheRelachee(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_W)
            touches[0] = false;
        if(e.getKeyCode() == KeyEvent.VK_A)
            touches[1]= false;
        if(e.getKeyCode() == KeyEvent.VK_S)
            touches[2] = false;
        if(e.getKeyCode() == KeyEvent.VK_D)
            touches[3] = false;
    }
    void mouvementCamera(){
        double mouvementX=0, mouvementY=0, mouvementZ=0;
        vecteurVue = new Vecteur(new Point3D(positionCible.getX() - positionCamera.getX(), positionCible.getY() - positionCamera.getY(), positionCible.getZ() - positionCamera.getZ())).normaliser();
        vecteurVertical = new Vecteur(new Point3D(0, 1, 0)).normaliser();
        vecteurVueCote = vecteurVue.produitVectoriel(vecteurVertical).normaliser();
        if(touches[0]) //W
        {
            mouvementX += vecteurVue.getX() ;
            mouvementY += vecteurVue.getY() ;
            mouvementZ += vecteurVue.getZ() ;
        }
        if(touches[1]) //A
        {
            mouvementX += vecteurVueCote.getX() ;
            mouvementY += vecteurVueCote.getY() ;
            mouvementZ += vecteurVueCote.getZ() ;
        }
        if(touches[2]) //S
        {
            mouvementX -= vecteurVue.getX() ;
            mouvementY -= vecteurVue.getY() ;
            mouvementZ -= vecteurVue.getZ() ;
        }
        if(touches[3]) //D
        {
            mouvementX -= vecteurVueCote.getX() ;
            mouvementY -= vecteurVueCote.getY() ;
            mouvementZ -= vecteurVueCote.getZ() ;
        }
        Vecteur vecteurMouvement = new Vecteur(new Point3D(mouvementX, mouvementY, mouvementZ));
        Point3D pt = new Point3D(positionCamera.getX() + vecteurMouvement.getX()*vitesse, positionCamera.getY() + vecteurMouvement.getY()*vitesse, positionCamera.getZ() + vecteurMouvement.getZ()*vitesse);
        bougerEn(pt);
    }

    void bougerEn(Point3D pt){
        lastPositionCamera = positionCamera;
        positionCamera.x = pt.getX();
        positionCamera.y = pt.getY();
        positionCamera.z = pt.getZ();
        majVue();
    }

    void majVue()
    {
        lastPositionCible = positionCible;
        double r = Math.sqrt(1 - (Math.sin(orientVerticale) * Math.sin(orientVerticale)));
        positionCible.x = positionCamera.getX() + r*Math.sin(orientHorizontale);
        positionCible.y = positionCamera.getY() + Math.sin(orientVerticale);
        positionCible.z = positionCamera.getZ() + r*Math.cos(orientHorizontale);

        vecteurVue.set(angleToVecteur());
        vecteurDroite = vecteurVue.produitVectoriel(vecteurVertical);
        vecteurDessus = vecteurDroite.produitVectoriel(vecteurVue);
        vecteurGauche = vecteurDroite.multiplier(-1);
        vecteurDessous = vecteurDessus.multiplier(-1);
    }

    Plan getPlanCamera(){
        Vecteur vecteurVue = getPositionCible().substract(getPositionCamera()).toVecteur().normaliser();
        Vecteur vecteurDirection = new Vecteur(new Point3D(1, 1, 1));
        Vecteur vecteurPlan1 = vecteurVue.produitVectoriel(vecteurDirection);

        Vecteur vecteurPlan2 = vecteurVue.produitVectoriel(vecteurPlan1);
        Plan plan = new Plan(vecteurPlan1, vecteurPlan2, getPositionCible());
        return plan;
    }

    Point3D getFocusPoint(){
        Point3D focusPoint = Calculateur3D.calculerPositionFocus(getPositionCamera(), getPositionCible(), getZoom());
        return focusPoint;
    }
    double getAngleHorizontal() {
        double angleHorizontal = Math.atan2(positionCible.getX() - positionCamera.getX(), positionCible.getZ() - positionCamera.getZ());
        angleHorizontal = Math.toDegrees(angleHorizontal);
        return angleHorizontal;
    }
    double getAngleVertical() {
        double distanceHorizontale = Math.sqrt(Math.pow(positionCible.getX() - positionCamera.getX(), 2) + Math.pow(positionCible   .getZ() - positionCamera.getZ(), 2));
        double dY = positionCible.getY() - positionCamera.getY();
        double angleVertical = Math.atan2(dY, distanceHorizontale);
        angleVertical = Math.toDegrees(angleVertical);
        return angleVertical;
    }

    Vecteur angleToVecteur(){
       return Calculateur3D.angleToVecteur(getAngleHorizontal(), getAngleVertical());
    }
    public Frustum createFrustum() {
        Plan nearClippingPlane = getPlanCamera();
        double aspectRatio = tailleEcran.getWidth() / tailleEcran.getHeight();
        double fov = getFOV();
        double tanRadFOV = Math.tan(Math.toRadians(fov / 2.0));
        double nearDistance = 1.0 / tanRadFOV; // distance du plan proche
        double farDistance = nearDistance * renderDistance; // exemple : plan lointain a 10 fois la distance du plan proche
        Point3D cameraPosition = getPositionCamera();
        Vecteur cameraDirection = nearClippingPlane.vecteurNormal.multiplier(1).normaliser(); // orientation de la caméra
        Vecteur vertical = new Vecteur(new Point3D(0,1,0)); // vecteur vertical
        Vecteur rightVector = cameraDirection.produitVectoriel(vertical).normaliser(); // vecteur horizontal
        Vecteur upVector = cameraDirection.produitVectoriel(rightVector).normaliser(); // vecteur dessus
        Point3D nearCenter = cameraPosition.add(cameraDirection.multiplier(nearDistance));
        Point3D farCenter = cameraPosition.add(cameraDirection.multiplier(farDistance));

        double nearHalfHeight = nearDistance * tanRadFOV;
        double nearHalfWidth = nearHalfHeight * aspectRatio;
        double farHalfHeight = farDistance * tanRadFOV;
        double farHalfWidth = farHalfHeight * aspectRatio;

        Point3D ntl = nearCenter.add(upVector.multiplier(nearHalfHeight)).add(rightVector.multiplier(-nearHalfWidth));
        Point3D ntr = nearCenter.add(upVector.multiplier(nearHalfHeight)).add(rightVector.multiplier(nearHalfWidth));
        Point3D nbl = nearCenter.add(upVector.multiplier(-nearHalfHeight)).add(rightVector.multiplier(-nearHalfWidth));
        Point3D nbr = nearCenter.add(upVector.multiplier(-nearHalfHeight)).add(rightVector.multiplier(nearHalfWidth));

        Point3D ftl = farCenter.add(upVector.multiplier(farHalfHeight)).add(rightVector.multiplier(-farHalfWidth));
        Point3D ftr = farCenter.add(upVector.multiplier(farHalfHeight)).add(rightVector.multiplier(farHalfWidth));
        Point3D fbl = farCenter.add(upVector.multiplier(-farHalfHeight)).add(rightVector.multiplier(-farHalfWidth));
        Point3D fbr = farCenter.add(upVector.multiplier(-farHalfHeight)).add(rightVector.multiplier(farHalfWidth));

        Polygon3D[] sides = new Polygon3D[4];
        sides[0] = new Polygon3D(new Point3D[] {ntl, ntr, ftr, ftl}, Color.MAGENTA, false); //Bottom
        sides[1] = new Polygon3D(new Point3D[] {nbl, ntl, ftl, fbl}, Color.RED, false); // Right
        sides[2] = new Polygon3D(new Point3D[] {nbr, nbl, fbl, fbr}, Color.GREEN, false); // Top
        sides[3] = new Polygon3D(new Point3D[] {ntr, nbr, fbr, ftr}, Color.BLUE, false); // Left
        Polygon3D near = new Polygon3D(new Point3D[] {ntl, nbl, nbr, ntr}, Color.YELLOW, false);
        Polygon3D far = new Polygon3D(new Point3D[] {ftl, ftr, fbr, fbl}, Color.CYAN, false);

        return new Frustum(sides, near, far);
    }

    public ArrayList<Point3D> getPoints(){
        Point3D cameraPosition = getPositionCamera();
        Plan nearClippingPlane = getPlanCamera();
        Vecteur cameraDirection = nearClippingPlane.vecteurNormal.multiplier(1).normaliser(); // orientation de la caméra
        double fov = getFOV();
        double tanRadFOV = Math.tan(Math.toRadians(fov / 2.0));
        double nearDistance = 1.0 / tanRadFOV; // distance du plan proche

        Vecteur vertical = new Vecteur(new Point3D(0,1,0)); // vecteur vertical
        Vecteur rightVector = cameraDirection.produitVectoriel(vertical).normaliser(); // vecteur horizontal
        Vecteur upVector = cameraDirection.produitVectoriel(rightVector).normaliser(); // vecteur dessus
        Point3D nearCenter = cameraPosition.add(cameraDirection.multiplier(nearDistance));
        double aspectRatio = tailleEcran.getWidth() / tailleEcran.getHeight();
        double nearHalfHeight = nearDistance * tanRadFOV;
        double nearHalfWidth = nearHalfHeight * aspectRatio;

        Point3D ntl = nearCenter.add(upVector.multiplier(nearHalfHeight)).add(rightVector.multiplier(-nearHalfWidth));
        Point3D ntr = nearCenter.add(upVector.multiplier(nearHalfHeight)).add(rightVector.multiplier(nearHalfWidth));
        Point3D nbl = nearCenter.add(upVector.multiplier(-nearHalfHeight)).add(rightVector.multiplier(-nearHalfWidth));
        Point3D nbr = nearCenter.add(upVector.multiplier(-nearHalfHeight)).add(rightVector.multiplier(nearHalfWidth));
        ArrayList<Point3D> points = new ArrayList<>();
        points.add(nbl);
        points.add(ntl);
        points.add(ntr);
        points.add(nbr);
        return points;
    }
    public ArrayList<Point3D> getPoints(Plan plan, double distance){
        Point3D cameraPosition = getPositionCamera();
        Plan nearClippingPlane  = plan;
        Vecteur cameraDirection = nearClippingPlane.vecteurNormal.multiplier(1).normaliser(); // orientation de la caméra
        double fov = getFOV();
        double tanRadFOV = Math.tan(Math.toRadians(fov / 2.0));
        double nearDistance = distance;

        Vecteur vertical = new Vecteur(new Point3D(0,1,0)); // vecteur vertical
        Vecteur rightVector = cameraDirection.produitVectoriel(vertical).normaliser(); // vecteur horizontal
        Vecteur upVector = cameraDirection.produitVectoriel(rightVector).normaliser(); // vecteur dessus
        Point3D nearCenter = cameraPosition.add(cameraDirection.multiplier(nearDistance));
        double aspectRatio = tailleEcran.getWidth() / tailleEcran.getHeight();
        double nearHalfHeight = nearDistance * tanRadFOV;
        double nearHalfWidth = nearHalfHeight * aspectRatio;

        Point3D ntl = nearCenter.add(upVector.multiplier(nearHalfHeight)).add(rightVector.multiplier(-nearHalfWidth));
        Point3D ntr = nearCenter.add(upVector.multiplier(nearHalfHeight)).add(rightVector.multiplier(nearHalfWidth));
        Point3D nbl = nearCenter.add(upVector.multiplier(-nearHalfHeight)).add(rightVector.multiplier(-nearHalfWidth));
        Point3D nbr = nearCenter.add(upVector.multiplier(-nearHalfHeight)).add(rightVector.multiplier(nearHalfWidth));
        ArrayList<Point3D> points = new ArrayList<>();
        points.add(nbl);
        points.add(ntl);
        points.add(ntr);
        points.add(nbr);
        return points;
    }

    public Point3D getPositionCurseur(Point2D point2D, Plan plan, double distance){
        if (point2D == null) return null;
        ArrayList<Point3D> points = getPoints(plan, distance);
        Point3D ntl = points.get(0);
        Point3D ntr = points.get(1);
        Point3D nbl = points.get(2);
        Point3D nbr = points.get(3);
        Vecteur vecteurHorizontal = new Vecteur(ntl, ntr);
        Vecteur vecteurVertical = new Vecteur(ntl, nbl);
        double x = point2D.getX() / tailleEcran.getWidth();
        double y = point2D.getY() / tailleEcran.getHeight();
        Point3D point = ntl.add(vecteurHorizontal.multiplier(x)).add(vecteurVertical.multiplier(y));
        return point;
    }

//    public Vecteur getVecteurCurseur(Point2D point2D){
//        Point3D point = getPositionCurseur(point2D);
//        if (point == null) return null;
//        return new Vecteur(getPositionCamera(), point);
//    }

    boolean positionChanged(){
        return !lastPositionCible.equals(positionCible) || !lastPositionCamera.equals(positionCamera);
    }
    boolean zoomChanged(){
        return lastZoom != zoom;
    }

}
