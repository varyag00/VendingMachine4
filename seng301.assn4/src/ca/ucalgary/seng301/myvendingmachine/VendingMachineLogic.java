package ca.ucalgary.seng301.myvendingmachine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.hardware.AbstractHardware;
import ca.ucalgary.seng301.vendingmachine.hardware.AbstractHardwareListener;
import ca.ucalgary.seng301.vendingmachine.hardware.CapacityExceededException;
import ca.ucalgary.seng301.vendingmachine.hardware.CoinRack;
import ca.ucalgary.seng301.vendingmachine.hardware.CoinSlot;
import ca.ucalgary.seng301.vendingmachine.hardware.CoinSlotListener;
import ca.ucalgary.seng301.vendingmachine.hardware.DisabledException;
import ca.ucalgary.seng301.vendingmachine.hardware.Display;
import ca.ucalgary.seng301.vendingmachine.hardware.EmptyException;
import ca.ucalgary.seng301.vendingmachine.hardware.IndicatorLight;
import ca.ucalgary.seng301.vendingmachine.hardware.ProductRack;
import ca.ucalgary.seng301.vendingmachine.hardware.Button;
import ca.ucalgary.seng301.vendingmachine.hardware.ButtonListener;
import ca.ucalgary.seng301.vendingmachine.hardware.SimulationException;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class VendingMachineLogic implements CoinSlotListener, ButtonListener {
    private int availableFunds = 0;
    private VendingMachine vendingMachine;
    private Map<Button, Integer> buttonToIndex = new HashMap<>();
    private Map<Integer, Integer> valueToIndexMap = new HashMap<>();
    private Display disp;
    private IndicatorLight exactChangeLight;
    private IndicatorLight outOfOrderLight;

    public VendingMachineLogic(VendingMachine vm) {
		vendingMachine = vm;
	
		vm.getCoinSlot().register(this);
		for(int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
		    Button sb = vm.getSelectionButton(i);
		    sb.register(this);
		    buttonToIndex.put(sb, i);
		}
		
		for(int i = 0; i < vm.getNumberOfCoinRacks(); i++) {
		    int value = vm.getCoinKindForRack(i);
		    valueToIndexMap.put(value, i);
		}
		
		//get references to vm's indicator lights
		exactChangeLight = vendingMachine.getExactChangeLight();
		//exact change light should be on unless we can guarantee that change can be made		TODO: this may not need to be turned on yet. depends on change
		exactChangeLight.activate();
		
		outOfOrderLight = vendingMachine.getOutOfOrderLight();
		//If a hardware problem occurs, the out of order light will be on; otherwise, it is off.
		outOfOrderLight.deactivate();
		
		//displaying message "Drink Pop!" upon creation of the vending machine
		disp = vendingMachine.getDisplay();
		disp.display("Drink Pop!");
    }

    @Override
    public void enabled(AbstractHardware<AbstractHardwareListener> hardware) {
    }

    @Override
    public void disabled(AbstractHardware<AbstractHardwareListener> hardware) {
    }

    @Override
    public void validCoinInserted(CoinSlot coinSlotSimulator, Coin coin) {
    	availableFunds += coin.getValue();
    	
    	//display current availableFunds 
		disp.display("Total: " + availableFunds + " units");
    }

    @Override
    public void coinRejected(CoinSlot coinSlotSimulator, Coin coin) {
    }

    @Override
    public void pressed(Button button) {
	Integer index = buttonToIndex.get(button);

	if(index == null){
		outOfOrderLight.activate();
	    throw new SimulationException("An invalid selection button was pressed");
	}

	int cost = vendingMachine.getProductKindCost(index);

	if(cost <= availableFunds) {
	    ProductRack pcr = vendingMachine.getProductRack(index);
	    if(pcr.size() > 0) {
		try {
		    pcr.dispenseProduct();
		    vendingMachine.getCoinReceptacle().storeCoins();
		    availableFunds = deliverChange(cost, availableFunds);
		    //display message "Drink Pop!" after returning change
			disp.display("Drink Pop!");
		}
		catch(DisabledException | EmptyException | CapacityExceededException e) {
			outOfOrderLight.activate();
		    throw new SimulationException(e);
		}
	    }
	}
	else {
		//displays message "Cost is <cost>"
	    disp.display("Cost is " + cost + "; available funds: " + availableFunds);
	    final Timer timer = new Timer();
	    timer.schedule(new TimerTask() {
		@Override
		public void run() {
		    timer.cancel();
		}
	    }, 5000);
	}
    }

    private Map<Integer, List<Integer>> changeHelper(ArrayList<Integer> values, int index, int changeDue) {
	if(index >= values.size())
	    return null;

	int value = values.get(index);
	Integer ck = valueToIndexMap.get(value);
	CoinRack cr = vendingMachine.getCoinRack(ck);

	Map<Integer, List<Integer>> map = changeHelper(values, index + 1, changeDue);

	if(map == null) {
	    map = new TreeMap<>(new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
		    return o2 - o1;
		}
	    });
	    map.put(0, new ArrayList<Integer>());
	}

	Map<Integer, List<Integer>> newMap = new TreeMap<>(map);
	for(Integer total : map.keySet()) {
	    List<Integer> res = map.get(total);
	    int intTotal = total;

	    for(int count = cr.size(); count >= 0; count--) {
		int newTotal = count * value + intTotal;
		if(newTotal <= changeDue) {
		    List<Integer> newRes = new ArrayList<>();
		    if(res != null)
			newRes.addAll(res);

		    for(int i = 0; i < count; i++)
			newRes.add(ck);

		    newMap.put(newTotal, newRes);
		}
	    }
	}

	return newMap;
    }

    private int deliverChange(int cost, int entered) throws CapacityExceededException, EmptyException, DisabledException {
	int changeDue = entered - cost;

	if(changeDue < 0)
	    throw new InternalError("Cost was greater than entered, which should not happen");

	ArrayList<Integer> values = new ArrayList<>();
	for(Integer ck : valueToIndexMap.keySet())
	    values.add(ck);

	Map<Integer, List<Integer>> map = changeHelper(values, 0, changeDue);

	List<Integer> res = map.get(changeDue);
	if(res == null) {
	    // An exact match was not found, so do your best
	    Iterator<Integer> iter = map.keySet().iterator();
	    Integer max = 0;
	    while(iter.hasNext()) {
		Integer temp = iter.next();
		if(temp > max)
		    max = temp;
	    }
	    res = map.get(max);
	}

	for(Integer ck : res) {
	    CoinRack cr = vendingMachine.getCoinRack(ck);
	    cr.releaseCoin();
	    changeDue -= vendingMachine.getCoinKindForRack(ck);
	}

	return changeDue;
    }

}
