package ca.ucalgary.seng301.vendingmachine.hardware;

/**
 * Represents a simple push button on the vending machine. It ignores the
 * enabled/disabled state.
 */
public final class SelectionButton extends
        AbstractHardware<SelectionButtonListener> {
    /**
     * Simulates the pressing of the button. Notifies its listeners of a
     * "pressed" event.
     */
    public void press() {
	notifyPressed();
    }

    private void notifyPressed() {
	Class<?>[] parameterTypes =
	        new Class<?>[] { SelectionButton.class };
	Object[] args = new Object[] { this };
	notifyListeners(SelectionButtonListener.class, "pressed", parameterTypes, args);
    }
}
