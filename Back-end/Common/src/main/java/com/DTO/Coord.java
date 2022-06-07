package com.DTO;

public class Coord {
	private double lon;
	private double lat;
	private String projection;
	// public final static String DEFAULT_PROJECTION="3857";
	public final static String DEFAULT_PROJECTION = "4326";

	public Coord() {
	}

	public Coord(double lon, double lat) {
		super();
		this.lon = lon;
		this.lat = lat;
		this.projection = DEFAULT_PROJECTION;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getProjection() {
		return projection;
	}

	public void setProjection(String projection) {
		this.projection = projection;
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if (obj instanceof Coord) {
			if (this.getLon() == ((Coord) obj).getLon() && this.getLat() == ((Coord) obj).getLat()) {
				ret = true;
			}
		}
		return ret;
	}
}
