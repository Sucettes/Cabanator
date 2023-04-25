//package ca.ulaval.glo2004.TestMelina.TestsUnitaires;
//
//import ca.ulaval.glo2004.BaseTests;
//import ca.ulaval.glo2004.domain.GestionCabanon.ControleurCabanon;
//import ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces.IServiceCabanon;
//import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Dimension3D;
//import ca.ulaval.glo2004.domain.GestionCabanon.dtos.CabanonDTO;
//import org.junit.Test;
//
//import java.util.ServiceLoader;
//
//import static com.google.common.truth.Truth.assertThat;
//import static org.junit.Assert.assertNotNull;
//
//public class ControleurCabanonTests extends BaseTests {
//    @Override
//    protected void Init() {
//        super.controleurCabanon = new ControleurCabanon(ServiceLoader.load(IServiceCabanon.class).findFirst().get());
//    }
//
//    @Test
//    public void Constructeur_DevraitInstancierControleurCabanon_EtantNonNull() {
//        assertNotNull(controleurCabanon);
//    }
//
//    @Test
//    public final void CreerProjet_DevraitCreerCabanonCorrectement() {
//        super.controleurCabanon.creerProjet(new Dimension3D(10, 10, 10));
//
//        assertThat(super.controleurCabanon.obtenirConfigurationCabanon()).isInstanceOf(CabanonDTO.class);
//    }
//}
