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
import java.util.ArrayList;
import java.util.List;

public class HashTableMenuController {

    private HashTableAnimationManager animationManager;
    
    // FXML Elements - Top Menu 
    @FXML private MenuItem menuBack;
    
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


    // FXML Elements - Bottom Panel
    @FXML private Label probestep;
    @FXML private Label probefunc;
    @FXML private Label hashfunc;
    @FXML private TextField tfResult;
    
    // Backend - NO DEFAULT VALUES
    private HashTable hashTable;
    private CollisionResolver collisionResolver;
    private int tableSize;
    
    // Visualization
    private Label[] arrowIndicators;
    private int currentProbeIndex = -1;
    private boolean isAnimating = false;
    
    
    public void initialize() {
    }
    // This method MUST be called before using the controller
    public void initializeWithResolver(CollisionResolver resolver, int size) {
        this.collisionResolver = resolver;
        this.tableSize = size;
        this.hashTable = new HashTable(size, resolver);
        
        // Initialize animation manager
        this.animationManager = new HashTableAnimationManager(this, hashTable);
        
        // Set up event listener for menu back button
        if (menuBack != null) {
            menuBack.setOnAction(e -> handleMenuBack());
        }
        
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
            animationManager.animateInsert(key, value);
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
            animationManager.animateSearch(key);
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
            animationManager.animateDelete(key);
        } catch (NumberFormatException e) {
            showAlert("Key must be a number", Alert.AlertType.ERROR);
        }
    }    
    
    private void handleMenuBack(){
        Main.setRoot("/FrontEnd/SelectionMenu/SelectionMenu.fxml");
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
    void disableButtons() {
        if (btnInsert != null) btnInsert.setDisable(true);
        if (btnSearch != null) btnSearch.setDisable(true);
        if (btnDelete != null) btnDelete.setDisable(true);
    }
    void enableButtons() {
        if (btnInsert != null) btnInsert.setDisable(false);
        if (btnSearch != null) btnSearch.setDisable(false);
        if (btnDelete != null) btnDelete.setDisable(false);
    }
    //Arrow helper(opend addressing) 
    void hideProbeArrow() {
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
    void showProbeArrow(int index) {
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
        String funcText = "h(k) = k mod " + tableSize;
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
    //Visualization methods
        public void updateVisualization() {
        if (collisionResolver instanceof SeparateChaining) {
            if (scrollPaneVertical != null) scrollPaneVertical.setVisible(true);
            if (scrollPaneHorizontal != null) scrollPaneHorizontal.setVisible(false);
            buildChainingVisualization();
        } else {
            if (scrollPaneHorizontal != null) scrollPaneHorizontal.setVisible(true);
            if (scrollPaneVertical != null) scrollPaneVertical.setVisible(false);
            buildOpenAddressingVisualization();
        }
    }
    public void setProbeStep(int step) {
        probestep.setText("i = " + String.valueOf(step));
    }

    public void setResultText(String text) {
        tfResult.setText(text);
    }
    public GridPane getHorizontalGrid() {
        return horizontalGrid;
    }

    public GridPane getChainingGrid() {
        return chainingGrid;
    }

    public CollisionResolver getCollisionResolver() {
        return collisionResolver;
    }
}