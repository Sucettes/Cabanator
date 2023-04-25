package ca.ulaval.glo2004.Utilitaires;

public enum RessourceMap {
    ICONE_JTREE_LEAF_ICON("/icons/jtree_leaf_icon.png"),
    ICONE_JTREE_OPEN_ICON("/icons/jtree_open_icon.png");

    private String chemin;
    private RessourceMap(String chemin) {
        this.chemin = chemin;
    }

    public String obtenirChemin() {
        return this.chemin;
    }
}