package ca.ucalgary.seng301.vendingmachine.hardware;

import ca.ucalgary.seng301.vendingmachine.Coin;

/**
 * A simple interface to allow a device to communicate with another device that
 * accepts coins.
 */
public interface AbstractCoinAcceptor {
    /**
     * Instructs the device to take the coin as input.
     * 
     * @param coin
     *            The coin to be taken as input.
     * @throws CapacityExceededException
     *             If the device does not have enough space for the coin.
     * @throws DisabledException 
     */
    void acceptCoin(Coin coin) throws CapacityExceededException, DisabledException;

    /**
     * Checks whether the device has enough space to expect one more item. If
     * this method returns true, an immediate call to acceptCoin should not
     * throw CapacityExceededException, unless an asynchronous addition has
     * occurred in the meantime.
     * 
     * @return true if there is space, false if there is not space
     */
    boolean hasSpace();
}
