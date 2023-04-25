//package ca.ulaval.glo2004;
//
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Mur;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
//import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
//import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;
//import org.junit.Test;
//
//import java.util.ArrayList;
//
//public class MurTests {
//    private Mur murCorrect;
//    private ArrayList<ArrayList<ArrayList<Planche>>> structureInterne;
//
//    protected void init(double longueur, double hauteur, double distanceEntreMontants, Point point, PointCardinal pointCardinal) {
//        murCorrect = new Mur(pointCardinal, point, longueur, hauteur);
//
//        murCorrect.configurerDistanceEntreMontants(distanceEntreMontants);
//
//        murCorrect.regenererStructureMur();
//        structureInterne = murCorrect.getStructureInterne();
//    }
//
//    //#region mur Nord
//    @Test
//    public void RegenererStructureMur_QuandUnBlocMurNordComplet() {
//        // 1x1
//        double longueur = 192;
//        double hauteur = 192;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.NORD);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandDeuxBlocsMurNordComplet() {
//        // 2x1
//        double longueur = 384;
//        double hauteur = 192;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.NORD);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandQuatreBlocsMurNordComplet() {
//        // 2x2
//        double longueur = 384;
//        double hauteur = 384;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.NORD);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandQuatreBlocsMurNordIncomplet() {
//        // 2x2 mais le dernier fais moins de 16 pieds
//        double longueur = 288;
//        double hauteur = 288;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.NORD);
//    }
//
//    //#endregion
//
//    //#region mur SUD
//    @Test
//    public void RegenererStructureMur_QuandUnBlocMurSUDComplet() {
//        // 1x1
//        double longueur = 192;
//        double hauteur = 192;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.SUD);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandDeuxBlocsMurSUDComplet() {
//        // 2x1
//        double longueur = 384;
//        double hauteur = 192;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.SUD);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandQuatreBlocsMurSUDComplet() {
//        // 2x2
//        double longueur = 384;
//        double hauteur = 384;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.SUD);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandQuatreBlocsMurSUDIncomplet() {
//        // 2x2 mais le dernier fais moins de 16 pieds
//        double longueur = 288;
//        double hauteur = 288;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.SUD);
//    }
//    //#endregion
//
//    //#region mur OUEST
//
//    @Test
//    public void RegenererStructureMur_QuandUnBlocMurOuestComplet() {
//        // 1x1
//        double longueur = 192;
//        double hauteur = 192;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.OUEST);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandDeuxBlocsMurOuestComplet() {
//        // 2x1
//        double longueur = 384;
//        double hauteur = 192;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.OUEST);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandQuatreBlocsMurOuestComplet() {
//        // 2x2
//        double longueur = 384;
//        double hauteur = 384;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.OUEST);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandQuatreBlocsMurOuestIncomplet() {
//        // 2x2 mais le dernier fais moins de 16 pieds
//        double longueur = 288;
//        double hauteur = 288;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.OUEST);
//    }
//    //#endregion
//
//    //#region mur EST
//    @Test
//    public void RegenererStructureMur_QuandUnBlocMurEstComplet() {
//        // 1x1
//        double longueur = 192;
//        double hauteur = 192;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.EST);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandDeuxBlocsMurEstComplet() {
//        // 2x1
//        double longueur = 384;
//        double hauteur = 192;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.EST);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandQuatreBlocsMurEstComplet() {
//        // 2x2
//        double longueur = 384;
//        double hauteur = 384;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.EST);
//    }
//
//    @Test
//    public void RegenererStructureMur_QuandQuatreBlocsMurEstIncomplet() {
//        // 2x2 mais le dernier fais moins de 16 pieds
//        double longueur = 288;
//        double hauteur = 288;
//        double distanceEntreMontants = 96;
//        init(longueur, hauteur, distanceEntreMontants, new Point(0, 0, 0), PointCardinal.EST);
//    }
//    //#endregion
//}
