package prisonvisitation1;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class VisitorDashboard extends Application {

    private static int loggedInVisitorID = 1;

    @Override
    public void start(Stage stage) {
        stage.setMaximized(true);

        // Top Navbar
        HBox navbar = new HBox();
        navbar.setPadding(new Insets(10));
        navbar.setStyle("-fx-background-color: #2f3542;");
        navbar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Visitor Dashboard");
        title.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Hyperlink logoutLink = new Hyperlink("Logout");
        logoutLink.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        logoutLink.setOnAction(e -> logout(stage));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navbar.getChildren().addAll(title, spacer, logoutLink);

        // Left: Analytics
        VBox analyticsSection = createAnalyticsSection();

        // Visit History Table
        Label visitHistoryLabel = new Label("📜 Visit History");
        visitHistoryLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TableView<VisitRecord> visitHistoryTable = createVisitHistoryTable();

        // Pending Requests Table
        Label pendingLabel = new Label("📝 Pending Requests");
        pendingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TableView<VisitRecord> pendingRequestsTable = createPendingRequestsTable();

        // Schedule Form
        Label scheduleLabel = new Label("🗓️ Schedule a Visit");
        scheduleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        VBox scheduleForm = createScheduleForm(visitHistoryTable);

        // Combine right section
        VBox rightSection = new VBox(20);
        rightSection.setPadding(new Insets(20));
        rightSection.setPrefWidth(750);
        rightSection.setAlignment(Pos.TOP_CENTER);
        rightSection.getChildren().addAll(
            visitHistoryLabel, visitHistoryTable,
            pendingLabel, pendingRequestsTable,
            scheduleLabel, scheduleForm
        );

        // Combine left and right sections
        HBox body = new HBox(20, analyticsSection, rightSection);
        body.setPadding(new Insets(20));
        VBox root = new VBox(navbar, body);

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Visitor Dashboard");
        stage.setScene(scene);
        stage.show();
    }


    private VBox createAnalyticsSection() {
        VBox section = new VBox(20);
        section.setAlignment(Pos.TOP_CENTER);
        section.setPadding(new Insets(20));
        section.setPrefWidth(700);
        section.setStyle("-fx-background-color: #f1f2f6;");
        Label title = new Label("📊 Visitor Analytics");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        LineChart<String, Number> lineChart = createVisitLineChart();
        BarChart<String, Number> barChart = createVisitBarChart();
        section.getChildren().addAll(title, lineChart, barChart);
        return section;
    }

    private LineChart<String, Number> createVisitLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        xAxis.setLabel("Date");
        yAxis.setLabel("Visits");
        chart.setTitle("Visit Frequency");
        XYChart.Series<String, Number> data = new XYChart.Series<>();

        String query = "SELECT visit_date, COUNT(*) AS count FROM VISIT WHERE visitor_id = ? AND visit_status = 'Approved' GROUP BY visit_date";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, loggedInVisitorID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.getData().add(new XYChart.Data<>(rs.getString("visit_date"), rs.getInt("count")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        chart.getData().add(data);
        return chart;
    }

    private BarChart<String, Number> createVisitBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        xAxis.setLabel("Inmate");
        yAxis.setLabel("Visits");
        chart.setTitle("Visits per Inmate");
        XYChart.Series<String, Number> data = new XYChart.Series<>();

        String query = "SELECT inmate_id, COUNT(*) AS count FROM VISIT WHERE visitor_id = ? AND visit_status = 'Approved' GROUP BY inmate_id";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, loggedInVisitorID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.getData().add(new XYChart.Data<>("Inmate " + rs.getInt("inmate_id"), rs.getInt("count")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        chart.getData().add(data);
        return chart;
    }

    private TableView<VisitRecord> createVisitHistoryTable() {
      TableView<VisitRecord> table = new TableView<>();
      table.setPrefHeight(200);

      TableColumn<VisitRecord, Integer> idCol = new TableColumn<>("Visit ID");
      idCol.setCellValueFactory(new PropertyValueFactory<>("visitId"));
      idCol.setPrefWidth(120);
      idCol.setStyle("-fx-alignment: CENTER;");

      TableColumn<VisitRecord, Integer> inmateCol = new TableColumn<>("Inmate ID");
      inmateCol.setCellValueFactory(new PropertyValueFactory<>("inmateId"));
      inmateCol.setPrefWidth(120);
      inmateCol.setStyle("-fx-alignment: CENTER;");

      TableColumn<VisitRecord, String> dateCol = new TableColumn<>("Date");
      dateCol.setCellValueFactory(new PropertyValueFactory<>("visitDate"));
      dateCol.setPrefWidth(160);
      dateCol.setStyle("-fx-alignment: CENTER;");

      TableColumn<VisitRecord, String> timeCol = new TableColumn<>("Time");
      timeCol.setCellValueFactory(new PropertyValueFactory<>("visitTime"));
      timeCol.setPrefWidth(150);
      timeCol.setStyle("-fx-alignment: CENTER;");

      TableColumn<VisitRecord, String> statusCol = new TableColumn<>("Status");
      statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
      statusCol.setPrefWidth(158);
      statusCol.setStyle("-fx-alignment: CENTER;");

      table.getColumns().addAll(idCol, inmateCol, dateCol, timeCol, statusCol);
      refreshVisitHistory(table);
      return table;
  }

    private void refreshVisitHistory(TableView<VisitRecord> table) {
        table.getItems().clear();
        String sql = "{CALL GetApprovedVisitsByVisitor(?)}"; // Call the stored procedure
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, loggedInVisitorID);  // Pass the logged-in visitor's ID
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                VisitRecord record = new VisitRecord(
                        rs.getInt("visit_id"),
                        rs.getInt("inmate_id"),
                        rs.getString("visit_date"),
                        rs.getString("visit_time"),
                        rs.getString("visit_status")
                );
                table.getItems().add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createScheduleForm(TableView<VisitRecord> tableToRefresh) {
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        form.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");
        form.setTranslateY(5); 

        TextField inmateIdField = new TextField();
        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField();
        TextField purposeField = new TextField();
        TextField relationshipField = new TextField();

        inmateIdField.setPromptText("Inmate ID");
        datePicker.setPromptText("Visit Date");
        timeField.setPromptText("Visit Time (HH:MM:SS)");
        purposeField.setPromptText("Purpose");
        relationshipField.setPromptText("Relationship to Inmate");

        // Create a HBox to place Date and Time fields side by side
        HBox dateTimeBox = new HBox(20); 
        dateTimeBox.setAlignment(Pos.CENTER_LEFT);  
        dateTimeBox.getChildren().addAll(datePicker, timeField);  

        Button submitBtn = new Button("Submit Visit Request");
        submitBtn.setOnAction(e -> {
            try (Connection conn = DBConnect.getConnection()) {
                String sql = "{CALL AddVisitRequest(?, ?, ?, ?, ?, ?)}"; 
                try (PreparedStatement stmt = conn.prepareCall(sql)) {
                    stmt.setInt(1, loggedInVisitorID);
                    stmt.setInt(2, Integer.parseInt(inmateIdField.getText()));
                    stmt.setDate(3, Date.valueOf(datePicker.getValue()));
                    stmt.setTime(4, Time.valueOf(LocalTime.parse(timeField.getText())));
                    stmt.setString(5, purposeField.getText());
                    stmt.setString(6, relationshipField.getText());
                    stmt.executeUpdate();
                }

                // Clear fields after submission
                inmateIdField.clear();
                datePicker.setValue(null);
                timeField.clear();
                purposeField.clear();
                relationshipField.clear();

                // Show confirmation pop-up
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Visit Request Submitted");
                alert.setHeaderText("Your visit request is up for review.");
                alert.setContentText("Please wait for the approval of your visit.");
                alert.showAndWait();

                // Refresh the visit history table
                refreshVisitHistory(tableToRefresh);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add all form fields to the VBox
        form.getChildren().addAll(
                inmateIdField, dateTimeBox, purposeField,
                relationshipField, submitBtn
        );
        return form;
    }


    private VBox createPendingRequestsSection() {
        VBox section = new VBox(20);
        section.setAlignment(Pos.TOP_CENTER);
        section.setPadding(new Insets(20));
        section.setPrefWidth(700);
        section.setStyle("-fx-background-color: #f1f2f6;");

        Label title = new Label("📝 Pending Requests");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Create the TableView for Pending Visits
        TableView<VisitRecord> pendingRequestsTable = createPendingRequestsTable();

        // Add everything to the section
        section.getChildren().addAll(title, pendingRequestsTable);
        return section;
    }

    
    private TableView<VisitRecord> createPendingRequestsTable() {
        TableView<VisitRecord> table = new TableView<>();
        table.setPrefHeight(200);

        TableColumn<VisitRecord, Integer> idCol = new TableColumn<>("Visit ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("visitId"));
        idCol.setPrefWidth(120);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<VisitRecord, Integer> inmateCol = new TableColumn<>("Inmate ID");
        inmateCol.setCellValueFactory(new PropertyValueFactory<>("inmateId"));
        inmateCol.setPrefWidth(120);
        inmateCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<VisitRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("visitDate"));
        dateCol.setPrefWidth(160);
        dateCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<VisitRecord, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("visitTime"));
        timeCol.setPrefWidth(150);
        timeCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<VisitRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(158);
        statusCol.setStyle("-fx-alignment: CENTER;");

        table.getColumns().addAll(idCol, inmateCol, dateCol, timeCol, statusCol);
        refreshPendingRequests(table);  
        return table;
    }

    private void refreshPendingRequests(TableView<VisitRecord> table) {
        table.getItems().clear();
        String sql = "{CALL GetPendingRequestsForVisitor(?)}"; 
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, loggedInVisitorID);  
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                VisitRecord record = new VisitRecord(
                        rs.getInt("visit_id"),
                        rs.getInt("inmate_id"),
                        rs.getString("visit_date"),
                        rs.getString("visit_time"),
                        rs.getString("visit_status")
                );
                table.getItems().add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void logout(Stage stage) {
        try {
            SessionManager.logoutVisitor(); 
            new LandingPage().start(new Stage());
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setLoggedInVisitorID(int id) {
        loggedInVisitorID = id;
    }

    public static class VisitRecord {
        private final int visitId, inmateId;
        private final String visitDate, visitTime, status;

        public VisitRecord(int visitId, int inmateId, String visitDate, String visitTime, String status) {
            this.visitId = visitId;
            this.inmateId = inmateId;
            this.visitDate = visitDate;
            this.visitTime = visitTime;
            this.status = status;
        }

        public int getVisitId() { return visitId; }
        public int getInmateId() { return inmateId; }
        public String getVisitDate() { return visitDate; }
        public String getVisitTime() { return visitTime; }
        public String getStatus() { return status; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
