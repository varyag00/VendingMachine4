package ca.ucalgary.seng301.vendingmachine.hardware;

/**
 * Represents a simple push button on the vending machine. It ignores the
 * enabled/disabled state.
 */
public final class Button extends
        AbstractHardware<ButtonListener> {
    /**
     * Simulates the pressing of the button. Notifies its listeners of a
     * "pressed" event.
     */
    public void press() {
	notifyPressed();
    }

    private void notifyPressed() {
	Class<?>[] parameterTypes =
	        new Class<?>[] { Button.class };
	Object[] args = new Object[] { this };
	notifyListeners(ButtonListener.class, "pressed", parameterTypes, args);
    }
}
