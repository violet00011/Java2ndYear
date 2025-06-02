package prisonvisitation1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardComponents {

    public static HBox createNavbar(Stage stage, String titleText) {
        HBox navbar = new HBox();
        navbar.setPadding(new Insets(10));
        navbar.setStyle("-fx-background-color: #2f3542;");
        navbar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Hyperlink logout = new Hyperlink("Logout");
        logout.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        logout.setOnAction(e -> {
            stage.close();
            System.out.println("Logging out...");
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navbar.getChildren().addAll(title, spacer, logout);
        return navbar;
    }

    public static VBox createSidebar(Stage stage) {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50; -fx-pref-width: 300px;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        Button viewRequestsBtn = new Button("👁️ View Visit Requests");
        Button manageVisitorsBtn = new Button("👥 Manage Visitors");
        Button manageInmatesBtn = new Button("🧍‍♂️ Manage Inmates");
        Button manageOfficersBtn = new Button("👮 Manage Officers");
        Button manageCellsBtn = new Button("🏢 Manage Cells");
        Button reportsBtn = new Button("📄 Reports");
        Button logoutBtn = new Button("🚪 Logout");

        viewRequestsBtn.setPrefWidth(250);
        manageVisitorsBtn.setPrefWidth(250);
        manageInmatesBtn.setPrefWidth(250);
        manageOfficersBtn.setPrefWidth(250);
        manageCellsBtn.setPrefWidth(250);
        reportsBtn.setPrefWidth(250);
        logoutBtn.setPrefWidth(250);

        logoutBtn.setOnAction(e -> {
            stage.close();
            System.out.println("Logging out...");
        });

        sidebar.getChildren().addAll(
                viewRequestsBtn, manageVisitorsBtn, manageInmatesBtn,
                manageOfficersBtn, manageCellsBtn, reportsBtn, logoutBtn
        );

        return sidebar;
    }
}
