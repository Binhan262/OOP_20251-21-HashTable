package FrontEnd.Main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Parent initialRoot = loadFXML("/FrontEnd/MainMenu/MainMenu.fxml");
        mainScene = new Scene(initialRoot);

        stage.setScene(mainScene);
        stage.setTitle("Hash Table Visualization");
        stage.show();
    }

    public static void setRoot(String fxml) {
        try {
            Parent root = loadFXML(fxml);
            mainScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        return FXMLLoader.load(Main.class.getResource(fxml));
    }

    public static void main(String[] args) {
        launch(args);
    }
}

