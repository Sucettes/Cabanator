package ca.ulaval.glo2004.gui.Edition;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.ToitDTO;
import ca.ulaval.glo2004.gui.PropertiesWindow;

import static ca.ulaval.glo2004.gui.Tableau.ChampAngle.obtenirAngle;

public class EditionToit {
    private final PropertiesWindow propertiesWindow;

    public EditionToit(PropertiesWindow propertiesWindow) {
        this.propertiesWindow = propertiesWindow;
    }

    public Object[][] obtenirProprietesToit() {
        final ToitDTO toit = App.getControleurCabanon().obtenirConfigurationToit();

        return new Object[][]{
                {propertiesWindow.obtenirTitre("Angle"),
                        obtenirAngle(toit.angle)
                },
                {propertiesWindow.obtenirTitre("DistanceEntreFermes"),
                        toit.distanceEntreFermes.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("DistanceEntreFermesSupp"),
                        toit.distanceEntreFermesSupp.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("LongueurPorteAFaux"),
                        toit.longueurPorteAFaux.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("DistanceEntreEntremises"),
                        toit.distanceEntreEntremises.getDistanceString()
                }
        };
    }

    public enum FonctionnalitePanneauConfigToit {
        CONFIGURER_ANGLE,
        CONFIGURER_DISTANCE_ENTRE_FERMES,
        CONFIGURER_DISTANCE_ENTRE_FERMES_SUPP,
        CONFIGURER_DISTANCE_PORTE_A_FAUX,
        CONFIGURER_DISTANCE_ENTRE_ENTREMISES,
        CHANGER_VUE;

        public static FonctionnalitePanneauConfigToit obtenirFonctionnalite(int positionLigne) {
            return switch (positionLigne) {
                case 0 -> CONFIGURER_ANGLE;
                case 1 -> CONFIGURER_DISTANCE_ENTRE_FERMES;
                case 2 -> CONFIGURER_DISTANCE_ENTRE_FERMES_SUPP;
                case 3 -> CONFIGURER_DISTANCE_PORTE_A_FAUX;
                default -> CONFIGURER_DISTANCE_ENTRE_ENTREMISES;
            };
        }
    }
}