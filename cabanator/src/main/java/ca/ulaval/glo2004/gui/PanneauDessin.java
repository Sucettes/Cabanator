package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.Utilitaires.Dimension3D;
import ca.ulaval.glo2004._3D.*;
import ca.ulaval.glo2004.domain.GestionCabanon.ControleurCabanon;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.gui.Edition.EditionMur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.UUID;

import static ca.ulaval.glo2004.gui.Couleurs.GRIS_FONCE;
import static ca.ulaval.glo2004.gui.Vue.getPointCardinal;

public class PanneauDessin extends JPanel {
    ControleurCabanon controleurCabanon;
    double MaxFPS = 1000, LastRefresh = 0;
    private Controleur3D controleur3D;
    private ControleurCamera controleurCamera;
    private boolean isOpened = false;
    private Cabanon3D cabanon3D;
    private Robot r;
    Grille grille;
    private Vue vue;
    private EditionMur editionMur;

    private final MouseListener popupListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (!SwingUtilities.isRightMouseButton(e)) return;
            final PopupPanneauDessin popup = new PopupPanneauDessin(
                    controleurCabanon, e.getPoint(), vue, PanneauDessin.this);
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    };

    public PanneauDessin() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                controleur3D.toucheAppuyee(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                controleur3D.toucheRelachee(e);
//                repaint();
            }
        });
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                requestFocusInWindow();
            }
        });

        addMouseWheelListener(mouseWheelEvent -> {
            controleur3D.sourisScrolled(mouseWheelEvent);
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!hasFocus()) {
                    requestFocusInWindow();
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    controleur3D.sourisDeplacee(e.getX(), e.getY());
                    centrerSouris();

                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (isOpened) {
                    controleur3D.setTailleEcran(getSize());
                    repaint();
                }
            }
        });
    }

    public void creerProjet(Dimension3D dimension) {
        controleurCabanon.creerProjet(dimension);
    }

    void centrerSouris() {
        try {
            r = new Robot();
            r.mouseMove(this.getLocationOnScreen().x + this.getWidth() / 2, this.getLocationOnScreen().y + this.getHeight() / 2);

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private Objet3D getObjet3DFromVue(Vue vue) {
        return switch (vue) {
            case PLANCHER -> cabanon3D.getPlancher3D();
            case MUR_EST -> cabanon3D.getMur(PointCardinal.EST);
            case MUR_OUEST -> cabanon3D.getMur(PointCardinal.OUEST);
            case MUR_SUD -> cabanon3D.getMur(PointCardinal.SUD);
            case MUR_NORD -> cabanon3D.getMur(PointCardinal.NORD);
            case TOIT -> cabanon3D.getToit();
            default -> null;
        };
    }

    public void init3D(ControleurCabanon controleurCabanon) {
        Point3D positionCible = new Point3D(0, 0, 0);
        Point3D positionCamera = new Point3D(0, 5, 10);
        controleurCamera = new ControleurCamera(positionCamera, positionCible, this.getSize());
        controleur3D = new Controleur3D(controleurCamera, this.getSize());
        this.controleurCabanon = controleurCabanon;
        cabanon3D = new Cabanon3D(controleurCabanon.obtenirAffichageCabanon());
        controleur3D.ajouterObjet(cabanon3D);
        controleur3D.updateAllObjets();
        controleur3D.definirOrdre();
        isOpened = true;
    }

    public void init3D() {
        Point3D positionCible = new Point3D(0, 0, 0);
        Point3D positionCamera = new Point3D(0, 5, 10);
        controleurCamera = new ControleurCamera(positionCamera, positionCible, this.getSize());
        controleur3D = new Controleur3D(controleurCamera, this.getSize());
        cabanon3D = new Cabanon3D(controleurCabanon.obtenirAffichageCabanon());
        controleur3D.ajouterObjet(cabanon3D);

        controleur3D.updateAllObjets();
        controleur3D.definirOrdre();
        isOpened = true;
    }

    //Regénère le cabanon au complet
    public void regenerateCabanon() {
        if (!isOpened) return;

        controleur3D.supprimerAllObjets();
        cabanon3D = new Cabanon3D(controleurCabanon.obtenirAffichageCabanon()); // Comme Java utilise les références, controleurCabanon possède les modifications
        controleur3D.ajouterObjet(cabanon3D);
        controleur3D.updateAllObjets();
    }

    //Regénère la vue 2D spécifiée
    public void regenerateVue(Vue vue) {
        if (!isOpened) return;
        controleur3D.supprimerAllObjets(); // On supprime l'ancien objet

        cabanon3D = new Cabanon3D(controleurCabanon.obtenirAffichageCabanon()); // On recrée le cabanon
        cabanon3D.setVisible(false);
        Objet3D objet3D = getObjet3DFromVue(vue); // On récupère l'objet 3D correspondant à la vue

        if (objet3D == null)
            return;
        objet3D.setVisible(true);
        if (vue == Vue.MUR_NORD || vue == Vue.MUR_SUD){
            cabanon3D.getMur(PointCardinal.EST).setVisible(true);
            cabanon3D.getMur(PointCardinal.OUEST).setVisible(true);
        }
        controleur3D.ajouterObjet(cabanon3D);
        controleur3D.updateAllObjets();

        if (editionMur != null)
            editionMur.mettreAJourListesComposantsMur(getPointCardinal(vue));
    }

    public void configurerPopup(Vue vue, EditionMur editionMur) {
        this.vue = vue;
        this.editionMur = editionMur;
        addMouseListener(popupListener);
    }

    public void retirerPopup() {
        vue = null;
        editionMur = null;
        removeMouseListener(popupListener);
    }

    public void executeWhenVisible(Runnable runnable) {
        if (isShowing()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(() -> executeWhenVisible(runnable));
        }
    }

    public void clearAll() {
        controleur3D = null;
        controleurCamera = null;
        cabanon3D = null;
        controleurCabanon = null;
        isOpened = false;
        invalidate();
//        revalidate();
        repaint();
    }
    public void changerVueToit(boolean vueFace) {
        if (!isOpened) return;
        controleur3D.activerMode2D(cabanon3D.getToit(), !vueFace);
        repaint();
    }
    public void display3DObject(Vue vue, ControleurCabanon controleurCabanon) {
        Point3D positionCible = new Point3D(0, 0, 0);
        Point3D positionCamera = new Point3D(0, 5, 10);
        controleurCamera = null;
        controleur3D = null;
        cabanon3D = null;
        this.controleurCabanon = controleurCabanon;
        controleurCamera = new ControleurCamera(positionCamera, positionCible, this.getSize());
        controleur3D = new Controleur3D(controleurCamera, this.getSize());
        cabanon3D = new Cabanon3D(controleurCabanon.obtenirAffichageCabanon());
        cabanon3D.setVisible(false);

        Objet3D objet3D = getObjet3DFromVue(vue);


        if (objet3D == null)
            return;
        objet3D.setVisible(true);
        controleur3D.ajouterObjet(cabanon3D);
        controleur3D.updateAllObjets();
        controleur3D.definirOrdre();
        controleur3D.activerMode2D(objet3D, vue == Vue.PLANCHER);
        if (vue == Vue.MUR_NORD || vue == Vue.MUR_SUD){
            cabanon3D.getMur(PointCardinal.EST).setVisible(true);
            cabanon3D.getMur(PointCardinal.OUEST).setVisible(true);
        }
        isOpened = true;
        repaint();
    }

    //Vu dans un tutoriel (Je ne me souviens plus de où)
    void autoRepaint() {
        long timeSLU = (long) (System.currentTimeMillis() - LastRefresh);

   /*     Checks ++;
        if(Checks >= 15)
        {
            drawFPS = Checks/((System.currentTimeMillis() - LastFPSCheck)/1000.0);
            LastFPSCheck = System.currentTimeMillis();
            Checks = 0;
        }
*/
        if (timeSLU < 1000.0 / MaxFPS) {
            try {
                Thread.sleep((long) (1000.0 / MaxFPS - timeSLU));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LastRefresh = System.currentTimeMillis();

        repaint();
    }

    public void setElementEnModification(UUID id) {
        controleur3D.setElementEnModification(id);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        ((Graphics2D) g).setBackground(GRIS_FONCE);

        if (isOpened) {

            controleur3D.update();
            controleur3D.setPolygone2DSelectionne(getMousePosition());
            Afficheur3D afficheur3D = new Afficheur3D(controleur3D, getMousePosition());
            afficheur3D.draw(g2D);
            autoRepaint();
        }
    }
}