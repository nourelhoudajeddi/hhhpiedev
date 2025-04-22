package tn.greenly.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.greenly.entites.Formation;
import tn.greenly.entites.Module;
import tn.greenly.services.FormationService;
import tn.greenly.services.ModuleService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AjouterFormationController {

    @FXML
    private TextField nomFormationField;

    @FXML
    private TextField descriptionFormationField;


    @FXML
    private TextField dureeField;

    @FXML
    private ChoiceBox<String> modeChoiceBox;

    @FXML
    private DatePicker dateCreationPicker;

    @FXML
    private DatePicker dateCreationPicker1;

    @FXML
    private ChoiceBox<String> nommoduleChoiceBox1;

    @FXML
    private Button ajouterFormationButton;

    @FXML
    private Button btnmodule;

    @FXML
    private Button ajouterFormationButton1;

    @FXML
    private Button btnformation;
    @FXML private Label nomFormationError;
    @FXML private Label descriptionFormationError;
    @FXML private Label dureeError;
    @FXML private Label modeError;
    @FXML private Label dateDebutError;
    @FXML private Label dateFinError;
    @FXML private Label moduleError;


    private final FormationService formationService = new FormationService();
    private final ModuleService moduleService = new ModuleService();

    private List<Module> modulesDisponibles;


    @FXML
    public void initialize() {
        modeChoiceBox.getItems().addAll("Présentiel", "Distanciel", "Hybride");

        try {
            modulesDisponibles = moduleService.recuperer();
            for (Module m : modulesDisponibles) {
                nommoduleChoiceBox1.getItems().add(m.getNomModule());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        nomFormationError.setVisible(false);
        descriptionFormationError.setVisible(false);
        dureeError.setVisible(false);
        modeError.setVisible(false);
        dateDebutError.setVisible(false);
        dateFinError.setVisible(false);
        moduleError.setVisible(false);
    }

    @FXML
    private void ajouterFormation() {
        try {
            // Récupération des données
            String nom = nomFormationField.getText();
            String description = descriptionFormationField.getText();
            String dureeStr = dureeField.getText();
            String mode = modeChoiceBox.getValue();
            LocalDate dateDebut = dateCreationPicker.getValue();
            LocalDate dateFin = dateCreationPicker1.getValue();

            // Validation des champs
            boolean isValid = true;

            if (nom.isEmpty()) {
                nomFormationError.setVisible(true);
                nomFormationField.setStyle("-fx-border-color: red;");
                isValid = false;
            } else {
                nomFormationError.setVisible(false);
                nomFormationField.setStyle("");
            }

            if (description.isEmpty()) {
                descriptionFormationError.setVisible(true);
                descriptionFormationField.setStyle("-fx-border-color: red;");
                isValid = false;
            } else {
                descriptionFormationError.setVisible(false);
                descriptionFormationField.setStyle("");
            }

            if (dateDebut == null) {
                dateDebutError.setText("La date de début est requise.");
                dateDebutError.setVisible(true);
                dateCreationPicker.setStyle("-fx-border-color: red;");
                isValid = false;
            }

            if (dateFin == null) {
                dateFinError.setText("La date de fin est requise.");
                dateFinError.setVisible(true);
                dateCreationPicker1.setStyle("-fx-border-color: red;");
                isValid = false;
            } else if (dateDebut != null && dateFin.isBefore(dateDebut)) {
                dateFinError.setText("La date de fin doit être après la date de début.");
                dateFinError.setVisible(true);
                dateCreationPicker1.setStyle("-fx-border-color: red;");
                isValid = false;
            } else {
                dateFinError.setVisible(false);
                dateCreationPicker1.setStyle(""); // optionnel : reset le style
            }


            if (dateDebut != null && dateFin != null && !dateDebut.isBefore(dateFin)) {
                dateDebutError.setText("La date de début doit être avant la date de fin.");
                dateDebutError.setVisible(true);
                dateCreationPicker.setStyle("-fx-border-color: red;");
                isValid = false;
            }



            if (mode == null) {
                modeError.setVisible(true);
                isValid = false;
            } else {
                modeError.setVisible(false);
            }


            String nomModuleChoisi = nommoduleChoiceBox1.getValue();
            if (nomModuleChoisi == null) {
                moduleError.setVisible(true);
                isValid = false;
            } else {
                moduleError.setVisible(false);
            }
            int duree = 0; // temporairement pour la portée
            if (dureeField.getText().isEmpty()) {
                dureeError.setText("La durée est requise.");
                dureeError.setVisible(true);
                dureeField.setStyle("-fx-border-color: red;");
                isValid = false;
            } else {
                try {
                    int valeur = Integer.parseInt(dureeField.getText());
                    if (valeur <= 0) {
                        dureeError.setText("La durée doit être supérieure à 0.");
                        dureeError.setVisible(true);
                        dureeField.setStyle("-fx-border-color: red;");
                        isValid = false;
                    } else {
                        duree = valeur;
                    }
                } catch (NumberFormatException e) {
                    dureeError.setText("La durée doit être un nombre.");
                    dureeError.setVisible(true);
                    dureeField.setStyle("-fx-border-color: red;");
                    isValid = false;
                }
            }

            // Validation spécifique de la date de fin
            if (dateDebut != null && dateFin != null && dateFin.isBefore(dateDebut)) {
                dateFinError.setText("La date de fin doit être après la date de début.");
                dateFinError.setVisible(true);
                isValid = false;
            } else {
                dateFinError.setVisible(false);
            }

            // Si tous les champs sont valides, procéder à l'ajout
            if (!isValid) {
                return;  // On arrête si une erreur est présente
            }

            // Trouver le module sélectionné
            Module moduleSelectionne = modulesDisponibles.stream()
                    .filter(m -> m.getNomModule().equals(nomModuleChoisi))
                    .findFirst()
                    .orElse(null);

            if (moduleSelectionne == null) {
                moduleError.setText("Le module sélectionné est introuvable.");
                moduleError.setVisible(true);
                return;
            }

            Date dateDebutFormation = Date.from(dateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dateFinFormation = Date.from(dateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Formation formation = new Formation(0, nom, description, Integer.parseInt(dureeStr), mode, dateDebutFormation, dateFinFormation, moduleSelectionne);

            // Appel du service pour ajouter la formation
            formationService.ajouter(formation);
            System.out.println("Formation ajoutée à la base de données avec succès.");

            // Redirection vers AfficherFormations.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ajouterFormationButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout de la formation.");
            e.printStackTrace(); // Affichage des erreurs SQL pour le débogage
        } catch (IOException e) {
            e.printStackTrace(); // Affichage des erreurs IO pour le débogage
        }
    }


    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    private void afficherformationsfront() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormationsFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ajouterFormationButton1.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
