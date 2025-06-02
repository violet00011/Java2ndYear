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

public class VisitorLogIn extends Application {

    @Override
    public void start(Stage stage) {
        // --- Top Navigation Bar ---
        Label jailTitle = new Label("Malolos Provincial Jail");
        jailTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label homeLink = createNavLink("Home", () -> switchPage(stage, new LandingPage()));
        Label aboutLink = createNavLink("About Us", () -> switchPage(stage, new AboutUs()));
        Label contactLink = createNavLink("Contact Us", () -> switchPage(stage, new ContactUS()));
        Label devsLink = createNavLink("Developers", () -> switchPage(stage, new Developers()));

        HBox navLinks = new HBox(25, homeLink, aboutLink, contactLink, devsLink);
        navLinks.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navBar = new HBox(20, jailTitle, spacer, navLinks);
        navBar.setPadding(new Insets(15));
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setStyle("-fx-background-color: #2F4F4F;");

        // --- Login Form Section ---
        Label loginLabel = new Label("Visitor Login");
        loginLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email or Username");
        emailField.setPrefWidth(250);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setPrefWidth(250);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("Login");
        Button backBtn = new Button("Back");
        loginBtn.setMinWidth(100);
        backBtn.setMinWidth(100);

        loginBtn.setOnAction(e -> {
            String email = emailField.getText();
            String password = passField.getText();

            if (authenticateUser(email, password)) {
                stage.close();
                try {
                    new VisitorDashboard().start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                errorLabel.setText("❌ Invalid email/username or password.");
            }
        });

        backBtn.setOnAction(e -> switchPage(stage, new LandingPage()));

        HBox buttons = new HBox(10, loginBtn, backBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox loginForm = new VBox(15, loginLabel, emailField, passField, buttons, errorLabel);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setPadding(new Insets(30));
        loginForm.setStyle("""
            -fx-background-color: white;
            -fx-border-radius: 10px;
            -fx-background-radius: 10px;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);
        """);

        StackPane centerPane = new StackPane(loginForm);
        centerPane.setPadding(new Insets(20));
        centerPane.setPrefSize(384, 576);

        BorderPane root = new BorderPane();
        root.setTop(navBar);
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Visitor Login");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        loginForm.setMaxWidth(450);
        loginForm.setMaxHeight(350);
    }

    private Label createNavLink(String text, Runnable action) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: lightblue; -fx-underline: true; -fx-cursor: hand;");
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> action.run());
        return label;
    }

    private void switchPage(Stage current, Application nextPage) {
        try {
            nextPage.start(new Stage());
            current.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean authenticateUser(String usernameOrEmail, String password) {
        try (Connection conn = DBConnect.getConnection()) {
            String sql = "SELECT * FROM VISITOR WHERE username = ? AND password_hash = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usernameOrEmail);
                stmt.setString(2, password);  

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
