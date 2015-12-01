package ca.ucalgary.seng301.vendingmachine.hardware;

/**
 * Listens for events emanating from a delivery chute.
 */
public interface DeliveryChuteListener extends AbstractHardwareListener {
    /**
     * Indicates that an item has been delivered to the indicated delivery
     * chute.
     */
    void itemDelivered(DeliveryChute chute);

    /**
     * Indicates that the door of the indicated delivery chute has been opened.
     */
    void doorOpened(DeliveryChute chute);

    /**
     * Indicates that the door of the indicated delivery chute has been closed.
     */
    void doorClosed(DeliveryChute chute);

    /**
     * Indicates that the delivery chute will not be able to hold any more
     * items.
     */
    void chuteFull(DeliveryChute chute);
}
