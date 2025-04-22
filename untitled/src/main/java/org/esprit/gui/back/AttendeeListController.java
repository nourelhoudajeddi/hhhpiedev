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
import org.esprit.models.Attendee;
import org.esprit.models.Event;
import org.esprit.services.AttendeeService;

import java.io.IOException;

public class AttendeeListController {
    private final Event event;
    private final AttendeeService attendeeService;
    private final Stage dialogStage;

    @FXML private TableView<Attendee> attendeeTable;
    @FXML private TableColumn<Attendee, Void> actionsCol;

    public AttendeeListController(Event event) {
        this.event = event;
        this.attendeeService = new AttendeeService();
        this.dialogStage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/AttendeeListDialog.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 600, 400);
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

            dialogStage.setScene(scene);
            dialogStage.setTitle("Attendees for " + event.getEventName());
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            // 👇 Manually call the init logic
            loadAttendees();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML", e);
        }
    }

    @FXML
    private void initialize() {
        setupActionButtons();
        loadAttendees();
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
                editButton.setTooltip(new Tooltip("Edit Attendee"));

                // Load delete icon
                Image deleteIcon = new Image(getClass().getResourceAsStream("/icons/delete.png"));
                deleteButton.setGraphic(new ImageView(deleteIcon));
                deleteButton.getStyleClass().add("icon-button");
                deleteButton.setTooltip(new Tooltip("Delete Attendee"));

                buttonBox.setStyle("-fx-alignment: CENTER;");

                // Set button actions
                editButton.setOnAction(event -> {
                    Attendee attendee = getTableView().getItems().get(getIndex());
                    handleEditAttendee(attendee);
                });

                deleteButton.setOnAction(event -> {
                    Attendee attendee = getTableView().getItems().get(getIndex());
                    handleDeleteAttendee(attendee);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });
    }

    private void loadAttendees() {
        System.out.println("Loading attendees for event ID: " + event.getId()); // Debug

        try {
            var attendees = attendeeService.getByEventId(event.getId());
            if (attendees == null || attendees.isEmpty()) {
                System.out.println("No attendees found for event ID: " + event.getId());
            } else {
                System.out.println("Attendees fetched: " + attendees);
            }
            attendeeTable.setItems(FXCollections.observableArrayList(attendees));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to load attendees: " + e.getMessage());
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

    private void handleEditAttendee(Attendee attendee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/AttendeeDialog.fxml"));
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Attendee");

            // Set the attendee data in the dialog
            AttendeeDialogController controller = loader.getController();
            controller.setAttendee(attendee);

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
                        Attendee updatedAttendee = controller.getAttendee();
                        attendeeService.update(updatedAttendee);
                        loadAttendees();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Attendee updated successfully!");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update attendee: " + e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load dialog: " + e.getMessage());
        }
    }

    private void handleDeleteAttendee(Attendee attendee) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Attendee");
        confirmation.setContentText("Are you sure you want to delete this attendee?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    attendeeService.delete(attendee);
                    loadAttendees();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Attendee deleted successfully!");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete attendee: " + e.getMessage());
                }
            }
        });
    }
}