package ca.ulaval.glo2004.gui.Edition;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.PlancherDTO;
import ca.ulaval.glo2004.gui.Edition.SousEdition.EditionLigneEntremises;
import ca.ulaval.glo2004.gui.PropertiesWindow;

public class EditionPlancher {
    public final EditionLigneEntremises editionLigneEntremises;

    private final PropertiesWindow propertiesWindow;

    public EditionPlancher(PropertiesWindow propertiesWindow) {
        this.propertiesWindow = propertiesWindow;

        editionLigneEntremises = new EditionLigneEntremises(propertiesWindow);
    }

    public Object[][] obtenirProprietesPlancher() {
        final PlancherDTO plancher = App.getControleurCabanon().obtenirConfigurationPlancher();

        return new Object[][]{
                {propertiesWindow.obtenirTitre("Longueur"),
                        plancher.longueur.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("Largeur"),
                        plancher.largeur.getDistanceString()
                },
                {propertiesWindow.obtenirTitre("DistanceEntreSolives"),
                        plancher.distanceEntreSolives.getDistanceString()
                }
        };
    }

    public enum FonctionnalitePanneauConfigPlancher {
        CONFIGURER_LONGUEUR,
        CONFIGURER_LARGEUR,
        CONFIGURER_DISTANCE_ENTRE_SOLIVES;

        public static FonctionnalitePanneauConfigPlancher obtenirFonctionnalite(int positionLigne) {
            return switch (positionLigne) {
                case 0 -> CONFIGURER_LONGUEUR;
                case 1 -> CONFIGURER_LARGEUR;
                default -> CONFIGURER_DISTANCE_ENTRE_SOLIVES;
            };
        }
    }
}