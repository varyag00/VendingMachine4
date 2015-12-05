/* Name: 	J. Daniel Gonzalez
 * UCID:	10058656
 * Class:	SENG 301
 * Ass:		4
 * 
 * Note: 	Tests N01, N02, N03, N04 have been added my assignment 3 test class
 */

package ca.ucalgary.seng301.myvendingmachine.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng301.myvendingmachine.VendingMachineLogic;
import ca.ucalgary.seng301.vendingmachine.Coin;
import ca.ucalgary.seng301.vendingmachine.Product;
import ca.ucalgary.seng301.vendingmachine.VendingMachineStoredContents;
import ca.ucalgary.seng301.vendingmachine.hardware.CoinRack;
import ca.ucalgary.seng301.vendingmachine.hardware.DisabledException;
import ca.ucalgary.seng301.vendingmachine.hardware.ProductRack;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;

public class Tests {
						
	/*	Attributes	*/
	
	VendingMachine vm;
	VendingMachineStoredContents vmsc;
	List<Integer> constructCoinArgs;
	int selButtCount;
	int coinRackCap;
	int popCanRackCap;
	int receptCap;
	
	List<String> configPopNamesArgs;
	List<Integer> configPopCostArgs;
	
	List<Integer> loadCoinCounts;
	List<Integer> loadPopCounts;
	
	List<String> extractActualStringOutput;
	List<String> extractExpectedStringOutput;
	
	int extractActualChangeValue;
	int extractExpectedChangeValue;
	List<Object> extractObjOutput;
	
	int actualChangeSum;
	int expectedChangeSum;
 	
	int expectedPaymentCoinsInStorageBin;
	int actualPaymentCoinsInStorageBin;
	
	ArrayList<String> actualUnsoldPopCans;
	ArrayList<String> expectedUnsoldPopCans;
	
	/* Setup & TearDown Methods	*/
	
	@Before
	public void setUp() throws Exception {
		constructCoinArgs = new ArrayList<Integer>();
		configPopNamesArgs = new ArrayList<String>(); 
		configPopCostArgs = new ArrayList<Integer>();
		configPopNamesArgs = new ArrayList<String>(); 
		configPopCostArgs = new ArrayList<Integer>();
		loadCoinCounts = new ArrayList<Integer>();
		loadPopCounts = new ArrayList<Integer>();
		extractActualStringOutput = new ArrayList<String>();
		extractExpectedStringOutput = new ArrayList<String>();
		extractActualChangeValue = 0;
		actualChangeSum = 0;
		expectedPaymentCoinsInStorageBin = 0;
		actualPaymentCoinsInStorageBin = 0;
		actualUnsoldPopCans = new ArrayList<String>(); 		
		expectedUnsoldPopCans = new ArrayList<String>();
	}

	@After
	public void tearDown() throws Exception {
		constructCoinArgs.clear();
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		loadCoinCounts.clear();
		loadPopCounts.clear();
		extractActualStringOutput.clear();
		extractExpectedStringOutput.clear();
		
		vm = null;
		vmsc = null;
	}

	/*	VendingMachineFactory methods required to test hardware	*/
	
	//construct method
    public void construct(List<Integer> coinKinds, int selectionButtonCount, int coinRackCapacity, int popCanRackCapacity, int receptacleCapacity) {
		int[] ck = new int[coinKinds.size()];
		int i = 0;
		for(Integer coinKind : coinKinds)
		    ck[i++] = coinKind;
		vm = new VendingMachine(ck, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity);
		new VendingMachineLogic(vm);
    }
	
    //configure method
    public void configure(List<String> popNames, List<Integer> popCosts) {
    	vm.configure(popNames, popCosts);
    }
    
    //load method
    public void load(List<Integer> coinCounts, List<Integer> popCanCounts) {
		int numberOfCoinRacks = vm.getNumberOfCoinRacks();
		int numberOfPopCanRacks = vm.getNumberOfProductRacks();
	
		if(coinCounts.size() != numberOfCoinRacks)
		    throw new IllegalArgumentException("The size of the coinCounts list must be identical to the number of coin racks in the machine");
		if(popCanCounts.size() != numberOfPopCanRacks)
		    throw new IllegalArgumentException("The size of the popCanCounts list must be identical to the number of pop can racks in the machine");
	
		int i = 0;
		for(Integer coinCount : coinCounts) {
		    CoinRack cr = vm.getCoinRack(i);
		    for(int count = 0; count < coinCount; count++)
			cr.loadWithoutEvents(new Coin(vm.getCoinKindForRack(i)));
		    i++;
		}
	
		i = 0;
		for(Integer popCanCount : popCanCounts) {
		    ProductRack pcr = vm.getProductRack(i);
		    for(int count = 0; count < popCanCount; count++)
			pcr.loadWithoutEvents(new Product(vm.getProductKindName(i)));
		    i++;
		}
    }
    
    //unload method
    public VendingMachineStoredContents unload() {
		VendingMachineStoredContents contents = new VendingMachineStoredContents();
		contents.paymentCoinsInStorageBin.addAll(vm.getStorageBin().unloadWithoutEvents());
		for(int i = 0; i < vm.getNumberOfProductRacks(); i++) {
		    ProductRack pcr = vm.getProductRack(i);
		    contents.unsoldProduct.add(new ArrayList<>(pcr.unloadWithoutEvents()));
		}
		for(int i = 0; i < vm.getNumberOfCoinRacks(); i++) {
		    CoinRack cr = vm.getCoinRack(i);
		    contents.unusedCoinsForChange.add(new ArrayList<>(cr.unloadWithoutEvents()));
		}
		return contents;
    }
    
    //extract
    public List<Object> extract() {
    	return Arrays.asList(vm.getDeliveryChute().removeItems());
    }
    
    //insert
    public void insert(int value) throws DisabledException {
    	vm.getCoinSlot().addCoin(new Coin(value));
    }
    
    //press method
    public void press(int value) {
    	vm.getSelectionButton(value).press();
    }
    
    
    /*	 Tests	*/
    
	@Test
	public void T01() throws DisabledException {
		
		/*	construct(5, 10, 25, 100; 3; 10; 10; 10)	*/
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 3;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
		/*	configure("Coke", "water", "stuff"; 250, 250, 205)	*/
		configPopNamesArgs.add("Coke");
		configPopNamesArgs.add("water");
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(250);
		configPopCostArgs.add(250);
		configPopCostArgs.add(205);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
		/*	load(1, 1, 2, 0; 1, 1, 1) 	*/
		loadCoinCounts.add(1);
		loadCoinCounts.add(1);
		loadCoinCounts.add(2);
		loadCoinCounts.add(0);
		
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
		/*	insert(100)	*/
		insert(100); 				
		
		/*	insert(100)	*/
		insert(100); 				
		
		/*	insert(25)	*/
		insert(25); 				

		/*	insert(25)	*/
		insert(25); 				

		/*	press(0)	*/
		press(0);
		
		/*	extract()	*/									
		
		extractObjOutput = extract(); 		

		/*	CHECK_DELIVERY(0, "Coke")	*/
		
		extractExpectedChangeValue = 0; 				//not updating actual change before calling helper funciton might mess up future tests
		extractExpectedStringOutput.add("Coke");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
				
		/*	unload()	*/
		
		vmsc = unload();
		
		/*	CHECK_TEARDOWN(315; 0; "water", "stuff")	*/
		
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 315;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		
		
		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
				
		expectedUnsoldPopCans.add("water");
		expectedUnsoldPopCans.add("stuff");
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
	}

	@Test
	public void T02() throws DisabledException {
		
		//construct(5, 10, 25, 100; 3; 10; 10; 10)
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 3;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
		//configure("Coke", "water", "stuff"; 250, 250, 205)
		configPopNamesArgs.add("Coke");
		configPopNamesArgs.add("water");
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(250);
		configPopCostArgs.add(250);
		configPopCostArgs.add(205);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
		//load(1, 1, 2, 0; 1, 1, 1)
		loadCoinCounts.add(1);
		loadCoinCounts.add(1);
		loadCoinCounts.add(2);
		loadCoinCounts.add(0);
		
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
		//insert(100)
		insert(100);
		
		//insert(100)
		insert(100);
		
		//insert(100)
		insert(100);
		
		//press(0)
		press(0);	
				
		//extract()
		extractObjOutput = extract(); 		
				
		//CHECK_DELIVERY(50, "Coke")
		extractExpectedChangeValue = 50; 				//not updating actual change before calling helper funciton might mess up future tests
		extractExpectedStringOutput.add("Coke");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
				
		//unload()
		vmsc = unload();
				
		//CHECK_TEARDOWN(315; 0; "water", "stuff")
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 315;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		
		
		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
				
		expectedUnsoldPopCans.add("water");
		expectedUnsoldPopCans.add("stuff");
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
		
	}

	@Test
	public void T03() throws DisabledException {
	//		construct(5, 10, 25, 100; 3; 10; 10; 10)
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 3;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
			
	//		extract()
		extractObjOutput = extract(); 		

			
	//		CHECK_DELIVERY(0)
		extractExpectedChangeValue = 0; 				
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
			
	//		unload()
		vmsc = unload();

			
	//		CHECK_TEARDOWN(0; 0)
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 0;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		
		
		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
				
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
	}
	
	@Test
	public void T04() throws DisabledException {
//		construct(5, 10, 25, 100; 3; 10; 10; 10)
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 3;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		configure("Coke", "water", "stuff"; 250, 250, 205)
		configPopNamesArgs.add("Coke");
		configPopNamesArgs.add("water");
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(250);
		configPopCostArgs.add(250);
		configPopCostArgs.add(205);
		
		configure(configPopNamesArgs, configPopCostArgs);
		

//		load(1, 1, 2, 0; 1, 1, 1)
		loadCoinCounts.add(1);
		loadCoinCounts.add(1);
		loadCoinCounts.add(2);
		loadCoinCounts.add(0);
		
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		

		
//		CHECK_DELIVERY(0)
		extractExpectedChangeValue = 0; 				//not updating actual change before calling helper funciton might mess up future tests
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();
		
//		CHECK_TEARDOWN(65; 0; "Coke", "water", "stuff")
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 65;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		
		
		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
				
		expectedUnsoldPopCans.add("Coke");
		expectedUnsoldPopCans.add("water");
		expectedUnsoldPopCans.add("stuff");
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
	}
	
	@Test
	public void T05() throws DisabledException {
//		construct(100, 5, 25, 10; 3; 2; 10; 10)
		//construct(5, 10, 25, 100; 3; 10; 10; 10)
		constructCoinArgs.add(100);
		constructCoinArgs.add(5);
		constructCoinArgs.add(25);
		constructCoinArgs.add(10);
		
		selButtCount = 3;
		coinRackCap = 2;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		configure("Coke", "water", "stuff"; 250, 250, 205)
		configPopNamesArgs.add("Coke");
		configPopNamesArgs.add("water");
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(250);
		configPopCostArgs.add(250);
		configPopCostArgs.add(205);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		load(0, 1, 2, 1; 1, 1, 1)
		loadCoinCounts.add(0);
		loadCoinCounts.add(1);
		loadCoinCounts.add(2);
		loadCoinCounts.add(1);
		
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(0)
		extractExpectedChangeValue = 0; 				//not updating actual change before calling helper funciton might mess up future tests
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		
		
//		CHECK_DELIVERY(50, "Coke")
		extractExpectedChangeValue = 50; 				//not updating actual change before calling helper funciton might mess up future tests
		extractExpectedStringOutput.add("Coke");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();
		
//		CHECK_TEARDOWN(215; 100; "water", "stuff")
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 215;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		
		
		expectedPaymentCoinsInStorageBin = 100;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.add("water");
		expectedUnsoldPopCans.add("stuff");
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
	}
	
	@Test
	public void T06() throws DisabledException {
		
//		construct(100, 5, 25, 10; 3; 10; 10; 10)
		constructCoinArgs.add(100);
		constructCoinArgs.add(5);
		constructCoinArgs.add(25);
		constructCoinArgs.add(10);
		
		selButtCount = 3;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		configure("Coke", "water", "stuff"; 250, 250, 205)
		configPopNamesArgs.add("Coke");
		configPopNamesArgs.add("water");
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(250);
		configPopCostArgs.add(250);
		configPopCostArgs.add(205);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		load(0, 1, 2, 1; 1, 1, 1)
		loadCoinCounts.add(0);
		loadCoinCounts.add(1);
		loadCoinCounts.add(2);
		loadCoinCounts.add(1);
		
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		
		
//		CHECK_DELIVERY(0)
		extractExpectedChangeValue = 0; 				
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);		
		
//		insert(100)
		insert(100);
		
//		extract()
		extractObjOutput = extract(); 		
		
//		CHECK_DELIVERY(0)
		extractExpectedChangeValue = 0; 				
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();
		
//		CHECK_TEARDOWN(65; 0; "Coke", "water", "stuff")
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 65;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		
		
		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);

		//checking List<List<PopCan>> unsoldPopCans 					
			
		expectedUnsoldPopCans.add("Coke");
		expectedUnsoldPopCans.add("water");
		expectedUnsoldPopCans.add("stuff");
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
	}
	
	@Test
	public void T07() throws DisabledException {
		
//		construct(5, 10, 25, 100; 3; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 3;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		configure("A", "B", "C"; 5, 10, 25)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("A");
		configPopNamesArgs.add("B");
		configPopNamesArgs.add("C");
		
		configPopCostArgs.add(5);
		configPopCostArgs.add(10);
		configPopCostArgs.add(25);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		load(1, 1, 2, 0; 1, 1, 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(1);
		loadCoinCounts.add(1);
		loadCoinCounts.add(2);
		loadCoinCounts.add(0);
		
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		configure("Coke", "water", "stuff"; 250, 250, 205)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("Coke");
		configPopNamesArgs.add("water");
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(250);
		configPopCostArgs.add(250);
		configPopCostArgs.add(205);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(0)
		extractExpectedChangeValue = 0; 				
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		press(0)
		press(0);

//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(50, "A")
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 50; 	
		extractExpectedStringOutput.add("A");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();

//		CHECK_TEARDOWN(315; 0; "B", "C")
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 315;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		expectedUnsoldPopCans.add("B");
		expectedUnsoldPopCans.add("C");
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
		
		
//		load(1, 1, 2, 0; 1, 1, 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(1);
		loadCoinCounts.add(1);
		loadCoinCounts.add(2);
		loadCoinCounts.add(0);
		
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		press(0)
		press(0);

//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(50, "Coke")
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 50; 	
		extractExpectedStringOutput.add("Coke");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();

		
//		CHECK_TEARDOWN(315; 0; "water", "stuff")
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 315;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		expectedUnsoldPopCans.add("water");
		expectedUnsoldPopCans.add("stuff");
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
		
	}
	
	@Test
	public void T08() throws DisabledException {
		
//		construct(5, 10, 25, 100; 1; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 1;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);

//		configure("stuff"; 140)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(140);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		load(0, 5, 1, 1; 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(0);
		loadCoinCounts.add(5);
		loadCoinCounts.add(1);
		loadCoinCounts.add(1);
		
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(155, "stuff")
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 155; 	
		extractExpectedStringOutput.add("stuff");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();

//		CHECK_TEARDOWN(320; 0)
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 320;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
	}
	
	@Test
	public void T09() throws DisabledException {
		
//		construct(5, 10, 25, 100; 1; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 1;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);

//		configure("stuff"; 140)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(140);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		load(1, 6, 1, 1; 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(1);
		loadCoinCounts.add(6);
		loadCoinCounts.add(1);
		loadCoinCounts.add(1);
		
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		
		
//		CHECK_DELIVERY(160, "stuff")
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 160; 	
		extractExpectedStringOutput.add("stuff");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();

//		CHECK_TEARDOWN(330; 0)
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 330;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
	}
	
	@Test
	public void T10() throws DisabledException {
		
//		construct(5, 10, 25, 100; 1; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 1;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		configure("stuff"; 140)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(140);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		load(1, 6, 1, 1; 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(1);
		loadCoinCounts.add(6);
		loadCoinCounts.add(1);
		loadCoinCounts.add(1);
		
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		insert(1)
		insert(1);
		
//		insert(139)
		insert(139);

//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		
		
//		CHECK_DELIVERY(140)
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 140; 	
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();
		
//		CHECK_TEARDOWN(190; 0; "stuff")
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 190;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		expectedUnsoldPopCans.add("stuff");
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
	}
	
	@Test
	public void T11() throws DisabledException {
		
//		construct(100, 5, 25, 10; 3; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(100);
		constructCoinArgs.add(5);
		constructCoinArgs.add(25);
		constructCoinArgs.add(10);
		
		selButtCount = 3;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);

//		configure("Coke", "water", "stuff"; 250, 250, 205)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("Coke");
		configPopNamesArgs.add("water");
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(250);
		configPopCostArgs.add(250);
		configPopCostArgs.add(205);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		load(0, 1, 2, 1; 1, 1, 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(0);
		loadCoinCounts.add(1);
		loadCoinCounts.add(2);
		loadCoinCounts.add(1);

		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(0)
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 0; 	
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		insert(100)
		insert(100);
		
//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(0)
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 0; 	
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();

//		CHECK_TEARDOWN(65; 0; "Coke", "water", "stuff")
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 65;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		expectedUnsoldPopCans.add("Coke");
		expectedUnsoldPopCans.add("water");
		expectedUnsoldPopCans.add("stuff");
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
		
//		load(0, 1, 2, 1; 1, 1, 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(0);
		loadCoinCounts.add(1);
		loadCoinCounts.add(2);
		loadCoinCounts.add(1);

		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(50, "Coke")
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 50; 	
		extractExpectedStringOutput.add("Coke");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();

//		CHECK_TEARDOWN(315; 0; "water", "stuff")
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 315;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		expectedUnsoldPopCans.add("water");
		expectedUnsoldPopCans.add("stuff");
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
		
//		construct(100, 5, 25, 10; 3; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(100);
		constructCoinArgs.add(5);
		constructCoinArgs.add(25);
		constructCoinArgs.add(10);
		
		selButtCount = 3;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		configure("Coke", "water", "stuff"; 250, 250, 205)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("Coke");
		configPopNamesArgs.add("water");
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(250);
		configPopCostArgs.add(250);
		configPopCostArgs.add(205);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		configure("A", "B", "C"; 5, 10, 25)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("A");
		configPopNamesArgs.add("B");
		configPopNamesArgs.add("C");
		
		configPopCostArgs.add(5);
		configPopCostArgs.add(10);
		configPopCostArgs.add(25);
		
		configure(configPopNamesArgs, configPopCostArgs);
//		unload()
		vmsc = unload();

//		CHECK_TEARDOWN(0; 0)
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 0;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
		
//		load(0, 1, 2, 1; 1, 1, 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(0);
		loadCoinCounts.add(1);
		loadCoinCounts.add(2);
		loadCoinCounts.add(1);

		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		insert(10)
		insert(10);
		
//		insert(5)
		insert(5);
		
//		insert(10)
		insert(10);
		
//		press(2)
		press(2);
		
//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(0, "C")
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 0; 	
		extractExpectedStringOutput.add("C");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();

//		CHECK_TEARDOWN(90; 0; "A", "B")
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 90;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		expectedUnsoldPopCans.add("A");
		expectedUnsoldPopCans.add("B");
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
		
		
	}
	
	@Test
	public void T12() throws DisabledException {
//		construct(5, 10, 25, 100; 1; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 1;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		configure("stuff"; 140)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(140);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		load(0, 5, 1, 1; 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(0);
		loadCoinCounts.add(5);
		loadCoinCounts.add(1);
		loadCoinCounts.add(1);
		
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		insert(100)
		insert(100);

//		insert(100)
		insert(100);

//		insert(100)
		insert(100);

//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(155, "stuff")
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 155; 	
		extractExpectedStringOutput.add("stuff");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();

//		CHECK_TEARDOWN(320; 0)
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 320;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
		
//		load(10, 10, 10, 10; 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(10);
		loadCoinCounts.add(10);
		loadCoinCounts.add(10);
		loadCoinCounts.add(10);
		
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
//		insert(25)
		insert(25);
		
//		insert(100)
		insert(100);
		
//		insert(10)
		insert(10);
		
//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(0, "stuff")
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 0; 	
		extractExpectedStringOutput.add("stuff");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();

//		CHECK_TEARDOWN(1400; 135)
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 1400;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 135;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
		
	}
	
	@Test
	public void T13() throws DisabledException {
		
//		construct(5, 10, 25, 100; 1; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 1;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		configure("stuff"; 135)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(135);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		load(10, 10, 10, 10; 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(10);
		loadCoinCounts.add(10);
		loadCoinCounts.add(10);
		loadCoinCounts.add(10);
		
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		insert(25)
		insert(25);
		
//		insert(100)
		insert(100);
		
//		insert(10)
		insert(10);
		
//		press(0)
		press(0);
		
//		extract()
		extractObjOutput = extract(); 		

//		CHECK_DELIVERY(0, "stuff")
		extractExpectedStringOutput.clear();
		extractActualStringOutput.clear();
		extractExpectedChangeValue = 0; 	
		extractExpectedStringOutput.add("stuff");
		extractTestHelper();	
		
		//check if change value is correct
		assertEquals(extractExpectedChangeValue, extractActualChangeValue);
		//check if pop names are correct
		assertArrayEquals(extractExpectedStringOutput.toArray(), extractActualStringOutput.toArray());
		
//		unload()
		vmsc = unload();

//		CHECK_TEARDOWN(1400; 135)
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 1400;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 135;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
		
	}
	
	/* Because we are testing the hardware directly via the VendingMachine class, the JUnit test U01, 
	 * which attempts to configure vending machine hardware that before the vending machine has been constructed 
	 * (i.e. before it exists), I decided to exclude the test U01 as it does not make sense.
	 * */
//	@Test(expected = NullPointerException.class)
//	public void U01() throws DisabledException{
//		
////		configure("Coke", "water", "stuff"; 250, 250, 205) // This SHOULD cause an error, but DOES NOT!
//		configPopNamesArgs.clear();
//		configPopCostArgs.clear();
//		configPopNamesArgs.add("Coke");
//		configPopNamesArgs.add("water");
//		configPopNamesArgs.add("stuff");
//		
//		configPopCostArgs.add(250);
//		configPopCostArgs.add(250);
//		configPopCostArgs.add(205);
//		
//		configure(configPopNamesArgs, configPopCostArgs);
//		
////		construct(5, 10, 25, 100; 3; 10; 10; 10)
//		constructCoinArgs.clear();
//		constructCoinArgs.add(5);
//		constructCoinArgs.add(10);
//		constructCoinArgs.add(25);
//		constructCoinArgs.add(100);
//		
//		selButtCount = 3;
//		coinRackCap = 10;
//		popCanRackCap = 10;
//		receptCap = 10;
//		
//		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
//		
////		load(1, 1, 2, 0; 1, 1, 1)
//		loadCoinCounts.clear();
//		loadPopCounts.clear();
//		loadCoinCounts.add(1);
//		loadCoinCounts.add(1);
//		loadCoinCounts.add(2);
//		loadCoinCounts.add(0);
//		
//		loadPopCounts.add(1);
//		loadPopCounts.add(1);
//		loadPopCounts.add(1);
//		
//		load(loadCoinCounts, loadPopCounts);
//		
////		unload()
//		vmsc = unload();
//
////		CHECK_TEARDOWN(65; 0; "Coke", "water", "stuff") // This causes an error for the dummy but should not
//		//checking List<List<Coin>> unusedCoinsForChange 
//		expectedChangeSum = 65;
//		unloadUnusedCoinsHelper();
//		
//		//check if the sum of the unused change is correct
//		assertEquals(expectedChangeSum, actualChangeSum);
//		
//
//		expectedPaymentCoinsInStorageBin = 0;
//		unloadPaymentCoinsInStorageBinHelper();
//
//		//check if the sum of the Coins in the storagebin is correct
//		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
//		
//		
//		//checking List<List<PopCan>> unsoldPopCans 					
//		expectedUnsoldPopCans.clear();
//		actualUnsoldPopCans.clear();
//		expectedUnsoldPopCans.add("Coke");
//		expectedUnsoldPopCans.add("water");
//		expectedUnsoldPopCans.add("stuff");
//		unloadUnsoldPopCansHelper();
//		
//		//check if the unsold PopCans is correct
//		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
//	}
	
	
	
	@Test(expected = ca.ucalgary.seng301.vendingmachine.hardware.SimulationException.class)
	public void U02() throws DisabledException{
//		construct(5, 10, 25, 100; 3; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 3;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		configure("Coke", "water", "stuff"; 250, 250 /* the cost of "stuff" is not defined */) // This SHOULD cause an error, but DOES NOT in the dummy!
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("Coke");
		configPopNamesArgs.add("water");
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(250);
		configPopCostArgs.add(250);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
//		load(1, 1, 2, 0; 1, 1, 1)
		loadCoinCounts.clear();
		loadPopCounts.clear();
		loadCoinCounts.add(1);
		loadCoinCounts.add(1);
		loadCoinCounts.add(2);
		loadCoinCounts.add(0);
		
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
//		unload()
		vmsc = unload();

//		CHECK_TEARDOWN(0; 0) // This passes, but we should not get this far
		//checking List<List<Coin>> unusedCoinsForChange 
		expectedChangeSum = 0;
		unloadUnusedCoinsHelper();
		
		//check if the sum of the unused change is correct
		assertEquals(expectedChangeSum, actualChangeSum);
		

		expectedPaymentCoinsInStorageBin = 0;
		unloadPaymentCoinsInStorageBinHelper();

		//check if the sum of the Coins in the storagebin is correct
		assertEquals(expectedPaymentCoinsInStorageBin, actualPaymentCoinsInStorageBin);
		
		
		//checking List<List<PopCan>> unsoldPopCans 					
		expectedUnsoldPopCans.clear();
		actualUnsoldPopCans.clear();
		unloadUnsoldPopCansHelper();
		
		//check if the unsold PopCans is correct
		assertArrayEquals(expectedUnsoldPopCans.toArray(), actualUnsoldPopCans.toArray());
	}
	
	@Test(expected = ca.ucalgary.seng301.vendingmachine.hardware.SimulationException.class)
	public void U03() throws DisabledException{
//		construct(5, 10, 25, 100; 3; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 3;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		configure("Coke", "water"; 250, 250)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("Coke");
		configPopNamesArgs.add("water");
		
		configPopCostArgs.add(250);
		configPopCostArgs.add(250);
		
		configure(configPopNamesArgs, configPopCostArgs);
	}
	
	@Test(expected = ca.ucalgary.seng301.vendingmachine.hardware.SimulationException.class)
	public void U04() throws DisabledException{
		//construct(1, 1; 1; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(1);
		constructCoinArgs.add(1);
		
		selButtCount = 1;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
	}
	
	@Test(expected = ca.ucalgary.seng301.vendingmachine.hardware.SimulationException.class)
	public void U05() throws DisabledException{
		//construct(0; 1; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(0);
		
		selButtCount = 1;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void U06() throws DisabledException{
//		construct(5, 10, 25, 100; 3; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 3;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		press(3)
		press(3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void U07() throws DisabledException{
//		construct(1; 1; 10; 10; 10)
		constructCoinArgs.clear();
		constructCoinArgs.add(1);
		
		selButtCount = 1;
		coinRackCap = 10;
		popCanRackCap = 10;
		receptCap = 10;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
//		configure(""; 1)
		configPopNamesArgs.clear();
		configPopCostArgs.clear();
		configPopNamesArgs.add("");
		
		configPopCostArgs.add(1);
		
		configure(configPopNamesArgs, configPopCostArgs);
	}
		
	/*	Assignment 4 tests	*/
	
	//test for messages
	@Test
	public void N01() throws DisabledException{
		
	}
	
	//test for outOfOrderLight
	@Test
	public void N02() throws DisabledException{
		
	}
	
	//test for exactChangeOnlyLight
	@Test
	public void N03() throws DisabledException{
		
	}

	//test for "Return Money" button
	@Test
	public void N04() throws DisabledException{
		
	}
	
	
	/*	Test Helper Methods	*/

	//updates values of extractActualChangeValue and extractActualStringOutput for extract method
	public void extractTestHelper(){
		extractActualChangeValue = 0;
		extractActualStringOutput.clear();
		
		for (Object obj : extractObjOutput){ 
			if(obj.getClass().equals(Product.class)) 
				extractActualStringOutput.add(((Product) obj).getName());
			else if(obj.getClass().equals(Coin.class))
				extractActualChangeValue += ((Coin) obj).getValue();
		}
	}
	
	//updates the value of actualChangeSum for unload method
	public void unloadUnusedCoinsHelper(){
		actualChangeSum = 0;
		
		for (List<Coin> llCoin : vmsc.unusedCoinsForChange){
			
			for (Coin c : llCoin){
				actualChangeSum += c.getValue();
			}		
		}
	}

	//updates the value of actualPaymentCoinsInStorageBin for unload method
	public void unloadPaymentCoinsInStorageBinHelper(){
		actualPaymentCoinsInStorageBin = 0;
		
		for (Coin c : vmsc.paymentCoinsInStorageBin){
			actualPaymentCoinsInStorageBin += c.getValue();
		}
	}
	
	//updates the value of actualUnsoldPopCans for unload method 
	public void unloadUnsoldPopCansHelper(){
		actualUnsoldPopCans.clear();
		
		for (List<Product> lpc : vmsc.unsoldProduct){
			if (lpc.size() > 0){						
				actualUnsoldPopCans.add(lpc.get(0).getName());		//add first element's name
			}
		}
	}
}