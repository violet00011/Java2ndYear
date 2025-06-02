package prisonvisitation1;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

public class ConfirmationSchedule {

    public static void display(
        String inmateID, String inmateName,
        String last, String first, String middle,
        String gender, String relationship,
        String address, String contact,
        String visitorID, String purpose,
        String date, String time, String status, String created
    ) {
        Stage window = new Stage();
        window.setTitle("Visit Confirmation");

        // Include emojis and add a friendly message
        String info = String.format(
            "✅ The visit is scheduled and will be reviewed by police officers.\nA text message will be sent.\n\n" +
            "👤 Inmate Visiting: %s (%s)\n" +
            "👥 Visitor Full Name: %s %s %s\n" +
            "💼 Visitor Gender: %s\n" +
            "💬 Relationship to Inmate: %s\n" +
            "🏠 Visitor Address: %s\n" +
            "📞 Visitor Contact Number: %s\n" +
            "🆔 One-Time Visitor ID: %s\n" +
            "🎯 Purpose of Visit: %s\n" +
            "📅 Visit Date: %s\n" +
            "⏰ Visit Time: %s\n" +
            "📝 Visit Status: %s\n" +
            "📅 Created: %s\n\n" +
            "❗ Please bring a valid ID for identity confirmation.",
            inmateID, inmateName, last, first, middle,
            gender, relationship, address, contact,
            visitorID, purpose, date, time, status, created
        );

        // Create a Pane for the information (VBox in this case)
        VBox infoPane = new VBox();
        infoPane.setSpacing(10);
        infoPane.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f9; -fx-border-radius: 10px;");
        

        
        // Create Labels for each line of the information
        String[] lines = info.split("\n");
        for (String line : lines) {
            Text text = new Text(line);
            text.setFont(new Font(14));

            // Make the specific lines bigger and bold
            if (line.contains("✅ The visit is scheduled")) {
                text.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4CAF50;");
            } else if (line.contains("❗ Please bring a valid ID")) {
                text.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #D32F2F;");
            } else {
                text.setStyle("-fx-font-size: 14px;");
            }

            infoPane.getChildren().add(text);
        }

        // Button to close
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20;");
        closeBtn.setOnAction(e -> {
            window.close();
            try {
                new LandingPage().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Container layout with centered elements
        VBox layout = new VBox(20, infoPane, closeBtn);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.setMinWidth(400);
        layout.setMaxHeight(900);

        // Scene with layout
        Scene scene = new Scene(layout, 600, 700);
        window.setScene(scene);
        window.show();
    }
}
