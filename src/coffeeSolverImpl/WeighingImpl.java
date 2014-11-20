package coffeeSolverImpl;

import coffeeSolver.Weighing;

public class WeighingImpl<E> implements Weighing<E> {

	@Override
	public double weight(E edge) {
		Edge castEdge = (Edge) edge;
		return castEdge._distance;
	}

}
