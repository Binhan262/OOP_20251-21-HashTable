package FrontEnd.SelectionMenu;

import java.io.IOException;

import Backend.HashTable.CollisionResolver.CollisionResolver;
import Backend.HashTable.CollisionResolver.DoubleHashingMethod1;
import Backend.HashTable.CollisionResolver.DoubleHashingMethod2;
import Backend.HashTable.CollisionResolver.LinearProbing;
import Backend.HashTable.CollisionResolver.QuadraticProbing;
import Backend.HashTable.CollisionResolver.SeparateChaining;
import FrontEnd.HashTableMenu.HashTableMenuController;
import FrontEnd.Main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.scene.*;

public class SelectionMenuController {

    @FXML
    private ComboBox<String> cbbStrategy;

    @FXML
    private TextField tfTableSize;

    @FXML
    private Button btnOK;

    @FXML
    private Button btnBack;

    @FXML
    public void initialize() {
        ObservableList<String> strategies = FXCollections.observableArrayList("Chaining", "Linear Probing", "Quadratic Probing", "Double Hashing Method 1", "Double Hashing Method 2");
        cbbStrategy.setItems(strategies);
        cbbStrategy.getSelectionModel().selectFirst();

        tfTableSize.setTextFormatter(new TextFormatter<>(change -> {
        return change.getControlNewText().matches("\\d+") ? change : null;
        }));
    }

    @FXML
    private void btnOKPressed(ActionEvent event) {
        String strategy = cbbStrategy.getValue();
        String strSize = tfTableSize.getText();

        int tableSize;
        CollisionResolver resolver;

        try {
            tableSize = Integer.parseInt(strSize);
        } catch (NumberFormatException e) {
            System.err.println("Invalid table size");
            return;
        }

        switch (strategy) {
            case "Chaining":
                resolver = new SeparateChaining(tableSize);
                break;
            case "Linear Probing":
                resolver = new LinearProbing(tableSize);
                break;
            case "Quadratic Probing":
                resolver = new QuadraticProbing(tableSize);
                break;
            case "Double Hashing Method 1":
                resolver = new DoubleHashingMethod1(tableSize);
                break;
            case "Double Hashing Method 2":
                resolver = new DoubleHashingMethod2(tableSize);
                break;
            default:
                System.err.println("No strategy selected");
                return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/FrontEnd/HashTableMenu/HashTableMenu.fxml")
            );
            Parent root = loader.load();

            HashTableMenuController controller = loader.getController();
            controller.initializeWithResolver(resolver, tableSize);

            Main.setRoot(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void btnBackPressed(ActionEvent event) {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }
}