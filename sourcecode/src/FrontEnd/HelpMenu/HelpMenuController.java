package FrontEnd.HelpMenu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class HelpMenuController {
    @FXML
    private Button btnBack;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnPrevious;

    @FXML
    private Label lblTitle;

    @FXML
    private Text txtContent;

    private int currentPage = 0;

    // Data for the Help Menu
    private final String[] titles = {
        "What is a Hash Table?",
        "Linear Probing",
        "Quadratic Probing",
        "Double Hashing",
        "Separate Chaining"
    };

    private final String[] descriptions = {
        "A Hash Table maps keys to indices using a Hash Function: h(k) = k % size.\n " +
        "When two keys map to the same index, a 'Collision' occurs.",
        
        "Linear Probing resolves collisions by searching for the next available slot sequentially (index + 1). \n" +
        "This method is simple to implement but can causes 'Primary Clustering'.",
        
        "Quadratic Probing resolves collisions by using a square function (index + i²) to find the next slot. \n" +
        "This method reduces 'Primary Clustering' but can cause 'Secondary Clustering'.",
        
        "Double Hashing resolves collisions by using a second hash function to calculate the step size to find the next slot. \n" +
        "This method can effectively reduce both 'Primary Clustering' and 'Secondary Clustering'.",
        
        "Separate Chaining stores a linked list at each table index. Colliding keys are simply added to the list. \n" +
        "This method prevent clustering entirely but it would create extra memory for pointers."
    };

    @FXML
    public void initialize() {
        updatePage();
    }

    @FXML
    private void handleNext() {
        if (currentPage < titles.length - 1) {
            currentPage++;
            updatePage();
        }
    }

    @FXML
    private void handlePrevious() {
        if (currentPage > 0) {
            currentPage--;
            updatePage();
        }
    }

    @FXML
    private void handleBack() {
        btnBack.getScene().getWindow().hide(); 
    }

    private void updatePage() {
        lblTitle.setText(titles[currentPage]);
        txtContent.setText(descriptions[currentPage]);

        // Disable buttons at boundaries
        btnPrevious.setDisable(currentPage == 0);
        btnNext.setDisable(currentPage == titles.length - 1);
    }
}