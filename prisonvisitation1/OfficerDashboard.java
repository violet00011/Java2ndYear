package prisonvisitation1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;



public class OfficerDashboard extends Application {

    @Override
    public void start(Stage stage) {
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
        VBox mainContent = createMainContent();
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

    // Main content area (where we will display analytics)
    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPrefWidth(1200);
        mainContent.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 20px;");
        
        // Title for the analytics section
        Label analyticsTitle = new Label("Graphical Analytics");
        analyticsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Create the graphs and add them to the main content
        VBox graphContainer = new VBox(20);
          graphContainer.setAlignment(Pos.CENTER);

          // First Row
        PieChart pieChart = createVisitorRequestPieChart();
        pieChart.setPrefSize(350, 350); 

        LineChart<String, Number> lineChart = createVisitorLineChart();

        HBox row1 = new HBox(20, pieChart, lineChart);
        row1.setAlignment(Pos.CENTER);

          // Second Row
          HBox row2 = new HBox(20, createOfficerAssignmentBarChart(), createInmateBreakdownAreaChart());
          row2.setAlignment(Pos.CENTER);

          // Third Row
            ScatterChart<String, Number> availabilityChart = createCellAvailabilityScatterChart();
            Label infoLabel = new Label("Capacity per cell is 10 Inmates");
            infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

            VBox chartWithLabel = new VBox(5, availabilityChart, infoLabel); // 5px spacing between chart and label
            chartWithLabel.setAlignment(Pos.CENTER);

            HBox row3 = new HBox(20, chartWithLabel);
            row3.setAlignment(Pos.CENTER);


          // Add rows to the container
          graphContainer.getChildren().addAll(row1, row2, row3);

        mainContent.getChildren().addAll(analyticsTitle, graphContainer);
        return mainContent;
    }

    // Pie Chart for Visitor Requests this week
    private PieChart createVisitorRequestPieChart() {
    PieChart pieChart = new PieChart();
    pieChart.setTitle("Visitor Requests This Week");
    try (Connection conn = DBConnect.getConnection()) {
            String query = "SELECT visit_status, COUNT(*) AS count " +
                           "FROM VISIT WHERE YEARWEEK(visit_date, 1) = YEARWEEK(CURDATE(), 1) " +
                           "GROUP BY visit_status";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
                String status = rs.getString("visit_status");
                int count = rs.getInt("count");
                pieChart.getData().add(new PieChart.Data(status, count));
            }
    } catch (SQLException e) {
            e.printStackTrace();
        }
    return pieChart;
    }

    // Line Chart for Visitors comparison (This Week vs Last Week)
    private LineChart<String, Number> createVisitorLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Visitors: This Week vs Last Week");
    XYChart.Series<String, Number> thisWeekSeries = new XYChart.Series<>();
        thisWeekSeries.setName("This Week");
        XYChart.Series<String, Number> lastWeekSeries = new XYChart.Series<>();
        lastWeekSeries.setName("Last Week");
    try (Connection conn = DBConnect.getConnection()) {
            String[] weekLabels = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    for (int i = 0; i < 7; i++) {
                String day = weekLabels[i];
    String queryThis = "SELECT COUNT(*) FROM VISIT " +
                    "WHERE WEEK(visit_date, 1) = WEEK(CURDATE(), 1) AND DAYNAME(visit_date) = ?";
                PreparedStatement stmtThis = conn.prepareStatement(queryThis);
                stmtThis.setString(1, day);
                ResultSet rsThis = stmtThis.executeQuery();
                if (rsThis.next()) {
                    thisWeekSeries.getData().add(new XYChart.Data<>(day, rsThis.getInt(1)));
                }
    String queryLast = "SELECT COUNT(*) FROM VISIT " +
                    "WHERE WEEK(visit_date, 1) = WEEK(CURDATE(), 1) - 1 AND DAYNAME(visit_date) = ?";
                PreparedStatement stmtLast = conn.prepareStatement(queryLast);
                stmtLast.setString(1, day);
                ResultSet rsLast = stmtLast.executeQuery();
                if (rsLast.next()) {
                    lastWeekSeries.getData().add(new XYChart.Data<>(day, rsLast.getInt(1)));
                }
            }
    } catch (SQLException e) {
            e.printStackTrace();
        }
    lineChart.getData().addAll(thisWeekSeries, lastWeekSeries);
        return lineChart;
    }


    // Area Chart for Inmates Breakdown (Prisoner vs Detainee)
    private AreaChart<String, Number> createInmateBreakdownAreaChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Inmate Breakdown");
    try (Connection conn = DBConnect.getConnection()) {
            String query = "SELECT inmate_type, COUNT(*) AS count FROM INMATE GROUP BY inmate_type";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
    XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Inmates");
    while (rs.next()) {
                String type = rs.getString("inmate_type");
                int count = rs.getInt("count");
                series.getData().add(new XYChart.Data<>(type, count));
            }
    areaChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return areaChart;
    }

private BarChart<String, Number> createOfficerAssignmentBarChart() {
    // Define CategoryAxis for the X axis and NumberAxis for the Y axis
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();

    // Explicitly define BarChart with String for X and Number for Y
    BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
    barChart.setTitle("Officer Assignment");

    try (Connection conn = DBConnect.getConnection()) {
        // Query to fetch officer data
        String query = "SELECT assigned_cell_id, COUNT(*) AS total " +
                       "FROM POLICE_OFFICER GROUP BY assigned_cell_id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Create Series of type String for X (assigned_cell_id) and Number for Y (total assignments)
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Assignments");

        // Process the result set and add data to the series
        while (rs.next()) {
            String cell = rs.getString("assigned_cell_id");  // Correct: it should be a String
            int count = rs.getInt("total"); 
            series.getData().add(new XYChart.Data<>(cell, count));
        }

        // Add the series to the chart
        barChart.getData().add(series);
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return barChart;
}

// Scatter Chart for Available Cells by Type
private ScatterChart<String, Number> createCellAvailabilityScatterChart() {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    ScatterChart<String, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
    scatterChart.setTitle("Available Cells by Type");

    try (Connection conn = DBConnect.getConnection()) {
        String query = "SELECT type, SUM(capacity - occupied_count) AS available FROM CELL GROUP BY type";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Available");

        while (rs.next()) {
            String type = rs.getString("type");
            int available = rs.getInt("available");
            series.getData().add(new XYChart.Data<>(type, available));
        }

        scatterChart.getData().add(series);
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return scatterChart;
}

        // Navigation placeholder methods
        private void showVisitRequests(Stage stage) { System.out.println("Showing visit requests..."); }
        private void manageVisitors(Stage stage) { System.out.println("Managing visitors..."); }
        private void manageInmates(Stage stage) { System.out.println("Managing inmates..."); }
        private void manageOfficers(Stage stage) { System.out.println("Managing officers..."); }
        private void manageCells(Stage stage) { System.out.println("Managing cells..."); }
        private void generateReports(Stage stage) { System.out.println("Generating reports..."); }
        private void logout(Stage stage) { stage.close(); }

        public static void main(String[] args) {
            launch(args);
        }
        }