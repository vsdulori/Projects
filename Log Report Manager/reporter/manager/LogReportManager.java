package edu.ncsu.csc316.log.manager;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.log.data.LogEntry;
import edu.ncsu.csc316.log.data.Timestamp;

/**
 * LogReportManager interacts to retrieve log file analytics and format the
 * output for the user interface
 * 
 * @author Dr. King
 * @author Vedant Dulori
 *
 */
public class LogReportManager {

    private UserActivityLogManager activityLogManager;

    /**
     * Constructs a new manager to process the log file at the given path
     * 
     * @param pathToFile the path to the input file with log entries
     */
    public LogReportManager(String pathToFile) {
        activityLogManager = new UserActivityLogManager(pathToFile);
    }

    /**
     * Returns a report of the N most frequently performed user activities in the
     * input log file.
     * 
     * @param number the number of user activities to include in the report
     * @return a report of the N most frequently performed user activities
     */
    public String getTopUserActivitiesReport(int number) {
		if (number < 1) {
			return "Please enter a number > 0";
		}

		Map<String, List<LogEntry>> topActivitiesMap = UserActivityLogManager.getEntriesByDate();
		if (topActivitiesMap.isEmpty()) {
			return "The log file does not contain any user activities";
		}
		String topUserActivitiesString = "";
		for (Entry<String, List<LogEntry>> topActivities : topActivitiesMap.entrySet()) {
//			if (dateActivities.getValue() == null) {
//				return "Actvities recorded during hour " + date;
//			} else {
			topUserActivitiesString = "Top User Activities Report " + " [\n";
			for (LogEntry topActivityEntries : topActivities.getValue()) {
				topUserActivitiesString += "   " + topActivityEntries.toString().substring(9) + "\n";
			}
			topUserActivitiesString += "]";
//			}
		}

		return topUserActivitiesString;
    }

    /**
     * Returns a report of log entries that were recorded on a specified date
     * 
     * @param date the date for which to retrieve log entries
     * @return a report of log entries that were recorded on the specified date
     */
    public String getDateReport(String date) {
		if (!Timestamp.isValidDate(date)) {
			return "Please enter a valid date in the format MM/DD/YYYY";
		}

		System.out.println(date);
		Map<String, List<LogEntry>> dateMap = UserActivityLogManager.getEntriesByDate();
		String dateEntriesString = "";
		for (Entry<String, List<LogEntry>> dateActivities : dateMap.entrySet()) {
			// System.out.println(dateActivities.getKey());
			if (dateActivities.getValue() == null) {
				return "Activities recorded on " + date;
			} else {

				int intArray[] = new int[3];
				String strArray[] = date.split("/", 5);
				for (int i = 0; i < intArray.length; i++) {
					intArray[i] = Integer.parseInt(strArray[i]);
				}
				String removeCheck = "";
				for (int item : intArray) {
					removeCheck += String.valueOf(item) + "/";
				}
				removeCheck = removeCheck.substring(0, removeCheck.length() - 1);
				if (removeCheck.equals(dateActivities.getKey())) {
					dateEntriesString = "Activities recorded on " + date + " [\n";
					for (LogEntry hourEntries : dateActivities.getValue()) {
						dateEntriesString += "   "
								+ hourEntries.toString().substring(9, hourEntries.toString().length() - 1) + "\n";
					}
					dateEntriesString += "]";
				}
			}
		}
		return dateEntriesString;
    }

    /**
     * Returns a report of log entries that were recorded during the specified hour
     * of the day, where 0 = 12AM-1AM; 1 = 1AM-2AM; 2 = 2AM-3AM; ... 23 = 11PM-midnight
     * 
     * @param hour the hour for which to retrieve log entries
     * @return a report of log entries that were recorded during the specified hour
     *         of the day
     */
    public String getHourReport(int hour) {
		if (hour < 0 || hour > 23) {
			return "Please enter a valid hour between 0 (12AM) and 23 (11PM)";
		}

		Map<Integer, List<LogEntry>> hourMap = UserActivityLogManager.getEntriesByHour();
		String hourEntriesString = "";
		for (Entry<Integer, List<LogEntry>> hourActivities : hourMap.entrySet()) {
			if (hourActivities.getValue() == null) {
				return "Actvities recorded during hour " + hour;
			} else {
				hourEntriesString = "Activities recorded during hour " + hour + " [\n";
				for (LogEntry hourEntries : hourActivities.getValue()) {
					hourEntriesString += "   " + hourEntries.toString().substring(9) + "\n";
				}
				hourEntriesString += "]";
			}
		}

		return hourEntriesString;

    }

}
