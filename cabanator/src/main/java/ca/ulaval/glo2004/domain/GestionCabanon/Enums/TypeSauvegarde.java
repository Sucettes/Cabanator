package ca.ulaval.glo2004.domain.GestionCabanon.Enums;

public enum TypeSauvegarde implements java.io.Serializable {
    NOUVELLE_VERSION_SAUVEGARDE("Nouvelle version de la sauvegarde"),
    NOUVEL_ENREGISTREMEMENT("Nouvel enregistrement"),
    ECRASER_SAUVEGARDE("Écraser la sauvegarde");

    private String nomAffichage;

    private TypeSauvegarde(String nomAffichage) {
        this.nomAffichage = nomAffichage;
    }

    @Override
    public String toString() {
        return this.nomAffichage;
    }
}