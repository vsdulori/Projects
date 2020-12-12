package edu.ncsu.csc316.trail.ui;
import java.util.Scanner;

import edu.ncsu.csc316.trail.manager.TrailReportManager;

/**
 * The UI for the entire application
 * 
 * @author Vedant Dulori
 *
 */
public class TrailReportManagerUI {
	/**
	 * Main function for the reportManagerUI
	 * @param args arguments
	 */
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		// Scanner inputFile = null;
		System.out.print("Please enter the input file name: ");
		String inputFileName = console.next();
		TrailReportManager manager;
		int exit = 0;
		while(exit == 0) {
			try {
				manager = new TrailReportManager(inputFileName);
				console = new Scanner(System.in);
				System.out.println("File imported successfully");
				System.out.println("Choose an action of your choice : )");
				System.out.println(
						"Get Least Costly Trails(lc), Get Restroom Locations (rl), Get Trail Sign Locations (sl), Quit (q)");
				String chosenAction = console.next();
				if (chosenAction.toLowerCase().contentEquals("lc")) {
					System.out.println("Please enter the cost per foot: ");
					double costPerFoot = console.nextDouble();
					System.out.println(manager.getLeastCostlyTrailsReport(costPerFoot));
				} else if (chosenAction.toLowerCase().contentEquals("rl")) {
					System.out.println("Please enter your minimum trail length: ");
					int minTrailLength = console.nextInt();
					System.out.println(manager.getRestroomLocations(minTrailLength));
				} else if (chosenAction.toLowerCase().equalsIgnoreCase("sl")) {
					System.out.println(manager.getTrailSignLocations());
				} else if (chosenAction.toLowerCase().contentEquals("q")) {
					System.out.println("Sad to see you go away... Byeeeee!!!!");
					exit = 1;
					console.close();
					System.exit(1);
				} else {
					System.out
							.println("Please enter a valid in order to use complete functionality of this application");
					System.out.println("Choose an action of your choice : )");
					System.out.println(
							"Get Least Costly Trails(lc), Get Restroom Locations (rl), Get Trail Sign Locations (sl), Quit (q)");
				}
			} catch (Exception e) {
				System.out.print("Please enter the input file name: ");
				inputFileName = console.next();
			}
		}
	}

}
