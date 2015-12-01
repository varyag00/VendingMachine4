package ca.ucalgary.seng301.vendingmachine.hardware;

import java.util.List;

import ca.ucalgary.seng301.vendingmachine.PopCan;

/**
 * Listens for events emanating from a pop can rack.
 */
public interface PopCanRackListener extends AbstractHardwareListener {
    /**
     * An event announced when the indicated pop can is added to the indicated
     * pop can rack.
     */
    void popAdded(PopCanRack popRack, PopCan pop);

    /**
     * An event announced when the indicated pop can is removed from the
     * indicated pop can rack.
     */
    void popRemoved(PopCanRack popRack, PopCan pop);

    /**
     * An event announced when the indicated pop can rack becomes full.
     */
    void popFull(PopCanRack popRack);

    /**
     * An event announced when the indicated pop can rack becomes empty.
     */
    void popEmpty(PopCanRack popRack);
    
    void unload(PopCanRack popRack, List<PopCan> cans);
    
    void load(PopCanRack popRack, PopCan...cans);
}
