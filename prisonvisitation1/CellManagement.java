package prisonvisitation1;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;

public class CellManagement extends Application {

    private TableView<Cell> table = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        HBox navbar = DashboardComponents.createNavbar(stage, "Officer Dashboard");
        VBox sidebar = createSidebar(stage);
        VBox mainContent = createMainContent();
        HBox layout = new HBox(20, sidebar, mainContent);
        layout.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setTop(navbar);
        root.setCenter(layout);

        Scene scene = new Scene(root, 1000, 800, Color.LIGHTGRAY);
        stage.setTitle("Cells Management");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private VBox createMainContent() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(1200);
        box.setStyle("-fx-background-color: white; -fx-padding: 15px;");

        Label title = new Label("Prison Cells");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableColumn<Cell, String> idCol = new TableColumn<>("Cell ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("cellId"));

        TableColumn<Cell, Integer> floorCol = new TableColumn<>("Floor");
        floorCol.setCellValueFactory(new PropertyValueFactory<>("floorNumber"));

        TableColumn<Cell, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<Cell, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Cell, Integer> occupiedCol = new TableColumn<>("Occupied");
        occupiedCol.setCellValueFactory(new PropertyValueFactory<>("occupiedCount"));

        TableColumn<Cell, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.setOnAction(event -> {
                    Cell c = getTableView().getItems().get(getIndex());
                    showEditCellDialog(c);
                });

                deleteBtn.setOnAction(event -> {
                    Cell c = getTableView().getItems().get(getIndex());
                    deleteCell(c.getCellId());
                    refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(10, editBtn, deleteBtn));
                }
            }
        });

        table.getColumns().addAll(idCol, floorCol, capacityCol, typeCol, occupiedCol, actionCol);
        refreshTable();

        box.getChildren().addAll(title, table);
        return box;
    }

    private void refreshTable() {
        ObservableList<Cell> data = FXCollections.observableArrayList();
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL GetAllCells()}")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Cell(
                        rs.getString("cell_id"),
                        rs.getInt("floor_number"),
                        rs.getInt("capacity"),
                        rs.getString("type"),
                        rs.getInt("occupied_count")
                ));
            }
            table.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showEditCellDialog(Cell c) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Cell");
        dialog.setHeaderText("Edit Cell ID: " + c.getCellId());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField floorField = new TextField(String.valueOf(c.getFloorNumber()));
        TextField capacityField = new TextField(String.valueOf(c.getCapacity()));
        TextField typeField = new TextField(c.getType());

        grid.add(new Label("Floor:"), 0, 0);
        grid.add(floorField, 1, 0);
        grid.add(new Label("Capacity:"), 0, 1);
        grid.add(capacityField, 1, 1);
        grid.add(new Label("Type:"), 0, 2);
        grid.add(typeField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                updateCell(
                        c.getCellId(),
                        Integer.parseInt(floorField.getText()),
                        Integer.parseInt(capacityField.getText()),
                        typeField.getText()
                );
                refreshTable();
            }
        });
    }

    private void updateCell(String cellId, int floorNumber, int capacity, String type) {
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL UpdateCell(?, ?, ?, ?)}")) {
            stmt.setString(1, cellId);
            stmt.setInt(2, floorNumber);
            stmt.setInt(3, capacity);
            stmt.setString(4, type);
            stmt.execute();
            System.out.println("Cell updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteCell(String cellId) {
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL DeleteCell(?)}")) {
            stmt.setString(1, cellId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Cell deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  private VBox createSidebar(Stage stage) {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        Button[] buttons = {
            createNavButton("👁️ View Visit Requests", new VisitRequests(), stage),
            createNavButton("👥 Manage Visitors", new VisitorsManagement(), stage),
            createNavButton("🧍‍♂️ Manage Inmates", new InmateManagement(), stage),
            createNavButton("👮 Manage Officers", new OfficerManagement(), stage),
            createNavButton("🏢 Manage Cells", new CellManagement(), stage),
            createNavButton("📄 Reports", new VisitorsManagement(), stage),
            createNavButton("🚪 Logout", new LandingPage(), stage)
        };

        setButtonStyles(buttons);
        sidebar.getChildren().addAll(buttons);
        return sidebar;
    }

    private Button createNavButton(String label, Application targetApp, Stage currentStage) {
        Button btn = new Button(label);
        btn.setPrefWidth(250);
        btn.setPrefHeight(50);
        btn.setOnAction(e -> {
            try {
                targetApp.start(new Stage());
                currentStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return btn;
    }

    private void setButtonStyles(Button... buttons) {
        for (Button button : buttons) {
            button.setStyle("-fx-font-size: 14px; -fx-background-color: #34495e; -fx-text-fill: white;");
        }
    }

    private void switchTo(Stage currentStage, Application nextPage) {
        try {
            nextPage.start(new Stage());
            currentStage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static class Cell {
        private final SimpleStringProperty cellId;
        private final SimpleIntegerProperty floorNumber;
        private final SimpleIntegerProperty capacity;
        private final SimpleStringProperty type;
        private final SimpleIntegerProperty occupiedCount;

        public Cell(String cellId, int floorNumber, int capacity, String type, int occupiedCount) {
            this.cellId = new SimpleStringProperty(cellId);
            this.floorNumber = new SimpleIntegerProperty(floorNumber);
            this.capacity = new SimpleIntegerProperty(capacity);
            this.type = new SimpleStringProperty(type);
            this.occupiedCount = new SimpleIntegerProperty(occupiedCount);
        }

        public String getCellId() { return cellId.get(); }
        public int getFloorNumber() { return floorNumber.get(); }
        public int getCapacity() { return capacity.get(); }
        public String getType() { return type.get(); }
        public int getOccupiedCount() { return occupiedCount.get(); }
    }
}
