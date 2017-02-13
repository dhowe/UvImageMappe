package test;


/*
 * simple test solver using multiplication as fitness function 
 */
public class MultSolver {
	
	protected int[] workers, jobs;
	protected int N;

	public MultSolver(int[] images, int[] quads) {
		
		this.jobs = quads;
		this.workers = images;
		this.N = Math.max(workers.length, jobs.length);
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
	 * @return the cost matrix which gives the cost of assigning the i'th worker to the j'th job at position (i, j).   
	 */
	public double[][] computeCostMatrix() {

		double[][] matrix = new double[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				matrix[i][j] = -1;
				if (j < jobs.length && i < workers.length)
					matrix[i][j] = fakeFitness(jobs[j], workers[i]); // test fitness function
			}
		}
		
		return matrix;
	}
	
	private double fakeFitness(int job, int worker) {

		return job * worker;
	}

}
