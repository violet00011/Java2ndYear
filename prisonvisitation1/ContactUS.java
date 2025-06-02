package prisonvisitation1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class ContactUS extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a WebView to display the map
        WebView webView = new WebView();
        webView.getEngine().load("https://leaflet-extras.github.io/leaflet-providers/preview/#openstreetmap");

        // Create a label for contact information
        Label contactInfo = new Label("Contact Information:\n" +
                "Name: Malolos City Jail\n" +
                "Address: Barangay Sto. Niño, Old City Hall, Malolos City, Bulacan, Philippines\n" +
                "Phone: 0943 512 4675\n" +
                "Email: pcsjmo@bulacan.gov.ph");

        // Create a layout and add the WebView and label
        StackPane layout = new StackPane();
        layout.getChildren().addAll(webView, contactInfo);

        // Create a scene and set it on the stage
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Contact Us - Malolos City Jail");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the application
        launch(args);
    }
}
