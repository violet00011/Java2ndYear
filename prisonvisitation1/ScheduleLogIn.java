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
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;


public class ScheduleLogIn extends Application {

    @Override
    public void start(Stage stage) {
        // Top Navigation Bar
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

        // Form Title
        Label formTitle = new Label("One-Time Visit Form");
        formTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        // Full Name Fields
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField middleNameField = new TextField();
        middleNameField.setPromptText("Middle Name");

        HBox nameRow = new HBox(10, lastNameField, firstNameField, middleNameField);
        nameRow.setAlignment(Pos.CENTER);

        // Gender
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female");
        genderCombo.setPromptText("Gender");

        // Relationship
        ComboBox<String> relationshipCombo = new ComboBox<>();
        relationshipCombo.getItems().addAll("Parent", "Child", "Sibling", "Grandparent", "Friend", "Legal Adviser", "Partner", "Spouse", "Cousin");
        relationshipCombo.setPromptText("Relationship to Inmate");

        TextField otherRelationshipField = new TextField();
        otherRelationshipField.setPromptText("Please specify");
        otherRelationshipField.setVisible(false);

        relationshipCombo.setOnAction(e -> {
            if ("Others".equals(relationshipCombo.getValue())) {
                otherRelationshipField.setVisible(true);
            } else {
                otherRelationshipField.setVisible(false);
            }
        });

        // Address
        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        // Contact Number
        TextField contactField = new TextField();
        contactField.setPromptText("Contact Number");

        // Purpose
        TextArea purposeField = new TextArea();
        purposeField.setPromptText("Purpose of Visit");
        purposeField.setPrefRowCount(2);

        // Date and Time in one row
        DatePicker visitDate = new DatePicker();

        // Time Spinner Setup (HH:MM:SS AM/PM)
        Spinner<Integer> hourSpinner = new Spinner<>(1, 24, 24); // Hours from 1 to 12
        Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, 0); // Minutes from 0 to 59
        Spinner<Integer> secondSpinner = new Spinner<>(0, 59, 0); // Seconds from 0 to 59

        // Combining into one time row (with seconds)
        HBox timeRow = new HBox(10, hourSpinner, new Label(":"), minuteSpinner, new Label(":"), secondSpinner);
        timeRow.setAlignment(Pos.CENTER);

        // To get the time in HH:MM:SS AM/PM format:
        String time = String.format("%02d:%02d:%02d", hourSpinner.getValue(), minuteSpinner.getValue(), secondSpinner.getValue());

        // Inmate Name (TextField now)
        TextField inmateField = new TextField();
        inmateField.setPromptText("Inmate ID");

        Label statusLabel = new Label();

        // Buttons
        Button submitBtn = new Button("Submit");
        Button backBtn = new Button("Back");
        submitBtn.setMinWidth(100);
        backBtn.setMinWidth(100);

submitBtn.setOnAction(e -> { 
    if (lastNameField.getText().isEmpty() || firstNameField.getText().isEmpty() || middleNameField.getText().isEmpty()
            || genderCombo.getValue() == null || relationshipCombo.getValue() == null
            || ("Others".equals(relationshipCombo.getValue()) && otherRelationshipField.getText().isEmpty())
            || addressField.getText().isEmpty() || contactField.getText().isEmpty()
            || purposeField.getText().isEmpty() || visitDate.getValue() == null
            || inmateField.getText().isEmpty()) {

        statusLabel.setText("❌ Please fill in all fields.");
        statusLabel.setStyle("-fx-text-fill: red;");
        return;
    }

    int inmateId;
    try {
        inmateId = Integer.parseInt(inmateField.getText().trim());
    } catch (NumberFormatException ex) {
        statusLabel.setText("❌ Not Valid Inmate ID.");
        statusLabel.setStyle("-fx-text-fill: red;");
        return;
    }

    try (Connection conn = DBConnect.getConnection()) {
        PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM INMATE WHERE inmate_id = ?");
        checkStmt.setInt(1, inmateId);
        ResultSet rs = checkStmt.executeQuery();

        if (!rs.next()) {
            statusLabel.setText("❌ Inmate Does Not Exist.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

String inmateName = rs.getString("last_name") + ", " + rs.getString("first_name") + " " + rs.getString("middle_name");

        String formattedTime = String.format("%02d:%02d:%02d", 
            hourSpinner.getValue(), minuteSpinner.getValue(), secondSpinner.getValue());

        CallableStatement statement = conn.prepareCall("{CALL InsertOneTimeVisitor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        statement.setString(1, lastNameField.getText());
        statement.setString(2, firstNameField.getText());
        statement.setString(3, middleNameField.getText());
        statement.setString(4, genderCombo.getValue());
        statement.setString(5, relationshipCombo.getValue());
        statement.setString(6, addressField.getText());
        statement.setString(7, contactField.getText());
        statement.setInt(8, inmateId);
        statement.setString(9, purposeField.getText());
        statement.setDate(10, java.sql.Date.valueOf(visitDate.getValue()));
        statement.setString(11, formattedTime);

        statement.executeUpdate();

        statusLabel.setText("✅ Visit scheduled successfully!");
        statusLabel.setStyle("-fx-text-fill: green;");

        // REDIRECT to ConfirmationSchedule.java window
        ConfirmationSchedule.display(
            String.valueOf(inmateId),
            inmateName,
            lastNameField.getText(),
            firstNameField.getText(),
            middleNameField.getText(),
            genderCombo.getValue(),
            relationshipCombo.getValue(),
            addressField.getText(),
            contactField.getText(),
            "GeneratedAfterInsert", 
            purposeField.getText(),
            visitDate.getValue().toString(),
            formattedTime,
            "Pending",
            java.time.LocalDateTime.now().toString()
        );

    } catch (SQLException ex) {
        ex.printStackTrace();
        statusLabel.setText("❌ Error: Could not schedule visit.");
        statusLabel.setStyle("-fx-text-fill: red;");
    }
});

        backBtn.setOnAction(e -> switchTo(stage, new LandingPage()));

        HBox buttonRow = new HBox(10, submitBtn, backBtn);
        buttonRow.setAlignment(Pos.CENTER);

        // Form Layout
        VBox formContainer = new VBox(15,
                formTitle,
                nameRow,
                genderCombo,
                relationshipCombo,
                otherRelationshipField,
                addressField,
                contactField,
                purposeField,
                visitDate,
                timeRow,
                inmateField,
                buttonRow,
                statusLabel
        );
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle("-fx-background-color: white; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        formContainer.setMaxWidth(450);
        formContainer.setMaxHeight(1000);

        StackPane centerPane = new StackPane(formContainer);
        centerPane.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setTop(navBar);
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Schedule a One-Time Visit");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private Label createHyperlink(String text, Runnable action) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: lightblue; -fx-underline: true; -fx-cursor: hand;");
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> action.run());
        return label;
    }

    private void switchTo(Stage stage, Application app) {
        try {
            app.start(new Stage());
            stage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}