package ca.ucalgary.seng301.myvendingmachine.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng301.myvendingmachine.VendingMachineLogic;
import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.hardware.DisabledException;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class SmallSetupVendingMachineFactoryTest2 extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	// construct(5, 10, 25, 100; 1; 10; 10; 10)
	// configure("stuff"; 140)
	// load(1, 6, 1, 1; 1)

	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 1, 10, 10, 10);
	new VendingMachineLogic(vm);

	vm.configure(Arrays.asList("stuff"), Arrays.asList(140));
	vm.loadCoins(1, 6, 1, 1);
	vm.loadPops(1);
    }

    @Test
    public void testT09HardForChange() throws DisabledException {
	// insert(100)
	// insert(100)
	// insert(100)
	// press(0)
	// extract()
	// CHECK_DELIVERY(160, "stuff")
	// unload()
	// CHECK_TEARDOWN(330; 0)
	
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(160, "stuff"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(330, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromPopRacks(vm));
    }

    @Test
    public void testT10InvalidCoin() throws DisabledException {
	// insert(1)
	// insert(139)
	// press(0)
	// extract()
	// CHECK_DELIVERY(140)
	// unload()
	// CHECK_TEARDOWN(190; 0; "stuff")
	vm.getCoinSlot().addCoin(new Coin(1));
	vm.getCoinSlot().addCoin(new Coin(139));
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(140), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(190, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff"), Utilities.extractAndSortFromPopRacks(vm));
    }
}
