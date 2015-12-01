package ca.ucalgary.seng301.vendingmachine;

import java.util.List;

import ca.ucalgary.seng301.vendingmachine.hardware.DisabledException;

/**
 * This interface specifies a set of event callbacks that your vending machine
 * implementation has to implement. The frontend calls these callbacks when the
 * corresponding script commands are encountered.
 */
public interface IVendingMachineFactory {
    /**
     * Constructs a vending machine that accepts a specified set of coin kinds
     * (positive integer denominations) and a certain number of pop selection
     * buttons. Coin kinds must have unique denominations; their order is used
     * for later reference, but it is otherwise free. The constructed vending
     * machine is then used for all further interactions until and unless a new
     * vending machine is constructed. The system starts WITHOUT any vending
     * machine constructed by default.
     * 
     * <p>
     * Coins for change are stored in coin racks of finite, positive capacity.
     * These are automatically replenished from coins entered by the customer
     * that have been used for payment.
     * 
     * <p>
     * Pop cans are stored in pop can racks of finite, positive capacity.
     * 
     * <p>
     * Coins used as payment are temporarily stored in a receptacle called the
     * coin receptacle. When these coins are used for a purchase, they are
     * transferred elsewhere: some may be returned as change, some can be stored
     * in the coin racks to replenish the stocks of change, and some can be
     * stored in the overflow receptacle (called the storage bin).
     * 
     * @throws IllegalArgumentException
     *             if any of the coin kinds is not positive, if the selection
     *             button count is not positive, or if either capacity is not
     *             positive
     * @throws IllegalStateException
     *             if the coin kinds do not have unique denominations
     */
    public void construct(List<Integer> coinKinds, int selectionButtonCount, int coinRackCapacity, int popCanRackCapacity, int receptacleCapacity);

    /**
     * Configures the current vending machine to use the indicated names and
     * costs for the pop kinds related to the selection buttons at the indexed
     * position. For example, the string at position 0 of popNames is to be used
     * as the name of the pops loaded into the vending machine that would be
     * vended by selection button 0. Pop names and pop costs DO NOT need to be
     * unique.
     * 
     * @throws IllegalArgumentException
     *             if the arguments are null, if the number of elements in
     *             either list is not identical to the number of selection
     *             buttons for the vending machine, if a pop name is not a
     *             string at least 1 character long, or if a pop cost is not a
     *             positive integer
     */
    public void configure(List<String> popNames, List<Integer> popCosts);

    /**
     * A set of coins and a set of pops is ADDED to the current vending machine.
     * The two lists will indicate the number of the items of the corresponding
     * type to be added. For example, the integer at position 0 of coinCounts
     * will indicate how many coins of type 0 are to be added. Coin and pop
     * types are determined at construction time.
     * 
     * @throws IllegalArgumentException
     *             if any of the counts are negative
     * @throws NullPointerException
     *             if either of the arguments is null
     */
    public void load(List<Integer> coinCounts, List<Integer> popCounts);

    /**
     * Called to remove all coins and pops from the VM, storing them as
     * specified within {@link VendingMachineStoredContents}. Should not prevent
     * the VM from continuing to function.
     */
    public VendingMachineStoredContents unload();

    /**
     * Called to remove all coins and pops from the delivery chute of the VM,
     * returning them in whatever order is convenient (as Coins and Pop
     * instances). Should not prevent the VM from continuing to function.
     */
    public List<Object> extract();

    /**
     * Called when a coin of the specified value is inserted in the VM.
     * 
     * @throws DisabledException
     */
    public void insert(int value) throws DisabledException;

    /**
     * Called when the specified button is pressed. Buttons are numbered
     * starting from 0. Each button corresponds to a Pop type created
     * 
     * @param value
     */
    public void press(int value);
}
