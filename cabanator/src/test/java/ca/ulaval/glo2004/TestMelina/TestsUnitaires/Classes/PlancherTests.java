//package ca.ulaval.glo2004.TestMelina.TestsUnitaires.Classes;
//
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Plancher;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Solive;
//import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
//import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
//import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//import org.junit.runners.Parameterized;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import static com.google.common.truth.Truth.assertThat;
//
//public class PlancherTests {
//    protected Plancher plancher;
//
//    protected void genererPlancher(double longueur, double largeur) {
//        plancher = new Plancher(longueur, largeur);
//    }
//
//    protected void genererPlancher(double longueur, double largeur, double distanceEntreEntremise) {
//        plancher = new Plancher(longueur, largeur, distanceEntreEntremise);
//    }
//
//    public static class ConstructeurTests extends PlancherTests {
//        protected double longueur;
//        protected double largeur;
//
//        @RunWith(Parameterized.class)
//        public static class DevraitCreerInstanceTests extends ConstructeurTests {
//            public DevraitCreerInstanceTests(double longueur, double largeur) {
//                this.longueur = longueur;
//                this.largeur = largeur;
//            }
//
//            @Parameterized.Parameters(name = "{index}: ConstructeurTests({0}, {1})")
//            public static Iterable<Object[]> data() {
//                return Arrays.asList(new Object[][]{
//                                {10, 20},
//                                {192, 192},
//                                {200, 300}
//                        }
//                );
//            }
//
//            @Test
//            public void NonNull() {
//                System.out.println(super.longueur);
//                System.out.println(super.largeur);
//                System.out.println("___");
//                super.genererPlancher(super.longueur, super.largeur);
//
//                assertThat(super.plancher).isNotNull();
//            }
//        }
//
//        public static class InstanceGenereeCorrectementTests extends PlancherTests {
//            private double longueur;
//            private double largeur;
//            private double distanceEntreSolives;
//            private Planche[][][] structure;
//
//            @Test
//            public void Constructeur_AvecDimensionsInferieuresLimite_Et3PoucesEntreSolives_DevraitCreerInstanceCorrectement() {
//                this.longueur = 12;
//                this.largeur = 10;
//                this.distanceEntreSolives = 3;
//
//                Point positionSoliveOuestAttendue = new Point(0, 0, 0);
//                Solive soliveOuest = new Solive(
//                        positionSoliveOuestAttendue,
//                        Sens.VOIR_EPAISSEUR,
//                        PointCardinal.OUEST.getDirection(),
//                        this.longueur
//                );
//
//                Point positionSoliveEstAttendue = new Point(0, 0, this.largeur - soliveOuest.getEpaisseurSelonSens());
//                Solive soliveEst = new Solive(
//                        positionSoliveEstAttendue,
//                        Sens.VOIR_EPAISSEUR,
//                        PointCardinal.EST.getDirection(),
//                        this.longueur
//                );
//
//                double longueurPlancheNordSudAttendue = this.largeur - 4;
//
//                Point positionSoliveNordAttendue = new Point(0, 0, 2);
//                Solive soliveNord = new Solive(
//                        positionSoliveNordAttendue,
//                        Sens.VOIR_EPAISSEUR,
//                        PointCardinal.NORD.getDirection(),
//                        longueurPlancheNordSudAttendue
//                );
//
//                Point positionSoliveSudAttendue = new Point(
//                        this.longueur - soliveNord.getEpaisseurSelonSens(),
//                        soliveNord.getPosition().getY(),
//                        soliveNord.getPosition().getZ());
//                Solive soliveSud = new Solive(
//                        positionSoliveSudAttendue,
//                        Sens.VOIR_EPAISSEUR,
//                        PointCardinal.SUD.getDirection(),
//                        longueurPlancheNordSudAttendue
//                );
//
//                // x = 3
//                Point positionSoliveInterieurBloc1 = new Point(
//                        positionSoliveNordAttendue.getX() + this.distanceEntreSolives,
//                        positionSoliveNordAttendue.getY(),
//                        positionSoliveNordAttendue.getZ());
//                // x = 6
//                Point positionSoliveInterieurBloc2 = new Point(
//                        positionSoliveNordAttendue.getX() + this.distanceEntreSolives * 2,
//                        positionSoliveNordAttendue.getY(),
//                        positionSoliveNordAttendue.getZ());
//                // x = 9
//                Point positionSoliveInterieurBloc3 = new Point(
//                        positionSoliveNordAttendue.getX() + this.distanceEntreSolives * 3,
//                        positionSoliveNordAttendue.getY(),
//                        positionSoliveNordAttendue.getZ());
//
//                Point[] positionAttendues = {
//                        positionSoliveOuestAttendue,
//                        positionSoliveEstAttendue,
//                        positionSoliveNordAttendue,
//                        positionSoliveSudAttendue,
//                        positionSoliveInterieurBloc1,
//                        positionSoliveInterieurBloc2,
//                        positionSoliveInterieurBloc3
//                };
//
//                this.structure = new Planche[][][]{{
//                        {
//                                soliveOuest,
//                                soliveEst,
//                                soliveNord,
//                                soliveSud,
//                                new Solive(
//                                        positionSoliveInterieurBloc1,
//                                        Sens.VOIR_EPAISSEUR,
//                                        PointCardinal.NORD.getDirection(),
//                                        longueurPlancheNordSudAttendue
//                                ),
//                                new Solive(
//                                        positionSoliveInterieurBloc2,
//                                        Sens.VOIR_EPAISSEUR,
//                                        PointCardinal.NORD.getDirection(),
//                                        longueurPlancheNordSudAttendue
//                                ),
//                                new Solive(
//                                        positionSoliveInterieurBloc3,
//                                        Sens.VOIR_EPAISSEUR,
//                                        PointCardinal.NORD.getDirection(),
//                                        longueurPlancheNordSudAttendue
//                                )
//                        }
//                }};
//
//                System.out.println(this.longueur);
//                System.out.println(this.largeur);
//                System.out.println(this.distanceEntreSolives);
//                System.out.println("___");
//
//                super.genererPlancher(this.longueur, this.largeur, this.distanceEntreSolives);
//
//                assertThat(super.plancher).isNotNull();
//
//                ArrayList<Planche> planches = super.plancher.getPlanches();
//                assertThat(planches).containsNoDuplicates();
//                assertThat(planches.size()).isEqualTo(positionAttendues.length);
//
//                for (int i = 0; i < planches.size(); i++) {
//                    // Vérifie la position
//                    System.out.println(planches.get(i).getPosition().getX() + ", "
//                            + planches.get(i).getPosition().getY() + ", "
//                            + planches.get(i).getPosition().getZ());
//                    System.out.println(positionAttendues[i].getX() + ", "
//                            + positionAttendues[i].getY() + ", "
//                            + positionAttendues[i].getZ());
//                    System.out.println("___");
//                    assertThat(planches.get(i).getPosition()).isEqualTo(positionAttendues[i]);
//
//                    // Vérifie si la position est valide
//                    if (i == planches.size() - 1) {
//                        assertThat(planches.get(i).getPositionEstValide()).isFalse();
//                    } else {
//                        assertThat(planches.get(i).getPositionEstValide()).isTrue();
//                    }
//                }
//            }
//
//            public void Constructeur_AvecDimensionsEgalesALimite_Et25PoucesEntreSolives_DevraitCreerInstanceCorrectement() {
//                this.longueur = Planche.LIMITE;
//                this.largeur = Planche.LIMITE;
//                this.distanceEntreSolives = 25;
//
//                Point positionSoliveOuestAttendue = new Point(0, 0, 0);
//                Solive soliveOuest = new Solive(
//                        positionSoliveOuestAttendue,
//                        Sens.VOIR_EPAISSEUR,
//                        PointCardinal.EST.getDirection(),
//                        this.longueur
//                );
//
//                Point positionSoliveEstAttendue = new Point(0, this.largeur - 2, this.longueur);
//                Solive soliveEst = new Solive(
//                        positionSoliveEstAttendue,
//                        Sens.VOIR_EPAISSEUR,
//                        PointCardinal.EST.getDirection(),
//                        this.longueur
//                );
//
//                double longueurPlancheNordSudAttendue = this.largeur - 4;
//
//                Point positionSoliveNordAttendue = new Point(0, 2, longueurPlancheNordSudAttendue);
//                Solive soliveNord = new Solive(
//                        positionSoliveNordAttendue,
//                        Sens.VOIR_EPAISSEUR,
//                        PointCardinal.NORD.getDirection(),
//                        longueurPlancheNordSudAttendue
//                );
//
//                Point positionSoliveSudAttendue = new Point(this.longueur - 2, 2, this.longueur - 2);
//                Solive soliveSud = new Solive(
//                        positionSoliveSudAttendue,
//                        Sens.VOIR_EPAISSEUR,
//                        PointCardinal.NORD.getDirection(),
//                        longueurPlancheNordSudAttendue
//                );
//
//                // x = 3
//                Point positionSoliveInterieurBloc1 = new Point(
//                        positionSoliveNordAttendue.getX() + this.distanceEntreSolives,
//                        positionSoliveNordAttendue.getY(),
//                        longueurPlancheNordSudAttendue);
//                // x = 6
//                Point positionSoliveInterieurBloc2 = new Point(
//                        positionSoliveNordAttendue.getX() + this.distanceEntreSolives * 2,
//                        positionSoliveNordAttendue.getY(),
//                        longueurPlancheNordSudAttendue);
//                // x = 9
//                Point positionSoliveInterieurBloc3 = new Point(
//                        positionSoliveNordAttendue.getX() + this.distanceEntreSolives * 3,
//                        positionSoliveNordAttendue.getY(),
//                        longueurPlancheNordSudAttendue);
//
//                Point[] positionAttendues = {
//                        positionSoliveOuestAttendue,
//                        positionSoliveEstAttendue,
//                        positionSoliveNordAttendue,
//                        positionSoliveSudAttendue,
//                        positionSoliveInterieurBloc1,
//                        positionSoliveInterieurBloc2,
//                        positionSoliveInterieurBloc3
//                };
//
//                this.structure = new Planche[][][]{{
//                        {
//                                soliveOuest,
//                                soliveEst,
//                                soliveNord,
//                                soliveSud,
//                                new Solive(
//                                        positionSoliveInterieurBloc1,
//                                        Sens.VOIR_EPAISSEUR,
//                                        PointCardinal.NORD.getDirection(),
//                                        longueurPlancheNordSudAttendue
//                                ),
//                                new Solive(
//                                        positionSoliveInterieurBloc2,
//                                        Sens.VOIR_EPAISSEUR,
//                                        PointCardinal.NORD.getDirection(),
//                                        longueurPlancheNordSudAttendue
//                                ),
//                                new Solive(
//                                        positionSoliveInterieurBloc3,
//                                        Sens.VOIR_EPAISSEUR,
//                                        PointCardinal.NORD.getDirection(),
//                                        longueurPlancheNordSudAttendue
//                                )
//                        }
//                }};
//
//                System.out.println(this.longueur);
//                System.out.println(this.largeur);
//                System.out.println(this.distanceEntreSolives);
//                System.out.println("___");
//
//                super.genererPlancher(this.longueur, this.largeur, this.distanceEntreSolives);
//
//                assertThat(super.plancher).isNotNull();
//
//                ArrayList<Planche> planches = super.plancher.getPlanches();
//                assertThat(planches).containsNoDuplicates();
//                assertThat(planches.size()).isEqualTo(positionAttendues.length);
//
//                for (int i = 0; i < planches.size(); i++) {
//                    // Vérifie la position
//                    assertThat(planches.get(i).getPosition()).isEqualTo(positionAttendues[i]);
//
//                    // Vérifie si la position est valide
//                    if (i == planches.size() - 1) {
//                        assertThat(planches.get(i).getPositionEstValide()).isFalse();
//                    } else {
//                        assertThat(planches.get(i).getPositionEstValide()).isTrue();
//                    }
//                }
//            }
//        }
//    }
//}