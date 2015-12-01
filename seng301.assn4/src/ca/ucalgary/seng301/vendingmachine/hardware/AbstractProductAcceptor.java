package ca.ucalgary.seng301.vendingmachine.hardware;

import ca.ucalgary.seng301.vendingmachine.Product;

/**
 * A simple interface to allow a device to communicate with another device that
 * accepts products.
 */
public interface AbstractProductAcceptor {
    /**
     * Instructs the device to take the product as input.
     * 
     * @param product
     *            The product to be taken as input.
     * @throws CapacityExceededException
     *             if the device does not have enough space for the product.
     * @throws DisabledException
     *             if the device is currently disabled.
     */
    public void acceptProduct(Product product) throws CapacityExceededException,
	    DisabledException;
}
