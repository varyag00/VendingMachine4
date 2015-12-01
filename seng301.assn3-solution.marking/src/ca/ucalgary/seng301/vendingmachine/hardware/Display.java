package ca.ucalgary.seng301.vendingmachine.hardware;

/**
 * A simple device that displays a string. How it does this is not part of the
 * simulation. A very long string might scroll continuously, for example.
 */
public final class Display extends AbstractHardware<DisplayListener> {
    private String message = null;

    /**
     * Tells the display to start displaying the indicated message. Announces a
     * "messageChange" event to its listeners.
     */
    public void display(String msg) {
	String oldMsg = message;
	message = msg;
	notifyMessageChange(oldMsg, msg);
    }

    /**
     * Permits the display message to be set without causing events to be
     * announced.
     */
    public void loadWithoutEvents(String message) {
	this.message = message;
    }

    private void notifyMessageChange(String oldMsg, String newMsg) {
	Class<?>[] parameterTypes = new Class<?>[] {Display.class, String.class, String.class};
	Object[] args = new Object[] {this, oldMsg, newMsg};
	notifyListeners(DisplayListener.class, "messageChange", parameterTypes, args);
    }
}
