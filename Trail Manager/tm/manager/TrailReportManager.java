package edu.ncsu.csc316.trail.manager;

import java.io.FileNotFoundException;
import java.util.Iterator;

import edu.ncsu.csc316.dsa.Weighted;
import edu.ncsu.csc316.dsa.graph.Graph.Edge;
import edu.ncsu.csc316.dsa.list.positional.PositionalList;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.dsa.set.Set;
import edu.ncsu.csc316.dsa.sorter.MergeSorter;
import edu.ncsu.csc316.dsa.sorter.Sorter;
import edu.ncsu.csc316.trail.data.Attraction;
import edu.ncsu.csc316.trail.data.PotentialTrail;
import edu.ncsu.csc316.trail.factory.DSAFactory;

/**
 * Creates String Reports of trail information for the user interface
 * 
 * @author Dr. King
 * @author Vedant Dulori
 */
public class TrailReportManager {

	/** TrailReportManage */
	private TrailManager trailManager;

	/**
	 * Constructs a new TrailReportManager to facilitate generation of String
	 * reports for the TrailManager user interface.
	 * 
	 * @param pathToTrailFile the path to the file that contains trail data
	 * @throws FileNotFoundException    if the file does not exist or cannot be read
	 * @throws IllegalArgumentException if the file does not contain any trail data
	 */
	public TrailReportManager(String pathToTrailFile) throws FileNotFoundException {
		trailManager = new TrailManager(pathToTrailFile);
	}

	/**
	 * Returns a report of the least costly trails to connect all attractions
	 * 
	 * @param costPerFoot the cost (in USD $) per foot of trail construction
	 * @return a String report of the least costly trails to connect all attractions
	 */
	public String getLeastCostlyTrailsReport(double costPerFoot) {
		if (costPerFoot <= 0) {
			return "Construction cost per linear foot must be > $0.00";
		}
		PositionalList<Edge<Weighted>> leastCostlyTrails = trailManager.getLeastCostlyTrails();
		PotentialTrail[] pTrailArr = new PotentialTrail[leastCostlyTrails.size()];
		Sorter<PotentialTrail> potenTrailSorter = DSAFactory.getComparisonSorter();
		Iterator<Edge<Weighted>> it = leastCostlyTrails.iterator();
		int distanceTraveled = 0;
		for(int i = 0; i < pTrailArr.length; i++) {
			Edge<Weighted> currEdge = it.next();
			pTrailArr[i] = (PotentialTrail) currEdge.getElement();
			distanceTraveled += pTrailArr[i].getWeight();
		}
		potenTrailSorter.sort(pTrailArr);
		double completeCost = distanceTraveled * costPerFoot;
		StringBuilder leastAttractTrailString = new StringBuilder(""); 
		leastAttractTrailString.append("Minimum Trails for $").append(String.format("%.2f", completeCost))
			.append(" ($").append(String.format("%.2f", costPerFoot)).append(" per linear foot) [\n");
		StringBuilder trailsString = new StringBuilder(""); 
		for(int i = 0; i < pTrailArr.length; i++) {
			double trailCost = pTrailArr[i].getWeight() * costPerFoot;
			trailsString.append("   from ");
			if(pTrailArr[i].getAttractionOne().getName().compareTo(pTrailArr[i].getAttractionTwo().getName()) > 0) {
				trailsString.append(pTrailArr[i].getAttractionTwo().getName()).append(" to ").append(pTrailArr[i].getAttractionOne().getName());
			} else {
				trailsString.append(pTrailArr[i].getAttractionOne().getName()).append(" to ").append(pTrailArr[i].getAttractionTwo().getName());
			}
			trailsString.append(" (");
			trailsString.append(pTrailArr[i].getWeight()).append(" feet for $").append(String.format("%.2f", trailCost)).append(")\n");
		}
		leastAttractTrailString.append(trailsString).append("]\n");
		return leastAttractTrailString.toString();
	}

	/**
	 * Returns a report of the attractions that should be considered for restroom
	 * locations.
	 * 
	 * @param minTrailLength the minimum trail length to use as a threshold for
	 *                       determining restroom locations
	 * @return a String report of the attractions that have connecting trails with
	 *         lengths longer than the minimum trail length threshold
	 */
	public String getRestroomLocations(int minTrailLength) {
		if (minTrailLength <= 0) {
			return "Trail length must be > 0 feet.";
		}
		if (minTrailLength >= 100000) {
			return "No attractions are endpoints of trails longer than 100000 feet.";
		}
		Set<Attraction> restroomLocations = trailManager.getAttractionsWithLongTrails(minTrailLength);
		if(restroomLocations.size() == 0) {
			return "No attractions are endpoints of trails longer than " + minTrailLength + " feet.";
		}
		Attraction[] restroomAttractionArr = new Attraction[restroomLocations.size()];
		Sorter<Attraction> restroomLocationSorter = DSAFactory.getComparisonSorter();
		Iterator<Attraction> it = restroomLocations.iterator();
		for(int i = 0; i < restroomAttractionArr.length; i++) {
			Attraction currRestroom = it.next();
			restroomAttractionArr[i] = currRestroom;
		}
		restroomLocationSorter.sort(restroomAttractionArr);
		StringBuilder restroomsString = new StringBuilder("");
		restroomsString.append("Attractions with adjacent trails longer than ").append(minTrailLength).append(" feet [\n");
		for(int i = 0; i < restroomAttractionArr.length; i++) {
			restroomsString.append("   ").append(restroomAttractionArr[i].getName()).append("\n");
		}
		restroomsString.append("]\n");
		return restroomsString.toString();
	}

	/**
	 * Returns a report of the attractions that should be considered for trail
	 * navigation sign locations.
	 * 
	 * @return a String report of the attractions that have at least 3 connecting
	 *         trails in the set of least costly trails
	 */
	public String getTrailSignLocations() {
		Map<Attraction, Integer> signMap = trailManager.getTrailIntersectionFrequencies();
		if(signMap.size() == 0) {
			return "No attractions need trail navigation signs.";
		}
		SignAttractionFrequencySortMap[] signAttractionFrequencyList = new SignAttractionFrequencySortMap[signMap.size()];
		Iterator<Map.Entry<Attraction, Integer>> itr = signMap.entrySet().iterator();
		for(int i = 0; i < signAttractionFrequencyList.length; i++) {
			Entry<Attraction, Integer> entry = itr.next();
			signAttractionFrequencyList[i] = new SignAttractionFrequencySortMap(entry.getKey(), entry.getValue());
		}
			
		MergeSorter<SignAttractionFrequencySortMap> mapSorter = new MergeSorter<SignAttractionFrequencySortMap>();
		mapSorter.sort(signAttractionFrequencyList);
		StringBuilder signLocationsString = new StringBuilder("");
		int ctr = 0;
		for(int i = 0; i < signAttractionFrequencyList.length; i++) {
			if(signAttractionFrequencyList[i].getFrequency() >= 3) {
				signLocationsString.append("   ");
				signLocationsString.append(signAttractionFrequencyList[i].getSignAttraction().getName()).append(": ").append(signAttractionFrequencyList[i].getFrequency()).append(" intersecting trails\n");
				ctr++;
			}
		}
		StringBuilder trailSignLocations = new StringBuilder("");
		trailSignLocations.append("Attractions that need a trail navigation sign [\n");
		trailSignLocations.append(signLocationsString);
		trailSignLocations.append("]\n");
		if(ctr == 0) {
			return "No attractions need trail navigation signs.";
		} else {
			return trailSignLocations.toString();
		}

	}
	
	/**
	 * Private class to implement sort map
	 * @author Vedant Dulori
	 *
	 */
	private class SignAttractionFrequencySortMap implements Comparable<SignAttractionFrequencySortMap> {
		/** Attraction key */
		private Attraction signAttraction;
		/** Frequency value */
		private int frequency;
		
		/**
		 * Construction
		 * @param signAttraction attraction key
		 * @param frequency attraction's frequency
		 */
		SignAttractionFrequencySortMap(Attraction signAttraction, int frequency) {
			this.signAttraction = signAttraction;
			this.frequency = frequency;
			
		}
		/**
		 * Compares different frequencies
		 * @param o key
		 * @return 0 if equal -1 if bigger and 1 if smaller
		 */
		public int compareTo(SignAttractionFrequencySortMap o) {
			if (frequency < o.getFrequency()) {
				return 1;
			} else if (frequency > o.getFrequency()) {
				return -1;
			} else {
				return this.signAttraction.compareTo(o.getSignAttraction());
			}
		}
		/**
		 * Returns frequency of the key
		 * @return frequency
		 */
		public int getFrequency() {
			return this.frequency;
		}
		/**
		 * Returns attraction key
		 * @return attraction object key
		 */
		public Attraction getSignAttraction() {
			return this.signAttraction;
		}
	}
}


