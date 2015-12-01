package ca.ucalgary.seng301.myvendingmachine.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng301.myvendingmachine.VendingMachineLogic;
import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.hardware.DisabledException;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class ScrambledCoinKindsVendingMachineFactoryTests2 extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	// construct(100, 5, 25, 10; 3; 10; 10; 10)
	// configure("Coke", "water", "stuff"; 250, 250, 205)
	// load(0, 1, 2, 1; 1, 1, 1)
	vm = new VendingMachine(new int[] {100, 5, 25, 10}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);

	vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250, 205));
	vm.loadCoins(0, 1, 2, 1);
	vm.loadPops(1, 1, 1);
    }

    @Test
    public void testT06ExtractBeforeSale() throws DisabledException {
	// press(0)
	// extract()
	// CHECK_DELIVERY(0)
	// insert(100)
	// insert(100)
	// insert(100)
	// extract()
	// CHECK_DELIVERY(0)
	// unload()
	// CHECK_TEARDOWN(65; 0; "Coke", "water", "stuff")
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));

	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(65, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("Coke", "stuff", "water"), Utilities.extractAndSortFromPopRacks(vm));
    }

    @Test
    public void testT11ExtractBeforeSaleComplex() throws DisabledException {
	// press(0)
	// extract()
	// CHECK_DELIVERY(0)
	// insert(100)
	// insert(100)
	// insert(100)
	// extract()
	// CHECK_DELIVERY(0)
	// unload()
	// CHECK_TEARDOWN(65; 0; "Coke", "water", "stuff")
	// load(0, 1, 2, 1; 1, 1, 1)
	// press(0)
	// extract()
	// CHECK_DELIVERY(50, "Coke")
	// unload()
	// CHECK_TEARDOWN(315; 0; "water", "stuff")
	// construct(100, 5, 25, 10; 3; 10; 10; 10)
	// configure("Coke", "water", "stuff"; 250, 250, 205)
	// configure("A", "B", "C"; 5, 10, 25)
	// unload()
	// CHECK_TEARDOWN(0; 0)
	// load(0, 1, 2, 1; 1, 1, 1)
	// insert(10)
	// insert(5)
	// insert(10)
	// press(2)
	// extract()
	// CHECK_DELIVERY(0, "C")
	// unload()
	// CHECK_TEARDOWN(90; 0; "A", "B")

	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));
	
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(65, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("Coke", "stuff", "water"), Utilities.extractAndSortFromPopRacks(vm));

	vm.loadCoins(0, 1, 2, 1);
	vm.loadPops(1, 1, 1);

	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromPopRacks(vm));

	vm = new VendingMachine(new int[] {100, 5, 25, 10}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);
	vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.<Integer> asList(250, 250, 205));
	vm.configure(Arrays.asList("A", "B", "C"), Arrays.<Integer> asList(5, 10, 25));
	assertEquals(0, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromPopRacks(vm));

	vm.loadCoins(0, 1, 2, 1);
	vm.loadPops(1, 1, 1);
	vm.getCoinSlot().addCoin(new Coin(10));
	vm.getCoinSlot().addCoin(new Coin(5));
	vm.getCoinSlot().addCoin(new Coin(10));
	vm.getSelectionButton(2).press();
	assertEquals(Arrays.asList(0, "C"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(90, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("A", "B"), Utilities.extractAndSortFromPopRacks(vm));
    }
}
