package business.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import business.transport.Route;
import business.transport.Station;
import business.transport.Transport;

public class PathFinding {

	private Transport transport;

	//Graph<Station> transport_graph; // path graph
	Graph transport_graph;
	HashMap<Station, List<Route>> buckets;
	
	// routes graph 
	public PathFinding() {
		this.transport = Transport.getTransport();
		buildTransportGraph();
		buildBuckets(); // TODO: remove
	}

	/*
	 * TODO: put this in transport
	 */
	private int calculateNumStationsInRoutes() {
		int n = 0;
		Collection<Route> routes = transport.getRoutes();
		for(Route r: routes) {
			n += r.getStations().size();
		}
		return n;
	}
	
	/*
	 * TODO: put this in transport
	 */
	private void buildBuckets() {
		Collection<Route> routes = transport.getRoutes();
		Collection<Station> stations = transport.getStations();
		buckets = new HashMap<Station, List<Route>>(); 

	    for	(Route route: routes) {
	    	for(Station station: stations) {
	    		// TODO: Do this better
	    		try {
	    			buckets.get(station).add(route);
	    		} catch (NullPointerException e) {
	    			List<Route> routeList = new ArrayList<Route>();
	    			routeList.add(route);
	    			buckets.put(station, routeList);
	    		}
	    	}
	    }
	}
	
	// TODO: This needs to change; not good
	private void buildTransportGraph() {
		 transport_graph = new Graph();

		Collection<Route> routes = transport.getRoutes();
	    Collection<Station> stations = transport.getStations();
	    
	    // connect all the consecutive route stations with a 0 weight node
	    for	(Route route: routes) {
	    	ListIterator<Station> stationRoutes = route.getStations().listIterator();
	        if(!stationRoutes.hasNext()) continue;

	        // TODO: sanity check
	        Station src = stationRoutes.next();
	        while(stationRoutes.hasNext()) {
	            Station dst = stationRoutes.next();
	            
	        	PathEntry srcEntry = PathEntry.getEntry(src, route);
	        	PathEntry dstEntry = PathEntry.getEntry(dst, route);
	        	
    	    	transport_graph.addAdjacencyEntry(srcEntry, new Node(dstEntry, 0));
    	    	src = dst;
	        }
	    }
	    
		for(Station station: stations) {
	    	List<Route> lst = buckets.get(station); // shouldn't be too big, probably less than 10
	    	
	    	// get all the possible pairs of stations
	    	for (int i = 0; i < lst.size(); i++) {
	    	    for (int j = i + 1; j < lst.size(); j++) {
	    	    	Route srcRoute = lst.get(i);
	    	    	Route dstRoute = lst.get(j);
	    	    	
	    	    	PathEntry srcEntry = PathEntry.getEntry(station, srcRoute);
	    	    	PathEntry dstEntry = PathEntry.getEntry(station, dstRoute);

	    	    	transport_graph.addAdjacencyEntry(srcEntry, new Node(dstEntry, dstRoute.getTicketPrice()));
	    	    }
	    	}
	    }
	}
	
	public Path findCheapestPath(Station A, Station B) { // (List<Station> path) {		
		// Adding the S and E node to the graph
		PathEntry S = PathEntry.getEntry(new Station(-1, null), null); // dummy entry 
		PathEntry E = PathEntry.getEntry(new Station(-2, null), null); // dummy entry
		
		List<Route> strtStation = buckets.get(A); // get all the lines that pass by the start Station
		List<Route> endStation = buckets.get(B); // get all the lines that pass by the end Station
		
		// Adding the "S" node
		for	(Route r: strtStation) {
			// String key = String.valueOf(A.getId())+";"+String.valueOf(r.getId());
			PathEntry entry = PathEntry.getEntry(A, r);
			
			transport_graph.addAdjacencyEntry(S, new Node(entry, r.getTicketPrice()));
		}
		
		// Adding the "E" node
		for (Route r: endStation) {
			PathEntry entry = PathEntry.getEntry(B, r);
			transport_graph.addAdjacencyEntry(entry, new Node(E, 0));
		}
		
		transport_graph.dijkstra(S);
		
		// removing the S node
		transport_graph.removeAdjacencyList(S);
		
		// removing the E node
		// Undo nodes to last station
		HashMap<PathEntry, List<Node>> adj = transport_graph.getAdj();
		for (Route r: endStation) {
			PathEntry entry = PathEntry.getEntry(B, r);
			List<Node> endLst = adj.get(entry);
			endLst.remove(endLst.size() - 1); 
			// remove last item added (which is the "E" node)
		}
		
		// TODO: Use this on the resulting path
		// TODO: change AbstractMap.SimpleEntry to something better
		List<PathEntry> lst = transport_graph.getPath(S, E);

		if(lst == null) {
			return null;
		}
		
		lst.remove(0); lst.remove(lst.size() - 1); // remove the S and E nodes

		Path path = new Path(lst);
		
		return path;
	}
}