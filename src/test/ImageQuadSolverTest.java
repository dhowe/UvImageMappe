package test;

import java.util.Arrays;

import org.junit.*;

import uvm.*;

public class ImageQuadSolverTest {

  @Test
  public void testComputeCost() {
  	
  	// TODO: add simple test data and expected result below (2)

		UvImage[] workers = null;
		Quad[] jobs = null;
		
		double expectedCost = .56788;
  	int imageIndex = 0, quadIndex = 0;
  	
		double result = new ImageQuadSolver(workers, jobs).cost(imageIndex, quadIndex);
    Assert.assertTrue(expectedCost == result);
  }
  
  @Test
  public void testExecute1() {
  	
  	// TODO: add simple test data and expected result below (3)
  		
		UvImage[] workers = null;
		Quad[] jobs = null;
		
		// the min cost matching of workers to jobs, 
		// with -1 indicating an unassigned worker/image
		int[] result = new ImageQuadSolver(workers, jobs).execute();
		
		for (int i = 0; i < result.length; i++) {
			System.out.println(i+") Quad@"+jobs[i].id+" :: Image#"+workers[i].imageName);
		}
		
    Assert.assertTrue(Arrays.equals(new int[] { /* Expected Result Here */ }, result));
  }
  
}