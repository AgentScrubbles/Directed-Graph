package coffeeSolverImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import coffeeSolver.CoffeeSolver;
import coffeeSolver.Dijkstra;
import coffeeSolver.Graph;
import coffeeSolver.Weighing;
import coffeeSolverImpl.GraphException;

;

public class CoffeeSolverImpl<V, E> implements CoffeeSolver<V, E> {

	/**
	 * Topological Sort
	 */
	@Override
	public List<Integer> sortVertices(Graph<V, E> graph) {
		return generateValidSortS(graph).iterator().next();
	}

	@Override
	public List<Integer> shortestPath(Graph<V, E> graph,
			List<Integer> locations, Weighing<E> weigh) {
		List<Integer> fullPath = new ArrayList<Integer>();

		Dijkstra<V, E> dij = new DijkstraImpl<V, E>();
		dij.setWeighing(weigh);
		dij.setGraph(graph);

		
		//Add the first one in.
		fullPath.add(locations.get(0));
		for (int i = 0; i < locations.size() - 1; i++) {
			dij.setStart(locations.get(i));
			dij.computeShortestPath();
			List<Integer> localPath = dij.getPath(locations.get(i + 1));
			fullPath.addAll(localPath);
		}

		return fullPath;
	}

	@Override
	public Collection<List<Integer>> generateValidSortS(Graph<V, E> graph) {
		Collection<List<Integer>> collection = new HashSet<List<Integer>>();
		Stack<Integer> path = new Stack<Integer>();

		TopoProcessor topo = new TopoProcessor(graph, path);
		Iterator<Integer> iter = graph.getVertices().iterator();

		while (iter.hasNext()) {
			try {
				DepthFirstSearch(graph, topo, iter.next(), true);
				collection.add(reverseStack(path));
			} catch (GraphException ge) {
				// Ignore, this means there was a back edge, and not to add it
				System.out.println(ge.getMessage());
			} catch (Exception e) {
				return null;
			}
		}
		return collection;
	}

	/**
	 * Generic form of Depth First Search.  Allows it to be manipulated in several different ways using abstraction
	 * @param graph
	 * 		Graph for work to be performed on
	 * @param processor
	 * 		See ProcessGraph interface.  Allows abstraction for what to do at critical points
	 * @param startIndex
	 * 		Index to start at
	 * @param processDisconnected
	 * 		If DFS should stop when all reachable vertices have been reached, or find an unreached node and continue.
	 * @throws GraphException
	 * 		Special exception that can be thrown from ProcessGraph interface.  Allows special errors that DFS may not
	 * 		know about
	 */
	public void DepthFirstSearch(Graph<V, E> graph, ProcessGraph processor,
			int startIndex, boolean processDisconnected) throws GraphException {
		Map<Integer, State> stateMap = new HashMap<Integer, State>();
		for (int i : graph.getVertices()) {
			stateMap.put(i, State.UNDISCOVERED);
		}
		processor.setStateMap(stateMap);
		DFS(graph, processor, startIndex, stateMap, processDisconnected); //Run DFS one time
		while (processDisconnected && stateMap.containsValue(State.UNDISCOVERED)){
			//Find a vertex with an UNDISCOVERED value.
			for(int key : stateMap.keySet()){
				if(stateMap.get(key) == State.UNDISCOVERED){
					DFS(graph, processor, key, stateMap, processDisconnected);
					break;
				}
			}
		}
	}
	
	private void DFS(Graph<V, E> graph, ProcessGraph processor, int index, Map<Integer, State> stateMap, boolean processDisconnected) throws IllegalArgumentException, GraphException{
		stateMap.remove(index);
		stateMap.put(index, State.DISCOVERED);
		processor.processVertexEarly(index);

		
			for (int edgeID : graph.getEdgesOf(index)) {
				// edge is the actual edge, get the vertex.
				// Process edge: if returns false cancel entire search
				processor.processEdge(edgeID);
				int neighborID = graph.getTarget(edgeID);
				if (stateMap.get(neighborID) == State.UNDISCOVERED) {
					DFS(graph, processor, neighborID, stateMap, processDisconnected);
				}
			}
		

		processor.ProcessVertexLate(index);
		stateMap.remove(index);
		stateMap.put(index, State.PROCESSED);
	}

	private List<Integer> reverseStack(Stack<Integer> stack) {
		List<Integer> list = new ArrayList<Integer>();
		while (!stack.isEmpty()) {
			list.add(stack.pop());
		}
		return list;
	}

	

	private class TopoProcessor implements ProcessGraph {

		Stack<Integer> _stack;
		Graph<V, E> _graph;
		Map<Integer, State> _stateMap;

		public TopoProcessor(Graph<V, E> graph, Stack<Integer> stack) {
			_stack = stack;
			_graph = graph;
		}

		@Override
		public void processVertexEarly(int vertexID) {

		}

		@Override
		public void processEdge(int edgeID) throws IllegalArgumentException,
				GraphException {
			// Check for back edges
			// If the edge -> targetID is in the path already, then it fails.
			if (_stateMap.get(_graph.getTarget(edgeID)) == State.DISCOVERED) {
				_stack.clear();
				throw new GraphException(edgeID, _graph.getAttribute(edgeID)
						.toString());
			}
		}

		@Override
		public void ProcessVertexLate(int vertexID) {
			_stack.push(vertexID);
		}


		@Override
		public void setStateMap(Map<Integer, State> stateMap) {
			_stateMap = stateMap;
		}

	}

	public enum State {
		DISCOVERED, UNDISCOVERED, PROCESSED
	}

}
