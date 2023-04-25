package ca.ulaval.glo2004._3D;

public class Frustum {
    public Plan[] cotes;
    public Polygon3D[] cotesPoly;
    public Plan planProche;
    public Plan planEloigne;
    public Polygon3D planProchePoly;
    public Polygon3D planEloignePoly;

    public Frustum(Polygon3D[] polygon3DS, Polygon3D planProche, Polygon3D planEloigne){
        cotes = new Plan[polygon3DS.length];
        for(int i=0;i<polygon3DS.length;i++){
            cotes[i] = new Plan(polygon3DS[i]);
        }
        cotesPoly = polygon3DS;
        this.planProche = new Plan(planProche);
        this.planEloigne = new Plan(planEloigne);
        planProchePoly = planProche;
        planEloignePoly = planEloigne;
    }
}
