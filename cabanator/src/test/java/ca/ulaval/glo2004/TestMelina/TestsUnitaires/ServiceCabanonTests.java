//package ca.ulaval.glo2004.TestMelina.TestsUnitaires;
//
//import ca.ulaval.glo2004.BaseTests;
//
//import static com.google.common.truth.Truth.assertThat;
//import static org.junit.Assert.*;
//
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Cabanon;
//import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Solive;
//import ca.ulaval.glo2004.domain.GestionCabanon.ControleurCabanon;
//import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
//import ca.ulaval.glo2004.domain.GestionCabanon.Factories.CabanonFactory;
//import ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces.IServiceCabanon;
//import ca.ulaval.glo2004.domain.GestionCabanon.Services.ServiceCabanon;
//import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Dimension3D;
//import org.junit.Test;
//
//import java.util.ServiceLoader;
//
//public class ServiceCabanonTests extends BaseTests {
//    private IServiceCabanon serviceCabanon;
//
//    @Override
//    protected void Init() {
//        serviceCabanon = ServiceLoader.load(IServiceCabanon.class).findFirst().get();
//        super.controleurCabanon = new ControleurCabanon(serviceCabanon);
//    }
//
//    @Test
//    public final void CreerProjet_DevraitCreerCabanonCorrectement() {
//        Dimension3D dimensionCabanon = new Dimension3D(10, 11, 12);
//        Dimension3D dimensionConvertie = dimensionCabanon.multiplier(12);
//        super.controleurCabanon.creerProjet(dimensionCabanon);
//
//        Cabanon cabanon = serviceCabanon.obtenirConfigurationCabanon();
//
//        assertThat(cabanon).isNotNull();
//        assertThat(dimensionConvertie).isEqualTo(cabanon.getDimensions());
//    }
//
//    @Test
//    public final void CreerProjet_DevraitRetournerCopieDeCabanon() {
//        Dimension3D dimensionCabanon = new Dimension3D(10, 11, 12);
//        super.controleurCabanon.creerProjet(dimensionCabanon);
//
//        Cabanon cabanon = serviceCabanon.obtenirConfigurationCabanon();
//
//        assertThat(cabanon).isInstanceOf(CabanonFactory.class);
//
//        assertThat(cabanon).isNotNull();
//    }
//}
