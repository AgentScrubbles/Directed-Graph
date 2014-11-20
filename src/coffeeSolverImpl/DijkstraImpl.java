package coffeeSolverImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import coffeeSolver.Dijkstra;
import coffeeSolver.Graph;
import coffeeSolver.Weighing;

public class DijkstraImpl<V, E> implements Dijkstra<V, E>{

	Graph<V, E> _graph;
	int _start;
	Weighing<E> _weighing;
	Map<Integer, VertWrapper> _complete;
	
	@Override
	public void setGraph(Graph<V, E> graph) {
		this._graph = graph;
	}

	@Override
	public void setStart(int startId) throws IllegalArgumentException,
			IllegalStateException {
		if(!_graph.getVertices().contains(startId)){
			throw new IllegalArgumentException("StartID not in graph!");
		}
		_start = startId;
	}

	@Override
	public void setWeighing(Weighing<E> weighing) {
		_weighing = weighing;
	}

	@Override
	public void computeShortestPath() throws IllegalStateException {
		if(_weighing == null || _graph == null){
			throw new IllegalStateException("Graph and Weighing not set!");
		}
		_complete = dijkstra();
	}

	@Override
	public List<Integer> getPath(int endId) throws IllegalArgumentException,
			IllegalStateException {
		if(_complete == null){
			throw new IllegalStateException("computeShortestPath not called!");
		}
		Stack<Integer> reversed = new Stack<Integer>();
		List<Integer> path = new ArrayList<Integer>();
		
		VertWrapper vw = _complete.get(endId);
		while(vw._vid != _start){
			reversed.push(vw._vid);
			vw = vw._parent;
		}
		while(!reversed.empty()){
			path.add(reversed.pop());
		}
		return path;
	}

	@Override
	public double getCost(int endId) throws IllegalArgumentException,
			IllegalStateException {
		return _complete.get(endId)._distance;
	}

	
	private Map<Integer, VertWrapper> dijkstra(){
		Map<Integer, VertWrapper> closed = new HashMap<Integer, VertWrapper>();
		
		PriorityQueue<VertWrapper> open = new PriorityQueue<VertWrapper>(10, new Comparator<VertWrapper>(){
			@Override
			public int compare(DijkstraImpl<V, E>.VertWrapper arg0,
					DijkstraImpl<V, E>.VertWrapper arg1) {
				return Double.compare(arg0._distance, arg1._distance);
			}
		});
		
		Map<Integer, VertWrapper> vertices = new HashMap<Integer, VertWrapper>();
		
		//Wrap all vertices
		for(int vID : _graph.getVertices()){
			vertices.put(vID, new VertWrapper(vID));
		}
		
		VertWrapper startV = new VertWrapper(_start);
		startV._distance = 0;
		open.add(startV);
		while(open.size() != 0){
			VertWrapper a = open.poll();
			closed.put(a._vid, a);
			for(int edgeID : _graph.getEdgesOf(a._vid)){
				VertWrapper neighbor = vertices.get(_graph.getTarget(edgeID));
				if(!closed.containsKey(neighbor._vid)){
					
					double alt = a._distance + _weighing.weight(_graph.getAttribute(edgeID));
					
					if(alt < neighbor._distance){
						open.add(neighbor);
						neighbor._distance = alt;
						neighbor._parent = a;
					}
				}
			}
		}
		return closed;
	}
	
	public class VertWrapper{
		int _vid;
		VertWrapper _parent;
		double _distance;
		VertWrapper(int vid){
			_distance = Double.MAX_VALUE;
			_parent = null;
			_vid = vid;
		}
		public double distance(){
			return _distance;
		}
		
	}
}
