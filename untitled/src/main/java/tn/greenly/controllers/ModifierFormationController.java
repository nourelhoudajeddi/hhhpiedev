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

public class ModifierFormationController {

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
    private Button btnformation;
    private Formation formationAModifier;

    private final FormationService formationService = new FormationService();
    private final ModuleService moduleService = new ModuleService();
    @FXML
    private Label nomFormationError;

    @FXML
    private Label descriptionFormationError;

    @FXML
    private Label dureeError;

    @FXML
    private Label modeError;

    @FXML
    private Label dateDebutError;

    @FXML
    private Label dateFinError;

    @FXML
    private Label moduleError;


    @FXML
    public void initialize() {
        // Remplir le choix des modes
        modeChoiceBox.getItems().addAll("Présentiel", "Distanciel", "Hybride");

        try {
            // Remplir les modules disponibles
            List<Module> modulesDisponibles = moduleService.recuperer();
            for (Module module : modulesDisponibles) {
                nommoduleChoiceBox1.getItems().add(module.getNomModule());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void modifierFormation() {
        // Réinitialiser les erreurs
        nomFormationError.setVisible(false);
        descriptionFormationError.setVisible(false);
        dureeError.setVisible(false);
        modeError.setVisible(false);
        dateDebutError.setVisible(false);
        dateFinError.setVisible(false);
        moduleError.setVisible(false);

        if (formationAModifier == null) {
            System.out.println("Aucune formation à modifier.");
            return;
        }

        boolean isValid = true;

        String nom = nomFormationField.getText();
        String description = descriptionFormationField.getText();
        String mode = modeChoiceBox.getValue();
        LocalDate dateDebut = dateCreationPicker.getValue();
        LocalDate dateFin = dateCreationPicker1.getValue();

        // Validation du nom
        if (nom.isEmpty()) {
            nomFormationError.setText("Le nom de la formation est requis.");
            nomFormationError.setVisible(true);
            nomFormationField.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        // Validation de la description
        if (description.isEmpty()) {
            descriptionFormationError.setText("La description est requise.");
            descriptionFormationError.setVisible(true);
            descriptionFormationField.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        // Validation de la durée
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

        // Validation du mode
        if (mode == null) {
            modeError.setText("Le mode est requis.");
            modeError.setVisible(true);
            modeChoiceBox.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        // Validation des dates
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
        }

        if (dateDebut != null && dateFin != null && !dateDebut.isBefore(dateFin)) {
            dateDebutError.setText("La date de début doit être avant la date de fin.");
            dateDebutError.setVisible(true);
            dateCreationPicker.setStyle("-fx-border-color: red;");
            isValid = false;
        }
        if (dateDebut != null && dateFin != null && !dateFin.isAfter(dateDebut)) {
            dateFinError.setText("La date de fin doit être apres la date de debut.");
            dateFinError.setVisible(true);
            dateCreationPicker1.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        // Validation du module
        String nomModuleChoisi = nommoduleChoiceBox1.getValue();
        if (nomModuleChoisi == null) {
            moduleError.setText("Le module est requis.");
            moduleError.setVisible(true);
            nommoduleChoiceBox1.setStyle("-fx-border-color: red;");
            isValid = false;
        }

        if (!isValid) {
            System.out.println("Veuillez remplir tous les champs correctement.");
            return;
        }

        // Conversion des dates
        Date dateDebutConverted = Date.from(dateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateFinConverted = Date.from(dateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Recherche du module sélectionné
        Module moduleSelectionne = null;
        try {
            List<Module> modules = moduleService.recuperer();
            for (Module m : modules) {
                if (m.getNomModule().equals(nomModuleChoisi)) {
                    moduleSelectionne = m;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (moduleSelectionne == null) {
            System.out.println("Module introuvable !");
            return;
        }

        // Mise à jour de la formation
        try {
            formationService.modifierFormation(
                    formationAModifier.getId(),
                    nom,
                    description,
                    duree,
                    mode,
                    dateDebutConverted,
                    dateFinConverted,
                    moduleSelectionne
            );
            System.out.println("Formation modifiée avec succès !");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) nomFormationField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée depuis le contrôleur parent pour passer la formation à modifier
    public void initData(Formation formation) {
        this.formationAModifier = formation;

        // Pré-remplissage des champs
        nomFormationField.setText(formation.getNomFormation());
        descriptionFormationField.setText(formation.getDescriptionFormation());
        dureeField.setText(String.valueOf(formation.getDureeFormation()));
        modeChoiceBox.setValue(formation.getModeFormation());

        // Conversion des dates
        LocalDate dateDebut = formation.getDateDebutFormation().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateFin = formation.getDateFinFormation().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        dateCreationPicker.setValue(dateDebut);
        dateCreationPicker1.setValue(dateFin);

        if (formation.getModule() != null) {
            nommoduleChoiceBox1.setValue(formation.getModule().getNomModule());
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
