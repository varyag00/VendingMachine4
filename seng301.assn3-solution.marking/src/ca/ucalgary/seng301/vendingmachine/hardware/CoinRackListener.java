package ca.ucalgary.seng301.vendingmachine.hardware;

import java.util.List;

import ca.ucalgary.seng301.vendingmachine.Coin;

/**
 * Listens for events emanating from a coin rack.
 */
public interface CoinRackListener extends AbstractHardwareListener {
    /**
     * Announces that the indicated coin rack is full of coins.
     */
    void coinsFull(CoinRack rack);

    /**
     * Announces that the indicated coin rack is empty of coins.
     */
    void coinsEmpty(CoinRack rack);

    /**
     * Announces that the indicated coin has been added to the indicated coin
     * rack.
     */
    void coinAdded(CoinRack rack, Coin coin);

    /**
     * Announces that the indicated coin has been added to the indicated coin
     * rack.
     */
    void coinRemoved(CoinRack rack, Coin coin);
    
    void load(CoinRack rack, Coin... coins);
    
    void unload(CoinRack rack, List<Coin> coins);
}
