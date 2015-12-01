package ca.ucalgary.seng301.vendingmachine;

/**
 * Represents a product.
 */
public final class Product {
    private String name;
    
    /**
     * Constructor for a product.
     * 
     * @param name Represents the brand name of the product.
     * @throws NullPointerException if name is null.
     */
    public Product(String name) {
	if(name == null)
	    throw new NullPointerException();
	
	this.name = name;
    }
    
    public String getName() {
	return name;
    }
}
