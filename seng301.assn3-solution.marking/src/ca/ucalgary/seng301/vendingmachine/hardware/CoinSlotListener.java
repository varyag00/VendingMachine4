package ca.ucalgary.seng301.vendingmachine.hardware;

import ca.ucalgary.seng301.vendingmachine.Coin;

/**
 * Listens for events emanating from a coin slot.
 */
public interface CoinSlotListener extends AbstractHardwareListener {
    /**
     * An event announcing that the indicated, valid coin has been inserted and
     * successfully delivered to the storage device connected to the indicated
     * coin slot.
     */
    void validCoinInserted(CoinSlot coinSlotSimulator, Coin coin);

    /**
     * An event announcing that the indicated coin has been returned.
     */
    void coinRejected(CoinSlot coinSlotSimulator, Coin coin);
}
