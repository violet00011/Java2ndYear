package prisonvisitation1;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import java.awt.Desktop;
import java.io.File;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class REPORTS extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Create the main VBox container to hold all report sections
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new javafx.geometry.Insets(20));

        // Add reports to the VBox
        mainContainer.getChildren().add(createInmateHealthConcernsReport());
        mainContainer.getChildren().add(createDailyVisitVolumeReport());
        mainContainer.getChildren().add(createMonthlyVisitVolumeReport());
        mainContainer.getChildren().add(createYearlyVisitVolumeReport());
        mainContainer.getChildren().add(createAllTimeVisitVolumeReport());
        mainContainer.getChildren().add(createVisitsByRelationshipReport());
        mainContainer.getChildren().add(createTopFrequentVisitorsReport());
        mainContainer.getChildren().add(createGenderDistributionReport());
        mainContainer.getChildren().add(createCellOccupancyReport());
        mainContainer.getChildren().add(createOneTimeVisitorLogsReport());

        // Set the ScrollPane to make the content scrollable
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);  // Make sure the width adjusts properly

        // Create the scene and set it on the stage
        Scene scene = new Scene(scrollPane, 800, 600);
        stage.setTitle("Prison Visitation Reports");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createInmateHealthConcernsReport() {
        VBox reportContainer = new VBox(10);
        reportContainer.setAlignment(Pos.TOP_LEFT);
        reportContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

        TableView<InmateHealthReport> table = createInmateHealthTable();

        // Create the "Print as PDF" button
        Button printButton = new Button("Print as PDF");
        printButton.setOnAction(event -> generateInmateHealthConcernsReportPDF());

        // Add components to the VBox
        reportContainer.getChildren().add(new Label("Inmate Health Concerns Report"));
        reportContainer.getChildren().add(table);
        reportContainer.getChildren().add(printButton);

        return reportContainer;
    }

    private TableView<InmateHealthReport> createInmateHealthTable() {
        TableView<InmateHealthReport> table = new TableView<>();

        TableColumn<InmateHealthReport, String> nameColumn = new TableColumn<>("Inmate Name");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<InmateHealthReport, String> issueColumn = new TableColumn<>("Health Issue");
        issueColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHealthIssue()));

        TableColumn<InmateHealthReport, String> birthColumn = new TableColumn<>("Birth Date");
        birthColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBirthDate()));

        TableColumn<InmateHealthReport, String> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));

        table.getColumns().addAll(nameColumn, issueColumn, birthColumn, genderColumn);

        ObservableList<InmateHealthReport> data = fetchInmateHealthData(); // Fetch data from DB
        table.setItems(data);

        return table;
    }

    private ObservableList<InmateHealthReport> fetchInmateHealthData() {
        ObservableList<InmateHealthReport> data = FXCollections.observableArrayList();
        // Database connection code to fetch data
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_inmate_health_report()}");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.add(new InmateHealthReport(
                        rs.getString("inmate_name"),
                        rs.getString("health_issue"),
                        rs.getString("birth_date"),
                        rs.getString("gender")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void generateInmateHealthConcernsReportPDF() {
        ObservableList<InmateHealthReport> data = fetchInmateHealthData();
        String fileName = "Inmate_Health_Concerns_Report.pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Inmate Health Concerns Report"));

            Table table = new Table(4); // 4 columns
            table.addCell("Inmate Name");
            table.addCell("Health Issue");
            table.addCell("Birth Date");
            table.addCell("Gender");

            for (InmateHealthReport report : data) {
                table.addCell(report.getName());
                table.addCell(report.getHealthIssue());
                table.addCell(report.getBirthDate());
                table.addCell(report.getGender());
            }

            document.add(table);
            document.close();

            // Auto-open the PDF (optional)
            Desktop.getDesktop().open(new File(fileName));

            System.out.println("PDF generated and opened: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private VBox createDailyVisitVolumeReport() {
        VBox reportContainer = new VBox(10);
        reportContainer.setAlignment(Pos.TOP_LEFT);
        reportContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

        TableView<VisitVolumeReport> table = createVisitVolumeTable();

        Button printButton = new Button("Print as PDF");
        printButton.setOnAction(event -> generateVisitVolumeReportPDF());

        reportContainer.getChildren().add(new Label("Daily Visit Volume Report"));
        reportContainer.getChildren().add(table);
        reportContainer.getChildren().add(printButton);

        return reportContainer;
    }

    private TableView<VisitVolumeReport> createVisitVolumeTable() {
        TableView<VisitVolumeReport> table = new TableView<>();

        TableColumn<VisitVolumeReport, String> dateColumn = new TableColumn<>("Visit Date");
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisitDate()));

        TableColumn<VisitVolumeReport, String> countColumn = new TableColumn<>("Visit Count");
        countColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisitCount()));

        table.getColumns().addAll(dateColumn, countColumn);

        ObservableList<VisitVolumeReport> data = fetchVisitVolumeData();
        table.setItems(data);

        return table;
    }

    private ObservableList<VisitVolumeReport> fetchVisitVolumeData() {
        ObservableList<VisitVolumeReport> data = FXCollections.observableArrayList();
        // Database connection code to fetch data
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_daily_visit_volume()}");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.add(new VisitVolumeReport(
                        rs.getString("visit_date"),
                        rs.getString("visit_count")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void generateVisitVolumeReportPDF() {
        ObservableList<VisitVolumeReport> data = fetchVisitVolumeData();
        String fileName = "Daily_Visit_Volume_Report.pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Daily Visit Volume Report"));

            Table table = new Table(2);
            table.addCell("Visit Date");
            table.addCell("Visit Count");

            for (VisitVolumeReport report : data) {
                table.addCell(report.getVisitDate());
                table.addCell(report.getVisitCount());
            }

            document.add(table);
            document.close();

            // Auto-open the PDF (optional)
            Desktop.getDesktop().open(new File(fileName));

            System.out.println("PDF generated and opened: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createMonthlyVisitVolumeReport() {
        VBox reportContainer = new VBox(10);
        reportContainer.setAlignment(Pos.TOP_LEFT);
        reportContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

        TableView<MonthlyVisitVolume> table = createMonthlyVisitVolumeTable();

        Button printButton = new Button("Print as PDF");
        printButton.setOnAction(event -> generateMonthlyVisitVolumeReportPDF());

        reportContainer.getChildren().add(new Label("Monthly Visit Volume Report"));
        reportContainer.getChildren().add(table);
        reportContainer.getChildren().add(printButton);

        return reportContainer;
    }

    private TableView<MonthlyVisitVolume> createMonthlyVisitVolumeTable() {
        TableView<MonthlyVisitVolume> table = new TableView<>();

        TableColumn<MonthlyVisitVolume, String> monthColumn = new TableColumn<>("Month");
        monthColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMonth()));

        TableColumn<MonthlyVisitVolume, String> visitCountColumn = new TableColumn<>("Visit Count");
        visitCountColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisitCount()));

        table.getColumns().addAll(monthColumn, visitCountColumn);

        ObservableList<MonthlyVisitVolume> data = fetchMonthlyVisitVolumeData();
        table.setItems(data);

        return table;
    }

    private ObservableList<MonthlyVisitVolume> fetchMonthlyVisitVolumeData() {
        ObservableList<MonthlyVisitVolume> data = FXCollections.observableArrayList();
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_monthly_visit_volume()}");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.add(new MonthlyVisitVolume(
                        rs.getString("month"),
                        rs.getString("visit_count")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void generateMonthlyVisitVolumeReportPDF() {
        ObservableList<MonthlyVisitVolume> data = fetchMonthlyVisitVolumeData();
        String fileName = "Monthly_Visit_Volume_Report.pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Monthly Visit Volume Report"));

            Table table = new Table(2); // 2 columns
            table.addCell("Month");
            table.addCell("Visit Count");

            for (MonthlyVisitVolume report : data) {
                table.addCell(report.getMonth());
                table.addCell(report.getVisitCount());
            }

            document.add(table);
            document.close();

            Desktop.getDesktop().open(new File(fileName));
            System.out.println("PDF generated and opened: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private VBox createYearlyVisitVolumeReport() {
        VBox reportContainer = new VBox(10);
        reportContainer.setAlignment(Pos.TOP_LEFT);
        reportContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

        TableView<YearlyVisitVolume> table = createYearlyVisitVolumeTable();

        Button printButton = new Button("Print as PDF");
        printButton.setOnAction(event -> generateYearlyVisitVolumeReportPDF());

        reportContainer.getChildren().add(new Label("Yearly Visit Volume Report"));
        reportContainer.getChildren().add(table);
        reportContainer.getChildren().add(printButton);

        return reportContainer;
    }

    private TableView<YearlyVisitVolume> createYearlyVisitVolumeTable() {
        TableView<YearlyVisitVolume> table = new TableView<>();

        TableColumn<YearlyVisitVolume, String> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getYear()));

        TableColumn<YearlyVisitVolume, String> visitCountColumn = new TableColumn<>("Visit Count");
        visitCountColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisitCount()));

        table.getColumns().addAll(yearColumn, visitCountColumn);

        ObservableList<YearlyVisitVolume> data = fetchYearlyVisitVolumeData();
        table.setItems(data);

        return table;
    }

    private ObservableList<YearlyVisitVolume> fetchYearlyVisitVolumeData() {
        ObservableList<YearlyVisitVolume> data = FXCollections.observableArrayList();
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_yearly_visit_volume()}");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.add(new YearlyVisitVolume(
                        rs.getString("year"),
                        rs.getString("visit_count")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void generateYearlyVisitVolumeReportPDF() {
        ObservableList<YearlyVisitVolume> data = fetchYearlyVisitVolumeData();
        String fileName = "Yearly_Visit_Volume_Report.pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Yearly Visit Volume Report"));

            Table table = new Table(2); // 2 columns
            table.addCell("Year");
            table.addCell("Visit Count");

            for (YearlyVisitVolume report : data) {
                table.addCell(report.getYear());
                table.addCell(report.getVisitCount());
            }

            document.add(table);
            document.close();

            Desktop.getDesktop().open(new File(fileName));
            System.out.println("PDF generated and opened: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private VBox createAllTimeVisitVolumeReport() {
        VBox reportContainer = new VBox(10);
        reportContainer.setAlignment(Pos.TOP_LEFT);
        reportContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

        TableView<AllTimeVisitVolume> table = createAllTimeVisitVolumeTable();

        Button printButton = new Button("Print as PDF");
        printButton.setOnAction(event -> generateAllTimeVisitVolumeReportPDF());

        reportContainer.getChildren().add(new Label("All-Time Visit Volume Report"));
        reportContainer.getChildren().add(table);
        reportContainer.getChildren().add(printButton);

        return reportContainer;
        }

    private TableView<AllTimeVisitVolume> createAllTimeVisitVolumeTable() {
        TableView<AllTimeVisitVolume> table = new TableView<>();

        TableColumn<AllTimeVisitVolume, String> startDateCol = new TableColumn<>("From");
        startDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStartDate()));

        TableColumn<AllTimeVisitVolume, String> endDateCol = new TableColumn<>("To");
        endDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndDate()));

        TableColumn<AllTimeVisitVolume, String> totalCountCol = new TableColumn<>("Total Visits");
        totalCountCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisitCount()));

        table.getColumns().addAll(startDateCol, endDateCol, totalCountCol);

        ObservableList<AllTimeVisitVolume> data = fetchAllTimeVisitVolumeData();
        table.setItems(data);

        return table;
    }

    private ObservableList<AllTimeVisitVolume> fetchAllTimeVisitVolumeData() {
        ObservableList<AllTimeVisitVolume> data = FXCollections.observableArrayList();
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_alltime_visit_volume()}");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.add(new AllTimeVisitVolume(
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("total_visits")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void generateAllTimeVisitVolumeReportPDF() {
        ObservableList<AllTimeVisitVolume> data = fetchAllTimeVisitVolumeData();
        String fileName = "All_Time_Visit_Volume_Report.pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("All-Time Visit Volume Report"));

            Table table = new Table(3); // 3 columns
            table.addCell("From");
            table.addCell("To");
            table.addCell("Total Visits");

            for (AllTimeVisitVolume report : data) {
                table.addCell(report.getStartDate());
                table.addCell(report.getEndDate());
                table.addCell(report.getVisitCount());
            }

            document.add(table);
            document.close();

            Desktop.getDesktop().open(new File(fileName));
            System.out.println("PDF generated and opened: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private VBox createVisitsByRelationshipReport() {
        VBox reportContainer = new VBox(10);
        reportContainer.setAlignment(Pos.TOP_LEFT);
        reportContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

        TableView<VisitsByRelationship> table = createVisitsByRelationshipTable();

        Button printButton = new Button("Print as PDF");
        printButton.setOnAction(event -> generateVisitsByRelationshipReportPDF());

        reportContainer.getChildren().add(new Label("Visits by Visitor-Inmate Relationship Report"));
        reportContainer.getChildren().add(table);
        reportContainer.getChildren().add(printButton);

        return reportContainer;
    }

    private TableView<VisitsByRelationship> createVisitsByRelationshipTable() {
        TableView<VisitsByRelationship> table = new TableView<>();

        TableColumn<VisitsByRelationship, String> relationshipCol = new TableColumn<>("Relationship");
        relationshipCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRelationship()));

        TableColumn<VisitsByRelationship, String> countCol = new TableColumn<>("Visit Count");
        countCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisitCount()));

        table.getColumns().addAll(relationshipCol, countCol);

        ObservableList<VisitsByRelationship> data = fetchVisitsByRelationshipData();
        table.setItems(data);

        return table;
    }

    private ObservableList<VisitsByRelationship> fetchVisitsByRelationshipData() {
        ObservableList<VisitsByRelationship> data = FXCollections.observableArrayList();
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_visits_by_relationship()}");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.add(new VisitsByRelationship(
                    rs.getString("relationship_to_inmate"),
                    rs.getString("total_visits")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void generateVisitsByRelationshipReportPDF() {
        ObservableList<VisitsByRelationship> data = fetchVisitsByRelationshipData();
        String fileName = "Visits_By_Relationship_Report.pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Visits by Visitor-Inmate Relationship Report"));

            Table table = new Table(2); 
            table.addCell("Relationship");
            table.addCell("Visit Count");

            for (VisitsByRelationship report : data) {
                table.addCell(report.getRelationship());
                table.addCell(report.getVisitCount());
            }

            document.add(table);
            document.close();

            Desktop.getDesktop().open(new File(fileName));
            System.out.println("PDF generated and opened: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private VBox createTopFrequentVisitorsReport() {
        VBox reportContainer = new VBox(10);
        reportContainer.setAlignment(Pos.TOP_LEFT);
        reportContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

        TableView<TopFrequentVisitorReport> table = createTopFrequentVisitorsTable();

        Button printButton = new Button("Print as PDF");
        printButton.setOnAction(e -> generateTopFrequentVisitorsPDF());

        reportContainer.getChildren().add(new Label("Top 3 Frequent Visitors"));
        reportContainer.getChildren().add(table);
        reportContainer.getChildren().add(printButton);

        return reportContainer;
    }

    
    private TableView<TopFrequentVisitorReport> createTopFrequentVisitorsTable() {
        TableView<TopFrequentVisitorReport> table = new TableView<>();

        TableColumn<TopFrequentVisitorReport, String> nameCol = new TableColumn<>("Visitor Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisitorName()));

        TableColumn<TopFrequentVisitorReport, String> countCol = new TableColumn<>("Visit Count");
        countCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisitCount()));

        table.getColumns().addAll(nameCol, countCol);

        ObservableList<TopFrequentVisitorReport> data = fetchTopFrequentVisitorsData();
        table.setItems(data);

        return table;
    }

    
    private ObservableList<TopFrequentVisitorReport> fetchTopFrequentVisitorsData() {
        ObservableList<TopFrequentVisitorReport> data = FXCollections.observableArrayList();

        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_top3_frequent_visitors()}");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.add(new TopFrequentVisitorReport(
                    rs.getString("visitor_name"),
                    rs.getString("visit_count")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    
    private void generateTopFrequentVisitorsPDF() {
        ObservableList<TopFrequentVisitorReport> data = fetchTopFrequentVisitorsData();
        String fileName = "Top_3_Frequent_Visitors_Report.pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Top 3 Frequent Visitors Report"));

            Table table = new Table(2);
            table.addCell("Visitor Name");
            table.addCell("Visit Count");

            for (TopFrequentVisitorReport record : data) {
                table.addCell(record.getVisitorName());
                table.addCell(record.getVisitCount());
            }

            document.add(table);
            document.close();

            Desktop.getDesktop().open(new File(fileName));
            System.out.println("PDF generated and opened: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private VBox createGenderDistributionReport() {
        VBox reportContainer = new VBox(10);
        reportContainer.setAlignment(Pos.TOP_LEFT);
        reportContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

        TableView<GenderDistributionReport> table = createGenderDistributionTable();

        Button printButton = new Button("Print as PDF");
        printButton.setOnAction(e -> generateGenderDistributionPDF());

        reportContainer.getChildren().add(new Label("Gender Distribution Report"));
        reportContainer.getChildren().add(table);
        reportContainer.getChildren().add(printButton);

        return reportContainer;
    }

    
    private TableView<GenderDistributionReport> createGenderDistributionTable() {
        TableView<GenderDistributionReport> table = new TableView<>();

        TableColumn<GenderDistributionReport, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGender()));

        TableColumn<GenderDistributionReport, String> totalCol = new TableColumn<>("Total Inmates");
        totalCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTotal()));

        table.getColumns().addAll(genderCol, totalCol);

        ObservableList<GenderDistributionReport> data = fetchGenderDistributionData();
        table.setItems(data);

        return table;
    }

    
    private ObservableList<GenderDistributionReport> fetchGenderDistributionData() {
        ObservableList<GenderDistributionReport> data = FXCollections.observableArrayList();

        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_gender_distribution()}");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.add(new GenderDistributionReport(
                    rs.getString("gender"),
                    rs.getString("total")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    
    private void generateGenderDistributionPDF() {
        ObservableList<GenderDistributionReport> data = fetchGenderDistributionData();
        String fileName = "Gender_Distribution_Report.pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Gender Distribution Report"));

            Table table = new Table(2);
            table.addCell("Gender");
            table.addCell("Total Inmates");

            for (GenderDistributionReport report : data) {
                table.addCell(report.getGender());
                table.addCell(report.getTotal());
            }

            document.add(table);
            document.close();

            Desktop.getDesktop().open(new File(fileName));
            System.out.println("PDF generated and opened: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private VBox createCellOccupancyReport() {
        VBox reportContainer = new VBox(10);
        reportContainer.setAlignment(Pos.TOP_LEFT);
        reportContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

        TableView<CellOccupancyReport> table = createCellOccupancyTable();

        Button printButton = new Button("Print as PDF");
        printButton.setOnAction(e -> generateCellOccupancyReportPDF());

        reportContainer.getChildren().add(new Label("Cell Occupancy Report"));
        reportContainer.getChildren().add(table);
        reportContainer.getChildren().add(printButton);

        return reportContainer;
    }

    
    private TableView<CellOccupancyReport> createCellOccupancyTable() {
        TableView<CellOccupancyReport> table = new TableView<>();

        TableColumn<CellOccupancyReport, String> cellIdCol = new TableColumn<>("Cell ID");
        cellIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCellId()));

        TableColumn<CellOccupancyReport, String> floorCol = new TableColumn<>("Floor Number");
        floorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFloorNumber()));

        TableColumn<CellOccupancyReport, String> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCapacity()));

        TableColumn<CellOccupancyReport, String> occupiedCol = new TableColumn<>("Occupied");
        occupiedCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOccupiedCount()));

        table.getColumns().addAll(cellIdCol, floorCol, capacityCol, occupiedCol);

        ObservableList<CellOccupancyReport> data = fetchCellOccupancyData();
        table.setItems(data);

        return table;
    }

    
    private ObservableList<CellOccupancyReport> fetchCellOccupancyData() {
        ObservableList<CellOccupancyReport> data = FXCollections.observableArrayList();

        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL get_cell_occupancy()}");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.add(new CellOccupancyReport(
                    rs.getString("cell_id"),
                    rs.getString("floor_number"),
                    rs.getString("capacity"),
                    rs.getString("occupied_count")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    
    private void generateCellOccupancyReportPDF() {
        ObservableList<CellOccupancyReport> data = fetchCellOccupancyData();
        String fileName = "Cell_Occupancy_Report.pdf";

        try {
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Cell Occupancy Report"));

            Table table = new Table(4);
            table.addCell("Cell ID");
            table.addCell("Floor Number");
            table.addCell("Capacity");
            table.addCell("Occupied");

            for (CellOccupancyReport report : data) {
                table.addCell(report.getCellId());
                table.addCell(report.getFloorNumber());
                table.addCell(report.getCapacity());
                table.addCell(report.getOccupiedCount());
            }

            document.add(table);
            document.close();

            Desktop.getDesktop().open(new File(fileName));
            System.out.println("PDF generated and opened: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private VBox createOneTimeVisitorLogsReport() {
    VBox reportContainer = new VBox(10);
    reportContainer.setAlignment(Pos.TOP_LEFT);
    reportContainer.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");

    TableView<OneTimeVisitorLog> table = createOneTimeVisitorLogsTable();

    Button printButton = new Button("Print as PDF");
    printButton.setOnAction(e -> generateOneTimeVisitorLogsReportPDF());

    reportContainer.getChildren().add(new Label("One-Time Visitor Logs Report"));
    reportContainer.getChildren().add(table);
    reportContainer.getChildren().add(printButton);

    return reportContainer;
}

    
    private TableView<OneTimeVisitorLog> createOneTimeVisitorLogsTable() {
    TableView<OneTimeVisitorLog> table = new TableView<>();

    TableColumn<OneTimeVisitorLog, String> dateCol = new TableColumn<>("Visit Date");
    dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisitDate()));

    TableColumn<OneTimeVisitorLog, String> nameCol = new TableColumn<>("Visitor Name");
    nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVisitorName()));

    TableColumn<OneTimeVisitorLog, String> relationshipCol = new TableColumn<>("Relationship to Inmate");
    relationshipCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRelationship()));

    table.getColumns().addAll(dateCol, nameCol, relationshipCol);

    ObservableList<OneTimeVisitorLog> data = fetchOneTimeVisitorLogsData();
    table.setItems(data);

    return table;
}

    
    private ObservableList<OneTimeVisitorLog> fetchOneTimeVisitorLogsData() {
    ObservableList<OneTimeVisitorLog> data = FXCollections.observableArrayList();

    try (Connection conn = DBConnect.getConnection();
         CallableStatement stmt = conn.prepareCall("{CALL get_onetime_visitor_logs()}");
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            data.add(new OneTimeVisitorLog(
                rs.getString("visit_date"),
                rs.getString("visitor_name"),
                rs.getString("relationship_to_inmate")
            ));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return data;
}

    
    private void generateOneTimeVisitorLogsReportPDF() {
    ObservableList<OneTimeVisitorLog> data = fetchOneTimeVisitorLogsData();
    String fileName = "OneTime_Visitor_Logs_Report.pdf";

    try {
        PdfWriter writer = new PdfWriter(fileName);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("One-Time Visitor Logs Report"));

        Table table = new Table(3);
        table.addCell("Visit Date");
        table.addCell("Visitor Name");
        table.addCell("Relationship");

        for (OneTimeVisitorLog log : data) {
            table.addCell(log.getVisitDate());
            table.addCell(log.getVisitorName());
            table.addCell(log.getRelationship());
        }

        document.add(table);
        document.close();

        Desktop.getDesktop().open(new File(fileName));
        System.out.println("PDF generated and opened: " + fileName);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
    


    // Placeholder classes for the reports data
    public static class InmateHealthReport {
        private final String name;
        private final String healthIssue;
        private final String birthDate;
        private final String gender;

        public InmateHealthReport(String name, String healthIssue, String birthDate, String gender) {
            this.name = name;
            this.healthIssue = healthIssue;
            this.birthDate = birthDate;
            this.gender = gender;
        }

        public String getName() {
            return name;
        }

        public String getHealthIssue() {
            return healthIssue;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public String getGender() {
            return gender;
        }
    }

    public static class VisitVolumeReport {
        private final String visitDate;
        private final String visitCount;

        public VisitVolumeReport(String visitDate, String visitCount) {
            this.visitDate = visitDate;
            this.visitCount = visitCount;
        }

        public String getVisitDate() {
            return visitDate;
        }

        public String getVisitCount() {
            return visitCount;
        }
    }

    public static class MonthlyVisitVolume {
        private String month;
        private String visitCount;

        public MonthlyVisitVolume(String month, String visitCount) {
            this.month = month;
            this.visitCount = visitCount;
        }

        public String getMonth() {
            return month;
        }

        public String getVisitCount() {
            return visitCount;
        }
    }
    
    
    public class YearlyVisitVolume {
        private String year;
        private String visitCount;

        public YearlyVisitVolume(String year, String visitCount) {
            this.year = year;
            this.visitCount = visitCount;
        }

        public String getYear() {
            return year;
        }

        public String getVisitCount() {
            return visitCount;
        }
    }
    
    public class AllTimeVisitVolume {
        private String startDate;
        private String endDate;
        private String visitCount;

        public AllTimeVisitVolume(String startDate, String endDate, String visitCount) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.visitCount = visitCount;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getVisitCount() {
            return visitCount;
        }
    }
    
    
    public class VisitsByRelationship {
        private String relationship;
        private String visitCount;

        public VisitsByRelationship(String relationship, String visitCount) {
            this.relationship = relationship;
            this.visitCount = visitCount;
        }

        public String getRelationship() {
            return relationship;
        }

        public String getVisitCount() {
            return visitCount;
        }
    }

    
    public class TopFrequentVisitorReport {
        private final String visitorName;
        private final String visitCount;

        public TopFrequentVisitorReport(String visitorName, String visitCount) {
            this.visitorName = visitorName;
            this.visitCount = visitCount;
        }

        public String getVisitorName() {
            return visitorName;
        }

        public String getVisitCount() {
            return visitCount;
        }
    }

    
    public class GenderDistributionReport {
        private final String gender;
        private final String total;

        public GenderDistributionReport(String gender, String total) {
            this.gender = gender;
            this.total = total;
        }

        public String getGender() {
            return gender;
        }

        public String getTotal() {
            return total;
        }
    }


    public class CellOccupancyReport {
        private final String cellId;
        private final String floorNumber;
        private final String capacity;
        private final String occupiedCount;

        public CellOccupancyReport(String cellId, String floorNumber, String capacity, String occupiedCount) {
            this.cellId = cellId;
            this.floorNumber = floorNumber;
            this.capacity = capacity;
            this.occupiedCount = occupiedCount;
        }

        public String getCellId() {
            return cellId;
        }

        public String getFloorNumber() {
            return floorNumber;
        }

        public String getCapacity() {
            return capacity;
        }

        public String getOccupiedCount() {
            return occupiedCount;
        }
    }

    
    public class OneTimeVisitorLog {
    private final String visitDate;
    private final String visitorName;
    private final String relationship;

    public OneTimeVisitorLog(String visitDate, String visitorName, String relationship) {
        this.visitDate = visitDate;
        this.visitorName = visitorName;
        this.relationship = relationship;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public String getRelationship() {
        return relationship;
    }
}


}
