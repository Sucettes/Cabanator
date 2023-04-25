//package ca.ulaval.glo2004;
//
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Bloc;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.LigneEntremises;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Plancher;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Entremise;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Solive;
//import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
//import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;
//import org.junit.Test;
//
//import java.util.ArrayList;
//
//import static com.google.common.truth.Truth.assertThat;
//
//public class PlancherTests {
//    private Plancher plancherCorrect;
//    private ArrayList<ArrayList<Bloc>> structureInterne;
//
//    protected void init(double longueur, double largeur, double distanceEntreSolives, Point point) {
//        plancherCorrect = new Plancher(
//                new Point(longueur / 2, largeur / 2, 0),
//                longueur,
//                largeur,
//                distanceEntreSolives,
//                new ArrayList<>(),
//                structureInterne);
//    }
//
//    @Test
//    public void Tests() {
//        Plancher plancher = new Plancher(200, 200);
//        plancher.getPlanches().size();
//
////        plancher.ajouterLigneEntremises(new Point(0, 96, 0), 4);
////        plancher.regenererStructurePlancher();
////        System.out.println();
//    }
////    @Test
////    public void RegenererStructurePlancher_QuandUnBlocCompletAvecUneSolive() {
////        // 1x1
////        double longueur = 192;
////        double largeur = 192;
////        double distanceEntreSolives = 96;
////        double decalageEntreEntremises = 4;
////        this.init(longueur, largeur, distanceEntreSolives, new Point(0, 0, 0));
////        plancherCorrect.regenererStructurePlancher();
////        structureInterne = plancherCorrect.getStructureInterne();
////        plancherCorrect.ajouterLigneEntremises(new Point(0, 0, 0), decalageEntreEntremises);
////        ArrayList<LigneEntremises> ligneEntremises = plancherCorrect.getLigneEntremises();
////
////        assertThat(plancherCorrect.getLongueur()).isEqualTo(longueur);
////        assertThat(plancherCorrect.getLargeur()).isEqualTo(largeur);
////        assertThat(plancherCorrect.getDistanceEntreSolives()).isEqualTo(distanceEntreSolives);
////        assertThat(structureInterne.get(0).get(0).size()).isEqualTo(5);
////
////        assertThat(structureInterne.get(0).get(0).get(0)).isEqualTo(
////                new Solive(new Point(-96, -96, 0), Sens.VOIR_EPAISSEUR, 90, 192)); // ouest
////        assertThat(structureInterne.get(0).get(0).get(1)).isEqualTo(
////                new Solive(new Point(-96, 94, 0), Sens.VOIR_EPAISSEUR, 270, 192)); // est
////        assertThat(structureInterne.get(0).get(0).get(2)).isEqualTo(
////                new Solive(new Point(-96, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // nord
////        assertThat(structureInterne.get(0).get(0).get(3)).isEqualTo(
////                new Solive(new Point(94, -94, 0), Sens.VOIR_EPAISSEUR, 180, 188)); // sud
////
////        assertThat(structureInterne.get(0).get(0).get(4)).isEqualTo(
////                new Solive(new Point(0, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // solive interne 1
////
////        assertThat(ligneEntremises.size()).isEqualTo(1);
////        assertThat(ligneEntremises.get(0).getDecalageEntreEntremises()).isEqualTo(decalageEntreEntremises);
////    }
////
////    @Test
////    public void RegenererStructurePlancher_QuandUnBlocCompletAvecTroisSolives() {
////        // 1x1
////        double longueur = 192;
////        double largeur = 192;
////        double distanceEntreSolives = 62;
////        double decalageEntreEntremises = 4;
////        this.init(longueur, largeur, distanceEntreSolives, new Point(0, 0, 0));
////        plancherCorrect.regenererStructurePlancher();
////        structureInterne = plancherCorrect.getStructureInterne();
////        plancherCorrect.ajouterLigneEntremises(new Point(0, 0, 0), decalageEntreEntremises);
////        ArrayList<LigneEntremises> ligneEntremises = plancherCorrect.getLigneEntremises();
////
////        assertThat(plancherCorrect.getLongueur()).isEqualTo(longueur);
////        assertThat(plancherCorrect.getLargeur()).isEqualTo(largeur);
////        assertThat(plancherCorrect.getDistanceEntreSolives()).isEqualTo(distanceEntreSolives);
////        assertThat(structureInterne.get(0).get(0).size()).isEqualTo(7);
////
////        assertThat(structureInterne.get(0).get(0).get(0)).isEqualTo(
////                new Solive(new Point(-96, -96, 0), Sens.VOIR_EPAISSEUR, 90, 192)); // ouest
////        assertThat(structureInterne.get(0).get(0).get(1)).isEqualTo(
////                new Solive(new Point(-96, 94, 0), Sens.VOIR_EPAISSEUR, 270, 192)); // est
////        assertThat(structureInterne.get(0).get(0).get(2)).isEqualTo(
////                new Solive(new Point(-96, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // nord
////        assertThat(structureInterne.get(0).get(0).get(3)).isEqualTo(
////                new Solive(new Point(94, -94, 0), Sens.VOIR_EPAISSEUR, 180, 188)); // sud
////
////        assertThat(structureInterne.get(0).get(0).get(4)).isEqualTo(
////                new Solive(new Point(-34, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // solive interne 1
////        assertThat(structureInterne.get(0).get(0).get(5)).isEqualTo(
////                new Solive(new Point(28, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // solive interne 2
////        assertThat(structureInterne.get(0).get(0).get(6)).isEqualTo(
////                new Solive(new Point(90, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // solive interne 3
////
////        assertThat(ligneEntremises.size()).isEqualTo(1);
////        assertThat(ligneEntremises.get(0).getDecalageEntreEntremises()).isEqualTo(decalageEntreEntremises);
////    }
////
////    @Test
////    public void RegenererStructurePlancher_QuandUnBlocsCompletDistanceSoliveTropGrande() {
////        // 1x1
////        double longueur = 192;
////        double largeur = 192;
////        double distanceEntreSolives = 500;
////        double decalageEntreEntremises = 4;
////        this.init(longueur, largeur, distanceEntreSolives, new Point(0, 0, 0));
////        plancherCorrect.regenererStructurePlancher();
////        structureInterne = plancherCorrect.getStructureInterne();
////        plancherCorrect.ajouterLigneEntremises(new Point(0, 0, 0), decalageEntreEntremises);
////        ArrayList<LigneEntremises> ligneEntremises = plancherCorrect.getLigneEntremises();
////
////        assertThat(plancherCorrect.getLongueur()).isEqualTo(longueur);
////        assertThat(plancherCorrect.getLargeur()).isEqualTo(largeur);
////        assertThat(plancherCorrect.getDistanceEntreSolives()).isEqualTo(distanceEntreSolives);
////        assertThat(structureInterne.get(0).get(0).size()).isEqualTo(4);
////
////        assertThat(structureInterne.get(0).get(0).get(0)).isEqualTo(
////                new Solive(new Point(-96, -96, 0), Sens.VOIR_EPAISSEUR, 90, 192)); // ouest
////        assertThat(structureInterne.get(0).get(0).get(1)).isEqualTo(
////                new Solive(new Point(-96, 94, 0), Sens.VOIR_EPAISSEUR, 270, 192)); // est
////        assertThat(structureInterne.get(0).get(0).get(2)).isEqualTo(
////                new Solive(new Point(-96, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // nord
////        assertThat(structureInterne.get(0).get(0).get(3)).isEqualTo(
////                new Solive(new Point(94, -94, 0), Sens.VOIR_EPAISSEUR, 180, 188)); // sud
////
////        assertThat(ligneEntremises.size()).isEqualTo(1);
////        assertThat(ligneEntremises.get(0).getDecalageEntreEntremises()).isEqualTo(decalageEntreEntremises);
////    }
////
////    @Test
////    public void RegenererStructurePlancher_QuandDeuxBlocsCompletAvecUneSolive() {
////        // 2x1
////        double longueur = 384;
////        double largeur = 192;
////        double distanceEntreSolives = 96;
////        double decalageEntreEntremises = 4;
////        this.init(longueur, largeur, distanceEntreSolives, new Point(0, 0, 0));
////        plancherCorrect.regenererStructurePlancher();
////        structureInterne = plancherCorrect.getStructureInterne();
////        plancherCorrect.ajouterLigneEntremises(new Point(0, 0, 0), decalageEntreEntremises);
////        ArrayList<LigneEntremises> ligneEntremises = plancherCorrect.getLigneEntremises();
////
////        assertThat(plancherCorrect.getLongueur()).isEqualTo(longueur);
////        assertThat(plancherCorrect.getLargeur()).isEqualTo(largeur);
////        assertThat(plancherCorrect.getDistanceEntreSolives()).isEqualTo(distanceEntreSolives);
////        assertThat(structureInterne.get(0).size()).isEqualTo(2);
////        assertThat(structureInterne.get(0).get(0).size()).isEqualTo(5);
////        assertThat(structureInterne.get(0).get(1).size()).isEqualTo(5);
////
////        // bloc 1 1
////        assertThat(structureInterne.get(0).get(0).get(0)).isEqualTo(
////                new Solive(new Point(-192, -96, 0), Sens.VOIR_EPAISSEUR, 90, 192)); // ouest
////        assertThat(structureInterne.get(0).get(0).get(1)).isEqualTo(
////                new Solive(new Point(-192, 94, 0), Sens.VOIR_EPAISSEUR, 270, 192)); // est
////        assertThat(structureInterne.get(0).get(0).get(2)).isEqualTo(
////                new Solive(new Point(-192, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // nord
////        assertThat(structureInterne.get(0).get(0).get(3)).isEqualTo(
////                new Solive(new Point(-2, -94, 0), Sens.VOIR_EPAISSEUR, 180, 188)); // sud
////
////        assertThat(structureInterne.get(0).get(0).get(4)).isEqualTo(
////                new Solive(new Point(-96, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // solive interne 1
////
////        // bloc 1 2
////        assertThat(structureInterne.get(0).get(1).get(0)).isEqualTo(
////                new Solive(new Point(-0, -96, 0), Sens.VOIR_EPAISSEUR, 90, 192)); // ouest
////        assertThat(structureInterne.get(0).get(1).get(1)).isEqualTo(
////                new Solive(new Point(0, 94, 0), Sens.VOIR_EPAISSEUR, 270, 192)); // est
////        assertThat(structureInterne.get(0).get(1).get(2)).isEqualTo(
////                new Solive(new Point(0, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // nord
////        assertThat(structureInterne.get(0).get(1).get(3)).isEqualTo(
////                new Solive(new Point(190, -94, 0), Sens.VOIR_EPAISSEUR, 180, 188)); // sud
////
////        assertThat(structureInterne.get(0).get(1).get(4)).isEqualTo(
////                new Solive(new Point(96, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // solive interne 1
////
////        assertThat(ligneEntremises.size()).isEqualTo(1);
////        assertThat(ligneEntremises.get(0).getDecalageEntreEntremises()).isEqualTo(decalageEntreEntremises);
////    }
////
////    @Test
////    public void RegenererStructurePlancher_Quand4BlocsIncomplet() {
////        // 2x2 mais le dernier fais moins de 16 pieds
////        double longueur = 288;
////        double largeur = 288;
////        double distanceEntreSolives = 96;
////        double decalageEntreEntremises = 4;
////        this.init(longueur, largeur, distanceEntreSolives, new Point(0, 0, 0));
////        plancherCorrect.regenererStructurePlancher();
////        structureInterne = plancherCorrect.getStructureInterne();
////        plancherCorrect.ajouterLigneEntremises(new Point(0, 0, 0), decalageEntreEntremises);
////        ArrayList<LigneEntremises> ligneEntremises = plancherCorrect.getLigneEntremises();
////
////        assertThat(plancherCorrect.getLongueur()).isEqualTo(longueur);
////        assertThat(plancherCorrect.getLargeur()).isEqualTo(largeur);
////        assertThat(plancherCorrect.getDistanceEntreSolives()).isEqualTo(distanceEntreSolives);
////        assertThat(structureInterne.get(0).size()).isEqualTo(2);
////        assertThat(structureInterne.get(1).size()).isEqualTo(2);
////        assertThat(structureInterne.get(0).get(0).size()).isEqualTo(5);
////        assertThat(structureInterne.get(0).get(1).size()).isEqualTo(4);
////        assertThat(structureInterne.get(1).get(0).size()).isEqualTo(5);
////        assertThat(structureInterne.get(1).get(1).size()).isEqualTo(4);
////
////        // bloc 0 0
////        assertThat(structureInterne.get(0).get(0).get(0)).isEqualTo(
////                new Solive(new Point(-144, -144, 0), Sens.VOIR_EPAISSEUR, 90, 192)); // ouest
////        assertThat(structureInterne.get(0).get(0).get(1)).isEqualTo(
////                new Solive(new Point(-144, 50, 0), Sens.VOIR_EPAISSEUR, 270, 192)); // est
////        assertThat(structureInterne.get(0).get(0).get(2)).isEqualTo(
////                new Solive(new Point(-144, -142, 0), Sens.VOIR_EPAISSEUR, 0, 192)); // nord
////        assertThat(structureInterne.get(0).get(0).get(3)).isEqualTo(
////                new Solive(new Point(46, -142, 0), Sens.VOIR_EPAISSEUR, 180, 192)); // sud
////
////        assertThat(structureInterne.get(0).get(0).get(4)).isEqualTo(
////                new Solive(new Point(-48, -142, 0), Sens.VOIR_EPAISSEUR, 0, 192)); // solive interne 1
////
////        // bloc 0 1
////        assertThat(structureInterne.get(0).get(1).get(0)).isEqualTo(
////                new Solive(new Point(48, -144, 0), Sens.VOIR_EPAISSEUR, 90, 96)); // ouest
////        assertThat(structureInterne.get(0).get(1).get(1)).isEqualTo(
////                new Solive(new Point(48, 50, 0), Sens.VOIR_EPAISSEUR, 270, 96)); // est
////        assertThat(structureInterne.get(0).get(1).get(2)).isEqualTo(
////                new Solive(new Point(48, -142, 0), Sens.VOIR_EPAISSEUR, 0, 192)); // nord
////        assertThat(structureInterne.get(0).get(1).get(3)).isEqualTo(
////                new Solive(new Point(142, -142, 0), Sens.VOIR_EPAISSEUR, 180, 192)); // sud
////
////        // bloc 1 0
////        assertThat(structureInterne.get(1).get(0).get(0)).isEqualTo(
////                new Solive(new Point(-144, 52, 0), Sens.VOIR_EPAISSEUR, 90, 192)); // ouest
////        assertThat(structureInterne.get(1).get(0).get(1)).isEqualTo(
////                new Solive(new Point(-144, 142, 0), Sens.VOIR_EPAISSEUR, 270, 192)); // est
////        assertThat(structureInterne.get(1).get(0).get(2)).isEqualTo(
////                new Solive(new Point(-144, 54, 0), Sens.VOIR_EPAISSEUR, 0, 88)); // nord
////        assertThat(structureInterne.get(1).get(0).get(3)).isEqualTo(
////                new Solive(new Point(46, 54, 0), Sens.VOIR_EPAISSEUR, 180, 88)); // sud
////
////        assertThat(structureInterne.get(1).get(0).get(4)).isEqualTo(
////                new Solive(new Point(-48, 54, 0), Sens.VOIR_EPAISSEUR, 0, 88)); // solive interne 1
////
////        // bloc 1 1
////        assertThat(structureInterne.get(1).get(1).get(0)).isEqualTo(
////                new Solive(new Point(48, 52, 0), Sens.VOIR_EPAISSEUR, 90, 96)); // ouest
////        assertThat(structureInterne.get(1).get(1).get(1)).isEqualTo(
////                new Solive(new Point(48, 142, 0), Sens.VOIR_EPAISSEUR, 270, 96)); // est
////        assertThat(structureInterne.get(1).get(1).get(2)).isEqualTo(
////                new Solive(new Point(48, 54, 0), Sens.VOIR_EPAISSEUR, 0, 88)); // nord
////        assertThat(structureInterne.get(1).get(1).get(3)).isEqualTo(
////                new Solive(new Point(142, 54, 0), Sens.VOIR_EPAISSEUR, 180, 88)); // sud
////
////        assertThat(ligneEntremises.size()).isEqualTo(1);
////        assertThat(ligneEntremises.get(0).getDecalageEntreEntremises()).isEqualTo(decalageEntreEntremises);
////    }
//
//    //#region Entremises
//    /**
//     @Test public void AjouterLigneEntremises_AjouteUneLigneALaListe() {
//     double longueur = 192;
//     double largeur = 192;
//     double distanceEntreSolives = 96;
//     double decalageEntreEntremises = 4;
//     this.init(longueur, largeur, distanceEntreSolives, new Point(0, 0, 0));
//     plancherCorrect.regenererStructurePlancher();
//     structureInterne = plancherCorrect.getStructureInterne();
//     plancherCorrect.ajouterLigneEntremises(new Point(0, 96, 0), decalageEntreEntremises);
//     ArrayList<LigneEntremises> ligneEntremises = plancherCorrect.getLigneEntremises();
//     System.out.println();
//     //        assertThat(plancherCorrect.getLongueur()).isEqualTo(longueur);
//     //        assertThat(plancherCorrect.getLargeur()).isEqualTo(largeur);
//     //        assertThat(plancherCorrect.getDistanceEntreSolives()).isEqualTo(distanceEntreSolives);
//     //        assertThat(structureInterne.get(0).get(0).size()).isEqualTo(7);
//     //
//     //        assertThat(structureInterne.get(0).get(0).get(0)).isEqualTo(
//     //                new Solive(new Point(-96, -96, 0), Sens.VOIR_EPAISSEUR, 90, 192)); // ouest
//     //        assertThat(structureInterne.get(0).get(0).get(1)).isEqualTo(
//     //                new Solive(new Point(-96, 94, 0), Sens.VOIR_EPAISSEUR, 270, 192)); // est
//     //        assertThat(structureInterne.get(0).get(0).get(2)).isEqualTo(
//     //                new Solive(new Point(-96, -94, 0), Sens.VOIR_EPAISSEUR, 0, 188)); // nord
//     //        assertThat(structureInterne.get(0).get(0).get(3)).isEqualTo(
//     //                new Solive(new Point(94, -94, 0), Sens.VOIR_EPAISSEUR, 180, 188)); // sud
//
//
//     //        assertThat(ligneEntremises.size()).isEqualTo(1);
//     //        assertThat(ligneEntremises.get(0).getDecalageEntreEntremises()).isEqualTo(decalageEntreEntremises);
//
//     // Modification des parametres pour tester la regeneration.
//
//
//     plancherCorrect.configurerLargeur(300);
//     plancherCorrect.configurerLongueur(384);
//     plancherCorrect.ajouterLigneEntremises(new Point(0, 15, 0), decalageEntreEntremises);
//     plancherCorrect.regenererStructurePlancher();
//     ArrayList<LigneEntremises> ligneEntremises2 = plancherCorrect.getLigneEntremises();
//     System.out.println();
//     }
//     */
//    //#endregion
//
//}