package ca.ucalgary.seng301.myvendingmachine.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng301.myvendingmachine.VendingMachineLogic;
import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.hardware.DisabledException;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class StandardSetupVendingMachineFactoryTests extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	// construct(5, 10, 25, 100; 3; 10; 10; 10)
	// configure("Coke", "water", "stuff"; 250, 250, 205)
	// load(1, 1, 2, 0; 1, 1, 1)
	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);

	vm.configure(Arrays.asList(new String[] {"Coke", "water", "stuff"}), Arrays.asList(new Integer[] {250, 250, 205}));
	vm.loadCoins(1, 1, 2, 0);
	vm.loadPops(1, 1, 1);
    }

    @Test
    public void testT01InsertAndPressExactChange() throws DisabledException {
	// insert(100)
	// insert(100)
	// insert(25)
	// insert(25)
	// press(0)
	// extract()
	// CHECK_DELIVERY(0, "Coke")
	// unload()
	// CHECK_TEARDOWN(315; 0; "water", "stuff")

	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(25));
	vm.getCoinSlot().addCoin(new Coin(25));

	vm.getSelectionButton(0).press();

	assertEquals(Arrays.asList(0, "Coke"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromPopRacks(vm));
    }

    @Test
    public void testT02InsertAndPressChangeExpected() throws DisabledException {
	// insert(100)
	// insert(100)
	// insert(100)
	// press(0)
	// extract()
	// CHECK_DELIVERY(50, "Coke")
	// unload()
	// CHECK_TEARDOWN(315; 0; "water", "stuff")
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));
	vm.getCoinSlot().addCoin(new Coin(100));

	vm.getSelectionButton(0).press();

	assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromPopRacks(vm));
    }

    @Test
    public void testT04PressWithoutInsert() throws DisabledException {
	// press(0)
	// extract()
	// CHECK_DELIVERY(0)
	// unload()
	// CHECK_TEARDOWN(65; 0; "Coke", "water", "stuff")
	vm.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(65, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList("Coke", "stuff", "water"), Utilities.extractAndSortFromPopRacks(vm));
    }
}
