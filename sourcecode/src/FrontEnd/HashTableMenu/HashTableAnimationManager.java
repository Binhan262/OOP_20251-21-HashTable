package FrontEnd.HashTableMenu;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;

import Backend.HashTable.HashTable;
import Backend.HashTable.Event.*;

public class HashTableAnimationManager {
    
    private final HashTableMenuController controller;
    private final HashTable hashTable;
    private final Queue<HashTableEvent> eventQueue;
    private final Map<String, String> originalStyles = new HashMap<>();
    
    private static final double STEP_DELAY = 800; 
    private static final double FLASH_DURATION = 400;
    
    public HashTableAnimationManager(HashTableMenuController controller, HashTable hashTable) {
        this.controller = controller;
        this.hashTable = hashTable;
        this.eventQueue = new LinkedList<>();
        
        hashTable.setEventListener(event -> eventQueue.offer(event));
    }
    
    public boolean isAnimating() {
        return controller.isAnimating();
    }
    
    public void animateInsert(int key, String value) {
        if (isAnimating()) return;
        
        controller.setAnimating(true);
        controller.disableButtons();
        controller.hideProbeArrow();
        eventQueue.clear();
        originalStyles.clear();
        
        controller.updateVisualization();
        
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
    
    public void animateSearch(int key) {
        if (isAnimating()) return;
        
        controller.setAnimating(true);
        controller.disableButtons();
        controller.hideProbeArrow();
        eventQueue.clear();
        originalStyles.clear();
        
        controller.updateVisualization();
        
        String result = hashTable.search(key);
        
        processEventQueue(() -> {
            if (result != null) {
                controller.setResultText("Found key " + key + " with value: " + result);
            } else {
                controller.setResultText("Key " + key + " not found in the table");
            }
            controller.updateVisualization();
            controller.hideProbeArrow();
            controller.setAnimating(false);
            controller.enableButtons();
        });
    }
    
    public void animateDelete(int key) {
        if (isAnimating()) return;
        
        controller.setAnimating(true);
        controller.disableButtons();
        controller.hideProbeArrow();
        eventQueue.clear();
        originalStyles.clear();
        
        controller.updateVisualization();
        
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
                
                final int step = currentStep;
                PauseTransition updateStep = createPause(50, () -> {
                    controller.setProbeStep(step);
                    controller.showProbeArrow(index);
                });
                sequence.getChildren().add(updateStep);

                PauseTransition smallPause = new PauseTransition(Duration.millis(50));
                sequence.getChildren().add(smallPause);

                PauseTransition highlight = createPause(STEP_DELAY, () -> 
                    highlightBucket(index, Color.YELLOW)
                );
                sequence.getChildren().add(highlight);

                PauseTransition holdColor = new PauseTransition(Duration.millis(FLASH_DURATION));
                sequence.getChildren().add(holdColor);
                
                PauseTransition setWhite = createPause(100, () -> 
                    restoreBucketStyle(index)
                );
                sequence.getChildren().add(setWhite);

                currentStep++;
            }
            else if (event instanceof BucketAcceptedEvent) {
                BucketAcceptedEvent bae = (BucketAcceptedEvent) event;
                int index = bae.getIndex();
                // Flash green to show success
                PauseTransition setGreen = createPause(0, () ->
                    highlightBucket(index, Color.LIGHTGREEN)
                );
                sequence.getChildren().add(setGreen);

                PauseTransition holdGreen = new PauseTransition(Duration.millis(FLASH_DURATION));
                sequence.getChildren().add(holdGreen);

                PauseTransition setWhite = createPause(0, () ->
                    restoreBucketStyle(index)
                );
                sequence.getChildren().add(setWhite);
            }
            else if (event instanceof CollisionBlockedEvent) {
                CollisionBlockedEvent cbe = (CollisionBlockedEvent) event;
                int index = cbe.getIndex();
                
                PauseTransition setRed = createPause(0, () ->
                    highlightBucket(index, Color.LIGHTCORAL)
                );
                sequence.getChildren().add(setRed);

                PauseTransition holdColor = new PauseTransition(Duration.millis(FLASH_DURATION));
                sequence.getChildren().add(holdColor);

                PauseTransition setWhite = createPause(0, () ->
                    restoreBucketStyle(index)
                );
                sequence.getChildren().add(setWhite);
            }
        }
        
        sequence.setOnFinished(e -> {
            controller.setProbeStep(0);
            originalStyles.clear();
            onComplete.run();
        });
        sequence.play();
    }
    
    private PauseTransition createPause(double millis, Runnable action) {
        PauseTransition pause = new PauseTransition(Duration.millis(millis));
        pause.setOnFinished(e -> action.run());
        return pause;
    }
    
    private void highlightBucket(int index, Color color) {
        if (controller.getCollisionResolver().getVisualizationType() == 
            Backend.HashTable.CollisionResolver.CollisionResolver.VisualizationType.CHAINING) {
            highlightChainingBucket(index, color);
        } else {
            highlightOpenAddressingBucket(index, color);
        }
    }
    
    private void restoreBucketStyle(int index) {
        if (controller.getCollisionResolver().getVisualizationType() == 
            Backend.HashTable.CollisionResolver.CollisionResolver.VisualizationType.CHAINING) {
            restoreChainingBucketStyle(index);
        } else {
            restoreOpenAddressingBucketStyle(index);
        }
    }
    
    private String getOpenAddressingKey(int index) {
        return "OPEN_" + index;
    }
    
    private String getChainingKey(int index) {
        return "CHAIN_" + index;
    }
    
    private void highlightOpenAddressingBucket(int index, Color color) {
        GridPane grid = controller.getHorizontalGrid();
        if (grid == null) return;
        
        String key = getOpenAddressingKey(index);
        
        grid.getChildren().stream()
            .filter(node -> node instanceof VBox)
            .filter(node -> GridPane.getColumnIndex(node) != null && 
                          GridPane.getColumnIndex(node) == index &&
                          GridPane.getRowIndex(node) != null &&
                          GridPane.getRowIndex(node) == 1)
            .findFirst()
            .ifPresent(node -> {
                VBox vbox = (VBox) node;
                if (!originalStyles.containsKey(key)) {
                    originalStyles.put(key, vbox.getStyle());
                }
                String colorString = toHexString(color);
                vbox.setStyle("-fx-border-color: black; -fx-border-width: 2; " +
                            "-fx-background-color: " + colorString + "; -fx-padding: 10;");
            });
    }
    
    private void restoreOpenAddressingBucketStyle(int index) {
        GridPane grid = controller.getHorizontalGrid();
        if (grid == null) return;
        
        String key = getOpenAddressingKey(index);
        
        grid.getChildren().stream()
            .filter(node -> node instanceof VBox)
            .filter(node -> GridPane.getColumnIndex(node) != null && 
                          GridPane.getColumnIndex(node) == index &&
                          GridPane.getRowIndex(node) != null &&
                          GridPane.getRowIndex(node) == 1)
            .findFirst()
            .ifPresent(node -> {
                VBox vbox = (VBox) node;
                String originalStyle = originalStyles.get(key);
                if (originalStyle != null) {
                    vbox.setStyle(originalStyle);
                } else {
                    vbox.setStyle("-fx-border-color: black; -fx-border-width: 2; " +
                                "-fx-background-color: white; -fx-padding: 10;");
                }
            });
    }
    
    private void highlightChainingBucket(int index, Color color) {
        GridPane grid = controller.getChainingGrid();
        if (grid == null) return;
        
        String key = getChainingKey(index);
        
        grid.getChildren().stream()
            .filter(node -> node instanceof VBox)
            .filter(node -> GridPane.getRowIndex(node) != null && 
                          GridPane.getRowIndex(node) == index &&
                          GridPane.getColumnIndex(node) != null &&
                          GridPane.getColumnIndex(node) == 0)
            .findFirst()
            .ifPresent(node -> {
                VBox vbox = (VBox) node;
                if (!originalStyles.containsKey(key)) {
                    originalStyles.put(key, vbox.getStyle());
                }
                String colorString = toHexString(color);
                vbox.setStyle("-fx-border-color: black; -fx-border-width: 2; " +
                            "-fx-background-color: " + colorString + "; -fx-padding: 10;");
            });
    }
    
    private void restoreChainingBucketStyle(int index) {
        GridPane grid = controller.getChainingGrid();
        if (grid == null) return;
        
        String key = getChainingKey(index);
        
        grid.getChildren().stream()
            .filter(node -> node instanceof VBox)
            .filter(node -> GridPane.getRowIndex(node) != null && 
                          GridPane.getRowIndex(node) == index &&
                          GridPane.getColumnIndex(node) != null &&
                          GridPane.getColumnIndex(node) == 0)
            .findFirst()
            .ifPresent(node -> {
                VBox vbox = (VBox) node;
                String originalStyle = originalStyles.get(key);
                if (originalStyle != null) {
                    vbox.setStyle(originalStyle);
                } else {
                    vbox.setStyle("-fx-border-color: black; -fx-border-width: 2; " +
                                "-fx-background-color: white; -fx-padding: 10;");
                }
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