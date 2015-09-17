package vip_project.algorithms;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * This class is used for testing the algorithms. I am using the
 * built-in ArrayList sorting method because I assume we will be 
 * able to retrieve information from SQLite (or whatever system
 * we end up using) and store it in an ArrayList. It is an efficient
 * implementation and easy to use. This idea should be scalable to 
 * use if/when we decide to make custom Movie classes and the like. 
 * 
 * @author Cyril Casapao
 */
public class AlgorithmTester {

	// Data sets to be used in tests
	private static ArrayList<String> alphabeticData =
		new ArrayList<String>(Arrays.asList(
			"Star Wars",
			"Indiana Jones",
			"Harry Potter and the Order of the Phoenix",
			"Pulp Fiction",
			"Up",
			"The Incredibles",
			"Napoleon Dynamite",
			"The Lego Movie",
			"My Neighbor Totoro",
			"AAA",
			"AaA",
			"Monty Python and the Holy Grail"
		)
	);
	
	private static ArrayList<String> alphanumericData = 
		new ArrayList<String>(Arrays.asList(
			"Star Wars Episode 5: The Empire Strikes Back",
			"Rocky II",
			"Star Wars Episode 3: Revenge of the Sith",
			"Rocky VI",
			"A Hard Day's Night",
			"Scott Pilgrim vs. the World",
			"Rocky IV",
			"Rocky V",
			"Help!",
			"Rocky III",
			"007: James Bond",
			"Up",
			"A Question?",
			"Monty Python and the Holy Grail",
			"Monty Python & the Holy Grail",
			"Airplane!",
			"03: Bames Jond"
		)
	);
	
	
	/**
	 * Here is the main method. It runs a looping user dialouge so we
	 * we can interactively test different search and sorting cases.
	 */
	public static void main(String[] args) {		
		Scanner scan = new Scanner(System.in);
		boolean wantToExit = false;
		System.out.println("Welcome to the algorithm tester." +
				"Type EXIT at any time to exit.");

		while(!wantToExit) {
			System.out.println("Please enter the number of the test " + 
					"you would like to run.");
			System.out.println("1) Alphabetic Sort - Sort an array of " +
					"purely alphabetic strings.");
			System.out.println("2) Alphanumeric Sort - Sort an array of " +
					"alphanumeric strings.");
			System.out.println("3) Alphabetic Search - Serach an array " +
					"of purely alphabetic strings.");
			System.out.println("4) Alphanumeric Search - Serach an array " +
					"of alphanumeric strings.");
			
			String input = scan.nextLine();
			wantToExit = checkInput(input);
		}
		scan.close();
		System.out.println("Exiting...");
		System.exit(0);
	} 
	
	
	/**
	 * This method decides what to do based on user input. It either
	 * calls one of the testing methods or it halts execution of the
	 * testing program.
	 * 
	 * @param 	text		The input to check
	 * @return 	boolean		True to exit; false to continue
	 */
	private static boolean checkInput(String text) {
		if(text.equals("EXIT")) {
			return true;
		}
		
		try {
			int input = Integer.parseInt(text);
			switch(input) {
				case 1: 	alphabeticSort();
							break;
				case 2: 	alphanumericSort();
							break;
				case 3: 
				case 4:
				default:	System.out.println("Invalid number.");
							return true;
			}
		} catch (NumberFormatException e) {
			System.out.println("That's not a valid number. Please try again.\n");
		}
		return false;
	}
	
	
	/**
	 * Test the built-in sort method on simple alphabetic strings.
	 */
	private static void alphabeticSort() {
		
		// Create a temporary local variable so the original
		// data set does not get modified which allows use to
		// reuse it in other tests.
		ArrayList<String> result = new ArrayList<String>();
		result.addAll(alphabeticData);
		
		// Now we sort it!
		Collections.sort(result);
		
		// Display testing messages.
		printSortMessages("alphabeticSort", alphabeticData, result);
	}
	
	
	/**
	 * Test the built-in sort method on more complicated
	 * alphanumeric strings.
	 */
	private static void alphanumericSort() {
		ArrayList<String> result = new ArrayList<String>();
		result.addAll(alphanumericData);
		Collections.sort(result);
		printSortMessages("alphanumericSort", alphanumericData, result);
	}
	
	
	/**
	 * This method simply prints the elements of the unsorted
	 * array and then the sorted result in an organized manner.
	 * It is here to separate the display logic from the actual
	 * testing logic and helps make testing a little more modular.
	 * 
	 * @param testName		The name of the test being performed
	 * @param toSort		The set of strings to be sorted
	 * @param result		The result of sorting
	 */
	private static void printSortMessages(
		String testName,
		ArrayList<String> toSort, 
		ArrayList<String> result
	)
	{
		System.out.println("|--------- BEGIN " + testName + "---------|");
		int length = toSort.size();
		
		System.out.println("Initial array:");
		for(int i = 0; i < length; i++) {
			System.out.println("Element " + i + ": " + toSort.get(i) + "");
		}

		System.out.println("\nAfter sort:");
		for(int i = 0; i < length; i++) {
			System.out.println("Element " + i + ": " + result.get(i) + "");
		}
		System.out.println("|--------- END " + testName + "---------|\n");
	}
}
