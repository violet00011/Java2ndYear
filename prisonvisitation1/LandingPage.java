package prisonvisitation1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;

public class LandingPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Left Title
        Label title = new Label("Malolos Provincial Jail");
        title.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Text links (styled as hyperlinks)
        Label homeLink = createHyperlink("Home", () -> {
            try {
                new LandingPage().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Label aboutUsLink = createHyperlink("About Us", () -> {
            try {
                new AboutUs().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Label contactUsLink = createHyperlink("Contact Us", () -> {
            try {
                new ContactUS().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Label developersLink = createHyperlink("Developers", () -> {
            try {
                new Developers().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Right side HBox for links
        HBox linkBox = new HBox(40, homeLink, aboutUsLink, contactUsLink, developersLink);
        linkBox.setAlignment(Pos.CENTER_RIGHT);
        linkBox.setMinWidth(400);  // Set minimum width so the items don't get compressed


        // Navigation bar HBox
        HBox navBar = new HBox();
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: #2F4F4F;");
        navBar.setSpacing(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPrefHeight(60);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navBar.getChildren().addAll(title, spacer, linkBox);

        // Main content
        Label systemTitle = new Label("Prison Visitation System");
        systemTitle.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");

        javafx.scene.control.Button visitorLoginBtn = new javafx.scene.control.Button("Visitor Login");
        visitorLoginBtn.setStyle("-fx-font-size: 20px;");
        javafx.scene.control.Button officerLoginBtn = new javafx.scene.control.Button("Police Officer Login");
        officerLoginBtn.setStyle("-fx-font-size: 20px;");
        javafx.scene.control.Button scheduleVisitBtn = new javafx.scene.control.Button("Schedule a Visit");
        scheduleVisitBtn.setStyle("-fx-font-size: 20px;");

        visitorLoginBtn.setMinWidth(250);
        officerLoginBtn.setMinWidth(250);
        scheduleVisitBtn.setMinWidth(250);

        visitorLoginBtn.setOnAction(e -> {
            try {
                new VisitorLogIn().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        officerLoginBtn.setOnAction(e -> {
            try {
                new OfficerLogIn().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        scheduleVisitBtn.setOnAction(e -> {
            try {
                new ScheduleLogIn().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox contentLayout = new VBox(30, systemTitle, visitorLoginBtn, officerLoginBtn, scheduleVisitBtn);
        contentLayout.setAlignment(Pos.CENTER);

        // Final layout
        BorderPane root = new BorderPane();
        root.setTop(navBar);
        root.setCenter(contentLayout);

        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Landing Page");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private Label createHyperlink(String text, Runnable action) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: lightblue; -fx-cursor: hand;");
        label.setMinWidth(Region.USE_PREF_SIZE); // prevents shrinking
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> action.run());
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
