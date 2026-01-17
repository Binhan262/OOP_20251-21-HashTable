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

    private final String[] titles = {
        "What is a Hash Table?",
        "Linear Probing",
        "Quadratic Probing",
        "Double Hashing",
        "Separate Chaining"
    };

    private final String[] descriptions = {
        "A hash table is a data structure that stores key - value pairs and provides fast average-time complexity" + 
        "for insertion, deletion, and lookup operations.\n" +
        " - How a hash table works:\n" +
        "1. A hash function maps a key to an index (bucket)\n" + 
        "2. The value is stored in that bucket\n" + 
        "3. When multiple keys map to the same index, a collision occurs\n" + 
        "4. A collision resolution strategy determines how to handle collisions",

        "Linear probing resolves collisions by searching sequentially for the next available bucket.\n" + 
        " - Probe Formula: index = (hash(key) + i) % tableSize\n" + 
        " - How it works:\n" + 
        "1. Compute initial index\n" + 
        "2. If occupied, move to the next bucket\n" + 
        "3. Repeat until an empty bucket is found\n" + 
        " - Visualization Concept\n" + 
        "   +,Buckets are highlighted sequentially\n" + 
        "   +,Probe path is clearly animated\n" + 
        " - Pros\n" + 
        "   +, Simple and cache-friendly\n" + 
        "   +, No extra memory for pointers\n" + 
        "   +, Fast when load factor is low\n" + 
        " - Cons\n" + 
        "   +, Suffers from primary clustering\n" + 
        "   +, Performance degrades rapidly as table fills\n" ,

        
        "Quadratic probing reduces clustering by increasing the probe distance quadratically.\n" + 
        " - Probe Formula: index = (hash(key) + i²) % tableSize\n" + 
        " - How it works: Instead of checking adjacent buckets, it jumps further each time\n" + 
        " - Visualization Concept\n" + 
        "   +, Non-linear probing pattern\n" + 
        "   +, Highlights demonstrate how collisions spread out\n" + 
        " - Pros\n" + 
        "   +, Reduces primary clustering\n" + 
        "   +, Better distribution than linear probing\n" + 
        " - Cons\n" + 
        "   +, Still subject to secondary clustering\n" + 
        "   +, May fail to probe all slots\n" + 
        "   +, Requires careful table size selection",
        
        "Double hashing uses a second hash function to determine the step size for probing.\n" + 
        "Probe Formula: index = (hash1(key) + i * hash2(key)) % tableSize\n" + 
        " - How it works:\n" + 
        "1. Each key has a unique probe sequence\n" + 
        "2. Collisions are spread more evenly\n" + 
        " - Visualization Concept\n" + 
        "   +, Unique probing paths per key\n" + 
        "   +, Clearly shows reduced clustering\n" + 
        " - Pros\n" + 
        "   +, Best distribution among open addressing methods\n" + 
        "   +, Minimal clustering\n" + 
        "   +, Better performance at higher load factors\n" + 
        " - Cons\n" + 
        "   +, More complex to implement\n" + 
        "   +, Requires two well-designed hash functions\n" + 
        "   +, Slightly higher computational cost",

        "Separate chaining stores multiple elements in the same bucket by using a secondary data structure, typically a linked list.\n" +
        "Each bucket acts as the head of a chain.\n" + 
        " - How it works:\n" + 
        "1. Hash function maps key to a bucket\n" + 
        "2. If the bucket is empty, insert the element\n" + 
        "3. If occupied, append the element to the chain\n" + 
        " - Visualization Concept\n" + 
        "   +, Each bucket contains a vertical or horizontal linked list\n" + 
        "   +, Traversal during search is animated node by node\n" + 
        " - Pros\n" + 
        "   +, Simple to implement\n" + 
        "   +, Table never becomes “full”\n" + 
        "   +, Performs well at high load factors\n" + 
        " - Cons\n" + 
        "   +, Extra memory overhead for pointers\n" + 
        "   +, Poor cache locality\n" + 
        "   +, Performance degrades if chains become long",
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

        btnPrevious.setDisable(currentPage == 0);
        btnNext.setDisable(currentPage == titles.length - 1);
    }
}