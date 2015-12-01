package ca.ucalgary.seng301.myvendingmachine.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng301.myvendingmachine.VendingMachineLogic;
import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.hardware.DisabledException;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class ChangingConfigurationVendingMachineTests extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	// construct(5, 10, 25, 100; 3; 10; 10; 10)
	// configure("A", "B", "C"; 5, 10, 25)
	// load(1, 1, 2, 0; 1, 1, 1)

	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);
	
	vm.configure(Arrays.asList("A",  "B", "C"), Arrays.asList(10, 10, 10));
	vm.loadCoins(1, 1, 2, 0);
	vm.loadPops(1, 1, 1);
    }

    @Test
    public void testT07ChangingConfiguration() throws DisabledException {
	// configure("Coke", "water", "stuff"; 250, 250, 205)
	// press(0)
	// extract()
	// CHECK_DELIVERY(0)
	// insert(100)
	// insert(100)
	// insert(100)
	// press(0)
	// extract()
	// CHECK_DELIVERY(50, "A")
	// unload()
	// CHECK_TEARDOWN(315; 0; "B", "C")
	// load(1, 1, 2, 0; 1, 1, 1)
	// insert(100)
	// insert(100)
	// insert(100)
	// press(0)
	// extract()
	// CHECK_DELIVERY(50, "Coke")
	// unload()
	// CHECK_TEARDOWN(315; 0; "water", "stuff")

	vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 205));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));

	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getSelectionButton(0).press();

	assertEquals(Arrays.asList(50, "A"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("B", "C"), Utilities.extractAndSortFromPopRacks(vm));
	
	vm.loadCoins(1, 1, 2, 0);
	vm.loadPops(1, 1, 1);

	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromPopRacks(vm));
    }
}
