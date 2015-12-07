/* Name: 	J. Daniel Gonzalez
 * UCID:	10058656
 * Class:	SENG 301
 * Ass:		4
 * 
 * Note: 	Tests N01, N02, N03, N04 have been added my assignment 3 test class
 */

package ca.ucalgary.seng301.myvendingmachine.test.assn4;

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
import ca.ucalgary.seng301.vendingmachine.hardware.Button;
import ca.ucalgary.seng301.vendingmachine.hardware.CoinRack;
import ca.ucalgary.seng301.vendingmachine.hardware.DisabledException;
import ca.ucalgary.seng301.vendingmachine.hardware.Display;
import ca.ucalgary.seng301.vendingmachine.hardware.ProductRack;
import ca.ucalgary.seng301.vendingmachine.hardware.VendingMachine;


public class NewTests {
						
	/*	Attributes	*/
	
	VendingMachine vm;
	VendingMachineStoredContents vmsc;
	VendingMachineLogic vml;
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
	
	Display disp;
	String expectedMSG;
	String actualMSG;
	
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
		vml = null;
	}

	/*	VendingMachineFactory methods required to test hardware	*/
	
	//construct method
    public void construct(List<Integer> coinKinds, int selectionButtonCount, int coinRackCapacity, int popCanRackCapacity, int receptacleCapacity) {
		int[] ck = new int[coinKinds.size()];
		int i = 0;
		for(Integer coinKind : coinKinds)
		    ck[i++] = coinKind;
		vm = new VendingMachine(ck, selectionButtonCount, coinRackCapacity, popCanRackCapacity, receptacleCapacity);
		vml = new VendingMachineLogic(vm);
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
    
	/*	Ass 4 tests	*/
	
	//test for messages
	//@Test
	public void N01() throws DisabledException{
		
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
		
		
		//display should read "Drink Pop!" right now
		disp = vm.getDisplay();
		expectedMSG = "Drink Pop!";
		actualMSG = disp.read();
		assertEquals(expectedMSG, actualMSG);
		
		/*	insert(100)	*/
		insert(100); 				
		
		/*	insert(100)	*/
		insert(100); 			
		
		/* 	press(0)	*/
		press(0);
		
		//display should read "Cost is 250; available funds: 200"
		expectedMSG = "Cost is 250; available funds: 200";
		actualMSG = disp.read();
		assertEquals(expectedMSG, actualMSG);
				
		/* 	insert(25)	*/
		insert(25);
		
		/* 	insert(25)	*/
		insert(25);
		
		//display should read "Total: 250 units" at this moment
		expectedMSG = "Total: 250 units";
		actualMSG = disp.read();
		assertEquals(expectedMSG, actualMSG);
		
	}
	
	//test for outOfOrderLight
	//@Test(expected = ca.ucalgary.seng301.vendingmachine.hardware.SimulationException.class)
	public void N02() throws DisabledException{
		/*	construct(5, 10, 25, 100; 3; 10; 1; 10)	*/
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 3;
		coinRackCap = 1;
		popCanRackCap = 1;
		receptCap = 1;
		
		construct(constructCoinArgs, selButtCount, coinRackCap, popCanRackCap, receptCap);
		
		/*	configure("Coke", "water", "stuff"; 250, 250, 205)	*/
		configPopNamesArgs.add("Coke");
		configPopNamesArgs.add("water");
		configPopNamesArgs.add("stuff");
		
		configPopCostArgs.add(250);
		configPopCostArgs.add(250);
		configPopCostArgs.add(205);
		
		configure(configPopNamesArgs, configPopCostArgs);
		
		/*	insert(100)	*/
		insert(100); 				
		
		/*	insert(100)	*/
		insert(100); 			
		
		/* 	insert(25)	*/
		insert(25);
		
		/* 	insert(25)	*/
		insert(25);
		
		/*	press(0)	*/
		press(0);
		
		/*	insert(100)	*/
		insert(100); 				
		
		/*	insert(100)	*/
		insert(100); 			
		
		/* 	insert(25)	*/
		insert(25);
		
		/* 	insert(25)	*/
		insert(25);
		
		/*	press(0)	*/
		press(0);
		
		assertTrue(vm.getOutOfOrderLight().isActive());
		//by now, a simulation exception should have been thrown (no room for more coins when user attempts to insert), which means 
	}
	
	//test for exactChangeOnlyLight
	//@Test
	public void N03() throws DisabledException{
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
		
		//change can be made here, so exact change light should be off
		assertEquals(false, vm.getExactChangeLight().isActive());
		
	}

	@Test 			//TODO: uncomment other tests
	public void N04() throws DisabledException{
		/*	construct(5, 10, 25, 100; 3; 1; 10; 10)	*/
		constructCoinArgs.add(5);
		constructCoinArgs.add(10);
		constructCoinArgs.add(25);
		constructCoinArgs.add(100);
		
		selButtCount = 3;
		coinRackCap = 1;
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
		loadCoinCounts.add(1);
		loadCoinCounts.add(1);
		
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		loadPopCounts.add(1);
		
		load(loadCoinCounts, loadPopCounts);
		
		//getExactChangeLight Should be on here
		assertEquals(true, vm.getExactChangeLight().isActive());
	}
	
	//test for "Return Money" button
//	@Test
	public void N05() throws DisabledException{
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
		
		insert(100);
		
		insert(100);
		
    	Button returnButt = vm.getReturnButton();
    	returnButt.press();
    	
    	Object[] items = vm.getDeliveryChute().removeItems();
    	int val = 0;
    	for (Object obj : items){
    		val += ((Coin) obj).getValue();
    	}
    	
    	//should return two 100 value coins for a total of 200
    	assertEquals(200, val);
		
    	
    	returnButt.press();
    	
    	items = vm.getDeliveryChute().removeItems();
    	val = 0;
    	for (Object obj : items){
    		val += ((Coin) obj).getValue();
    	}
    	
    	//should return zero coins for a total of 0
    	assertEquals(0, val);

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