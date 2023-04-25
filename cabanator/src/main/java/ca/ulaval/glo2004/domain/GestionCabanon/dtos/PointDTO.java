package ca.ulaval.glo2004.domain.GestionCabanon.dtos;

import ca.ulaval.glo2004.domain.GestionCabanon.Factories.PointFactory;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.ValeurImperiale;

public class PointDTO {
    public ValeurImperiale x;
    public ValeurImperiale y;
    public ValeurImperiale z;

    protected PointDTO(Point point) {
        this.x = new ValeurImperiale(Math.abs(point.getX()));
        this.y = new ValeurImperiale(point.getY());
        this.z = new ValeurImperiale(point.getZ());
    }

    public PointDTO(double x, double y, double z) {
        this.x = new ValeurImperiale(x);
        this.y = new ValeurImperiale(y);
        this.z = new ValeurImperiale(z);
    }

    public Point FromDTO() {
        if (this.x.getDistanceDouble() > 0) {
            this.x.setDistanceDouble(-x.getDistanceDouble());
        }
        return PointFactory.FromDTO(this);
    }

    @Override
    public String toString() {
        return "(" + Math.abs(x.getDistanceDouble()) + ", " + y.getDistanceDouble() + ", " + z.getDistanceDouble() + ")";
    }
}
