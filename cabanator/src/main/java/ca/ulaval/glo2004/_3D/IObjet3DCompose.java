package ca.ulaval.glo2004._3D;

public interface IObjet3DCompose {
    Objet3D getComposante(Polygon3D poly);

    boolean contient(Objet3D objet);
}
