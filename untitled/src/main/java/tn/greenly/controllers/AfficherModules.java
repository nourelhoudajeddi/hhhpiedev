package tn.greenly.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.greenly.entites.Module;
import tn.greenly.services.ModuleService;

import java.sql.SQLException;
import java.util.List;
import java.io.IOException;


public class AfficherModules {

    @FXML
    private TableView<Module> moduleTable;

    @FXML
    private TableColumn<Module, Integer> colID;
    @FXML
    private TableColumn<Module, String> colNomModule;
    @FXML
    private TableColumn<Module, String> colDescription;
    @FXML
    private TableColumn<Module, Integer> colNbHeures;
    @FXML
    private TableColumn<Module, String> colNiveau;
    @FXML
    private TableColumn<Module, String> colCategorie;
    @FXML
    private TableColumn<Module, String> colStatut;
    @FXML
    private TableColumn<Module, String> colDateCreation;
    @FXML
    private TableColumn<Module, Void> colModifier;

    @FXML
    private TableColumn<Module, String> colSupprimer;


    @FXML
    private Button btnaj;
    @FXML
    private Button btnformation;


    private final ModuleService moduleService = new ModuleService();

    @FXML
    public void initialize() {
        // Association des colonnes aux attributs
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomModule.setCellValueFactory(new PropertyValueFactory<>("nomModule"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("descriptionModule"));
        colNbHeures.setCellValueFactory(new PropertyValueFactory<>("nbHeures"));
        colNiveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDateCreation.setCellValueFactory(new PropertyValueFactory<>("datecreationModule"));


        // Si vous avez besoin d'ajouter des actions comme Modifier et Supprimer, vous pouvez configurer les boutons ici.
        colModifier.setCellFactory(getModifierCellFactory());  // For the modify button

        colBtnSupprimer.setCellFactory(cellFactory);



        // Remplir la table
        loadModules();
    }

    private void loadModules() {
        try {
            List<Module> modules = moduleService.recuperer();
            ObservableList<Module> observableModules = FXCollections.observableArrayList(modules);
            moduleTable.setItems(observableModules);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleAjouterModule() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/AjouterModule.fxml"));
            javafx.scene.Parent root = loader.load();
            btnaj.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    TableColumn<Module, Void> colBtnSupprimer = new TableColumn<>("🗑️");

    Callback<TableColumn<Module, Void>, TableCell<Module, Void>> cellFactory = new Callback<>() {
        @Override
        public TableCell<Module, Void> call(final TableColumn<Module, Void> param) {
            return new TableCell<Module, Void>() {
                private final Button btn = new Button("Supprimer");

                {
                    btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
                    btn.setOnAction((event) -> {
                        Module module = getTableView().getItems().get(getIndex());

                        // Appelle ta méthode DAO pour supprimer de la base
                        try {
                            moduleService.supprimer(module);  // Ensure that moduleService has a method to delete the module
                            getTableView().getItems().remove(module);  // Remove the module from the table view
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Échec de la suppression du module.");
                            alert.showAndWait();
                        }
                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
        }
    };
    private Callback<TableColumn<Module, Void>, TableCell<Module, Void>> getModifierCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Module, Void> call(final TableColumn<Module, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Modifier");

                    {
                        btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
                        btn.setOnAction(event -> {
                            Module selectedModule = getTableView().getItems().get(getIndex());
                            ouvrirPageModifier(selectedModule);  // Call method to open the modify page
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        };
    }


    private void ouvrirPageModifier(Module module) {
        try {
            // Charger la nouvelle scène (page de modification)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierModule.fxml"));
            javafx.scene.Parent root = loader.load();

            // Passer le module à la page de modification
            ModifierModulerController controller = loader.getController();
            controller.initData(module);  // Initier les données du module à modifier

            // Ouvrir la nouvelle scène dans un nouveau stage
            Stage stage = (Stage) moduleTable.getScene().getWindow();
            Scene scene = new Scene(root);
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



};
