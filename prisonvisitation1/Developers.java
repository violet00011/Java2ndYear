

package prisonvisitation1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Developers extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Developers Page");
        StackPane layout = new StackPane(label);
        Scene scene = new Scene(layout, 400, 300);

        primaryStage.setTitle("Developers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
