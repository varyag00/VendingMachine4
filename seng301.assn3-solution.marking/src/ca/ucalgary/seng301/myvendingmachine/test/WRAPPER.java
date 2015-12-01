package ca.ucalgary.seng301.myvendingmachine.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class WRAPPER {
    private static List<String> expecteds =
	new ArrayList<String>(Arrays.asList("construct(5, 10, 25, 100; 3; 10; 10; 10)\n", "construct(5, 10, 25, 100; 3; 10; 10; 10)\n",
					    "construct(5, 10, 25, 100; 3; 10; 10; 10)\n", "construct(1, 1; 1; 10; 10; 10)\n", "construct(0; 1; 10; 10; 10)\n",
					    "construct(1; 1; 10; 10; 10)\n",
					    "construct(5, 10, 25, 100; 1; 10; 10; 10)\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\ninsert(25)\ninsert(100)\ninsert(10)\npress(0)\nextract()\n",
					    "construct(5, 10, 25, 100; 1; 10; 10; 10)\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\n",
					    "construct(100, 5, 25, 10; 3; 2; 10; 10)\npress(0)\nextract()\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\n",
					    "construct(5, 10, 25, 100; 3; 10; 10; 10)\nextract()\n",
					    "construct(5, 10, 25, 100; 1; 10; 10; 10)\ninsert(25)\ninsert(100)\ninsert(10)\npress(0)\nextract()\n",
					    "construct(5, 10, 25, 100; 3; 10; 10; 10)\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\n",
					    "construct(100, 5, 25, 10; 3; 10; 10; 10)\npress(0)\nextract()\ninsert(100)\ninsert(100)\ninsert(100)\nextract()\n",
					    "construct(5, 10, 25, 100; 3; 10; 10; 10)\ninsert(100)\ninsert(100)\ninsert(25)\ninsert(25)\npress(0)\nextract()\n",
					    "construct(5, 10, 25, 100; 3; 10; 10; 10)\npress(0)\nextract()\n",
					    "construct(5, 10, 25, 100; 1; 10; 10; 10)\ninsert(1)\ninsert(139)\npress(0)\nextract()\n",
					    "construct(5, 10, 25, 100; 1; 10; 10; 10)\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\n",
					    "construct(100, 5, 25, 10; 3; 10; 10; 10)\ninsert(10)\ninsert(5)\ninsert(10)\npress(2)\nextract()\n",
					    "construct(5, 10, 25, 100; 3; 10; 10; 10)\npress(0)\nextract()\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\ninsert(100)\ninsert(100)\ninsert(100)\npress(0)\nextract()\n"));

    private static int testCount, totalTestCount = 0, totalMissing = 0, totalExtra = 0;
    public static StringBuilder actual;
    public static ArrayList<String> actuals;

    @BeforeClass
    public static void WRAPPERinit() {
	testCount = 0;
	actuals = new ArrayList<>();
    }

    @Before
    public void WRAPPERsetup() {
	actual = null;
	testCount++;
    }

    @After
    public void WRAPPERteardown() {
	// System.err.println("TEST: " + ++counter);
	System.err.print(actual.toString());
	System.err.println();

	actuals.add(actual.toString());

	int index = expecteds.indexOf(actual.toString());
	if(index < 0) {
	    System.err.println("MISSING:");
	    System.err.println(actual);
	}
    }

    @AfterClass
    public static void WRAPPERcheck() {
	System.err.println("SUMMARY");

	int missing = 0;
	for(String actual : actuals) {
	    int index = expecteds.indexOf(actual);
	    if(index < 0) {
		missing++;
		System.err.println("MISSING:");
		System.err.println(actual);
	    }
	    else {
		expecteds.remove(index);
	    }
	}

	totalMissing += missing;
	totalExtra = expecteds.size();
	totalTestCount += testCount;
	System.err.println("Test count: " + totalTestCount + "; missing: " + totalMissing + "; extra: " + totalExtra + "\n");

	actuals = null;
    }
}
