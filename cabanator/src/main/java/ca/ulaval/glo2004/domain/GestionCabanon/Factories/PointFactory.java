package ca.ulaval.glo2004.domain.GestionCabanon.Factories;

import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;
import ca.ulaval.glo2004.domain.GestionCabanon.dtos.PointDTO;

public class PointFactory extends Point {

    public PointFactory(Point point) {
        super(point.getX(), point.getY(), point.getZ());
    }

    public static PointFactory FromPoint(Point point) {
        return new PointFactory(point);
    }

    public static Point FromDTO(PointDTO pointDTO) {
        return new Point(pointDTO.x.getDistanceDouble(), pointDTO.y.getDistanceDouble(), pointDTO.z.getDistanceDouble());
    }
}
