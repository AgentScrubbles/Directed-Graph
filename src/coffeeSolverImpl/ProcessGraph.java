package coffeeSolverImpl;

import java.util.Map;

import coffeeSolverImpl.GraphException;
import coffeeSolverImpl.CoffeeSolverImpl.State;

public interface ProcessGraph {
	
	void processVertexEarly(int vertexID);
	
	void processEdge(int edgeID) throws IllegalArgumentException, GraphException;
	
	void ProcessVertexLate(int vertexID);
	
	void setStateMap(Map<Integer, State> stateMap);
}

