package business.transport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Transport {

	private static Transport transport = new Transport();
	
	private HashMap<Integer, Station> stations;
	private HashMap<Integer, Route> routes;
	private HashMap<Station, List<Route>> routesByStation;
	
	private Transport() {
		this.stations = new HashMap<Integer, Station>();
		this.routes = new HashMap<Integer, Route>();
		buildRoutesByStation();
	}
	
	private Transport(HashMap<Integer, Station> stations, HashMap<Integer, Route> routes) {
		this.stations = stations;
		this.routes = routes;
	}

	public void addStation(int id, Station station) {
		stations.put(id, station);
	}
	
	public void addRoute(int id, Route route) {
		routes.put(id, route);
	}
	
	public Collection<Station> getStations() {
		return stations.values();
	}
	/*
	public void setStations(HashMap<Integer, Station> stations) {
		this.stations = stations;
	}
	*/
	public Collection<Route> getRoutes() {
		return routes.values();
	}
	
	/*
	public void setRoutes(HashMap<Integer, Route> routes) {
		this.routes = routes;
	}
	*/

	public static Transport getTransport() {
		return transport;
	}

	public static void setTransport(Transport transport) {
		Transport.transport = transport;
	}

	public Station getStationById(int curId) {
		return stations.get(curId);
	}

	public Route getRouteById(int id) {
		return routes.get(id);
	}

	private void buildRoutesByStation() {
		Collection<Route> routes = transport.getRoutes();
		Collection<Station> stations = transport.getStations();
		routesByStation = new HashMap<Station, List<Route>>(); 
	    for	(Route route: routes) {
	    	for(Station station: stations) {
	    		// TODO: Do this better
	    		try {
	    			routesByStation.get(station).add(route);
	    		} catch (NullPointerException e) {
	    			List<Route> routeList = new ArrayList<Route>();
	    			routeList.add(route);
	    			routesByStation.put(station, routeList);
	    		}
	    	}
	    }
	}
	
	public List<Route> getRoutesByStation(Station s) {
		return routesByStation.get(s);
	}
}
