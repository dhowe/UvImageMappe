package uvm;

import alg.HungarianAlgorithm;

public class ImageQuadSolver {
	
	protected UvImage[] workers;
	protected Quad[] jobs;
	protected int N;

	public ImageQuadSolver(UvImage[] images, Quad[] quads) {
		
		this.jobs = quads;
		this.workers = images;
		this.N = Math.max(workers.length, jobs.length);
	}

	public int getN() {
		
		return N;
	}

	/**
	 * Returns the min cost matching of workers/images to jobs/quads
	 * with -1 indicating an unassigned worker/image  
	 */
	public int[] execute() {
		
		return new HungarianAlgorithm(computeCostMatrix()).execute();
	}
	
	/**
	 * Computes the cost of a single worker/job pair
	 * @return the cost of assigning the i'th worker (image) to the j'th job (quad) at position (i, j).   
	 */
	public double cost(int imageIndexI, int quadIndexJ) {
		
		return computeCostMatrix()[imageIndexI][quadIndexJ];
	}

	/**
	 * Computes the matrix of costs for each worker/image pair
	 * @return the cost matrix which gives the costs for assigning the i'th worker (image) 
	 * 				 to the j'th job (quad) at position (i, j).   
	 */
	public double[][] computeCostMatrix() {

		double[][] matrix = new double[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				matrix[i][j] = -1;
				if (j < jobs.length && i < workers.length)
					matrix[i][j] = jobs[j].fitness(workers[i]);
			}
		}
		
		return matrix;
	}

}
