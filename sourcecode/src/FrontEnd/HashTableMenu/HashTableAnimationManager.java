package FrontEnd.HashTableMenu;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.Queue;

import Backend.HashTable.HashTable;
import Backend.HashTable.CollisionResolver.CollisionResolver;
import Backend.HashTable.Event.*;
import Backend.LinkedList.LinkedList;

public class HashTableAnimationManager {
    
    private final HashTableMenuController controller;
    private final HashTable hashTable;
    private final Queue<HashTableEvent> eventQueue;
    
    // Animation settings
    private static final double STEP_DELAY = 800; 
    private static final double FLASH_DURATION = 400;
    
    public HashTableAnimationManager(HashTableMenuController controller, HashTable hashTable) {
        this.controller = controller;
        this.hashTable = hashTable;
        this.eventQueue = new java.util.LinkedList<>();
        
        hashTable.setEventSink(event -> eventQueue.offer(event));
        
        for (int i = 0; i < hashTable.getTableSize(); i++) {
            LinkedList list = hashTable.getTableAtIndex(i);
            if (list != null) {
                list.setEventSink(event -> eventQueue.offer(event));
            }
        }
    }
    
    public boolean isAnimating() {
        return controller.isAnimating();
    }
    
    // Insert Animation
    public void animateInsert(int key, String value) {
        if (isAnimating()) return;
        
        controller.setAnimating(true);
        controller.disableButtons();
        controller.hideProbeArrow();
        eventQueue.clear();
        
        boolean success = hashTable.insert(key, value);
        
        processEventQueue(() -> {
            if (success) {
                controller.setResultText("Successfully inserted key " + key + " with value: " + value);
            } else {
                controller.setResultText("Failed to insert key " + key + ": table is full");
            }
            controller.updateVisualization();
            controller.hideProbeArrow();
            controller.setAnimating(false);
            controller.enableButtons();
        });
    }
    
    // Search Animation
    public void animateSearch(int key) {
        if (isAnimating()) return;
        
        controller.setAnimating(true);
        controller.disableButtons();
        controller.hideProbeArrow();
        eventQueue.clear();
        
        String result = hashTable.search(key);
        
        processEventQueue(() -> {
            if (result != null) {
                controller.setResultText("Found key " + key + " with value: " + result);
            } else {
                controller.setResultText("Key " + key + " not found in the table");
            }
            controller.hideProbeArrow();
            controller.setAnimating(false);
            controller.enableButtons();
        });
    }
    
    // Delete Animation
    public void animateDelete(int key) {
        if (isAnimating()) return;
        
        controller.setAnimating(true);
        controller.disableButtons();
        controller.hideProbeArrow();
        eventQueue.clear();
        
        boolean success = hashTable.delete(key);
        
        processEventQueue(() -> {
            if (success) {
                controller.setResultText("Successfully deleted key " + key);
            } else {
                controller.setResultText("Key " + key + " not found, nothing to delete");
            }
            controller.updateVisualization();
            controller.hideProbeArrow();
            controller.setAnimating(false);
            controller.enableButtons();
        });
    }
    
    private void processEventQueue(Runnable onComplete) {
        if (eventQueue.isEmpty()) {
            onComplete.run();
            return;
        }
        
        SequentialTransition sequence = new SequentialTransition();
        int currentStep = 0;
        
        while (!eventQueue.isEmpty()) {
            HashTableEvent event = eventQueue.poll();
            
            if (event instanceof BucketAccessedEvent) {
                BucketAccessedEvent bae = (BucketAccessedEvent) event;
                int index = bae.getIndex();
                
                // Update probe step label
                final int step = currentStep;
                PauseTransition updateStep = createPause(50, () -> {
                    Platform.runLater(() -> {
                        controller.setProbeStep(step);
                        controller.showProbeArrow(index);
                    });
                });
                sequence.getChildren().add(updateStep);

                // Add a small pause to allow UI to update
                PauseTransition smallPause = new PauseTransition(Duration.millis(50));
                sequence.getChildren().add(smallPause);

                // Highlight the accessed bucket
                PauseTransition highlight = createPause(STEP_DELAY, () -> 
                    highlightBucket(index, Color.YELLOW)
                );
                sequence.getChildren().add(highlight);

                PauseTransition holdColor = new PauseTransition(Duration.millis(FLASH_DURATION));
                sequence.getChildren().add(holdColor);
                
                // Un-highlight
                PauseTransition unhighlight = createPause(100, () -> 
                    highlightBucket(index, Color.WHITE)
                );
                sequence.getChildren().add(unhighlight);
                
                currentStep++;
            }
            else if (event instanceof CollisionDetectedEvent) {
                CollisionDetectedEvent cde = (CollisionDetectedEvent) event;
                int index = cde.getIndex();
                
                PauseTransition setRed = createPause(0, () ->
                    highlightBucket(index, Color.LIGHTCORAL)
                );
                sequence.getChildren().add(setRed);

                PauseTransition holdColor = new PauseTransition(Duration.millis(FLASH_DURATION));
                sequence.getChildren().add(holdColor);

                PauseTransition setWhite = createPause(0, () ->
                    highlightBucket(index, Color.WHITE)
                );
                sequence.getChildren().add(setWhite);
            }
            else if (event instanceof NodeVisitedEvent) {
                PauseTransition pause = createPause(STEP_DELAY / 2, () -> {});
                sequence.getChildren().add(pause);
            }
            else if (event instanceof NodeInsertEvent) {
                NodeInsertEvent nie = (NodeInsertEvent) event;
                
                PauseTransition flash = createPause(FLASH_DURATION, () -> 
                    controller.setResultText("Inserting key " + nie.getKey() + "...")
                );
                sequence.getChildren().add(flash);

                PauseTransition holdColor = new PauseTransition(Duration.millis(FLASH_DURATION));
                sequence.getChildren().add(holdColor);
            }
            else if (event instanceof NodeDeletedEvent) {
                NodeDeletedEvent nde = (NodeDeletedEvent) event;
                
                PauseTransition flash = createPause(FLASH_DURATION, () -> 
                    controller.setResultText("Deleting key " + nde.getKey() + "...")
                );
                sequence.getChildren().add(flash);
            }
        }
        
        // Add final completion action
        sequence.setOnFinished(e -> onComplete.run());
        sequence.play();
    }
    
    private PauseTransition createPause(double millis, Runnable action) {
        PauseTransition pause = new PauseTransition(Duration.millis(millis));
        pause.setOnFinished(e -> action.run());
        return pause;
    }
    
    private void highlightBucket(int index, Color color) {
        CollisionResolver.VisualizationType vizType = 
            hashTable.getResolver().getVisualizationType();
        
        switch (vizType) {
            case CHAINING:
                highlightChainingBucket(index, color);
                break;
            case OPEN_ADDRESSING:
                highlightOpenAddressingBucket(index, color);
                break;
        }
    }
    
    private void highlightOpenAddressingBucket(int index, Color color) {
        GridPane grid = controller.getHorizontalGrid();
        if (grid == null) return;
        
        grid.getChildren().stream()
            .filter(node -> node instanceof VBox)
            .filter(node -> GridPane.getColumnIndex(node) != null && 
                          GridPane.getColumnIndex(node) == index &&
                          GridPane.getRowIndex(node) != null &&
                          GridPane.getRowIndex(node) == 1)
            .findFirst()
            .ifPresent(node -> {
                String colorString = toHexString(color);
                node.setStyle("-fx-border-color: black; -fx-border-width: 2; " +
                            "-fx-background-color: " + colorString + "; -fx-padding: 10;");
            });
    }
    
    private void highlightChainingBucket(int index, Color color) {
        GridPane grid = controller.getChainingGrid();
        if (grid == null) return;
        
        grid.getChildren().stream()
            .filter(node -> node instanceof VBox)
            .filter(node -> GridPane.getRowIndex(node) != null && 
                          GridPane.getRowIndex(node) == index &&
                          GridPane.getColumnIndex(node) != null &&
                          GridPane.getColumnIndex(node) == 0)
            .findFirst()
            .ifPresent(node -> {
                String colorString = toHexString(color);
                node.setStyle("-fx-border-color: black; -fx-border-width: 2; " +
                            "-fx-background-color: " + colorString + "; -fx-padding: 10;");
            });
    }
    
    private String toHexString(Color color) {
        if (color == Color.WHITE) return "white";
        if (color == Color.YELLOW) return "#FFFF99";
        if (color == Color.LIGHTCORAL) return "#ef6a6aff";
        if (color == Color.LIGHTGREEN) return "#90EE90";
        return String.format("#%02X%02X%02X",
            (int)(color.getRed() * 255),
            (int)(color.getGreen() * 255),
            (int)(color.getBlue() * 255));
    }
}