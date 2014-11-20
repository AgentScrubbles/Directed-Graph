package coffeeSolverImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import lange.brandon.GraphVisualizer;
import coffeeSolver.CoffeeSolver;
import coffeeSolver.Dijkstra;
import coffeeSolver.Graph;

public class Runner {

	public static void main(String[] args) throws IOException {

		Graph<Vertex,Edge> graph = readFile("src/newAmes.txt");
		

		Graph<Vertex,Edge> directions = createDirectionsGraph();
		

		
		
		CoffeeSolver<Vertex,Edge> cs = new CoffeeSolverImpl<Vertex,Edge>();
		Collection<List<Integer>> validSorts = cs.generateValidSortS(directions);
		List<Integer> ingredientList = validSorts.iterator().next();
		
		List<Integer> fullPath = cs.shortestPath(graph, ingredientList, new WeighingImpl<Edge>());
		
		for(int i = 0; i < fullPath.size() - 1; i++){
			Edge e;
			Set<Integer> outgoingEdges = graph.getEdgesOf(fullPath.get(i));
			for(Integer edge : outgoingEdges){
				if(graph.getTarget(edge) == fullPath.get(i+1)){
					e = graph.getAttribute(edge);
					System.out.println(fullPath.get(i) + ": " + e._name);
					System.out.println("\t" + graph.getData(i).getLatitude() + ", " + graph.getData(i).getLongitude());
				}
			}
			
		}
		
		
		
		
		
		System.out.println();
	}
	
	private static Graph<Vertex, Edge> createDirectionsGraph(){
		Graph<Vertex, Edge> directionsGraph = new GraphImpl<Vertex,Edge>();
		/**
		 * Important Location IDs 
		 * 		Ingredient A: 1055 
		 * 		Ingredient B: 371
		 * 		Ingredient C: 2874 
		 * 		Ingredient D: 2351 
		 * 		Ingredient E: 2956 
		 * 		Ingredient F: 1171 
		 * 		Ing G & Jim: 1208 You: 2893
		 * 
		 * 		A -> C
		 * 		A -> F
		 * 		B -> C
		 * 		B -> D
		 * 		C -> D
		 * 		C -> E
		 * 		F -> C
		 * 		F -> E
		 * 		A -> G
		 * 		B -> G
		 * 		C -> G
		 * 		D -> G
		 * 		E -> G
		 * 		F -> G
		 * 
		 */
		//directionsGraph.addVertex(new Vertex(2893)); //You
		directionsGraph.addVertex(new Vertex(1055)); //A
		directionsGraph.addVertex(new Vertex(371)); //B
		directionsGraph.addVertex(new Vertex(2874)); //C
		directionsGraph.addVertex(new Vertex(2351)); //D
		directionsGraph.addVertex(new Vertex(2956)); //E
		directionsGraph.addVertex(new Vertex(1171)); //F
		directionsGraph.addVertex(new Vertex(1208)); //G
		directionsGraph.addVertex(new Vertex(2893));
		
		
		
		directionsGraph.addEdge(1055, 2874, new Edge(0, 1055, 2874));
		directionsGraph.addEdge(1055, 1171, new Edge(1, 1055, 1171));
		directionsGraph.addEdge(371, 2874, new Edge(2, 371, 2874));
		directionsGraph.addEdge(371, 2351, new Edge(3, 371, 2351));
		directionsGraph.addEdge(2874, 2351, new Edge(4, 2874, 2351));
		directionsGraph.addEdge(2874, 2956, new Edge(5, 2874, 2956));
		directionsGraph.addEdge(1171, 2874, new Edge(6, 1171, 2874));
		directionsGraph.addEdge(1171, 2956, new Edge(7, 1171, 2956));
		directionsGraph.addEdge(1055, 1208, new Edge(8, 1055, 1208));
		directionsGraph.addEdge(371, 1208, new Edge(9, 371, 1208));
		directionsGraph.addEdge(2874, 1208, new Edge(10, 2874, 1208));
		directionsGraph.addEdge(2351, 1208, new Edge(11, 2351, 1208));
		directionsGraph.addEdge(2956, 1208, new Edge(12, 2956, 1208));
		directionsGraph.addEdge(1171, 1208, new Edge(13, 1171, 1208));
		directionsGraph.addEdge(2893, 1055, new Edge(14, 2893, 1055));
		directionsGraph.addEdge(2893, 371, new Edge(15, 2893, 371));
		directionsGraph.addEdge(2893, 2874, new Edge(16, 2893, 2874));
		directionsGraph.addEdge(2893, 2351, new Edge(17, 2893, 2351));
		directionsGraph.addEdge(2893, 2956, new Edge(18, 2893, 2956));
		directionsGraph.addEdge(2893, 1171, new Edge(19, 2893, 1171));
		
		// Changed to src tgt, src is dependent on tgt
		
		
		return directionsGraph;
		
		
	}
	
	
	@SuppressWarnings("null")
	private static Graph<Vertex,Edge> readFile(String fileName) throws IOException{
		BufferedReader reader = null;

		Graph<Vertex, Edge> graph = new GraphImpl<Vertex, Edge>();

		try {
			reader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			reader.close();
			return null;
		}
		String line = null;
		try {

			// Vertice line
			line = reader.readLine();
			int vertCount = Integer.parseInt(line.substring(
					line.indexOf(':') + 1).trim());
			for (int i = 0; i < vertCount; i++) {
				String[] vertArr = reader.readLine().split(",");
				// id, lat, lon
				int id = Integer.parseInt(vertArr[0]);
				double lat = Double.parseDouble(vertArr[1]);
				double lon = Double.parseDouble(vertArr[2]);

				graph.addVertex(new Vertex(id, lat, lon));

			}
			// Edges
			line = reader.readLine();
			int edgeCount = Integer.parseInt(line.substring(
					line.indexOf(':') + 1).trim());
			for (int i = 0; i < edgeCount; i++) {
				// i will act as the unique ID.
				String[] edgeArr = reader.readLine().split(",");
				Edge edge = null;
				Edge reversedEdge = null;
				int source = -1;
				int target = -1;
				double dist = 0.0;
				String name = "";
				if (edgeArr.length == 3) {
					// src, trg, dist
					source = Integer.parseInt(edgeArr[0]);
					target = Integer.parseInt(edgeArr[1]);
					dist = Double.parseDouble(edgeArr[2]);
					edge = new Edge(i, source, target, dist);
					reversedEdge = new Edge(-i, target, source, dist);
				} else if (edgeArr.length == 4) {
					source = Integer.parseInt(edgeArr[0]);
					target = Integer.parseInt(edgeArr[1]);
					dist = Double.parseDouble(edgeArr[2]);
					name = edgeArr[3];
					edge = new Edge(i, source, target, dist, name);
					reversedEdge = new Edge(-i, target, source, dist, name);
				} else {
					throw new Exception("Unknown input: " + edgeArr[0]);
				}
				graph.addEdge(source, target, edge);
				graph.addEdge(target, source, reversedEdge);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return graph;
	}

}
