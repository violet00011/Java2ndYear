package prisonvisitation1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OfficerLogIn extends Application {

    // Static variable to store the logged-in officer's ID
    public static int officerId = -1;

    @Override
    public void start(Stage stage) {
        // Navigation Bar
        Label titleLeft = new Label("Malolos Provincial Jail");
        titleLeft.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label homeLink = createHyperlink("Home", () -> switchTo(stage, new LandingPage()));
        Label aboutUsLink = createHyperlink("About Us", () -> switchTo(stage, new AboutUs()));
        Label contactUsLink = createHyperlink("Contact Us", () -> switchTo(stage, new ContactUS()));
        Label developersLink = createHyperlink("Developers", () -> switchTo(stage, new Developers()));

        HBox linkBox = new HBox(25, homeLink, aboutUsLink, contactUsLink, developersLink);
        linkBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navBar = new HBox(20, titleLeft, spacer, linkBox);
        navBar.setPadding(new Insets(15));
        navBar.setStyle("-fx-background-color: #2F4F4F;");
        navBar.setAlignment(Pos.CENTER_LEFT);

        // Login Form Container
        Label loginTitle = new Label("Police Officer Login");
        loginTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        TextField usernameField = new TextField(); // Changed this to usernameField
        usernameField.setPromptText("Username");
        usernameField.setPrefWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(250);

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        Button loginButton = new Button("Login");
        Button backButton = new Button("Back");
        loginButton.setMinWidth(100);
        backButton.setMinWidth(100);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText(); // Get the username
            String password = passwordField.getText(); // Get the entered password

            if (validateLogin(username, password)) { // Check against DB
                statusLabel.setText("✅ Login successful!");
                statusLabel.setStyle("-fx-text-fill: green;");
                stage.close();
                try {
                    new OfficerDashboard().start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                statusLabel.setText("❌ Invalid username or password.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        backButton.setOnAction(e -> switchTo(stage, new LandingPage()));

        HBox buttonRow = new HBox(10, loginButton, backButton);
        buttonRow.setAlignment(Pos.CENTER);

        VBox formContainer = new VBox(15, loginTitle, usernameField, passwordField, buttonRow, statusLabel);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle("-fx-background-color: white; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 5);");

        StackPane outerContainer = new StackPane(formContainer);
        outerContainer.setPadding(new Insets(20));
        outerContainer.setPrefSize(384, 576); // 4x6 inch feel

        BorderPane root = new BorderPane();
        root.setTop(navBar);
        root.setCenter(outerContainer);
        BorderPane.setAlignment(formContainer, Pos.CENTER);

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Police Officer Login");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        formContainer.setMaxWidth(450);
        formContainer.setMaxHeight(350);
    }

    private Label createHyperlink(String text, Runnable action) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: lightblue; -fx-underline: true; -fx-cursor: hand;");
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> action.run());
        return label;
    }

    private void switchTo(Stage stage, Application page) {
        try {
            page.start(new Stage());
            stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Method to validate login against the database
    private boolean validateLogin(String username, String password) {
        try {
            Connection conn = DBConnect.getConnection();
            String query = "SELECT officer_id, password_hash FROM POLICE_OFFICER WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("password_hash");

                    // Directly compare stored password and entered password
                    if (storedPassword.equals(password)) {
                        // Save the officer_id in the session (static variable)
                        officerId = rs.getInt("officer_id");
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
