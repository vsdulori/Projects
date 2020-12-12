package edu.ncsu.csc316.trail.manager;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import edu.ncsu.csc316.trail.factory.DSAFactory;
/**
 * Tests TrailReportManager
 * @author Vedant Dulori
 *
 */
public class TrailReportManagerTest {

	TrailReportManager manager;
	TrailReportManager manager2;
	String getLeastCostlyTrail = "Minimum Trails for $7656.00 ($0.50 per linear foot) [\n" + 
			"   from Coffee Shop to Dog Park (2640 feet for $1320.00)\n" + 
			"   from Beautiful Falls to Coffee Shop (3168 feet for $1584.00)\n" + 
			"   from Coffee Shop to Elephant Sculpture (3696 feet for $1848.00)\n" + 
			"   from Airlie Gardens to Beautiful Falls (5808 feet for $2904.00)\n" + 
			"]\n";
	String restroomLocations = "Attractions with adjacent trails longer than 5300 feet [\n" + 
			"   Airlie Gardens\n" + 
			"]\n";
	
	String trailSigns2 = "Attractions that need a trail navigation sign [\n" + 
			"   Beautiful Falls: 4 intersecting trails\n" +
			"   Coffee Shop: 3 intersecting trails\n" + 
			"   Lake Raleigh: 3 intersecting trails\n" + 
			"]\n";
	String trailSigns = "Attractions that need a trail navigation sign [\n" +
			"   Coffee Shop: 3 intersecting trails\n" +
			"]\n";
	/**
	 * Sets up the variable
	 * @throws Exception possible exception that can be thrown
	 */
	@Before
	public void setUp() throws Exception {
		new DSAFactory();
		manager = new TrailReportManager("input/sample.csv");
		manager2 = new TrailReportManager("input/sample2.csv");
	}

	/**
     * Tests getLeastCostlyTrailsReport()
     */
	@Test
	public void testGetLeastCostlyTrailsReport() {
		assertEquals(getLeastCostlyTrail, manager.getLeastCostlyTrailsReport(0.5));
	}
	
	/**
     * Tests getRestroomLocations()
     */
	@Test
	public void testGetRestroomLocations() {
		assertEquals(restroomLocations, manager.getRestroomLocations(5300));
		
    }
	
	/**
     * Tests getTrailSignLocations()
     */
	@Test
    public void testGetTrailSignLocations() {
		assertEquals(trailSigns, manager.getTrailSignLocations());
		assertEquals(trailSigns2, manager2.getTrailSignLocations());
    }
	
}
