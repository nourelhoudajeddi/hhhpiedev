package tn.greenly.controllers;
import tn.greenly.entites.Module;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import tn.greenly.services.ModuleService;

import java.io.IOException;
import java.time.ZoneId;

public class ModifierModulerController {

    @FXML
    private TextField nomModuleField;

    @FXML
    private TextField descriptionModuleField;

    @FXML
    private TextField nbHeuresField;

    @FXML
    private ChoiceBox<String> niveauChoiceBox;

    @FXML
    private ComboBox<String> categorieComboBox;

    @FXML
    private CheckBox statutCheckBox;

    @FXML
    private DatePicker dateCreationPicker;

    @FXML
    private Button ajouterModuleButton;

    @FXML
    private Button btnuser;

    @FXML
    private Button btnmodule;

    @FXML
    private Button btnformation;

    @FXML
    private Button btnproduit;

    @FXML
    private Button btncommande;

    @FXML
    private Button btnsponsors;

    @FXML
    private Button btnevent;

    @FXML
    private Button btndon;

    @FXML
    private Button btnassoc;
    @FXML
    private int idModule;
    @FXML
    private Label nomModuleError;
    @FXML
    private Label descriptionModuleError;
    @FXML
    private Label nbHeuresError;
    @FXML
    private Label niveauError;
    @FXML
    private Label categorieError;
    @FXML
    private Label dateCreationError;


    @FXML
    private void initialize() {
        niveauChoiceBox.getItems().addAll("Débutant", "Intermédiaire", "Avancé");
        categorieComboBox.getItems().addAll("Meubles", "Verre", "Plastique", "Papier","Métaux","Vetements");
    }
    // Méthode appelée par le bouton "Modifier un module"

    @FXML
    void modifierModule(ActionEvent event) {
        String nom = nomModuleField.getText();
        String description = descriptionModuleField.getText();
        String nbHeuresStr = nbHeuresField.getText();
        String niveau = niveauChoiceBox.getValue();
        String categorie = categorieComboBox.getValue();
        boolean actif = statutCheckBox.isSelected();
        java.time.LocalDate localDate = dateCreationPicker.getValue();

        boolean isValid = true;

        // Réinitialiser les erreurs et styles
        nomModuleError.setVisible(false);
        descriptionModuleError.setVisible(false);
        nbHeuresError.setVisible(false);
        niveauError.setVisible(false);
        categorieError.setVisible(false);
        dateCreationError.setVisible(false);

        nomModuleField.setStyle("");
        descriptionModuleField.setStyle("");
        nbHeuresField.setStyle("");
        niveauChoiceBox.setStyle("");
        categorieComboBox.setStyle("");
        dateCreationPicker.setStyle("");

        if (nom.isEmpty()) {
            nomModuleError.setVisible(true);
            nomModuleField.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (description.isEmpty()) {
            descriptionModuleError.setVisible(true);
            descriptionModuleField.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (nbHeuresStr == null || nbHeuresStr.isEmpty()) {
            nbHeuresError.setText("Ce champ est requis");
            nbHeuresError.setVisible(true);
            nbHeuresField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            try {
                int nbHeures = Integer.parseInt(nbHeuresStr);
                if (nbHeures <= 0) {
                    nbHeuresError.setText("Le nombre d'heures doit être positif");
                    nbHeuresError.setVisible(true);
                    nbHeuresField.setStyle("-fx-border-color: red;");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                nbHeuresError.setText("Il faut mettre un nombre d'heures valide");
                nbHeuresError.setVisible(true);
                nbHeuresField.setStyle("-fx-border-color: red;");
                isValid = false;
            }
        }


        if (niveau == null) {
            niveauError.setVisible(true);
            niveauChoiceBox.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (categorie == null) {
            categorieError.setVisible(true);
            categorieComboBox.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (localDate == null) {
            dateCreationError.setText("Ce champ est requis");
            dateCreationError.setVisible(true);
            dateCreationPicker.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!isValid) {
            System.out.println("Veuillez corriger les erreurs.");
            return;
        }

        int nbHeures = Integer.parseInt(nbHeuresStr);
        java.util.Date date = java.sql.Date.valueOf(localDate);

        try {
            ModuleService service = new ModuleService();
            service.modifier(idModule, nom, description, nbHeures, niveau, categorie, actif, date);

            System.out.println("Module modifié avec succès !");
            goToAfficherModules();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isSameDay(java.util.Date date1, java.util.Date date2) {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
                cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
    }


    public void initData(Module module) {
        if (module != null) {
            idModule = module.getId(); // Initialiser l'ID du module
            nomModuleField.setText(module.getNomModule());
            descriptionModuleField.setText(module.getDescriptionModule());
            nbHeuresField.setText(String.valueOf(module.getNbHeures()));
            niveauChoiceBox.setValue(module.getNiveau());
            categorieComboBox.setValue(module.getCategorie());
            statutCheckBox.setSelected(module.isStatut());
            dateCreationPicker.setValue(module.getDatecreationModule().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
    }
    @FXML
    private void goToAfficherModules() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherModules.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnmodule.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToAfficherFormations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnformation.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
