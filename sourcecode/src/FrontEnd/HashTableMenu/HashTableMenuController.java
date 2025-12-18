//Carefull: Read careafully before modifying anything in this file or create anything related to it
//Carefull: Read careafully before modifying anything in this file or create anything related to it
//Carefull: Read careafully before modifying anything in this file or create anything related to it


package FrontEnd.HashTableMenu;

import Backend.HashTable.HashTable;
import Backend.HashTable.CollisionResolver.*;
import Backend.LinkedList.LinkedList;
import Backend.LinkedList.Node;
import FrontEnd.Main.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class HashTableMenuController {
    
    // FXML Elements - Top Menu and Labels
    @FXML private MenuItem menuBack;
    @FXML private Label hashfunc;
    @FXML private Label probestep;
    @FXML private Label probefunc;
    
    // FXML Elements - Left Panel
    @FXML private TextField inputkey;
    @FXML private TextField inputvalue;
    @FXML private Button btnInsert;
    @FXML private Button btnSearch;
    @FXML private Button btnDelete;
    
    // FXML Elements - Center Visualization
    @FXML private ScrollPane scrollPaneHorizontal;
    @FXML private ScrollPane scrollPaneVertical;
    @FXML private GridPane horizontalGrid;
    @FXML private GridPane chainingGrid;
    
    // Backend - NO DEFAULT VALUES
    private HashTable hashTable;
    private CollisionResolver collisionResolver;
    private int tableSize;
    
    // Visualization
    private Label[] arrowIndicators;
    private int currentProbeIndex = -1;
    private boolean isAnimating = false;
    
    
    public void initialize() {
        // Only setup menu action - NO data initialization  
        // Note: updateVisualization() and other setup will be called
        // by initializeWithResolver() from the setup screen
        //Lam them cai man set up thi ms vt tiep dc
    }
    // This method MUST be called before using the controller
    public void initializeWithResolver(CollisionResolver resolver, int size) {
        this.collisionResolver = resolver;
        this.tableSize = size;
        this.hashTable = new HashTable(size, resolver);
        
        // Now build the visualization
        updateVisualization();
        updateHashFunctionLabel();
    }

    //Event Handlers 
    @FXML
    private void handleInsertpressed(){
        if(isAnimating){
            showAlert("Please wait for current operation to complete", Alert.AlertType.WARNING);
            return;
        } 
        String keyStr = inputkey.getText();
        String value = inputvalue.getText();
        if(keyStr==null || keyStr.isEmpty()){
            showAlert("Please enter a key", Alert.AlertType.WARNING);
            return;
        }
        if(value==null || value.isEmpty()){
            showAlert("Please enter a value", Alert.AlertType.WARNING);
            return;
        }
        try {
            int key = Integer.parseInt(keyStr);
            animateInsert(key, value);
        } catch (NumberFormatException e) {
            showAlert("Key must be a number", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSearchpressed() {
        if (isAnimating) {
            showAlert("Please wait for current operation to complete", Alert.AlertType.WARNING);
            return;
        }
        
        String keyStr = inputkey.getText();
        
        if (keyStr == null || keyStr.isEmpty()) {
            showAlert("Please enter a key to search", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            int key = Integer.parseInt(keyStr);
            animateSearch(key);
        } catch (NumberFormatException e) {
            showAlert("Key must be a number", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleDeletepressed() {
        if (isAnimating) {
            showAlert("Please wait for current operation to complete", Alert.AlertType.WARNING);
            return;
        }
        
        String keyStr = inputkey.getText();
        
        if (keyStr == null || keyStr.isEmpty()) {
            showAlert("Please enter a key to delete", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            int key = Integer.parseInt(keyStr);
            animateDelete(key);
        } catch (NumberFormatException e) {
            showAlert("Key must be a number", Alert.AlertType.ERROR);
        }
    }    
    
    private void handleMenuBack(){
        Main.setRoot("/FrontEnd/SelectionMenu/SelectionMenu.fxml");
    }
    
    //Animation methods
    //Insert
    private void animateInsert(int key, String value) {
        isAnimating = true;
        disableButtons();
        
        SequentialTransition sequence = new SequentialTransition();
        
        int step = 0;
        boolean inserted = false;
        
        while (step < tableSize) {
            int index = collisionResolver.probe(key, step);
            LinkedList list = hashTable.getTableAtIndex(index);
            
            final int currentIndex = index;
            final int currentStep = step;
            
            // Show probe arrow
            PauseTransition showArrow = new PauseTransition(Duration.millis(2000));
            showArrow.setOnFinished(e -> {
                showProbeArrow(currentIndex);
                if (probestep != null) {
                    probestep.setText(String.valueOf(currentStep));
                }
            });
            sequence.getChildren().add(showArrow);
            
            // Check if key already exists in this slot
            String existingValue = list.search(key);
            if (existingValue != null) {
                // Key exists - update value
                hashTable.insert(key, value);  // This will update the value
                
                PauseTransition updatePause = new PauseTransition(Duration.millis(2000));
                updatePause.setOnFinished(e -> {
                    updateVisualization();
                    hideProbeArrow();
                    showAlert("Key " + key + " already exists at index " + currentIndex + 
                             "\nValue updated to: " + value, Alert.AlertType.INFORMATION);
                    isAnimating = false;
                    enableButtons();
                });
                sequence.getChildren().add(updatePause);
                inserted = true;
                break;
            }
            
            // Check if we can insert here (empty slot)
            if (!collisionResolver.needsProbing(list.isEmpty())) {
                // Insert here
                final boolean insertResult = hashTable.insert(key, value);
                
                PauseTransition insertPause = new PauseTransition(Duration.millis(2000));
                insertPause.setOnFinished(e -> {
                    updateVisualization();
                    hideProbeArrow();
                    if (insertResult) {
                        showAlert("Key " + key + " inserted at index " + currentIndex, Alert.AlertType.INFORMATION);
                    } else {
                        showAlert("Insertion failed", Alert.AlertType.ERROR);
                    }
                    isAnimating = false;
                    enableButtons();
                });
                sequence.getChildren().add(insertPause);
                inserted = true;
                break;
            }
            
            step++;
        }
        
        if (!inserted) {
            PauseTransition failPause = new PauseTransition(Duration.millis(2000));
            failPause.setOnFinished(e -> {
                hideProbeArrow();
                showAlert("Hash table is full or no suitable slot found", Alert.AlertType.ERROR);
                isAnimating = false;
                enableButtons();
            });
            sequence.getChildren().add(failPause);
        }
        
        sequence.play();
    }
    //Search
    private void animateSearch(int key) {
        isAnimating = true;
        disableButtons();
        
        SequentialTransition sequence = new SequentialTransition();
        
        int step = 0;
        boolean found = false;
        
        while (step < tableSize) {
            int index = collisionResolver.probe(key, step);
            LinkedList list = hashTable.getTableAtIndex(index);
            
            final int currentIndex = index;
            final int currentStep = step;
            
            // Show probe arrow
            PauseTransition showArrow = new PauseTransition(Duration.millis(2000));
            showArrow.setOnFinished(e -> {
                showProbeArrow(currentIndex);
                if (probestep != null) {
                    probestep.setText(String.valueOf(currentStep));
                }
            });
            sequence.getChildren().add(showArrow);
            
            // Check if key exists
            String foundValue = list.search(key);
            if (foundValue != null) {
                PauseTransition foundPause = new PauseTransition(Duration.millis(2000));
                foundPause.setOnFinished(e -> {
                    hideProbeArrow();
                    showAlert("Key " + key + " found at index " + currentIndex + "\nValue: " + foundValue, 
                             Alert.AlertType.INFORMATION);
                    isAnimating = false;
                    enableButtons();
                });
                sequence.getChildren().add(foundPause);
                found = true;
                break;
            }
            
            // If empty slot in open addressing, stop searching
            if (collisionResolver.needsProbing(false) && list.isEmpty()) {
                PauseTransition notFoundPause = new PauseTransition(Duration.millis(2000));
                notFoundPause.setOnFinished(e -> {
                    hideProbeArrow();
                    showAlert("Key " + key + " not found (empty slot encountered)", Alert.AlertType.INFORMATION);
                    isAnimating = false;
                    enableButtons();
                });
                sequence.getChildren().add(notFoundPause);
                found = true; // To exit while loop
                break;
            }
            
            step++;
        }
        
        if (!found) {
            PauseTransition notFoundPause = new PauseTransition(Duration.millis(2000));
            notFoundPause.setOnFinished(e -> {
                hideProbeArrow();
                showAlert("Key " + key + " not found", Alert.AlertType.INFORMATION);
                isAnimating = false;
                enableButtons();
            });
            sequence.getChildren().add(notFoundPause);
        }
        
        sequence.play();
    }
    //Delete
    private void animateDelete(int key) {
        isAnimating = true;
        disableButtons();
        
        SequentialTransition sequence = new SequentialTransition();
        
        int step = 0;
        boolean deleted = false;
        
        while (step < tableSize) {
            int index = collisionResolver.probe(key, step);
            LinkedList list = hashTable.getTableAtIndex(index);
            
            final int currentIndex = index;
            final int currentStep = step;
            
            // Show probe arrow
            PauseTransition showArrow = new PauseTransition(Duration.millis(2000));
            showArrow.setOnFinished(e -> {
                showProbeArrow(currentIndex);
                if (probestep != null) {
                    probestep.setText(String.valueOf(currentStep));
                }
            });
            sequence.getChildren().add(showArrow);
            
            // Check if key exists and delete
            if (list.search(key) != null) {
                final boolean deleteResult = hashTable.delete(key);
                
                PauseTransition deletePause = new PauseTransition(Duration.millis(2000));
                deletePause.setOnFinished(e -> {
                    updateVisualization();
                    hideProbeArrow();
                    if (deleteResult) {
                        showAlert("Key " + key + " deleted from index " + currentIndex, Alert.AlertType.INFORMATION);
                    } else {
                        showAlert("Deletion failed", Alert.AlertType.ERROR);
                    }
                    isAnimating = false;
                    enableButtons();
                });
                sequence.getChildren().add(deletePause);
                deleted = true;
                break;
            }
            
            // If empty slot in open addressing, stop searching
            if (collisionResolver.needsProbing(false) && list.isEmpty()) {
                PauseTransition notFoundPause = new PauseTransition(Duration.millis(2000));
                notFoundPause.setOnFinished(e -> {
                    hideProbeArrow();
                    showAlert("Key " + key + " not found (empty slot encountered)", Alert.AlertType.INFORMATION);
                    isAnimating = false;
                    enableButtons();
                });
                sequence.getChildren().add(notFoundPause);
                deleted = true; // To exit while loop
                break;
            }
            
            step++;
        }
        
        if (!deleted) {
            PauseTransition notFoundPause = new PauseTransition(Duration.millis(2000));
            notFoundPause.setOnFinished(e -> {
                hideProbeArrow();
                showAlert("Key " + key + " not found", Alert.AlertType.INFORMATION);
                isAnimating = false;
                enableButtons();
            });
            sequence.getChildren().add(notFoundPause);
        }
        
        sequence.play();
    }
    //Visualization methods
    private void updateVisualization() {
        if (collisionResolver instanceof SeparateChaining) {
            buildChainingVisualization();
            scrollPaneHorizontal.setVisible(false);
            scrollPaneVertical.setVisible(true);
        } else {
            buildOpenAddressingVisualization();
            scrollPaneHorizontal.setVisible(true);
            scrollPaneVertical.setVisible(false);
        }
    }

    private void buildOpenAddressingVisualization(){
        //Reset grid
        horizontalGrid.getChildren().clear();
        horizontalGrid.getColumnConstraints().clear();
        horizontalGrid.getRowConstraints().clear();
        arrowIndicators = new Label[tableSize];

        //Setup columns
        for (int i = 0; i < tableSize; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.NEVER);
            col.setPrefWidth(Region.USE_COMPUTED_SIZE);
            horizontalGrid.getColumnConstraints().add(col);
        }
        //Setup 2 rows
        for (int i = 0; i < 2; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.NEVER);
            row.setPrefHeight(Region.USE_COMPUTED_SIZE);
            horizontalGrid.getRowConstraints().add(row);
        }
        //Row 0: Arrows
        for (int i = 0; i < tableSize; i++) {
            Label arrow = new Label("↓");
            arrow.setFont(Font.font(24));
            arrow.setTextFill(Color.RED);
            arrow.setVisible(false);
            arrow.setAlignment(Pos.CENTER);
            arrow.setMaxWidth(Double.MAX_VALUE);
            
            arrowIndicators[i] = arrow;
            horizontalGrid.add(arrow, i, 0);
            GridPane.setHalignment(arrow, HPos.CENTER);
        }
        //Row 1: Cells
        for (int i = 0; i < tableSize; i++) {
            LinkedList list = hashTable.getTableAtIndex(i);
            VBox cell = createHashCellFromList(i, list);
            horizontalGrid.add(cell, i, 1);
        }
    }

    private void buildChainingVisualization() {
        chainingGrid.getChildren().clear();
        chainingGrid.getColumnConstraints().clear();
        chainingGrid.getRowConstraints().clear();
        
        // Find max columns
        int maxColumns = 3;//Minimum columns
        for (int i = 0; i < tableSize; i++) {
            LinkedList list = hashTable.getTableAtIndex(i);
            int chainLength = list.getLength();
            int cols = 1 + (chainLength == 0 ? 2 : chainLength * 2 + 2);
            maxColumns = Math.max(maxColumns, cols);
        }
        // Setup columns
        for (int i = 0; i < maxColumns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.NEVER);
            col.setPrefWidth(Region.USE_COMPUTED_SIZE);
            chainingGrid.getColumnConstraints().add(col);
        }
        // Setup rows
        for (int i = 0; i < tableSize; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.NEVER);
            row.setPrefHeight(Region.USE_COMPUTED_SIZE);
            chainingGrid.getRowConstraints().add(row);
        }
        // Build each row
        for (int row = 0; row < tableSize; row++) {
            int col = 0;
            // Index cell
            VBox indexCell = createHashCell(row, "", "");
            chainingGrid.add(indexCell, col++, row);
            
            LinkedList list = hashTable.getTableAtIndex(row);
            List<NodeData> nodes = getChainNodes(list);
            
            if (nodes.isEmpty()) {
                Label arrow = createArrowLabel();
                chainingGrid.add(arrow, col++, row);
                
                Label nullLabel = createNullLabel();
                chainingGrid.add(nullLabel, col++, row);
            } 
            else {
                for (NodeData nodeData : nodes) {
                    Label arrow = createArrowLabel();
                    chainingGrid.add(arrow, col++, row);
                    VBox node = createChainNode(String.valueOf(nodeData.key), nodeData.value);
                    chainingGrid.add(node, col++, row);
                }
                Label arrow = createArrowLabel();
                chainingGrid.add(arrow, col++, row);
                Label nullLabel = createNullLabel();
                chainingGrid.add(nullLabel, col++, row);
            }
        }
    }
    
    //Helper methods
    //Alert helper
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Hash Table Visualization");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    //Button state helper
    private void disableButtons() {
        if (btnInsert != null) btnInsert.setDisable(true);
        if (btnSearch != null) btnSearch.setDisable(true);
        if (btnDelete != null) btnDelete.setDisable(true);
    }
    private void enableButtons() {
        if (btnInsert != null) btnInsert.setDisable(false);
        if (btnSearch != null) btnSearch.setDisable(false);
        if (btnDelete != null) btnDelete.setDisable(false);
    }
    //Arrow helper(opend addressing) 
    //Doan nay t nghi nen chi de chi o hien tai thoi 
    //Ko chi nh qua lai ko bt dang chi cai gi nua
    private void hideProbeArrow() {
        if (currentProbeIndex >= 0 && currentProbeIndex < arrowIndicators.length 
            && arrowIndicators[currentProbeIndex] != null) {
            arrowIndicators[currentProbeIndex].setVisible(false);
        }
        //Reset next time
        currentProbeIndex = -1;
        if (probestep != null) {
            probestep.setText("0");
        }
    }
    private void showProbeArrow(int index) {
        hideProbeArrow();
        if (!(collisionResolver instanceof SeparateChaining) && 
            index >= 0 && index < arrowIndicators.length && 
            arrowIndicators[index] != null) {
            arrowIndicators[index].setVisible(true);
            currentProbeIndex = index;
        }
    }
    //Arrow helper(chaining)
    private Label createArrowLabel() {
        Label arrow = new Label("→");
        arrow.setFont(Font.font(20));
        arrow.setAlignment(Pos.CENTER);
        arrow.setPrefWidth(30);
        return arrow;
    }
    
    private Label createNullLabel() {
        Label nullLabel = new Label("null");
        nullLabel.setFont(Font.font(14));
        nullLabel.setTextFill(Color.GRAY);
        nullLabel.setAlignment(Pos.CENTER_LEFT);
        return nullLabel;
    }
    //Chainning visualization helper
    //Copy of backend data
    private List<NodeData> getChainNodes(LinkedList list) {
        List<NodeData> nodes = new ArrayList<>();
        Node current = list.getHead();
        while (current != null) {
            nodes.add(new NodeData(current.key, current.value));
            current = current.next;
        }
        return nodes;
    }
    //Create chaining node
    private VBox createChainNode(String key, String value) {
        VBox node = new VBox(5);
        node.setAlignment(Pos.CENTER);
        node.setStyle("-fx-border-color: blue; -fx-border-width: 2; " +
                      "-fx-background-color: lightblue; -fx-padding: 8;");
        node.setPrefSize(90, 70);
        node.setMinSize(90, 70);
        
        Label keyLabel = new Label("Key: " + key);
        keyLabel.setFont(Font.font(14));

        Label valueLabel = new Label("Value: " + value);
        valueLabel.setFont(Font.font(14));
        
        node.getChildren().addAll(keyLabel, valueLabel);
        return node;
    }
    //Create cell from list(opend addressing)
    private VBox createHashCell(int index, String key, String value) {
        VBox cell = new VBox(5);
        cell.setAlignment(Pos.CENTER);
        cell.setStyle("-fx-border-color: black; -fx-border-width: 2; " +
                      "-fx-background-color: white; -fx-padding: 10;");
        cell.setPrefSize(100, 80);
        cell.setMinSize(100, 80);
        
        Label indexLabel = new Label(String.valueOf(index));
        indexLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        if (key == null || key.isEmpty()) {
            Label emptyLabel = new Label("empty");
            emptyLabel.setFont(Font.font(12));
            emptyLabel.setTextFill(Color.GRAY);
            cell.getChildren().addAll(indexLabel, emptyLabel);
        } else {
            Label keyLabel = new Label("Key: " + key);
            keyLabel.setFont(Font.font(14));
            Label valueLabel = new Label("Value: " + value);
            valueLabel.setFont(Font.font(14));
            cell.getChildren().addAll(indexLabel, keyLabel, valueLabel);
        }
        return cell;
    }
    private VBox createHashCellFromList(int index, LinkedList list) {
        List<NodeData> nodes = getChainNodes(list);
        if (nodes.isEmpty()) {
            return createHashCell(index, "", "");
        } else {
            NodeData first = nodes.get(0);
            return createHashCell(index, String.valueOf(first.key), first.value);
        }
    }
    //Update hash function label
    private void updateHashFunctionLabel() {
        String funcText = "k mod " + tableSize;
        if (hashfunc != null) {
            hashfunc.setText(funcText);
        }
        
        String probeName = collisionResolver.getName();
        if (probefunc != null) {
            probefunc.setText(probeName);
        }
    }
    
    //Inner class to hold node data(Safe visualization cause immediate data extraction)
    private static class NodeData {
        int key;
        String value;

        NodeData(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}