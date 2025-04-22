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
import tn.greenly.entites.Formation;
import tn.greenly.services.FormationService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherFormationsFront implements Initializable {

    @FXML
    private FlowPane formationContainer;

    private final FormationService formationService = new FormationService();
    @FXML
    private Button btnmodule;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Formation> formations = formationService.recuperer();
            for (Formation formation : formations) {
                VBox card = createFormationCard(formation);
                formationContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createFormationCard(Formation formation) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setPrefWidth(250);
        card.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label nomLabel = new Label("📘 " + formation.getNomFormation());
        nomLabel.setFont(new Font("Arial", 16));
        nomLabel.setTextFill(Color.web("#333"));

        Label descriptionLabel = new Label("📝 " + formation.getDescriptionFormation());
        descriptionLabel.setWrapText(true);

        Label dureeLabel = new Label("⏳ Durée : " + formation.getDureeFormation() + " h");
        Label modeLabel = new Label("📡 Mode : " + formation.getModeFormation());

        Label dateDebutLabel = new Label("📅 Début : " + formation.getDateDebutFormation());
        Label dateFinLabel = new Label("📅 Fin : " + formation.getDateFinFormation());

        card.getChildren().addAll(nomLabel, descriptionLabel, dureeLabel, modeLabel, dateDebutLabel, dateFinLabel);
        return card;
    }
    @FXML
    private void gotomodules() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherModulesFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnmodule.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
}}
