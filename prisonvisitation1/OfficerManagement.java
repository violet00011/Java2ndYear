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

public class OfficerManagement extends Application {

    private TableView<Officer> table = new TableView<>();
    private ObservableList<Officer> masterData = FXCollections.observableArrayList();
    private TextField searchField;

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
        stage.setTitle("Officer Management");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        refreshTable(); // Load initial data
    }

    private VBox createMainContent() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setPrefWidth(1200);
        box.setStyle("-fx-background-color: white; -fx-padding: 15px;");

        Label title = new Label("Police Officers");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox searchBox = createSearchBox();

        TableColumn<Officer, Integer> idCol = new TableColumn<>("Officer ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("officerId"));

        TableColumn<Officer, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Officer, String> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));

        TableColumn<Officer, String> badgeCol = new TableColumn<>("Badge Number");
        badgeCol.setCellValueFactory(new PropertyValueFactory<>("badgeNumber"));

        TableColumn<Officer, String> contactCol = new TableColumn<>("Contact Number");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        TableColumn<Officer, String> assignedCellCol = new TableColumn<>("Assigned Cell");
        assignedCellCol.setCellValueFactory(new PropertyValueFactory<>("assignedCellId"));

        TableColumn<Officer, String> adminAccessCol = new TableColumn<>("Admin Access");
        adminAccessCol.setCellValueFactory(new PropertyValueFactory<>("accessAdmin"));

        TableColumn<Officer, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.setOnAction(event -> {
                    Officer officer = getTableView().getItems().get(getIndex());
                    showEditOfficerDialog(officer);
                });

                deleteBtn.setOnAction(event -> {
                    Officer officer = getTableView().getItems().get(getIndex());
                    deleteOfficer(officer.getOfficerId());
                    refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actionBox = new HBox(10, editBtn, deleteBtn);
                    setGraphic(actionBox);
                }
            }
        });

        table.getColumns().addAll(idCol, nameCol, rankCol, badgeCol, contactCol, assignedCellCol, adminAccessCol, actionCol);
        table.setItems(masterData);

        box.getChildren().addAll(title, searchBox, table);
        return box;
    }

    private HBox createSearchBox() {
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10, 0, 10, 0));
        searchBox.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Search officers by ID, name, rank, or badge...");
        searchField.setPrefWidth(400);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> performSearch());

        Button searchButton = new Button("🔍");
        searchButton.setOnAction(e -> performSearch());

        searchBox.getChildren().addAll(searchField, searchButton);
        return searchBox;
    }

    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        ObservableList<Officer> filteredList = FXCollections.observableArrayList();

        for (Officer officer : masterData) {
            if (String.valueOf(officer.getOfficerId()).contains(searchText) ||
                officer.getFullName().toLowerCase().contains(searchText) ||
                officer.getRank().toLowerCase().contains(searchText) ||
                officer.getBadgeNumber().toLowerCase().contains(searchText)) {
                filteredList.add(officer);
            }
        }

        table.setItems(filteredList);
    }

    private void refreshTable() {
        masterData.clear();
        try (Connection conn = DBConnect.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL GetAllOfficers()}")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fullName = rs.getString("last_name") + ", " + rs.getString("first_name");
                masterData.add(new Officer(
                        rs.getInt("officer_id"),
                        fullName,
                        rs.getString("rank"),
                        rs.getString("badge_number"),
                        rs.getString("contact_number"),
                        rs.getString("assigned_cell_id"),
                        rs.getBoolean("access_admin")
                ));
            }

            if (searchField != null && !searchField.getText().isEmpty()) {
                performSearch();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showEditOfficerDialog(Officer officer) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Officer");
        dialog.setHeaderText("Edit Officer ID: " + officer.getOfficerId());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField rankField = new TextField(officer.getRank());
        TextField badgeField = new TextField(officer.getBadgeNumber());
        TextField contactField = new TextField(officer.getContactNumber());
        TextField assignedCellField = new TextField(officer.getAssignedCellId());
        CheckBox adminAccessCheck = new CheckBox("Admin Access");
        adminAccessCheck.setSelected(officer.isAccessAdmin());

        grid.add(new Label("Rank:"), 0, 0);
        grid.add(rankField, 1, 0);
        grid.add(new Label("Badge Number:"), 0, 1);
        grid.add(badgeField, 1, 1);
        grid.add(new Label("Contact Number:"), 0, 2);
        grid.add(contactField, 1, 2);
        grid.add(new Label("Assigned Cell ID:"), 0, 3);
        grid.add(assignedCellField, 1, 3);
        grid.add(adminAccessCheck, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                updateOfficer(officer.getOfficerId(), rankField.getText(), badgeField.getText(),
                        contactField.getText(), assignedCellField.getText(), adminAccessCheck.isSelected());
                return ButtonType.OK;
            }
            return null;
        });

        dialog.showAndWait();
        refreshTable();
    }

    private void updateOfficer(int officerId, String rank, String badgeNumber, String contactNumber,
                               String assignedCellId, boolean accessAdmin) {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE POLICE_OFFICER SET rank = ?, badge_number = ?, contact_number = ?, " +
                             "assigned_cell_id = ?, access_admin = ? WHERE officer_id = ?")) {

            pstmt.setString(1, rank);
            pstmt.setString(2, badgeNumber);
            pstmt.setString(3, contactNumber);
            pstmt.setString(4, assignedCellId);
            pstmt.setBoolean(5, accessAdmin);
            pstmt.setInt(6, officerId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteOfficer(int officerId) {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM POLICE_OFFICER WHERE officer_id = ?")) {
            pstmt.setInt(1, officerId);
            pstmt.executeUpdate();
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

    public static class Officer {
        private final SimpleIntegerProperty officerId;
        private final SimpleStringProperty fullName;
        private final SimpleStringProperty rank;
        private final SimpleStringProperty badgeNumber;
        private final SimpleStringProperty contactNumber;
        private final SimpleStringProperty assignedCellId;
        private final boolean accessAdmin;

        public Officer(int officerId, String fullName, String rank, String badgeNumber,
                       String contactNumber, String assignedCellId, boolean accessAdmin) {
            this.officerId = new SimpleIntegerProperty(officerId);
            this.fullName = new SimpleStringProperty(fullName);
            this.rank = new SimpleStringProperty(rank);
            this.badgeNumber = new SimpleStringProperty(badgeNumber);
            this.contactNumber = new SimpleStringProperty(contactNumber);
            this.assignedCellId = new SimpleStringProperty(assignedCellId);
            this.accessAdmin = accessAdmin;
        }

        public int getOfficerId() { return officerId.get(); }
        public String getFullName() { return fullName.get(); }
        public String getRank() { return rank.get(); }
        public String getBadgeNumber() { return badgeNumber.get(); }
        public String getContactNumber() { return contactNumber.get(); }
        public String getAssignedCellId() { return assignedCellId.get(); }
        public boolean isAccessAdmin() { return accessAdmin; }
    }
}
