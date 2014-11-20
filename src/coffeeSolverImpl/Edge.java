package coffeeSolverImpl;

public class Edge {
	int _source;
	int _target;
	double _distance;
	String _name;
	int _id;
	
	public Edge(int id, int source, int target, double distance){
		_id = id;
		_source = source;
		_target = target;
		_distance = distance;
		_name = "";
	}
	public Edge(int id, int source, int target, double distance, String name){
		_id = id;
		_source = source;
		_target = target;
		_distance = distance;
		_name = name;
	}
	
	public Edge(int id, int source, int target){
		_id = id;
		_source = source;
		_target = target;
	}
	
	@Override
	public int hashCode(){
		return _id;
	}
}
