package ca.ucalgary.seng301.vendingmachine.hardware;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ca.ucalgary.seng301.vendingmachine.Product;

/**
 * Represents a storage rack for products within the vending machine. More than
 * one would typically exist within the same vending machine. The product rack
 * has finite, positive capacity. A product rack can be disabled, which prevents
 * it from dispensing products.
 */
public final class ProductRack extends AbstractHardware<ProductRackListener> {
    private int maxCapacity;
    private Queue<Product> queue = new LinkedList<Product>();
    private ProductChannel sink;

    /**
     * Creates a new product rack with the indicated maximum capacity. The product
     * rack initially is empty.
     * 
     * @param capacity
     *            Positive integer indicating the maximum capacity of the rack.
     * @throws SimulationException
     *             if the indicated capacity is not positive.
     */
    public ProductRack(int capacity) {
	if(capacity <= 0)
	    throw new SimulationException("Capacity cannot be non-positive: " + capacity);

	this.maxCapacity = capacity;
    }

    public int size() {
	return queue.size();
    }

    /**
     * Returns the maximum capacity of this product rack. Causes no events.
     */
    public int getCapacity() {
	return maxCapacity;
    }

    /**
     * Connects the product rack to an outlet channel, such as the delivery
     * chute. Causes no events.
     * 
     * @param sink
     *            The channel to be used as the outlet for dispensed products.
     */
    public void connect(ProductChannel sink) {
	this.sink = sink;
    }

    /**
     * Adds the indicated product to this product rack if there is sufficient
     * space available. If the product is successfully added to this product
     * rack, a "productAdded" event is announced to its listeners. If, as a result
     * of adding this product, this product rack has become full, a "productFull"
     * event is announced to its listeners.
     * 
     * @param product
     *            The product to be added.
     * @throws CapacityExceededException
     *             if the product rack is already full.
     * @throws DisabledException
     *             if the product rack is currently disabled.
     */
    public void addProduct(Product product) throws CapacityExceededException,
	    DisabledException {
	if(isDisabled())
	    throw new DisabledException();

	if(queue.size() >= maxCapacity)
	    throw new CapacityExceededException();

	queue.add(product);

	notifyProductAdded(product);

	if(queue.size() >= maxCapacity)
	    notifyProductFull();
    }

    /**
     * Causes one product to be removed from this product rack, to be placed in
     * the output channel to which this product rack is connected. If a product
     * is removed from this product rack, a "productRemoved" event is announced to
     * its listeners. If the removal of the product causes this product rack to
     * become empty, a "productEmpty" event is announced to its listeners.
     * 
     * @throws DisabledException
     *             if this product rack is currently disabled.
     * @throws EmptyException
     *             if no products are currently contained in this product rack.
     * @throws CapacityExceededException
     *             if the output channel cannot accept the dispensed product.
     */
    public void dispenseProduct() throws DisabledException, EmptyException,
	    CapacityExceededException {
	if(isDisabled())
	    throw new DisabledException();

	if(queue.isEmpty())
	    throw new EmptyException();

	Product product = queue.remove();
	notifyProductRemoved(product);

	if(sink == null)
	    throw new SimulationException("The output channel is not connected");

	sink.acceptProduct(product);

	if(queue.isEmpty())
	    notifyProductEmpty();
    }

    /**
     * Allows products to be loaded into the product rack without causing events
     * to occur on its listeners. This permits a simple initialization. Note
     * that any existing products in the rack are not removed.
     * 
     * @param products
     *            One or more products to be loaded into this product rack.
     * @throws SimulationException
     *             if the number of cans to be loaded exceeds the capacity of
     *             this product rack.
     */
    public void loadWithoutEvents(Product... products) throws SimulationException {
	if(maxCapacity < queue.size() + products.length)
	    throw new SimulationException("Capacity exceeded by attempt to load");

	for(Product product : products) {
	    queue.add(product);
	}
    }

    /**
     * Unloads products from the rack without causing events to be announced.
     */
    public List<Product> unloadWithoutEvents() {
	List<Product> result = new ArrayList<>(queue);
	queue.clear();
	return result;
    }

    private void notifyProductAdded(Product product) {
	Class<?>[] parameterTypes =
	        new Class<?>[] { ProductRack.class, Product.class };
	Object[] args = new Object[] { this, product };
	notifyListeners(ProductRackListener.class, "productAdded", parameterTypes, args);
    }

    private void notifyProductFull() {
	Class<?>[] parameterTypes =
	        new Class<?>[] { ProductRack.class };
	Object[] args = new Object[] { this };
	notifyListeners(ProductRackListener.class, "productFull", parameterTypes, args);
    }

    private void notifyProductEmpty() {
	Class<?>[] parameterTypes =
	        new Class<?>[] { ProductRack.class };
	Object[] args = new Object[] { this };
	notifyListeners(ProductRackListener.class, "productEmpty", parameterTypes, args);
    }

    private void notifyProductRemoved(Product product) {
	Class<?>[] parameterTypes =
	        new Class<?>[] { ProductRack.class, Product.class };
	Object[] args = new Object[] { this, product };
	notifyListeners(ProductRackListener.class, "productRemoved", parameterTypes, args);
    }
}
