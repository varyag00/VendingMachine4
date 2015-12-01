package ca.ucalgary.seng301.myvendingmachine.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng301.myvendingmachine.VendingMachineLogic;
import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.hardware.DisabledException;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class SmallSetupVendingMachineFactoryTest3 extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	// construct(5, 10, 25, 100; 1; 10; 10; 10)
	// configure("stuff"; 135)
	// load(10, 10, 10, 10; 1)

	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 1, 10, 10, 10);
	new VendingMachineLogic(vm);

	vm.configure(Arrays.asList("stuff"), Arrays.asList(135));
	vm.loadCoins(10, 10, 10, 10);
	vm.loadPops(1);
    }
    
    @Test
    public void testT13NeedToStorePayment() throws DisabledException {
	// insert(25)
	// insert(100)
	// insert(10)
	// press(0)
	// extract()
	// CHECK_DELIVERY(0, "stuff")
	// unload()
	// CHECK_TEARDOWN(1400; 135)

	vm.getCoinSlot().addCoin(new Coin(25));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(10));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0, "stuff"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(1400, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(135, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromPopRacks(vm));
    }

}
