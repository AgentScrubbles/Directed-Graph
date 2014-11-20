package coffeeSolverImpl;

import lange.brandon.Locatable;

public class Vertex implements Locatable{
	private int _id;
	private double _lat;
	private double _lon;
	
	public Vertex(int id, double lat, double lon){
		_id = id;
		_lat = lat;
		_lon = lon;
	}
	
	public Vertex(int id){
		_id = id;
	}
	
	@Override
	public int hashCode(){
		return _id;
	}

	@Override
	public double getLatitude() {
		return _lat;
	}

	@Override
	public double getLongitude() {
		return _lon;
	}
	
}
