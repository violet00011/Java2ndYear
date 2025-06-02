
package prisonvisitation1;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;

public class VisitRequests extends Application {

    private TableView<VisitRequest> table;
    private ObservableList<VisitRequest> data;

    @Override
    public void start(Stage stage) throws Exception {
        // ===== NAVBAR =====
        HBox navbar = new HBox();
        navbar.setPadding(new Insets(10));
        navbar.setStyle("-fx-background-color: #2f3542;");
        navbar.setAlignment(Pos.CENTER_LEFT);

        Label navTitle = new Label("Officer Dashboard");
        navTitle.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Hyperlink logoutLink = new Hyperlink("Logout");
        logoutLink.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        logoutLink.setOnAction(e -> logout(stage));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navbar.getChildren().addAll(navTitle, spacer, logoutLink);

        // ===== SIDEBAR + MAIN CONTENT =====
        VBox sidebar = createSidebar(stage);
        VBox mainContent = createVisitRequestsContent();
        HBox layout = new HBox(20, sidebar, mainContent);
        layout.setPadding(new Insets(20));

        // ===== ROOT CONTAINER =====
        BorderPane root = new BorderPane();
        root.setTop(navbar);     // navbar at the top
        root.setCenter(layout);  // your original layout in center

        // ===== SCENE SETUP =====
        Scene scene = new Scene(root, 1000, 800, Color.LIGHTGRAY);
        stage.setTitle("Officer Dashboard");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    // Sidebar layout creation
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

// Modify the createVisitRequestsContent method to include a search bar
private VBox createVisitRequestsContent() {
    table = new TableView<>();
    data = FXCollections.observableArrayList();

    // Create search bar component
    TextField searchField = new TextField();
    searchField.setPromptText("Search by visitor name, date or time...");
    searchField.setPrefWidth(400);
    
    // Add listener to filter table when text changes
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
        filterTable(newValue);
    });
    
    // Create a horizontal layout for search components
    HBox searchBox = new HBox(10, searchField);
    searchBox.setPadding(new Insets(0, 0, 10, 0));
    searchBox.setAlignment(Pos.CENTER_LEFT);

    // Create columns for the table
    TableColumn<VisitRequest, String> idColumn = new TableColumn<>("Visit ID");
    idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getVisitId())));
    idColumn.setPrefWidth(200); // Set preferred width for this column

    TableColumn<VisitRequest, String> visitorNameColumn = new TableColumn<>("Visitor Name");
    visitorNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVisitorName()));
    visitorNameColumn.setPrefWidth(300); // Adjust column width

    TableColumn<VisitRequest, String> visitDateColumn = new TableColumn<>("Visit Date");
    visitDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVisitDate()));
    visitDateColumn.setPrefWidth(200); // Adjust column width

    TableColumn<VisitRequest, String> visitTimeColumn = new TableColumn<>("Visit Time");
    visitTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVisitTime()));
    visitTimeColumn.setPrefWidth(200); // Adjust column width

    TableColumn<VisitRequest, String> actionColumn = new TableColumn<>("Actions");
    actionColumn.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));
    actionColumn.setCellFactory(col -> {
        return new TableCell<VisitRequest, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Create approve button
                    Button approveButton = new Button("Approve");
                    approveButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    approveButton.setOnAction(e -> approveVisit(getTableView().getItems().get(getIndex())));

                    // Create deny button
                    Button denyButton = new Button("Deny");
                    denyButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    denyButton.setOnAction(e -> denyVisit(getTableView().getItems().get(getIndex())));

                    // Arrange buttons in a horizontal layout
                    HBox buttons = new HBox(10, approveButton, denyButton);
                    buttons.setAlignment(Pos.CENTER);
                    setGraphic(buttons);
                }
            }
        };
    });
    actionColumn.setPrefWidth(250); // Adjust column width

    // Add columns to the table
    table.getColumns().add(idColumn);
    table.getColumns().add(visitorNameColumn);
    table.getColumns().add(visitDateColumn);
    table.getColumns().add(visitTimeColumn);
    table.getColumns().add(actionColumn);

    // Enable the table to expand with the screen
    table.setMaxWidth(Double.MAX_VALUE); // Allow the table to fill available space

    // Load data from database
    loadData();

    // Create the main content layout
    VBox mainContent = new VBox(10, searchBox, table);
    mainContent.setSpacing(20); // Add space between the table and other elements if needed

    return mainContent;
}

// Add a new method to filter the table based on search text
private void filterTable(String searchText) {
    if (searchText == null || searchText.isEmpty()) {
        // If search is empty, show all data
        table.setItems(data);
        return;
    }
    
    // Create a filtered list to store search results
    ObservableList<VisitRequest> filteredData = FXCollections.observableArrayList();
    
    // Convert search text to lowercase for case-insensitive search
    String lowerCaseSearch = searchText.toLowerCase();
    
    // Loop through all items and add matches to filteredData
    for (VisitRequest request : data) {
        // Search by visitor name, visit date, or visit time
        if (request.getVisitorName().toLowerCase().contains(lowerCaseSearch) ||
            request.getVisitDate().toLowerCase().contains(lowerCaseSearch) ||
            request.getVisitTime().toLowerCase().contains(lowerCaseSearch)) {
            filteredData.add(request);
        }
    }
    
    // Update the table with filtered results
    table.setItems(filteredData);
}

    //para ma fetch data
    private void loadData() {
        String sql = "CALL GetPendingVisitRequests()";  
        data.clear();

        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");  
                String visitorName = rs.getString("visitor_name");
                String visitDate = rs.getString("visit_date");
                String visitTime = rs.getString("visit_time");
                String source = rs.getString("source");

                data.add(new VisitRequest(id, visitorName, visitDate, visitTime, source));
            }

            table.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private void approveVisit(VisitRequest visitRequest) {
        if ("onetime".equals(visitRequest.getSource())) {
            String procedureCall = "{call ApproveVisitor(?, ?)}";

            try (Connection conn = DBConnect.getConnection();
                 CallableStatement stmt = conn.prepareCall(procedureCall)) {

                stmt.setInt(1, visitRequest.getVisitId());
                stmt.setInt(2, getCurrentOfficerId());
                stmt.executeUpdate();
                data.remove(visitRequest);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if ("visit".equals(visitRequest.getSource())) {
            String procedureCall = "{call ApproveVisit(?)}";

            try (Connection conn = DBConnect.getConnection();
                 CallableStatement stmt = conn.prepareCall(procedureCall)) {

                stmt.setInt(1, visitRequest.getVisitId());
                stmt.executeUpdate();
                data.remove(visitRequest);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void denyVisit(VisitRequest visitRequest) {
        if ("onetime".equals(visitRequest.getSource())) {
            String procedureCall = "{call DenyVisitor(?)}";

            try (Connection conn = DBConnect.getConnection();
                 CallableStatement stmt = conn.prepareCall(procedureCall)) {

                stmt.setInt(1, visitRequest.getVisitId());
                stmt.executeUpdate();
                data.remove(visitRequest);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if ("visit".equals(visitRequest.getSource())) {
            String procedureCall = "{call DenyVisit(?)}";

            try (Connection conn = DBConnect.getConnection();
                 CallableStatement stmt = conn.prepareCall(procedureCall)) {

                stmt.setInt(1, visitRequest.getVisitId());
                stmt.executeUpdate();
                data.remove(visitRequest);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int getCurrentOfficerId() {
        // Replace this with logic to get the logged-in officer's ID
        return 1; // Example officer ID
    }

    private void logout(Stage stage) {
        // Implement logout functionality
    }

    public static void main(String[] args) {
        launch(args);
    }

 public class VisitRequest {
    private final int visitId;
    private final String visitorName;
    private final String visitDate;
    private final String visitTime;
    private final String source;

    public VisitRequest(int visitId, String visitorName, String visitDate, String visitTime, String source) {
        this.visitId = visitId;
        this.visitorName = visitorName;
        this.visitDate = visitDate;
        this.visitTime = visitTime;
        this.source = source;
    }

    public int getVisitId() {
        return visitId;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public String getSource() {
        return source;
    }
}

}

//        mainContent.setPrefHeight(900);
 //        mainContent.setPrefHeight(800);