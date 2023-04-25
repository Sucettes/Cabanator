package ca.ulaval.glo2004._3D;

import ca.ulaval.glo2004.domain.Afficheur.Afficheur;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;
import ca.ulaval.glo2004.gui.Couleurs;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

import static ca.ulaval.glo2004.gui.Couleurs.COULEUR_FOND_3D;
import static ca.ulaval.glo2004.gui.Couleurs.COULEUR_TEXTE;

public class Afficheur3D extends Afficheur {
    Controleur3D controleur3D;
    int taillePointeur = 6;
    int tailleTexte = 13;
    Grille grille;
    Point2D ptCurseur = new Point2D.Double(0, 0);
    Ligne rayonCurseur;
    private LinkedList<String> listeDebugInfos = new LinkedList<String>();
    private LinkedList<String> listeCommandes = new LinkedList<String>();
    public Afficheur3D(Controleur3D controleur3D) {
        this.controleur3D = controleur3D;
    }
    public Afficheur3D(Controleur3D controleur3D, Point2D ptCurseur) {
        this.controleur3D = controleur3D;
        this.ptCurseur = ptCurseur;
    }
    private void setDebugInfos(){
        listeDebugInfos.clear();
        Point3D positionCamera = controleur3D.getPositionCamera();
        Point3D positionCible = controleur3D.getPositionCible();
        double zoom = controleur3D.getZoom();
        double yaw = controleur3D.controleurCamera.getAngleHorizontal();
        PointCardinal pointCardinal = Calculateur3D.angleToPointCardinal(yaw);
        double pitch = controleur3D.controleurCamera.getAngleVertical();
        double fov = controleur3D.controleurCamera.getFOV();
        double renderDistance = controleur3D.controleurCamera.renderDistance;
        int nombrePolygones = controleur3D.getNbDessinables();
        int nombrePolygonesVisibles = controleur3D.getNbPolygonesVisiblesApresCulling();
        Polygon3D polygoneSelectionne = controleur3D.polygone3DSelectionne;
        boolean selectionPrecision = controleur3D.modeSelectionPrecision;
        ArrayList<Point3D> nearPlane = controleur3D.controleurCamera.getPoints();
        double espacementGrille = controleur3D.espacementGrille;
        boolean grilleActivee = controleur3D.grilleActivee;

        if (controleur3D.showDebug) listeDebugInfos.add("Position de la caméra: X: %.5f Y: %.5f Z: %.5f".formatted(positionCamera.getX(), positionCamera.getY(), positionCamera.getZ()));
        if (controleur3D.showDebug) listeDebugInfos.add("Position de la cible: X: %.5f Y: %.5f Z: %.5f".formatted(positionCible.getX(), positionCible.getY(), positionCible.getZ()));
        if (controleur3D.showDebug) listeDebugInfos.add("Zoom: %4.2f".formatted(zoom));
        if (controleur3D.showDebug) listeDebugInfos.add("Yaw: %.5f (%s)".formatted(yaw, pointCardinal));
        if (controleur3D.showDebug) listeDebugInfos.add("Pitch: %.5f".formatted(pitch));
        if (controleur3D.showDebug) listeDebugInfos.add("FOV: %.3f".formatted(fov));
        if (controleur3D.showDebug) listeDebugInfos.add("Render distance: %3f".formatted(renderDistance));
        if (controleur3D.showDebug) listeDebugInfos.add("Nombre de polygones: %d".formatted(nombrePolygones));
        if (controleur3D.showDebug) listeDebugInfos.add("Nombre de polygones visibles: %d".formatted(nombrePolygonesVisibles));
        if (controleur3D.showDebug) if (polygoneSelectionne != null)
            listeDebugInfos.add("Polygone selectionné: %s".formatted(polygoneSelectionne.scale(controleur3D.pouceToPixel)));
        if (controleur3D.showDebug) if (controleur3D.objet3DSelectionne != null)
            listeDebugInfos.add("Objet selectionné: %s".formatted(controleur3D.objet3DSelectionne.scale(controleur3D.pouceToPixel)));
        if (controleur3D.showDebug) listeDebugInfos.add("Mode sélection de précision: %s".formatted(selectionPrecision));
        if (controleur3D.showDebug) listeDebugInfos.add("Pouce to pixel ratio: %f".formatted(controleur3D.pouceToPixel));
        if (controleur3D.showDebug) listeDebugInfos.add("Orientation V : %s".formatted(controleur3D.controleurCamera.orientVerticale));
        if (controleur3D.showDebug) listeDebugInfos.add("Orientation H : %s".formatted(controleur3D.controleurCamera.orientHorizontale));
        if (controleur3D.showDebug) listeDebugInfos.add("Culling: %s".formatted(controleur3D.seuilCulling));
        if (controleur3D.showDebug) listeDebugInfos.add("Near plane: %s".formatted(nearPlane));
        listeDebugInfos.add("Espacement de la grille: %s".formatted(ValeurImperiale.convertirEnFraction(espacementGrille)));
        listeDebugInfos.add("Grille activee: %s".formatted(grilleActivee));

    }

    private void setCommandes(){
        listeCommandes.clear();
        listeCommandes.add("Controles: ");
        listeCommandes.add("WASD pour bouger la caméra");
        listeCommandes.add("Molette pour zoomer");
        listeCommandes.add("RightClick Drag pour orienter la caméra");
        listeCommandes.add("R: Reset caméra");
        listeCommandes.add("P: Activer/Désactiver le mode sélection de précision");
        listeCommandes.add("Numpad 9: Augmenter la distance de rendu");
        listeCommandes.add("Numpad 3: Diminuer la distance de rendu");
        listeCommandes.add("I: Diminuer le ratio pouce/pixel");
        listeCommandes.add("O: Augmenter le ratio pouce/pixel");
        listeCommandes.add("Numpad 8: Augmenter le seuil de culling"); // Pour cull les polygones cachés par d'autres
        listeCommandes.add("Numpad 2: Diminuer le seuil de culling"); // Pour cull les polygones cachés par d'autres
        listeCommandes.add("G: Afficher/Cacher la grille");
        listeCommandes.add("V: Augmenter l'espacement de la grille");
        listeCommandes.add("B: Diminuer l'espacement de la grille");
        listeCommandes.add("F3: Afficher/Cacher les infos de debug");


    }

    void drawPointeur(Graphics g)
    {
        g.setColor(COULEUR_TEXTE);
        g.drawLine((int)(controleur3D.tailleEcran.getWidth()/2 - taillePointeur), (int)(controleur3D.tailleEcran.getHeight()/2), (int)(controleur3D.tailleEcran.getWidth()/2 + taillePointeur), (int)(controleur3D.tailleEcran.getHeight()/2));
        g.drawLine((int)(controleur3D.tailleEcran.getWidth()/2), (int)(controleur3D.tailleEcran.getHeight()/2 - taillePointeur), (int)(controleur3D.tailleEcran.getWidth()/2), (int)(controleur3D.tailleEcran.getHeight()/2 + taillePointeur));
    }


    void drawDebugInfos(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(COULEUR_TEXTE);
        g2D.setFont(new Font("Arial", Font.PLAIN, tailleTexte));

        setDebugInfos();

        for (String info : listeDebugInfos) {
            g2D.drawString(info, 10, tailleTexte * (listeDebugInfos.indexOf(info) + 1));
        }
    }

    void drawLine(Graphics g, Line2D line){
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(line.color);
        g2D.setStroke(new BasicStroke((float) line.epaisseur));
        g2D.drawLine((int) line.p1.getX(), (int) line.p1.getY(), (int) line.p2.getX(), (int) line.p2.getY());

    }

    void drawCommandes(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(COULEUR_TEXTE);
        g2D.setFont(new Font("Arial", Font.BOLD, tailleTexte));

        setCommandes();

        int lineOffset = listeDebugInfos.size() + 1 + 1; //+1 pour laisser une ligne vide entre les infos et les commandes
        for (String commande : listeCommandes) {
            g2D.drawString(commande, 10, tailleTexte * (listeCommandes.indexOf(commande) + lineOffset));
        }
    }
    private Color colorWithAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }


    void drawPolygone(Polygon2D polygone, Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        if (polygone.canDraw(controleur3D)) {
            Objet3D objet3D = controleur3D.objet3DSelectionne;
            //REMINDER: NE PAS DÉCOMMENTER LA LIGNE CI-DESSOUS
            /*ArrayList<Polygon2D> polygonesObjet = new ArrayList<>();
            if (objet3D!=null)
                polygonesObjet = new ArrayList<>(Arrays.stream(objet3D.polygons).map(p -> p.getProjection(controleur3D)).toList());*/

            Color couleurSelection = polygone.couleur.darker(); // Mieux que la transparence au niveau performance
            Color couleurInvalide = Couleurs.COULEUR_ELEMENT_INVALIDE;
            if (!polygone.estValide)
                polygone.setCouleur(couleurInvalide);
            else if (controleur3D.polygone2DSelectionne == polygone){
                polygone.setCouleur(couleurSelection);
//                drawLine(g, polygone.normal);
            }
            else if (objet3D!=null && objet3D.contient(polygone))
                polygone.setCouleur(couleurSelection);

            polygone.dessinerPolygon(g2D);
        }
    }



    @Override
    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED
        );
        g2D.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );
        g2D.setRenderingHint(
                RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_DISABLE
        );
        g2D.setRenderingHint(
                RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED
        );
        g2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        g2D.setRenderingHint(
                RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_SPEED);
        ((Graphics2D) g).setBackground(COULEUR_FOND_3D);
        g.clearRect(0, 0, 2000, 1200);

        ArrayList<Polygon2D> polygones = controleur3D.getPolygonesVisibles();
        if (polygones!=null && controleur3D.nouvelOrdreAffichage.length>0)
            for (int i = 0; i < polygones.size(); i++) {
                Polygon2D polygone = polygones.get(controleur3D.nouvelOrdreAffichage[i]);
                drawPolygone(polygone, g2D);

            }

        drawDebugInfos(g2D);
        drawCommandes(g2D);
        drawPointeur(g2D);
        drawGrille(g2D);

    }

    void drawGrille(Graphics g){
        if (!controleur3D.grilleActivee) return;
        Grille grille = controleur3D.grille;
        if (grille!=null) {
            for (Ligne ligne : grille.lignes) {
                drawLine(g, ligne.getProjection());
            }
        }
    }

}
