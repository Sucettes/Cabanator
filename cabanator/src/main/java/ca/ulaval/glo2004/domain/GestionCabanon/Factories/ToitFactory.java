package ca.ulaval.glo2004.domain.GestionCabanon.Factories;

import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Toit;

public class ToitFactory extends Toit {
    private ToitFactory(Toit toit) {
        super(
                toit.getAngle(),
                toit.getDistanceEntreFermes(),
                toit.getDistanceEntreFermesSupp(),
                toit.getLongueurPorteAFaux(),
                toit.getDistanceEntreEntremise()
        );
    }

    public static ToitFactory FromToit(Toit toit) {
        return new ToitFactory(toit);
    }
}