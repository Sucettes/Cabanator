package ca.ulaval.glo2004.domain.GestionCabanon.Utilitaires;

import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.CabanonAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Affichage.PlancheAffichage;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Cabanon;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.CoutTypePlanche;
import ca.ulaval.glo2004.domain.GestionCabanon.Classes.Planches.Planche;
import ca.ulaval.glo2004.domain.GestionCabanon.Enums.TypeSauvegarde;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class GestionFichiersCabanon {
    public static final String SPLIT_CHAR_SEQUENCE_INFOS_PROJETS = "*";
    public static final String SPLIT_CHAR_SEQUENCE_NOM_SAUVEGARDE = "_";
    private static final String VERSION_PREFIXE = "V-";
    public static final String EXTENSION_FICHIER = ".cbntr";
    public static final String EXTENSION_CHEMIN_ACCESS = ".cbntracs";

    private static final String DOSSIER_CABANATOR = "Cabanator";
    private static String cheminAcces = System.getProperty("user.dir") + "\\" + GestionFichiersCabanon.DOSSIER_CABANATOR;

    public static String getCheminAccess() {
        return GestionFichiersCabanon.cheminAcces;
    }

    public static Object[] lireProjet(String cheminFichier) throws IOException, ClassNotFoundException {
        FileInputStream fichier = null;
        ObjectInputStream fichierEntre = null;

        try {
            fichier = new FileInputStream(cheminFichier);
            fichierEntre = new ObjectInputStream(fichier);

            Object[] cabanon = (Object[]) fichierEntre.readObject();
            fichierEntre.close();
            fichier.close();

            GestionFichiersCabanon.enregistrerChemin(cheminFichier);

            return cabanon;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("GestionFichiersCabanon.lireProjet");
            if (fichierEntre != null) {
                fichierEntre.close();
            }
            if (fichier != null) {
                fichier.close();
            }

            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("GestionFichiersCabanon.lireProjet");
            fichierEntre.close();
            fichier.close();

            throw e;
        }
    }

    public static boolean enregistrerProjet(
            String cheminFichier,
            String nomProjet,
            String versionSauvegarde,
            Cabanon cabanon,
            boolean renommerVersionsProjet,
            TypeSauvegarde typeSauvegarde,
            Stack<Cabanon> cabanonsPrecedents,
            Stack<Cabanon> cabanonsSuivants) throws IOException, ClassNotFoundException {
        Path path = FileSystems.getDefault().getPath(GestionFichiersCabanon.cheminAcces);
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) { // Crée le dossier contenant les fichiers de sauvegarde
            new File(GestionFichiersCabanon.cheminAcces).mkdir();
//            try {
//                Files.setAttribute(path, "dos:hidden", true); // dossier invisible
//            } catch (IOException e) {
//                System.out.println(e);
//            }
        }

        FileOutputStream fichier = null;
        ObjectOutputStream fichierSortie = null;
        String cheminSauvegarde = null;
        try {
            String cheminDossier;
            File fichierSauvegarde = new File(cheminFichier);
            if (!Objects.equals(cheminFichier, "")) {
                if (cheminFichier.contains(GestionFichiersCabanon.EXTENSION_FICHIER)) {
                    cheminDossier = fichierSauvegarde.getParent();
                } else {
                    cheminDossier = fichierSauvegarde.getAbsolutePath().replace(fichierSauvegarde.getName(), "");
                }
            } else {
                cheminDossier = GestionFichiersCabanon.cheminAcces;
            }

            cheminSauvegarde = cheminDossier
                    + "\\"
                    + GestionFichiersCabanon.obtenirNomFichier(nomProjet, versionSauvegarde, typeSauvegarde);

            fichier = new FileOutputStream(cheminSauvegarde, false);
            fichierSortie = new ObjectOutputStream(fichier);

            fichierSortie.writeObject(new Object[]{cabanon, cabanonsPrecedents, cabanonsSuivants});
            fichierSortie.close();
            fichier.close();

            GestionFichiersCabanon.enregistrerChemin(cheminFichier);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("GestionFichiersCabanon.creerSauvegarde");
            if (fichierSortie != null) {
                fichierSortie.close();
            }
            if (fichier != null) {
                fichier.close();
                Files.deleteIfExists(Path.of(cheminSauvegarde));
            }

            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("GestionFichiersCabanon.lireProjet");
            fichier.close();

            throw e;
        }

        return false;
    }

    public static boolean exporterProjet(String cheminFichier, Cabanon cabanon) throws IOException {
        FileWriter fichier = null;
        String cheminSauvegarde = null;
        try {
            if (Objects.equals(cheminFichier, "")) {
                cheminFichier = GestionFichiersCabanon.cheminAcces;
            }

            String nomProjet = Objects.equals(cabanon.getNom(), "") ? "exp_projet" : cabanon.getNom();
            cheminSauvegarde = cheminFichier + "\\"
                    + nomProjet + GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_NOM_SAUVEGARDE
                    + GestionFichiersCabanon.getDateToString(OffsetDateTime.now()).replaceAll(":", "_")
                    + ".csv";

            fichier = new FileWriter(cheminSauvegarde, false);

            fichier.write(String.join(";", new String[]{
                    "Type",
                    "Longueur",
                    "Coût",
                    "Position valide",
                    "Angle de coupe",
                    "\r"
            }));

            CabanonAffichage cabanonAffichage = new CabanonAffichage(cabanon);
            ArrayList<PlancheAffichage> pl = cabanonAffichage.getExportationplanches().stream()
                    .sorted((PlancheAffichage p1, PlancheAffichage p2) -> {
                        if (p1.getType().ordinal() < p2.getType().ordinal()) {
                            return -1;
                        } else if (p1.getType().ordinal() == p2.getType().ordinal()) {
                            return 0;
                        } else {
                             return 1;
                        }
                    }).sorted((p1, p2) -> {
                        double l1 = ValeurImperiale.convertirStringEnDouble(ValeurImperiale.convertirEnFraction(p1.getLongueur()));
                        double l2 = ValeurImperiale.convertirStringEnDouble(ValeurImperiale.convertirEnFraction(p2.getLongueur()));
                        return (l1 <= l2) ? (l1 == l2) ? 0 : 1 : -1;
                    }).collect(Collectors.toCollection(ArrayList::new));

            for (PlancheAffichage planche : pl) {
                System.out.println(String.join(";", new String[]{
                        planche.getType().toString(),
                        ValeurImperiale.convertirEnFraction(planche.getLongueur()),
                        String.format("%.2f$", (planche.getLongueur() * CoutTypePlanche.getCoutPiedPlanche(planche.getType()))),
                        (planche.getPositionEstValide() ? "vrai" : "faux"),
                        planche.getAngleDeCoupe().toString(),
                        "\r"
                }));
                fichier.write(String.join(";", new String[]{
                        planche.getType().toString(),
                        ValeurImperiale.convertirEnFraction(planche.getLongueur()),
                        String.format("%.2f$", (planche.getLongueur() * CoutTypePlanche.getCoutPiedPlanche(planche.getType()))),
                        (planche.getPositionEstValide() ? "vrai" : "faux"),
                        planche.getAngleDeCoupe().toString(),
                        "\r"
                }));
            }

            fichier.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("GestionFichiersCabanon.creerSauvegarde");
            if (fichier != null) {
                fichier.close();
                Files.deleteIfExists(Path.of(cheminSauvegarde));
            }

            throw e;
        }

        return false;
    }

    public static HashMap<String, ArrayList<String>> obtenirNomProjets() {
        File dossier = new File(GestionFichiersCabanon.cheminAcces);
        File[] fichiers = dossier.listFiles();
        HashMap<String, ArrayList<String>> nomsProjets = new HashMap<>();

        for (int i = 0; i < Objects.requireNonNull(fichiers).length; i++) {
            File fichier = fichiers[i];
            if (fichier.isFile()) {
                try {
                    Instant instantModification = Files.getLastModifiedTime(
                            FileSystems.getDefault().getPath(fichier.getAbsolutePath()),
                            LinkOption.NOFOLLOW_LINKS).toInstant();
                    if (fichier.getName().contains(GestionFichiersCabanon.EXTENSION_FICHIER)
                            && !fichier.getName().contains(GestionFichiersCabanon.EXTENSION_CHEMIN_ACCESS)) {
                        OffsetDateTime date = OffsetDateTime.ofInstant(instantModification, ZoneId.systemDefault());
                        String nomFichier = fichier.getName()
                                .replace(GestionFichiersCabanon.EXTENSION_FICHIER, "");
                        String[] partiesNomFichier = nomFichier.split(GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_NOM_SAUVEGARDE);
                        String nom = partiesNomFichier[0];
                        String version = GestionFichiersCabanon.obtenirVersionProjetFormatter(partiesNomFichier[1], TypeSauvegarde.ECRASER_SAUVEGARDE);

                        if (!nomsProjets.containsKey(nom)) {
                            ArrayList<String> infos = new ArrayList<>();

                            infos.add(nom + GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_INFOS_PROJETS
                                    + version + GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_INFOS_PROJETS
                                    + getDateToString(date) + GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_INFOS_PROJETS
                                    + fichier.getAbsolutePath());
                            nomsProjets.put(nom, infos);
                        } else {
                            nomsProjets.get(nom).add(nom + GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_INFOS_PROJETS
                                    + version + GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_INFOS_PROJETS
                                    + getDateToString(date) + GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_INFOS_PROJETS
                                    + fichier.getAbsolutePath());
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Map.Entry<String, ArrayList<String>> entry : nomsProjets.entrySet()) {
            entry.setValue(entry.getValue().stream().sorted().collect(Collectors.toCollection(ArrayList::new)));
        }

        return nomsProjets;
    }

    public static String obtenirVersionUlterieureProjet(String nomProjet, String versionProjet, TypeSauvegarde
            typeSauvegarde) {
        if (TypeSauvegarde.ECRASER_SAUVEGARDE == typeSauvegarde) {
            return GestionFichiersCabanon.obtenirVersionProjetFormatter(versionProjet, typeSauvegarde);
        }

        File dossier = new File(GestionFichiersCabanon.cheminAcces);
        File[] fichiers = dossier.listFiles();
        List<String> versions = new ArrayList<>();

        for (int i = 0; i < Objects.requireNonNull(fichiers).length; i++) {
            File fichier = fichiers[i];
            if (fichier.isFile()) {
                if (fichier.getName().contains(GestionFichiersCabanon.EXTENSION_FICHIER)
                        && !fichier.getName().contains(GestionFichiersCabanon.EXTENSION_CHEMIN_ACCESS)
                        && GestionFichiersCabanon.nomProjetEstFichier(fichier, nomProjet)) {
                    String nomFichier = fichier.getName().replace(GestionFichiersCabanon.EXTENSION_FICHIER, "");
                    String[] partiesNomFichier = nomFichier.split(GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_NOM_SAUVEGARDE);
                    String version = GestionFichiersCabanon.obtenirVersionProjetFormatter(partiesNomFichier[1], TypeSauvegarde.ECRASER_SAUVEGARDE);
                    versions.add(version);
                }
            }
        }

        versions.stream().filter(version -> {
            List<String> versionnageVersion = GestionFichiersCabanon.obtenirVersionnage(version);
            List<String> versionnageProjet = GestionFichiersCabanon.obtenirVersionnage(version);
            int indexVersionnage = GestionFichiersCabanon.obtenirIndexVersionnage(versionnageProjet, typeSauvegarde);
            return Integer.parseInt(versionnageVersion.get(indexVersionnage))
                    >= Integer.parseInt(versionnageProjet.get(indexVersionnage));
        });
        versions = versions.stream().sorted().toList();
        String versionUlterieure = "";
        if (versions.size() > 0) {
            versionUlterieure = versions.get(versions.size() - 1);
        }
        versionUlterieure = GestionFichiersCabanon.obtenirVersionProjetFormatter(versionUlterieure, typeSauvegarde);
        return versionUlterieure;
    }

    private static String obtenirVersionProjetFormatter(String versionCourante, TypeSauvegarde typeSauvegarde) {
        if (!versionCourante.contains(GestionFichiersCabanon.VERSION_PREFIXE)) {
            versionCourante = GestionFichiersCabanon.VERSION_PREFIXE + "1.0";
        }

        List<String> versionnage = GestionFichiersCabanon.obtenirVersionnage(versionCourante);
        int indexVersionnage = GestionFichiersCabanon.obtenirIndexVersionnage(versionnage, typeSauvegarde);
        int nbVersionsUlterieures = GestionFichiersCabanon.obtenirNbVersionsUlterieures(typeSauvegarde);
        versionnage.set(indexVersionnage, String.valueOf(Integer.parseInt(versionnage.get(indexVersionnage)) + nbVersionsUlterieures));

        if (TypeSauvegarde.ECRASER_SAUVEGARDE != typeSauvegarde) {
            versionnage = versionnage.subList(0, indexVersionnage + 1);
            versionnage.add("0");
        }

        return GestionFichiersCabanon.VERSION_PREFIXE + String.join(".", versionnage);
    }

    private static List<String> obtenirVersionnage(String version) {
        version = version.replace(GestionFichiersCabanon.VERSION_PREFIXE, "");
        return new ArrayList<>(stream(version.split("\\.")).filter(t -> t.length() > 0).toList());
    }

    private static int obtenirIndexVersionnage(List<String> versionnage, TypeSauvegarde typeSauvegarde) {
        if (TypeSauvegarde.NOUVELLE_VERSION_SAUVEGARDE == typeSauvegarde) {
            return versionnage.size() - 1; // dernier point
        } else if (TypeSauvegarde.NOUVEL_ENREGISTREMEMENT == typeSauvegarde) {
            return versionnage.size() - 2; // avant dernier point
        }

        return versionnage.size() - 1;
    }

    private static int obtenirNbVersionsUlterieures(TypeSauvegarde typeSauvegarde) {
        if (TypeSauvegarde.NOUVELLE_VERSION_SAUVEGARDE == typeSauvegarde) {
            return 1; // dernier point
        } else if (TypeSauvegarde.NOUVEL_ENREGISTREMEMENT == typeSauvegarde) {
            return 1; // avant dernier point
        }

        return 0;
    }

    //#region MÉTHODES PRIVÉES
    private static String getDateToString(OffsetDateTime date) {
        String temps = date.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM));
        temps = stream(temps
                .replaceAll("\\s", "")
                .split("[a-zA-Z]")).filter(t -> t.length() > 0)
                .collect(Collectors.joining(":"));

        return date.format(DateTimeFormatter.ISO_LOCAL_DATE) + "-" + temps;
    }

    private static String obtenirNomFichier(String nomProjet, String versionSauvegarde, TypeSauvegarde
            typeSauvegarde) {
        return nomProjet
                + GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_NOM_SAUVEGARDE
                + GestionFichiersCabanon.obtenirVersionProjetFormatter(versionSauvegarde, typeSauvegarde)
                + GestionFichiersCabanon.EXTENSION_FICHIER;
    }

    private static void enregistrerChemin(String cheminFichier) throws ClassNotFoundException, IOException {
        FileInputStream fichier = null;
        ObjectInputStream fichierEntre = null;
        FileOutputStream fichierS = null;
        ObjectOutputStream fichierSortie = null;
        try {

            String cheminAccess = GestionFichiersCabanon.getCheminAccess() + "\\" +
                    GestionFichiersCabanon.EXTENSION_CHEMIN_ACCESS;
            String[] cheminFichiers = new String[]{};
            if (new File(cheminAccess).exists()) {
                fichier = new FileInputStream(cheminAccess);
                fichierEntre = new ObjectInputStream(fichier);
                cheminFichiers = (String[]) fichierEntre.readObject();
                fichierEntre.close();
                fichier.close();

                List<String> cheminFichiersTemp = new ArrayList<>();

                for (String chemin : cheminFichiers) {
                    Path path = FileSystems.getDefault().getPath(chemin);
                    if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)
                            && !path.startsWith(GestionFichiersCabanon.getCheminAccess())) {
                        cheminFichiersTemp.add(chemin);
                    }
                }
                cheminFichiersTemp.add(cheminFichier);

                cheminFichiers = cheminFichiersTemp.toArray(new String[0]);
            } else {
                Path path = FileSystems.getDefault().getPath(cheminFichier);
                if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)
                        && !path.startsWith(GestionFichiersCabanon.getCheminAccess())) {
                    cheminFichiers = new String[]{cheminFichier};
                }
            }

            fichierS = new FileOutputStream(cheminAccess, false);
            fichierSortie = new ObjectOutputStream(fichierS);

            fichierSortie.writeObject(cheminFichiers);

            fichierSortie.close();
            fichierS.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("GestionFichiersCabanon.lireProjet");
            if (fichierEntre != null) {
                fichierEntre.close();
            }
            if (fichier != null) {
                fichier.close();
            }

            if (fichierSortie != null) {
                fichierSortie.close();
            }
            if (fichierS != null) {
                fichierS.close();
            }
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("GestionFichiersCabanon.lireProjet");
            fichierEntre.close();
            fichier.close();
            throw e;
        }
    }

    private static ArrayList<File> obtenirProjetsAvecCheminExterne(String nomCabanon) throws ClassNotFoundException, IOException {
        FileInputStream fichier = null;
        ObjectInputStream fichierEntre = null;
        try {
            String cheminAccess = GestionFichiersCabanon.getCheminAccess() + "\\" +
                    GestionFichiersCabanon.EXTENSION_CHEMIN_ACCESS;
            String[] cheminFichiers;
            ArrayList<File> fichiers = new ArrayList<>();
            if (new File(cheminAccess).exists()) {
                fichier = new FileInputStream(cheminAccess);
                fichierEntre = new ObjectInputStream(fichier);
                cheminFichiers = (String[]) fichierEntre.readObject();
                fichierEntre.close();
                fichier.close();


                for (String chemin : cheminFichiers) {
                    Path path = FileSystems.getDefault().getPath(chemin);
                    if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)
                            && GestionFichiersCabanon.nomProjetEstFichier(path, nomCabanon)) {
                        fichiers.add(path.toFile());
                    }
                }
            }
            return fichiers;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("GestionFichiersCabanon.lireProjet");
            if (fichierEntre != null) {
                fichierEntre.close();
            }
            fichier.close();
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("GestionFichiersCabanon.lireProjet");
            fichierEntre.close();
            fichier.close();
            throw e;
        }
    }

    private static ArrayList<File> obtenirProjets(String nomCabanon) {
        File dossier = new File(GestionFichiersCabanon.cheminAcces);
        File[] fichiers = dossier.listFiles();
        ArrayList<File> f = new ArrayList<>();

        for (int i = 0; i < Objects.requireNonNull(fichiers).length; i++) {
            File fichier = fichiers[i];
            if (fichier.isFile()) {
                if (fichier.getName().contains(GestionFichiersCabanon.EXTENSION_FICHIER)
                        && !fichier.getName().contains(GestionFichiersCabanon.EXTENSION_CHEMIN_ACCESS)
                        && GestionFichiersCabanon.nomProjetEstFichier(fichier, nomCabanon)) {
                    f.add(fichier);
                }
            }
        }

        return f;
    }

    private static void renommerVersionsProjet(String nomProjet, String nomCabanon, String versionSauvegarde, TypeSauvegarde typeSauvegarde)
            throws IOException, ClassNotFoundException {
        ArrayList<File> fichiers = GestionFichiersCabanon.obtenirProjetsAvecCheminExterne(nomCabanon);
        fichiers.addAll(GestionFichiersCabanon.obtenirProjets(nomCabanon));

        for (File fichier : fichiers) {
            String cheminSauvegarde = fichier.getParent();
            cheminSauvegarde = cheminSauvegarde
                    + "\\"
                    + GestionFichiersCabanon.obtenirNomFichier(nomProjet, versionSauvegarde, typeSauvegarde);
            File fichierRenomme = new File(cheminSauvegarde);
            if (fichierRenomme.createNewFile()) {
                fichier.renameTo(fichierRenomme);
            }
        }
    }

    private static boolean nomProjetEstFichier(File fichier, String nomProjet) {
        return GestionFichiersCabanon.nomProjetEstFichier(fichier.toPath(), nomProjet);
    }

    private static boolean nomProjetEstFichier(Path cheminfichier, String nomProjet) {
        String nomFichier = cheminfichier.getFileName().toString().replace(GestionFichiersCabanon.EXTENSION_FICHIER, "");
        String[] partiesNomFichier = nomFichier.split(GestionFichiersCabanon.SPLIT_CHAR_SEQUENCE_NOM_SAUVEGARDE);
        return Objects.equals(partiesNomFichier[0], nomProjet);
    }
    //#endregion
}