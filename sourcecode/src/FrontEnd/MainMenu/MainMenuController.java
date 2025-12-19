package FrontEnd.MainMenu;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.application.Platform;
import FrontEnd.Main.Main;


public class MainMenuController {

    @FXML
    private Button btnCreateTable;

    @FXML
    private Button btnHelpMenu;

    @FXML
    private Button btnQuit;

    @FXML
    private void btnCreateHashTablePressed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnd/SelectionMenu/SelectionMenu.fxml"));
            Parent root = loader.load();

            Stage selectionStage = new Stage();
            selectionStage.setTitle("Hash Table Settings");

            selectionStage.initModality(Modality.APPLICATION_MODAL);
            selectionStage.setScene(new Scene(root));
            selectionStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnHelpPressed(ActionEvent event) {
        Main.showSecondaryStage("/FrontEnd/HelpMenu/HelpMenu.fxml");
    }

    @FXML
    private void btnQuitPressed(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}

