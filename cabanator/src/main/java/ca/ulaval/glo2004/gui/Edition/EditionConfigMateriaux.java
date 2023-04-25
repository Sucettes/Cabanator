package ca.ulaval.glo2004.gui.Edition;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.CoutTypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.gui.PropertiesWindow;

import static ca.ulaval.glo2004.gui.Tableau.ChampArgent.obtenirArgent;

public class EditionConfigMateriaux {
    private final PropertiesWindow propertiesWindow;

    public EditionConfigMateriaux(PropertiesWindow propertiesWindow) {
        this.propertiesWindow = propertiesWindow;
    }

    public Object[][] obtenirProprietesConfigMateriaux() {
        return new Object[][]{
                {propertiesWindow.obtenirTitre("LongueurMax2x4"),
                        TypePlanche.getLimitePlanche(TypePlanche.P_2X4).getDistanceString()
                },
                {propertiesWindow.obtenirTitre("LongueurMax2x6"),
                        TypePlanche.getLimitePlanche(TypePlanche.P_2X6).getDistanceString()
                },
                {propertiesWindow.obtenirTitre("LongueurMax2x8"),
                        TypePlanche.getLimitePlanche(TypePlanche.P_2X8).getDistanceString()
                },
                {propertiesWindow.obtenirTitre("LongueurMax2x10"),
                        TypePlanche.getLimitePlanche(TypePlanche.P_2X10).getDistanceString()
                },
                {propertiesWindow.obtenirTitre("CoutPied2x4"),
                        obtenirArgent(CoutTypePlanche.getCoutPiedPlanche(TypePlanche.P_2X4))
                },
                {propertiesWindow.obtenirTitre("CoutPied2x6"),
                        obtenirArgent(CoutTypePlanche.getCoutPiedPlanche(TypePlanche.P_2X6))
                },
                {propertiesWindow.obtenirTitre("CoutPied2x8"),
                        obtenirArgent(CoutTypePlanche.getCoutPiedPlanche(TypePlanche.P_2X8))
                },
                {propertiesWindow.obtenirTitre("CoutPied2x10"),
                        obtenirArgent(CoutTypePlanche.getCoutPiedPlanche(TypePlanche.P_2X10))
                }
        };
    }

    public enum FonctionnalitePanneauConfigMateriaux {
        CONFIGURER_LONGUEUR_MAX_2X4,
        CONFIGURER_LONGUEUR_MAX_2X6,
        CONFIGURER_LONGUEUR_MAX_2X8,
        CONFIGURER_LONGUEUR_MAX_2X10,
        CONFIGURER_COUT_PIED_2X4,
        CONFIGURER_COUT_PIED_2X6,
        CONFIGURER_COUT_PIED_2X8,
        CONFIGURER_COUT_PIED_2X10;

        public static FonctionnalitePanneauConfigMateriaux obtenirFonctionnalite(int positionLigne) {
            return switch (positionLigne) {
                case 0 -> CONFIGURER_LONGUEUR_MAX_2X4;
                case 1 -> CONFIGURER_LONGUEUR_MAX_2X6;
                case 2 -> CONFIGURER_LONGUEUR_MAX_2X8;
                case 3 -> CONFIGURER_LONGUEUR_MAX_2X10;
                case 4 -> CONFIGURER_COUT_PIED_2X4;
                case 5 -> CONFIGURER_COUT_PIED_2X6;
                case 6 -> CONFIGURER_COUT_PIED_2X8;
                default -> CONFIGURER_COUT_PIED_2X10;
            };
        }
    }
}