package coffeeSolverImpl;

public class GraphException extends Exception{

	private String _message;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6393559320431236254L;
	
	public GraphException(String s){
		_message = s;
	}
	
	public GraphException(int edgeID, String name){
		_message = "Back edge found on edge " + edgeID + ": " + name;
	}

	@Override
	public String getMessage(){
		return _message;
	}
}
