package uvm;

import alg.HungarianAlgorithm;

public class ImageQuadSolver {
	
	protected UvImage[] workers;
	protected Quad[] jobs;

	public ImageQuadSolver(UvImage[] images, Quad[] quads) {
		
		this.workers = images;
		this.jobs = quads;
	}

	/**
	 * Returns the min cost matching of workers/images to jobs/quads
	 * with -1 indicating an unassigned worker/image  
	 */
	public int[] execute() {
		
		return new HungarianAlgorithm(computeCostMatrix()).execute();
	}
	
	/**
	 * Returns the cost of a single worker/job pair  
	 */
	public double cost(int imageIndex, int quadIndex) {
		
		return computeCostMatrix()[imageIndex][quadIndex]; // may be reversed here
	}

	/**
	 * Returns the matrix of costs for each worker/image pair  
	 */
	public double[][] computeCostMatrix() {

		int N = Math.max(workers.length, jobs.length);
		double[][] costMatrix = new double[N][N];
		for (int i = 0; i < costMatrix.length; i++) {
			for (int j = 0; j < costMatrix.length; j++) {
				costMatrix[j][i] = jobs[i].fitness(workers[j]); // j,i may be reversed here
			}
		}
		
		return costMatrix;
	}

}
