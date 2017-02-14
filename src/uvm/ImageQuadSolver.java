package uvm;

import alg.HungarianAlgorithm;

import java.util.*;

public class ImageQuadSolver {

	protected UvImage[] workers;
	protected Quad[] jobs;

	public ImageQuadSolver(List<UvImage> images, List<Quad> quads) {

		this(images.toArray(new UvImage[0]), quads.toArray(new Quad[0]));
	}

	public ImageQuadSolver(UvImage[] images, Quad[] quads) {

		this.jobs = quads;
		this.workers = images;
	}

	/**
	 * Returns the min cost matching of workers/images to jobs/quads with -1
	 * indicating an unassigned worker/image
	 */
	public int[] execute() {

		return new HungarianAlgorithm(computeCostMatrix()).execute();
	}

	/**
	 * Computes the min cost matching of workers/images to jobs/quads and then
	 * assigns the paired UvImage to each Quad
	 */
	public void assign() {

		int[] pairings = new HungarianAlgorithm(computeCostMatrix()).execute();
		for (int i = 0; i < pairings.length; i++) {
			if (pairings[i] != -1) {
				jobs[pairings[i]].assignImage(workers[i]);
			}
			// System.out.println(i+") Quad@"+jobs.get(result[i]).id+" :: Image#"+workers.get(i).imageName);
		}
	}

	/**
	 * Computes the cost of a single worker/job pair
	 * 
	 * @return the cost of assigning the i'th worker (image) to the j'th job
	 *         (quad) at position (i, j).
	 */
	public double cost(int imageIndexI, int quadIndexJ) {

		return computeCostMatrix()[imageIndexI][quadIndexJ];
	}

	/**
	 * Computes the matrix of costs for each worker/image pair
	 * 
	 * @return the cost matrix which gives the costs for assigning the i'th worker
	 *         (image) to the j'th job (quad) at position (i, j).
	 */
	public double[][] computeCostMatrix() {

		float jobsUnit = getMaxQuadLength(jobs);
		float workersUnit = getMaxImageLength(workers);

		double[][] matrix = new double[workers.length][jobs.length];
		for (int i = 0; i < workers.length; i++) {
			for (int j = 0; j < jobs.length; j++) {
				matrix[i][j] = jobs[j].fitness(workers[i], workersUnit, jobsUnit);
			}
		}
		return matrix;
	}

	float getMaxImageLength(UvImage[] ads) {

		float maxImageL = 0;

		for (int i = 0; i < ads.length; i++) {

			float w = ads[i].width;
			float h = ads[i].height;

			if (w > maxImageL || h > maxImageL) {
				maxImageL = w > h ? w : h;
			}
		}

		return maxImageL;
	}

	float getMaxQuadLength(Quad[] quads) {

		float maxQuadLength = 0;

		for (int i = 0; i < quads.length; i++) {

			float w = quads[i].bounds[2];
			float h = quads[i].bounds[3];

			if (w > maxQuadLength || h > maxQuadLength) {
				maxQuadLength = w > h ? w : h;
			}
		}

		return maxQuadLength;
	}

}
