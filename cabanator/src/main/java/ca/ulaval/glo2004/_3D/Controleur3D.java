package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;


public class Controleur3D {
    int nombreDePolygones2D;
    int nombreDePolygones3D;
    ControleurCamera controleurCamera;
    ArrayList<Polygon2D> polygonesDessinables;
    ArrayList<Polygon3D> polygones3D;
    ArrayList<Objet3D> objet3DS;
    HashMap<Integer, Integer> polygones3DToObjet3D;
    public int[] nouvelOrdreAffichage;
    public double[] nouvelOrdreDistances;
    Dimension tailleEcran;
    Polygon2D polygone2DSelectionne;
    Polygon3D polygone3DSelectionne;
    Objet3D objet3DSelectionne;
    public boolean modeSelectionPrecision = false;
    public double pouceToPixel = 0.3;
    boolean mode2D = false;
    public Grille grille;
    public Plan plan2D;
    public double distancePlan2D = 0;
    double seuilCulling = 87;
    double seuilCulling3 = 0;
    boolean grilleActivee = true;
    double espacementGrille = ValeurImperiale.convertirStringEnDouble("2' 6\"");
    boolean showDebug = false;
    public Controleur3D(ControleurCamera controleurCamera, Dimension dimension){
        this.controleurCamera = controleurCamera;
        nombreDePolygones2D = 0;
        nombreDePolygones3D = 0;
        polygonesDessinables = new ArrayList<Polygon2D>();
        polygones3D = new ArrayList<Polygon3D>();
        objet3DS = new ArrayList<Objet3D>();
        polygones3DToObjet3D = new HashMap<Integer, Integer>();
        tailleEcran = dimension;
    }

    //#region Getters et Setters
    public Point3D getPositionCamera() { return this.controleurCamera.getPositionCamera(); }
    public void setPositionCamera(Point3D positionCamera) { this.controleurCamera.setPositionCamera(positionCamera); }
    public Point3D getPositionCible() { return this.controleurCamera.getPositionCible(); }
    public void setPositionCible(Point3D positionCible) { this.controleurCamera.setPositionCible(getPositionCible()); }
    public double getZoom() { return this.controleurCamera.getZoom(); }
    public void setZoom(double zoom) { this.controleurCamera.setZoom(zoom); }
    public int getNbDessinables() { return this.polygonesDessinables.size(); }
    public ArrayList<Polygon2D> getPolygonesDessinables() { return this.polygonesDessinables; }
    public ArrayList<Polygon3D> getPolygones3D() { return this.polygones3D; }
    public ArrayList<Objet3D> getObjet3DS() { return objet3DS; }
    public double getVitesse() { return controleurCamera.getVitesse(); }
    public void setVitesse(double vitesse) { controleurCamera.setVitesse(vitesse); }
    public double getVitRotationHorizontale() { return controleurCamera.getVitRotationHorizontale(); }
    public void setVitRotationHorizontale(double vitRotationHorizontale) { controleurCamera.setVitRotationHorizontale(vitRotationHorizontale); }
    public double getVitRotationVerticale() { return controleurCamera.getVitRotationVerticale(); }
    public void setVitRotationVerticale(double vitRotationVerticale) { controleurCamera.setVitRotationVerticale(vitRotationVerticale); }
    public double getZoomMin() { return controleurCamera.getZoomMin(); }
    public void setZoomMin(double zoomMin) { controleurCamera.setZoomMin(zoomMin); }
    public double getZoomMax() { return controleurCamera.getZoomMax(); }
    public void setZoomMax(double zoomMax) { controleurCamera.setZoomMax(zoomMax); }
    public double getOrientVerticale() { return controleurCamera.getOrientVerticale(); }
    public void setOrientVerticale(double orientVerticale) { controleurCamera.setOrientVerticale(orientVerticale); }
    public double getOrientHorizontale() { return controleurCamera.getOrientHorizontale(); }
    public void setOrientHorizontale(double orientHorizontale) { controleurCamera.setOrientHorizontale(orientHorizontale); }
    public double getFOV() { return controleurCamera.getFOV(); }
    public void setTailleEcran(Dimension tailleEcran) {
        this.tailleEcran = tailleEcran;
        controleurCamera.setTailleEcran(tailleEcran);
    }
    public Plan getPlanCamera() { return controleurCamera.getPlanCamera(); }
    public Point3D getFocusPoint() { return controleurCamera.getFocusPoint(); }
    public double getAngleHorizontal() { return controleurCamera.getAngleHorizontal(); }
    public double getAngleVertical() { return controleurCamera.getAngleVertical(); }
    public Vecteur angleToVecteur() { return controleurCamera.angleToVecteur(); }
    public Frustum createFrustum() { return controleurCamera.createFrustum(); }
    public boolean positionChanged() { return controleurCamera.positionChanged(); }
    public boolean zoomChanged() { return controleurCamera.zoomChanged(); }
    public void setRenderDistance(int renderDistance) { controleurCamera.renderDistance = renderDistance; }
    public int getRenderDistance() { return controleurCamera.renderDistance; }
    public ControleurCamera getcontroleurCamera() { return controleurCamera; }
    public Objet3D getObjet3DSelectionne() { return objet3DSelectionne; }
    public Polygon3D getPolygone3DSelectionne() { return polygone3DSelectionne; }
    public Polygon2D getPolygone2DSelectionne() { return polygone2DSelectionne; }

    //#endregion

    public void sourisDeplacee(double nouveauX, double nouveauY) { controleurCamera.sourisDeplacee(nouveauX, nouveauY); }

    public void sourisScrolled(MouseWheelEvent e) { controleurCamera.sourisScrolled(e); }

    public void toucheAppuyee(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
            setRenderDistance(getRenderDistance() + 10);
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
            setRenderDistance(getRenderDistance() - 10);
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            modeSelectionPrecision = !modeSelectionPrecision;
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            resetCamera();
        }
        if (e.getKeyCode() == KeyEvent.VK_I) {
            pouceToPixel-=0.01;
        }
        if (e.getKeyCode() == KeyEvent.VK_O) {
            pouceToPixel+=0.01;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD8)
            seuilCulling +=1;
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD2)
            seuilCulling -=1;
        if (e.getKeyCode() == KeyEvent.VK_G)
            grilleActivee = !grilleActivee;
        if (e.getKeyCode() == KeyEvent.VK_V) {
            this.espacementGrille += 0.1;
            regenererGrille();
        }
        if (e.getKeyCode() == KeyEvent.VK_B){
            if(this.espacementGrille > 0.1) {
                this.espacementGrille -= 0.1;
                regenererGrille();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            this.showDebug = !this.showDebug;
        }

        controleurCamera.toucheAppuyee(e);
    }

    public void toucheRelachee(KeyEvent e) {
        controleurCamera.toucheRelachee(e);
    }

    public void mouvementCamera() {
        if(!mode2D)
            controleurCamera.mouvementCamera();
    }

    public void bougerEn(Point3D pt) { controleurCamera.bougerEn(pt); }

    public void majVue() { controleurCamera.majVue(); }

    void desactiverMode2D() {
        this.mode2D = false;
        // Réafficher les objets cachés
        objet3DS.forEach(obj -> obj.setVisible(true));
        update();
    }

    public void resetCamera() {
        if (mode2D) return;
        this.controleurCamera.resetCamera();
        update();
    }
    public void resetZoom() {
        this.controleurCamera.resetZoom();
        update();
    }

    public void ajouterPolygones(Polygon3D[] polygones) {
        for (Polygon3D poly : polygones) {
            ajouterPolygone(poly);
        }
    }
    public void activerMode2D(){
        // Déplacer la camera au centre de l'objet pour donner l'illusion de 2D
        Objet3D objet3D = getObjet3DSelectionne().scale(pouceToPixel);
        if (objet3D == null) return;

        Point3D posCamera = controleurCamera.getPositionCamera();
        Plan planPolygone = polygone3DSelectionne.scale(pouceToPixel).toPlan(posCamera); // Plan du polygone (caméra pour déterminer si la face pointe vers l'extérieur ou l'intérieur)
        Vecteur normal = planPolygone.vecteurNormal; // Vecteur normal au plan du polygone
        boolean vueDeFace = !planPolygone.vecteurNormal.isParallelTo(new Vecteur(new Point3D(0,1,0)));

        if (vueDeFace && planPolygone.isFacingOutward)
            normal = normal.multiplier(-1); // Si la face pointe vers l'extérieur, on inverse le vecteur normal (vue de face)

        if (!vueDeFace && !planPolygone.isFacingOutward)
            normal = normal.multiplier(-1); // Si la face pointe vers l'intérieur, on inverse le vecteur normal (vue de haut ou de bas)

        double[] normalAngles = Calculateur3D.vecteurToAngle(normal); // Angles de la normale (horizontal, vertical)
        double angleH = Math.toRadians(normalAngles[0]);
        double angleV =  Math.toRadians(normalAngles[1]);
        resetZoom();
        double tanRadFOV = Math.tan(Math.toRadians(controleurCamera.getFOV() / 2.0));
        float diviseur = 1.5f; //Plus le diviseur est grand, plus la caméra est proche du polygone
//        double elementSize = polygone3DSelectionne.scale(pouceToPixel).getRayon();
        double elementSize = objet3D.getRayon()*2;
        float espacement = (float) ((elementSize / diviseur) / tanRadFOV + (elementSize  / diviseur)); // Espacement de la caméra par rapport au polygone
        Point3D posCamera2;

        Point3D centre = objet3D.getCenter();
        for (Objet3D objet : objet3DS) {
            if (getObjet3DSelectionne() instanceof IObjet3DCompose objet3DCompose){
                if (objet3DCompose.contient(objet)) {
                    objet.setVisible(true);
                    continue;
                }
            }
            if (objet != getObjet3DSelectionne()) {
                objet.setVisible(false);
            }
        }

        if (vueDeFace){
            controleurCamera.setOrientVerticale(angleV); // Mettre la caméra à l'angle de la normale
            controleurCamera.setOrientHorizontale(angleH); // Mettre la caméra à l'angle de la normale

            double dY = espacement * Math.sin(angleV); // espacement en Y
            double dZ = espacement * Math.cos(angleH); // espacement en Z
            double dX = espacement * Math.sin(angleH); // espacement en X
            posCamera2 = new Point3D(centre.x - dX, centre.y + dY, centre.z - dZ); // Nouvelle position de la caméra centrée sur le polygone

        }
        else {
            controleurCamera.setOrientVerticale(-(angleV - 0.000001)); // Mettre la caméra à l'angle de la normale
            controleurCamera.setOrientHorizontale(0); // Mettre la caméra à l'angle de la normale

            double dY = espacement * Math.sin(angleV); // espacement en Y
            posCamera2 = new Point3D(centre.x, centre.y + dY, centre.z); // Nouvelle position de la caméra centrée sur le polygone

        }
        setPositionCamera(posCamera2); // Mettre la caméra à la nouvelle position
        update();
        mode2D = true;

    }

    public void activerMode2D(Objet3D objet3D, boolean topView){
        // Déplacer la camera au centre de l'objet pour donner l'illusion de 2D
        Objet3D objet3DResized = objet3D.scale(pouceToPixel);
        if (objet3DResized == null) return;

        resetZoom();
        double tanRadFOV = Math.tan(Math.toRadians(controleurCamera.getFOV() / 2.0));
        float diviseur = 1.5f; //Plus le diviseur est grand, plus la caméra est proche du polygone

        double elementSize = objet3DResized.getRayon()*2;
        float espacement = (float) ((elementSize / diviseur) / tanRadFOV + (elementSize  / diviseur)); // Espacement de la caméra par rapport au polygone
        Point3D posCamera2;

        //Cacher les autres objets
        Point3D centre = objet3DResized.getCenter();
        for (Objet3D obj : objet3DS) {
            if (objet3D instanceof IObjet3DCompose objet3DCompose){
                if (objet3DCompose.contient(obj)) {
                    obj.setVisible(true);
                    continue;
                }
            }
            if (obj != objet3D){
                obj.setVisible(false);
            }
        }

        if (!topView){
            double angleH = 0;
            if (objet3D instanceof Mur3D mur3D){
                angleH = Math.toRadians(mur3D.orientation.getDirection()-90);
            }
            if (objet3D instanceof Toit3D toit3D){
               angleH = Math.toRadians(PointCardinal.NORD.getDirection() -90);
            }
            controleurCamera.setOrientVerticale(0); // Mettre la caméra à l'angle de la normale
            controleurCamera.setOrientHorizontale(angleH); // Mettre la caméra à l'angle de la normale

            double dY = espacement * Math.sin(0); // espacement en Y
            double dZ = espacement * Math.cos(angleH); // espacement en Z
            double dX = espacement * Math.sin(angleH); // espacement en X
            posCamera2 = new Point3D(centre.x - dX, centre.y + dY, centre.z - dZ); // Nouvelle position de la caméra centrée sur le polygone

        }
        else {
            controleurCamera.setOrientVerticale(-(Math.toRadians(90) - 0.000001)); // Mettre la caméra à l'angle de la normale
            controleurCamera.setOrientHorizontale(0); // Mettre la caméra à l'angle de la normale

            double dY = espacement * Math.sin(Math.toRadians(90)); // espacement en Y
            posCamera2 = new Point3D(centre.x, centre.y + dY, centre.z); // Nouvelle position de la caméra centrée sur le polygone

        }
        setPositionCamera(posCamera2); // Mettre la caméra à la nouvelle position

        update();
        majVue();
        mode2D = true;
//        this.grille = new Grille("2' 6\"", this, getPlanCamera(), espacement);
        plan2D = new Plan(getPlanCamera());
        distancePlan2D = espacement;
        regenererGrille();
    }

    public void setElementEnModification(UUID uuid){
        this.objet3DS.stream().filter(objet3D -> objet3D.getId() != null && objet3D.getId().equals(uuid)).findFirst().ifPresent(objet3D -> {
            this.objet3DSelectionne = objet3D;
        });
    };

    public void update() {
        mouvementCamera();
        updateAllObjets();
        definirOrdre();
    }

    public void ajouterObjet(Objet3D objet3D) {
        // Si l'objet est un cabanon3D, on ajoute ses composants
        if (objet3D instanceof Cabanon3D cabanon){
            for (Objet3D composant : cabanon.getComposants()){
                ajouterObjet(composant);
            }
            return;
        }
        objet3DS.add(objet3D);
        if (objet3D.polygons == null) return;
        for (Polygon3D polygon3D : objet3D.polygons) {
            if (polygones3D.contains(polygon3D)) continue;
            ajouterPolygone(polygon3D);
            int indexPoly = polygones3D.size() - 1;
            int indexObjet = objet3DS.size() - 1;
            polygones3DToObjet3D.put(indexPoly, indexObjet);
        }
    }

    public void ajouterPolygone(Polygon3D polygon3D) {
        Polygon2D polygon2D = polygon3D.getProjection(this, true, true);
        polygonesDessinables.add(polygon2D);
        polygones3D.add(polygon3D);
        int indexPoly = polygones3D.size() - 1;
        int indexObjet = objet3DS.size() - 1;
        polygones3DToObjet3D.put(indexPoly, indexObjet);
        incrementerNombreDePolygones();
    }
    public void updateAllObjets() {
        for (int i = 0; i < polygones3D.size(); i++) {
            updateObjet(polygones3D.get(i));
        }
        if (grille != null)
            grille.update(this);
    }

    public void updateObjet(Objet3D objet3D) {
        objet3D.update(this);
    }

    public void supprimerObjet(Objet3D objet3D) {
        if (objet3D == null) return;

        boolean removed = objet3DS.remove(objet3D);
        if (!removed) return;
        for (Polygon3D polygon3D : objet3D.polygons) {
            supprimerPolygone(polygon3D);
        }
    }

    public void supprimerAllObjets() {
        objet3DS.clear();
        polygonesDessinables.clear();
        polygones3D.clear();
        polygones3DToObjet3D.clear();
        nombreDePolygones2D = 0;
        nombreDePolygones3D = 0;
    }
    public void supprimerPolygone(Polygon3D polygon3D) {
        int indexPoly = polygones3D.indexOf(polygon3D);
        polygonesDessinables.remove(indexPoly);
        polygones3D.remove(indexPoly);
        polygones3DToObjet3D.remove(indexPoly);
        decrementerNombreDePolygones();
    }

    void regenererGrille() {
        if (!mode2D || plan2D==null) return;
        grille = new Grille(ValeurImperiale.convertirEnFraction(this.espacementGrille), this, plan2D, distancePlan2D);
    }

    public void setPolygone2DSelectionne(Point ptCurseur){
        if (ptCurseur == null) return;
        if (mode2D) return;
        polygone2DSelectionne = null;
        polygone3DSelectionne = null;
        objet3DSelectionne = null;
        ArrayList<Polygon2D> visibles = getPolygonesVisibles();
        ArrayList<Polygon3D> polygones3DVisibles = getPolygones3DVisibles();
        int[] ordreAffichageSelectable = getNouvelOrdreSelectable();
        for(int i = ordreAffichageSelectable.length-1; i >= 0; i--) {
            int indexPoly = ordreAffichageSelectable[i]; // Index = index du polygone dans la liste des polygones visibles
            Polygon2D poly = visibles.get(indexPoly);
            int vraiIndexPoly = polygonesDessinables.indexOf(poly);
            if (poly.selectable && poly.estPointe(ptCurseur) && poly.isVisible()) {
                polygone2DSelectionne = poly;
                polygone3DSelectionne = polygones3DVisibles.get(indexPoly);
                if (polygones3DToObjet3D.containsKey(vraiIndexPoly)&&objet3DS.size()>0)
                    objet3DSelectionne=obtenirObjetSelectionne(vraiIndexPoly);
                break;
            }
        }
    }

    //Retourne la composante primitive de la composante pointée
    private Objet3D selectionDePrecision(Objet3D obj3D){
        if (obj3D instanceof IObjet3DCompose){
            Objet3D composant = ((IObjet3DCompose) obj3D).getComposante(polygone3DSelectionne);
            while (composant instanceof IObjet3DCompose){
                composant = ((IObjet3DCompose) composant).getComposante(polygone3DSelectionne);
            }
            return composant;
        }
        return obj3D;
    }


    private Objet3D obtenirObjetSelectionne(int indexPoly) {
        Objet3D objet3D = objet3DS.get(polygones3DToObjet3D.get(indexPoly));
        if (this.modeSelectionPrecision){
            return selectionDePrecision(objet3D);
        }
        return objet3D;

    }


    //Définit l'ordre d'affichage des polygones (plus proche -> plus loin)
    public void definirOrdre() {

        ArrayList<Polygon2D> polygonesVisibles = getPolygonesVisibles();
        double[] distances = polygonesVisibles.stream().mapToDouble(Polygon2D::getDistanceMoyenne).toArray();
        nouvelOrdreAffichage = polygonesVisibles.stream().mapToInt(polygonesVisibles::indexOf).toArray();
        mergeSort(distances, nouvelOrdreAffichage, 0, distances.length - 1);

        nouvelOrdreDistances = distances;
    }

    private void mergeSort(double[] distances, int[] nouvelOrdre, int debut, int fin) {
        if (debut < fin) {
            int milieu = (debut + fin) / 2;
            mergeSort(distances, nouvelOrdre, debut, milieu);
            mergeSort(distances, nouvelOrdre, milieu + 1, fin);
            merge(distances, nouvelOrdre, debut, milieu, fin);
        }
    }

    private void merge(double[] distances, int[] nouvelOrdre, int debut, int milieu, int fin) {
        int taille1 = milieu - debut + 1;
        int taille2 = fin - milieu;

        double[] gauche = new double[taille1];
        double[] droite = new double[taille2];
        int[] gaucheOrdre = new int[taille1];
        int[] droiteOrdre = new int[taille2];

        for (int i = 0; i < taille1; i++) {
            gauche[i] = distances[debut + i];
            gaucheOrdre[i] = nouvelOrdre[debut + i];
        }
        for (int j = 0; j < taille2; j++) {
            droite[j] = distances[milieu + 1 + j];
            droiteOrdre[j] = nouvelOrdre[milieu + 1 + j];
        }

        int i = 0, j = 0, k = debut;
        while (i < taille1 && j < taille2) {
            if (gauche[i] > droite[j]) {
                distances[k] = gauche[i];
                nouvelOrdre[k] = gaucheOrdre[i];
                i++;
            } else {
                distances[k] = droite[j];
                nouvelOrdre[k] = droiteOrdre[j];
                j++;
            }
            k++;
        }
        while (i < taille1) {
            distances[k] = gauche[i];
            nouvelOrdre[k] = gaucheOrdre[i];
            i++;
            k++;
        }
        while (j < taille2) {
            distances[k] = droite[j];
            nouvelOrdre[k] = droiteOrdre[j];
            j++;
            k++;
        }
    }

    public void incrementerNombreDePolygones() {
        nombreDePolygones2D++;
        nombreDePolygones3D++;
    }
    public void decrementerNombreDePolygones() {
        nombreDePolygones2D--;
        nombreDePolygones3D--;
    }

    public int getNbPolygonesVisibles() {
        return (int) polygonesDessinables.stream().filter(Polygon2D::isVisible).count();
    }

    public int getNbPolygonesVisiblesApresCulling() {
        return (int) polygonesDessinables.stream().filter(Polygon2D::isVisible).filter(p -> p.canDraw(this)).count();
    }

    public ArrayList<Polygon2D> getPolygonesVisibles() {
        return polygonesDessinables.stream().filter(Polygon2D::isVisible).collect(Collectors.toCollection(ArrayList::new));
    }
    public ArrayList<Polygon3D> getPolygones3DVisibles() {
        return polygones3D.stream().filter(p -> p.projection.visible).collect(Collectors.toCollection(ArrayList::new));
    }
    private int getNombreDeSelectable() {
        return (int) polygonesDessinables.stream().filter(Polygon2D::isSelectable).count();
    }

    private int[] getNouvelOrdreSelectable(){
        ArrayList<Polygon2D> visible = getPolygonesVisibles();
        return Arrays.stream(nouvelOrdreAffichage).filter(i -> visible.get(i).isSelectable()).toArray();
    }
}