package ca.ucalgary.seng301.myvendingmachine.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng301.myvendingmachine.VendingMachineLogic;
import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.hardware.DisabledException;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class ScrambledCoinKindsVendingMachineFactoryTests extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	// construct(100, 5, 25, 10; 3; 2; 10; 10)
	// configure("Coke", "water", "stuff"; 250, 250, 205)
	// load(0, 1, 2, 1; 1, 1, 1)
	vm = new VendingMachine(new int[] {100, 5, 25, 10}, 3, 2, 10, 10);
	new VendingMachineLogic(vm);

	vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 205));
	vm.loadCoins(0, 1, 2, 1);
	vm.loadPops(1, 1, 1);
    }

    @Test
    public void testT05ScrambledCoinKinds() throws DisabledException {
	// press(0)
	// extract()
	// CHECK_DELIVERY(0)
	// insert(100)
	// insert(100)
	// insert(100)
	// press(0)
	// extract()
	// CHECK_DELIVERY(50, "Coke")
	// unload()
	// CHECK_TEARDOWN(215; 100; "water", "stuff")
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));

	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(215, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(100, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromPopRacks(vm));
    }
}
