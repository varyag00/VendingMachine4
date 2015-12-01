package ca.ucalgary.seng301.myvendingmachine.test;

import java.util.Arrays;

import org.junit.After;
import org.junit.Test;

import ca.ucalgary.seng301.myvendingmachine.VendingMachineLogic;
import ca.ucalgary.seng301.vendingmachine.hardware.SimulationException;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class BadVendingMachineFactoryTest extends WRAPPER {
    private VendingMachine vm;

    @After
    public void teardown() {
	vm = null;
    }

    // Note: U01 doesn't make much sense anymore. If you didn't construct the
    // VendingMachine instance, the rest will immediately fail, always.

    @Test(expected = SimulationException.class)
    public void testU02Costs() {
	// construct(5, 10, 25, 100; 3; 10; 10; 10)
	// configure("Coke", "water", "stuff"; 250, 250)
	// load(1, 1, 2, 0; 1, 1, 1) 
	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);
	vm.configure(Arrays.asList("Coke", "water", "stuff"), Arrays.asList(250, 250));
	vm.loadCoins(1, 1, 2, 0);
	vm.loadPops(1, 1, 1);
    }

    @Test(expected = SimulationException.class)
    public void testU03Names() {
	// construct(5, 10, 25, 100; 3; 10; 10; 10)
	// configure("Coke", "water"; 250, 250)
	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);
	vm.configure(Arrays.asList("Coke", "water"), Arrays.asList(250, 250));
    }

    @Test(expected = SimulationException.class)
    public void testU04NonUniqueDenomination() {
	// construct(1, 1; 1; 10; 10; 10)
	new VendingMachine(new int[] {1, 1}, 1, 10, 10, 10);
    }

    @Test(expected = SimulationException.class)
    public void testU05CoinKind() {
	// construct(0; 1; 10; 10; 10)
	new VendingMachine(new int[] {0}, 1, 10, 10, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testU06ButtonNumber() {
	// construct(5, 10, 25, 100; 3; 10; 10; 10)
	// press(3)
	vm = new VendingMachine(new int[] {5, 10, 25, 100}, 3, 10, 10, 10);
	new VendingMachineLogic(vm);
	vm.getSelectionButton(3).press();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testU07EmptyName() {
	// construct(1; 1; 10; 10; 10)
	// configure(""; 1)
	vm = new VendingMachine(new int[] {1}, 1, 10, 10, 10);
	new VendingMachineLogic(vm);
	vm.configure(Arrays.asList(""), Arrays.asList(1));
    }
}
