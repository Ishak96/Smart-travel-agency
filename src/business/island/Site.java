package business.island;

import business.transport.Station;

public abstract class Site {
	
	private int id;
	
	private String name;
	
	private Position position;
	
	private Station station;
	
	private int duration; // in seconds
	
	private float pertinance;
	
	public Site() {
		
	}
	
	public Site(int id, String name, Position position, Station station) {
		this.id = id;
		this.name = name;
		this.position = position;
		this.station = station;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Position getPosition() {
		return position;
	}

	public void setPlace(Position position) {
		this.position = position;
	}

	public Station getStation() {
		return station;
	}

	public int getDuration() {
		return duration;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public float getPertinance() {
		return pertinance;
	}
	
	public void setPertinance(float pertinance) {
		this.pertinance = pertinance;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public abstract boolean isHistoricSite();
	public abstract boolean isActivitySite();

	@Override
	public String toString() {
		return name;
	}
}
