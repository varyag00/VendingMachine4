package ca.ucalgary.seng301.vendingmachine.hardware;

import ca.ucalgary.seng301.vendingmachine.Coin;

/**
 * Listens for events emanating from a coin receptacle.
 */
public interface CoinReceptacleListener extends AbstractHardwareListener {
    /**
     * An event that announces that the indicated coin has been added to the
     * indicated receptacle.
     */
    void coinAdded(CoinReceptacle receptacle, Coin coin);

    /**
     * An event that announces that all coins have been removed from the
     * indicated receptacle.
     */
    void coinsRemoved(CoinReceptacle receptacle);

    /**
     * An event that announces that the indicated receptacle is now full.
     */
    void coinsFull(CoinReceptacle receptacle);
}
