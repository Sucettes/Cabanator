//package ca.ulaval.glo2004.TestMelina.TestsUnitaires.Classes;
//
//import ca.ulaval.glo2004.BaseTests;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Cabanon;
//import ca.ulaval.glo2004.domain.GestionCabanon.ControleurCabanon;
//import ca.ulaval.glo2004.domain.GestionCabanon.Factories.CabanonFactory;
//import ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces.IServiceCabanon;
//import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Dimension3D;
//import org.junit.Test;
//
//import java.util.ServiceLoader;
//
//import static com.google.common.truth.Truth.assertThat;
//
//public class CabanonTests extends BaseTests {
//    @Override
//    protected void Init() {
//    }
//
//    @Test
//    public final void Constructeur_DevraitCreerInstance_NonNulle() {
//        Dimension3D dimensionCabanon = new Dimension3D(200, 300, 400);
//        Cabanon cabanon = new Cabanon(dimensionCabanon);
//
//        assertThat(cabanon).isNotNull();
//    }
//
//    @Test
//    public final void GetDimensions_DevraitRetournerBonnesDimensions() {
//        Dimension3D dimensionCabanon = new Dimension3D(10, 11, 12);
//        Cabanon cabanon = new Cabanon(dimensionCabanon);
//
//
//        assertThat(cabanon.getDimensions()).isEqualTo(dimensionCabanon);
//    }
//}