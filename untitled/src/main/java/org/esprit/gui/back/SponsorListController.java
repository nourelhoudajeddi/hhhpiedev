package org.esprit.gui.back;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.esprit.models.Sponsor;
import org.esprit.models.Event;
import org.esprit.services.SponsorService;

import java.io.IOException;

public class SponsorListController {
    private final Event event;
    private final SponsorService sponsorService;
    private final Stage dialogStage;

    @FXML private TableView<Sponsor> sponsorTable;
    @FXML private TableColumn<Sponsor, Void> actionsCol;

    public SponsorListController(Event event) {
        this.event = event;
        this.sponsorService = new SponsorService();
        this.dialogStage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/SponsorListDialog.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 400);
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

            dialogStage.setScene(scene);
            dialogStage.setTitle("Sponsors for " + event.getEventName());
            dialogStage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML", e);
        }
    }

    @FXML
    private void initialize() {
        setupActionButtons();
        loadSponsors();
    }

    private void setupActionButtons() {
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
            private final HBox buttonBox = new HBox(5, editButton, deleteButton);

            {
                // Load edit icon
                Image editIcon = new Image(getClass().getResourceAsStream("/icons/edit.png"));
                editButton.setGraphic(new ImageView(editIcon));
                editButton.getStyleClass().add("icon-button");
                editButton.setTooltip(new Tooltip("Edit Sponsor"));

                // Load delete icon
                Image deleteIcon = new Image(getClass().getResourceAsStream("/icons/delete.png"));
                deleteButton.setGraphic(new ImageView(deleteIcon));
                deleteButton.getStyleClass().add("icon-button");
                deleteButton.setTooltip(new Tooltip("Delete Sponsor"));

                buttonBox.setStyle("-fx-alignment: CENTER;");

                // Set button actions
                editButton.setOnAction(event -> {
                    Sponsor sponsor = getTableView().getItems().get(getIndex());
                    handleEditSponsor(sponsor);
                });

                deleteButton.setOnAction(event -> {
                    Sponsor sponsor = getTableView().getItems().get(getIndex());
                    handleDeleteSponsor(sponsor);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });
    }

    private void handleEditSponsor(Sponsor sponsor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/SponsorDialog.fxml"));
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Sponsor");

            // Set the sponsor data in the dialog
            SponsorDialogController controller = loader.getController();
            controller.setSponsor(sponsor);

            // Get the OK button from the dialog
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);

            // Override the default action of the OK button
            okButton.addEventFilter(ActionEvent.ACTION, event -> {
                // If validation fails, consume the event to prevent dialog from closing
                if (!controller.validateFields()) {
                    event.consume();
                }
            });

            dialog.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    try {
                        // At this point, validation has already passed
                        Sponsor updatedSponsor = controller.getSponsor();
                        sponsorService.update(updatedSponsor);
                        loadSponsors();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Sponsor updated successfully!");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update sponsor: " + e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load dialog: " + e.getMessage());
        }
    }

    private void handleDeleteSponsor(Sponsor sponsor) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Sponsor");
        confirmation.setContentText("Are you sure you want to delete this sponsor?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    sponsorService.delete(sponsor);
                    loadSponsors();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Sponsor deleted successfully!");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete sponsor: " + e.getMessage());
                }
            }
        });
    }

    private void loadSponsors() {
        try {
            sponsorTable.setItems(FXCollections.observableArrayList(
                    sponsorService.getByEventId(event.getId())
            ));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to load sponsors: " + e.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }

    public void show() {
        dialogStage.show();
    }

    public void showAndWait() {
        dialogStage.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}