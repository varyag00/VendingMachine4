package ca.ucalgary.seng301.myvendingmachine.test;

import java.util.ArrayList;
import java.util.List;

import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.PopCan;
import ca.ucalgary.seng301.vendingmachine.hardware.CoinRack;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class Utilities {
    /**
     * A convenience method for checking the contents of the delivery chute
     * against an expected list. The list should contain one String per expected
     * pop (its name) and zero or more positive Integers, in any order. The
     * Integers are added together and compared to the total value of coins in
     * the delivery chute. Empties the chute.
     * 
     * @param expectedItems
     *            A list of Strings and Integers
     * @return true if the delivery is as expected, else false
     */
    public static List<Object> extractAndSortFromDeliveryChute(VendingMachine vm) {
	Object[] actualItems = vm.getDeliveryChute().removeItems();
	int actualValue = 0;
	List<Object> actualList = new ArrayList<>();

	for(Object obj : actualItems) {
	    if(obj instanceof PopCan) {
		PopCan pc = (PopCan)obj;
		String name = pc.getName();
		actualList.add(name);
	    }
	    else
		actualValue += ((Coin)obj).getValue();
	}

	actualList.sort(null);

	actualList.add(0, actualValue);
	return actualList;
    }

    /**
     * Convenience method for checking the total value of coins in the coin
     * racks. Empties the coin racks.
     *
     * @param expectedSum
     *            The total value expected
     * @return true if the actual sum of coin values equals the expected sum,
     *         else false
     */
    public static int extractAndSumFromCoinRacks(VendingMachine vm) {
	int total = 0;

	for(int i = 0, max = vm.getNumberOfCoinRacks(); i < max; i++) {
	    CoinRack cr = vm.getCoinRack(i);
	    List<Coin> coins = cr.unloadWithoutEvents();
	    for(Coin coin : coins)
		total += coin.getValue();
	}

	return total;
    }

    /**
     * Convenience method for checking the total value of coins in the storage
     * bin. Empties the storage bin.
     *
     * @param expectedSum
     *            The total value expected
     * @return true if the actual sum of coin values equals the expected sum,
     *         else false
     */
    public static int extractAndSumFromStorageBin(VendingMachine vm) {
	int total = 0;

	List<Coin> coins = vm.getStorageBin().unloadWithoutEvents();
	for(Coin coin : coins)
	    total += coin.getValue();

	return total;
    }

    /**
     * Convenience method for checking the contents of pop racks. Empties the
     * pop racks.
     *
     * @param expectedList
     *            The names of the pops expected to be present. Can be an empty
     *            list. The same string can be repeated as necessary. The order
     *            is not significant.
     * @return true if the actual pops have the exactly the same names as the
     *         expected ones else false
     */
    public static List<String> extractAndSortFromPopRacks(VendingMachine vm) {
	List<PopCan> actualPopCans = new ArrayList<>();
	for(int i = 0, max = vm.getNumberOfPopCanRacks(); i < max; i++)
	    actualPopCans.addAll(vm.getPopCanRack(i).unloadWithoutEvents());

	List<String> actualList = new ArrayList<>();
	for(PopCan popCan : actualPopCans)
	    actualList.add(popCan.getName());

	actualList.sort(null);

	return actualList;
    }

}
