package ca.ulaval.glo2004;

import ca.ulaval.glo2004.domain.GestionCabanon.ControleurCabanon;
import ca.ulaval.glo2004.domain.GestionCabanon.Services.Interfaces.IServiceCabanon;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.gui.Navigateur;

import java.util.ServiceLoader;

public class App {
    private static ControleurCabanon controleurCabanon;

    public static void main(String[] args) {
        controleurCabanon = new ControleurCabanon(ServiceLoader.load(IServiceCabanon.class).findFirst().get());
        Navigateur.creerFrame();
        Navigateur.afficherFenetreAccueil();
    }

    public static ControleurCabanon getControleurCabanon() {
        return App.controleurCabanon;
    }

    public static void reinitialiserControleurCabanon(){
        App.controleurCabanon = new ControleurCabanon(ServiceLoader.load(IServiceCabanon.class).findFirst().get());
    }
}