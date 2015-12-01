package ca.ucalgary.seng301.vendingmachine.hardware;

import ca.ucalgary.seng301.vendingmachine.Product;

/**
 * Listens for events emanating from a product rack.
 */
public interface ProductRackListener extends AbstractHardwareListener {
    /**
     * An event announced when the indicated product is added to the indicated
     * product rack.
     */
    void productAdded(ProductRack productRack, Product product);

    /**
     * An event announced when the indicated product is removed from the
     * indicated product rack.
     */
    void productRemoved(ProductRack productRack, Product product);

    /**
     * An event announced when the indicated product rack becomes full.
     */
    void productFull(ProductRack productRack);

    /**
     * An event announced when the indicated product rack becomes empty.
     */
    void productEmpty(ProductRack productRack);
}
