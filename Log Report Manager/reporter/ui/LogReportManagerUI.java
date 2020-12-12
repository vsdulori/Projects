package edu.ncsu.csc316.log.ui;

import java.util.Scanner;

import edu.ncsu.csc316.log.manager.LogReportManager;

/**
 * The UI for the entire application
 * 
 * @author Vedant Dulori
 *
 */
public class LogReportManagerUI {

	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		Scanner inputFile = null;
		System.out.print("Please enter the input file name: ");
		String inputFileName = console.next();

		try {
			LogReportManager manager = new LogReportManager(inputFileName);
			System.out.println("File imported successfully");
			System.out.println("Choose an action of your choice : )");
			System.out.println(
					"Get Top Activities(ta), Sort Activities by Date (d), Sort Activities by hour (h), Quit (q)");
			String chosenAction = console.nextLine();
			while (!chosenAction.toLowerCase().contentEquals("q")) {
				if (chosenAction.equalsIgnoreCase(chosenAction)) {
					console.close();
					System.exit(1);
				}
				if (chosenAction.toLowerCase().contentEquals("ta")) {
					System.out.println("Please enter number of top activities you want: ");
					int num = console.nextInt();
					System.out.println(manager.getTopUserActivitiesReport(num));
				} else if (chosenAction.toLowerCase().contentEquals("d")) {
					System.out.println("Please enter your desired date : ");
					String date = console.nextLine();
					System.out.println(manager.getDateReport(date));
				} else if (chosenAction.toLowerCase().contentEquals("h")) {
					System.out.println("Please enter an hour you wish to see: ");
					int hour = console.nextInt();
					System.out.println(manager.getHourReport(hour));
				} else if (chosenAction.toLowerCase().contentEquals("q")) {
					System.out.println("Sad to see you go away... Byeeeee!!!!");
					System.exit(1);
				} else {
					System.out
							.println("Please enter a valid in order to use complete functionality of this application");
					System.out.println("Choose an action of your choice : )");
					System.out.println(
							"Get Top Activities(ta), Sort Activities by Date (d), Sort Activities by hour (h), Quit (q)");
				}
			}
		} catch (Exception e) {
			System.out.println("Invalid file path/name");
		}
	}

}
