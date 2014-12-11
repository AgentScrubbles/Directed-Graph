package coffeeSolverImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import coffeeSolver.Graph;

public class GraphImpl<V, E> implements Graph<V, E>{

	private Map<Integer, Vert> adjList;
	private Map<Integer, Edge> edjList;
	
	/**
	 * Internal class for edges.  This will store the generic E variable, it's source
	 * and target.  hashCode will return the E hashcode function.
	 * @author Robert
	 *
	 */
	class Edge{
		private E _e;
		private int _src;
		private int _trg;
		public Edge(E e, int src, int target){
			_e = e;
			_src = src;
			_trg = target;
		}
		@Override
		public int hashCode(){
			return _e.hashCode();
		}
	}
	
	/**
	 * Internal class for vertices. d Contains a hashmap of all connected edges.
	 * From there connected vertices can be found.  hashcode will return V's hashcode.
	 * @author Robert
	 *
	 */
	class Vert{
		private V _v;
		private Map<Integer, Edge> _edges;
		public Vert(V v){
			_v = v;
			_edges = new HashMap<Integer, Edge>();
		}
		public void addEdge(Edge e){
			_edges.put(e._e.hashCode(), e);
		}
		@Override
		public int hashCode(){
			return _v.hashCode();
		}
	}
	
	public GraphImpl(){
		adjList = new HashMap<Integer, Vert>();
		edjList = new HashMap<Integer, Edge>();
	}
	
	@Override
	public int addVertex(V v) {
		adjList.put(v.hashCode(), new Vert(v));
		return v.hashCode();
	}

	@Override
	public int addEdge(int srcID, int targetID, E attr){
		Edge edge = new Edge(attr, srcID, targetID);
		Vert src = adjList.get(srcID);
		src.addEdge(edge);
		edjList.put(attr.hashCode(), edge);
		return attr.hashCode();
	}


	@Override
	public Set<Integer> getVertices() {
		return adjList.keySet();
	}

	@Override
	public Set<Integer> getEdges() {
		return edjList.keySet(); //All edges are located here.
	}

	@Override
	public E getAttribute(int id) throws IllegalArgumentException {
		return edjList.get(id)._e; //Locates the edge by ID, returns the E.
	}

	@Override
	public V getData(int id) throws IllegalArgumentException {
		return adjList.get(id)._v; //Finds the Vert from the ID, returns V.
	}

	@Override
	public int getSource(int id) throws IllegalArgumentException {
		return edjList.get(id)._src; //REturns the source of the edge
	}

	@Override
	public int getTarget(int id) throws IllegalArgumentException {
		return edjList.get(id)._trg; //REturns the target of the edge
	}

	@Override
	public Set<Integer> getEdgesOf(int id) throws IllegalArgumentException {
		Vert vert = adjList.get(id);
		return vert._edges.keySet();
	}
}
