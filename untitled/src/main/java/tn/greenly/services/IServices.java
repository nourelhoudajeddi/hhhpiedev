package tn.greenly.services;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IServices<M> {
        void ajouter(M m) throws SQLException;
        void supprimer(M m)throws SQLException;
        void modifier(int id, String nomModule, String descriptionModule, int nbHeures, String niveau, String categorie, boolean statut, java.util.Date dateCreationModule) throws SQLException;
        // Modifier tous les attributs d'un module : idModule, nomModule, descriptionModule, nbHeures, niveau, categorie, statut, dateCreation
        List<M> recuperer() throws SQLException;
}