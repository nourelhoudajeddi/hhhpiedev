package tn.greenly.services;

import tn.greenly.entites.Module;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface IServicesformation<F> {
    void ajouter(F f) throws SQLException;
    void supprimer(F f)throws SQLException;
    List<F> recuperer() throws SQLException;
    void modifierFormation(int idFormation, String nomFormation, String descriptionFormation, int dureeFormation,
                                  String modeFormation, Date dateDebutFormation, Date dateFinFormation, Module module) throws SQLException;

}
