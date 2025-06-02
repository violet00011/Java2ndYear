package PRISON_VISITATION;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AboutUs extends Application {
    @Override
    public void start(Stage primaryStage) {
        // ───── NAVBAR ─────
        Label title = new Label("Malolos Provincial Jail");
        title.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label homeLink = createHyperlink("Home", () -> switchScene(new LandingPage(), primaryStage));
        Label aboutUsLink = createHyperlink("About Us", () -> switchScene(new AboutUs(), primaryStage));
        Label contactUsLink = createHyperlink("Contact Us", () -> switchScene(new ContactUS(), primaryStage));
        Label developersLink = createHyperlink("Developers", () -> switchScene(new Developers(), primaryStage));

        HBox navLinks = new HBox(30, homeLink, aboutUsLink, contactUsLink, developersLink);
        navLinks.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navbar = new HBox(30, title, spacer, navLinks);
        navbar.setPadding(new Insets(15));
        navbar.setStyle("-fx-background-color: #2f3542;");
        navbar.setAlignment(Pos.CENTER_LEFT);

        // ───── CONTENT ─────
        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(30));
        contentBox.setMaxWidth(800);
        contentBox.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 10, 0, 0, 5); -fx-background-radius: 10;");
        contentBox.setAlignment(Pos.CENTER); // Center content vertically and horizontally

        Label infoLabel = new Label(
            "About Malolos City Jail\n\n" +
            "Location: Barangay Sto. Niño, Old City Hall, Malolos City, Bulacan\n" +
            "Contact Number: 0969-576-2122\n" +
            "Email: maloloscj@r3.bjmp.gov.ph\n\n" 
           
        );
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-font-size: 14px;");

        // Load the image from the local path
        Image mapImage = new Image("file:/C:/Users/villa/Downloads/Project_Assets/BJMPMAP.png");
        ImageView mapView = new ImageView(mapImage);
        mapView.setFitWidth(600); // Adjust width as needed
        mapView.setPreserveRatio(true);
        mapView.setSmooth(true);

        VBox.setMargin(mapView, new Insets(20, 0, 0, 0));


        contentBox.getChildren().addAll(infoLabel, mapView);

        StackPane centeredContainer = new StackPane(contentBox);
        centeredContainer.setAlignment(Pos.CENTER); // Center content
        centeredContainer.setPadding(new Insets(40));
        centeredContainer.setStyle("-fx-background-color: #f0f0f0;");

        VBox root = new VBox(navbar, centeredContainer);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("About Malolos City Jail");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private Label createHyperlink(String text, Runnable action) {
        Label link = new Label(text);
        link.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        link.setOnMouseEntered(e -> link.setStyle("-fx-text-fill: #70a1ff; -fx-font-size: 16px;"));
        link.setOnMouseExited(e -> link.setStyle("-fx-text-fill: white; -fx-font-size: 16px;"));
        link.setOnMouseClicked(e -> action.run());
        return link;
    }

    private void switchScene(Application app, Stage oldStage) {
        try {
            app.start(new Stage());
            oldStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
