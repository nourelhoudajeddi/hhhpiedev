package org.esprit.gui.back;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.esprit.models.Event;
import org.esprit.models.Attendee;
import org.esprit.models.Sponsor;
import org.esprit.services.EventService;
import org.esprit.services.AttendeeService;
import org.esprit.services.SponsorService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class EventManagementController implements Initializable {
    private EventService eventService;
    private Event event; // Add this field to store the current event

    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, Integer> idCol;
    @FXML private TableColumn<Event, String> nameCol;
    @FXML private TableColumn<Event, String> descCol;
    @FXML private TableColumn<Event, String> locCol;
    @FXML private TableColumn<Event, LocalDateTime> dateCol;
    @FXML private TableColumn<Event, String> statusCol;
    @FXML private TableColumn<Event, Void> actionsCol;

    @FXML private TableView<Attendee> attendeeTable;
    @FXML private TableColumn<Attendee, Integer> attendeeIdCol;
    @FXML private TableColumn<Attendee, String> attendeeNameCol;
    @FXML private TableColumn<Attendee, String> attendeeEmailCol;
    @FXML private TableColumn<Attendee, String> attendeePhoneCol; // Add this line
    @FXML private TableColumn<Attendee, String> attendeeEventCol; // Add this line
    @FXML private TableColumn<Attendee, Void> attendeeActionsCol;

    @FXML private TableView<Sponsor> sponsorTable;
    @FXML private TableColumn<Sponsor, Integer> sponsorIdCol;
    @FXML private TableColumn<Sponsor, String> sponsorNameCol;
    @FXML private TableColumn<Sponsor, String> sponsorContributionCol;
    @FXML private TableColumn<Sponsor, String> sponsorEventCol; // Add this line
    @FXML private TableColumn<Sponsor, Void> sponsorActionsCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventService = new EventService();

        // Set up event table columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("eventDescription"));
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Configure event actions column
        setupActionButtons();

        // Load initial event data
        refreshTable();

        // Set up attendee table
        setupAttendeeTable();

        // Set up sponsor table
        setupSponsorTable();
    }

    private void setupAttendeeTable() {
        attendeeTable.setItems(FXCollections.observableArrayList());

        attendeeIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        attendeeNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        attendeeEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        attendeePhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        attendeeEventCol.setCellValueFactory(cellData -> {
            Event event = cellData.getValue().getEvent();
            return new SimpleStringProperty(event != null ? event.getEventName() : "");
        }); // Add this line

        attendeeActionsCol.setCellFactory(param -> new TableCell<>() {
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

        refreshAttendeeTable();
    }

    private void setupSponsorTable() {
        sponsorTable.setItems(FXCollections.observableArrayList());

        sponsorIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        sponsorNameCol.setCellValueFactory(new PropertyValueFactory<>("sponsorName"));
        sponsorContributionCol.setCellValueFactory(new PropertyValueFactory<>("montant"));
        sponsorEventCol.setCellValueFactory(cellData -> {
            Event event = cellData.getValue().getEvent();
            return new SimpleStringProperty(event != null ? event.getEventName() : "");
        }); // Add this line

        sponsorActionsCol.setCellFactory(param -> new TableCell<>() {
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

        refreshSponsorTable();
    }

    private void handleEditAttendee(Attendee attendee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/AttendeeDialog.fxml"));
            DialogPane dialogPane = loader.load();
            AttendeeDialogController controller = loader.getController();
            controller.setAttendee(attendee);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Attendee");

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
                        // At this point, validation has passed
                        Attendee updatedAttendee = controller.getAttendee();
                        new AttendeeService().update(updatedAttendee);
                        refreshAttendeeTable();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Attendee updated successfully!");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update attendee: " + e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load attendee dialog: " + e.getMessage());
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
                    new AttendeeService().delete(attendee);
                    refreshAttendeeTable();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Attendee deleted successfully!");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete attendee: " + e.getMessage());
                }
            }
        });
    }

    private void handleEditSponsor(Sponsor sponsor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/SponsorDialog.fxml"));
            DialogPane dialogPane = loader.load();
            SponsorDialogController controller = loader.getController();
            controller.setSponsor(sponsor);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Sponsor");

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
                        new SponsorService().update(updatedSponsor);
                        refreshSponsorTable();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Sponsor updated successfully!");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update sponsor: " + e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load sponsor dialog: " + e.getMessage());
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
                    new SponsorService().delete(sponsor);
                    refreshSponsorTable();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Sponsor deleted successfully!");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete sponsor: " + e.getMessage());
                }
            }
        });
    }

    private void setupActionButtons() {
        actionsCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Event, Void> call(final TableColumn<Event, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button();
                    private final Button deleteButton = new Button();
                    private final Button attendeesButton = new Button("Attendees");
                    private final Button sponsorsButton = new Button("Sponsors");

                    {
                        // Load edit icon
                        Image editIcon = new Image(getClass().getResourceAsStream("/icons/edit.png"));
                        editButton.setGraphic(new ImageView(editIcon));
                        editButton.getStyleClass().add("icon-button");
                        editButton.setTooltip(new Tooltip("Edit Event"));

                        // Load delete icon
                        Image deleteIcon = new Image(getClass().getResourceAsStream("/icons/delete.png"));
                        deleteButton.setGraphic(new ImageView(deleteIcon));
                        deleteButton.getStyleClass().add("icon-button");
                        deleteButton.setTooltip(new Tooltip("Delete Event"));

                        // Set button actions
                        editButton.setOnAction(event -> {
                            Event eventData = getTableView().getItems().get(getIndex());
                            showEditEventDialog(eventData);
                        });

                        deleteButton.setOnAction(event -> {
                            Event eventData = getTableView().getItems().get(getIndex());
                            deleteEvent(eventData);
                        });

                        attendeesButton.setOnAction(event -> {
                            Event eventData = getTableView().getItems().get(getIndex());
                            new AttendeeListController(eventData).show();
                        });

                        sponsorsButton.setOnAction(event -> {
                            Event eventData = getTableView().getItems().get(getIndex());
                            new SponsorListController(eventData).show();
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox(5, editButton, deleteButton, attendeesButton, sponsorsButton);
                            buttons.setStyle("-fx-alignment: CENTER;");
                            setGraphic(buttons);
                        }
                    }
                };
            }
        });
    }    @FXML
    private void showAddEventDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/EventDialog.fxml"));
            DialogPane dialogPane = loader.load();
            EventDialogController controller = loader.getController();
            controller.setMode(EventDialogController.Mode.ADD);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add New Event");

            dialog.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    try {
                        Event newEvent = controller.getEvent();
                        eventService.add(newEvent);
                        refreshTable();
                        showAlert(AlertType.INFORMATION, "Success", "Event added successfully!");
                    } catch (Exception e) {
                        showAlert(AlertType.ERROR, "Error", "Failed to add event: " + e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to load dialog: " + e.getMessage());
        }
    }    private void showEditEventDialog(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/EventDialog.fxml"));
            DialogPane dialogPane = loader.load();
            EventDialogController controller = loader.getController();
            controller.setMode(EventDialogController.Mode.EDIT);
            controller.setEvent(event);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Event");

            dialog.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    try {
                        Event updatedEvent = controller.getEvent();
                        eventService.update(updatedEvent);
                        refreshTable();
                        showAlert(AlertType.INFORMATION, "Success", "Event updated successfully!");
                    } catch (Exception e) {
                        showAlert(AlertType.ERROR, "Error", "Failed to update event: " + e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to load dialog: " + e.getMessage());
        }
    }

    private void deleteEvent(Event event) {
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Event");
        confirmation.setContentText("Are you sure you want to delete this event?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    eventService.delete(event);
                    refreshTable();
                    showAlert(AlertType.INFORMATION, "Success", "Event deleted successfully!");
                } catch (Exception e) {
                    showAlert(AlertType.ERROR, "Error", "Failed to delete event: " + e.getMessage());
                }
            }
        });
    }

    private void refreshTable() {
        try {
            List<Event> events = eventService.getAll();
            eventTable.setItems(FXCollections.observableArrayList(events));
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Failed to load events: " + e.getMessage());
        }
    }

    private void refreshAttendeeTable() {
        try {
            AttendeeService attendeeService = new AttendeeService();
            List<Attendee> attendees = attendeeService.getAll();
            attendeeTable.setItems(FXCollections.observableArrayList(attendees));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load attendees: " + e.getMessage());
        }
    }

    private void refreshSponsorTable() {
        try {
            SponsorService sponsorService = new SponsorService();
            List<Sponsor> sponsors = sponsorService.getAll();
            sponsorTable.setItems(FXCollections.observableArrayList(sponsors));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load sponsors: " + e.getMessage());
        }
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void backToDashboard() {
        try {
            // Close current window
            Stage currentStage = (Stage) eventTable.getScene().getWindow();
            currentStage.close();

            // Open dashboard window
            Parent root = FXMLLoader.load(getClass().getResource("/DashboardWindow.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 400, 300);
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Event Management System - Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to return to dashboard: " + e.getMessage());
        }
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}