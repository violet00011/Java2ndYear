package prisonvisitation1;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;

public class InmateManagement extends Application {

    private TableView<Inmate> table = new TableView<>();
    private ObservableList<Inmate> masterData = FXCollections.observableArrayList();
    private FilteredList<Inmate> filteredData;
    private TextField searchField;

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
        stage.setTitle("Inmate Management");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private VBox createMainContent() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(1200);
        box.setStyle("-fx-background-color: white; -fx-padding: 15px;");

        Label title = new Label("Inmates");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        Button addInmateBtn = new Button("➕ Add Inmate");
        addInmateBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        addInmateBtn.setOnAction(e -> showAddInmateDialog());


        // Search components
        HBox searchBox = createSearchBox();
        
        // Define columns
        TableColumn<Inmate, Integer> idCol = new TableColumn<>("Inmate ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("inmateId"));

        TableColumn<Inmate, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Inmate, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("inmateType"));

        TableColumn<Inmate, String> healthIssueCol = new TableColumn<>("Health Issues");
        healthIssueCol.setCellValueFactory(new PropertyValueFactory<>("healthIssue"));

        TableColumn<Inmate, String> cellIdCol = new TableColumn<>("Cell ID");
        cellIdCol.setCellValueFactory(new PropertyValueFactory<>("cellId"));

        TableColumn<Inmate, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.setOnAction(event -> {
                    Inmate i = getTableView().getItems().get(getIndex());
                    showEditInmateDialog(i);
                });

                deleteBtn.setOnAction(event -> {
                    Inmate i = getTableView().getItems().get(getIndex());
                    deleteInmate(i.getInmateId());
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

        table.getColumns().addAll(idCol, nameCol, typeCol, healthIssueCol, cellIdCol, actionCol);
        refreshTable();

        // Set up filtered list
        filteredData = new FilteredList<>(masterData, p -> true);
        table.setItems(filteredData);

        box.getChildren().addAll(title, addInmateBtn, searchBox, table);
        return box;
    }

    private HBox createSearchBox() {
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10, 0, 10, 0));
        searchBox.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Search inmates by ID, name, type, health issue, or cell...");
        searchField.setPrefWidth(400);
        
        // Add listener to filter as you type
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            performSearch();
        });
        
        Button searchButton = new Button("🔍");
        searchButton.setOnAction(e -> performSearch());

        searchBox.getChildren().addAll(searchField, searchButton);
        return searchBox;
    }

    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        
        filteredData.setPredicate(inmate -> {
            // If search text is empty, show all inmates
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            
            // Compare search text with all available fields
            return String.valueOf(inmate.getInmateId()).contains(searchText) ||
                   inmate.getFullName().toLowerCase().contains(searchText) ||
                   inmate.getInmateType().toLowerCase().contains(searchText) ||
                   (inmate.getHealthIssue() != null && 
                    inmate.getHealthIssue().toLowerCase().contains(searchText)) ||
                   (inmate.getCellId() != null && 
                    inmate.getCellId().toLowerCase().contains(searchText));
        });
    }

    // Refresh table using stored procedure GetAllInmates
    private void refreshTable() {
        masterData.clear();
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL GetAllInmates()}")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fullName = rs.getString("last_name") + ", " + rs.getString("first_name");
                String inmateType = rs.getString("inmate_type");
                masterData.add(new Inmate(
                        rs.getInt("inmate_id"),
                        fullName,
                        inmateType,
                        rs.getString("health_issue"),
                        rs.getString("cell_id")
                ));
            }
            
            // If there's an active search, apply it again
            if (searchField != null && !searchField.getText().isEmpty()) {
                performSearch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void showAddInmateDialog() {
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Add New Inmate");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20));

    TextField typeField = new TextField();
    TextField lastNameField = new TextField();
    TextField firstNameField = new TextField();
    TextField middleNameField = new TextField();
    TextField genderField = new TextField();
    DatePicker birthDatePicker = new DatePicker();
    TextField civilStatusField = new TextField();
    TextField nationalityField = new TextField();
    TextField addressField = new TextField();
    TextField healthIssueField = new TextField();
    TextField cellIdField = new TextField();
    TextField photoPathField = new TextField();

    grid.add(new Label("Type:"), 0, 0); grid.add(typeField, 1, 0);
    grid.add(new Label("Last Name:"), 0, 1); grid.add(lastNameField, 1, 1);
    grid.add(new Label("First Name:"), 0, 2); grid.add(firstNameField, 1, 2);
    grid.add(new Label("Middle Name:"), 0, 3); grid.add(middleNameField, 1, 3);
    grid.add(new Label("Gender:"), 0, 4); grid.add(genderField, 1, 4);
    grid.add(new Label("Birth Date:"), 0, 5); grid.add(birthDatePicker, 1, 5);
    grid.add(new Label("Civil Status:"), 0, 6); grid.add(civilStatusField, 1, 6);
    grid.add(new Label("Nationality:"), 0, 7); grid.add(nationalityField, 1, 7);
    grid.add(new Label("Address:"), 0, 8); grid.add(addressField, 1, 8);
    grid.add(new Label("Health Issue:"), 0, 9); grid.add(healthIssueField, 1, 9);
    grid.add(new Label("Cell ID:"), 0, 10); grid.add(cellIdField, 1, 10);
    grid.add(new Label("Photo Path:"), 0, 11); grid.add(photoPathField, 1, 11);

    dialog.getDialogPane().setContent(grid);
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    dialog.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            addInmateToDatabase(
                typeField.getText(), lastNameField.getText(), firstNameField.getText(), middleNameField.getText(), genderField.getText(),
                birthDatePicker.getValue() == null ? null : Date.valueOf(birthDatePicker.getValue()),
                civilStatusField.getText(), nationalityField.getText(), addressField.getText(), healthIssueField.getText(),
                cellIdField.getText(), photoPathField.getText()
            );
            refreshTable();
        }
    });
}

    
    private void addInmateToDatabase(String type, String lastName, String firstName, String middleName, String gender,
                                  java.sql.Date birthDate, String civilStatus, String nationality,
                                  String address, String healthIssue, String cellId, String photoPath) {
    try (Connection conn = DBConnect.getConnection();
         CallableStatement stmt = conn.prepareCall("{CALL AddInmate(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {

        stmt.setString(1, type);
        stmt.setString(2, lastName);
        stmt.setString(3, firstName);
        stmt.setString(4, middleName);
        stmt.setString(5, gender);
        stmt.setDate(6, birthDate);
        stmt.setString(7, civilStatus);
        stmt.setString(8, nationality);
        stmt.setString(9, address);
        stmt.setString(10, healthIssue);
        stmt.setString(11, cellId);
        stmt.setString(12, photoPath);

        stmt.execute();
        System.out.println("Inmate added successfully.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // Show a dialog with all editable fields
    private void showEditInmateDialog(Inmate i) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Inmate");
        dialog.setHeaderText("Edit Inmate ID: " + i.getInmateId());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField healthIssueField = new TextField(i.getHealthIssue());
        TextField cellIdField = new TextField(i.getCellId());

        grid.add(new Label("Health Issues:"), 0, 0);
        grid.add(healthIssueField, 1, 0);
        grid.add(new Label("Cell ID:"), 0, 1);
        grid.add(cellIdField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                updateInmate(i.getInmateId(), healthIssueField.getText(), cellIdField.getText());
                refreshTable();
            }
        });
    }

    // Calls stored procedure UpdateInmate
    private void updateInmate(int inmateId, String healthIssue, String cellId) {
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL UpdateInmate(?, ?, ?)}")) {

            stmt.setInt(1, inmateId);
            stmt.setString(2, healthIssue);
            stmt.setString(3, cellId);

            stmt.execute();
            System.out.println("Inmate updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteInmate(int id) {
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL DeleteInmate(?)}")) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Inmate with ID " + id + " was successfully deleted.");
            } else {
                System.out.println("No inmate found with ID " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error executing DELETE procedure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Sidebar containing navigation buttons
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
    // Inmate class now includes health issue and cell ID
    public static class Inmate {
        private final SimpleIntegerProperty inmateId;
        private final SimpleStringProperty fullName;
        private final SimpleStringProperty inmateType;
        private final SimpleStringProperty healthIssue;
        private final SimpleStringProperty cellId;

        public Inmate(int id, String fullName, String inmateType, String healthIssue, String cellId) {
            this.inmateId = new SimpleIntegerProperty(id);
            this.fullName = new SimpleStringProperty(fullName);
            this.inmateType = new SimpleStringProperty(inmateType);
            this.healthIssue = new SimpleStringProperty(healthIssue == null ? "" : healthIssue);
            this.cellId = new SimpleStringProperty(cellId == null ? "" : cellId);
        }

        public int getInmateId() { return inmateId.get(); }
        public String getFullName() { return fullName.get(); }
        public String getInmateType() { return inmateType.get(); }
        public String getHealthIssue() { return healthIssue.get(); }
        public String getCellId() { return cellId.get(); }
    }
}