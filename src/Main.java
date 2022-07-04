import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        URL url = getClass().getResource("convertorGUI.fxml");
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Currency Convertor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
