package ca.ulaval.glo2004.domain.GestionCabanon.Enums;

public enum PointCardinal implements java.io.Serializable {
    NORD("Nord", 0),
    EST("Est", 270),
    SUD("Sud", 180),
    OUEST("Ouest", 90);

    private final String nom;
    private final double direction;

    PointCardinal(String nom, double direction) {
        this.nom = nom;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return this.nom;
    }

    public double getDirection() {
        return this.direction;
    }
}