package ca.ucalgary.seng301.vendingmachine.hardware;

/**
 * Listens for events emanating from a selection button.
 */
public interface ButtonListener extends
        AbstractHardwareListener {
    /**
     * An event that is announced to the listener when the indicated button has
     * been pressed.
     */
    void pressed(Button button);
}
