package tn.greenly.controllers;
import tn.greenly.entites.Module;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.greenly.services.ModuleService;

import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.paint.Color;


public class AjouterModuleController {
    private final ModuleService moduleService = new ModuleService();

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
    private Button btnmodule;

    @FXML
    private Button btnformation;

    @FXML
    private Button ajouterModuleButton1;


    // Labels d'erreur
    @FXML private Label nomModuleError;
    @FXML private Label descriptionModuleError;
    @FXML private Label nbHeuresError;
    @FXML private Label niveauError;
    @FXML private Label categorieError;
    @FXML private Label dateCreationError;

    @FXML
    private void initialize() {
        niveauChoiceBox.getItems().addAll("Débutant", "Intermédiaire", "Avancé");
        categorieComboBox.getItems().addAll("Meubles", "Verre", "Plastique", "Papier", "Métaux", "Vetements");
    }

    @FXML
    private void ajouterModule() {
        // 1. Récupération des données
        String nom = nomModuleField.getText();
        String description = descriptionModuleField.getText();
        String nbHeures = nbHeuresField.getText();
        String niveau = niveauChoiceBox.getValue();
        String categorie = categorieComboBox.getValue();
        boolean statut = statutCheckBox.isSelected();
        java.util.Date dateCreation = dateCreationPicker.getValue() != null ? java.sql.Date.valueOf(dateCreationPicker.getValue()) : null;

        // 2. Contrôles de saisie
        boolean isValid = true;

        // Vérification des champs et gestion des messages d'erreur
        if (nom == null || nom.isEmpty()) {
            nomModuleError.setVisible(true);
            nomModuleField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            nomModuleError.setVisible(false);
            nomModuleField.setStyle("");
        }

        if (description == null || description.isEmpty()) {
            descriptionModuleError.setVisible(true);
            descriptionModuleField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            descriptionModuleError.setVisible(false);
            descriptionModuleField.setStyle("");
        }

        if (nbHeures == null || nbHeures.isEmpty()) {
            nbHeuresError.setText("Ce champ est requis");
            nbHeuresError.setVisible(true);
            nbHeuresField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            try {
                int heures = Integer.parseInt(nbHeures);
                if (heures <= 0) {
                    nbHeuresError.setText("Le nombre d'heures doit être positif");
                    nbHeuresError.setVisible(true);
                    nbHeuresField.setStyle("-fx-border-color: red;");
                    isValid = false;
                } else {
                    nbHeuresError.setVisible(false);
                    nbHeuresField.setStyle("");
                }
            } catch (NumberFormatException e) {
                nbHeuresError.setText("Il faut mettre un nombre d'heures");
                nbHeuresError.setVisible(true);
                nbHeuresField.setStyle("-fx-border-color: red;");
                isValid = false;
            }
        }



        if (niveau == null || niveau.isEmpty()) {
            niveauError.setVisible(true);
            niveauChoiceBox.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            niveauError.setVisible(false);
            niveauChoiceBox.setStyle("");
        }

        if (categorie == null || categorie.isEmpty()) {
            categorieError.setVisible(true);
            categorieComboBox.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            categorieError.setVisible(false);
            categorieComboBox.setStyle("");
        }

        // Vérification de la date de création : Si elle n'est pas aujourd'hui
        if (dateCreation == null) {
            dateCreationError.setText("La date de création est requise");
            dateCreationError.setVisible(true);
            dateCreationPicker.setStyle("-fx-border-color: red;");
            isValid = false;
        } else if (!isSameDay(dateCreation, new java.util.Date())) {
            dateCreationError.setText("La date de création est incorrecte");
            dateCreationError.setVisible(true);
            dateCreationPicker.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            dateCreationError.setVisible(false);
            dateCreationPicker.setStyle("");
        }

        // Si tous les champs sont valides, on ajoute le module
        if (isValid) {
            Module module = new Module();
            module.setNomModule(nom);
            module.setDescriptionModule(description);
            module.setNbHeures(Integer.parseInt(nbHeures));
            module.setNiveau(niveau);
            module.setCategorie(categorie);
            module.setStatut(statut);
            module.setDatecreationModule(dateCreation);

            try {
                moduleService.ajouter(module);  // Appel de la méthode ajouter dans le service
                System.out.println("Module ajouté à la base de données.");

                // Redirection vers l'affichage
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherModules.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ajouterModuleButton.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour vérifier si deux dates sont le même jour
    private boolean isSameDay(java.util.Date date1, java.util.Date date2) {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
                cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
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
    @FXML
    private void affichermodulesfront() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormationsFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ajouterModuleButton1.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

