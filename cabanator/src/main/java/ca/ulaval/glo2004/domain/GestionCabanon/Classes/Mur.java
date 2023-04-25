package ca.ulaval.glo2004.domain.GestionCabanon.Classes;

import ca.ulaval.glo2004._3D.Vecteur;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Accessoire;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Fenetre;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Accessoires.Porte;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Entremise;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Montant;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Solive;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.PointCardinal;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.Sens;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Interfaces.IClonable;
import ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires.Point;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

public class Mur extends Contour implements IClonable<Mur> {
    protected static final TypePlanche T_PLANCHE_EST_OUEST = TypePlanche.P_2X4;
    private final Class<? extends Planche> CLASSE_PLANCHE_NORD_SUD = Montant.class;
    private final TypePlanche T_PLANCHE_NORD_SUD = TypePlanche.P_2X4;
    private final Sens SENS_PLANCHE_NORD_SUD = Sens.VOIR_EPAISSEUR;
    private final Class<? extends Planche> CLASSE_PLANCHE_EST_OUEST = Solive.class;
    private final Sens SENS_PLANCHE_EST_OUEST = Sens.VOIR_EPAISSEUR;

    //#region ATTRIBUTS

    private final UUID id = UUID.randomUUID();
    private final PointCardinal positionMur;
    private final PointCardinal pointCardinal;
    private double distanceEntreMontants;
    private ArrayList<Entremise> entremises;
    private ArrayList<Accessoire> accessoires;
    private Point centre;

    //#endregion

    //#region CONSTRUCTEURS

    /**
     * Constructeur pour DTO
     */
    public Mur(PointCardinal positionMur, PointCardinal pointCardinal, Point origine, double hauteur, double longueur,
               double distanceEntreMontants, ArrayList<Entremise> entremises, ArrayList<Accessoire> accessoires) {
        super(0, hauteur); //Dissociation de la classe Contour
        this.longueur = longueur;
        this.positionMur = positionMur;
        this.pointCardinal = pointCardinal;
        this.setOrigine(origine);
        this.setDistanceEntreMontants(distanceEntreMontants);
        this.setEntremises(entremises);
        this.setAccessoires(accessoires);
    }

    /**
     * Constructeur pour un mur existant
     */
    Mur(PointCardinal positionMur, PointCardinal pointCardinal, Point origine, double longueur, double hauteur,
        double distanceEntreMontants, ArrayList<Entremise> entremises, ArrayList<Accessoire> accessoires,
        ArrayList<ArrayList<Bloc>> structureInterne) {
        super(origine, longueur, hauteur, structureInterne); //Dissociation de la classe Contour

        this.positionMur = positionMur;
        this.pointCardinal = pointCardinal;
        this.setDistanceEntreMontants(distanceEntreMontants);
        this.setEntremises(entremises);
        this.setAccessoires(accessoires);
    }

    /**
     * Constructeur pour la création
     */
    public Mur(PointCardinal positionMur, PointCardinal pointCardinal, Point origine, double longueur, double hauteur) {
        super(origine, longueur, hauteur); //Dissociation de la classe Contour

        this.positionMur = positionMur;
        this.pointCardinal = pointCardinal;
        this.accessoires = new ArrayList<>();
        this.entremises = new ArrayList<>();

        this.configurerDistanceEntreMontants(30);
    }

    //#endregion

    //#region PROPRIÉTÉS ET INDEXEURS

    public UUID getId() {
        return this.id;
    }

    public double getHauteur() {
        return this.getLargeur();
    }

    public PointCardinal getPositionMur() {
        return this.positionMur;
    }

    public PointCardinal getPointCardinal() {
        return this.pointCardinal;
    }

    @Override
    public void setOrigine(Point origine) {
        super.setOrigine(origine);

        if (this.getStructureInterne() != null && !this.getStructureInterne().isEmpty()) {
            this.regenererStructure();
        }
    }

    private Point getCentre() {
        return this.centre;
    }

    private Point getNouveauCentre() {
        Point origine = this.getOrigine();
        this.centre = new Point(
                origine.getX() - (this.getLongueur() / 2),
                origine.getY() + (this.getHauteur() / 2),
                origine.getZ());
        return this.centre;
    }

    public double getDistanceEntreMontants() {
        return this.distanceEntreMontants;
    }

    private void setDistanceEntreMontants(double distance) {
        if (distance < 0) {
            throw new InvalidParameterException("distance est negative!");
        }
        this.distanceEntreMontants = distance;
    }

    public ArrayList<Entremise> getEntremises() {
        return this.entremises;
    }

    public void setEntremises(ArrayList<Entremise> entremises) {
        this.entremises = entremises;
    }

    public ArrayList<Accessoire> getAccessoires() {
        return accessoires;
    }

    public void setAccessoires(ArrayList<Accessoire> accessoires) {
        this.accessoires = accessoires;
    }

    //#endregion

    //#region MÉTHODES

    public Accessoire obtenirAccessoire(UUID id) {
        Optional<Accessoire> accessoire =
                this.accessoires.stream().filter(a -> a.getId() == id).findFirst();

        if (accessoire.isEmpty()) {
            throw new InvalidParameterException("L'accessoire est introuvable pour l'id : '" + id + "'");
        }

        return accessoire.get();
    }

    public Entremise obtenirEntremise(UUID id) {
        Optional<Entremise> entremise =
                this.entremises.stream().filter(a -> a.getId() == id).findFirst();

        if (entremise.isEmpty()) {
            throw new InvalidParameterException("L'accessoire est introuvable pour l'id : '" + id + "'");
        }

        return entremise.get();
    }

    public void configurerDistanceEntreMontants(double distance) {
        if (distance < 0) {
            throw new InvalidParameterException("distance est negative!");
        }
        this.setDistanceEntreMontants(distance);
        this.regenererStructure();
    }

    @Override
    protected void regenererStructure() {
        this.genererStructure(this.getLongueur(), this.getLargeur());
    }

    public void regenererStructureMur() {
        this.regenererStructure();
    }

    protected ArrayList<Bloc> genererLigneBlocs(double positionX, double positionY, double positionZ, double longueur, double hauteurRestante) {
        ArrayList<Bloc> ligneBlocs = new ArrayList<>();

        double positionXCourante = positionX;

        while (longueur > 0) {
            Point origineBloc = new Point(positionXCourante, positionY, positionZ);

            double longueurBlocCourant = TypePlanche.getLimitePlanche(this.T_PLANCHE_NORD_SUD).getDistanceDouble();
            if ((longueur - TypePlanche.getLimitePlanche(this.T_PLANCHE_NORD_SUD).getDistanceDouble()) < 0) {
                longueurBlocCourant = longueur;
            }

            // Obtention de la largeur intérieur du bloc
            double hauteurTotaleBloc = TypePlanche.getLimitePlanche(this.T_PLANCHE_NORD_SUD).getDistanceDouble() + (this.T_PLANCHE_EST_OUEST.getEpaisseur() * 2);
            if ((hauteurRestante - hauteurTotaleBloc < 0)) {
                hauteurTotaleBloc = hauteurRestante;
            }

            double hauteurInterieurBloc = hauteurTotaleBloc - this.T_PLANCHE_EST_OUEST.getEpaisseur() * 2;

            double positionYCourante = positionY;

            ArrayList<Planche> blocPlanches = new ArrayList<>(this.genererContourBloc(
                    positionXCourante,
                    positionYCourante,
                    positionZ,
                    longueurBlocCourant,
                    hauteurInterieurBloc));

            Bloc blocCourant = new Bloc(blocPlanches, longueurBlocCourant, hauteurTotaleBloc, origineBloc);

            positionXCourante = blocCourant.getOrigine().getX() - blocCourant.getLongueur(); //Maybe un bug ici v (+= / -=)
            longueur -= longueurBlocCourant;

            ligneBlocs.add(blocCourant);
        }

        return ligneBlocs;
    }

    private ArrayList<Planche> genererContourBloc(double pX, double pY, double pZ, double longueur, double hauteur) {
        ArrayList<Planche> contourBloc = new ArrayList<>();

        // Création de la planche à l'ouest
        Planche plancheBas = this.creerPlanche(
                this.CLASSE_PLANCHE_EST_OUEST,
                new Point(pX, pY, pZ),
                this.SENS_PLANCHE_EST_OUEST,
                new Vecteur(1, 0, 0),
                longueur);
        plancheBas.setType(TypePlanche.P_2X4);
        plancheBas.debug_name = "plancheBas";

        // Création de la planche au nord
        pY += plancheBas.getEpaisseur();

        Planche plancheGauche = this.creerPlanche(
                this.CLASSE_PLANCHE_NORD_SUD,
                new Point(pX, pY, pZ),
                this.SENS_PLANCHE_NORD_SUD,
//                    PointCardinal.NORD.getDirection(),
                new Vecteur(0, 1, 0),
                hauteur
        );
        plancheGauche.setType(T_PLANCHE_NORD_SUD);
        plancheGauche.debug_name = "plancheGauche";
        // Création de la planche à l'est
        pY += plancheGauche.getLongueur();

        Planche plancheHaut = this.creerPlanche(
                this.CLASSE_PLANCHE_EST_OUEST,
                new Point(pX, pY, pZ),
                this.SENS_PLANCHE_EST_OUEST,
//                    PointCardinal.EST.getDirection(),
                new Vecteur(1, 0, 0),
                longueur);
        plancheHaut.setType(TypePlanche.P_2X4);
        plancheHaut.debug_name = "plancheHaut";

        // Création de la planche au sud
        pX -= longueur - plancheGauche.getEpaisseur();
        Point pointPlancheSud = new Point(pX, plancheGauche.getPosition().getY(), pZ);

        Planche plancheDroite = this.creerPlanche(
                this.CLASSE_PLANCHE_NORD_SUD,
                pointPlancheSud,
                this.SENS_PLANCHE_NORD_SUD,
                new Vecteur(0, 1, 0),
                hauteur);
        plancheDroite.setType(T_PLANCHE_NORD_SUD);
        plancheDroite.debug_name = "plancheDroite";

        contourBloc.add(plancheBas);
        contourBloc.add(plancheHaut);
        contourBloc.add(plancheGauche);
        contourBloc.add(plancheDroite);

        return contourBloc;
    }

    ArrayList<Planche> genererInterieurBloc(Planche montantOriginel, double longueur, List<Accessoire> accessoires) {
        if (montantOriginel == null) {
            throw new InvalidParameterException("montantOriginel est null!");
        }
        if (longueur < 0) {
            throw new InvalidParameterException("longueur est negative!");
        }
        ArrayList<Planche> interieurBloc = new ArrayList<>();

        Point positionMontantOriginel = montantOriginel.getPosition();
        double epaisseurMontant = montantOriginel.getEpaisseur();

        double positionXPrecedente = positionMontantOriginel.getX() + epaisseurMontant;
        double positionXMax = (positionMontantOriginel.getX() + epaisseurMontant) - (longueur - epaisseurMontant * 2);
        double positionXDernierMontant = (positionMontantOriginel.getX() + epaisseurMontant) - (longueur - epaisseurMontant);
        double longueurRestante = longueur;
        boolean positionXValide;

        do {
            double positionXCourante = positionXPrecedente;
            double positionXSuivante = positionXCourante - this.getDistanceEntreMontants();

            // si vrai, mettre position suivante à positionXAccessoire - longueurAccessoire - epaisseurMontants * 2
            boolean accessoireContientPXSuivant = false;
            ArrayList<Accessoire> accessoiresTrouve = new ArrayList<>();
            for (Accessoire a : accessoires) {
                double pEnX = positionXSuivante - epaisseurMontant;
                double pXMin = a.getOrigine().getX() + epaisseurMontant * 2;
                double pXMax = a.getOrigine().getX() - a.getLongueurTotale(); // 0 - 20
                if (pEnX < pXMin && pEnX >= pXMax) {
                    accessoiresTrouve.add(a);
                    accessoireContientPXSuivant = true;
                }
            }

            positionXValide = positionXSuivante >= positionXMax;

            boolean ajouterPlanche = -positionXDernierMontant + positionXPrecedente > this.getDistanceEntreMontants()
                    && positionXSuivante >= positionXDernierMontant;

            if (positionXValide || (ajouterPlanche && positionXSuivante > positionXDernierMontant)) {
                positionXCourante = positionXSuivante;
            }

            if (ajouterPlanche && !accessoireContientPXSuivant) {
                Montant m = new Montant(
                        new Point(
                                positionXCourante - epaisseurMontant,
                                positionMontantOriginel.getY(),
                                positionMontantOriginel.getZ()),
                        this.SENS_PLANCHE_NORD_SUD,
                        montantOriginel.getVecteurDirection(),
                        montantOriginel.getLongueur(),
                        montantOriginel.getType(),
                        positionXValide
                );
                m.debug_name = "Montant intérieur";
                interieurBloc.add(m);
            }

            if (accessoiresTrouve.size() > 0) {
                Accessoire dernierAccessoire = accessoiresTrouve.get(accessoiresTrouve.size() - 1);
                positionXCourante = dernierAccessoire.getOrigine().getX() - dernierAccessoire.getLongueurTotale() + epaisseurMontant;
            }
            positionXPrecedente = positionXCourante;
            longueurRestante -= this.getDistanceEntreMontants();
        } while (longueurRestante > 0 && positionXValide);

        return interieurBloc;
    }

    @Override
    protected void regenererAccessoiresEtComposants() {
        System.out.println("=============");
        System.out.println("mur : " + this.getPositionMur());
        System.out.println("regenererAccessoiresEtComposants : " + this.getAccessoires().size());

        Point centre = this.getCentre();
        Point nouveauCentre = this.getNouveauCentre();
        boolean centreMurDiffere = centre != null && !centre.equals(nouveauCentre);
        double ratioPX = 1;
        double ratioPY = 1;

        if (centreMurDiffere) {
            double pXOrigine = this.getOrigine().getX();
            double pYOrigine = this.getOrigine().getY();
            double ancienPX = centre.getX() - pXOrigine;
            double nouveauPX = nouveauCentre.getX() - pXOrigine;
            ratioPX = nouveauPX / ancienPX;

            double ancienPY = centre.getY() - pYOrigine;
            double nouveauPY = nouveauCentre.getY() - pYOrigine;
            ratioPY = nouveauPY / ancienPY;
        }

        for (Accessoire accessoire : this.getAccessoires()) {
            int index = trouverIndexLigneBloc(accessoire.getCentre(), true);
            if (index != -1) {
                Bloc bloc = this.getStructureInterne().get(index).get(0);
                accessoire.setOrigineParent(bloc.getOrigine());
                accessoire.setHauteurParent(bloc.getHauteur());
            } else {
                accessoire.setOrigineParent(this.getOrigine().cloner());
                accessoire.setHauteurParent(accessoire.getHauteurTrou());
                accessoire.setPositionAccessoireEstValide(false);
            }

            // Permet d'éviter de recentrer pour rien
            if (centreMurDiffere) {
                accessoire.reconfigurerPosition(ratioPX, ratioPY);
            }

            this.validerPositionAccessoire(accessoire);
        }

        // Permet d'éviter de recentrer pour rien
        if (centreMurDiffere) {
            for(Entremise entremise : this.getEntremises()) {
                entremise.reconfigurerPosition(ratioPY);
            }
        }
    }

    //#region Porte

    @Override
    protected void genererStructure(double longueur, double hauteur) {
        if (longueur < 0) {
            throw new InvalidParameterException("longueur est negative!");
        }
        if (hauteur < 0) {
            throw new InvalidParameterException("hauteur est negative!");
        }

        if (longueur > 0) {
            this.genererContourBlocs(longueur, hauteur);
            this.regenererAccessoiresEtComposants();
            this.genererInterieurBlocs();
        }
    }

    private void genererContourBlocs(double longueur, double hauteur) {
        this.structureInterne = new ArrayList<>();

        Point pointOrigine = this.getOrigine();

        double positionXCourante = pointOrigine.getX();
        double positionYCourante = pointOrigine.getY();
        double positionZ = pointOrigine.getZ();

        double hauteurRestante = hauteur;

        while (hauteurRestante > 0) {
            ArrayList<Bloc> ligneBlocs = this.genererLigneBlocs(
                    positionXCourante,
                    positionYCourante,
                    positionZ,
                    longueur,
                    hauteurRestante);

            this.structureInterne.add(ligneBlocs);

            double hauteurBlocCourant = 0;

            try {
                hauteurBlocCourant = this.obtenirLargeurTotaleBloc(ligneBlocs.get(0));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            hauteurRestante -= hauteurBlocCourant;
            positionYCourante += hauteurBlocCourant;
        }
    }

    private void genererInterieurBlocs() {
        for (ArrayList<Bloc> ligneBlocs : this.getStructureInterne()) {
            for (Bloc bloc : ligneBlocs) {
                List<Accessoire> accessoiresOrdonnes = this.accessoires.stream()
                        .filter(accessoire -> { // Filtre tous ceux qui sont dans le bloc en Y
                            Vecteur directionEstOuest = new Vecteur(1, 0, 0);
                            Planche soliveOuest = bloc.getFirstPlanche(directionEstOuest);
                            Planche soliveEst = bloc.getFirstPlanche(planche ->
                                    planche.getVecteurDirection().equals(directionEstOuest) && planche != soliveOuest);
                            double maxBlocEnLargeur = soliveEst.getPosition().getY();
                            double minBlocEnLargeur = soliveOuest.getPosition().getY() + soliveOuest.getEpaisseur();
                            double pY = accessoire.getCentre().getY();

                            Vecteur directionNordSud = new Vecteur(0, 1, 0);
                            Planche sN = bloc.getFirstPlanche(directionNordSud);
                            Planche sS = bloc.getFirstPlanche(planche ->
                                    planche.getVecteurDirection().equals(directionNordSud) && planche != sN);
                            double minBlocEnLongueur = sN.getPosition().getX();
                            double maxBlocEnLongueur = sS.getPosition().getX() - this.T_PLANCHE_NORD_SUD.getEpaisseur();
                            double pX = accessoire.getCentre().getX();

                            return pY <= maxBlocEnLargeur && pY >= minBlocEnLargeur && pX <= minBlocEnLongueur && pX >= maxBlocEnLongueur;
                        })
                        .sorted(Comparator.comparingDouble((Accessoire accessoire) -> accessoire.getOrigine().getX()).reversed())
                        .toList();
                // Configure la position si invalide avant la génération de l'intérieur du bloc
                accessoiresOrdonnes.forEach(accessoire -> { // Ne pas mettre positionValide à true | positionValide peut être false
                    boolean pXValide = this.validerPositionXPourAccessoire(
                            bloc.getOrigine(),
                            accessoire.getOrigine(),
                            bloc.getLongueur(),
                            accessoire.getLongueurTotale());

                    if (pXValide) {
                        // Cherche un accessoire superposé dans la liste de tous les accessoires
                        boolean accessoireNonSuperpose = true;
                        Accessoire accessoireSuivant;
                        int i = accessoiresOrdonnes.indexOf(accessoire) + 1;

                        while (i < accessoiresOrdonnes.size() && accessoireNonSuperpose) {
                            accessoireSuivant = accessoiresOrdonnes.get(i);
                            boolean pXAccessoireCourantValide = this.validerPositionXPourAccessoire(
                                    bloc.getOrigine(),
                                    accessoireSuivant.getOrigine(),
                                    bloc.getLongueur(),
                                    accessoireSuivant.getLongueurTotale());
                            if (pXAccessoireCourantValide) {
                                accessoireNonSuperpose = !accessoire.contientPoint(
                                        accessoireSuivant.getOrigine(),
                                        accessoireSuivant.getLongueurTotale());
                                if (!accessoireNonSuperpose) {
//                                System.out.println("accessoireNonSuperpose GenererLigneBlocs " + accessoireSuivant.getClass().getSimpleName());
                                    accessoireSuivant.setPositionAccessoireEstValide(false);
                                }
                            }
                            i++;
                        }
                    } else if (accessoire.getPositionAccessoireEstValide()) {
//                    System.out.println("pXValide GenererLigneBlocs " + accessoire.getClass().getSimpleName());
                        accessoire.setPositionAccessoireEstValide(false);
                    }

                    if (!accessoire.getPositionAccessoireEstValide()) {
//                    System.out.println("pXInvalide = " + accessoire.getClass().getSimpleName());
                        accessoire.regenererStructure();
                    }
                });

                Vecteur directionEstOuest = new Vecteur(1, 0, 0);
                Planche soliveOuest = bloc.getFirstPlanche(directionEstOuest);

                ArrayList<Planche> planchesOuest = new ArrayList<>();
                planchesOuest.add(soliveOuest);

                int indexPlancheOuestGauche = 0;
                Planche firstPlanche = bloc.getFirstPlanche(new Vecteur(0, 1, 0));

                for (Accessoire accessoire : accessoiresOrdonnes) { //Ajoute les montants à gauche et à droite des accessoires
                    Point pMNord = new Point(
                            accessoire.getOrigine().getX() + this.T_PLANCHE_NORD_SUD.getEpaisseur(),
                            accessoire.getOrigineParent().getY() + this.T_PLANCHE_EST_OUEST.getEpaisseur(),
                            accessoire.getOrigine().getZ()
                    );
                    Planche mNord = this.creerPlanche(
                            this.CLASSE_PLANCHE_NORD_SUD,
                            pMNord,
                            this.SENS_PLANCHE_NORD_SUD,
                            new Vecteur(0, 1, 0),
                            firstPlanche.getLongueur()
                    );
                    mNord.setType(this.T_PLANCHE_NORD_SUD);
                    mNord.debug_name = "montant Nord accessoire";

                    Point pMSud = new Point(
                            accessoire.getOrigine().getX() - accessoire.getLongueurTotale(),
                            pMNord.getY(),
                            pMNord.getZ()
                    );
                    Planche mSud = this.creerPlanche(
                            this.CLASSE_PLANCHE_NORD_SUD,
                            pMSud,
                            this.SENS_PLANCHE_NORD_SUD,
                            new Vecteur(0, 1, 0),
                            firstPlanche.getLongueur()
                    );
                    mNord.setType(this.T_PLANCHE_NORD_SUD);
                    mNord.debug_name = "montant Sud accessoire";

                    boolean positionValide = accessoire.getPositionAccessoireEstValide();

                    if (positionValide) {
                        // Ajouter trou dans mur pour porte
                        if (accessoire instanceof Porte porte) {
                            Planche plancheOuestGauche = planchesOuest.get(indexPlancheOuestGauche);
                            double longueurPlancheOuestTotale = plancheOuestGauche.getLongueur();

                            double pXPlancheOuestGauche = plancheOuestGauche.getPosition().getX();
                            double pXAccessoire = porte.getOrigine().getX();

                            double longueurPlancheOuestGauche = pXPlancheOuestGauche - pXAccessoire;
                            plancheOuestGauche.setLongueur(longueurPlancheOuestGauche);

                            double longueurPorte = porte.getLongueurTotale();
                            double pXPlancheOuestDroite = pXAccessoire - longueurPorte;
                            double longueurPlancheOuestDroite = -((pXPlancheOuestGauche - longueurPlancheOuestTotale)
                                    - pXPlancheOuestDroite);
                            Point pPlancheOuestDroite = new Point(
                                    pXPlancheOuestDroite,
                                    plancheOuestGauche.getPosition().getY(),
                                    plancheOuestGauche.getPosition().getZ());

                            Planche plancheOuestDroite = this.creerPlanche(
                                    this.CLASSE_PLANCHE_EST_OUEST,
                                    pPlancheOuestDroite,
                                    this.SENS_PLANCHE_EST_OUEST,
                                    new Vecteur(1, 0, 0),
                                    longueurPlancheOuestDroite);
                            plancheOuestDroite.setType(TypePlanche.P_2X4);
                            plancheOuestDroite.debug_name = "plancheBas";

                            planchesOuest.add(plancheOuestDroite);
                            indexPlancheOuestGauche++;
                        }
                    } else {
                        mNord.setPositionEstValide(false);
                        mSud.setPositionEstValide(false);
                    }

                    bloc.ajouterPlanche(mNord);
                    bloc.ajouterPlanche(mSud);
                }

                if (planchesOuest.size() > 1) {
                    bloc.ajouterPlanches(planchesOuest.subList(1, planchesOuest.size()));
                }

                bloc.ajouterPlanches(genererInterieurBloc(
                        firstPlanche,
                        bloc.getLongueur(),
                        accessoiresOrdonnes));
            }
        }
    }

    @Override
    double obtenirLargeurTotaleBloc(Bloc bloc) {
        Planche plancheOuest = bloc.getFirstPlanche(new Vecteur(1, 0, 0));

        double largeurPlancheOuest = plancheOuest.getEpaisseur();

        double longueurPlancheNord = bloc
                .getFirstPlanche(new Vecteur(0, 1, 0))
                .getLongueur();

        return largeurPlancheOuest * 2 + longueurPlancheNord;
    }

    //#endregion

    @Override
    public ArrayList<Planche> getPlanches() {
        ArrayList<Planche> planches = super.getPlanches();

        planches.addAll(this.getEntremises());

        return planches;
    }

    @Override
    public Mur cloner() {
        Mur clone = new Mur(
                this.getPositionMur(),
                this.getPointCardinal(),
                this.getOrigine().cloner(),
                this.getLongueur(),
                this.getHauteur(),
                this.getDistanceEntreMontants(),
                this.getEntremises().stream()
                        .map(Entremise::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.getAccessoires().stream()
                        .map(Accessoire::cloner)
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.obtenirCloneStructureInterne());
        return clone;
    }

    //#endregion

    //#region METHODS

    public void ajouterEntremise(Point position) {
        if (position == null) {
            throw new InvalidParameterException("positionCurseur est null!");
        }


        if (position.getX() == 0 && position.getY() == 0 && position.getZ() == 0) {
            position.setX(this.getOrigine().getX() - (this.getLongueur() / 2));
            position.setY(this.getHauteur() < TypePlanche.getLimitePlanche(this.T_PLANCHE_NORD_SUD).getDistanceDouble() ? this.getHauteur() / 3 : 50);
        }

        //  obtenir la liste des montants en y ou l'entremise doit etre ajoute
        ArrayList<Planche> montants = new ArrayList<>();
        this.getStructureInterne().forEach(ligneBlocs ->
                ligneBlocs.forEach(bloc -> montants.addAll(bloc.getPlanches()
                        .stream()
                        .filter(p -> (p instanceof Montant)
                                && (position.getY() <= (p.getPosition().getY() + p.getLongueur()))
                                && (position.getY() >= (p.getPosition().getY())))
                        .sorted((p1, p2) -> p1.getPosition().getX() <= p2.getPosition().getX() ? 1 : -1)
                        .collect(Collectors.toCollection(ArrayList::new)))
                ));

        // trouve les 2 montants entre la quelle l'entremise doit etre ajoute
        if (montants.size() >= 2) {
            Optional<Montant> montantDroite = montants.stream()
                    .filter(m -> m.getPosition().getX() < position.getX() + m.getEpaisseur())
                    .map(Montant::new).findFirst();

            if (montantDroite.isPresent()) {
                int index = montants.indexOf(montantDroite.get());
                if (index > 0) {
                    Montant montantGauche = (Montant) montants.get(index - 1);
                    double longueur = montantGauche.getPosition().getX()
                            - montantDroite.get().getPosition().getX() - montantGauche.getEpaisseur();

                    position.setX(montantGauche.getPosition().getX() - montantGauche.getEpaisseur());
                    position.setZ(montantGauche.getPosition().getZ());
                    Entremise entremise = new Entremise(position, SENS_PLANCHE_EST_OUEST, new Vecteur(1, 0, 0), longueur);
                    this.entremises.add(entremise);
                }
            }
        }
    }

    public void positionnerEntremise(UUID id, Point positionCurseur) {
        if (id == null) {
            throw new InvalidParameterException("id est null!");
        }
        if (positionCurseur == null) {
            throw new InvalidParameterException("positionCurseur est null!");
        }
        Entremise entremise = this.entremises.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
        if (entremise != null) {
            int index = this.entremises.indexOf(entremise);
            this.entremises.get(index).setPosition(positionCurseur);
            this.entremises.get(index).setPositionEstValide(validerPositionEntremise(this.entremises.get(index)));
            this.regenererStructure();
        }
    }

    public void supprimerEntremise(UUID id) {
        if (id == null) {
            throw new InvalidParameterException("id est null!");
        }
        Entremise entremise = this.entremises.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
        if (entremise != null) {
            this.entremises.remove(entremise);
            this.regenererStructure();
        }
    }

    public void ajouterPorte(Point positionCurseur, double longueurTrou, double hauteurTrou) {
        if (positionCurseur == null) {
            throw new InvalidParameterException("positionCurseur est null!");
        }
        if (longueurTrou < 0) {
            throw new InvalidParameterException("longueurTrou est negative!");
        }
        if (hauteurTrou < 0) {
            throw new InvalidParameterException("hauteurTrou est negative!");
        }


        if (positionCurseur.getX() == 0 && positionCurseur.getY() == 0 && positionCurseur.getZ() == 0) {
            positionCurseur = this.getOrigine().cloner();
            positionCurseur.setX(this.getOrigine().getX() - (this.getLongueur() / 2));
            positionCurseur.setY(this.getHauteur() < TypePlanche.getLimitePlanche(this.T_PLANCHE_NORD_SUD).getDistanceDouble() ? this.getHauteur() / 3 : 50);
            longueurTrou = this.getLongueur() < 14 ? this.getLongueur() / 2 : 12;
            hauteurTrou = this.getHauteur() < 14 ? this.getHauteur() / 2 : 12;
        }

        // valider porte est dans la premiere rangee.
        int index = trouverIndexLigneBloc(positionCurseur, true);
        if (index == 0) {
            Bloc bloc = this.getStructureInterne().get(index).get(0);

            Porte porte = new Porte(
                    positionCurseur,
                    longueurTrou,
                    hauteurTrou,
                    bloc.getHauteur(),
                    this.getOrigine().cloner());

            this.accessoires.add(porte);
            this.regenererStructure();
        }
    }

    public void supprimerPorte(UUID id) {
        if (id == null) {
            throw new InvalidParameterException("id est null!");
        }
        this.accessoires.remove(this.obtenirAccessoire(id));
        this.regenererStructure();
    }

    public void ajouterFenetre(Point positionCurseur, double longueurTrou, double hauteurTrou) {
        if (positionCurseur == null) {
            throw new InvalidParameterException("positionCurseur est null!");
        }
        if (longueurTrou < 0) {
            throw new InvalidParameterException("longueurTrou est negative!");
        }
        if (hauteurTrou < 0) {
            throw new InvalidParameterException("hauteurTrou est negative!");
        }


        if (positionCurseur.getX() == 0 && positionCurseur.getY() == 0 && positionCurseur.getZ() == 0) {
            positionCurseur = this.getOrigine().cloner();
            positionCurseur.setX(this.getOrigine().getX() - (this.getLongueur() / 2));
            positionCurseur.setY(this.getHauteur() < TypePlanche.getLimitePlanche(this.T_PLANCHE_NORD_SUD).getDistanceDouble() ? this.getHauteur() / 3 : 50);
            longueurTrou = this.getLongueur() < 14 ? this.getLongueur() / 2 : 12;
            hauteurTrou = this.getHauteur() < 14 ? this.getHauteur() / 2 : 12;
        }

        int index = trouverIndexLigneBloc(positionCurseur, true);
        if (index != -1) {
            Bloc bloc = this.getStructureInterne().get(index).get(0);
            Fenetre fenetre = new Fenetre(
                    positionCurseur,
                    longueurTrou,
                    hauteurTrou,
                    bloc.getHauteur(),
                    this.getOrigine().cloner());

            this.accessoires.add(fenetre);

            this.regenererStructure();
        }
    }

    public void supprimerFenetre(UUID id) {
        if (id == null) {
            throw new InvalidParameterException("id est null!");
        }
        this.accessoires.remove(this.obtenirAccessoire(id));
        this.regenererStructure();
    }

    public void configurerTypeLinteauPorte(UUID id, TypePlanche type) {
        int index = this.accessoires.indexOf(this.obtenirAccessoire(id));
        if (this.accessoires.get(index) instanceof Porte porte) {
            porte.configurerTypeLinteau(type);
            this.regenererStructure();
        }
    }

    public void configurerTypeLinteauFenetre(UUID id, TypePlanche type) {
        int index = this.accessoires.indexOf(this.obtenirAccessoire(id));
        if (this.accessoires.get(index) instanceof Fenetre fenetre) {
            fenetre.configurerTypeLinteau(type);
            this.regenererStructure();
        }
    }

    public void configurerDistanceEntreMontantsPorte(UUID id, double distance) {
        int index = this.accessoires.indexOf(this.obtenirAccessoire(id));
        if (this.accessoires.get(index) instanceof Porte porte) {
            porte.configurerDistanceEntreMontants(distance);
            this.regenererStructure();
        }
    }

    public void configurerDistanceEntreMontantsFenetre(UUID id, double distance) {
        int index = this.accessoires.indexOf(this.obtenirAccessoire(id));
        if (this.accessoires.get(index) instanceof Fenetre fenetre) {
            fenetre.configurerDistanceEntreMontants(distance);
            this.regenererStructure();
        }
    }

    public void configurerDimensionsAccessoire(UUID id, double longueur, double largeur) {
        int index = this.accessoires.indexOf(this.obtenirAccessoire(id));
        this.accessoires.get(index).configurerLongueurTrou(longueur);
        this.accessoires.get(index).configurerHauteurTrou(largeur);
        this.regenererStructure();
    }

    public void positionnerPorte(UUID id, Point positionCurseur) {
        int index = this.accessoires.indexOf(this.obtenirAccessoire(id));
        if (this.accessoires.get(index) instanceof Porte porte) {
            porte.configurerPosition(positionCurseur);
            this.regenererStructure();
        } else {
            throw new InvalidParameterException("L'id : '" + id + "' ne correspond à aucun accessoire étant une fenêtre");
        }
    }

    public void positionnerFenetre(UUID id, Point positionCurseur) {
        int index = this.accessoires.indexOf(this.obtenirAccessoire(id));
        if (this.accessoires.get(index) instanceof Fenetre fenetre) {
            fenetre.configurerPosition(positionCurseur);
            this.regenererStructure();
        } else {
            throw new InvalidParameterException("L'id : '" + id + "' ne correspond à aucun accessoire étant une fenêtre");
        }
    }

    private void validerPositionAccessoire(Accessoire accessoire) {
        boolean pValideAvantValidation = accessoire.getPositionAccessoireEstValide();

        accessoire.regenererStructure(); // positionValide = true

        if (accessoire instanceof Porte porte) {
            this.validerPositionPorte(porte);
        }
        if (accessoire instanceof Fenetre fenetre) {
            this.validerPositionFenetre(fenetre);
        }

        boolean pVdalideApresValidation = accessoire.getPositionAccessoireEstValide();
        if ((!pValideAvantValidation && pVdalideApresValidation)
                || (pValideAvantValidation && !pVdalideApresValidation)) { // positionValide => false -> true
//            System.out.println("ValiderPositionAccessoire");
            accessoire.regenererStructure();
        }
    }

    private void validerPositionPorte(Porte porte) {
        boolean pXValide = this.validerPositionXDansMurPourAccessoire(porte.getOrigine(), porte.getLongueurTotale());
        boolean pYValide = this.getOrigine().getY() == porte.getOrigine().getY();

        porte.setPositionAccessoireEstValide(pXValide && pYValide);
    }

    private void validerPositionFenetre(Fenetre fenetre) {
        boolean positionValide = this.validerPositionYPourAccessoire(
                fenetre.getOrigine(),
                fenetre.getLongueurTotale(),
                fenetre.getStructureInterne().getHauteur());

        fenetre.setPositionAccessoireEstValide(positionValide);
    }

    private boolean validerPositionEntremise(Entremise entremise) {
        boolean positionValide = this.validerPositionYPourAccessoire(
                entremise.getPosition(),
                entremise.getLongueur(),
                entremise.getEpaisseur()) && this.validerPositionXDansMurPourAccessoire(entremise.getPosition(), entremise.getLongueur());

        entremise.setPositionEstValide(positionValide);
        return positionValide;
    }
    private boolean validerPositionXDansMurPourAccessoire(Point pOrigine, double longueur) {
        return this.validerPositionXPourAccessoire(
                this.getOrigine(),
                pOrigine,
                this.getLongueur(),
                longueur);
    }

    private boolean validerPositionXPourAccessoire(Point pOrigine, Point pOrigineAccessoire, double longueur, double longueurAccessoire) {
        double pXOrigine = pOrigineAccessoire.getX();
        double epaisseurMontants = T_PLANCHE_NORD_SUD.getEpaisseur() * 2;

        double pX = pOrigine.getX();
        double pXMax = pX - longueur;

        double pXOrigineMin = pX - epaisseurMontants;
        double pXOrigineMax = pXMax + epaisseurMontants + longueurAccessoire;

        boolean pXValide = pXOrigine <= pXOrigineMin && pXOrigine >= pXOrigineMax;

        return pXValide;
    }

    private boolean validerPositionYPourAccessoire(Point pOrigine, double longueur, double hauteur) {
        boolean pXValide = this.validerPositionXDansMurPourAccessoire(pOrigine, longueur);

        int indexBloc = this.trouverIndexLigneBloc(
                pOrigine,
                true);
        if (indexBloc < 0) {
            return false;
        }

        Bloc bloc = this.getStructureInterne().get(indexBloc).get(0);
        Vecteur direction = new Vecteur(1, 0, 0);
        double epaisseurPlancheOuest = bloc.getFirstPlanche(direction).getEpaisseur();
        double pYMinBloc = bloc.getOrigine().getY() + epaisseurPlancheOuest;
        double pYMaxBloc = pYMinBloc + bloc.getHauteur() - epaisseurPlancheOuest - hauteur;
        double pY = pOrigine.getY();

        return pXValide && pY >= pYMinBloc && pY <= pYMaxBloc;
    }


    //#endregion
}