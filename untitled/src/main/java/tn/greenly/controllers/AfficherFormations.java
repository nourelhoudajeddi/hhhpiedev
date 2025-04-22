package tn.greenly.controllers;

import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.greenly.entites.Formation;
import tn.greenly.services.FormationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherFormations {

    @FXML
    private TableView<Formation> moduleTable;

    @FXML
    private TableColumn<Formation, Integer> colID;
    @FXML
    private TableColumn<Formation, String> colNomFormation;
    @FXML
    private TableColumn<Formation, String> colDescription;
    @FXML
    private TableColumn<Formation, Integer> colDuree;
    @FXML
    private TableColumn<Formation, String> colMode;
    @FXML
    private TableColumn<Formation, String> colDateDebut;
    @FXML
    private TableColumn<Formation, String> colDateFin;
    @FXML
    private TableColumn<Formation, String> colNomModule;
    @FXML
    private TableColumn<Formation, Void> colBtnModifier;
    @FXML
    private TableColumn<Formation, Void> colBtnSupprimer;

    @FXML
    private Button btnajf;
    @FXML
    private Button btnmodule;
    @FXML
    private Button btnformation;

    private final FormationService formationService = new FormationService();

    @FXML
    public void initialize() {
        colID.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        colNomFormation.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nomFormation"));
        colDescription.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("descriptionFormation"));
        colDuree.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dureeFormation"));
        colMode.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("modeFormation"));
        colDateDebut.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateDebutFormation"));
        colDateFin.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateFinFormation"));
        colNomModule.setCellValueFactory(new PropertyValueFactory<>("nomModule"));

        addModifierButtonToTable();
        addSupprimerButtonToTable();

        loadFormations();
    }

    private void loadFormations() {
        try {
            List<Formation> formations = formationService.recuperer();
            ObservableList<Formation> observableFormations = FXCollections.observableArrayList(formations);
            moduleTable.setItems(observableFormations);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addModifierButtonToTable() {
        colBtnModifier.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
                btn.setOnAction(event -> {
                    Formation formation = getTableView().getItems().get(getIndex());
                    ouvrirPageModifier(formation); // Appel de la méthode qui ouvre la page de modification
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }
    private void ouvrirPageModifier(Formation formation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierFormation.fxml"));
            Parent root = loader.load();

            ModifierFormationController controller = loader.getController();
            controller.initData(formation); // envoie les données de la formation à modifier

            // Changer la scène
            Stage stage = (Stage) moduleTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void addSupprimerButtonToTable() {
        colBtnSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");

                btn.setOnAction(event -> {
                    Formation formation = getTableView().getItems().get(getIndex());
                    try {
                        formationService.supprimer(formation);
                        loadFormations(); // recharger après suppression
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    @FXML
    private void handleAjouterFormation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            Parent root = loader.load();
            btnajf.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
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
