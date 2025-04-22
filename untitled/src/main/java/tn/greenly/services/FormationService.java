package tn.greenly.services;

import tn.greenly.entites.Formation;
import tn.greenly.entites.Module;
import tn.greenly.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public  class FormationService implements IServicesformation<Formation> {
    Connection cnx;

    public FormationService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Formation formation) throws SQLException {
        // Vérifier si le module existe déjà via son nom
        String moduleQuery = "SELECT id FROM module WHERE nom_module = ?";
        PreparedStatement moduleStmt = cnx.prepareStatement(moduleQuery);
        moduleStmt.setString(1, formation.getModule().getNomModule());
        ResultSet moduleRs = moduleStmt.executeQuery();

        int moduleId;

        if (moduleRs.next()) {
            // Le module existe, on récupère son ID
            moduleId = moduleRs.getInt("id");
        } else {
            // Le module n'existe pas, on l'insère
            String insertModule = "INSERT INTO module(nom_module) VALUES(?)";
            PreparedStatement insertModuleStmt = cnx.prepareStatement(insertModule, Statement.RETURN_GENERATED_KEYS);
            insertModuleStmt.setString(1, formation.getModule().getNomModule());
            insertModuleStmt.executeUpdate();
            ResultSet generatedKeys = insertModuleStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                moduleId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Échec de l'insertion du module, aucun ID généré.");
            }
        }

        // Ensuite, on insère la formation avec l’ID du module récupéré ou nouvellement créé
        String sql = "INSERT INTO formation(nom_formation, description_formation, duree_formation, mode_formation, datedebut_formation, datefin_formation, module_id) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, formation.getNomFormation());
        ps.setString(2, formation.getDescriptionFormation());
        ps.setInt(3, formation.getDureeFormation());
        ps.setString(4, formation.getModeFormation());
        ps.setTimestamp(5, new Timestamp(formation.getDateDebutFormation().getTime()));
        ps.setTimestamp(6, new Timestamp(formation.getDateFinFormation().getTime()));
        ps.setInt(7, moduleId);
        ps.executeUpdate();

        System.out.println("Formation ajoutée avec module : " + formation.getModule().getNomModule());
    }


    @Override
    public void supprimer(Formation formation) throws SQLException {
        String sql = "DELETE FROM formation WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, formation.getId());
        ps.executeUpdate();
        System.out.println("Formation supprimée");
    }

    @Override
    public void modifierFormation(int idFormation, String nomFormation, String descriptionFormation, int dureeFormation,
                                  String modeFormation, Date dateDebutFormation, Date dateFinFormation, Module module) throws SQLException {

        // Vérifier si le module existe
        String moduleQuery = "SELECT id FROM module WHERE nom_module = ?";
        PreparedStatement moduleStmt = cnx.prepareStatement(moduleQuery);
        moduleStmt.setString(1, module.getNomModule());
        ResultSet moduleRs = moduleStmt.executeQuery();

        int moduleId;

        if (moduleRs.next()) {
            // Le module existe déjà
            moduleId = moduleRs.getInt("id");
        } else {
            // Le module n'existe pas, on l'insère
            String insertModule = "INSERT INTO module(nom_module) VALUES(?)";
            PreparedStatement insertModuleStmt = cnx.prepareStatement(insertModule, Statement.RETURN_GENERATED_KEYS);
            insertModuleStmt.setString(1, module.getNomModule());
            insertModuleStmt.executeUpdate();
            ResultSet generatedKeys = insertModuleStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                moduleId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Échec de l'insertion du module.");
            }
        }

        // Modifier la formation avec le module associé
        String sql = "UPDATE formation SET nom_formation = ?, description_formation = ?, duree_formation = ?, " +
                "mode_formation = ?, datedebut_formation = ?, datefin_formation = ?, module_id = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, nomFormation);
        ps.setString(2, descriptionFormation);
        ps.setInt(3, dureeFormation);
        ps.setString(4, modeFormation);
        ps.setTimestamp(5, new Timestamp(dateDebutFormation.getTime()));
        ps.setTimestamp(6, new Timestamp(dateFinFormation.getTime()));
        ps.setInt(7, moduleId);
        ps.setInt(8, idFormation);
        ps.executeUpdate();

        System.out.println("Formation modifiée avec succès !");
    }





    @Override
    public List<Formation> recuperer() throws SQLException {
            String sql = "SELECT f.*, m.id as module_id, m.nom_module " +
                    "FROM formation f " +
                    "JOIN module m ON f.module_id = m.id";
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            List<Formation> formations = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomFormation = rs.getString("nom_formation");
                String descriptionFormation = rs.getString("description_formation");
                int dureeFormation = rs.getInt("duree_formation");
                String modeFormation = rs.getString("mode_formation");
                Timestamp dateDebutFormation = rs.getTimestamp("datedebut_formation");
                Timestamp dateFinFormation = rs.getTimestamp("datefin_formation");

                // Module data from the join
                int moduleId = rs.getInt("module_id");
                String nomModule = rs.getString("nom_module");

                Module module = new Module();
                module.setId(moduleId);
                module.setNomModule(nomModule);

                Formation formation = new Formation();
                formation.setId(id);
                formation.setNomFormation(nomFormation);
                formation.setDescriptionFormation(descriptionFormation);
                formation.setDureeFormation(dureeFormation);
                formation.setModeFormation(modeFormation);
                formation.setDateDebutFormation(new java.util.Date(dateDebutFormation.getTime()));
                formation.setDateFinFormation(new java.util.Date(dateFinFormation.getTime()));
                formation.setModule(module);

                formations.add(formation);
            }

            return formations;
        }
    public List<Formation> getFormationsParModule(Module module) throws SQLException {
        List<Formation> formations = new ArrayList<>();
        String query = "SELECT * FROM formation WHERE module_id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, module.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Formation formation = new Formation(
                        rs.getInt("id"),
                        rs.getString("nom_formation"),
                        rs.getString("description_formation"),
                        rs.getInt("duree_formation"),
                        rs.getString("mode_formation"),
                        rs.getDate("datedebut_formation"),
                        rs.getDate("datefin_formation"),
                        module // Associer le module à la formation
                );
                formations.add(formation);
            }
        }
        return formations;
    }

}