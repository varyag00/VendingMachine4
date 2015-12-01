package ca.ucalgary.seng301.vendingmachine;

/**
 * Represents a pop can. It has no methods or fields because a pop can is a
 * physical object on which computations cannot be performed.
 */
public final class PopCan {
    private String name;
    
    /**
     * Constructor for a pop can.
     * 
     * @param name Represents the brand name of the pop in the can
     * @throws NullPointerException if name is null
     */
    public PopCan(String name) {
	if(name == null)
	    throw new NullPointerException();
	
	this.name = name;
    }
    
    public String getName() {
	return name;
    }
}
