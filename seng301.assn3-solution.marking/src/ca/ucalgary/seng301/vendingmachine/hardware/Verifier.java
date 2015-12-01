package ca.ucalgary.seng301.vendingmachine.hardware;

import java.util.List;

import ca.ucalgary.seng301.myvendingmachine.test.WRAPPER;
import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.PopCan;

public class Verifier implements CoinSlotListener, SelectionButtonListener, DeliveryChuteListener, CoinRackListener, CoinReceptacleListener,
		      PopCanRackListener {
    private VendingMachine vm;

    enum State {
		WAIT,
		LOAD,
		UNLOAD
    }

    private State state = State.WAIT;
    private boolean[] pops, coins, receptacles;
    private StringBuilder sb = new StringBuilder();

    public Verifier(VendingMachine vm, int[] coinKinds, int selectionButtonCount, int coinRackCapacity, int popRackCapacity, int receptacleCapacity) {
	this.vm = vm;
	
	WRAPPER.actual = sb;

	pops = new boolean[selectionButtonCount];
	coins = new boolean[coinKinds.length];
	receptacles = new boolean[2];

	
	// construct(5, 10, 25, 100; 3; 10; 10; 10)
	sb.append("construct(");

	sb.append(coinKinds[0]);
	for(int i = 1, size = coinKinds.length; i < size; i++)
	    sb.append(", " + coinKinds[i]);

	sb.append("; " + selectionButtonCount);
	sb.append("; " + coinRackCapacity);
	sb.append("; " + popRackCapacity);
	sb.append("; " + receptacleCapacity + ")\n");
    }
    
    public void register() {
	vm.getCoinSlot().register(this);
	for(int i = 0, size = vm.getNumberOfSelectionButtons(); i < size; i++)
	    vm.getSelectionButton(i).register(this);
	vm.getDeliveryChute().register(this);
	for(int i = 0, size = vm.getNumberOfCoinRacks(); i < size; i++)
	    vm.getCoinRack(i).register(this);
	for(int i = 0, size = vm.getNumberOfPopCanRacks(); i < size; i++)
	    vm.getPopCanRack(i).register(this);
	vm.getCoinReceptacle().register(this);
	vm.getStorageBin().register(this);
    }

    @Override
    public void enabled(AbstractHardware<AbstractHardwareListener> hardware) {
    }

    @Override
    public void disabled(AbstractHardware<AbstractHardwareListener> hardware) {
    }

    @Override
    public void itemDelivered(DeliveryChute chute) {
    }

    @Override
    public void doorOpened(DeliveryChute chute) {
    }

    @Override
    public void doorClosed(DeliveryChute chute) {
	sb.append("extract()\n");
    }

    @Override
    public void chuteFull(DeliveryChute chute) {
    }

    @Override
    public void pressed(SelectionButton button) {
	int index = -1;

	for(int i = 0, size = vm.getNumberOfSelectionButtons(); i < size; i++) {
	    if(vm.getSelectionButton(i) == button) {
		index = i;
		break;
	    }
	}

	sb.append("press(" + index + ")\n");
    }

    @Override
    public void validCoinInserted(CoinSlot coinSlotSimulator, Coin coin) {
	sb.append("insert(" + coin.getValue() + ")\n");
    }

    @Override
    public void coinRejected(CoinSlot coinSlotSimulator, Coin coin) {
	sb.append("insert(" + coin.getValue() + ")\n");
    }

    @Override
    public void popAdded(PopCanRack popRack, PopCan pop) {
    }

    @Override
    public void popRemoved(PopCanRack popRack, PopCan pop) {
    }

    @Override
    public void popFull(PopCanRack popRack) {
    }

    @Override
    public void popEmpty(PopCanRack popRack) {
    }

    @Override
    public void coinAdded(CoinReceptacle receptacle, Coin coin) {
    }

    @Override
    public void coinsRemoved(CoinReceptacle receptacle) {
    }

    @Override
    public void coinsFull(CoinReceptacle receptacle) {
    }

    @Override
    public void coinsFull(CoinRack rack) {
    }

    @Override
    public void coinsEmpty(CoinRack rack) {
    }

    @Override
    public void coinAdded(CoinRack rack, Coin coin) {
    }

    @Override
    public void coinRemoved(CoinRack rack, Coin coin) {
    }

    private int getIndex(CoinRack rack) {
	int i = 0;
	while(true) {
	    if(vm.getCoinRack(i) == rack)
		return i;

	    i++;
	}
    }

    @Override
    public void unload(CoinRack rack, List<Coin> result) {
	switch(state) {
	case WAIT:
	    state = State.UNLOAD;
	    coins[getIndex(rack)] = true;
	    break;

	case LOAD:
	    sb.append("FAIL: Unload expected\n");
	    break;

	default:
	    int index = getIndex(rack);
	    if(coins[index]) {
		sb.append("FAIL: Duplicate unload\n");
		break;
	    }

	    coins[index] = true;
	}

	check();
    }

    @Override
    public void load(CoinRack rack, Coin... coins) {
	switch(state) {
	case WAIT:
	    state = State.LOAD;
	    this.coins[getIndex(rack)] = true;
	    break;

	case UNLOAD:
	    sb.append("FAIL: Load expected\n");
	    break;

	default:
	    int index = getIndex(rack);
	    if(this.coins[index]) {
		sb.append("FAIL: Duplicate load\n");
		break;
	    }

	    this.coins[index] = true;
	}

	check();
    }

    private int getIndex(PopCanRack rack) {
	int i = 0;
	while(true) {
	    if(vm.getPopCanRack(i) == rack)
		return i;

	    i++;
	}
    }

    @Override
    public void unload(PopCanRack popRack, List<PopCan> cans) {
	switch(state) {
	case WAIT:
	    state = State.UNLOAD;
	    pops[getIndex(popRack)] = true;
	    break;

	case LOAD:
	    sb.append("FAIL: Unload expected\n");
	    break;

	default:
	    int index = getIndex(popRack);
	    if(pops[index]) {
		sb.append("FAIL: Duplicate unload\n");
		break;
	    }

	    pops[index] = true;
	}

	check();
    }

    @Override
    public void load(PopCanRack popRack, PopCan... cans) {
	switch(state) {
	case WAIT:
	    state = State.LOAD;
	    pops[getIndex(popRack)] = true;
	    break;

	case UNLOAD:
	    sb.append("FAIL: Load expected\n");
	    break;

	default:
	    int index = getIndex(popRack);
	    if(pops[index]) {
		sb.append("FAIL: Duplicate unload\n");
		break;
	    }

	    pops[index] = true;
	}

	check();
    }

    @Override
    public void unload(CoinReceptacle receptacle, List<Coin> coins) {
	switch(state) {
	case WAIT:
	    state = State.UNLOAD;
	    reinit();
	    receptacles[getIndex(receptacle)] = true;
	    break;

	case LOAD:
	    sb.append("FAIL: load expected\n");
	    break;

	default:
	    int index = getIndex(receptacle);
	    if(receptacles[index]) {
		sb.append("FAIL: Repeated unloading\n");
		break;
	    }

	    receptacles[index] = true;
	}

	check();
    }

    private void check() {
	boolean done = true;

	switch(state) {
	case LOAD:
	    for(int i = 0, size = coins.length; i < size; i++)
		done &= coins[i];
	    for(int i = 0, size = pops.length; i < size; i++)
		done &= pops[i];
	    break;

	case UNLOAD:
	    for(int i = 0, size = coins.length; i < size; i++)
		done &= coins[i];
	    for(int i = 0, size = pops.length; i < size; i++)
		done &= pops[i];
	    done &= receptacles[0];
	    done &= receptacles[1];
	    break;

	default:
	    return;
	}

	if(done)
	    switch(state) {
	    case LOAD:
		sb.append("load()\n");
		break;
	    case UNLOAD:
		sb.append("unload()\n");
		break;
	    default:
		return;
	    }

	state = State.WAIT;
	reinit();
    }

    private int getIndex(CoinReceptacle receptacle) {
	if(receptacle == vm.getCoinReceptacle())
	    return 0;
	else
	    return 1;
    }

    @Override
    public void load(CoinReceptacle receptacle, Coin... coins) {
	sb.append("FAIL: Receptacle being loaded\n");
    }

    private void reinit() {
	for(int i = 0, size = pops.length; i < size; i++)
	    pops[i] = false;

	for(int i = 0, size = coins.length; i < size; i++)
	    coins[i] = false;

	receptacles[0] = false;
	receptacles[1] = false;
    }
}
