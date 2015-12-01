package ca.ucalgary.seng301.vendingmachine.hardware;

/**
 * Listens for events emanating from a display device.
 */
public interface DisplayListener extends AbstractHardwareListener {
    /**
     * Event that announces that the message on the indicated display has
     * changed.
     */
    void messageChange(Display display, String oldMsg, String newMsg);
}
