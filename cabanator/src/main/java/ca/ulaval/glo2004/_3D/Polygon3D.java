package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;

import java.awt.*;
import java.util.ArrayList;

public class Polygon3D extends Objet3D{
    public Point3D[] points;
    Vecteur normal;
    public double distanceMoyenne;
    Polygon2D projection;
    public Polygon3D(Point3D[] points, Color color) {
        super();
        this.points = points;
        this.polygons = new Polygon3D[]{this};
        this.couleur = color;
        this.position = points[0];
    }

    public Polygon3D(Point3D[] points, Color color, boolean visible) {
        super();
        this.points = points;
        this.polygons = new Polygon3D[]{this};
        this.couleur = color;
        this.position = points[0];
        this.visible = visible;
    }
    public Polygon3D(Polygon3D p) {
        super();
        //Clone
        this.points = new Point3D[p.points.length];
        for (int i = 0; i < p.points.length; i++) {
            this.points[i] = new Point3D(p.points[i]);
        }
        this.couleur = p.couleur;
        this.visible = p.visible;
        this.position = p.position;
        this.distanceMoyenne = p.distanceMoyenne;
        this.estValide = p.estValide;
        if (p.projection != null)
            this.projection = new Polygon2D(p.projection);
        this.scale = p.scale;
        this.normal = p.normal;
//        this.distanceReelle = p.distanceReelle;
    }


    /**
     * Retourne la plus grande distance entre le coin du polygone et un de ses points
     * */
    public double getRayon() {
        double rayon = 0;
        for (Point3D point : points) {
            double distance = Calculateur3D.calculerDistanceDeP(point, position);
            if (distance > rayon) {
                rayon = distance;
            }
        }
        return rayon;
    }


    public boolean getVisible() { return this.visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public int getNbPoints() { return points.length; }
    public Color getCouleur() { return couleur; }
    public void setCouleur(Color couleur) { this.couleur = couleur; }

    public Point3D getCenter() {
        double x = 0;
        double y = 0;
        double z = 0;
        for (Point3D point : points) {
            x += point.x;
            y += point.y;
            z += point.z;
        }
        return new Point3D(x / points.length, y / points.length, z / points.length);
    }

    double obtenirDistanceMoyenne(Point3D vueCamera) {
        /*double total = 0;
        for (Point3D point : points) {
            total += Calculateur3D.calculerDistanceDeP(point, vueCamera);
        }
        return total / points.length;*/
        Point3D centreGravite = getCenter();
        return Calculateur3D.calculerDistanceDeP(centreGravite, vueCamera);
    }

//    double obtenirDistanceRéelle(controleurCamera camera) {
//        Point3D positionCamera = camera.positionCamera;
//        Vecteur vecteurCamera = new Vecteur(positionCamera);
//        Point3D pointDeRepere = points[0];
//        Vecteur vecteurRepere = new Vecteur(pointDeRepere);
//        double scalaire = (vecteurCamera.soustraire(vecteurRepere)).produitScalaire(normal);
//        double normeNormal = normal.longueur==0?1:normal.longueur;
//        double distance = scalaire / normeNormal;
//        return distance;
//    }

    //https://www.cubic.org/docs/3dclip.htm
    //Partiellement inspiré de ce code
    public Polygon3D clipToPlane_2(Plan plan){
        ArrayList<Point3D> clipPoints = getClipPoints(plan);
        this.points = clipPoints.toArray(new Point3D[0]);
        return this;
    }
    private ArrayList<Point3D> getClipPoints(Plan plan){
        ArrayList<Point3D> clipPoints = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            Point3D pointA = points[i];
            Point3D pointB = points[(i+1)%points.length];
            Vecteur a = new Vecteur(pointA);
            Vecteur b = new Vecteur(pointB);
            double da = a.produitScalaire(plan.vecteurNormal) - plan.distance;
            double db = b.produitScalaire(plan.vecteurNormal) - plan.distance;
            double s= da/(da-db);
            if(da > 0 && db > 0){
                clipPoints.add(pointB);
            }else if(da < 0 && db < 0){
                //Ne rien faire
            }else if(da < 0 && db > 0){
                clipPoints.add(intersectionPlan(pointA, pointB, plan));
                clipPoints.add(pointB);
            }else if(da >0 && db < 0){
                clipPoints.add(pointA);
                clipPoints.add(intersectionPlan(pointA, pointB, plan));
            }
        }
        return clipPoints;
    }

    Polygon3D clipToFrustum(Frustum frustum){
        Polygon3D clippedPolygon = new Polygon3D(this);
        for (Plan plan : frustum.cotes) {
            clippedPolygon.clipToPlane_2(plan);
        }
        clippedPolygon.clipToPlane_2(frustum.planProche);
        clippedPolygon.clipToPlane_2(frustum.planEloigne);
        return clippedPolygon;
    }

    private Point3D intersectionPlan(Point3D a, Point3D b, Plan plan){
        Vecteur vA = new Vecteur(a);
        Vecteur vB = new Vecteur(b);
        double distanceA = vA.produitScalaire(plan.vecteurNormal) - plan.distance; // Distance entre le point A et le plan
        double distanceB = vB.produitScalaire(plan.vecteurNormal) - plan.distance; // Distance entre le point B et le plan
        float facteurIntersection = (float) (distanceA / (distanceA - distanceB));
        return new Point3D(
                a.getX() + (b.getX() - a.getX())*facteurIntersection,
                a.getY() + (b.getY() - a.getY())*facteurIntersection,
                a.getZ() + (b.getZ() - a.getZ())*facteurIntersection
        );
    }
    Plan toPlan(){
        return new Plan(this);
    }
    Plan toPlan(Point3D camera){
        return new Plan(this, camera);
    }

    @Override
    void update(Controleur3D controleur3D){
        int index = controleur3D.getPolygones3D().indexOf(this);
        projection = getProjection(controleur3D, true, true);
        if (index != -1)
            controleur3D.getPolygonesDessinables().set(index, projection);
    }
    void update(Controleur3D controleur3D, boolean rescale, boolean clip){
        int index = controleur3D.getPolygones3D().indexOf(this);
        projection = getProjection(controleur3D, rescale, clip);
        if (index != -1)
            controleur3D.getPolygonesDessinables().set(index, projection);
    }
    @Override
    public Polygon3D scale(double scale){
        Polygon3D newPolygon = new Polygon3D(this);
        for (int i = 0; i < newPolygon.points.length; i++) {
            newPolygon.points[i] = newPolygon.points[i].multiplier(scale);
        }
        return newPolygon;
    }
    public void translate(Point3D pt) {
        for (int i = 0; i < points.length; i++) {
            points[i] = points[i].translate(pt);
        }
    }

    @Override
    public Vecteur getNormal(Polygon3D polygon3D){
        return polygon3D.toPlan().vecteurNormal;
    }

    Polygon2D getProjection(Controleur3D controleur3D, boolean rescale, boolean clip){
        ControleurCamera controleurCamera = controleur3D.getcontroleurCamera();
        Polygon3D polygone3D;
        if (rescale)
            polygone3D = new Polygon3D(this).scale(controleur3D.pouceToPixel);
        else
            polygone3D = new Polygon3D(this);
//        Polygon3D polygone3D = new Polygon3D(this).scale(controleur3D.pouceToPixel);
        Point3D centre = polygone3D.getCenter();
        Frustum frustum = controleurCamera.createFrustum(); // "Cone" de vision de la caméra
        Vecteur vv = new Vecteur();  // NE FONCTIONNE QUE PARCE QUE TOUT EST UNE PLANCHE (CUBE) SINON IL FAUT FAIRE UN PLAN A PARTIR DU POLYGONE
//        Si le polygone appartient a un cube dans objets3D, on récupère la normal
        ArrayList<Objet3D> objets3D = controleur3D.objet3DS;
        for (Objet3D objet3D : objets3D) {
            if (objet3D.contient(this)) {
                vv = objet3D.getNormal(this);
                break;
            }
        }
        Vecteur vecteurNormal = vv; // On récupère le vecteur normal du plan

        Ligne polyNormal = vecteurNormal.obtenirLigne(centre, 1, Color.RED, 0.7); // On crée la ligne de la normale du polygone
        Polygon3D ligneNormal = polyNormal.polygons[0]; // On récupère le (seul) polygone de la ligne (Ligne est un objet3D, qui contient un tableau de polygones)
        Polygon3D ligneNormalClipped = ligneNormal.clipToFrustum(frustum); // On coupe la ligne au frustum (pour éviter de l'afficher si elle n'est pas visible)

        Point[] newPointsLigneNormal = new Point[ligneNormalClipped.points.length];
        for (int j = 0; j < ligneNormalClipped.getNbPoints(); j++)  // On récupère la projection des points de la ligne
            newPointsLigneNormal[j] = ligneNormalClipped.points[j].getProjection(controleur3D);

        Line2D normalLine = new Line2D(newPointsLigneNormal, Color.RED, 0.7); // On crée la ligne 2D
        // [Fin de la projection de la normale du polygone]

        // [ Projection du polygone ]
        Polygon3D clippedPolygon;
        if (clip)
            clippedPolygon = polygone3D // On met à l'échelle le polygone
                .clipToFrustum(frustum); // On coupe le polygone au frustum (pour éviter de l'afficher s'il n'est pas visible)
        else
            clippedPolygon = polygone3D;
        Point[] newPoints = new Point[clippedPolygon.points.length];
        for (int j = 0; j < clippedPolygon.getNbPoints(); j++) // On récupère la projection des points du polygone
            newPoints[j] = clippedPolygon.points[j].getProjection(controleur3D);
        // [ Fin de la projection du polygone ]

        Polygon2D polygon2D = new Polygon2D(newPoints, clippedPolygon.couleur, selectable, visible); // On crée le polygone 2D
//        polygon2D.normal = normalLine; // Ajout de la normale au polygone 2D
        polygon2D.estValide = clippedPolygon.estValide; // On indique si le polygone est valide ou non

        polygon2D.normalVecteur = vecteurNormal;
        distanceMoyenne = clippedPolygon
                .obtenirDistanceMoyenne(controleurCamera.getPositionCamera());

        normal = vecteurNormal;

        polygon2D.setDistanceMoyenne(distanceMoyenne);
        polygon2D.normal = normalLine;
        return polygon2D;
    }



    @Override
    public String toString() {
        String s = "Polygon3D{";
        s += "couleur=" + couleur;
        s+=", position=" + points[0];
        s+=", centre=" + getCenter();
//        s+=", normal=" + normal;
        /*s += ", points=";
        for (Point3D point : points) {
            s += point + ", ";
        }*/
        s += " distanceMoyenne=" + distanceMoyenne;
//        s += " distanceReelle=" + distanceReelle;
        s += "}";
        s += ", visible=" + visible;
        return s;
    }

    public void rotate(double xAngle, double yAngle, double zAngle) {
        // Mise à jour des angles de rotation
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        Dimension3D dimension = new Dimension3D(0, 0, 0);
        for (Point3D point : points) {
            dimension.largeur = Math.max(dimension.largeur, point.x);
            dimension.hauteur = Math.max(dimension.hauteur, point.y);
            dimension.longueur = Math.max(dimension.longueur, point.z);
        }

        // Rotation des points de chaque polygon
        for (Point3D point : points) {
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

    public void rotateAroundPoint(Point3D pivot, double xAngle, double yAngle, double zAngle) {
        // Mise à jour des angles de rotation
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        Dimension3D dimension = new Dimension3D(0, 0, 0);
        for (Point3D point : points) {
            dimension.largeur = Math.max(dimension.largeur, point.x);
            dimension.hauteur = Math.max(dimension.hauteur, point.y);
            dimension.longueur = Math.max(dimension.longueur, point.z);
        }

        // Rotation des points de chaque polygon
        for (Point3D point : points) {
            // Translation du point pour qu'il soit centré sur le pivot
            point.x -= pivot.x;
            point.y -= pivot.y;
            point.z -= pivot.z;

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
            point.x += pivot.x;
            point.y += pivot.y;
            point.z += pivot.z;
        }
    }

    @Override
    public void faceTo(Point3D point) {


    }

    public void rotateAroundCorner(double xAngle, double yAngle, double zAngle) {
        // Mise à jour des angles de rotation
        rotationX += xAngle;
        rotationY += yAngle;
        rotationZ += zAngle;

        Dimension3D dimension = new Dimension3D(0, 0, 0);
        for (Point3D point : points) {
            dimension.largeur = Math.max(dimension.largeur, point.x);
            dimension.hauteur = Math.max(dimension.hauteur, point.y);
            dimension.longueur = Math.max(dimension.longueur, point.z);
        }

        // Rotation des points de chaque polygon
        for (Point3D point : points) {
            // Translation du point pour qu'il soit centré sur l'origine
            point.x -= position.x + dimension.largeur;
            point.y -= position.y + dimension.hauteur;
            point.z -= position.z + dimension.longueur;

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
            point.x += position.x + dimension.largeur;
            point.y += position.y + dimension.hauteur;
            point.z += position.z + dimension.longueur;
        }
    }
}
