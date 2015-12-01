package ca.ucalgary.seng301.myvendingmachine.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng301.myvendingmachine.VendingMachineLogic;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class NoConfigureOrLoadVendingMachineFactoryTests extends WRAPPER {
    private VendingMachine vm;

    @Before
    public void setup() {
	// construct(5, 10, 25, 100; 3; 10; 10; 10)
	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);
    }

    @Test
    public void testT03TeardownWithoutConfigureOrLoad() {
	// extract()
	// CHECK_DELIVERY(0)
	// unload()
	// CHECK_TEARDOWN(0; 0)
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(vm));
	assertEquals(0, Utilities.extractAndSumFromCoinRacks(vm));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(vm));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromPopRacks(vm));
    }
}
