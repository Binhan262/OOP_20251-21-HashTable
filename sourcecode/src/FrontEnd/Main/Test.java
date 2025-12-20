package FrontEnd.Main;

import Backend.HashTable.CollisionResolver.CollisionResolver;
import Backend.HashTable.CollisionResolver.SeparateChaining;
import FrontEnd.HashTableMenu.HashTableMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd/HashTableMenu/HashTableMenu.fxml"));
        Parent root = loader.load(); 

        HashTableMenuController controller = loader.getController();

        int tableSize = 50;
        CollisionResolver resolver = new SeparateChaining(tableSize);

        controller.initializeWithResolver(resolver, tableSize); 

        stage.setScene(new Scene(root));
        stage.setTitle("Test");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
