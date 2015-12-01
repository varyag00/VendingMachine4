package ca.ucalgary.seng301.vendingmachine.hardware;

import ca.ucalgary.seng301.vendingmachine.Product;

/**
 * Represents the hardware through which a product is carried from one device to
 * another. Once the hardware is configured, product channels will not be used
 * directly by other applications.
 */
public final class ProductChannel implements AbstractProductAcceptor {
    private AbstractProductAcceptor sink;

    /**
     * Creates a new product channel whose output will go to the indicated sink.
     */
    public ProductChannel(AbstractProductAcceptor sink) {
	this.sink = sink;
    }

    /**
     * This method should only be called from simulated hardware devices.
     * 
     * @throws CapacityExceededException
     *             if the output sink cannot accept the product.
     * @throws DisabledException
     *             if the output sink is currently disabled.
     */
    @Override
    public void acceptProduct(Product product) throws CapacityExceededException,
	    DisabledException {
	sink.acceptProduct(product);
    }
}
