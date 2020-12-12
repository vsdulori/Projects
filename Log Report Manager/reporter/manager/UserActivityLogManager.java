package edu.ncsu.csc316.log.manager;

import edu.ncsu.csc316.dsa.list.ArrayBasedList;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.sorter.QuickSorter;
import edu.ncsu.csc316.log.data.LogEntry;
import edu.ncsu.csc316.log.data.Timestamp;
import edu.ncsu.csc316.log.factory.DSAFactory;
import edu.ncsu.csc316.log.io.LogEntryReader;

/**
 * This manager class processes a list of log entries to facilitate analytics,
 * including: - identifying the most frequently performed user activities -
 * grouping log entries by date - grouping log entries by hour of the day
 * 
 * @author Dr. King
 * @author Vedant Dulori
 *
 */
public class UserActivityLogManager {

	static private List<LogEntry> userEntries = LogEntryReader.loadLogEntries("input/activityLog_sample.csv");

	// private MergeSorter<E> sorter = DSAFactory.getComparisonSorter();
	/**
	 * Constructs a new manager to process the log entries contained in the file at
	 * the specified path
	 * 
	 * @param pathToFile the path to the input log entry file
	 */
    public UserActivityLogManager(String pathToFile) {
		// userEntries =
    }

    /**
     * Returns a List of the most frequently performed N user activities in the
     * input log entry file. Returns an empty List if the log contains no entries.
     * 
     * @param number the number of user activities to include in the frequency
     *               report
     * @return a List of the most frequently performed N user activities in the
     *         input log entry file
     */
	public List<String> getTopActivities(int number) {

		int entriesSize = userEntries.size();

		String actionResource[] = new String[entriesSize];

		for (int i = 0; i < entriesSize; i++) {
    		
			String actionResourceString = userEntries.get(i).getAction() + " " + userEntries.get(i).getResource();
			actionResource[i] = actionResourceString;

    	}

		int actionResourceFrequency[] = new int[entriesSize];
		int elementChecked = -1;
		for (int i = 0; i < entriesSize; i++) {
			int counter = 1;
			for (int j = i + 1; j < entriesSize; j++) {
				if (actionResource[i].equals(actionResource[j])) {
					counter++;
					// To avoid counting same element again
					actionResourceFrequency[j] = elementChecked;
				}
			}
			if (actionResourceFrequency[i] != elementChecked)
				actionResourceFrequency[i] = counter;
		}

		int actionResourceFrequencyUpdateNum[] = new int[actionResourceFrequency.length];
		for (int i = 0; i < actionResourceFrequency.length; i++) {
			// for (int j = 0; i < actionResourceFrequency.length; i++) {
			int j = 0;
			if (actionResourceFrequency[i] != -1) {
				actionResourceFrequencyUpdateNum[j] = actionResourceFrequency[i];
				// System.out.println(actionResourceFrequencyUpdateNum[j]);
				j++;
			}
			// }
		}

		String actionResourceFrequencyUpdate[] = new String[actionResourceFrequency.length];
		String sortingFrequencyEntries[] = new String[actionResourceFrequency.length];
		for (int i = 0; i < actionResourceFrequency.length; i++) {
			for (int j = 0; j < actionResourceFrequency.length; j++) {
			//int j = 0;
			// System.out.println(actionResourceFrequency[i]);
				if (actionResourceFrequency[i] != -1) {
					actionResourceFrequencyUpdate[j] = userEntries.get(i).toString();
				}
			}
		}
		int counter = 0;
		for (int item : actionResourceFrequency) {
			if (item != -1) {
				counter++;
			}
		}

		List<Integer> noNullList = new ArrayBasedList<Integer>();
		for (int i = 0; i < counter; i++) {
			if (actionResourceFrequency[i] != -1) {
				noNullList.addLast(actionResourceFrequency[i]);
			}
		}

		String[] toSort = new String[counter];
		for (int i = 0; i < noNullList.size(); i++) {
			toSort[i] = String.valueOf(noNullList.removeFirst());
		}

		for (int i = 0; i < toSort.length; i++) {
			System.out.println(toSort);
		}
		for (int j = 0; j < actionResourceFrequency.length; j++) {
			if (actionResourceFrequency[j] != -1) {

				sortingFrequencyEntries[lastIndex(sortingFrequencyEntries)] = actionResourceFrequency[j]
						+ userEntries.get(j).toString();
			}
		}
		
		for (int i = 0; i < actionResourceFrequency.length; i++) {
			// System.out.println(actionResourceFrequency[i]);
		}
		// System.out.println(sortingFrequencyEntries.length);
		// printArray(sortingFrequencyEntries);
		QuickSorter<String> frequencySorter = new QuickSorter<String>();
		frequencySorter.sort(toSort);


		// printArray(sortingFrequencyEntries);
//		for (int i = 0; i < actionResource.length; i++) {
//			System.out.println(sortingFrequencyEntries[i]);
//		}
		return null;
    }

    /**
     * Returns a Map that represents the List of log entries performed on each
     * unique date. For the Map, the String key represents the date in the format
     * MM/DD/YYYY For the Map, the List (value of entry) represents the list of log
     * entries performed on that date. Returns an empty Map if the log contains no
     * entries.
     * 
     * @return a Map that represents the List of log entries performed on each
     *         unique date
     */
	public static Map<String, List<LogEntry>> getEntriesByDate() {

		Map<String, List<LogEntry>> sortedEntryMap = DSAFactory.getMap();

		if (userEntries.isEmpty() == true)
			return sortedEntryMap;

		int entriesSize = userEntries.size();
		
		ArrayBasedList<Timestamp> tsArray = new ArrayBasedList<Timestamp>();
		
		for (int i = 0; i < entriesSize; i++) {
			if (uniqueDate(tsArray, userEntries.get(i).getTimestamp()) == true) {
				tsArray.addLast(userEntries.get(i).getTimestamp());
			}
				
		}

		String timeStampStringArray[] = new String[tsArray.size()];
		for (int i = 0; i < tsArray.size(); i++) {
			timeStampStringArray[i] = tsArray.get(i).getMonth() + "/" + tsArray.get(i).getDay() + "/"
					+ tsArray.get(i).getYear();
		}

		for (int i = 0; i < tsArray.size(); i++) {
			sortedEntryMap.put(timeStampStringArray[i], new ArrayBasedList<LogEntry>());

		}
		for (int i = 0; i < tsArray.size(); i++) {
			for (LogEntry entry : userEntries) {
				String checkTimeStamp = entry.getTimestamp().getMonth() + "/" + entry.getTimestamp().getDay() + "/"
						+ entry.getTimestamp().getYear();
				if (checkTimeStamp.equals(timeStampStringArray[i])) {
					sortedEntryMap.get(timeStampStringArray[i]).addLast(entry);
				}
			}
		}



		for (Map.Entry<String, List<LogEntry>> sortEntries : sortedEntryMap.entrySet()) {
			sort(sortEntries.getValue());
		}
//		for (String entry : sortedEntryMap) {
//			// System.out.println("key is : " + entry);
//			for (LogEntry item : sortedEntryMap.get(entry)) {
//				System.out.println(" " + item.toString());
//			}
//			// System.out.println();
//			// System.out.println();
//		}
			


		return sortedEntryMap;
			
    }


    /**
     * Returns a Map that represents the List of log entries performed during each
     * hour of the day. For the Map, the Integer key represents the hour of the day
     * (from 0-23, where 0=12AM-1AM; 1 = 1AM-2AM; etc.) in the format MM/DD/YYYY For
     * the Map, the List (value of entry) represents the list of log entries
     * performed during the given hour of the day. Returns an empty Map if the log
     * contains no entries.
     * 
     * @return a Map that represents the List of log entries performed during each
     *         hour of the day
     */
	public static Map<Integer, List<LogEntry>> getEntriesByHour() {

		Map<Integer, List<LogEntry>> sortedEntryHourMap = DSAFactory.getMap();

		if (userEntries.isEmpty() == true)
			return sortedEntryHourMap;

		for (int i = 0; i < 24; i++) {
			sortedEntryHourMap.put(i, new ArrayBasedList<LogEntry>());
		}

		for (LogEntry entry : userEntries) {
			int hour = entry.getTimestamp().isAM()
					? entry.getTimestamp().getHour() == 12 ? 0 : entry.getTimestamp().getHour()
					: entry.getTimestamp().getHour() == 12 ? 12 : entry.getTimestamp().getHour() + 12;
			sortedEntryHourMap.get(hour).addLast(entry);
		}

		for (Map.Entry<Integer, List<LogEntry>> sortEntries : sortedEntryHourMap.entrySet()) {
			sort(sortEntries.getValue());
		}

//		for (Integer entry : sortedEntryHourMap) {
//			System.out.println("key is : " + entry);
//			for (LogEntry item : sortedEntryHourMap.get(entry)) {
//				System.out.println(" " + item.toString());
//			}
//			System.out.println();
//			System.out.println();
//		}
		return sortedEntryHourMap;

    }

	public static void main(String[] args) {

		// Map<String, List<LogEntry>> dh = getEntriesByDate();
		// Map<Integer, List<LogEntry>> ih = getEntriesByHour();
//		for (LogEntry entries : ih.get(1)) {
//			System.out.println(entries);
//		}
	}

	public static <E> void printArray(E[] array) {
		for (E item : array) {
			System.out.println(item);
		}
	}

	public static <E> boolean elementExists(E[] array, E element) {
		for (E item : array) {
			if (element == item) {
				return true;
			}
		}
		return false;
	}

	public static <E> boolean uniqueDate(ArrayBasedList<Timestamp> array, Timestamp e) {
		for (int i = 0; i < array.size(); i++) {
			if ((e.getDay() == array.get(i).getDay()) && (e.getMonth() == array.get(i).getMonth())
					&& (e.getYear() == array.get(i).getYear()))
					return false;
		}
		return true;
		
	}

	/**
	 * This method sorts a given list
	 * 
	 * @param <E>  is the generic type of E
	 * @param data is the list it needs to sort
	 */
	public static <E> void sort(List<LogEntry> data) {

		int n = data.size();
		for (int i = 0; i < n - 1; i++) {
			int min = i;
			for (int j = i + 1; j <= n - 1; j++) {
				if (data.get(j).getTimestamp().compareTo(data.get(min).getTimestamp()) < 0) {
					min = j;
				}
			}

			if (!(i == min)) {
				LogEntry x = data.get(i);
				data.set(i, data.get(min));
				data.set(min, x);
			}
		}
	}

	/**
	 * Returns the index of the last element array
	 * 
	 * @param <E>   is the generic type E
	 * @param array is the array it returns the last element
	 * @return the index of the last element
	 */
	public <E> int lastIndex(E[] array) {
		for (int i = 0; i < array.length; i++) {
			return i;
		}
		return -1;
	}
}
