package tn.greenly.services;
import tn.greenly.entites.Formation;
import tn.greenly.entites.Module;
import tn.greenly.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleService implements IServices<Module> {
    Connection cnx;

    public ModuleService() {
        cnx = MyDataBase.getInstance().getCnx();

    }

    @Override
    public void ajouter(Module module) throws SQLException {
        String sql = "INSERT INTO module(nom_module, description_module, nb_heures, niveau, categorie, statut, datecreation_module) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, module.getNomModule());
        ps.setString(2, module.getDescriptionModule());
        ps.setInt(3, module.getNbHeures());
        ps.setString(4, module.getNiveau());
        ps.setString(5, module.getCategorie());
        ps.setBoolean(6, module.isStatut());
        ps.setTimestamp(7, new Timestamp(module.getDatecreationModule().getTime()));

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            module.setId(rs.getInt(1)); // Très important pour garder la cohérence
        }

        System.out.println("Module ajouté avec ID: " + module.getId());
    }
    @Override
    public void supprimer(Module module) throws SQLException {
        // Étape 1 : Supprimer les formations associées au module
        String getFormationsQuery = "SELECT * FROM formation WHERE module_id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(getFormationsQuery)) {
            stmt.setInt(1, module.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Pour chaque formation associée, supprimer la formation
                int formationId = rs.getInt("id");
                String deleteFormationQuery = "DELETE FROM formation WHERE id = ?";
                try (PreparedStatement deleteStmt = cnx.prepareStatement(deleteFormationQuery)) {
                    deleteStmt.setInt(1, formationId);
                    deleteStmt.executeUpdate();
                    System.out.println("Formation supprimée : " + formationId);
                }
            }
            System.out.println("Formations associées supprimées.");
        }

        // Étape 2 : Supprimer le module
        String deleteModuleQuery = "DELETE FROM module WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(deleteModuleQuery)) {
            ps.setInt(1, module.getId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Module supprimé avec succès.");
            } else {
                System.out.println("Module introuvable.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du module : " + e.getMessage());
        }
    }

    @Override
    public void modifier(int id, String nomModule, String descriptionModule, int nbHeures, String niveau, String categorie, boolean statut, java.util.Date dateCreationModule) throws SQLException {
        // Vérifier si le module existe
        String moduleQuery = "SELECT id FROM module WHERE id = ?";
        PreparedStatement moduleStmt = cnx.prepareStatement(moduleQuery);
        moduleStmt.setInt(1, id);  // Vérification basée sur l'ID du module
        ResultSet moduleRs = moduleStmt.executeQuery();

        if (!moduleRs.next()) {
            throw new SQLException("Module non trouvé avec l'ID: " + id);
        }

        // Maintenant, on peut procéder à la modification du module
        String updateModule = "UPDATE module SET nom_module = ?, description_module = ?, nb_heures = ?, niveau = ?, categorie = ?, statut = ?, datecreation_module = ? WHERE id = ?";
        try (PreparedStatement updateStmt = cnx.prepareStatement(updateModule)) {
            updateStmt.setString(1, nomModule);
            updateStmt.setString(2, descriptionModule);
            updateStmt.setInt(3, nbHeures);
            updateStmt.setString(4, niveau);
            updateStmt.setString(5, categorie);
            updateStmt.setBoolean(6, statut);
            updateStmt.setTimestamp(7, new Timestamp(dateCreationModule.getTime()));
            updateStmt.setInt(8, id);  // ID du module à modifier
            updateStmt.executeUpdate();
        }
    }

    @Override
    public List<Module> recuperer() throws SQLException {
        String sql = "SELECT * FROM module";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<Module> modules = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String nomModule = rs.getString("nom_module");
            String descriptionModule = rs.getString("description_module");
            int nbHeures = rs.getInt("nb_heures");
            String niveau = rs.getString("niveau");
            String categorie = rs.getString("categorie");
            boolean statut = rs.getBoolean("statut");
            Timestamp dateCreation = rs.getTimestamp("datecreation_module");
            Module module = new Module();
            module.setId(id);
            module.setNomModule(nomModule);
            module.setDescriptionModule(descriptionModule);
            module.setNbHeures(nbHeures);
            module.setNiveau(niveau);
            module.setCategorie(categorie);
            module.setStatut(statut);
            module.setDatecreationModule(new java.util.Date(dateCreation.getTime()));
            modules.add(module);
        }
        return modules;
    }
}
