package prisonvisitation1;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;

public class VisitorsManagement extends Application {

    private TableView<Visitor> table = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // ===== NAVBAR USING DashboardComponents =====
        HBox navbar = DashboardComponents.createNavbar(stage, "Officer Dashboard");

        // ===== SIDEBAR + MAIN CONTENT =====
        VBox sidebar = createSidebar(stage);
        VBox mainContent = createMainContent();
        HBox layout = new HBox(20, sidebar, mainContent);
        layout.setPadding(new Insets(20));

        // ===== ROOT CONTAINER =====
        BorderPane root = new BorderPane();
        root.setTop(navbar);
        root.setCenter(layout);

        // ===== SCENE SETUP =====
        Scene scene = new Scene(root, 1000, 800, Color.LIGHTGRAY);
        stage.setTitle("Visitors Management");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private VBox createMainContent() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(1200);
        box.setStyle("-fx-background-color: white; -fx-padding: 15px;");

        Label title = new Label("Visitors");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Define columns
        TableColumn<Visitor, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("visitorId"));

        TableColumn<Visitor, String> fnameCol = new TableColumn<>("First Name");
        fnameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Visitor, String> lnameCol = new TableColumn<>("Last Name");
        lnameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Visitor, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<Visitor, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Visitor, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        TableColumn<Visitor, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Visitor, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.setOnAction(event -> {
                    Visitor v = getTableView().getItems().get(getIndex());
                    showEditVisitorDialog(v);
                });

                deleteBtn.setOnAction(event -> {
                    Visitor v = getTableView().getItems().get(getIndex());
                    deleteVisitor(v.getVisitorId());
                    refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10, editBtn, deleteBtn);
                    setGraphic(box);
                }
            }
        });

        table.getColumns().addAll(idCol, fnameCol, lnameCol, genderCol, addressCol, contactCol, usernameCol, actionCol);
        refreshTable();

        box.getChildren().addAll(title, table);
        return box;
    }

    // Refresh table using stored procedure GetAllVisitors
    private void refreshTable() {
        ObservableList<Visitor> data = FXCollections.observableArrayList();
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL GetAllVisitors()}")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Visitor(
                        rs.getInt("visitor_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("contact_number"),
                        rs.getString("username")
                ));
            }
            table.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Show a dialog with all editable fields
    private void showEditVisitorDialog(Visitor v) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Visitor");
        dialog.setHeaderText("Edit Visitor ID: " + v.getVisitorId());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField lastNameField = new TextField(v.getLastName());
        TextField addressField = new TextField(v.getAddress());
        TextField contactField = new TextField(v.getContactNumber());
        TextField usernameField = new TextField(v.getUsername());

        grid.add(new Label("Last Name:"), 0, 0);
        grid.add(lastNameField, 1, 0);
        grid.add(new Label("Address:"), 0, 1);
        grid.add(addressField, 1, 1);
        grid.add(new Label("Contact:"), 0, 2);
        grid.add(contactField, 1, 2);
        grid.add(new Label("Username:"), 0, 3);
        grid.add(usernameField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                updateVisitor(v.getVisitorId(),
                        lastNameField.getText(),
                        addressField.getText(),
                        contactField.getText(),
                        usernameField.getText());
                refreshTable();
            }
        });
    }

    // Calls stored procedure UpdateVisitor
    private void updateVisitor(int visitorId, String lastName, String address, String contactNumber, String username) {
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL UpdateVisitor(?, ?, ?, ?, ?)}")) {

            stmt.setInt(1, visitorId);
            stmt.setString(2, lastName);
            stmt.setString(3, address);
            stmt.setString(4, contactNumber);
            stmt.setString(5, username);

            stmt.execute();
            System.out.println("Visitor updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

private void deleteVisitor(int id) {
    try (Connection conn = DBConnect.getConnection();
         CallableStatement stmt = conn.prepareCall("{CALL DeleteVisitor(?)}")) {
        
        // Make sure the ID is valid
        stmt.setInt(1, id);
        
        // Execute the stored procedure
        int rowsAffected = stmt.executeUpdate(); // Use executeUpdate() for DELETE operation
        
        if (rowsAffected > 0) {
            System.out.println("Visitor with ID " + id + " was successfully deleted.");
        } else {
            System.out.println("No visitor found with ID " + id);
        }
        
    } catch (SQLException e) {
        System.out.println("Error executing DELETE procedure: " + e.getMessage());
        e.printStackTrace();
    }
}

    private VBox createSidebar(Stage stage) {
       VBox sidebar = new VBox(10);
       sidebar.setPadding(new Insets(20));
       sidebar.setStyle("-fx-background-color: #2c3e50; -fx-pref-width: 300px; -fx-text-fill: white;");
       sidebar.setAlignment(Pos.TOP_CENTER);

       javafx.scene.control.Button viewRequestsBtn = new javafx.scene.control.Button("👁️ View Visit Requests");
       viewRequestsBtn.setPrefWidth(250);
       viewRequestsBtn.setPrefHeight(50);
       viewRequestsBtn.setOnAction(e -> {
           System.out.println("View Visit Requests Button Clicked");
           try {
               new VisitRequests().start(new Stage());
               stage.close();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
       });

       Button manageVisitorsBtn = new Button("👥 Manage Visitors");
       manageVisitorsBtn.setPrefWidth(250);
       manageVisitorsBtn.setPrefHeight(50);
       manageVisitorsBtn.setOnAction(e -> {
           System.out.println("Manage Visitors Button Clicked");
           try {
               new VisitorsManagement().start(new Stage());
               stage.close();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
       });

       Button manageInmatesBtn = new Button("🧍‍♂️ Manage Inmates");
       manageInmatesBtn.setPrefWidth(250);
       manageInmatesBtn.setPrefHeight(50);
       manageInmatesBtn.setOnAction(e -> {
           System.out.println("Manage Inmates Button Clicked");
              try {
               new InmateManagement().start(new Stage());
               stage.close();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
       });

       Button manageOfficersBtn = new Button("👮 Manage Officers");
       manageOfficersBtn.setPrefWidth(250);
       manageOfficersBtn.setPrefHeight(50);
       manageOfficersBtn.setOnAction(e -> {
           System.out.println("Manage Officers Button Clicked");
             try {
               new OfficerManagement().start(new Stage());
               stage.close();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
       });

       Button manageCellsBtn = new Button("🏢 Manage Cells");
       manageCellsBtn.setPrefWidth(250);
       manageCellsBtn.setPrefHeight(50);
       manageCellsBtn.setOnAction(e -> {
           System.out.println("Manage Cells Button Clicked");
             try {
               new CellManagement().start(new Stage());
               stage.close();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
       });

       Button reportsBtn = new Button("📄 Reports");
       reportsBtn.setPrefWidth(250);
       reportsBtn.setPrefHeight(50);
       reportsBtn.setOnAction(e -> {
           System.out.println("Reports Button Clicked");
             try {
               new VisitorsManagement().start(new Stage());
               stage.close();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
       });

       Button logoutBtn = new Button("🚪 Logout");
       logoutBtn.setPrefWidth(250);
       logoutBtn.setPrefHeight(50);
       logoutBtn.setOnAction(e -> {
           System.out.println("Logout Button Clicked");
            try {
               new LandingPage().start(new Stage());
               stage.close();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
       });

       // Set button styles
       setButtonStyles(viewRequestsBtn, manageVisitorsBtn, manageInmatesBtn, manageOfficersBtn, manageCellsBtn, reportsBtn, logoutBtn);

       // Add buttons to sidebar
       sidebar.getChildren().addAll(viewRequestsBtn, manageVisitorsBtn, manageInmatesBtn, 
                                    manageOfficersBtn, manageCellsBtn, reportsBtn, logoutBtn);

       return sidebar;
   }
    
        private void setButtonStyles(Button... buttons) {
        for (Button button : buttons) {
            button.setMinWidth(180);
            button.setStyle("-fx-font-size: 14px; -fx-background-color: #34495e; -fx-text-fill: white;");
        }
    }

    private void logout(Stage stage) {
        stage.close();
        System.out.println("Logging out...");
    }

    // Visitor class now includes address and username
    public static class Visitor {
        private final SimpleIntegerProperty visitorId;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty gender;
        private final SimpleStringProperty address;
        private final SimpleStringProperty contactNumber;
        private final SimpleStringProperty username;

        public Visitor(int id, String fname, String lname, String gender, String address, String contact, String username) {
            this.visitorId = new SimpleIntegerProperty(id);
            this.firstName = new SimpleStringProperty(fname);
            this.lastName = new SimpleStringProperty(lname);
            this.gender = new SimpleStringProperty(gender);
            this.address = new SimpleStringProperty(address);
            this.contactNumber = new SimpleStringProperty(contact);
            this.username = new SimpleStringProperty(username);
        }

        public int getVisitorId() { return visitorId.get(); }
        public String getFirstName() { return firstName.get(); }
        public String getLastName() { return lastName.get(); }
        public String getGender() { return gender.get(); }
        public String getAddress() { return address.get(); }
        public String getContactNumber() { return contactNumber.get(); }
        public String getUsername() { return username.get(); }
    }
}
