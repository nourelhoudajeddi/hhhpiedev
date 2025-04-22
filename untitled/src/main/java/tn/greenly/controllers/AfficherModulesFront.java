package tn.greenly.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import tn.greenly.entites.Module;
import tn.greenly.services.ModuleService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherModulesFront implements Initializable {

    @FXML
    private FlowPane moduleContainer;  // Correspond au fx:id de votre FlowPane dans FXML

    @FXML
    private Button btnformation;

    private final ModuleService moduleService = new ModuleService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Module> modules = moduleService.recuperer();
            for (Module module : modules) {
                VBox card = createModuleCard(module);
                moduleContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createModuleCard(Module module) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setPrefWidth(250);
        card.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label nomLabel = new Label("📘 " + module.getNomModule());
        nomLabel.setFont(new Font("Arial", 16));
        nomLabel.setTextFill(Color.web("#333"));

        Label descriptionLabel = new Label("📝 " + module.getDescriptionModule());
        descriptionLabel.setWrapText(true);

        Label nbHeuresLabel = new Label("⏳ Nombre d'heures : " + module.getNbHeures());
        Label niveauLabel = new Label("📊 Niveau : " + module.getNiveau());
        Label categorieLabel = new Label("📂 Catégorie : " + module.getCategorie());
        Label statutLabel = new Label("📅 Statut : " + module.isStatut());
        Label dateCreationLabel = new Label("🗓️ Création : " + module.getDatecreationModule());

        card.getChildren().addAll(nomLabel, descriptionLabel, nbHeuresLabel, niveauLabel, categorieLabel, statutLabel, dateCreationLabel);
        return card;
    }
    @FXML
    private void gotoformations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherFormationsFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnformation.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }}


