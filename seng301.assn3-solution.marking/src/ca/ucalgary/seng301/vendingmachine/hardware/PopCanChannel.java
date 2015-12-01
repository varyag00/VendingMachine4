package ca.ucalgary.seng301.vendingmachine.hardware;

import ca.ucalgary.seng301.vendingmachine.PopCan;

/**
 * Represents the hardware through which a pop can is carried from one device to
 * another. Once the hardware is configured, pop can channels will not be used
 * directly by other applications.
 */
public final class PopCanChannel implements AbstractPopCanAcceptor {
    private AbstractPopCanAcceptor sink;

    /**
     * Creates a new pop can channel whose output will go to the indicated sink.
     */
    public PopCanChannel(AbstractPopCanAcceptor sink) {
	this.sink = sink;
    }

    /**
     * This method should only be called from simulated hardware devices.
     * 
     * @throws CapacityExceededException
     *             if the output sink cannot accept the pop.
     * @throws DisabledException
     *             if the output sink is currently disabled.
     */
    @Override
    public void acceptPop(PopCan pop) throws CapacityExceededException,
	    DisabledException {
	sink.acceptPop(pop);
    }
}
