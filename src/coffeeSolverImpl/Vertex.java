package coffeeSolverImpl;


public class Vertex{
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

	public double lat(){
		return _lat;
	}
	public double lon(){
		return _lon;
	}
	
}
