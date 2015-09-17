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
	
	// This is initialized in the main method; it is
	// a combination of the above two ArrayLists
	private static ArrayList<String> allData;
	
	
	/**
	 * Here is the main method. It runs a looping user dialogue so we
	 * we can interactively test different search and sorting cases.
	 * It also initializes an ArrayList of all the test items which
	 * is used in testing the search functionality.
	 */
	public static void main(String[] args) {		
		allData = new ArrayList<String>();
		allData.addAll(alphabeticData);
		allData.addAll(alphanumericData);
		
		// Begin dialogue loop
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
			System.out.println("3) Search - Serach an array of " +
					"alphanumeric strings.");
			
			String input = scan.nextLine();
			wantToExit = checkInput(input, scan);
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
	 * We need to receive a scanner because instantiating and closing
	 * a new local scanner also closes System.in which is not what 
	 * we want.
	 * 
	 * @param 	text		The input to check
	 * @param	scan		The scanner that receives input
	 * @return 	boolean		True to exit; false to continue
	 */
	private static boolean checkInput(String text, Scanner scan) {
		if(text.equals("EXIT")) {
			return true;
		}
		
		try {
			int input = Integer.parseInt(text);
			switch(input) {
				case 1: 	printSortMessages(
								"Alphabetic Test", 
								alphabeticData
							);
							break;
				case 2: 	printSortMessages(
								"Alphanumeric Test",
								alphanumericData
							);
							break;
				case 3: 	System.out.println("Enter a title to find.");
							String toFind = scan.nextLine();
							printSearchMessages("Search", toFind, allData);
							break;
				default:	System.out.println("Invalid number.");
							return true;
			}
		} catch (NumberFormatException e) {
			System.out.println("That's not a valid number. Please try again.\n");
		}
		return false;
	}
	
	
	/**
	 * This method tests the built-in sort. It performs the sort
	 * and prints the original unsorted array and then the sorted
	 * array.
	 * 
	 * @param testName		The name of the test being performed
	 * @param toSort		The set of strings to be sorted
	 * @param result		The result of sorting
	 */
	private static void printSortMessages(
		String testName,
		ArrayList<String> dataToSort
	)
	{
		
		// Create a temporary local variable so the original
		// data set does not get modified which allows use to
		// reuse it in other tests.
		ArrayList<String> result = new ArrayList<String>();
		result.addAll(dataToSort);
		
		// Now we sort it!
		Collections.sort(result);
		
		// Keep this info in a local variable so we don't have
		// to keep calling the size method in our loops
		int length = dataToSort.size();
		
		// Print messages
		System.out.println("|--------- BEGIN " + testName + "---------|");
		System.out.println("Initial array:");
		for(int i = 0; i < length; i++) {
			System.out.println("Element " + i + ": " + dataToSort.get(i) + "");
		}
		System.out.println("\nAfter sort:");
		for(int i = 0; i < length; i++) {
			System.out.println("Element " + i + ": " + result.get(i) + "");
		}
		System.out.println("|--------- END " + testName + "---------|\n");
	}
	
	
	/**
	 * This method tests out the built-in binary search. It performs
	 * the search and prints the location at which the search key
	 * was found.
	 * 
	 * @param testName	The name of the test being performed
	 * @param toFind	The string we are searching for
	 * @param data		The data set we are searching through
	 */
	private static void printSearchMessages(
		String testName,
		String toFind,
		ArrayList<String> data
	)
	{
		// Perform the search
		int result = search(toFind);
		int length = data.size();
		System.out.println("Result is " + result);
		
		// Print messages
		System.out.println("|--------- BEGIN " + testName + "---------|");
		System.out.println("Searching this data set for " + toFind + "...");
		
		for(int i = 0; i < length; i++){
			System.out.println("Element " + i + ": " + data.get(i) + "");
		}
		
		if(result >= 0) {
			System.out.println("\nFound at index " + result + ".");
		} else {
			System.out.println(toFind + " was not found in the data set.");
		}
		
		System.out.println("|--------- END " + testName + "---------|\n");
	}
	
	
	/**
	 * This method searches an ArrayList for the given string. Instead
	 * of writing our own specialized search, I think it would be best 
	 * to simply sort the ArrayList using the sort method and then
	 * perform a binary search. 
	 * 
	 * The built-in search is designed to run in O(n*log(n)) time. 
	 * Adding a binary search will put our time at O(n*log(n) + log(n))
	 * which isn't too bad.
	 * 
	 * If we want to make the search case insensitive, we will need to
	 * run the block of code that I commented out. It's a brute force
	 * loop through the ArrayList that makes all strings lower case.
	 * This will add an extra running time factor of O(n) but I think
	 * it would be worth it. Who wants a case sensitive search anyway?
	 * 
	 * @param toFind	The string to find
	 * @return int		Index of the element if found; 
	 * 					A negative number if not found
	 */
	private static int search(String toFind){
		ArrayList<String> sortedData = new ArrayList<String>();
		sortedData.addAll(allData);
		Collections.sort(sortedData);
		
		// Uncomment this block of code for case insensitive search
		/**
		int length = sortedData.size();
		for(int i = 0; i < length; i++) {
			sortedData.set(i, sortedData.get(i).toLowerCase());
		}
		*/
		
		String[] sortedArray = sortedData.toArray(new String[sortedData.size()]);
		return Arrays.binarySearch(sortedArray, toFind);	
	}
}