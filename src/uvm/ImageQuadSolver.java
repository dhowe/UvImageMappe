package uvm;

import alg.HungarianAlgorithm;
import processing.core.PApplet;

import java.util.*;

public class ImageQuadSolver  {
	
	protected List<UvImage> workers;
	protected List<Quad> jobs;
	protected int N;
	protected int workersUnit;
	protected float jobsUnit;

	public ImageQuadSolver(List<UvImage> images, List<Quad> quads) {
		
		this.jobs = quads;
		this.workers = images;
		this.N = Math.max(workers.size(), jobs.size());
		this.workersUnit = getMaxImageLength(workers);
		this.jobsUnit = getMaxQuadLength(jobs);
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
				
				if (j < jobs.size() && i < workers.size())
					matrix[i][j] = jobs.get(i).fitness(workers.get(i), workersUnit, jobsUnit);
			}
		}
		
		return matrix;
	}
	
	int getMaxImageLength(List<UvImage> ads) {

		int maxImageL = 0;

		for (int i = 0; i < ads.size(); i++) {
			if (ads.get(i).width > maxImageL || ads.get(i).height > maxImageL) {
				maxImageL = ads.get(i).width > ads.get(i).height ? ads.get(i).width : ads.get(i).height;
			}
		}
 
//		System.out.println("\nMax Image Lenth:"+ maxImageL);
	
		return maxImageL;
	}
	
	float getMaxQuadLength(List<Quad> quads) {

		float maxQuadLength = 0;

		for (int i = 0; i < quads.size(); i++) {

			float w = quads.get(i).bounds[2];
			float h = quads.get(i).bounds[3];

			if (w > maxQuadLength || h > maxQuadLength) {
				maxQuadLength = w > h ? w : h;
			}

		}
		
//		System.out.println("\nMax Quad Lenth:" + maxQuadLength);

		return maxQuadLength;
	}
	

}
