package FrontEnd.MainMenu;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainMenuController {
    @FXML
    private Button btnCreateTable;

    @FXML
    private Button btnHelpMenu;

    @FXML
    private Button btnQuit;


    @FXML
    void btnCreateHashTablePressed(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FrontEnd/SelectionMenu/SelectionMenu.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }  
    @FXML
    void btnHelpPressed(ActionEvent event) {

    }

    @FXML
    void btnQuitPressed(ActionEvent event) {

    }
}
