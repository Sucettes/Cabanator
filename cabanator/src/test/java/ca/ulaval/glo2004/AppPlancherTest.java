//package ca.ulaval.glo2004;
//
//import ca.ulaval.glo2004.domain.GestionCabanon.ControleurCabanon;
//import ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces.IServiceCabanon;
//import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Dimension3D;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.ServiceLoader;
//
//public class AppPlancherTest extends BaseTests {
//    private final double largeur = 100;
//    private final double longeur = 100;
//    private final double hauteur = 100;
//
//    @Override
//    protected void Init() {
//        IServiceCabanon serviceCabanon = ServiceLoader.load(IServiceCabanon.class).findFirst().get();
//
//        super.controleurCabanon = new ControleurCabanon(serviceCabanon);
//    }
//
//    @Test
//    public void testPlancher() {
//        super.controleurCabanon.creerProjet(new Dimension3D(this.largeur, this.longeur, this.hauteur));
//
//        super.controleurCabanon.obtenirConfigurationPlancher();
//    }
//
//    @Test
//    public void testObtenirNbPlanchesSelonDistance() {
//        super.controleurCabanon.creerProjet(new Dimension3D(this.largeur, this.longeur, this.hauteur));
//    }
//}
