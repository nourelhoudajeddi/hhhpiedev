package tn.greenly.tests;

import tn.greenly.entites.Formation;
import tn.greenly.entites.Module;
import tn.greenly.services.FormationService;
import tn.greenly.services.ModuleService;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ModuleService ms = new ModuleService();
        FormationService fs = new FormationService();
        // Création d'un module avec ses attributs
        Module module = new Module();

        module.setNomModule("qb Web");
        module.setDescriptionModule("allahouma bi9owatika ya karim ya 9awiy ti5dem ya rabi");
        module.setNbHeures(40);
        module.setNiveau("Avancé");
        module.setCategorie("Informatique");
        module.setStatut(true);
        module.setDatecreationModule(new java.util.Date()); //Utilisation de la date actuelle

        Formation formation = new Formation();
       formation.setNomFormation("akll");
        formation.setDescriptionFormation("pieda");
        formation.setModeFormation("Distanciel");
        formation.setDureeFormation(4);
        formation.setDateDebutFormation(new java.util.Date());
        formation.setDateFinFormation(new java.util.Date());
        formation.setModule(module);
        System.out.println("Nom du module associé : " + formation.getModule().getNomModule());




        try {
            // Ajouter le module
            ms.ajouter(module);
            fs.ajouter(formation);

                List<Module> modules = ms.recuperer();
                for (Module mod : modules) {
                    System.out.println("Module ID: " + mod.getId()
                            + ", Nom: " + mod.getNomModule()
                            + ", Description: " + mod.getDescriptionModule()
                            + ", Nombre d'heures: " + mod.getNbHeures()
                            + ", Niveau: " + mod.getNiveau()
                            + ", Catégorie: " + mod.getCategorie()
                            + ", Statut: " + mod.isStatut() // Si le statut est un boolean, on utilise isStatut()
                            + ", Date de création: " + mod.getDatecreationModule());
                }


                List<Formation> formations = fs.recuperer();
            for (Formation f : formations) {
                System.out.println("Formation ID: " + f.getId()
                        + ", Nom: " + f.getNomFormation()
                        + ", Description: " + f.getDescriptionFormation()
                        + ", Mode: " + f.getModeFormation()
                        + ", Date début: " + f.getDateDebutFormation()
                        + ", Date fin: " + f.getDateFinFormation()
                      //  + ", Module ID: " + f.getModule_id());
                        + ", Module Nom: " + (f.getModule() != null ? f.getModule().getNomModule() : "N/A"));
                System.out.println("Nombre de formations : " + formations.size());  // Afficher la taille de la liste des formations

            }


            // Modifier un module (exemple de modification)
            Module moduleToModify = modules.get(10);


// Appel de la méthode avec la date convertie
            ms.modifier(
                    moduleToModify.getId(),
                    "Développement Web",
                    "Formation sur le développement web",
                    45,
                    "Avancé",
                    "Informatique",
                    true,
                    new java.util.Date()
            );

            Formation formationToModify = formations.get(10); // ou n'importe quel index valide

// Modifier le nom du module avant d'appeler la méthode
            formationToModify.getModule().setNomModule("Nouveau Nom du Module");

// Appel de la méthode en passant aussi le module
         /*  fs.modifierFormation(
                    formationToModify.getId(),
                    "la",
                    "francais",
                    5,
                    "Présentiel",
                    new java.util.Date(),
                    new java.util.Date(),
                    formationToModify.getModule() // on passe le module ici
            );*/




            modules = ms.recuperer(); // Récupérer à nouveau les modules après modification
            System.out.println("Modules après modification :");
            for (Module mod : modules) {
                System.out.println("Module ID: " + mod.getId()
                        + ", Nom: " + mod.getNomModule()
                        + ", Description: " + mod.getDescriptionModule()
                        + ", Nombre d'heures: " + mod.getNbHeures()
                        + ", Niveau: " + mod.getNiveau()
                        + ", Catégorie: " + mod.getCategorie()
                        + ", Statut: " + mod.isStatut() // Si le statut est un boolean, on utilise isStatut()
                        + ", Date de création: " + mod.getDatecreationModule());
            }

            // Afficher la liste des formations après modification
           // formations = fs.recuperer(); // Récupérer à nouveau les formations après modification
           /* System.out.println("Formations après modification :");
            for (Formation f : formations) {
                System.out.println("Formation ID: " + f.getId()
                        + ", Nom: " + f.getNomFormation()
                        + ", Description: " + f.getDescriptionFormation()
                        + ", Mode: " + f.getModeFormation()
                        + ", Date début: " + f.getDateDebutFormation()
                        + ", Date fin: " + f.getDateFinFormation()
                        + ", Module Nom: " + (f.getModule() != null ? f.getModule().getNomModule() : "N/A"));
            }*/

            // Supprimer un module (exemple de suppression)
           Module moduleToS= modules.get(14);
            ms.supprimer(moduleToS);// Suppression du module modifié

            //Formation formationToDelete = formations.get(1); // ou autre index valide
           // fs.supprimer(formationToDelete);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
