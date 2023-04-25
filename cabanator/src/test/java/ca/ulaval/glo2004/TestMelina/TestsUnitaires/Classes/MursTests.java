//package ca.ulaval.glo2004.TestMelina.TestsUnitaires.Classes;
//
//import ca.ulaval.glo2004.BaseTests;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Mur;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Murs;
//import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
//import org.junit.Test;
//
//import static com.google.common.truth.Truth.assertThat;
//
//public class MursTests extends BaseTests {
//    @Override
//    protected void Init() {
//
//    }
//
//    @Test
//    public final void Constructeur_DevraitInstancerListeMurs_NonNulle() {
//        Murs murs = new Murs(10, 11, 12);
//
//        assertThat(murs).isNotNull();
//    }
//
//    @Test
//    public final void Constructeur_DevraitInstancerListeMurs_AvecUnMurNonNulParPointCardinal() {
//        Murs murs = new Murs(10, 11, 12);
//
//        for (PointCardinal point : PointCardinal.values()) {
//            assertThat(murs.getMur(point)).isNotNull();
//        }
//    }
//
//    @Test
//    public final void Constructeur_DevraitContenir4Murs_CentresCorrectements() {
//        Murs murs = new Murs(10, 11, 12);
//
//
//        for (PointCardinal point : PointCardinal.values()) {
//            assertThat(murs.getMur(point)).isNotNull();
//        }
//    }
//
//    @Test
//    public final void TestSetDistanceEntreMontantsEtAutre() {
//        Murs murs = new Murs(10, 11, 12);
//        Mur mur = murs.getMur(PointCardinal.NORD);
//
//        mur.configurerDistanceEntreMontants(24);
//
//        assertThat(mur.getDistanceEntreMontants()).isEqualTo(24);
//
//        murs.configurerLongueur(200);
//    }
//}