package edu.ncsu.csc316.trail.manager;

import java.io.FileNotFoundException;
import edu.ncsu.csc316.dsa.Weighted;
import edu.ncsu.csc316.dsa.graph.Graph;
import edu.ncsu.csc316.dsa.graph.Graph.Edge;
import edu.ncsu.csc316.dsa.graph.Graph.Vertex;
import edu.ncsu.csc316.dsa.graph.MinimumSpanningTreeUtil;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.list.positional.PositionalList;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.set.Set;
import edu.ncsu.csc316.trail.data.Attraction;
import edu.ncsu.csc316.trail.data.PotentialTrail;
import edu.ncsu.csc316.trail.factory.DSAFactory;
import edu.ncsu.csc316.trail.io.TrailReader;

/**
 * Manages analysis of potential trails to connect attractions.
 * 
 * @author Dr. King
 * @author Vedant Dulori
 */
public class TrailManager {

	/** Graph that holds all the trails */
	private Graph<Attraction, Weighted> trailGraph;

	/**
	 * Creates a new TrailManager
	 * 
	 * @param pathToTrailFile the path to the file containing trail data
	 * @throws FileNotFoundException    if the input file does not exist or cannot
	 *                                  be opened
	 * @throws IllegalArgumentException if the input file does not contain any trail
	 *                                  data
	 */
	public TrailManager(String pathToTrailFile) throws FileNotFoundException {
		List<PotentialTrail> list = TrailReader.loadPotentialTrails(pathToTrailFile);
		trailGraph = buildGraph(list);
	}

	/**
	 * Builds a graph from the provided input list. Vertices represent Attractions
	 * Edges represent PotentialTrails
	 * 
	 * @param trailData the list of data for potential trails
	 * @return a Graph that models the provided trail data
	 */
	private static Graph<Attraction, Weighted> buildGraph(List<PotentialTrail> trailData) {
		Graph<Attraction, Weighted> g =  DSAFactory.getUndirectedGraph();
		Map<Attraction, Vertex<Attraction>> m =  DSAFactory.getMap();
		for(int i = 0; i < trailData.size(); i++) {
			PotentialTrail t = trailData.get(i);
			Vertex<Attraction> v1 = null;
			Vertex<Attraction> v2 = null;
			if(m.get(t.getAttractionOne()) == null) {
				v1 = g.insertVertex(t.getAttractionOne());
				m.put(t.getAttractionOne(), v1);
			} else {
				v1 = m.get(t.getAttractionOne());
			}
			if(m.get(t.getAttractionTwo()) == null) {
				v2 = g.insertVertex(t.getAttractionTwo());
				m.put(t.getAttractionTwo(), v2);
			} else {
				v2 = m.get(t.getAttractionTwo());
			}
			g.insertEdge(v1, v2, t);
		}
		return g;
	}

	/**
	 * Returns a Set of Attractions for which all connecting trails are longer than
	 * minTrailLength ( greater than (but not equal to) minTrailLength ).
	 * 
	 * @param minTrailLength the minimum trail length to use as a threshold
	 * @return a Set of Attractions for which restrooms should be located
	 */
	public Set<Attraction> getAttractionsWithLongTrails(int minTrailLength) {
		Set<Attraction> s1 = DSAFactory.getSet();
		for(Vertex<Attraction> i : trailGraph.vertices()) {
			boolean longAttraction = true;
			for(Edge<Weighted> e : trailGraph.incomingEdges(i)) {
				if(e.getElement().getWeight() <= minTrailLength) {
					longAttraction = false;
				}
			}
			if(longAttraction) {
				s1.add(i.getElement());
			}
		}
		return s1;
	}

	/**
	 * Returns a PositionalList of Weighted edges that represents trails that
	 * produce the least costly solution to connect all attractions
	 * 
	 * @return a PositionalList of Weighted Edges represent the least costly trails
	 */
	public PositionalList<Edge<Weighted>> getLeastCostlyTrails() {
		return MinimumSpanningTreeUtil.primJarnik(trailGraph);
	}

	/**
	 * Return a Map that represents the number of trails that intersect at each
	 * specific attraction
	 * 
	 * @return a Map that represents the number of trails that intersect at each
	 *         specific attraction
	 */
	public Map<Attraction, Integer> getTrailIntersectionFrequencies() {
		PositionalList<Edge<Weighted>> t = getLeastCostlyTrails();
		Map<Attraction, Integer> inrsTrails = DSAFactory.getMap();
		for(Edge<Weighted> e: t) {
			Attraction i = ((PotentialTrail) e.getElement()).getAttractionOne();
			Attraction j = ((PotentialTrail) e.getElement()).getAttractionTwo();
			if(inrsTrails.get(j) != null) {
				int currVal = inrsTrails.get(j);
				inrsTrails.remove(j);
				currVal = currVal + 1;
				inrsTrails.put(j, currVal);
			}
			else {
				inrsTrails.put(j, 1);
			}
			if(inrsTrails.get(i) != null) {
				int currVal = inrsTrails.get(i);
				inrsTrails.remove(i);
				currVal = currVal + 1;
				inrsTrails.put(i, currVal);
			}
			else {
				inrsTrails.put(i, 1);
			}
		}
		return inrsTrails;

	}
}
