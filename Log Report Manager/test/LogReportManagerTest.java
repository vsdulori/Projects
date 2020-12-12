package edu.ncsu.csc316.log.manager;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests all the methods
 *
 * @author Vedant Dulori
 */

public class LogReportManagerTest {

	LogReportManager list;
	private String test = "input/activityLog_sample.csv";

	@Before
	public void setUp() {
		list = new LogReportManager(test);

	}

	/**
	 * Tests topUserActivitiesReport()
	 */
	@Test
	public void testTopUserActivitiesReport() {
		assertEquals("Top User Activities Report [\n" + "   13: sort HL7 Code 422\n" + 
				"   2: print office visit OV02132\n" + 
				"]", list.getTopUserActivitiesReport(2));
		assertEquals("Top User Activities Report [\n" + 
				"   13: sort HL7 Code 422\n" + 
				"   2: print office visit OV02132\n" + 
				"   1: unmerge notification NX1115\n" + 
				"   1: view HL7 Code 422\n" + 
				"]", list.getTopUserActivitiesReport(4));
		assertEquals("Please enter a number > 0", list.getTopUserActivitiesReport(-1));

	}
	
	/**
	 * Tests getDateReport()
	 */
	@Test
	public void testgetDateReport() {
		assertEquals("Activities recorded on 01/04/2016 [\n" + 
				"   hqcooney, 01/04/2016 01:12:22AM, view, HL7 Code 422\n" + 
				"   gphsu, 01/04/2016 12:44:52PM, sort, HL7 Code 422\n" + 
				"]", list.getDateReport("01/04/2016"));
		assertEquals("Please enter a valid date in the format MM/DD/YYYY", list.getDateReport("13/04/2016"));
		assertEquals("Please enter a valid date in the format MM/DD/YYYY", list.getDateReport("01-04-2016"));

	}
	
	/**
	 * Tests getHourReport()
	 */
	@Test
	public void testgetHourReport() {
		assertEquals(
				"Activities recorded during hour 5 [\n" + "   gphsu, 02/27/2017 05:26:50AM, sort, HL7 Code 422\n"
						+ "   hqcooney, 09/15/2017 05:44:15AM, unmerge, notification NX1115\n" + "]",
				list.getHourReport(5));
		assertEquals("Activities recorded during hour 5 []", list.getHourReport(2));
		assertEquals("Please enter a valid hour between 0 (12AM) and 23 (11PM)", list.getTopUserActivitiesReport(24));
		assertEquals("Please enter a valid hour between 0 (12AM) and 23 (11PM)", list.getTopUserActivitiesReport(-1));

	}

}