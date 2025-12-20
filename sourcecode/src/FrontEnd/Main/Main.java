package FrontEnd.Main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;
    private static Stage secondaryStage;
    private static Scene mainScene;
    private static Scene secondaryScene;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        secondaryStage = new Stage();

        Parent primaryRoot = loadFXML("/FrontEnd/MainMenu/MainMenu.fxml");
        mainScene = new Scene(primaryRoot);

        stage.setScene(mainScene);
        stage.setTitle("Hash Table Visualization");
        stage.show();

        Parent secondaryRoot = loadFXML("/FrontEnd/HelpMenu/HelpMenu.fxml");
        secondaryScene = new Scene(secondaryRoot);

        secondaryStage.setTitle("Help");
        secondaryStage.setScene(secondaryScene);
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.hide();
    }

    public static void setPrimaryRoot(String fxml) {
        try {
            Parent root = loadFXML(fxml);
            mainScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setSecondaryRoot(String fxml) {
        try {
            Parent root = loadFXML(fxml);
            secondaryScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showSecondaryStage(String fxml) {
        setSecondaryRoot(fxml);
        secondaryStage.show();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        return FXMLLoader.load(Main.class.getResource(fxml));
    }

    public static void setRoot(Parent root) {
        mainScene.setRoot(root);
    }   

    public static void main(String[] args) {
        launch(args);
    }

    public static void showSecondaryStage() {
        secondaryStage.show();
    }
}

