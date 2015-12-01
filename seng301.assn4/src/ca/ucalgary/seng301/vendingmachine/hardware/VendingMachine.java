package ca.ucalgary.seng301.vendingmachine.hardware;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a standard configuration of the vending machine hardware:
 * <ul>
 * <li>one coin slot;</li>
 * <li>one coin receptacle (called the coin receptacle) to temporarily store
 * coins entered by the user;</li>
 * <li>one coin receptacle (called the storage bin) to store coins that have
 * been accepted as payment;</li>
 * <li>a set of one or more coin racks (the number and the denomination of coins
 * stored by each is specified in the constructor);</li>
 * <li>one delivery chute used to deliver products and to return coins;</li>
 * <li>a set of one or more product racks (the number, cost, and product name
 * stored in each is specified in the constructor);</li>
 * <li>one textual display;</li>
 * <li>one money return button;</li>
 * <li>a set of one or more selection buttons (exactly one per product rack);
 * and</li>
 * <li>two indicator lights: one to indicate that exact change should be used by
 * the user; the other to indicate that the machine is out of order.</li>
 * </ul>
 * <p>
 * The component devices are interconnected as follows:
 * <ul>
 * <li>the output of the coin slot is connected to the input of the coin
 * receptacle;</li>
 * <li>the outputs of the coin receptacle are connected to the inputs of the
 * coin racks (for valid coins to be stored therein), the delivery chute (for
 * invalid coins or other coins to be returned), and the storage bin (for coins
 * to be accepted that do not fit in the coin racks);</li>
 * <li>the output of each coin rack is connected to the delivery chute; and</li>
 * <li>the output of each product rack is connected to the delivery chute.</li>
 * </ul>
 * <p>
 * Each component device can be disabled to prevent any physical movements.
 * Other functionality is not affected by disabling a device; hence devices that
 * do not involve physical movements are not affected by "disabling" them.
 * <p>
 * Most component devices have some sort of maximum capacity (e.g., of the
 * number of products that can be stored therein). In some cases, this is a
 * simplification of the physical reality for the sake of simulation.
 */
public final class VendingMachine {
    private boolean safetyOn = false;

    private int[] coinKinds;
    private CoinSlot coinSlot;
    private CoinReceptacle receptacle, storageBin;
    private DeliveryChute deliveryChute;
    private CoinRack[] coinRacks;
    private Map<Integer, CoinChannel> coinRackChannels;
    private ProductRack[] productRacks;
    private Display display;
    private Button returnButton;
    private Button[] selectionButtons;
    private int[] productCosts;
    private String[] productNames;
    private IndicatorLight exactChangeLight, outOfOrderLight;

    public static VendingMachine vm;

    /**
     * Creates a standard arrangement for the vending machine. All the
     * components are created and interconnected. The machine is initially
     * empty. The product kind names and costs are initialized to "" and 1
     * respectively.
     * 
     * <p>
     * All product kinds
     * 
     * @param coinKinds
     *            The values (in cents) of each kind of coin. The order of the
     *            kinds is maintained. One coin rack is produced for each kind.
     *            Each kind must have a unique, positive value.
     * @param coinRackCapacity
     *            The maximum capacity of each coin rack in the machine. Must be
     *            positive.
     * @param productRackCapacity
     *            The maximum capacity of each product rack in the machine. Must be
     *            positive.
     * @param receptacleCapacity
     *            The maximum capacity of the coin receptacle, storage bin, and
     *            delivery chute. Must be positive.
     * @throws SimulationException
     *             if any of the arguments is null or &lt; 1.
     */
    public VendingMachine(int[] coinKinds, int selectionButtonCount, int coinRackCapacity, int productRackCapacity, int receptacleCapacity) {
	if(coinKinds == null)
	    throw new SimulationException("Arguments may not be null");

	if(selectionButtonCount < 1 || coinRackCapacity < 1 || productRackCapacity < 1)
	    throw new SimulationException("Counts and capacities must be positive");

	if(coinKinds.length < 1)
	    throw new SimulationException("At least one coin kind must be accepted");

	this.coinKinds = Arrays.copyOf(coinKinds, coinKinds.length);

	Set<Integer> currentCoinKinds = new HashSet<>();
	for(int coinKind : coinKinds) {
	    if(coinKind < 1)
		throw new SimulationException("Coin kinds must have positive values");

	    if(currentCoinKinds.contains(coinKind))
		throw new SimulationException("Coin kinds must have unique values");

	    currentCoinKinds.add(coinKind);
	}

	display = new Display();

	coinSlot = new CoinSlot(coinKinds);

	receptacle = new CoinReceptacle(receptacleCapacity);

	storageBin = new CoinReceptacle(receptacleCapacity);

	deliveryChute = new DeliveryChute(receptacleCapacity);

	coinRacks = new CoinRack[coinKinds.length];
	coinRackChannels = new HashMap<Integer, CoinChannel>();
	for(int i = 0; i < coinKinds.length; i++) {
	    coinRacks[i] = new CoinRack(coinRackCapacity);
	    coinRacks[i].connect(new CoinChannel(deliveryChute));

	    coinRackChannels.put(new Integer(coinKinds[i]), new CoinChannel(coinRacks[i]));
	}

	productRacks = new ProductRack[selectionButtonCount];
	for(int i = 0; i < selectionButtonCount; i++) {
	    productRacks[i] = new ProductRack(productRackCapacity);
	    productRacks[i].connect(new ProductChannel(deliveryChute));
	}

	productNames = new String[selectionButtonCount];
	for(int i = 0; i < selectionButtonCount; i++)
	    productNames[i] = "";
	productCosts = new int[selectionButtonCount];
	for(int i = 0; i < selectionButtonCount; i++)
	    productCosts[i] = 1;

	returnButton = new Button();
	selectionButtons = new Button[selectionButtonCount];
	for(int i = 0; i < selectionButtonCount; i++) {
	    selectionButtons[i] = new Button();
	}

	coinSlot.connect(new CoinChannel(receptacle), new CoinChannel(deliveryChute));
	receptacle.connect(coinRackChannels, new CoinChannel(deliveryChute), new CoinChannel(storageBin));

	exactChangeLight = new IndicatorLight();
	outOfOrderLight = new IndicatorLight();

	vm = this;
    }

    public void configure(List<String> productNames, List<Integer> productCosts) {
	if(productNames.size() != this.productNames.length || productCosts.size() != this.productCosts.length)
	    throw new SimulationException("The number of names and costs must be identical to the number of product racks in the machine");

	productNames.toArray(this.productNames);
	for(String productName : productNames)
	    if(productName.equals(""))
		throw new IllegalArgumentException("Product names cannot be the empty string");

	int i = 0;
	for(Integer productCost : productCosts)
	    this.productCosts[i++] = productCost;
    }

    /**
     * Disables all the components of the hardware that involve physical
     * movements. Activates the out of order light.
     */
    public void enableSafety() {
	safetyOn = true;
	coinSlot.disable();
	deliveryChute.disable();

	for(int i = 0; i < productRacks.length; i++)
	    productRacks[i].disable();

	for(int i = 0; i < coinRacks.length; i++)
	    coinRacks[i].disable();

	outOfOrderLight.activate();
    }

    /**
     * Enables all the components of the hardware that involve physical
     * movements. Deactivates the out of order light.
     */
    public void disableSafety() {
	safetyOn = false;
	coinSlot.enable();
	deliveryChute.enable();

	for(int i = 0; i < productRacks.length; i++)
	    productRacks[i].enable();

	for(int i = 0; i < coinRacks.length; i++)
	    coinRacks[i].enable();

	outOfOrderLight.deactivate();
    }

    /**
     * Returns whether the safety is currently engaged.
     */
    public boolean isSafetyEnabled() {
	return safetyOn;
    }

    /**
     * Returns the exact change light.
     */
    public IndicatorLight getExactChangeLight() {
	return exactChangeLight;
    }

    /**
     * Returns the out of order light.
     */
    public IndicatorLight getOutOfOrderLight() {
	return outOfOrderLight;
    }

    /**
     * Returns a selection button at the indicated index.
     * 
     * @param index
     *            The index of the desired selection button.
     * @throws IndexOutOfBoundsException
     *             if the index < 0 or the index >= number of selection buttons.
     */
    public Button getSelectionButton(int index) {
	return selectionButtons[index];
    }

    /**
     * Returns the money return button.
     */
    public Button getReturnButton() {
	return returnButton;
    }

    /**
     * Returns the number of selection buttons.
     */
    public int getNumberOfSelectionButtons() {
	return selectionButtons.length;
    }

    /**
     * Returns the coin slot.
     */
    public CoinSlot getCoinSlot() {
	return coinSlot;
    }

    /**
     * Returns the coin receptacle.
     */
    public CoinReceptacle getCoinReceptacle() {
	return receptacle;
    }

    /**
     * Returns the storage bin.
     */
    public CoinReceptacle getStorageBin() {
	return storageBin;
    }

    /**
     * Returns the delivery chute.
     */
    public DeliveryChute getDeliveryChute() {
	return deliveryChute;
    }

    /**
     * Returns the number of coin racks.
     */
    public int getNumberOfCoinRacks() {
	return coinRacks.length;
    }

    /**
     * Returns a coin rack at the indicated index.
     * 
     * @param index
     *            The index of the desired coin rack.
     * @throws IndexOutOfBoundsException
     *             if the index < 0 or the index >= number of coin racks.
     */
    public CoinRack getCoinRack(int index) {
	return coinRacks[index];
    }

    /**
     * Returns the coin rack that handles coins of the specified kind. If none
     * exists, null is returned.
     * 
     * @param kind
     *            The value of the coin kind for which the rack is sought.
     */
    public CoinRack getCoinRackForKind(int kind) {
	CoinChannel cc = coinRackChannels.get(kind);
	if(cc != null)
	    return (CoinRack)cc.getSink();
	return null;
    }

    public Integer getCoinKindForRack(int index) {
	return coinKinds[index];
    }

    /**
     * Returns the number of product racks.
     */
    public int getNumberOfProductRacks() {
	return productRacks.length;
    }

    /**
     * Returns the name of the product kind whose selection button and rack are at
     * the indicated index.
     * 
     * @param index
     *            The index of the desired product rack/selection button.
     * @throws IndexOutOfBoundsException
     *             if the index < 0 or the index >= number of product rack.
     */
    public String getProductKindName(int index) {
	return productNames[index];
    }

    /**
     * Returns the cost of the product kind whose selection button and rack are at
     * the indicated index.
     * 
     * @param index
     *            The index of the desired product rack/selection button.
     * @throws IndexOutOfBoundsException
     *             if the index < 0 or the index >= number of product rack.
     */
    public int getProductKindCost(int index) {
	return productCosts[index];
    }

    /**
     * Returns a product rack at the indicated index.
     * 
     * @param index
     *            The index of the desired product rack.
     * @throws IndexOutOfBoundsException
     *             if the index < 0 or the index >= number of product rack.
     */
    public ProductRack getProductRack(int index) {
	return productRacks[index];
    }

    /**
     * Returns the textual display.
     */
    public Display getDisplay() {
	return display;
    }
}
