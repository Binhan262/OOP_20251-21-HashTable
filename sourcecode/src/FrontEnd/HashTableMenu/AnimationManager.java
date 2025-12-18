package FrontEnd.HashTableMenu;

import javafx.beans.property.*;

public class AnimationManager {

    private static final BooleanProperty animating =
        new SimpleBooleanProperty(false);

    public static BooleanProperty animatingProperty() {
        return animating;
    }

    public static boolean isAnimating() {
        return animating.get();
    }

    static void setAnimating(boolean value) {
        animating.set(value);
    }
}